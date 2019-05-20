package com.bh.product.api.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author scj
 *
 */
public enum LocationEnum {
	HOMEPAGE(1,"首页"),
	CITY(2,"同城"),
	HAITAO(3,"海淘"),
	SECKILL(4,"秒杀"),
	CLEARSALE(5,"清仓");
	private Integer code;
	private String location;
    
	private LocationEnum(Integer code, String location) {
		this.code = code;
		this.location = location;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static List<Map<String,Object>> getLocationList(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("code", HOMEPAGE.getCode());
		map1.put("location", HOMEPAGE.getLocation());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", CITY.getCode());
		map2.put("location", CITY.getLocation());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", HAITAO.getCode());
		map3.put("location", HAITAO.getLocation());
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("code", SECKILL.getCode());
		map4.put("location", SECKILL.getLocation());
		
		Map<String,Object> map5 = new HashMap<>();
		map5.put("code", CLEARSALE.getCode());
		map5.put("location", CLEARSALE.getLocation());
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		return list;
	}
}

	
