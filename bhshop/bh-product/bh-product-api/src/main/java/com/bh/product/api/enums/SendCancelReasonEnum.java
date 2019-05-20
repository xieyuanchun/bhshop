package com.bh.product.api.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author xxj
 *
 */
public enum SendCancelReasonEnum {
	UNSEND(1,"距离远无法送达"),
	BUSY(2,"工作繁忙来不及送"),
	OTHER(3,"其它原因");
	private Integer code;
	private String reason;
    
	private SendCancelReasonEnum(Integer code, String reason) {
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
		map1.put("code", UNSEND.getCode());
		map1.put("reason", UNSEND.getReason());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", BUSY.getCode());
		map2.put("reason", BUSY.getReason());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", OTHER.getCode());
		map3.put("reason", OTHER.getReason());
		list.add(map1);
		list.add(map2);
		list.add(map3);
		return list;
	}
}

	
