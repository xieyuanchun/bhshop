package com.bh.order.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.Track;
import com.bh.result.BhResult;

public class OrderShop implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 805390728006071969L;

	// 该表是商家订单
	private Integer id;
	
	private Integer couponId;
	private Integer couponPrice;
	
	private Integer mId;// 用户ID

	private Integer shopId;// 商家ID

	private Integer orderId;// 订单ID

	private String orderNo;// 订单号

	private String shopOrderNo;// 商家订单号

	private Integer status;// 商家订单状态：1待付，2待发货，3已发货，4已收货，5待评价、6已取消、7已评价、8已删除

	private Integer isRefund;// '是否退款:0否，1部分退款，2全部退款',

	private Integer deliveryPrice;//邮费
	
    private Integer dState;
    
    private Integer orderPrice;//订单总金额单位分
    
    private Date addtime; //下单时间
    
    private Integer gDeliveryPrice;//2017-11-08cheng添加：该shop下面对应的总的goods的deliveryPrice
    
    private Integer isShopCheckout;//是否给店铺结算货款 0:未结算;2:等待结算1:已结算',
    
    private Date receivedtime;//收货时间
    
    private Date castTime; //抛单时间
    
    private Date sendTime; //发货时间
    
    private String remark; //备注
    
    private String errCode;//京东错误的代码2018-1-8cheng

    private String errMsg;//京东下单错误的信息2018-1-8 cheng
    
    private Integer savePrice;//滨惠豆抵扣的钱，分为单位2018-1-16 cheng
    
    private String couponsPrice; //优惠劵抵扣的金额 zlk
    
    private Integer deliveryWay; //配送方式：0速达，1商家自配，2京东物流，3表示其他物流
    
    private Integer printCount; //打印次数 zlk

    private String printCode;//打印验证码 zlk
    
    private Date printTime;//打印销售单时间 zlk
    
    private String address; //买家地址
    
    private String tel ; //联系电话
    
    private String fullName; //买家姓名
    
    private Integer refundStatus; //退款状态
    
    private String refuseReason;  //拒绝退款理由
    
    private String adminName; //审核人名称
    
    private Date disposeTime; //审核时间
    
    private Date applyTime; //申请时间
    
    private String note; //退款说明
    
    private String reason; //退款原因
    
    private Integer payWay; //支付方式
    
    private Integer payStatus; //支付状态：1待付款（未支付）,2已付款（已支付）
    
    private String shopAddress; //商家地址
    
    private Integer deliveryTime ; //配送所需时间
    
    private double shopDistance; //配送员到商家距离单位km
    
    private double shoptoUserDistance; //商家到用户距离单位km
    
    private String[] imageStr; // 退款说明图
    
    private double shopLng; // 商家经度
    
    private double shopLat; // 商家纬度
    
    private double userLng; // 用户经度
    
    private double userLat; // 用户纬度
    
	private List<OrderSku> orderSku ;//orderSku的一个pojo 2017-10-27星期五由cheng添加,不需要在数据库中存在
    private Order order;
    private double realDeliveryPrice;//2017-11-3cheng添加
    private OrderSimple orderSimple;//2017-10-1cheng添加
    private Integer goodsNumber;//2017-11-2cheng添加
    private double realOrderPrice;//2017-11-2cheng添加
    private String goodsName;//2017-11-06cheng添加
    private double allPrice;
    private double realgDeliveryPrice;
	private String mystatus;// 2017-11-07cheng添加
	
	private Integer teamStatus;//-1拼单失败,0拼团中,1拼团成功,2该单不是拼团单
	private String teamStatusName;
	private Integer waitNum;
	
	private boolean groupOrder; //是否团购单
	
	private Integer groupType; //拼团状态
	
	private String jdorderid;//默认是0,如果是京东则京东id
	
	private String goodName;//商品名称 18-1-5 曾添加
	
	private String skuNum; //商品号 18-1-5 曾添加
	
	private Integer goodNum;//商品数量 18-1-5 曾添加
	
	private String  printState; //打印后的状态  zlk
	
	private String  taobaoTemplate; //淘宝的打印模板zlk
	
	private String expressName;  //快递公司zlk

    private String expressNo;    //快递单号zlk
	
    private JSONObject logistics;    //其他物流 配送信息zlk
    
    private List<Track> orderTrack;    //京东物流 配送信息zlk
    
    private String jd;    //是否是京东的，0速达、商家自配, 1其他，2是京东物流zlk
    
    private String returnAddress;    //退货地址  2018.4.16 zlk
    
	private Map<String, Object> goodsSkuInfo; //订单列表处商品sku部分信息
	
	private String goodsImage; //订单列表处商品图片
	
	private Map<String, Object> userInfo; //订单列表处用户信息

	private double amount;//某商家下单总金额（单位分） xieyc{ 总金额-运费 }
	
	private String realShopOrderAmount;//某商家的商品下单金额（单位元） xieyc  { 总金额-运费-退款金额}
	
	private Integer top;//排序  xieyc
	private Track track;//cheng ,订单物流信息
	
	private String pageSize;
	
	private String currentPage;
	
	private Date startTime;
	
	private Date endTime;
	private double realSavePrice;//滨惠豆抵扣的钱，元为单位2018-1-27 cheng
	private double realCouponsPrice;//优惠卷抵扣的钱

	private String waitTime;
	
	private String token;
	
	private double amountEveryDay;
	
	private List<String> orderNoList;//用于多个订单号的查询
	private List<String> orderShopNoList;//用户多个订单号的查询
	
	

	private int year;   
	private int month;        
	private String yearMonth;   
	
	private Integer teamNum;
	private Integer robotNum;
	private Integer realNum;
	
	private double deductionPrice;//荷兰式抵扣金额
    private Integer skuPayPrice;//商品实际支付价格, 等于  sum(order_sku.sku_pay_price)  group by member_shop_id
	
    private Integer isBean; //0 不使用 1使用
	
    private Date validLen; //订单有效时间
    
    
    private Integer confirmOrder;
	
	
	public Integer getConfirmOrder() {
		return confirmOrder;
	}

	public void setConfirmOrder(Integer confirmOrder) {
		this.confirmOrder = confirmOrder;
	}
    
	public Integer getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Integer couponPrice) {
		this.couponPrice = couponPrice;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public double getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(double deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	

	

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public double getAmountEveryDay() {
		return amountEveryDay;
	}

	public void setAmountEveryDay(double amountEveryDay) {
		this.amountEveryDay = amountEveryDay;
	}

	public double getRealCouponsPrice() {
		return realCouponsPrice;
	}

	public void setRealCouponsPrice(double realCouponsPrice) {
		this.realCouponsPrice = realCouponsPrice;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	
	public String getRealShopOrderAmount() {
		return realShopOrderAmount;
	}

	public void setRealShopOrderAmount(String realShopOrderAmount) {
		this.realShopOrderAmount = realShopOrderAmount;
	}

	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
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

	private String code;//二维码 18-1-5 曾添加
	
	private String paymentNo;//第三方支付交易号(支付单号)
	
	
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public boolean isGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(boolean groupOrder) {
		this.groupOrder = groupOrder;
	}

	public double getShopLng() {
		return shopLng;
	}

	public void setShopLng(double shopLng) {
		this.shopLng = shopLng;
	}

	public double getShopLat() {
		return shopLat;
	}

	public void setShopLat(double shopLat) {
		this.shopLat = shopLat;
	}

	public double getUserLng() {
		return userLng;
	}

	public void setUserLng(double userLng) {
		this.userLng = userLng;
	}

	public double getUserLat() {
		return userLat;
	}

	public void setUserLat(double userLat) {
		this.userLat = userLat;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getCastTime() {
		return castTime;
	}

	public void setCastTime(Date castTime) {
		this.castTime = castTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String[] getImageStr() {
		return imageStr;
	}

	public void setImageStr(String[] imageStr) {
		this.imageStr = imageStr;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public double getShopDistance() {
		return shopDistance;
	}

	public void setShopDistance(double shopDistance) {
		this.shopDistance = shopDistance;
	}

	public double getShoptoUserDistance() {
		return shoptoUserDistance;
	}

	public void setShoptoUserDistance(double shoptoUserDistance) {
		this.shoptoUserDistance = shoptoUserDistance;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public Date getDisposeTime() {
		return disposeTime;
	}

	public void setDisposeTime(Date disposeTime) {
		this.disposeTime = disposeTime;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
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

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getIsShopCheckout() {
		return isShopCheckout;
	}

	public void setIsShopCheckout(Integer isShopCheckout) {
		this.isShopCheckout = isShopCheckout;
	}


	
	

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(String skuNum) {
		this.skuNum = skuNum;
	}
    
	public Integer getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
	
	
	
	
	public String getMystatus() {
		return mystatus;
	}

	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	
	public double getRealOrderPrice() {
		return realOrderPrice;
	}

	public void setRealOrderPrice(double realOrderPrice) {
		this.realOrderPrice = realOrderPrice;
	}


	private List<OrderShop> orderShopList;

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Integer deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public Integer getdState() {
		return dState;
	}

	public void setdState(Integer dState) {
		this.dState = dState;
	}

	public OrderSimple getOrderSimple() {
		return orderSimple;
	}

	public void setOrderSimple(OrderSimple orderSimple) {
		this.orderSimple = orderSimple;
	}

	public List<OrderShop> getOrderShopList() {
		return orderShopList;
	}

	public void setOrderShopList(List<OrderShop> orderShopList) {
		this.orderShopList = orderShopList;
	}

	private String shopName;// memberShop的一个一个字段
							// 2017-10-27星期五由cheng添加,不需要在数据库中存在，shopId对应名商铺的名称

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo == null ? null : shopOrderNo.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	public List<OrderSku> getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(List<OrderSku> orderSku) {
		this.orderSku = orderSku;
	}

	
	public double getRealDeliveryPrice() {
		return realDeliveryPrice;
	}

	public void setRealDeliveryPrice(double realDeliveryPrice) {
		this.realDeliveryPrice = realDeliveryPrice;
	}

	public double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(double allPrice) {
		this.allPrice = allPrice;
	}

	public Integer getgDeliveryPrice() {
		return gDeliveryPrice;
	}

	public void setgDeliveryPrice(Integer gDeliveryPrice) {
		this.gDeliveryPrice = gDeliveryPrice;
	}

	public double getRealgDeliveryPrice() {
		return realgDeliveryPrice;
	}

	public void setRealgDeliveryPrice(double realgDeliveryPrice) {
		this.realgDeliveryPrice = realgDeliveryPrice;
	}

	public Date getReceivedtime() {
		return receivedtime;
	}

	public void setReceivedtime(Date receivedtime) {
		this.receivedtime = receivedtime;
	}

	public Integer getTeamStatus() {
		return teamStatus;
	}

	public void setTeamStatus(Integer teamStatus) {
		this.teamStatus = teamStatus;
	}

	public String getTeamStatusName() {
		return teamStatusName;
	}

	public void setTeamStatusName(String teamStatusName) {
		this.teamStatusName = teamStatusName;
	}

	public Integer getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}

	public String getJdorderid() {
		return jdorderid;
	}

	public void setJdorderid(String jdorderid) {
		this.jdorderid = jdorderid;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(Integer savePrice) {
		this.savePrice = savePrice;
	}

	
	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public String getPrintState() {
		return printState;
	}

	public void setPrintState(String printState) {
		this.printState = printState;
	}

	public String getTaobaoTemplate() {
		return taobaoTemplate;
	}

	public void setTaobaoTemplate(String taobaoTemplate) {
		this.taobaoTemplate = taobaoTemplate;
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

	public JSONObject getLogistics() {
		return logistics;
	}

	public void setLogistics(JSONObject logistics) {
		this.logistics = logistics;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public List<String> getOrderNoList() {
		return orderNoList;
	}

	public void setOrderNoList(List<String> orderNoList) {
		this.orderNoList = orderNoList;
	}

	public List<String> getOrderShopNoList() {
		return orderShopNoList;
	}

	public void setOrderShopNoList(List<String> orderShopNoList) {
		this.orderShopNoList = orderShopNoList;
	}

	public String getCouponsPrice() {
		return couponsPrice;
	}

	public void setCouponsPrice(String couponsPrice) {
		this.couponsPrice = couponsPrice;
	}

	public Integer getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(Integer deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public List<Track> getOrderTrack() {
		return orderTrack;
	}

	public void setOrderTrack(List<Track> orderTrack) {
		this.orderTrack = orderTrack;
	}

	public String getJd() {
		return jd;
	}

	public void setJd(String jd) {
		this.jd = jd;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public Integer getRobotNum() {
		return robotNum;
	}

	public void setRobotNum(Integer robotNum) {
		this.robotNum = robotNum;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

	public Integer getRealNum() {
		return realNum;
	}

	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public Integer getSkuPayPrice() {
		return skuPayPrice;
	}

	public void setSkuPayPrice(Integer skuPayPrice) {
		this.skuPayPrice = skuPayPrice;
	}

	public Integer getIsBean() {
		return isBean;
	}

	public void setIsBean(Integer isBean) {
		this.isBean = isBean;
	}

	public Date getValidLen() {
		return validLen;
	}

	public void setValidLen(Date validLen) {
		this.validLen = validLen;
	}

    
	
    
    
	

}