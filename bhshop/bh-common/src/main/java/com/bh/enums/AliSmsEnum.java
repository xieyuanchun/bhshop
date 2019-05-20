package com.bh.enums;

public enum AliSmsEnum {
	PHONE_VALI("SMS_123925007","手机号验证"),
	BUSI_VALI("SMS_123885010","商户资料审核"),
	JD_PRICE_NOTICE("","京东商品价格变动通知");
	
	private AliSmsEnum(String smsTemplateCode,String remark){
		this.smsTemplateCode = smsTemplateCode;
		this.remark = remark;
	}
    private String smsTemplateCode;
    private String remark;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSmsTemplateCode() {
		return smsTemplateCode;
	}
	public void setSmsTemplateCode(String smsTemplateCode) {
		this.smsTemplateCode = smsTemplateCode;
	}
}
