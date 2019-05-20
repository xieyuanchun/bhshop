package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.TopicDauction;
import com.bh.goods.pojo.TopicDauctionPrice;

public interface TopicDauctionService {
	//添加
	int insert (TopicDauction entity) throws Exception;
	
	//修改
	int edit (TopicDauction entity) throws Exception;
	
	//删除
	int delete (TopicDauction entity) throws Exception;
	
	//获取
	TopicDauction get (TopicDauction entity) throws Exception;
	
	//定时检查变更商品价格
	int changeGoodsPrice () throws Exception;
	
	List<TopicDauctionPrice> getPointAndPrice(int goodsId) throws Exception;
	
	Map<String, Object> dauctionDetail(int goodsId) throws Exception;
}
