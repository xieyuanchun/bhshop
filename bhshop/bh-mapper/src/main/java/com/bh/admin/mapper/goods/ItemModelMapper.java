package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.ItemModel;

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