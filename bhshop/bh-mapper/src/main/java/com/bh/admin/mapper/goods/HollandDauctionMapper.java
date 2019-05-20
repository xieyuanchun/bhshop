package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.HollandDauction;

public interface HollandDauctionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HollandDauction record);

    int insertSelective(HollandDauction record);

    HollandDauction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HollandDauction record);

    int updateByPrimaryKey(HollandDauction record);
    
    HollandDauction getListByGoodsId(Integer goodsId);
}