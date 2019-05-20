package com.bh.product.api.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.CashDepositMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsFavoriteMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.HollandDauctionLogMapper;
import com.bh.goods.mapper.HollandDauctionMapper;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.HollandDauction;
import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.ApiHollandDauctionLogService;
import com.bh.product.api.service.ApiHollandDauctionService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.IDUtils;
import com.bh.utils.PageBean;
import com.bh.utils.pay.HttpService;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;


@Service
public class ApiHollandDauctionLogServiceImpl implements ApiHollandDauctionLogService{
	@Autowired
	private HollandDauctionLogMapper mapper;
	@Autowired
	private HollandDauctionMapper dauctionMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsCartMapper goodsCartMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private ApiHollandDauctionService dauctionService;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private GoodsFavoriteMapper favoriteMapper;
	/**
	 * 用户竞拍操作
	 */
	@Override
	public int insert(HollandDauctionLog entity) throws Exception {
		Date date = new Date();
		HollandDauction hd  = null;
		HollandDauction dauctionThis = new HollandDauction();
		HollandDauction dauction = new HollandDauction();
		dauction.setGoodsId(entity.getGoodsId());
		hd = dauctionMapper.getByGoodsId(dauction);
		
		//判断是否交押金
		CashDeposit findCashDeposit = new CashDeposit();
		findCashDeposit.setmId(entity.getmId());
		findCashDeposit.setGoodsId(entity.getGoodsId());
		findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
		if(cashDeposit.size()==0){
			return 999;
		}
				
		List<HollandDauctionLog> list = mapper.getListByGoodsIdAndStatus(entity);
		if(list.size()>0){
			HollandDauctionLog oldLog = list.get(0);
			Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
			Long curTimeStamp = date.getTime();
			
			if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
				if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
					oldLog.setPayStatus(1); //待支付
					oldLog.setEndTime(date);
					//生成待支付订单
					Order order = rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
					String orderNo = order.getOrderNo();
					if(orderNo!=null){
						oldLog.setOrderNo(orderNo);
					}
					mapper.updateByPrimaryKeySelective(oldLog);
					//未中标用户退押金
					dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
					
					if(hd.getStoreNums()>0){
						if(hd.getStoreNums()==1){
							
						}else{
							entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
							hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
							Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
							hd.setLoseTime(loseTime);
							hd.setStartTime(date);
						}
						hd.setStoreNums(hd.getStoreNums()-1);
						dauctionMapper.updateByPrimaryKeySelective(hd);
					}
					
					Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					int price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
					entity.setPrice(price);
				}else{
					int highPrice = (int)(entity.getHighPrice()*100)+(int)(entity.getRealPrice()*100);//判断当前出价是否是最高价
					if(highPrice <= oldLog.getPrice()){
						return 555;
					}
					
					oldLog.setdStatus(1); //将上一次的竞拍记录置为无效
					mapper.updateByPrimaryKeySelective(oldLog);
					
					entity.setCurrentPeriods(oldLog.getCurrentPeriods());
					entity.setPrice(oldLog.getPrice()+(int)(entity.getRealPrice()*100));
				}
			}else{
				Long thisTimeStamp = hd.getStartTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				int price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
				entity.setPrice(price);
				entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
			}
		}else{
			entity.setCurrentPeriods(1); //期数
			Long thisTimeStamp = hd.getStartTime().getTime();
			Long cTime = date.getTime()-thisTimeStamp;
			int second = (int)(cTime/1000/60);
			int db = second /hd.getTimeSection();
			int price = hd.getDauctionPrice()-db*hd.getScopePrice();
			if(price<hd.getLowPrice()){
				price = hd.getLowPrice();
			}
			entity.setPrice(price);
		}
		
		dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
		if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
			return 666;
		}else{
			entity.setCurrentStore(dauctionThis.getStoreNums());
		}
		
		entity.sethId(hd.getId());
		entity.setdStatus(0);
		entity.setAddTime(new Date());
		return mapper.insertSelective(entity);
	}
	
	/*public int insert(HollandDauctionLog entity) throws Exception {
		Date date = new Date();
		HollandDauction hd  = null;
		HollandDauction dauctionThis = new HollandDauction();
		HollandDauction dauction = new HollandDauction();
		dauction.setGoodsId(entity.getGoodsId());
		hd = dauctionMapper.getByGoodsId(dauction);
		
		//判断是否交押金
		CashDeposit findCashDeposit = new CashDeposit();
		findCashDeposit.setmId(entity.getmId());
		findCashDeposit.setGoodsId(entity.getGoodsId());
		findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
		if(cashDeposit.size()==0){
			return 999;
		}
				
		List<HollandDauctionLog> list = mapper.getListByGoodsIdAndStatus(entity);
		if(list.size()>0){
			HollandDauctionLog oldLog = list.get(0);
			Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
			Long curTimeStamp = date.getTime();
			
			if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
				if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
					oldLog.setPayStatus(1); //待支付
					oldLog.setEndTime(date);
					//生成待支付订单
					Order order = rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
					String orderNo = order.getOrderNo();
					if(orderNo!=null){
						oldLog.setOrderNo(orderNo);
					}
					mapper.updateByPrimaryKeySelective(oldLog);
					//未中标用户退押金
					dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
					
					if(hd.getStoreNums()>0){
						if(hd.getStoreNums()==1){
							
						}else{
							entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
							hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
							Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
							hd.setLoseTime(loseTime);
						}
						hd.setStoreNums(hd.getStoreNums()-1);
						dauctionMapper.updateByPrimaryKeySelective(hd);
					}
					
					Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					int price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
					entity.setPrice(price);
				}else{
					int highPrice = (int)(entity.getHighPrice()*100)+(int)(entity.getRealPrice()*100);//判断当前出价是否是最高价
					if(highPrice <= oldLog.getPrice()){
						return 555;
					}
					
					oldLog.setdStatus(1); //将上一次的竞拍记录置为无效
					mapper.updateByPrimaryKeySelective(oldLog);
					
					entity.setCurrentPeriods(oldLog.getCurrentPeriods());
					entity.setPrice(oldLog.getPrice()+(int)(entity.getRealPrice()*100));
				}
			}else{
				Long thisTimeStamp = oldLog.getEndTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				int price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
				entity.setPrice(price);
				entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
			}
		}else{
			entity.setCurrentPeriods(1); //期数
			Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
			Long thisTimeStamp = goods.getUpTime().getTime();
			Long cTime = date.getTime()-thisTimeStamp;
			int second = (int)(cTime/1000/60);
			int db = second /hd.getTimeSection();
			int price = hd.getDauctionPrice()-db*hd.getScopePrice();
			if(price<hd.getLowPrice()){
				price = hd.getLowPrice();
			}
			entity.setPrice(price);
		}
		
		dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
		if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
			return 666;
		}else{
			entity.setCurrentStore(dauctionThis.getStoreNums());
		}
		
		entity.sethId(hd.getId());
		entity.setdStatus(0);
		entity.setAddTime(new Date());
		return mapper.insertSelective(entity);
	}*/


	@Override
	public PageBean<HollandDauctionLog> apiLogList(HollandDauctionLog entity) throws Exception {
		double realPrice = 0;
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), 10, true);
		List<HollandDauctionLog> list = mapper.getListByGoodsIdAndCurrentPeriods(entity);
		if(list.size()>0){
			for(HollandDauctionLog log : list){
				realPrice = (double)log.getPrice()/100;
				log.setRealPrice(realPrice);
				Member member = memberMapper.selectByPrimaryKey(log.getmId());
				if(member!=null){
					log.setmNick(URLDecoder.decode(member.getUsername(), "utf-8"));
					if(!StringUtils.isEmptyOrWhitespaceOnly(member.getHeadimgurl())){
						log.setHeadImg(member.getHeadimgurl());
					}else{
						log.setHeadImg(Contants.headImage);
					}
				}
			}
		}
		PageBean<HollandDauctionLog> pageBean = new PageBean<>(list);
		return pageBean;
	}
	/**
	 * @Description: 生成荷兰式拍卖订单
	 * @author xieyc
	 * @date 2018年5月23日 下午1:00:27 
	 */
	public Order rendAuctionOrder(int price,int mId,int goodsId){
		Goods goods=goodsMapper.selectByPrimaryKey(goodsId);
		GoodsSku goodsSku=goodsSkuMapper.selectListByGoodsId(goodsId).get(0);//拍卖商品（goods与goods_sku  一对一）
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(goods.getShopId());
		MemberUserAddress memberUserAddress= memberUserAddressMapper.getByUserId(mId);//查询用户默认收货地址
		if(memberUserAddress==null){//没有默认地址的话，取该用户的所有地址的第一条地址
			List<MemberUserAddress> list=memberUserAddressMapper.selectAllBymId(mId);
			if(list.size()>0){
				memberUserAddress=list.get(0);
			}
		}
		
		
		/****************goods_cart 记录生成 start***************************/
		GoodsCart saveGoodsCart=new GoodsCart();
		saveGoodsCart.setmId(mId);//用户id
		saveGoodsCart.setShopId(goods.getShopId());//商家的id
		saveGoodsCart.setgId(goodsId);//商品good的id
		saveGoodsCart.setNum(1);//数量
		saveGoodsCart.setAddtime(new Date());//添加时间
		saveGoodsCart.setIsDel(4);//是否删除，0是不删除，1是删除,2清空失效的商品,3.一键购买的时候，不在购物车展示;4.提交订单，不需要在购物车中展示
		saveGoodsCart.setGskuid(goodsSku.getId());//goods_sku的id
		saveGoodsCart.setTeamPrice(goodsSku.getTeamPrice());//团购价格单位分
		//saveGoodsCart.setIsJD(0);//是否是京东商品，0否，1是
		goodsCartMapper.insertSelective(saveGoodsCart);//保存购物车
		/****************goods_cart 记录生成 end***************************/
		
		/**
		 * 邮费规则:2018-5-21 滨惠商城跟会员收取的邮费标准是：（跟京东一模一样）
		 * 
		 * 订单金额＜49元，收取基础运费8元，不收续重运费；
		 * 
		 * 49元≤订单金额＜99元，收取基础运费6元，不收续重运费；
		 * 
		 * 订单金额≥99元，免基础运费，不收续重运费。
		 * 
		 */
		int gDeliveryPrice=0;//荷兰式商品免邮（不需要做判断）
	/*	if (price < 4900) {
			gDeliveryPrice = Contants.price1;
		} else if ((price < 9900) && (price >= 4900)) {
			gDeliveryPrice = Contants.price2;
		} else if (price > 9900) {
			gDeliveryPrice = 0;
		}*/
		
		/***************order_main记录生成  start*****************/
		Order saveOrder = new Order();
		saveOrder.setOrderNo(IDUtils.getOrderNo(memberShop.getBusiPayPre()));//订单号
		saveOrder.setmId(mId);// 用户的id
		saveOrder.setPaymentId(3);//支付方式（ 1货到付款 2 支付宝支付  3微信支付'）
		saveOrder.setPaymentStatus(1);//'支付状态 (0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败)
		saveOrder.setPaymentNo(null);//第三方支付交易号
		saveOrder.setDeliveryId(0);//配送方式
		saveOrder.setDeliveryTime(null);//配送时间
		saveOrder.setDeliveryStatus(0);// 发货状态(0未发货1已发货2为部分发货 )
		saveOrder.setShopId(goods.getShopId());//店铺id
		//saveOrder.setIsShopCheckout(null);//是否给店铺结算货款 0:未结算;2:等待结算1:已结算
		saveOrder.setStatus(1);//订单状态 (1下单，2已付，3完成)
		saveOrder.setSkuPrice(0);//商品市场总价单位分
		saveOrder.setSkuPriceReal(0);//商品销售价格单位分
		saveOrder.setDeliveryPrice(gDeliveryPrice);//物流原价单位分
		//saveOrder.setDeliveryPriceReal(0);//物流支付价格单位分(不注释报错   不知道原因)
		saveOrder.setDiscountPrice(0);//改价金额单位分
		saveOrder.setPromotionPrice(0);//优惠活动金额
		saveOrder.setCouponsPrice(0);//优惠券金额
		saveOrder.setCouponsId(null);//优惠券id
		saveOrder.setOrderPrice(price+gDeliveryPrice);//订单总金额单位分（已包含邮费）
		saveOrder.setAddtime(new Date());//下单时间
		saveOrder.setPaytime(null);//支付时间
		saveOrder.setSendtime(null);//发货时间
		saveOrder.setIsDel(false);//0为正常1为删除
		if(memberUserAddress != null){
			saveOrder.setmAddrId(memberUserAddress.getId());//用户地址Id
		}
		saveOrder.setCartId(saveGoodsCart.getId()+"");//购物车ID
		saveOrder.setFz(5);//是否是团购单  0不是，1普通拼团单，2秒杀，3抽奖，4惠省钱，5拍卖
		saveOrder.settNo(null);//首次生成订单的添加（首次生成拼团单号）
		saveOrder.setIsBeans(0);// 是否是使用滨惠豆抵扣
		saveOrder.setSavePrice(0);//折扣金额
		saveOrder.setExpressName(null);//快递公司
		saveOrder.setExpressNo(null);//快递单号
		
		orderMapper.insertSelective(saveOrder);//保存order
		/***************order_main记录生成  end********************/
		
		
		
		/***************order_shop记录生成  start*****************/
		OrderShop saveOrderShop = new OrderShop();
		saveOrderShop.setmId(mId);//用户的id
		saveOrderShop.setShopId(goods.getShopId());//商家ID
		saveOrderShop.setOrderId(saveOrder.getId());//订单ID
		saveOrderShop.setOrderNo(saveOrder.getOrderNo());//订单号
		saveOrderShop.setShopOrderNo(IDUtils.getOrderNo(memberShop.getBusiPayPre()));//商家订单号
		saveOrderShop.setStatus(1);//商家订单状态：1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除、9备货中
		saveOrderShop.setIsRefund(0);//是否退款:0否，1部分退款，2全部退款
		saveOrderShop.setDeliveryPrice(0);//配送费单位分
		saveOrderShop.setdState(0);//配送状态0初始化，1待接单，2待发货，3配送中，4已送达，5已取消，6已确认
		saveOrderShop.setIsShopCheckout(null);//是否给店铺结算货款 0:未结算;2:等待结算1:已结算
		saveOrderShop.setOrderPrice(price+gDeliveryPrice);//订单总金额单位分(包含邮费)
		saveOrderShop.setgDeliveryPrice(gDeliveryPrice);//邮费
		saveOrderShop.setReceivedtime(null);//收货时间
		saveOrderShop.setCastTime(null);//抛单时间
		saveOrderShop.setSendTime(null);//发货时间
		saveOrderShop.setJdorderid("0");//如果包含京东商品则是jdOrderId的编号，如果是0，则该单不是京东单，如果是-1则表示该商品向京东下单失败，失败的代码erro_code和失败的信息err_msg
		saveOrderShop.setRemark(null);//备注
		saveOrderShop.setErrCode(null);//京东下单的错误代码
		saveOrderShop.setErrMsg(null);//京东下单错误的信息
		saveOrderShop.setSavePrice(0);//使用滨惠豆抵扣的钱
		saveOrderShop.setDeliveryWay(0);//配送方式：0速达，1商家自配，2京东物流，3表示其他物流
		saveOrderShop.setPrintCount(0);//打印销售单次数
		saveOrderShop.setPrintCode(null);//打印验证码
		
		orderShopMapper.insertSelective(saveOrderShop);//保存order_shop
		/***************order_shop记录生成  end*******************/
		
		
		
		/***************order_sku记录生成  start*****************/
		OrderSku saveOrderSku = new OrderSku();
		saveOrderSku.setOrderId(saveOrder.getId());//订单id
		saveOrderSku.setOrderShopId(saveOrderShop.getId());//商家订单id
		saveOrderSku.setGoodsId(goodsId);//商品id
		saveOrderSku.setGoodsName(goods.getName());//商品名字
		saveOrderSku.setSkuId(goodsSku.getId());//skuid
		saveOrderSku.setSkuNo(goodsSku.getSkuNo());//sku编码
		
		JSONObject jsonObj = new JSONObject(goodsSku.getValue());
		JSONArray personList = jsonObj.getJSONArray("url");
		saveOrderSku.setSkuImage((String) personList.get(0));//商品图片
		
		saveOrderSku.setSkuNum(1);//商品数量
		saveOrderSku.setSkuMarketPrice(goodsSku.getMarketPrice());//市场价格单位分
		saveOrderSku.setSkuSellPriceReal(price);//支付价格单位分
		saveOrderSku.setSkuWeight(goodsSku.getWeight());//商品重量
		saveOrderSku.setSkuValue(goodsSku.getValue());//规格属性数组
		//saveOrderSku.setIsSend(0);//是否已发货 0、未发货;1、已发货
		saveOrderSku.setIsRefund(0);//是否评价0未评价.1已评价
		saveOrderSku.setShopId(goods.getShopId());//商品所属店铺id
		saveOrderSku.setdState(0);//配送状态0初始化，1抛单，2配送中，3待结算，4已完成
		saveOrderSku.setsId(0);//配送员id
		saveOrderSku.setRefund(0);//是否退款0.为正常,1.退款中,2.退款完成
		//saveOrderSku.setTeamPrice(0);//团购价格单位分
		
		orderSkuMapper.insertSelective(saveOrderSku);//保存order_sku
		
		/***************order_sku记录生成  start*****************/
		
		
		return saveOrder;
	}


	@Override
	public int updateStatus() throws Exception {
		int row = 0;
		OrderShop entity = new OrderShop();
		List<HollandDauctionLog> list = mapper.getListByDStatusAndPayStatus();
		if(list.size()>0){
			for(HollandDauctionLog log : list){
				Long timeStamp = log.getAddTime().getTime()+86400000;
				Date date = new Date();
				Long curTimeStamp = date.getTime();
				if(curTimeStamp>=timeStamp){
					log.setdStatus(3);
					row = mapper.updateByPrimaryKeySelective(log);
					if(log.getOrderNo()!=null){
						entity.setOrderNo(log.getOrderNo());
						OrderShop orderShop = orderShopMapper.getByOrderNo(entity);
						if(orderShop!=null){
							orderShop.setStatus(6); //取消订单
							orderShopMapper.updateByPrimaryKeySelective(orderShop);
						}
					}
				}
			}
		}
		return row;
	}


	@Override
	public int updateSecondStatus() throws Exception {
		Member member = null;
		Double realPrice = null;
		String userName = null;
		String headImg = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String baseUrl = Contants.BIN_HUI_URL+"/bh-webserver/webPush/sendAuctionPayMsg";
		int row = 0;
		List<HollandDauctionLog> list = mapper.getListByDStatusAndPayStatusAndSecond();
		if(list.size()>0){
			for(HollandDauctionLog log : list){
				realPrice = (double)log.getPrice()/100;
				member = memberMapper.selectByPrimaryKey(log.getmId());
				if(StringUtils.isEmptyOrWhitespaceOnly(member.getUsername())){
					userName = "游客";
				}else{
					userName = member.getUsername();
				}
				if(StringUtils.isEmptyOrWhitespaceOnly(member.getHeadimgurl())){
					headImg = Contants.headImage;
				}else{
					headImg = member.getHeadimgurl();
				}
				
				HollandDauction hd = dauctionMapper.selectByPrimaryKey(log.gethId());
				Integer sendThisCurrentPeriods = null;
				Date date = new Date();
				if(hd!=null){
					sendThisCurrentPeriods = hd.getCurrentPeriods();//当前期标识，消息推送用
					if(hd.getStoreNums()==1){
						hd.setStoreNums(hd.getStoreNums()-1);
					}else if(hd.getStoreNums()>1){
						hd.setStoreNums(hd.getStoreNums()-1);
						hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
						Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
						hd.setLoseTime(loseTime);
						hd.setStartTime(date);
					}
					row = dauctionMapper.updateByPrimaryKeySelective(hd);
				}
				log.setPayStatus(1); //待支付
				log.setEndTime(date);
				Order order = rendAuctionOrder(log.getPrice(), log.getmId(), log.getGoodsId());
				String orderNo =order.getOrderNo(); 
				if(orderNo!=null){
					log.setOrderNo(orderNo);
				}
				row = mapper.updateByPrimaryKeySelective(log);
				//未中标用户退押金
				dauctionService.refundDeposit(log.getGoodsId(), log.getCurrentPeriods(), log.getmId());
				System.out.println("**********"+log.getGoodsId().toString());
				String url = baseUrl+"?mId="+log.getmId()
							+"&goodsId="+log.getGoodsId().toString()
							+"&userName="+userName
							+"&headImg="+headImg
							+"&auctionPrice="+realPrice.toString()
							+"&curTime="+df.format(log.getAddTime())
							+"&orderId="+order.getId()
							+"&currentPeriods="+sendThisCurrentPeriods.toString();
				HttpService.doGet(url);
			}
		}
		return row;
	}
	
	private Date accountLoseTime(HollandDauction hd, Date endTime){
		//计算新一期流拍时间
		int middle = hd.getDauctionPrice() - hd.getLowPrice();
		int remainder = middle % hd.getScopePrice(); //余数
		int result = middle / hd.getScopePrice();  //去余数结果
		int num = remainder == 0 ? result+1:result+2;
		Long thisTimeStamp = (long) (num * hd.getTimeSection()*60000);
		Long reusltTimeStamp = endTime.getTime() + thisTimeStamp;
		Date date = new Date(reusltTimeStamp);			
		return date;
	}


	@Override
	public int insertFirst(HollandDauctionLog entity) throws Exception {
		int row = 0;
		Date date = new Date();
		HollandDauction hd  = null;
		HollandDauction dauctionThis = new HollandDauction();
		HollandDauction dauction = new HollandDauction();
		dauction.setGoodsId(entity.getGoodsId());
		hd = dauctionMapper.getByGoodsId(dauction);
		
		//判断是否交押金
		CashDeposit findCashDeposit = new CashDeposit();
		findCashDeposit.setmId(entity.getmId());
		findCashDeposit.setGoodsId(entity.getGoodsId());
		findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
		if(cashDeposit.size()==0){
			return 999;
		}
		System.out.println(hd.getCurrentPeriods());
		System.out.println(entity.getCurrentPeriods());
		
		HollandDauctionLog lg = new HollandDauctionLog();
		lg.setCurrentPeriods(entity.getCurrentPeriods());
		lg.setGoodsId(entity.getGoodsId());
		List<HollandDauctionLog> lgList = mapper.getListByGoodsIdAndCurrentPeriods(lg);
		if(lgList.size()>0){//防止异步期出价问题
			if(lgList.get(0).getdStatus()==4){//当前期流拍
				return 1;
			}
			if(lgList.get(0).getOrderNo()!=null){//当前期已成交
				return 1;
			}
		}else{
			Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
			List<HollandDauctionLog> list = mapper.getListByGoodsIdAndStatus(entity);
			if(list.size()>0){
				HollandDauctionLog oldLog = list.get(0);  
				Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
				Long curTimeStamp = date.getTime();
				
				if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
					if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
						oldLog.setPayStatus(1); //待支付
						oldLog.setEndTime(date);
						//生成待支付订单
						Order order = rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
						String orderNo = order.getOrderNo();
						if(orderNo!=null){
							oldLog.setOrderNo(orderNo);
						}
						row = mapper.updateByPrimaryKeySelective(oldLog);
						//未中标用户退押金
						dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
						
						if(hd.getStoreNums()>0){
							if(hd.getStoreNums()==1){
								
							}else{
								entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
								hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
								Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
								hd.setLoseTime(loseTime);
								hd.setStartTime(date);
							}
							hd.setStoreNums(hd.getStoreNums()-1);
							row = dauctionMapper.updateByPrimaryKeySelective(hd);
						}
						
						Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
						Long cTime = date.getTime()-thisTimeStamp;
						int second = (int)(cTime/1000/60);
						int db = second /hd.getTimeSection();
						int price = hd.getDauctionPrice()-db*hd.getScopePrice();
						if(price<hd.getLowPrice()){
							price = hd.getLowPrice();
						}
						entity.setPrice(price);
					}else{
						dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
						if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
							return 666;
						}else{
							return 1;
						}
					}
				}else{
					Long thisTimeStamp = hd.getStartTime().getTime();
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					int price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
					entity.setPrice(price);
					entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
				}
			}else{
				entity.setCurrentPeriods(1); //期数
				Long thisTimeStamp = hd.getStartTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				int price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
				entity.setPrice(price);
			}
			
			dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
			if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
				return 666;
			}else{
				entity.setCurrentStore(dauctionThis.getStoreNums());
			}
			
			entity.sethId(hd.getId());
			entity.setdStatus(0);
			entity.setAddTime(new Date());
			row = mapper.insertSelective(entity);
		}
		return row;
	}
	
	/*public int insertFirst(HollandDauctionLog entity) throws Exception {
		int row = 0;
		Date date = new Date();
		HollandDauction hd  = null;
		HollandDauction dauctionThis = new HollandDauction();
		HollandDauction dauction = new HollandDauction();
		dauction.setGoodsId(entity.getGoodsId());
		hd = dauctionMapper.getByGoodsId(dauction);
		
		//判断是否交押金
		CashDeposit findCashDeposit = new CashDeposit();
		findCashDeposit.setmId(entity.getmId());
		findCashDeposit.setGoodsId(entity.getGoodsId());
		findCashDeposit.setCurrentPeriods(hd.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		List<CashDeposit> cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit);
		if(cashDeposit.size()==0){
			return 999;
		}
		System.out.println(hd.getCurrentPeriods());
		System.out.println(entity.getCurrentPeriods());
		
		HollandDauctionLog lg = new HollandDauctionLog();
		lg.setCurrentPeriods(entity.getCurrentPeriods());
		lg.setGoodsId(entity.getGoodsId());
		List<HollandDauctionLog> lgList = mapper.getListByGoodsIdAndCurrentPeriods(lg);
		if(lgList.size()>0){//防止异步期出价问题
			if(lgList.get(0).getdStatus()==4){//当前期流拍
				return 1;
			}
			if(lgList.get(0).getOrderNo()!=null){//当前期已成交
				return 1;
			}
		}else{
			Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
			List<HollandDauctionLog> list = mapper.getListByGoodsIdAndStatus(entity);
			if(list.size()>0){
				HollandDauctionLog oldLog = list.get(0);  
				Long timeStamp = oldLog.getAddTime().getTime()+30*1000;
				Long curTimeStamp = date.getTime();
				
				if(oldLog.getdStatus()==0 && oldLog.getPayStatus()==0){ //拍卖中
					if(curTimeStamp>=timeStamp){ //检测上次拍卖是否过了30秒
						oldLog.setPayStatus(1); //待支付
						oldLog.setEndTime(date);
						//生成待支付订单
						Order order = rendAuctionOrder(oldLog.getPrice(), oldLog.getmId(), oldLog.getGoodsId());
						String orderNo = order.getOrderNo();
						if(orderNo!=null){
							oldLog.setOrderNo(orderNo);
						}
						row = mapper.updateByPrimaryKeySelective(oldLog);
						//未中标用户退押金
						dauctionService.refundDeposit(oldLog.getGoodsId(), oldLog.getCurrentPeriods(), oldLog.getmId());
						
						if(hd.getStoreNums()>0){
							if(hd.getStoreNums()==1){
								
							}else{
								entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
								hd.setCurrentPeriods(hd.getCurrentPeriods()+1);
								Date loseTime = accountLoseTime(hd, date);//新一期流拍时间
								hd.setLoseTime(loseTime);
							}
							hd.setStoreNums(hd.getStoreNums()-1);
							row = dauctionMapper.updateByPrimaryKeySelective(hd);
						}
						
						Long thisTimeStamp = oldLog.getEndTime().getTime(); //上期结束时间等于本期的开始时间
						Long cTime = date.getTime()-thisTimeStamp;
						int second = (int)(cTime/1000/60);
						int db = second /hd.getTimeSection();
						int price = hd.getDauctionPrice()-db*hd.getScopePrice();
						if(price<hd.getLowPrice()){
							price = hd.getLowPrice();
						}
						entity.setPrice(price);
					}else{
						dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
						if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
							return 666;
						}else{
							return 1;
						}
					}
				}else{
					Long thisTimeStamp = oldLog.getEndTime().getTime();
					Long cTime = date.getTime()-thisTimeStamp;
					int second = (int)(cTime/1000/60);
					int db = second /hd.getTimeSection();
					int price = hd.getDauctionPrice()-db*hd.getScopePrice();
					if(price<hd.getLowPrice()){
						price = hd.getLowPrice();
					}
					entity.setPrice(price);
					entity.setCurrentPeriods(oldLog.getCurrentPeriods()+1);
				}
			}else{
				entity.setCurrentPeriods(1); //期数
				Long thisTimeStamp = goods.getUpTime().getTime();
				Long cTime = date.getTime()-thisTimeStamp;
				int second = (int)(cTime/1000/60);
				int db = second /hd.getTimeSection();
				int price = hd.getDauctionPrice()-db*hd.getScopePrice();
				if(price<hd.getLowPrice()){
					price = hd.getLowPrice();
				}
				entity.setPrice(price);
			}
			
			dauctionThis = dauctionMapper.selectByPrimaryKey(hd.getId());
			if(dauctionThis.getStoreNums()<1){ //判断是否还有库存
				return 666;
			}else{
				entity.setCurrentStore(dauctionThis.getStoreNums());
			}
			
			entity.sethId(hd.getId());
			entity.setdStatus(0);
			entity.setAddTime(new Date());
			row = mapper.insertSelective(entity);
		}
		return row;
	}*/

	/**
	 * 用户竞拍历史记录
	 */
	@Override
	public List<Map<String, Object>> apiUserLogList(HollandDauctionLog entity) throws Exception {
		HollandDauction hd = null;
		Goods goods = null;
		List<Map<String, Object>> result = new ArrayList<>();
		
		entity.setPageSize(Contants.PAGE_SIZE);
		entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage())-1)*Contants.PAGE_SIZE);
		List<HollandDauctionLog> list = mapper.getByUserLog(entity);
		if(list.size()>0){
			for(HollandDauctionLog log : list){
				Map<String, Object> map =new HashMap<>();
				hd = dauctionMapper.selectByPrimaryKey(log.gethId());
				hd.setRealDauctionPrice((double)hd.getDauctionPrice()/100);
				hd.setRealLowPrice((double)hd.getLowPrice()/100);
				hd.setRealScopePrice((double)hd.getScopePrice()/100);
				
				goods = goodsMapper.selectByPrimaryKey(log.getGoodsId());
				GoodsFavorite favorite = favoriteMapper.findByGoodsIdAndMid(goods.getId(), entity.getmId());
				if(favorite!=null){
					map.put("collect", 0);
				}else{
					map.put("collect", 1);
				}
				map.put("goodsImage", goods.getImage());
				map.put("goodsName", goods.getName());
				
				map.put("startTime", hd.getStartTime());
				map.put("loseTime", hd.getLoseTime());
				
				List<HollandDauctionLog> logList =  mapper.getListByGoodsIdAndStatus(log);
				if(logList.size()>0){
					if(logList.get(0).getdStatus()==0 && logList.get(0).getPayStatus()==0){//当前期正在进行中
						map.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
					}else{
						map.put("price", null);
					}
				}else{
					map.put("price", null);
				}
				map.put("dauction", hd);
				
				if(log.getOrderNo() != null){
					map.put("isSuccess", true);
				}else{
					map.put("isSuccess", false);
				}
				result.add(map);
			}
		}
		return result;
	}
	
	/*public List<Map<String, Object>> apiUserLogList(HollandDauctionLog entity) throws Exception {
		HollandDauction hd = null;
		Goods goods = null;
		List<Map<String, Object>> result = new ArrayList<>();
		
		entity.setPageSize(Contants.PAGE_SIZE);
		entity.setCurrentPageIndex((Integer.valueOf(entity.getCurrentPage())-1)*Contants.PAGE_SIZE);
		List<HollandDauctionLog> list = mapper.getByUserLog(entity);
		if(list.size()>0){
			for(HollandDauctionLog log : list){
				Map<String, Object> map =new HashMap<>();
				hd = dauctionMapper.selectByPrimaryKey(log.gethId());
				hd.setRealDauctionPrice((double)hd.getDauctionPrice()/100);
				hd.setRealLowPrice((double)hd.getLowPrice()/100);
				hd.setRealScopePrice((double)hd.getScopePrice()/100);
				
				goods = goodsMapper.selectByPrimaryKey(log.getGoodsId());
				GoodsFavorite favorite = favoriteMapper.findByGoodsIdAndMid(goods.getId(), entity.getmId());
				if(favorite!=null){
					map.put("collect", 0);
				}else{
					map.put("collect", 1);
				}
				map.put("goodsImage", goods.getImage());
				map.put("goodsName", goods.getName());
				
				if(hd.getCurrentPeriods()==1){
					map.put("startTime", goods.getUpTime());
				}else{
					HollandDauctionLog lg = new HollandDauctionLog();
					lg.setGoodsId(goods.getId());
					List<HollandDauctionLog> logList = mapper.getListByGoodsIdAndStatus(lg);
					map.put("startTime", logList.get(0).getEndTime());
				}
				map.put("loseTime", hd.getLoseTime());
				
				List<HollandDauctionLog> logList =  mapper.getListByGoodsIdAndStatus(log);
				if(logList.size()>0){
					if(logList.get(0).getdStatus()==0 && logList.get(0).getPayStatus()==0){//当前期正在进行中
						map.put("price", (double)logList.get(logList.size()-1).getPrice()/100);
					}else{
						map.put("price", null);
					}
				}else{
					map.put("price", null);
				}
				map.put("dauction", hd);
				
				if(log.getOrderNo() != null){
					map.put("isSuccess", true);
				}else{
					map.put("isSuccess", false);
				}
				result.add(map);
			}
		}
		return result;
	}
*/
	
	/**
	 * <p>Description: 扫描流拍（定时器用）</p>
	 *  @author scj  
	 *  @date 2018年6月6日
	 */
	@Override
	public int updateLostTimeRecord() throws Exception {
		int row = 0;
		List<HollandDauction> list = dauctionMapper.getLostTimeRecord();
		Date now = new Date();
		if(list.size()>0){
			for(HollandDauction hd : list){
				HollandDauctionLog log = new HollandDauctionLog();
				log.setGoodsId(hd.getGoodsId());
				log.setCurrentPeriods(hd.getCurrentPeriods());
				List<HollandDauctionLog> logList = mapper.getListByGoodsIdAndCurrentPeriods(log);
				if(logList.size()==0){
					HollandDauctionLog entity = new HollandDauctionLog();
					entity.setGoodsId(hd.getGoodsId());
					entity.sethId(hd.getId());
					entity.setCurrentStore(hd.getStoreNums());
					entity.setAddTime(now);
					entity.setdStatus(4);//流拍
					entity.setCurrentPeriods(hd.getCurrentPeriods());
					entity.setEndTime(now); //结束时间
					row = mapper.insertSelective(entity);
					
					hd.setTotalPeriods(hd.getTotalPeriods()+1); //总期数+1
					hd.setCurrentPeriods(hd.getCurrentPeriods()+1); //开始下一期
					//计算新一期流拍时间
					int middle = hd.getDauctionPrice() - hd.getLowPrice();
					int remainder = middle % hd.getScopePrice(); //余数
					int result = middle / hd.getScopePrice();  //去余数结果
					int num = remainder == 0 ? result+1:result+2;
					Long thisTimeStamp = (long) (num * hd.getTimeSection()*60000);
					Long reusltTimeStamp = now.getTime() + thisTimeStamp;
					Date date = new Date(reusltTimeStamp);
					hd.setLoseTime(date);
					hd.setStartTime(now);
					row = dauctionMapper.updateByPrimaryKeySelective(hd);
				}
			}
		}
		return row;
	}
}
