package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsBrand;

public interface GoodsBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsBrand record);

    int insertSelective(GoodsBrand record);

    GoodsBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsBrand record);

    int updateByPrimaryKey(GoodsBrand record);
}