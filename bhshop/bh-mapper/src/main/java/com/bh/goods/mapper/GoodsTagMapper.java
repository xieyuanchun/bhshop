package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.GoodsTag;

public interface GoodsTagMapper {
	int deleteByPrimaryKey(Integer id);
	
    int insert(GoodsTag record);

    int insertSelective(GoodsTag record);
    
    GoodsTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsTag record);

    int updateByPrimaryKey(GoodsTag record);
    
    
    List<GoodsTag> listPage();
}