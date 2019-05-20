package com.bh.product.api.service;

import java.util.Map;
import com.bh.goods.pojo.AuctionGoodsInfo;
import com.bh.goods.pojo.CashDeposit;
import com.bh.order.pojo.Order;

public interface AuctionService {


	void refundDeposit(int goodsId, int currentPeriods, int mId);

	void depositJsp(String orderId, String addressId);

	int payDeposit(CashDeposit cashDeposit);

	Map<String, Object> isPayDeposit(CashDeposit cashDeposit);

	AuctionGoodsInfo getBhShopGoodsInfo(Integer valueOf);

	Map<String, Object> isCollect(String goodsIds, Integer mId);

	Order rendAucOrderAndRefundDeposit(int auctionPrice, int mId, int goodsId,int currentPeriods);
	
}
