package com.bh.auc.mapper;

import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.vo.AuctionApiGoods;

import java.util.List;

public interface AuctionConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuctionConfig record);

    int insertSelective(AuctionConfig record);

    AuctionConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuctionConfig record);

    int updateByPrimaryKey(AuctionConfig record);
    
    //选择所有待拍卖的商品
    List<AuctionConfig> getAllWaitAcu();

    AuctionConfig getByGoodsId(Integer goodsId);
   
    List<AuctionConfig> listPage(AuctionConfig entity);

    List<AuctionApiGoods> getAuctionApiGoodList(AuctionConfig entity);
}