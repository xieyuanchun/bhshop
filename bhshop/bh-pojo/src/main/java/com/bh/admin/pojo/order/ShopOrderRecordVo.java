package com.bh.admin.pojo.order;

import java.util.Date;

public class ShopOrderRecordVo {
	
	private Integer shopOrderId;
	
	private String shopOrderNo;
		
	private String goodsName;
	
	private String username;
	
	private String status;
	
	private double amount;
	
	private Date addTime;
	
	private Integer orderIdStatus;
	
	private Integer orderSkuId;
	
	private Integer paymentStatus;
	
	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Integer getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(Integer orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public Integer getOrderIdStatus() {
		return orderIdStatus;
	}

	public void setOrderIdStatus(Integer orderIdStatus) {
		this.orderIdStatus = orderIdStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}


	

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
