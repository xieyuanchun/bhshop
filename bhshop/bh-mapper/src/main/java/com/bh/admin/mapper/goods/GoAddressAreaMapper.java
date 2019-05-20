package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoAddressArea;


public interface GoAddressAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoAddressArea record);

    int insertSelective(GoAddressArea record);

    GoAddressArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoAddressArea record);

    int updateByPrimaryKeyWithBLOBs(GoAddressArea record);

    int updateByPrimaryKey(GoAddressArea record);
    
    
    //cheng
    List<GoAddressArea> selectByParams(GoAddressArea record);
    List<GoAddressArea> selectById(GoAddressArea go);
    
    GoAddressArea selectByName(String name);
}