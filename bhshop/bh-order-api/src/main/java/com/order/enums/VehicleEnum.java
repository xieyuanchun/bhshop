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
public enum VehicleEnum {
	BIKE(1,"自行车",1),
	TRICYCLE(2,"三轮车",0),
	ELECTROMBILE(3,"电动车",0),
	BIG_ELECTROMBILE(4,"大踏板电动车",0),
	MOTORBIKE(5,"摩托车",0),
	SEDAN_CAR(6,"小汽车",0),
	TRUCKS(7,"小货车",0);
	private VehicleEnum(Integer code, String name, Integer isDefult) {
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

	public static List<Map<String,Object>> getVehicleList(){
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("code", BIKE.getCode());
		map1.put("name", BIKE.getName());
		map1.put("isDefult", BIKE.getIsDefult());
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("code", TRICYCLE.getCode());
		map2.put("name", TRICYCLE.getName());
		map2.put("isDefult", TRICYCLE.getIsDefult());
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("code", ELECTROMBILE.getCode());
		map3.put("name", ELECTROMBILE.getName());
		map3.put("isDefult", ELECTROMBILE.getIsDefult());
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("code", BIG_ELECTROMBILE.getCode());
		map4.put("name", BIG_ELECTROMBILE.getName());
		map4.put("isDefult", BIG_ELECTROMBILE.getIsDefult());
		
		Map<String,Object> map5 = new HashMap<>();
		map5.put("code", MOTORBIKE.getCode());
		map5.put("name", MOTORBIKE.getName());
		map5.put("isDefult", MOTORBIKE.getIsDefult());
		
		Map<String,Object> map6 = new HashMap<>();
		map6.put("code", SEDAN_CAR.getCode());
		map6.put("name", SEDAN_CAR.getName());
		map6.put("isDefult", SEDAN_CAR.getIsDefult());
		
		Map<String,Object> map7 = new HashMap<>();
		map7.put("code", TRUCKS.getCode());
		map7.put("name", TRUCKS.getName());
		map7.put("isDefult", TRUCKS.getIsDefult());
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		
		return list;
	}
 

}

	
