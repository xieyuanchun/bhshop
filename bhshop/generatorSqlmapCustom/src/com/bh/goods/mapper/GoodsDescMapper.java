package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsDesc;

public interface GoodsDescMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(GoodsDesc record);

    int insertSelective(GoodsDesc record);

    GoodsDesc selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(GoodsDesc record);

    int updateByPrimaryKeyWithBLOBs(GoodsDesc record);
}