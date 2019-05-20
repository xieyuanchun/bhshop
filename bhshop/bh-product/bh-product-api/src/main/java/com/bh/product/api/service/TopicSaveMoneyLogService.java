package com.bh.product.api.service;

import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface TopicSaveMoneyLogService {

	    //添加
	   Integer add(TopicSavemoneyLog entity, Member member);
	   //列表
	   PageBean<TopicSavemoneyLog> listPage(TopicSavemoneyLog entity);
	
	
}
