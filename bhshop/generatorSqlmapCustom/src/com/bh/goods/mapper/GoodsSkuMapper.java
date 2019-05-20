package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsSku;

public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSku record);

    int insertSelective(GoodsSku record);

    GoodsSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsSku record);

    int updateByPrimaryKeyWithBLOBs(GoodsSku record);

    int updateByPrimaryKey(GoodsSku record);
}