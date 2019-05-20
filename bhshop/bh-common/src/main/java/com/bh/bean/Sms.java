package com.bh.bean;
import java.io.Serializable;
public class Sms implements Serializable{
	private static final long serialVersionUID = 1L;
	private String phoneNo; //电话号码
	private String smsContent;//短信内容
	private String goodsName;//商品名字
	private String shopName;//店铺名字
	private String msg;//下架理由
	private String orderShopNo;//打印的商家订单号
	private String verification;//验证码
	
	public String getOrderShopNo() {
		return orderShopNo;
	}
	public void setOrderShopNo(String orderShopNo) {
		this.orderShopNo = orderShopNo;
	}
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
}
