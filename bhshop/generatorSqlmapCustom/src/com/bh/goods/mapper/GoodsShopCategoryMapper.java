package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsShopCategory;

public interface GoodsShopCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsShopCategory record);

    int insertSelective(GoodsShopCategory record);

    GoodsShopCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsShopCategory record);

    int updateByPrimaryKey(GoodsShopCategory record);
}