package com.bh.admin.service;

import com.bh.admin.pojo.goods.JdGoodsNotice;
import com.bh.utils.PageBean;

public interface JdGoodsNoticeService {
	
	/*商品变更消息列表*/
	PageBean<JdGoodsNotice> pageList(JdGoodsNotice entity) throws Exception;
	
	/*已阅*/
	int changeReadStatus(String id) throws Exception; 
}
