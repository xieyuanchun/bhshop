package com.bh.user.api.enums;

import com.bh.config.Contants;

public enum SwiftpassPayInterfaceEnum {
	//公众号支付
	WXJSPAY("/bh-swiftpass-pay/swiftpass/wx/jsPay"),
	//充值支付
	WXRECHARGE("/bh-swiftpass-pay/swiftpass/wx/recharge"),
	//种子支付
	WXSEEDPAY("/bh-swiftpass-pay/swiftpass/wx/seedPay"),
	//APP支付
	WXAPPPAY("/bh-swiftpass-pay/swiftpass/wx/appPay"),
	//退款
	WXJSREFUND("/bh-swiftpass-pay/swiftpass/wx/refund"),
	//押金支付
	DEPOSITPAY("/bh-swiftpass-pay/swiftpass/wx/depositPay");
		
	
	//private String baseUrl = "http://192.168.1.44:8080";
	private String baseUrl = Contants.BIN_HUI_URL;
	private String method;
	private SwiftpassPayInterfaceEnum(String method) {
		this.method = baseUrl+method;
	}
	public String getMethod() {
		//方法名  
		return method;
	}
}
