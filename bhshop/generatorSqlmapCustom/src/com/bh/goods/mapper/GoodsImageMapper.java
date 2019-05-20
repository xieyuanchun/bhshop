package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsImage;

public interface GoodsImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsImage record);

    int insertSelective(GoodsImage record);

    GoodsImage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsImage record);

    int updateByPrimaryKey(GoodsImage record);
}