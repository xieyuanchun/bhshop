package com.bh.product.api.service;

import com.bh.goods.pojo.JdGoodsNotice;
import com.bh.utils.PageBean;

public interface JdGoodsNoticeService {
	
	/*商品变更消息列表*/
	PageBean<JdGoodsNotice> pageList(String currentPage, int pageSize, String name, String type, String isRead) throws Exception;
	
	/*已阅*/
	int changeReadStatus(String id) throws Exception; 
}
