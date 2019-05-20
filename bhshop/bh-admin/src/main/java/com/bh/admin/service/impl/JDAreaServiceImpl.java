package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoAddressAreaMapper;
import com.bh.admin.pojo.goods.GoAddressArea;
import com.bh.admin.service.JDAreaService;
import com.bh.jd.api.JDAreaApi;
import com.bh.jd.bean.area.CheckArea;

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

	public List<GoAddressArea> mSelectGoAddressByParams(GoAddressArea area) {
		List<GoAddressArea> list = new ArrayList<>();
		list = goAddressAreaMapper.selectByParams(area);
		return list;
	}
}
