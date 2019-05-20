package com.bh.auc.mapper;

import com.bh.auc.pojo.AuctionRecord;

import java.util.List;

public interface AuctionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuctionRecord record);

    int insertSelective(AuctionRecord record);

    AuctionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuctionRecord record);

    int updateByPrimaryKey(AuctionRecord record);

	List<AuctionRecord> listPage(AuctionRecord findAuctionRecord);

    List<AuctionRecord> apiUserAuctionRecord(AuctionRecord entity);

    void insertBatch( List<AuctionRecord> listWaitInsert);
}