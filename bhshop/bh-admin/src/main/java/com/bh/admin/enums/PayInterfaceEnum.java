package com.bh.admin.enums;

import com.bh.config.Contants;

public enum PayInterfaceEnum {
	WXJSPAY("/pay-java/jsapi"),
	WXAPPPAY("/pay-java/wxAppPay"),
	TOWXQRPAY("/pay-java/toWxQrPay"),
	TOALIPAY("/pay-java/toAliPay"),
	TOREFUND("/pay-java/refund");
	private String baseUrl = Contants.BIN_HUI_URL;
//	private String baseUrl = "http://192.168.0.10";
	private String method;
	private PayInterfaceEnum(String method) {
		this.method = baseUrl+method;
	}
	public String getMethod() {
		//方法名  
		return method;
	}
}
