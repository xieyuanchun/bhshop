package com.bh.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberNoticeMapper;
import com.bh.admin.mapper.user.MemberSendMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.MSendMsg;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.MemberNotice;
import com.bh.admin.pojo.user.MemberSend;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.service.MBusOrderService;
import com.bh.config.Contants;

import com.bh.utils.JsonUtils;

@Service
public class MBusOrderServiceImpl implements MBusOrderService{
	
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private MemberSendMapper memberSendMapper;
	@Autowired
	private MemberNoticeMapper memberNoticeMapper;
	
	
	public MSendMsg selectMSendMsg(MSendMsg msg1) throws Exception{
		MSendMsg msg = new MSendMsg();
		OrderSku orderSku = new OrderSku();
		orderSku.setOrderShopId(msg1.getOrderShopId());
		List<OrderSku> list = orderSkuMapper.selectOrderSkuByParams(orderSku);
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(msg1.getOrderShopId());
		if (orderShop != null) {
			Order order = orderMapper.selectByPrimaryKey(orderShop.getOrderId());
			if (order !=null) {
				MemberUserAddress memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
				if (memberUserAddress !=null) {
					StringBuffer sb = new StringBuffer();
					sb.append(memberUserAddress.getProvname() + " ")
					.append(memberUserAddress.getCityname() + " ")
					.append(memberUserAddress.getAreaname() + " ")
					.append(memberUserAddress.getFourname() + " ")
					.append(memberUserAddress.getAddress());
					msg.setReceiverAddress(sb.toString());
					
					msg.setReceiverName(memberUserAddress.getFullName());
					msg.setReceiverPhone(memberUserAddress.getTel());
				}
				
			}
		}
		if (list.size() > 0) {
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(list.get(0).getSkuId());
			Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
			
			 String value = goodsSku.getValue();
			 msg.setValueObj(JsonUtils.stringToObject(value));
			 msg.setRealPrice(list.get(0).getSkuSellPriceReal() / 100);
			 msg.setNum(list.get(0).getSkuNum());
			 org.json.JSONObject jsonObj = new org.json.JSONObject(value);
		     org.json.JSONArray personList = jsonObj.getJSONArray("url");
			 msg.setGoodsImage((String) personList.get(0));
			 msg.setGoodsName(goods.getName());
			 msg.setGoodsId(goods.getId());
			 msg.setShopId(goods.getShopId()+"");
			 msg.setOrderShopId(orderShop.getId());
			 
		}
		return msg;
	}
	
	
	
	/**
	 * 商家整单抛单操作
	 */
	@Override
	public OrderShop castSheet(String id, String deliveryPrice) throws Exception {
		Integer price = (int)(Double.parseDouble(deliveryPrice)*100);
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		Order order = orderMapper.selectByPrimaryKey(orderShop.getOrderId());

		orderShop.setdState(Contants.CATE_ONE);//1抛单
		orderShop.setDeliveryPrice(price); //设置物流价
		orderShop.setCastTime(new Date()); //抛单时间
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		
		List<OrderSku> list = orderSkuMapper.getByOrderShopId(orderShop.getId());
		for(OrderSku sku : list){
			sku.setIsSend(true);//已发货
			orderSkuMapper.updateByPrimaryKeySelective(sku);
		}
		
		List<OrderShop> orderShopList = orderShopMapper.getByOrderIdAndStatus(order.getId()); //查询同一订单下的商家订单是否全部发货
		if(orderShopList.size()>0){
			order.setDeliveryStatus(Contants.DELIVERY_PART); //2部分发货
		}else{
			order.setDeliveryStatus(Contants.DELIVERY_ALL); //1已发货
		}
		orderMapper.updateByPrimaryKeySelective(order);
		if(row ==0){
			orderShop = null;
		}else if (row == 1) {//商家抛单cheng
			MemberSend memberSend = new MemberSend();
			List<MemberSend> sends = memberSendMapper.selectMemberSendByParams(memberSend);
			if (sends.size() > 0) {
				List<MemberNotice> notice1 = new ArrayList<>();
				
				for (int i = 0; i < sends.size(); i++) {
					MemberNotice notice = new MemberNotice();
					java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					
					Date date = new Date();
				    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date));
				    String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
		
					String string ="您有新的信息,请注意查收!";
					notice.setmId(sends.get(i).getmId());
					notice.setMessage(string);
					notice.setMsgType(1);
					notice.setIsRead(0);
					notice.setAddTime(new Date());
					notice.setUpdateTime(new Date());
					notice.setLastExcuTime(sdf.parse(d));
					notice1.add(notice);
				}
			
				memberNoticeMapper.insertSelectiveByBatch(notice1);
				row =1;
			}
		}
		return orderShop;
	}
	
}
