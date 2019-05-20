package com.bh.product.api.service.impl;

import com.bh.config.Contants;
import com.bh.goods.mapper.*;
import com.bh.goods.pojo.*;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.AuctionService;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuctionServiceImpl implements AuctionService{
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private GoodsCartMapper goodsCartMapper;
	@Autowired
	private BargainRecordMapper  bargainRecordMapper;
	@Autowired
	private  AuctionConfigMapper auctionConfigMapper;
	@Autowired
	private GoodsFavoriteMapper goodsFavoriteMapper;
	
	/**
	 * @Description: 生成荷兰式拍卖订单
	 * @author xieyc
	 * @date 2018年5月23日 下午1:00:27 
	 */
	public Order rendAucOrderAndRefundDeposit(int price, int mId, int goodsId,int currentPeriods){
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
		
        /***************************bargain_record 表插入记录 start********************************/
        BargainRecord saveBargainRecord = new BargainRecord();
        saveBargainRecord.setGoodsId(goodsId);//商品id
        saveBargainRecord.setUserId(mId);//用户id
        saveBargainRecord.setCurrentPeriods(currentPeriods);//当前期
        saveBargainRecord.setOrderNo(saveOrder.getOrderNo());//订单编号
        saveBargainRecord.setOrderPrice(price);//订单价格
        saveBargainRecord.setAddTime(new Date());//添加时间
        saveBargainRecord.setUpdateTime(new Date());//更新时间
        bargainRecordMapper.insertSelective(saveBargainRecord);
        /***************************bargain_record 表插入记录 end********************************/
		
		this.refundDeposit(goodsId,currentPeriods,mId);//退保证金
		
		
		return saveOrder;
	}
	/**
	 * @Description: 退保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54
	 */
	public void refundDeposit(int goodsId, int currentPeriods, int mId) {// mid该期成功的用户id
		// 获取某个用户某个商品的某一期交纳的保证金是多少
		CashDeposit findCashDeposit = new CashDeposit();// 查询条件
		findCashDeposit.setGoodsId(goodsId);
		findCashDeposit.setCurrentPeriods(currentPeriods);
		List<CashDeposit> cashDepositList = cashDepositMapper.getCashDeposit(findCashDeposit);
		for (CashDeposit cashDeposit : cashDepositList) {
			if (cashDeposit.getmId().intValue() != mId) {// 改期成功竞拍的人在这里不退
				Wallet wallet = walletMapper.getWalletByUid(cashDeposit.getmId()).get(0);
				wallet.setMoney(wallet.getMoney() + cashDeposit.getDepositPrice());
				walletMapper.updateByPrimaryKeySelective(wallet); // 更新钱包金额+

				cashDeposit.setIsrefund(1);// 已经退保证金
				cashDeposit.setRefundTime(new Date());// 退保证金时间
				cashDepositMapper.updateByPrimaryKeySelective(cashDeposit);

				this.insertRefundWalletLog(cashDeposit.getmId(), cashDeposit.getDepositPrice());// 插入钱包记录
			}
		}
	}
	
	/**
	 * 	Description: 插入退保证金出账记录
	 *  @author xieyc 
	 *  @date 2018年5月25日
	 */
	private int insertRefundWalletLog(int mId, int amount){
		WalletLog saveWalletLog = new WalletLog();
		saveWalletLog.setmId(mId);
		saveWalletLog.setAmount(amount);
		saveWalletLog.setInOut(0); //进账
		saveWalletLog.setAddTime(new Date());
		saveWalletLog.setRemark("拍卖退保证金");
		saveWalletLog.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
		saveWalletLog.setStatus(1);
		saveWalletLog.setType(0);
		return walletLogMapper.insertSelective(saveWalletLog);
	}


	/**
	 * @Description: 当商品价格小于保证金的时候,点击去结算调用此接口（不调用wxjsp接口）
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	public void depositJsp(String orderId,String addressId ) {
		Order order = orderMapper.selectByPrimaryKey(Integer.valueOf(orderId));//订单
		if(StringUtils.isNotEmpty(addressId)){
			order.setmAddrId(Integer.valueOf(addressId));//荷兰式订单允许修改地址
		}
		order.setPaymentStatus(2);
		order.setStatus(2);
		order.setPaytime(new Date());
		order.setPaymentId(7); //钱包支付
		orderMapper.updateByPrimaryKeySelective(order);
		OrderShop entity = new OrderShop();
		entity.setOrderNo(order.getOrderNo());
		OrderShop orderShop = orderShopMapper.selectByOrderNo(entity);
		orderShop.setStatus(2);
		orderShopMapper.updateByPrimaryKeySelective(orderShop);
		List<OrderSku> skuList = orderSkuMapper.getSkuListByOrderId(order.getId());
		Goods goods = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
		goods.setSale(goods.getSale()+1);
		goodsMapper.updateByPrimaryKeySelective(goods);
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
		goodsSku.setStoreNums(goodsSku.getStoreNums()-1);
		goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		
		/********************更新记录状态  start**************************/
		BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order.getOrderNo());//拍卖记录
		bargainRecord.setDiscountPrice(order.getOrderPrice());//抵扣金额
		bargainRecord.setUpdateTime(new Date());//更新时间
		bargainRecordMapper.updateByPrimaryKeySelective(bargainRecord);//更新记录
		/********************更新记录状态  end**************************/

		// 获取某个用户某个商品的某一期交纳的保证金是多少
		CashDeposit findCashDeposit = new CashDeposit();// 查询条件
		findCashDeposit.setmId(bargainRecord.getUserId());
		findCashDeposit.setGoodsId(bargainRecord.getGoodsId());
		findCashDeposit.setCurrentPeriods(bargainRecord.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);
		
		cashDeposit.setIsrefund(1);// 已经退保证金
		cashDeposit.setRefundTime(new Date());// 退保证金时间
		cashDepositMapper.updateByPrimaryKeySelective(cashDeposit);//更新保证金表
		
		int refundMonny=cashDeposit.getDepositPrice()-order.getOrderPrice();//退还的保证金
		if(refundMonny>0){
			/*********** 退还保证金 start ****************/	
			Wallet wallet = walletMapper.getWalletByUid(bargainRecord.getUserId()).get(0);
			wallet.setMoney(wallet.getMoney() + refundMonny);
			walletMapper.updateByPrimaryKeySelective(wallet); // 更新钱包金额
			/*********** 退还保证金 end ****************/	
			this.insertRefundWalletLog(bargainRecord.getUserId(),refundMonny);//插入钱包记录
		}
	}
	/**
	 * @Description: 用户某个商品某一期是否已经交了保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54
	 */
	public Map<String, Object> isPayDeposit(CashDeposit entity) {
		Map<String, Object> returnMap=new HashMap<String, Object>();		
		// 获取某个用户某个商品的某一期交纳的保证金是多少
		CashDeposit findCashDeposit = new CashDeposit();// 查询条件
		findCashDeposit.setmId(entity.getmId());
		findCashDeposit.setGoodsId(entity.getGoodsId());
		findCashDeposit.setCurrentPeriods(entity.getCurrentPeriods());
		findCashDeposit.setIsrefund(0);
		List<CashDeposit> cashDepositList = cashDepositMapper.getCashDeposit(findCashDeposit);
		/**
		 * isPayDeposit 为true  直接进入拍卖商品详情页
		 * isPayDeposit 为false 的时候 ==》若goodsCashDeposit<=walletBalance（钱包金额足够交保证金） 时调用  交保证金接口（payDeposit）
		 * 								否则	跳入充值钱包页面
		 */
		if(cashDepositList.size()>0){
			returnMap.put("isPayDeposit",true);//交了保证金
		}else{
			AuctionConfig auctionConfig=auctionConfigMapper.getByGoodsId(entity.getGoodsId());
			returnMap.put("isPayDeposit",false);//没有交保证金
			returnMap.put("goodsCashDeposit",(double)auctionConfig.getCashDeposit()/100);//商品的保证金
			
			List<Wallet> walletList =walletMapper.getWalletByUid(entity.getmId());
			if(walletList.size()>0){
				returnMap.put("walletBalance",(double)walletList.get(0).getMoney()/100);//钱包余额
			}else{
				returnMap.put("walletBalance",0);//钱包余额
			}
		}
		return returnMap;
	}
	/**
	 * @Description:交保证金接口
	 * @author xieyc
	 * @date 2018年7月10日 上午9:56:09 
	 */
	public int payDeposit(CashDeposit entity) {
		int row=0;
		AuctionConfig auctionConfig=auctionConfigMapper.getByGoodsId(entity.getGoodsId());
		int cashDeposit=auctionConfig.getCashDeposit();//该商品需要交纳的保证金
		if(cashDeposit>0){
			Wallet wallet =walletMapper.getWalletByUid(entity.getmId()).get(0);
			if(wallet!=null){
				int money=wallet.getMoney()-cashDeposit;
				if(money>=0){
					wallet.setMoney(wallet.getMoney()-cashDeposit);
					row = walletMapper.updateByPrimaryKey(wallet);//更新钱包金额
					row = insertWalletLog(entity.getmId(),cashDeposit);//钱包记录
					row = insertCashDeposit(entity.getGoodsId(), entity.getmId(),cashDeposit,auctionConfig);//保证金表插入记录
				}else{
					return -2;//钱包金额不足
				}
			}else{
				return -1;//钱包不存在
			}
		}	
		return row;
	}
	/**
	 * <p>Description: 插入出账记录</p>
	 *  @author scj  
	 *  @date 2018年5月25日
	 */
	private int insertWalletLog(int mId, int amount){
		WalletLog entity = new WalletLog();
		entity.setmId(mId);
		entity.setAmount(amount);
		entity.setInOut(1); //出账
		entity.setAddTime(new Date());
		entity.setRemark("拍卖支付保证金");
		entity.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
		entity.setStatus(1);
		entity.setType(0);
		return walletLogMapper.insertSelective(entity);
	}
	
	/**
	 * <p>Description: 插入押金支付记录</p>
	 *  @author scj  
	 *  @date 2018年5月29日
	 */
	private int insertCashDeposit(int goodsId, int mId, int price,AuctionConfig auctionConfig){
		CashDeposit entity = new CashDeposit();
		entity.setmId(mId);
		entity.setGoodsId(goodsId);
		entity.setDepositPrice(price);
		entity.setIsrefund(0);
		entity.setPayTime(new Date());
		if(auctionConfig!=null){
			entity.sethId(auctionConfig.getId());
			entity.setCurrentPeriods(auctionConfig.getCurrentPeriods());
		}
		return cashDepositMapper.insertSelective(entity);
	}
	/**
	 * @Description: 拍卖时获取商品信息接口
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	public AuctionGoodsInfo getBhShopGoodsInfo(Integer goodsId) {
		AuctionGoodsInfo auctionGoodsInfo=new AuctionGoodsInfo();
		auctionGoodsInfo.setGoodsId(goodsId);//商品id
		List<GoodsSku> skuList=goodsSkuMapper.selectListByGoodsId(goodsId);
		Goods goods=goodsMapper.selectByPrimaryKey(goodsId);
		if(skuList.size()>0 &&goods!=null){
			auctionGoodsInfo.setGoodsName(goods.getName());//商品名字
			auctionGoodsInfo.setSkuId(skuList.get(0).getId());//商品skuid
			auctionGoodsInfo.setValue(skuList.get(0).getValue());//商品规格属性
			auctionGoodsInfo.setStoreNums(skuList.get(0).getStoreNums());//商品库存
		}
		return auctionGoodsInfo;
	}
	/**
	 * @Description:某个商品某个用户是否收藏过
	 * @author xieyc
	 * @date @date 2018年7月6日 下午2:06:15
	 */
	public Map<String, Object> isCollect(String goodsIds, Integer mId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String[] strGoodsId = goodsIds.split(",");
		for (String goodsId : strGoodsId) {
			if (mId == null) {
				returnMap.put(goodsId, false);
			} else {
				GoodsFavorite goodsFavorite = goodsFavoriteMapper.findByGoodsIdAndMid(Integer.valueOf(goodsId), mId);
				if (goodsFavorite != null) {
					returnMap.put(goodsId, true);
				} else {
					returnMap.put(goodsId, false);
				}
			}
		}
		return returnMap;
	}
}
