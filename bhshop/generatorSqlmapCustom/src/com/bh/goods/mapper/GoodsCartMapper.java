package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsCart;

public interface GoodsCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCart record);

    int insertSelective(GoodsCart record);

    GoodsCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCart record);

    int updateByPrimaryKeyWithBLOBs(GoodsCart record);

    int updateByPrimaryKey(GoodsCart record);
}