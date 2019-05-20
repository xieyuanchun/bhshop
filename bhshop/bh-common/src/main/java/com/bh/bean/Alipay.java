package com.bh.bean;

import java.io.Serializable;
/**
 * 支付宝实体类
 * @author xxj
 *
 */
public class Alipay implements Serializable{
	//
	private static final long serialVersionUID = 1L;
	//总金额
	private String totalAmount;
	//标题
	private String subject;
	//描述
	private String body;
	//外部交易号
	private String outTradeNo;
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	

}
