package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.GoodsCategroyJd;

public interface GoodsCategroyJdMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(GoodsCategroyJd record);

    int insertSelective(GoodsCategroyJd record);

    GoodsCategroyJd selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(GoodsCategroyJd record);

    int updateByPrimaryKey(GoodsCategroyJd record);
}