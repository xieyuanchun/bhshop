package com.order.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author scj
 *
 */
public enum SendOrderTypeEnum {
	ALL_TYPE(1,"全部类型",1),
	SHORT_RANGE(2,"近距离",0),
	FAR_RANGE(3,"远距离",0),
	HIGH_PRICE(4,"高价单",0),
	LOW_PRICE(5,"低价单",0);
	private SendOrderTypeEnum(Integer code, String name, Integer isDefult) {
			this.code = code;
			this.name = name;
			this.isDefult = isDefult;
	}
	private Integer code;
	private String name;
	private Integer isDefult;
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsDefult() {
		return isDefult;
	}

	public void setIsDefult(Integer isDefult) {
		this.isDefult = isDefult;
	}

	public static List<Map<String,Object>> getTypeList(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("code", ALL_TYPE.getCode());
		map1.put("name", ALL_TYPE.getName());
		map1.put("isDefult", ALL_TYPE.getIsDefult());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", SHORT_RANGE.getCode());
		map2.put("name", SHORT_RANGE.getName());
		map2.put("isDefult", SHORT_RANGE.getIsDefult());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", FAR_RANGE.getCode());
		map3.put("name", FAR_RANGE.getName());
		map3.put("isDefult", FAR_RANGE.getIsDefult());
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("code", HIGH_PRICE.getCode());
		map4.put("name", HIGH_PRICE.getName());
		map4.put("isDefult", HIGH_PRICE.getIsDefult());
		
		Map<String,Object> map5 = new HashMap<>();
		map5.put("code", LOW_PRICE.getCode());
		map5.put("name", LOW_PRICE.getName());
		map5.put("isDefult", LOW_PRICE.getIsDefult());
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		return list;
	}
 

}

	
