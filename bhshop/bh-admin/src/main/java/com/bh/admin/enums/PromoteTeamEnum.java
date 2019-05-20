package com.bh.admin.enums;

import com.bh.config.Contants;

public enum PromoteTeamEnum {
	//根据订单号促团
	promoteTeamByOrderNo("/bh-order-api/order/promoteTeamByOrderNo"),
	//根据商品ID促团
	promoteTeamByGoodsId("/bh-order-api/order/promoteTeamByGoodsId");
	private String baseUrl = Contants.BIN_HUI_URL;
	private String method;
	private PromoteTeamEnum(String method) {
		this.method = baseUrl+method;
	}
	public String getMethod() {
		//方法名  
		return method;
	}
}
