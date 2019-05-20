package com.bh.order.pojo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyOrderShopDetail {
	private Integer id;
	
	private String mystatus;
	private Integer status;
	
	private String shopName;
	
	private boolean isAfterSale;
	
	
	
	
	//商品总价
	private String allPrice;
	//滨惠豆抵扣
	private String realSavePrice;
	//优惠券抵扣
	private String couponsPrice;
	//拍卖押金抵扣
	private String deductionPrice;
	//运费
	private String realgDeliveryPrice;
	//实付款
	private String realOrderPrice;
	//订单编号
	private String shopOrderNo;
	
	
	//配送方式：速达配送
	//配送日期：工作日、双休日与假日均可送货
	//剩余：{{parseInt(waitTime/24)}}天{{waitTime%24}}时自动确认,waitTime的值是分钟
	private String waitTime;
	//退款成功:1
	//退款失败:2
	//换货申请中：3
	//换货审核通过:4
	//换货成功:5
	//换货失败:6
	//退货申请中:7
	//退款申请中:8
	//退货失败:9
	//退货退款成功:10
	//退货退款失败:11
	private Integer refundStatus;
	
	private  List<Map<Object,Object>> orderSku;
	
	private Map<String, Object> orderSimple;
	
	private String validLen;
	
	
	

	public boolean isAfterSale() {
		return isAfterSale;
	}
	public void setAfterSale(boolean isAfterSale) {
		this.isAfterSale = isAfterSale;
	}
	public List<Map<Object, Object>> getOrderSku() {
		return orderSku;
	}
	public void setOrderSku(List<Map<Object, Object>> orderSku) {
		this.orderSku = orderSku;
	}
	public String getMystatus() {
		return mystatus;
	}
	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Map<String, Object> getOrderSimple() {
		return orderSimple;
	}
	public void setOrderSimple(Map<String, Object> orderSimple) {
		this.orderSimple = orderSimple;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getAllPrice() {
		return allPrice;
	}
	public void setAllPrice(String allPrice) {
		this.allPrice = allPrice;
	}
	public String getRealSavePrice() {
		return realSavePrice;
	}
	public void setRealSavePrice(String realSavePrice) {
		this.realSavePrice = realSavePrice;
	}
	public String getCouponsPrice() {
		return couponsPrice;
	}
	public void setCouponsPrice(String couponsPrice) {
		this.couponsPrice = couponsPrice;
	}
	public String getDeductionPrice() {
		return deductionPrice;
	}
	public void setDeductionPrice(String deductionPrice) {
		this.deductionPrice = deductionPrice;
	}
	public String getRealgDeliveryPrice() {
		return realgDeliveryPrice;
	}
	public void setRealgDeliveryPrice(String realgDeliveryPrice) {
		this.realgDeliveryPrice = realgDeliveryPrice;
	}
	public String getRealOrderPrice() {
		return realOrderPrice;
	}
	public void setRealOrderPrice(String realOrderPrice) {
		this.realOrderPrice = realOrderPrice;
	}
	public String getShopOrderNo() {
		return shopOrderNo;
	}
	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public Integer getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValidLen() {
		return validLen;
	}
	public void setValidLen(String validLen) {
		this.validLen = validLen;
	}
    
	
}
