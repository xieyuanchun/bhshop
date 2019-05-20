package com.order.enums;

import com.bh.config.Contants;

public enum UnionPayInterfaceEnum {
	//公众号支付
	WXJSPAY("/bh-union-pay/union/wx/jsPay"),
	//充值支付
	WXRECHARGE("/bh-union-pay/union/wx/recharge"),
	WXMRECHARGE("/bh-union-pay/union/wx/mRecharge"),
	//种子支付
	WXSEEDPAY("/bh-union-pay/union/wx/seedPay"),
	//APP支付
	WXAPPPAY("/bh-union-pay/union/wx/appPay"),
	//退款
	WXJSREFUND("/bh-union-pay/union/wx/refund"),
	//押金支付-微信公众号
	DEPOSITPAY("/bh-union-pay/union/wx/depositPay"),
	//免审核押金-pc端微信二维码扫描支付
	promiseMoney("/bh-union-pay/union/wx/promiseMoney"),
	//微信小程序支付
	WXSMALLAPPPAY("/bh-union-pay/union/wx/wxSmallApp"),
	//免审核押金-微信公众号
	WXPROMISEMONEY("/bh-union-pay/union/wx/promiseMoney");
			
	
//	private String baseUrl = "http://192.168.0.100";
	private String baseUrl = Contants.BIN_HUI_URL;
	private String method;
	private UnionPayInterfaceEnum(String method) {
		this.method = baseUrl+method;
	}
	public String getMethod() {
		//方法名  
		return method;
	}
}
