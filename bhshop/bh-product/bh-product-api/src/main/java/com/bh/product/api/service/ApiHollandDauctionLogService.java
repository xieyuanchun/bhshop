package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.order.pojo.Order;
import com.bh.utils.PageBean;

public interface ApiHollandDauctionLogService {
	//用户竞拍操作
	int insert(HollandDauctionLog entity) throws Exception;
	//竞拍记录
	PageBean<HollandDauctionLog>  apiLogList(HollandDauctionLog entity) throws Exception;

	
	Order rendAuctionOrder(int price,int mId,int goodsId);

	//24小时未支付，调度使用
	int updateStatus() throws Exception;
	
	//出价30秒倒计时，调度使用
	int updateSecondStatus() throws Exception;
	
	//去竞拍（first调）
	int insertFirst(HollandDauctionLog entity) throws Exception;
	
	//用户竞拍历史记录
	List<Map<String, Object>>  apiUserLogList(HollandDauctionLog entity) throws Exception;
	
	//更新流拍记录
	int updateLostTimeRecord()throws Exception;
}
