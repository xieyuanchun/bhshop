package com.bh.jd.api;

import com.alibaba.fastjson.JSONObject;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.area.CheckArea;
import com.bh.jd.enums.GoodsEnum;
import com.bh.jd.util.JDUtil;
import com.bh.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class JDAreaApi {
	public static void main(String[] arg){
	    	
	}	
	
	//4.1 获取一级地址
	public static Map<String, String> getProvince(){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_PROVINCE.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<Map<String, String>> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<Map<String, String>>>(){}.getType());
		return jdResult.getResult();
	}
	
	//4.2 获取二级地址
	public static Map<String, String> getCity(String id){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("id",id);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_CITY.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<Map<String, String>> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<Map<String, String>>>(){}.getType());
		return jdResult.getResult();
	}
	
	//4.3 获取三级地址
	public static Map<String, String> getCounty(String id){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("id",id);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_COUNTY.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<Map<String, String>> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<Map<String, String>>>(){}.getType());
		return jdResult.getResult();
	}
	
	//4.4 获取四级地址
	public static Map<String, String> getTown(String id){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("id",id);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_TOWN.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<Map<String, String>> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<Map<String, String>>>(){}.getType());
		return jdResult.getResult();
	}
	
	//4.4 获取四级地址
	public static CheckArea checkArea(String provinceId,String cityId,String countyId,String townId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("provinceId",provinceId);
		jsonObject.put("cityId", cityId);
		jsonObject.put("countyId", countyId);
		jsonObject.put("townId", townId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.CHECK_AREA.getMethod(), jsonObject);
	
		Gson gson = new Gson();
		JdResult<CheckArea> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<CheckArea>>(){}.getType());
		return jdResult.getResult();
	}
}
