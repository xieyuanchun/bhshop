package com.bh.admin.pojo.order;

import java.util.Date;

public class OrderSku {
	private Integer id;

	private Integer orderId;

	private Integer orderShopId;

	private Integer goodsId;

	private String goodsName;

	private Integer skuId;

	private String skuNo;

	private String skuImage;

	private Integer skuNum;

	private Integer skuMarketPrice;

	private Integer skuSellPriceReal;

	private Integer skuWeight;

	private Boolean isSend;

	private Integer isRefund;

	private Integer shopId;

	private String shopName; // 店铺名称

	private String skuValue;

	private Integer sId;

	private Integer dState;

	private double realMarketPrice; // 市场价转换为‘元’的价格

	private double realSellPrice; // 支付价转换为‘元’的价格

	private Object valueObj;// 2017-11-06程凤云添加
	private String shopOrderNo;// 2017-11-16程凤云添加 商家订单号
	private double totalPrice;// 2017-11-16程凤云添加 总价
	private Date addtime;// 下单时间
	private String mystatus;// 2017-11-07cheng添加
	private Integer status;
	private Integer refund;// 是否退款0.为正常,1.退款中,2.退款完成
	private Integer storeNums;// 库存
	private Integer shopNumAmount;// 某种商品的销售总数 xieyc

	private Integer teamPrice;

	private Integer savePrice;

	private Long jdSkuNo;

	private int amount;// 某商家下单总金额（单位分） xieyc{ 总金额-运费 }

	private String realShopOrderAmount;// 某商家的商品下单金额（单位元） xieyc

	private Date startTime;

	private Date endTime;

	private Integer currentPage;

	private Integer pageSize;

	private String startDateTime;

	private String endDateTime;

	private String username;
	
	private Integer commentOwner;
    
    
	public Integer getCommentOwner() {
		return commentOwner;
	}

	public void setCommentOwner(Integer commentOwner) {
		this.commentOwner = commentOwner;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRealShopOrderAmount() {
		return realShopOrderAmount;
	}

	public void setRealShopOrderAmount(String realShopOrderAmount) {
		this.realShopOrderAmount = realShopOrderAmount;
	}

	private Long orderCatId;
	private Integer couponPrice;// 优惠卷抵扣金额（单位分）(不同券按比例计算分配到这里抵扣
	private Integer skuPayPrice;// 商品实际支付价格,等于 sku_num * sku_sell_price_real -
								// save_price - coupon_price ' AFTER
								// `coupon_price

	public Long getOrderCatId() {
		return orderCatId;
	}

	public void setOrderCatId(Long orderCatId) {
		this.orderCatId = orderCatId;
	}

	public Integer getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(Integer teamPrice) {
		this.teamPrice = teamPrice;
	}

	public Integer getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(Integer savePrice) {
		this.savePrice = savePrice;
	}

	private Integer top;// 排序 （xieyc）

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getShopNumAmount() {
		return shopNumAmount;
	}

	public void setShopNumAmount(Integer shopNumAmount) {
		this.shopNumAmount = shopNumAmount;
	}

	public String getMystatus() {
		return mystatus;
	}

	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}

	public Object getValueObj() {
		return valueObj;
	}

	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getRealMarketPrice() {
		return realMarketPrice;
	}

	public void setRealMarketPrice(double realMarketPrice) {
		this.realMarketPrice = realMarketPrice;
	}

	public double getRealSellPrice() {
		return realSellPrice;
	}

	public void setRealSellPrice(double realSellPrice) {
		this.realSellPrice = realSellPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderShopId() {
		return orderShopId;
	}

	public void setOrderShopId(Integer orderShopId) {
		this.orderShopId = orderShopId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo == null ? null : skuNo.trim();
	}

	public String getSkuImage() {
		return skuImage;
	}

	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage == null ? null : skuImage.trim();
	}

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public Integer getSkuMarketPrice() {
		return skuMarketPrice;
	}

	public void setSkuMarketPrice(Integer skuMarketPrice) {
		this.skuMarketPrice = skuMarketPrice;
	}

	public Integer getSkuSellPriceReal() {
		return skuSellPriceReal;
	}

	public void setSkuSellPriceReal(Integer skuSellPriceReal) {
		this.skuSellPriceReal = skuSellPriceReal;
	}

	public Integer getSkuWeight() {
		return skuWeight;
	}

	public void setSkuWeight(Integer skuWeight) {
		this.skuWeight = skuWeight;
	}

	public Boolean getIsSend() {
		return isSend;
	}

	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getSkuValue() {
		return skuValue;
	}

	public void setSkuValue(String skuValue) {
		this.skuValue = skuValue == null ? null : skuValue.trim();
	}

	public Integer getsId() {
		return sId;
	}

	public void setsId(Integer sId) {
		this.sId = sId;
	}

	public Integer getdState() {
		return dState;
	}

	public void setdState(Integer dState) {
		this.dState = dState;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}

	public Integer getStoreNums() {
		return storeNums;
	}

	public void setStoreNums(Integer storeNums) {
		this.storeNums = storeNums;
	}

	public Integer getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Integer couponPrice) {
		this.couponPrice = couponPrice;
	}

	public Integer getSkuPayPrice() {
		return skuPayPrice;
	}

	public void setSkuPayPrice(Integer skuPayPrice) {
		this.skuPayPrice = skuPayPrice;
	}

}