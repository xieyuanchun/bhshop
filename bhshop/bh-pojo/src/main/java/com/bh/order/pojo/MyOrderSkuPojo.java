package com.bh.order.pojo;

import java.util.Date;

public class MyOrderSkuPojo {
	private String keyOne;
	private String valueOne;
	
	private String keyTwo;
	private String valueTwo;
	
	private String keyThree;
	private String valueThree;

	private String keyFour;
	private String valueFour;
	
	private String keyFive;
	private String valueFive;
	private String reason;
	
	private String skuImage;
	
	private String afterSaleReasons;
	
	private String goodsName;
	
	private Integer skuNum;
	
	private Integer goodsId;
	
	private Integer shopId;
	
	private Integer refund;
	
	private Integer status;
	
	private double realPrice;
	
	private Integer amount;
	
	private String expressNo;
	
	private String refuseReason;
	
	private boolean showModifyButton;
	private boolean showLogisticsButton;
	private boolean showLookLogisticsButton;
	private boolean showTip;
	

	private String blackColumn;
	private String redColumn;
	private String statusName;
	private Integer orderSkuId;
	
	private String expressName;
	private String mName;
	private String mPhone;
	private String specifications;
    private String voucherImage;
    private String returnAddress;
	
	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getVoucherImage() {
		return voucherImage;
	}

	public void setVoucherImage(String voucherImage) {
		this.voucherImage = voucherImage;
	}

	private Date saveTime;
	
	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	private String validTime;
	
	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	private Date logisticsValidTime;
	public Date getLogisticsValidTime() {
		return logisticsValidTime;
	}

	public void setLogisticsValidTime(Date logisticsValidTime) {
		this.logisticsValidTime = logisticsValidTime;
	}

	public Date getRefundValidTime() {
		return refundValidTime;
	}

	public void setRefundValidTime(Date refundValidTime) {
		this.refundValidTime = refundValidTime;
	}

	private Date refundValidTime;
	
	public boolean isShowLookLogisticsButton() {
		return showLookLogisticsButton;
	}

	public void setShowLookLogisticsButton(boolean showLookLogisticsButton) {
		this.showLookLogisticsButton = showLookLogisticsButton;
	}
	
	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	private Integer refundType;
	
	
	
	public boolean isShowModifyButton() {
		return showModifyButton;
	}

	public void setShowModifyButton(boolean showModifyButton) {
		this.showModifyButton = showModifyButton;
	}

	public boolean isShowLogisticsButton() {
		return showLogisticsButton;
	}

	public void setShowLogisticsButton(boolean showLogisticsButton) {
		this.showLogisticsButton = showLogisticsButton;
	}

	public String getBlackColumn() {
		return blackColumn;
	}

	public void setBlackColumn(String blackColumn) {
		this.blackColumn = blackColumn;
	}

	public String getRedColumn() {
		return redColumn;
	}

	public void setRedColumn(String redColumn) {
		this.redColumn = redColumn;
	}

	

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getKeyOne() {
		return keyOne;
	}

	public void setKeyOne(String keyOne) {
		this.keyOne = keyOne;
	}

	public String getValueOne() {
		return valueOne;
	}

	public void setValueOne(String valueOne) {
		this.valueOne = valueOne;
	}

	public String getKeyTwo() {
		return keyTwo;
	}

	public void setKeyTwo(String keyTwo) {
		this.keyTwo = keyTwo;
	}

	public String getValueTwo() {
		return valueTwo;
	}

	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
	}

	public String getKeyThree() {
		return keyThree;
	}

	public void setKeyThree(String keyThree) {
		this.keyThree = keyThree;
	}

	public String getValueThree() {
		return valueThree;
	}

	public void setValueThree(String valueThree) {
		this.valueThree = valueThree;
	}

	public String getKeyFour() {
		return keyFour;
	}

	public void setKeyFour(String keyFour) {
		this.keyFour = keyFour;
	}

	public String getValueFour() {
		return valueFour;
	}

	public void setValueFour(String valueFour) {
		this.valueFour = valueFour;
	}

	public String getKeyFive() {
		return keyFive;
	}

	public void setKeyFive(String keyFive) {
		this.keyFive = keyFive;
	}

	public String getValueFive() {
		return valueFive;
	}

	public void setValueFive(String valueFive) {
		this.valueFive = valueFive;
	}

	public String getSkuImage() {
		return skuImage;
	}

	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}

	public String getAfterSaleReasons() {
		return afterSaleReasons;
	}

	public void setAfterSaleReasons(String afterSaleReasons) {
		this.afterSaleReasons = afterSaleReasons;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Integer getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(Integer orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public boolean isShowTip() {
		return showTip;
	}

	public void setShowTip(boolean showTip) {
		this.showTip = showTip;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
	
	
}