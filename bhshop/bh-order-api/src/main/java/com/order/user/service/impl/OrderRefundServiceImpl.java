package com.order.user.service.impl;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.aliyun.oss.event.ProgressPublisher;
import com.bh.config.Contants;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderExpressTypeMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderPaymentMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderRefundDocStepMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderRefundDocStep;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSimple;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderSkuReturn;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.order.enums.RefundReasonEnum;
import com.order.user.service.OrderRefundService;

@Service
public class OrderRefundServiceImpl implements OrderRefundService {
	
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private MemberShopMapper memberShopMember;

	@Autowired
	private GoodsCartMapper goodsCartMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderSkuMapper orderSkuMapper;

	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;

	@Autowired
	private MemberShopMapper memberShopMapper;

	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	@Autowired
	private OrderExpressTypeMapper orderExpressTypeMapper;

	@Autowired
	private OrderPaymentMapper orderPaymentMapper;

	@Autowired
	private OrderShopMapper orderShopMapper;

	@Autowired
	private CouponMapper couponMapper;
	
	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private OrderRefundDocStepMapper orderRefundDocStepMapper;
	
	
	public BhResult showRefundGoods(OrderSku o) throws Exception {
		BhResult bhResult = null;

		OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(o.getId());

		Set<String> set = new HashSet<>();

		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderSku.getShopId());
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());
		Order order = orderMapper.selectByPrimaryKey(orderSku.getOrderId());
		
		Integer status = orderShop.getStatus();
		MemberUserAddress address = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());

		orderSku.setShopName(memberShop.getShopName());// 获得商家名称
		orderSku.setShopOrderNo(orderShop.getShopOrderNo());
		orderSku.setAddtime(order.getAddtime());
		switch (status) {
		case 1:
			orderSku.setStatus(1);
			orderSku.setMystatus(Contants.orderShopStatu1);
			break;
		case 2:
			orderSku.setStatus(2);
			orderSku.setMystatus(Contants.orderShopStatu2);
			break;
		case 5:
			orderSku.setStatus(5);
			orderSku.setMystatus(Contants.orderShopStatu5);
			break;
		case 7:
			orderSku.setStatus(7);
			orderSku.setMystatus(Contants.orderShopStatu7);
			break;	
		default:
			orderSku.setMystatus(Contants.refund0);
		}

		set.add(String.valueOf(orderSku.getGoodsId()));

		String value = orderSku.getSkuValue();
		Object object = JsonUtils.stringToObject(value);
		orderSku.setValueObj(object);
		int p= orderSku.getSkuNum()*orderSku.getSkuSellPriceReal()-orderSku.getSavePrice()-orderSku.getCouponPrice();
		double realSellPrice = (double) p / 100;
		orderSku.setRealSellPrice(realSellPrice);
		double markPrice = (double) orderSku.getSkuMarketPrice() / 100;
		orderSku.setRealMarketPrice(markPrice);
		//退款金额取totalPrice字段
		if (realSellPrice<=0) {
			realSellPrice=0;
		}
		orderSku.setTotalPrice(realSellPrice);
		
		
		//fanjh 2018.8.30 add
		OrderRefundDoc orderRefundDoc=orderRefundDocMapper.selectByOrderSkuId1(o.getId());
		if(orderRefundDoc!=null) {
			orderSku.setNote(orderRefundDoc.getNote());
			orderSku.setAfterSaleReasons(orderRefundDoc.getAfterSaleReasons());
			orderSku.setReason(orderRefundDoc.getReason());
		}
		
		

		OrderSkuReturn return1 = new OrderSkuReturn();
		return1.setOrderSku(orderSku);
		return1.setMemberUserAddress(address);
		bhResult = new BhResult(200, "请求成功", return1);

		return bhResult;
	}

	public int refundgoods(OrderRefundDoc refundDoc) throws Exception {
		int row = 0;

		OrderSku orderSku = new OrderSku();
		GoodsSku goodsSku = new GoodsSku();
		Order order = new Order();
		orderSku = orderSkuMapper.selectByPrimaryKey(refundDoc.getOrderSkuId());
		goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
		order = orderMapper.selectByPrimaryKey(orderSku.getOrderId());
		int p=orderSku.getSkuNum()*orderSku.getSkuSellPriceReal()-orderSku.getSavePrice()-orderSku.getCouponPrice();
		if (p<=0) {
			p=0;
		}
		OrderShop orderShopWithBh = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());//xieyc
		OrderRefundDoc refund = new OrderRefundDoc();
		refund.setAddtime(new Date());
		refund.setmId(refundDoc.getmId());
		refund.setOrderId(orderSku.getOrderId());
		refund.setGoodsId(goodsSku.getGoodsId());
		refund.setSkuId(orderSku.getSkuId());
		refund.setAmount(p);
		refund.setOrderSkuId(refundDoc.getOrderSkuId());
		refund.setAfterSaleReasons(refundDoc.getAfterSaleReasons());
		refund.setSpecifications(refundDoc.getSpecifications());
		refund.setVoucherImage(refundDoc.getVoucherImage());
		
		if (StringUtils.isNotBlank(refundDoc.getNote())) {// 退款描述
			refund.setNote(refundDoc.getNote());
		}
		if (StringUtils.isNotEmpty(refundDoc.getReason())) {// 退款原因
			refund.setReason(refundDoc.getReason());
		}
		if (StringUtils.isNotEmpty(refundDoc.getmName())) {
			refund.setmName(refundDoc.getmName());
		}
		if (StringUtils.isNotEmpty(refundDoc.getmPhone())) {
			refund.setmPhone(refundDoc.getmPhone());
		}
		if (StringUtils.isNotEmpty(refundDoc.getImg())) {
			refund.setImg(refundDoc.getImg());
		}
		refund.setOrderAmount(order.getOrderPrice());
		refund.setStatus(0);// 退款状态，0:申请退款 1:退款失败 2:退款成功，3：已拒绝,5:申请退货中,6:申请退货失败'
		refund.setShopId(orderSku.getShopId());
		refund.setOrderShopId(orderSku.getOrderShopId());
		refund.setRefundType(refundDoc.getRefundType());
	
		OrderRefundDoc doc1 = new OrderRefundDoc();
		doc1.setOrderSkuId(refundDoc.getOrderSkuId());
		OrderRefundDoc doc = orderRefundDocMapper.selectByOrderSkuId(doc1);
		if (doc != null) {
			refund.setId(doc.getId());
			row = orderRefundDocMapper.updateByPrimaryKeySelective(refund);
			
			//设置审核失效时间
			Calendar ca = Calendar.getInstance();
			ca.setTime(refund.getAddtime());
			ca.add(Calendar.MINUTE, Contants.fiveDayLen);
			refund.setRefundValidTime(ca.getTime());
			orderRefundDocMapper.updateByPrimaryKeySelective(refund);
		}	else{
			row = orderRefundDocMapper.insertSelective(refund);
			
			//设置审核失效时间
			Calendar ca = Calendar.getInstance();
			ca.setTime(refund.getAddtime());
			ca.add(Calendar.MINUTE, Contants.fiveDayLen);
			refund.setRefundValidTime(ca.getTime());
			orderRefundDocMapper.updateByPrimaryKeySelective(refund);
			
			
			try {
				if (refund.getId() !=null) {
					insertOrderRefundStep(refund.getId());
				}
				OrderShop orderShop = new OrderShop();// 将orderShop的is_refund设置1
				orderShop.setId(orderSku.getOrderShopId());
				orderShop.setIsRefund(1);
				orderShopMapper.updateOrderShopByParams(orderShop);
				OrderSku orderSku2 = new OrderSku();
				orderSku2.setId(orderSku.getId());
				orderSku2.setRefund(1);
				orderSku2.setTeamPrice(orderShopWithBh.getStatus());
				orderSkuMapper.updateByPrimaryKeySelective(orderSku2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		int count = countOrderShopId(refund.getOrderSkuId());
		if (count ==0) {
			OrderShop orderShop1 = new OrderShop();// 将orderShop的is_refund设置1
			orderShop1.setId(orderSku.getOrderShopId());
			orderShop1.setIsRefund(2);
			orderShopMapper.updateOrderShopByParams(orderShop1);
		}
		return row;
	}
	
	/**
	 * @Description: 退款金额的计算(商品价格-优惠卷抵扣金额)（作废）
	 * @author xieyc
	 * @date 2018年6月11日 上午10:37:21 
	 */
	public int refundMony(OrderShop orderShop,OrderSku orderSku){
		/***********计算该退款商品的抵扣金额（按比例退款）  xieyc*****************/
		int couponAfterPrice=0;//优惠劵抵扣金额
		//Goods refundGoods= goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());//退款商品的信息
		int refundGoodsPrcie=orderSku.getSkuNum() * orderSku.getSkuSellPriceReal();//退款商品的价格
		if (orderShop.getCouponId() > 0) {
			CouponLog couponLog = couponLogMapper.selectByPrimaryKey(orderShop.getCouponId());
			Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
			int  couponDeductionAllPrice= orderShop.getCouponPrice();//所有商品抵扣的金额
			List<OrderSku> listOrderSku=orderSkuMapper.getSkuListByOrderShopId(orderShop.getId());
			int allGoodsPrice=0;
			
			if(coupon.getCouponType()==1){//普通卷（满减劵）
				if(coupon.getCatId()==0){//该商家通用劵
					for (OrderSku sku : listOrderSku) {//该商家订单所有商品的价格
						int goodsPrice=sku.getSkuNum() * sku.getSkuSellPriceReal();
						allGoodsPrice+=goodsPrice;
					}
				}else{//该商家某分类的劵
					if(orderSku.getOrderCatId().equals(coupon.getCatId())){//判断该商品是否参与了 该优惠劵的使用
						for (OrderSku sku : listOrderSku) {
							if(orderSku.getOrderCatId().equals(coupon.getCatId())){//该商家订单这个分类商品的价格
								int goodsPrice=sku .getSkuNum() * sku .getSkuSellPriceReal();
								allGoodsPrice+=goodsPrice;
							}
						}
					}
				}
				if(allGoodsPrice>0){//计算退款比列
					double deductionPrice=((double)refundGoodsPrcie/allGoodsPrice)*couponDeductionAllPrice;//该商品的价格/参与抵扣的所有商品的金额 *总抵扣金额 
					DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数  					
					couponAfterPrice=refundGoodsPrcie-Integer.parseInt(df.format(deductionPrice));//该商品的价格/参与抵扣的所有商品的金额 *总抵扣金额 
					return couponAfterPrice;
				}
			}
			int realPrice=0;
			if(coupon.getCouponType()==3){//红包劵
				for (OrderSku sku : listOrderSku) {//该商家订单所有商品的价格
					int goodsPrice=sku.getSkuNum() * sku.getSkuSellPriceReal();
					allGoodsPrice+=goodsPrice;
				}
				int gDeliveryPrice=orderShop.getgDeliveryPrice();//邮费
				
				int cha=couponDeductionAllPrice-gDeliveryPrice;//邮费与商品价格-优惠劵抵扣金额
				if(cha>0){//抵扣金额>邮费
					realPrice=allGoodsPrice-cha;//实际支付价格
					if(realPrice==0){
						return 0;//返回（为红包劵的时候实际支付为0 的时候）
					}
				}else{
					realPrice=allGoodsPrice;//实际支付价格（这种实际支付价格不为0 ）
				}
				
			}
			if(realPrice>0 && allGoodsPrice>0){//计算退款比列(实际支付不为0的时候)
				double deductionPrice=((double)refundGoodsPrcie/allGoodsPrice)*realPrice;//该商品的价格/参与抵扣的所有商品的金额 *总抵扣金额 
				DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数  					
				couponAfterPrice=Integer.parseInt(df.format(deductionPrice));//该商品的价格/参与抵扣的所有商品的金额 *总抵扣金额 
				return couponAfterPrice;//返回
			}
		}
		return refundGoodsPrcie;//返回
	}
	
	
	public OrderShop selectOrderShopStatus(Integer id) throws Exception{
		OrderShop orderShop = new OrderShop();
		OrderSku orderSku = new OrderSku();
		orderSku = orderSkuMapper.selectByPrimaryKey(id);
		orderShop = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());
		return orderShop;
	}
	
	public OrderRefundDoc selectOrderRefundByOrderSkuId(Integer id) throws Exception{
		OrderRefundDoc r = new OrderRefundDoc();
		OrderRefundDoc r1 = new OrderRefundDoc();
		r1.setOrderSkuId(id);
		r = orderRefundDocMapper.selectByOrderSkuId(r1);
		return r;
	}

	/********* 2017-11-1星期三 **********/
	// 订单列表（全部订单--通过orderShop查询）
	public PageBean<OrderShop> selectAllOrderShopList(OrderShop orderShop, Integer page, Integer rows)
			throws Exception {
		try {
			List<OrderShop> orderShopList = new ArrayList<>();
			System.out.println("page-->" + page + "  rows--->" + rows);
			PageHelper.startPage(page, rows, true);

			orderShopList = orderShopMapper.selectOrderShopParams(orderShop);// and
																				// m_id=?
																				// group
																				// by
																				// order_id
																				// order
																				// by
																				// order_id
																				// desc

			System.out.println("size:");
			for (int i = 0; i < orderShopList.size(); i++) {
				OrderShop orderShop2 = new OrderShop();
				orderShop2.setmId(orderShop.getmId());
				orderShop2.setOrderId(orderShopList.get(i).getOrderId());
				List<OrderShop> orderShops = orderShopMapper.selectOrderShopByOrderIds(orderShop2);

				for (int m = 0; m < orderShops.size(); m++) {
					double realDeliveryPrice = 0.0;// 总邮费
					int deliveryPrice = 0;
					double allPrice = 0.0;

					Set<String> set = new HashSet<>();

					OrderSku orderSku = new OrderSku();
					orderSku.setOrderId(orderShops.get(m).getOrderId());
					orderSku.setShopId(orderShops.get(m).getShopId());
					List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);// 该商家下面的所有商品的sku

					MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.get(m).getShopId());
					orderShops.get(m).setShopName(memberShop.getShopName());// 获得商家名称
					orderShops.get(m).setOrderSku(orderSkuList);

					// orderShops.get(m).setMystatus(String.valueOf(orderShops.get(m).getStatus()));
					switch (orderShops.get(m).getStatus()) {
					case 1:
						orderShops.get(m).setMystatus(Contants.orderShopStatu1);
						break;
					case 2:
						orderShops.get(m).setMystatus(Contants.orderShopStatu2);
						break;
					case 3:
						orderShops.get(m).setMystatus(Contants.orderShopStatu3);
						break;
					case 4:
						orderShops.get(m).setMystatus(Contants.orderShopStatu4);
						break;
					case 5:
						orderShops.get(m).setMystatus(Contants.orderShopStatu5);
						break;
					case 6:
						orderShops.get(m).setMystatus(Contants.orderShopStatu6);
						break;
					case 7:
						orderShops.get(m).setMystatus(Contants.orderShopStatu7);
						break;
					case 8:
						orderShops.get(m).setMystatus(Contants.orderShopStatu8);
						break;
					default:
						orderShops.get(m).setMystatus(Contants.orderShopStatu1);
					}

					double price = 0.0;
					int goodsNum = 0;
					for (int n = 0; n < orderSkuList.size(); n++) {
						set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));

						String value = orderSkuList.get(n).getSkuValue();
						Object object = JsonUtils.stringToObject(value);
						orderSkuList.get(n).setValueObj(object);
						double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						orderSkuList.get(n).setRealSellPrice(realSellPrice);
						double markPrice = (double) orderSkuList.get(n).getSkuMarketPrice() / 100;
						orderSkuList.get(n).setRealMarketPrice(markPrice);
						Integer num = orderSkuList.get(n).getSkuNum();
						double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						double sellPrice1 = sellPrice * num;
						goodsNum = goodsNum + num;
						price = price + sellPrice1;
					}
					double realgDe = (double) orderShops.get(m).getgDeliveryPrice() / 100;
					double realOrderPrice = (double) orderShops.get(m).getOrderPrice() / 100;
					double totalproce = realOrderPrice - realgDe;
					orderShops.get(m).setRealgDeliveryPrice(realgDe);
					orderShops.get(m).setGoodsNumber(goodsNum);
					orderShops.get(m).setRealOrderPrice(totalproce);
					orderShops.get(m).setAllPrice(realOrderPrice);
					// 根据id获得goods商品的集合
					List<String> myList = new ArrayList<>(set);
					List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(myList);
					for (int n = 0; n < goodsList.size(); n++) {
						int deliveryPrice1 = goodsList.get(n).getDeliveryPrice();
						double dePrice = (double) deliveryPrice1 / 100;
						realDeliveryPrice = realDeliveryPrice + dePrice;
						deliveryPrice = deliveryPrice + deliveryPrice1;
					}
					double allPrice1 = 0.0;
					allPrice1 = realDeliveryPrice + price;
					allPrice = allPrice + allPrice1;

					orderShops.get(m).setDeliveryPrice(deliveryPrice);
					orderShops.get(m).setRealDeliveryPrice(realDeliveryPrice);

				}

				orderShopList.get(i).setOrderShopList(orderShops);
				orderShopList.get(i).setShopOrderNo(null);

				Order order = orderMapper.selectByPrimaryKey(orderShopList.get(i).getOrderId());
				OrderSimple orderSimple = new OrderSimple();
				orderSimple.setId(order.getId());
				orderSimple.setPaymentId(order.getPaymentId());// 支付方式
				orderSimple.setPaymentStatus(order.getPaymentStatus());
				orderSimple.setAddtime(order.getAddtime());
				orderSimple.setOrderPrice(order.getOrderPrice());
				orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
				orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
				double realOrderPrice = (double) order.getOrderPrice() / 100;
				double realGdel = (double) order.getDeliveryPrice() / 100;
				orderSimple.setG_deliveryPrice(realGdel);
				orderSimple.setRealOrderPrice(realOrderPrice);
				/** 设置地址 */
				if (order.getmAddrId() != null) {
					// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
					MemberUserAddress memberUserAddress = new MemberUserAddress();
					memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
					orderSimple.setMemberUserAddress(memberUserAddress);
				} else {
					orderSimple.setMemberUserAddress(null);
				}

				orderShopList.get(i).setOrderSimple(orderSimple);

			}

			PageInfo<OrderShop> info = new PageInfo<>(orderShopList);

			PageBean<OrderShop> pageBean = null;

			pageBean = new PageBean<>(orderShopList);
			pageBean.setTotal(info.getTotal());
			pageBean.setList(info.getList());

			return pageBean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/********* 2017-11-1星期三 **********/
	// 订单列表（全部订单--通过orderShop查询）
	public PageBean<OrderShop> selectAllOrderShopList1(OrderShop orderShop, Integer page, Integer rows)
			throws Exception {
		try {

			System.out.println("page-->" + page + "  rows--->" + rows);
			PageHelper.startPage(page, rows, true);
			List<OrderShop> orderShops = orderShopMapper.selectOrderShopParams(orderShop);

			for (int m = 0; m < orderShops.size(); m++) {
				double realDeliveryPrice = 0.0;// 总邮费
				int deliveryPrice = 0;
				double allPrice = 0.0;

				Set<String> set = new HashSet<>();

				OrderSku orderSku = new OrderSku();
				orderSku.setOrderId(orderShops.get(m).getOrderId());
				orderSku.setShopId(orderShops.get(m).getShopId());
				List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);// 该商家下面的所有商品的sku

				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.get(m).getShopId());
				orderShops.get(m).setShopName(memberShop.getShopName());// 获得商家名称
				orderShops.get(m).setOrderSku(orderSkuList);

				// orderShops.get(m).setMystatus(String.valueOf(orderShops.get(m).getStatus()));
				switch (orderShops.get(m).getStatus()) {
				case 1:
					orderShops.get(m).setMystatus(Contants.orderShopStatu1);
					break;
				case 2:
					orderShops.get(m).setMystatus(Contants.orderShopStatu2);
					break;
				case 3:
					orderShops.get(m).setMystatus(Contants.orderShopStatu3);
					break;
				case 4:
					orderShops.get(m).setMystatus(Contants.orderShopStatu4);
					break;
				case 5:
					orderShops.get(m).setMystatus(Contants.orderShopStatu5);
					break;
				case 6:
					orderShops.get(m).setMystatus(Contants.orderShopStatu6);
					break;
				case 7:
					orderShops.get(m).setMystatus(Contants.orderShopStatu7);
					break;
				case 8:
					orderShops.get(m).setMystatus(Contants.orderShopStatu8);
					break;
				default:
					orderShops.get(m).setMystatus(Contants.orderShopStatu1);
				}

				double price = 0.0;
				int goodsNum = 0;
				for (int n = 0; n < orderSkuList.size(); n++) {
					set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));

					String value = orderSkuList.get(n).getSkuValue();
					Object object = JsonUtils.stringToObject(value);
					orderSkuList.get(n).setValueObj(object);
					double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					orderSkuList.get(n).setRealSellPrice(realSellPrice);
					double markPrice = (double) orderSkuList.get(n).getSkuMarketPrice() / 100;
					orderSkuList.get(n).setRealMarketPrice(markPrice);
					Integer num = orderSkuList.get(n).getSkuNum();
					double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					double sellPrice1 = sellPrice * num;
					goodsNum = goodsNum + num;
					price = price + sellPrice1;
				}
				double realgDe = (double) orderShops.get(m).getgDeliveryPrice() / 100;
				double realOrderPrice = (double) orderShops.get(m).getOrderPrice() / 100;
				double totalproce = realOrderPrice - realgDe;
				orderShops.get(m).setRealgDeliveryPrice(realgDe);
				orderShops.get(m).setGoodsNumber(goodsNum);
				orderShops.get(m).setRealOrderPrice(totalproce);
				orderShops.get(m).setAllPrice(realOrderPrice);
				// 根据id获得goods商品的集合
				List<String> myList = new ArrayList<>(set);
				List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(myList);
				for (int n = 0; n < goodsList.size(); n++) {
					int deliveryPrice1 = goodsList.get(n).getDeliveryPrice();
					double dePrice = (double) deliveryPrice1 / 100;
					realDeliveryPrice = realDeliveryPrice + dePrice;
					deliveryPrice = deliveryPrice + deliveryPrice1;
				}
				double allPrice1 = 0.0;
				allPrice1 = realDeliveryPrice + price;
				allPrice = allPrice + allPrice1;

				orderShops.get(m).setDeliveryPrice(deliveryPrice);
				orderShops.get(m).setRealDeliveryPrice(realDeliveryPrice);

			}

			/*
			 * Order order =
			 * orderMapper.selectByPrimaryKey(orderShops.getOrderId());
			 * OrderSimple orderSimple = new OrderSimple();
			 * orderSimple.setId(order.getId());
			 * orderSimple.setPaymentId(order.getPaymentId());// 支付方式
			 * orderSimple.setPaymentStatus(order.getPaymentStatus());
			 * orderSimple.setAddtime(order.getAddtime());
			 * orderSimple.setOrderPrice(order.getOrderPrice());
			 * orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId())
			 * ); orderSimple.setPaymentStatusName(String.valueOf(order.
			 * getPaymentStatus())); double realOrderPrice = (double)
			 * order.getOrderPrice() / 100; double realGdel = (double)
			 * order.getDeliveryPrice()/100;
			 * orderSimple.setG_deliveryPrice(realGdel);
			 * orderSimple.setRealOrderPrice(realOrderPrice);
			 *//** 设置地址 *//*
							 * if (order.getmAddrId() != null) { //
							 * 如果查询的地址不为空，则设置该地址，如果为空，则设为null MemberUserAddress
							 * memberUserAddress = new MemberUserAddress();
							 * memberUserAddress =
							 * memberUserAddressMapper.selectByPrimaryKey(order.
							 * getmAddrId()); orderSimple.setMemberUserAddress(
							 * memberUserAddress); } else {
							 * orderSimple.setMemberUserAddress(null); }
							 */

			PageInfo<OrderShop> info = new PageInfo<>(orderShops);

			PageBean<OrderShop> pageBean = null;

			pageBean = new PageBean<>(orderShops);
			pageBean.setTotal(info.getTotal());
			pageBean.setList(info.getList());

			return pageBean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/********* 2017-11-1星期三 **********/
	// 订单列表（全部订单--通过orderShop查询）
	public PageBean<OrderSku> selectAllOrderShopList2(OrderSku orderSku1, Integer page, Integer rows) throws Exception {
		try {

			System.out.println("page-->" + page + "  rows--->" + rows);
			PageHelper.startPage(page, rows, true);

			List<OrderSku> orderSkuList = orderSkuMapper.selectOrderSkuBymId(orderSku1);

			Set<String> set = new HashSet<>();

			for (int n = 0; n < orderSkuList.size(); n++) {
				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderSkuList.get(n).getShopId());
				OrderShop orderShop = orderShopMapper.selectByPrimaryKey(orderSkuList.get(n).getOrderShopId());
				Order order = orderMapper.selectByPrimaryKey(orderSkuList.get(n).getOrderId());
				Integer status = orderShop.getStatus();

				orderSkuList.get(n).setShopName(memberShop.getShopName());// 获得商家名称
				orderSkuList.get(n).setShopOrderNo(orderShop.getShopOrderNo());
				orderSkuList.get(n).setAddtime(order.getAddtime());
				switch (status) {
				case 2:
					orderSkuList.get(n).setStatus(2);
					orderSkuList.get(n).setMystatus(Contants.refund0);
					break;
				case 5:
					orderSkuList.get(n).setStatus(5);
					orderSkuList.get(n).setMystatus(Contants.refund1);
					break;
				default:
					orderSkuList.get(n).setMystatus(Contants.refund0);
				}

				set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));

				String value = orderSkuList.get(n).getSkuValue();
				Object object = JsonUtils.stringToObject(value);
				orderSkuList.get(n).setValueObj(object);
				double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
				orderSkuList.get(n).setRealSellPrice(realSellPrice);
				double markPrice = (double) orderSkuList.get(n).getSkuMarketPrice() / 100;
				orderSkuList.get(n).setRealMarketPrice(markPrice);
				Integer num = orderSkuList.get(n).getSkuNum();
				double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
				double totalPrice = sellPrice * num;
				orderSkuList.get(n).setTotalPrice(totalPrice);

			}

			PageInfo<OrderSku> info = new PageInfo<>(orderSkuList);

			PageBean<OrderSku> pageBean = null;

			pageBean = new PageBean<>(orderSkuList);
			pageBean.setTotal(info.getTotal());
			pageBean.setList(info.getList());

			return pageBean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*********** 已退款商品列表的展示 **********/
	public PageBean<OrderRefundDoc> showrefundedlist(OrderRefundDoc doc, Integer page, Integer rows) throws Exception{
		System.out.println("page-->" + page + "  rows--->" + rows);
		PageHelper.startPage(page, rows, true);
		List<OrderRefundDoc> list = new ArrayList<>();
		list = orderRefundDocMapper.selectRefundList(doc);
		for(int i=0;i<list.size();i++){
			if (StringUtils.isNotEmpty(list.get(i).getmName())) {
				list.get(i).setmName(URLDecoder.decode(list.get(i).getmName(),"utf-8"));
			}
			if (StringUtils.isNotEmpty(list.get(i).getmPhone())) {
				list.get(i).setmPhone(URLDecoder.decode(list.get(i).getmPhone(), "utf-8"));
			}
			if (StringUtils.isNotEmpty(list.get(i).getNote())) {
				list.get(i).setNote(URLDecoder.decode(list.get(i).getNote(), "utf-8"));
			}
			
			
			double price = (double)list.get(i).getAmount()/100;
			list.get(i).setRealPrice(price);
			OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(list.get(i).getOrderSkuId());
			Order order = orderMapper.selectByPrimaryKey(list.get(i).getOrderId());
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShop.getShopId());
			double sellPrice = (double)orderSku.getSkuSellPriceReal()/100;
			double markPrice = (double) orderSku.getSkuMarketPrice()/100;
			double realOrderAmount =(double) list.get(i).getOrderAmount()/100;
			double realAmount = (double) list.get(i).getAmount()/100;
			Object obj = JsonUtils.stringToObject(orderSku.getSkuValue());
			Integer num = orderSku.getSkuNum();
			double totalPrice = num * sellPrice;
			orderSku.setTotalPrice(totalPrice);
			
			orderSku.setRealSellPrice(sellPrice);
			orderSku.setRealMarketPrice(markPrice);
			orderSku.setValueObj(obj);
			list.get(i).setOrdersTime(order.getAddtime());//下单时间
			list.get(i).setOrderSku(orderSku);
			//2018-4-18取orderNo
			list.get(i).setOrderShopNo(orderShop.getShopOrderNo());
			list.get(i).setShopName(memberShop.getShopName());
			list.get(i).setRealAmount(realAmount);
			list.get(i).setRealOrderAmount(realOrderAmount);
			switch (list.get(i).getStatus()) {
			case 0:
				list.get(i).setStatusName(Contants.stautName0);
				break;
			case 1:
				list.get(i).setStatusName(Contants.statuName1);
				break;	
			case 2:
				list.get(i).setStatusName(Contants.statuName2);
				break;
			case 3:
				String reason = list.get(i).getRefuseReason();
				if (StringUtils.isEmpty(reason)){
					reason = "哦，店小二已联系买家";
				}else{
					reason = list.get(i).getRefuseReason();
				}
				String statusname =Contants.statuName3 + "退款理由是："+reason;
				list.get(i).setStatusName(statusname);
				break;
			default:
				break;
			}
			
			String reson = list.get(i).getReason();
			if (reson.equals(RefundReasonEnum.REFUND_MENEY.getReason())) {
				list.get(i).setReasonCode(1);
			}else if (reson.equals(RefundReasonEnum.REFUND_GOODS.getReason())) {
				list.get(i).setReasonCode(2);
			}else if (reson.equals(RefundReasonEnum.REFUND_MENEY_GOODS.getReason())) {
				list.get(i).setReasonCode(3);
			}
			
		}
		PageInfo<OrderRefundDoc> info = new PageInfo<>(list);

		PageBean<OrderRefundDoc> pageBean = null;

		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());
		return pageBean;

	}
	
	public GoodsComment selectCommentById(GoodsComment goodsComment) throws Exception{
		GoodsComment comment = new GoodsComment();
		goodsComment.setIsAddEvaluate(0);
		goodsComment.setReid(0);
		comment = goodsCommentMapper.selectByParams(goodsComment);
		return comment;
	}
	
	public Goods selectGoodsById(Integer id) throws Exception{
		Goods goods = new Goods();
		OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(id);
		if (orderSku.getGoodsId() !=null) {
			goods = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
		}else{
			goods = goodsMapper.selectByPrimaryKey(1);
		}
		return goods;
	}
	
	// 2017-11-1 -pc端的需要
		public OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception {

			try {

				OrderShop orderShop2 = new OrderShop();
				Set<String> set = new HashSet<>();

				orderShop2.setId(orderShop.getId());
				OrderShop orderShops = new OrderShop();
				orderShops = orderShopMapper.selectByOrderShopId(orderShop2);

				OrderSku orderSku = new OrderSku();
				orderSku.setOrderId(orderShops.getOrderId());
				orderSku.setShopId(orderShops.getShopId());

				double realDeliveryPrice = 0.0;// 总邮费
				int deliveryPrice = 0;
				double allPrice = 0.0;

				Order order = new Order();
				order = orderMapper.selectByPrimaryKey(orderShops.getOrderId());
				OrderSimple orderSimple = new OrderSimple();
				List<OrderSku> orderSkuList = orderSkuMapper.selectOrderSkuByParams1(orderSku);

				if (orderSkuList.size() <1) {
					return orderShops;
				}else{
					MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.getShopId());
					orderShops.setShopName(memberShop.getShopName());

					switch (orderShops.getStatus()) {
					case 1:
						orderShops.setMystatus(Contants.orderShopStatu1);
						break;
					case 2:
						orderShops.setMystatus(Contants.orderShopStatu2);
						break;
					case 3:
						orderShops.setMystatus(Contants.orderShopStatu3);
						break;
					case 4:
						orderShops.setMystatus(Contants.orderShopStatu4);
						break;
					case 5:
						orderShops.setMystatus(Contants.orderShopStatu5);
						break;
					case 6:
						orderShops.setMystatus(Contants.orderShopStatu6);
						break;
					case 7:
						orderShops.setMystatus(Contants.orderShopStatu7);
						break;
					case 8:
						orderShops.setMystatus(Contants.orderShopStatu8);
						break;
					default:
						orderShops.setMystatus(Contants.orderShopStatu1);
					}

					double price = 0.0;
					int goodsNum = 0;
					for (int n = 0; n < orderSkuList.size(); n++) {
						set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));// 将商品的id捡出来
						String value = orderSkuList.get(n).getSkuValue();
						Object object = JsonUtils.stringToObject(value);
						orderSkuList.get(n).setValueObj(object);// 将skuValue转为object

						double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						orderSkuList.get(n).setRealSellPrice(realSellPrice);// 销售价格double=int/100

						double markPrice = (double) orderSkuList.get(n).getRealMarketPrice() / 100;
						orderSkuList.get(n).setRealMarketPrice(markPrice);

						Integer num = orderSkuList.get(n).getSkuNum();
						double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						double sellPrice1 = sellPrice * num;
						goodsNum = goodsNum + num;
						price = price + sellPrice1;
					}
					orderShops.setOrderSku(orderSkuList);
					List<String> myList = new ArrayList<>(set);
					List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(myList);
					for (int n = 0; n < goodsList.size(); n++) {
						int deliveryPrice1 = goodsList.get(n).getDeliveryPrice();
						double realPrice = (double) deliveryPrice1 / 100;
						realDeliveryPrice = realDeliveryPrice + realPrice;
						deliveryPrice = deliveryPrice + deliveryPrice1;
					}
					double allPrice1 = 0.0;
					allPrice1 = realDeliveryPrice + price;
					allPrice = allPrice + allPrice1;
					
				
					double realgDe = (double) orderShops.getgDeliveryPrice()/100;
					double realOrderPrice = (double) orderShops.getOrderPrice()/100;
					double totalproce = realgDe + realOrderPrice;
					
					orderShops.setRealgDeliveryPrice(realgDe);
					orderShops.setDeliveryPrice(deliveryPrice);
					orderShops.setRealDeliveryPrice(realDeliveryPrice);
					orderShops.setAllPrice(totalproce);
					orderShops.setGoodsNumber(goodsNum);
					orderShops.setRealOrderPrice(realOrderPrice);

					orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
					orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
					orderSimple.setId(order.getId());
					orderSimple.setPaymentId(order.getPaymentId());// 支付方式
					orderSimple.setPaymentStatus(order.getPaymentStatus());
					orderSimple.setAddtime(order.getAddtime());
					orderSimple.setOrderPrice(order.getOrderPrice());
					
					double realOrderPrice1 = (double) order.getOrderPrice() / 100;
					double realGdel = (double) order.getDeliveryPrice()/100;
					orderSimple.setG_deliveryPrice(realGdel);
					orderSimple.setRealOrderPrice(realOrderPrice1);
					/** 设置地址 */
					if (order.getmAddrId() != null) {
						// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
						MemberUserAddress memberUserAddress = new MemberUserAddress();
						memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
						orderSimple.setMemberUserAddress(memberUserAddress);
					} else {
						orderSimple.setMemberUserAddress(null);
					}

					orderShops.setOrderSimple(orderSimple);
					return orderShops;
				}

				

			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
			// return orderShopList;
		}
		
		public int updateOrderRefundByParams(OrderRefundDoc doc) throws Exception{
			int row=0;
			row = orderRefundDocMapper.updateByPrimaryKeySelective(doc);
			//设置审核失效时间
			Calendar ca = Calendar.getInstance();
			ca.setTime(doc.getAddtime());
			ca.add(Calendar.MINUTE, Contants.fiveDayLen);
			doc.setRefundValidTime(ca.getTime());
			orderRefundDocMapper.updateByPrimaryKeySelective(doc);
			try {
				insertOrderRefundStep(doc.getId());
			} catch (Exception e) {
				// TODO: handle exception
			}
			return row;
		}
		
	public int countOrderShopId(Integer orderSkuId) throws Exception{
		int row =-1;
		OrderSku orderSku = new OrderSku();
		orderSku = orderSkuMapper.selectByPrimaryKey(orderSkuId);
		
		if (orderSku !=null) {
			//该orderShop下面有多少商品
			int count = orderSkuMapper.selectCountOrderSku(orderSku.getId());
			//该orderShop对应已有多少商品退款了？
			int count1 =orderRefundDocMapper.selectCountByOrderShopId(orderSku.getOrderShopId());
			//还剩多少商品没退款
			row = count - count1;
		}
		return row;
		
	}
	
	public int insertOrderRefundStep(Integer docId) throws Exception{
		int row = 0;
		OrderRefundDoc doc = orderRefundDocMapper.selectByPrimaryKey(docId);
		OrderRefundDocStep step = new OrderRefundDocStep();
		step.setAddtime(new Date());
		step.setMid(doc.getmId());
		step.setRefundType(doc.getRefundType());
		step.setOrderRefundDocId(doc.getId());
		row = orderRefundDocStepMapper.insertSelective(step);
		return row;
	}
	
	public static void main(String[] args) {
		int refundGoodsPrcie=10;
		int allGoodsPrice=15;
		int couponDeductionAllPrice=6;
		double a=(double)refundGoodsPrcie/allGoodsPrice;
		double b=a*couponDeductionAllPrice;
		double couponDeductionPrice=((double)refundGoodsPrcie/allGoodsPrice)*couponDeductionAllPrice;//该商品的价格/参与抵扣的所有商品的金额 *总抵扣金额 
		DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数  
		System.out.println(a); 
		System.out.println(b); 
		System.out.println(df.format(couponDeductionPrice));  
		
	}
	
	
	//查询该商品是否申请过售后服务
	public Integer selectIsRefund(Integer orderSkuId){
		return orderRefundDocMapper.selectIsRefund(orderSkuId);
	}
	public Integer selectOShopStatus(Integer orderSkuId){
		return orderRefundDocMapper.selectOShopStatus(orderSkuId);
	}
	public List<OrderRefundDoc> selectOrderRefundDoc(Integer orderSkuId){
		return orderRefundDocMapper.selectRefundType(orderSkuId);
	}
}
