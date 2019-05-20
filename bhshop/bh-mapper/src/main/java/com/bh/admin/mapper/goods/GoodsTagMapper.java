package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsTag;


public interface GoodsTagMapper {
	int deleteByPrimaryKey(Integer id);
	
    int insert(GoodsTag record);

    int insertSelective(GoodsTag record);
    
    GoodsTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsTag record);

    int updateByPrimaryKey(GoodsTag record);
    
    
    List<GoodsTag> listPage();
}