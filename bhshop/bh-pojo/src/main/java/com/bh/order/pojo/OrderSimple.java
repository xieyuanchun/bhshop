package com.bh.order.pojo;

import java.util.Date;


import com.bh.user.pojo.MemberUserAddress;

public class OrderSimple {
    private Integer id;

    private Integer paymentId;//支付类型 1货到付款 2 支付宝支付  3微信支付
    

    private Integer paymentStatus;//支付状态 :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败 

    private Date addtime;//下单时间
    
    private double realOrderPrice; //订单总价转换成“元”，用于页面
    private double g_deliveryPrice;//物流价2017-11-13cheng添加
    private double realSavePrice;//滨惠豆抵扣的价格
    
    private Integer orderPrice;//订单总金额单位分
 
    private MemberUserAddress memberUserAddress;//2017-10-18星期三
    private String paymentIdName;
    private String paymentStatusName;
    private Integer fz;//程凤云2018-4-12增加该字段用户是否拍卖单的判断(不为空且=5)前端需求

	public String getPaymentIdName() {
		 switch (paymentIdName) {
			case "1":  return "货到付款 ";
			case "2":  return "支付宝支付";
			case "3":  return "微信支付";
			case "4":  return "免支付";
			default: return "";
		}
	}

	public void setPaymentIdName(String paymentIdName) {
		this.paymentIdName = paymentIdName;
	}

	public String getPaymentStatusName() {
		  switch (paymentStatusName) {
		case "0":  return "货到付款";
		case "1":  return "待付款";
		case "2":  return "已付款";
		case "3":  return "待退款";
		case "4":  return "退款成功";
		case "5":  return "退款失败 ";
		case "6":  return "公司转帐";
		case "7":  return "邮局汇款";
		default: return "";
	}
	}

	public void setPaymentStatusName(String paymentStatusName) {
		
		this.paymentStatusName = paymentStatusName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	

	public double getRealOrderPrice() {
		return realOrderPrice;
	}

	public void setRealOrderPrice(double realOrderPrice) {
		this.realOrderPrice = realOrderPrice;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public MemberUserAddress getMemberUserAddress() {
		return memberUserAddress;
	}

	public void setMemberUserAddress(MemberUserAddress memberUserAddress) {
		this.memberUserAddress = memberUserAddress;
	}

	public double getG_deliveryPrice() {
		return g_deliveryPrice;
	}

	public void setG_deliveryPrice(double g_deliveryPrice) {
		this.g_deliveryPrice = g_deliveryPrice;
	}

	public double getRealSavePrice() {
		return realSavePrice;
	}

	public void setRealSavePrice(double realSavePrice) {
		this.realSavePrice = realSavePrice;
	}

	public Integer getFz() {
		return fz;
	}

	public void setFz(Integer fz) {
		this.fz = fz;
	}
	
    
    

}
