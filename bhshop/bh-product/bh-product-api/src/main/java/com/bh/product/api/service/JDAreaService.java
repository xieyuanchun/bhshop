package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.GoAddressArea;
import com.bh.jd.bean.area.CheckArea;

public interface JDAreaService {

	
	List<GoAddressArea> selectGoAddressByParams(GoAddressArea addressArea);
	
	CheckArea checkArea(String provinceId,String cityId,String countyId,String townId);
}
