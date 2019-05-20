package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsDesc;

public interface GoodsDescMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insertGoodsDesc(GoodsDesc record);

    int insertSelective(GoodsDesc record);

    GoodsDesc selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(GoodsDesc record);

    int updateByPrimaryKeyWithBLOBs(GoodsDesc record);
    
    
    
    int deleteByGoodsId(Integer goodsId);
    
    GoodsDesc selectByGoodsIdAndIsPc(Integer goodsId, Integer isPc);
    
    int updateByGoodsIdAndIsPc(GoodsDesc record);
    
}