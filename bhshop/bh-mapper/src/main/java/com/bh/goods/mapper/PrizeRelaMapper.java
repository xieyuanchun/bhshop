package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.PrizeRela;

public interface PrizeRelaMapper {
    int insert(PrizeRela record);

    int insertSelective(PrizeRela record);
    
    //PrizeRela selectByPrimaryKey(Integer id);
    
    List<PrizeRela> selectByGoodsId(Integer goodsId);
    
}