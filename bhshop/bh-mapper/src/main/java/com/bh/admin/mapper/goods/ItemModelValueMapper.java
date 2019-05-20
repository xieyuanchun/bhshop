package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.ItemModelValue;

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