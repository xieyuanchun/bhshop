package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsAttr;

public interface GoodsAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAttr record);

    int insertSelective(GoodsAttr record);

    GoodsAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAttr record);

    int updateByPrimaryKey(GoodsAttr record);
}