package com.bh.product.api.enums;

import com.bh.config.Contants;

public enum UnionPayInterfaceEnum {
	WXJSPAY("/bh-union-pay/union/wx/jsPay"),
	WXRECHARGE("/bh-union-pay/union/wx/recharge"),
	WXJSREFUND("/bh-union-pay/union/wx/refund");
//	private String baseUrl = "http://192.168.0.100";
	private String baseUrl = Contants.BIN_HUI_URL;
//	private String baseUrl = "http://192.168.0.10";
	private String method;
	private UnionPayInterfaceEnum(String method) {
		this.method = baseUrl+method;
	}
	public String getMethod() {
		//方法名  
		return method;
	}
}
