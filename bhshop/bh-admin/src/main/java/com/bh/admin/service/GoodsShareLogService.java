package com.bh.admin.service;

import com.bh.admin.pojo.goods.GoodsShareLog;
import com.bh.utils.PageBean;

public interface GoodsShareLogService {
	
	/*插入分享日志*/
	int insertLog(int mId, String rMId, String reMId, String shareUrl, String orderNo, String teamNo, String skuId, String shareType,
			String orderType, String openId) throws Exception;
	
	/*后台分享订单日志管理*/
	PageBean<GoodsShareLog> pageList(String orderNo, String goodsName, String orderType, int pageSize, String currentPage,int shopId) throws Exception;
}
