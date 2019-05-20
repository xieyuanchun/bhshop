package com.bh.goods.mapper;

import com.bh.goods.pojo.AuctionConfig;

public interface AuctionConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuctionConfig record);

    int insertSelective(AuctionConfig record);

    AuctionConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuctionConfig record);

    int updateByPrimaryKey(AuctionConfig record);

	AuctionConfig getByGoodsId(Integer goodsId);
    
    
}