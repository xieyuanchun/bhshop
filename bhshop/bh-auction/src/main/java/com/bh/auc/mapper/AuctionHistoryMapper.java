package com.bh.auc.mapper;

import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.vo.AuctionHistoryVo;

import java.util.List;

public interface AuctionHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuctionHistory record);

    int insertSelective(AuctionHistory record);

    AuctionHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuctionHistory record);

    int updateByPrimaryKey(AuctionHistory record);

	AuctionHistory getByConfIdAndCurrentPeriods(Integer id, Integer currentPeriods);

	List<AuctionHistory> getNoRendOrderBargainRecord();

    AuctionHistory getByMidAndCurrentPeriods(Integer id, Integer currentPeriods, Integer integer);

    List<AuctionHistoryVo> listPage(AuctionHistory entity);
}