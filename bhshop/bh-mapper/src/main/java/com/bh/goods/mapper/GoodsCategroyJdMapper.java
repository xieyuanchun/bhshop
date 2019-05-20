package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsCategroyJd;

public interface GoodsCategroyJdMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(GoodsCategroyJd record);

    int insertSelective(GoodsCategroyJd record);

    GoodsCategroyJd selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(GoodsCategroyJd record);

    int updateByPrimaryKey(GoodsCategroyJd record);
}