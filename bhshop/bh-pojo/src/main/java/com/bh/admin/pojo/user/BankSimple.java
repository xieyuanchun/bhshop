package com.bh.admin.pojo.user;

public class BankSimple {
	private String bankCardNo;//银行卡号
	
	private String bankName;//银行名称
	
	private String bankKind;//银行种类 如：中国银行信用卡
	 
	private String bankType;//卡类型 如:信用卡

	private String bankCode;//卡的编码:如 CMB
	
	private String img;

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	
	
	
}
