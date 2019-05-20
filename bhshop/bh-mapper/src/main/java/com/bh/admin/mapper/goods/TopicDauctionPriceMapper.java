package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.TopicDauctionPrice;

public interface TopicDauctionPriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicDauctionPrice record);

    int insertSelective(TopicDauctionPrice record);

    TopicDauctionPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicDauctionPrice record);

    int updateByPrimaryKey(TopicDauctionPrice record);
    
    
    List<TopicDauctionPrice> selectByGoodsId(Integer goodsId);
    
    List<TopicDauctionPrice> selectByTgId(Integer tgId);
    
    
    
    //2018-04-08程凤云根据商品查询它的当前价格
    List<TopicDauctionPrice> selectCurrentPrice(TopicDauctionPrice price);
}