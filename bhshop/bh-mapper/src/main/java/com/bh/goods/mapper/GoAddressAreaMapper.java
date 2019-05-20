package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoAddressArea;

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
    
    GoAddressArea selectProv(String name);
    
    GoAddressArea selectCity(@Param("name")String name, @Param("parentId")Integer parentId);
    
    GoAddressArea selectArea(@Param("name1")String name1, @Param("parentId1")Integer parentId1);
}