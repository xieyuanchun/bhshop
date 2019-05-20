package com.bh.user.api.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CardTypeEnum {
	//法定代表人证件类型：0身份证，1户口本，2军官证、3护照
	CODE0(0,"身份证"),
	CODE1(1,"户口本"),
	CODE2(2,"军官证"),
	CODE3(3,"护照");
	
	
	
	private CardTypeEnum(Integer cardType, String cardTypeName) {
		this.cardType = cardType;
		this.cardTypeName = cardTypeName;
	}

	private Integer cardType;
	private String cardTypeName;
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	
	public static List<Map<String,Object>> getReasonList(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("cardType", CODE0.getCardType());
		map1.put("cardTypeName", CODE0.getCardTypeName());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("cardType", CODE1.getCardType());
		map2.put("cardTypeName",CODE1.getCardTypeName());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("cardType", CODE2.getCardType());
		map3.put("cardTypeName", CODE2.getCardTypeName());
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("cardType", CODE3.getCardType());
		map4.put("cardTypeName", CODE3.getCardTypeName());
		
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		return list;
	}
	
}
