package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.ItemModel;

public interface ItemModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemModel record);

    int insertSelective(ItemModel record);

    ItemModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemModel record);

    int updateByPrimaryKeyWithBLOBs(ItemModel record);

    int updateByPrimaryKey(ItemModel record);
    
    
    List<ItemModel> listPage(ItemModel record);
    
    List<ItemModel> getByCatId(ItemModel record);
} 