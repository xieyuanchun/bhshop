package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.PrizeRela;

public interface PrizeRelaMapper {
    int insert(PrizeRela record);

    int insertSelective(PrizeRela record);
    
    //PrizeRela selectByPrimaryKey(Integer id);
    
    List<PrizeRela> selectByGoodsId(Integer goodsId);
    
}