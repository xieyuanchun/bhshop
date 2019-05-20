package com.order.enums;

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
	public static List<Map<String,Object>> getRefundReasonList2(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("code", REFUND_MENEY.getCode());
		map1.put("reason", REFUND_MENEY.getReason());
		list.add(map1);
		return list;
	}
	public static List<Map<String,Object>> getRefundReasonList5Or7(){
		List<Map<String,Object>> list = new ArrayList<>();
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", REFUND_GOODS.getCode());
		map2.put("reason", REFUND_GOODS.getReason());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", REFUND_MENEY_GOODS.getCode());
		map3.put("reason", REFUND_MENEY_GOODS.getReason());
		
		list.add(map2);
		list.add(map3);
		
		
		
		return list;
	}
	
	
	//范进红2018.8.24 add
	public static List<Map<String,Object>> getRefundReasonList5(){
		List<Map<String,Object>> list = new ArrayList<>();
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", REFUND_GOODS.getCode());
		map2.put("reason", REFUND_GOODS.getReason());
		
		list.add(map2);
		/*Map<String,Object> map1 = new HashMap<>();
		map1.put("1", "七天无理由换货");
		map1.put("2", "尺码拍错/不喜欢/效果差");
		map1.put("3", "质量问题");
		map1.put("4", "颜色/款式/外貌与商品描述不符");
		map1.put("5", "买家发错货");
		map1.put("code", REFUND_GOODS.getCode());
		list.add(map1);*/
		return list;
	}
	
	
	
	public static void main(String[] arg) {


	}
	
}

	
