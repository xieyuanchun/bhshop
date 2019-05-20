package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.GoAddressArea;
import com.bh.jd.bean.area.CheckArea;

public interface JDAreaService {

	
	List<GoAddressArea> selectGoAddressByParams(GoAddressArea addressArea);
	
	CheckArea checkArea(String provinceId,String cityId,String countyId,String townId);

	List<GoAddressArea> mSelectGoAddressByParams(GoAddressArea area);
}
