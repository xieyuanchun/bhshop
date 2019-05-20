package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.ItemModelValue;

public interface ItemModelValueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemModelValue record);

    int insertSelective(ItemModelValue record);

    ItemModelValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemModelValue record);

    int updateByPrimaryKeyWithBLOBs(ItemModelValue record);

    int updateByPrimaryKey(ItemModelValue record);
    
    
    List<ItemModelValue> selectByGoodsId(ItemModelValue record);
}