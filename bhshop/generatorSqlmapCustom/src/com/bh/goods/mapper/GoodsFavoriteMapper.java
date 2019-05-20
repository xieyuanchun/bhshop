package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsFavorite;

public interface GoodsFavoriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsFavorite record);

    int insertSelective(GoodsFavorite record);

    GoodsFavorite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsFavorite record);

    int updateByPrimaryKey(GoodsFavorite record);
}