package com.bh.admin.service;


import com.bh.admin.pojo.goods.AuctionGoodsInfo;
import com.bh.admin.pojo.goods.HollandDauction;
import com.bh.admin.pojo.goods.HollandDauctionLog;
import com.bh.utils.PageBean;


public interface HollandDauctionService {

	int saveOrUpdate(HollandDauction entity);

	HollandDauction hollandDauctionDetail(int parseInt);

	PageBean<HollandDauctionLog> hollandDauctionLogList(HollandDauctionLog entity);

	AuctionGoodsInfo getBhShopGoodsInfo(Integer valueOf);
}
