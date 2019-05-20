package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.Areas;
import com.bh.admin.pojo.user.MemberUserAddress;




public interface AreasMapper {
	
    int deleteByPrimaryKey(Integer areaId);

    int insert(Areas record);

    int insertSelective(Areas record);

    Areas selectByPrimaryKey(Integer areaId);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);
	
	//获得所有的省
	List<MemberUserAddress> getprovince();
	
	//根据省的id获取市
	List<MemberUserAddress> getcity(Integer id);
	
	List<MemberUserAddress> gettown(Integer id);
	
	//获得所有的省
		List<Areas> getprovince1();
		
		//根据省的id获取市
		List<Areas> getcity1(Integer id);
		
		List<Areas> gettown1(Integer id);
}
