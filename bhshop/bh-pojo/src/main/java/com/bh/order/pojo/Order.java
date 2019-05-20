package com.bh.order.pojo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;

public class Order {
	private Integer id;

	private Integer mId;

	private String orderNo;// 订单的id，格式为yyyyMMddHHmmss+6位随机数

	private Integer paymentId;// 支付方式

	private Integer paymentStatus;// 支付状态
									// :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败
									// ，默认是1待支付

	private String paymentNo;// 第三方交易号

	private Integer deliveryId;// 配送方式

	private Date deliveryTime;// 配送时间

	private Integer deliveryStatus;// 发货状态0未发货1已发货2为部分发货

	private Integer shopId;// 店铺id

	private Integer isShopCheckout;// 是否给店铺结算货款 0:未结算;2:等待结算1:已结算',

	private Integer status;// 订单状态 1下单，2已付，3待评价，4完成

	private Integer refundStatus; // 商家订单退状态

	private Integer orderShopStatus; // 商家订单状态：1待付，2待发货，3已发货，4已收货，5已取消'、6已取消、7已评价、8已删除',

	private Integer dState; // 配送状态

	private Integer skuPrice;// 商品市场总价单位分

	private Integer skuPriceReal;// 商品销售价格单位分

	private Integer deliveryPrice;// 物流原价单位分

	private Integer deliveryPriceReal;

	private Integer discountPrice;// 改价金额单位分

	private Integer promotionPrice;// 优惠活动金额

	private Integer couponsPrice;// 优惠券金额

	private Integer orderPrice;// 订单总金额单位分

	private String couponsId;

	private Date addtime;// 下单时间

	private Date paytime;// 支付时间

	private Date sendtime;// 发货时间

	private Date completetime;// 完成时间

	private Boolean isDel;// 0为正常1为删除

	private Integer mAddrId;// 用户地址Id


	private Integer isBeans;// 1调用使用滨惠豆下的单，0不使用
    private Integer savePrice;//滨惠豆抵扣的钱

    private String expressName;

    private String expressNo;
    private Integer skuPayPrice;//商品实际支付价格,等于 sum(order_sku.sku_pay_price)  group by order_main

    
	private String address; // 买家地址

	private String tel; // 联系电话

	private String fullName; // 买家姓名

	private String goodsId;// 商品的id，传过来的是一个字符串，如果有多个,则有多个商品1,2,3,4,5,6，不需要存入数据库
							// 2017-9-18添加

	private List<OrderSku> orderSku;// orderSku的一个pojo
									// 2017-9-18星期一由cheng添加,不需要在数据库中存在
	private List<OrderShop> orderShops;//// orderShop的一个pojo
										//// 2017-10-27星期五由cheng添加,不需要在数据库中存在
	private double realPrice; // 订单总价转换成“元”，用于页面

	private MemberUserAddress memberUserAddress;// 2017-10-18星期三
	private List<String> goodsNames;// 2017-10-19添加,用户回显支付宝的上面的商品名称

	private double realDeliveryPrice; // 订单邮费转化为元
	private double realSavePrice;//滨惠豆抵扣的金额
	private String cartId;

	private String goodsName; // 商品名称

	private String goodsImage; // 商品图片

	private Integer fz; // 拼团单1是

	private String tNo;

	private String teamNo; // 团号

	private Integer groupFz;

	private Map<String, Object> goodsSkuInfo;

	private Map<String, Object> userInfo;

	private String memberUserName;// 会员名字 xieyc
	
	private Integer goodsSkuId; //活动商品下单使用
	
	private Integer tgId;//活动商品下单使用
	
	private String topicNo;//活动商品下单使用
	
	private String actNo; //活动邀请码
	
	private String currentPage;
	
	private String pageSize;
	
	private String shopOrderNo;
	
	private Date startTime;
	
	private Date endTime;
	
	private Integer groupType;
	
	private String jdOrderId;
	
	private Integer couponNum; //优惠劵 1没值  2有值
	
	private Integer teamNum;
	
	private Integer robyNum;
	
	private MemberShop shopInfo;
	
	private Integer realNum;
	
	private Integer surplusNum;//开团剩余名额
	
	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getActNo() {
		return actNo;
	}

	public void setActNo(String actNo) {
		this.actNo = actNo;
	}

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public Integer getTgId() {
		return tgId;
	}

	public void setTgId(Integer tgId) {
		this.tgId = tgId;
	}

	public Integer getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(Integer goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Map<String, Object> getGoodsSkuInfo() {
		return goodsSkuInfo;
	}

	public void setGoodsSkuInfo(Map<String, Object> goodsSkuInfo) {
		this.goodsSkuInfo = goodsSkuInfo;
	}

	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
	}

	public String getMemberUserName() {
		return memberUserName;
	}

	public void setMemberUserName(String memberUserName) {
		this.memberUserName = memberUserName;
	}

	public Integer getGroupFz() {
		return groupFz;
	}

