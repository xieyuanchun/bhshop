package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsModelAttr;

public interface GoodsModelAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsModelAttr record);

    int insertSelective(GoodsModelAttr record);

    GoodsModelAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsModelAttr record);

    int updateByPrimaryKeyWithBLOBs(GoodsModelAttr record);

    int updateByPrimaryKey(GoodsModelAttr record);
}