package com.bh.admin.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author xxj
 *
 */
public enum RefundReasonEnum {
	REFUND_MENEY(1,"退款"),
	REFUND_GOODS(2,"换货"),
	REFUND_MENEY_GOODS(3,"退款退货"),
	REFUND_TEAM(4,"拼单失败");
	private Integer code;
	private String reason;
    
	private RefundReasonEnum(Integer code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	public static List<Map<String,Object>> getRefundReasonList(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("code", REFUND_MENEY.getCode());
		map1.put("reason", REFUND_MENEY.getReason());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", REFUND_GOODS.getCode());
		map2.put("reason", REFUND_GOODS.getReason());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", REFUND_MENEY_GOODS.getCode());
		map3.put("reason", REFUND_MENEY_GOODS.getReason());
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("code", REFUND_TEAM.getCode());
		map4.put("reason", REFUND_TEAM.getReason());
		list.add(map1);
		list.add(map2);
		list.add(map3);
	
		return list;
	}
}

	
