package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.GoodsSaleP;

public interface GoodsSalePMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSaleP record);

    int insertSelective(GoodsSaleP record);

    GoodsSaleP selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsSaleP record);

    int updateByPrimaryKey(GoodsSaleP record);
    
    List<GoodsSaleP> selectAll(GoodsSaleP g);
}