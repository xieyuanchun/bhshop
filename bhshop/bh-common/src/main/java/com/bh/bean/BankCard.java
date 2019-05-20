package com.bh.bean;

import java.io.Serializable;

public class BankCard implements Serializable{
	private static final long serialVersionUID = 1L;
	//卡号
	private String cardNo;
	//持卡人名
	private String name;
	//持卡人身份证
	private String idNo;
	//手机号
	private String phoneNo;
	//匹配结果
	private String respMessage;
	//返回代码
	private String respCode;
	//银行名称:如中国银行
	private String bankName;
	//银行种类 如：中国银行信用卡
	private String bankKind;
	//卡类型 如:信用卡
	private String bankType;
	//卡的编码:如 CMB
	private String bankCode;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankKind() {
		return bankKind;
	}
	public void setBankKind(String bankKind) {
		this.bankKind = bankKind;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


}
