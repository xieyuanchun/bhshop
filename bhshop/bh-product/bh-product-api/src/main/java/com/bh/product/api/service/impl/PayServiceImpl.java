package com.bh.product.api.service.impl;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.enums.UnionPayInterfaceEnum;
import com.bh.product.api.service.PayService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.pay.HttpService;
import com.product.vo.UnionPayRefundVO;

@Service
public class PayServiceImpl implements PayService {
	@Autowired
	private OrderMapper mapper; //订单
	@Autowired
	private OrderSkuMapper SkuMapper; //订单商品
	@Autowired
	private GoodsMapper goodsMapper;	//商品
	@Autowired
	private OrderRefundDocMapper refundMapper; //退款单
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; // 商品属性
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberMapper memberMapper;
	
	@Transactional
	@Override
	public boolean refund(String orderNo) {
		try{
			OrderRefundDoc doc = new OrderRefundDoc();
			Order order = mapper.getOrderByOrderNo(orderNo);
			List<OrderSku> skuList = SkuMapper.getByOrderId(order.getId());
			OrderSku orderSku =skuList.get(0); 
			Goods goods = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
			
			
			MemberShop memberShop =memberShopMapper.selectByPrimaryKey(order.getShopId());
			UnionPayRefundVO vo = new UnionPayRefundVO();
			vo.setRefundAmount(order.getOrderPrice()+"");
			vo.setMerOrderId(orderNo);
			//vo.setMd5Key("fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR");
			vo.setMd5Key(memberShop.getMd5Key());
			String ret = HttpService.doPostJson(UnionPayInterfaceEnum.WXJSREFUND.getMethod(),vo);
			if("SUCCESS".equals(ret.replaceAll("\"", ""))){
				doc.setStatus(Contants.REFUND_SUCCESS);//2-成功
				goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
				goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
			}else{
				doc.setStatus(Contants.REFUND_FAIL);//1-失败
				
			}
			goodsMapper.updateByPrimaryKeySelective(goods);
			goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
			
			Member member=memberMapper.selectByPrimaryKey(order.getmId());
			doc.setmName(member.getUsername());
			doc.setmPhone(member.getPhone());
			doc.setOrderSkuId(orderSku.getId());
			doc.setShopId(memberShop.getmId());
			doc.setReason("活动失败");
			
			doc.setAddtime(new Date());
			doc.setDisposeTime(new Date());
			doc.setAdminUser("admin");
			doc.setAmount(order.getOrderPrice());
			doc.setGoodsId(goods.getId());
			doc.setGoodsName(goods.getName());
			doc.setNote("活动失败");
			doc.setOrderAmount(order.getOrderPrice());
			doc.setOrderId(order.getId());
			doc.setOrderShopId(orderSku.getOrderShopId());
			doc.setOrderShopNo(orderSku.getShopOrderNo());
			doc.setSkuId(goodsSku.getId());
			doc.setReason("活动失败退款");
			doc.setmId(order.getmId());
			refundMapper.insertSelective(doc);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	
		return true;
	}

}
