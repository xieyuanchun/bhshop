package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.jd.api.JDAreaApi;
import com.bh.jd.bean.area.CheckArea;
import com.bh.product.api.service.JDAreaService;

@Service
public class JDAreaServiceImpl implements JDAreaService{
	
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
	
	public List<GoAddressArea> selectGoAddressByParams(GoAddressArea record){
		List<GoAddressArea> list = new ArrayList<>();
		list = goAddressAreaMapper.selectByParams(record);
		return list;
	}
	
	public CheckArea checkArea(String provinceId,String cityId,String countyId,String townId){
		CheckArea checkArea = JDAreaApi.checkArea(provinceId, cityId, countyId, townId);
		return checkArea;
	}
}
