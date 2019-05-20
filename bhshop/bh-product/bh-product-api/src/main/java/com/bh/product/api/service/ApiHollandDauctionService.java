package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.HollandDauction;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface ApiHollandDauctionService {
	PageBean<HollandDauction> apiDauctionList(HollandDauction entity, Member member) throws Exception;
	/**
	 * 
	 * @param mId 用户  ID
	 * @param userName 用户名
	 * @param headImg  用户头像
	 * @param auctionPrice 当前用户拍卖价格
	 * @return
	 */
	boolean dauctionNotice(Integer mId, String auctionPrice, String goodsId, String currentPeriods);
	
	
	int payDeposit(CashDeposit cashDeposit);
	
	
	void refundDeposit(int goodsId, int currentPeriods, int mId);
	
	
	void depositJsp(String orderId, String addressId);
	
	//最新一期拍卖配置信息
	Map<String, Object> dauctionDetail(HollandDauction entity, Member member);
	
	Map<String, Object> nextDauctionDetail(HollandDauction entity);
}
