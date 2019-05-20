package com.bh.admin.service;

import com.bh.admin.pojo.goods.TopicDauctionLog;
import com.bh.utils.PageBean;

public interface TopicDauctionLogService {
	
	int add(TopicDauctionLog entity) throws Exception;
	
	int edit(TopicDauctionLog entity) throws Exception;
	
	int delete(TopicDauctionLog entity) throws Exception;
	
	TopicDauctionLog get(TopicDauctionLog entity) throws Exception;
	
	PageBean<TopicDauctionLog> listPage (TopicDauctionLog entity) throws Exception;
}
