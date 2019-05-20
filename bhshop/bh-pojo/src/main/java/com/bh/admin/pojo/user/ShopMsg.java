package com.bh.admin.pojo.user;

public class ShopMsg {
	private Integer step;
	
	private String errNote;
	
	//0不需要支付pos押金，1需要支付pos押金
	private Integer isNeedPay;

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getErrNote() {
		return errNote;
	}

	public void setErrNote(String errNote) {
		this.errNote = errNote;
	}

	public Integer getIsNeedPay() {
		return isNeedPay;
	}

	public void setIsNeedPay(Integer isNeedPay) {
		this.isNeedPay = isNeedPay;
	}

	
	
}