	public void setGroupFz(Integer groupFz) {
		this.groupFz = groupFz;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public Integer getFz() {
		return fz;
	}

	public void setFz(Integer fz) {
		this.fz = fz;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getRealDeliveryPrice() {
		return realDeliveryPrice;
	}

	public void setRealDeliveryPrice(double realDeliveryPrice) {
		this.realDeliveryPrice = realDeliveryPrice;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public Integer getdState() {
		return dState;
	}

	public void setdState(Integer dState) {
		this.dState = dState;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getOrderShopStatus() {
		return orderShopStatus;
	}

	public void setOrderShopStatus(Integer orderShopStatus) {
		this.orderShopStatus = orderShopStatus;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public Integer getPaymentId() {
		return paymentId;
		/*
		 * switch (paymentId) { case 0: return "货到到付"; case 1: return "在线支付";
		 * case 2: return "公司转帐"; case 3: return "邮局汇款"; default: return ""; }
		 */
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
		/*
		 * switch (paymentStatus) { case 0: return "货到付款";//支付状态
		 * :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败 ，默认是1待支付 case 1: return
		 * "待付款（未支付）"; case 2: return "已付款（已支付）"; case 3: return "待退款"; case 4:
		 * return "退款成功"; case 5: return "退款失败"; default: return ""; }
		 */
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo == null ? null : paymentNo.trim();
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getIsShopCheckout() {
		return isShopCheckout;
	}

	public void setIsShopCheckout(Integer isShopCheckout) {
		this.isShopCheckout = isShopCheckout;
	}

	public Integer getStatus() {
		return status;
		/*
		 * switch (status) {//订单状态
		 * 1生成订单,2支付订单,3已经发货,4完成订单,5待收货6退款,7部分退款8用户取消订单,9作废订单,10退款中，11待评价,12待抛单,
		 * 默认是1 case 1: return "生成订单"; case 2: return "支付订单"; case 3: return
		 * "已经发货"; case 4: return "完成订单"; case 5: return "待收货"; case 6: return
		 * "退款"; case 7: return "部分退款"; case 8: return "用户取消订单"; case 9: return
		 * "作废订单"; case 10: return "退款中"; case 11 :return "待评价"; case 12:return
		 * "待抛单"; default: return ""; }
		 */
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(Integer skuPrice) {
		this.skuPrice = skuPrice;
	}

	public Integer getSkuPriceReal() {
		return skuPriceReal;
	}

	public void setSkuPriceReal(Integer skuPriceReal) {
		this.skuPriceReal = skuPriceReal;
	}

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Integer deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	

	public Integer getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(Integer promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public Integer getCouponsPrice() {
		return couponsPrice;
	}

	public void setCouponsPrice(Integer couponsPrice) {
		this.couponsPrice = couponsPrice;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	
	
	public String getCouponsId() {
		return couponsId;
	}

	public void setCouponsId(String couponsId) {
		this.couponsId = couponsId;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public Date getSendtime() {
		return sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public Date getCompletetime() {
		return completetime;
	}

	public void setCompletetime(Date completetime) {
		this.completetime = completetime;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public List<OrderSku> getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(List<OrderSku> orderSku) {
		this.orderSku = orderSku;
	}

	public MemberUserAddress getMemberUserAddress() {
		return memberUserAddress;
	}

	public void setMemberUserAddress(MemberUserAddress memberUserAddress) {
		this.memberUserAddress = memberUserAddress;
	}

	public List<String> getGoodsNames() {
		return goodsNames;
	}

	public void setGoodsNames(List<String> goodsNames) {
		this.goodsNames = goodsNames;
	}

	public Integer getmAddrId() {
		return mAddrId;
	}

	public void setmAddrId(Integer mAddrId) {
		this.mAddrId = mAddrId;
	}

	public List<OrderShop> getOrderShops() {
		return orderShops;
	}

	public void setOrderShops(List<OrderShop> orderShops) {
		this.orderShops = orderShops;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String gettNo() {
		return tNo;
	}

	public void settNo(String tNo) {
		this.tNo = tNo;
	}

	public Integer getDeliveryPriceReal() {
		return deliveryPriceReal;
	}

	public void setDeliveryPriceReal(Integer deliveryPriceReal) {
		this.deliveryPriceReal = deliveryPriceReal;
	}

	public Integer getIsBeans() {
		return isBeans;
	}

	public void setIsBeans(Integer isBeans) {
		this.isBeans = isBeans;
	}

	public Integer getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(Integer savePrice) {
		this.savePrice = savePrice;
	}
	
	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public double getRealSavePrice() {
		return realSavePrice;
	}

	public void setRealSavePrice(double realSavePrice) {
		this.realSavePrice = realSavePrice;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public Integer getRobyNum() {
		return robyNum;
	}

	public void setRobyNum(Integer robyNum) {
		this.robyNum = robyNum;
	}

	public MemberShop getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(MemberShop shopInfo) {
		this.shopInfo = shopInfo;
	}

	public Integer getRealNum() {
		return realNum;
	}

	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}

	public Integer getSkuPayPrice() {
		return skuPayPrice;
	}

	public void setSkuPayPrice(Integer skuPayPrice) {
		this.skuPayPrice = skuPayPrice;
	}
	
}