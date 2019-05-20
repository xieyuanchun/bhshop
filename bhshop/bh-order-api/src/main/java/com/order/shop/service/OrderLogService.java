package com.order.shop.service;

import com.bh.order.pojo.OrderLog;
import com.bh.utils.PageBean;

public interface OrderLogService {
	
	/*获取日志分页列表*/
	PageBean<OrderLog> PageAll(String orderNo, String action, String currentPage, String pageSize, int shopId) throws Exception;
	public int insert(OrderLog record);
	public int insertSelective(OrderLog record);
}
