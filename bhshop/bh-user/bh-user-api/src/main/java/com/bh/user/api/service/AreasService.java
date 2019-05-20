package com.bh.user.api.service;

import java.util.List;

import com.bh.goods.pojo.GoAddressArea;
import com.bh.user.pojo.Areas;
import com.bh.user.pojo.MemberUserAddress;


public interface AreasService {
	
	 List<MemberUserAddress> getProvince() throws Exception;
	
	 List<MemberUserAddress> getCity(String areaId) throws Exception;

	 List<MemberUserAddress> getTown(String areaId) throws Exception;
	 
	 
	 List<Areas> getAllInfo() throws Exception;
	 
	 List<GoAddressArea> getAllJDInfo() throws Exception;
	 
	 /*递归获取省市区*/
	 List<Areas> selectByLevel(String parentId, String fz) throws Exception;
	 
}
