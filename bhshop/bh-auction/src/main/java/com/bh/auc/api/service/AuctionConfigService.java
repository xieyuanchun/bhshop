package com.bh.auc.api.service;


import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.pojo.BhShopGoodsInfo;
import com.bh.auc.vo.AuctionHistoryVo;
import com.bh.utils.PageBean;


public interface AuctionConfigService {
	/**
	 * 拍卖师进程
	 */
	int auctioneerProcess()throws Exception;

	AuctionConfig auctionConfigDetail(int parseInt);

	int goAuction(String goodsId, String storeNum, String sysCode);

	int setAuctionConfig(AuctionConfig entity);

	PageBean<AuctionConfig> auctionGoodList(AuctionConfig entity);
	
	int upDownAuctionGoods(int parseInt, int parseInt2);

	BhShopGoodsInfo getBhShopGoodsInfo(Integer valueOf);

    PageBean<AuctionHistoryVo> auctionHistoryList(AuctionHistory entity);

    void test();
}
