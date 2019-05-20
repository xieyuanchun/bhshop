package com.bh.auc.api.service;

import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionRecord;
import com.bh.auc.vo.AuctionApiGoods;

import java.util.List;
import java.util.Map;



public interface AuctionApiService {


	List<AuctionApiGoods> auctionApiGoodList(AuctionConfig entity);


	void rendAucOrderAndRefundDeposit();


	void refundDeposit();


	Map<String, Object> auctionApiGoodDetails(int parseInt, Integer id, Integer currentPeriods) throws Exception;

	List< Map<String, Object> > apiUserAuctionRecord(AuctionRecord entity);
}
