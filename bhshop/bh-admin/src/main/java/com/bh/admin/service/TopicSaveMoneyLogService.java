package com.bh.admin.service;

import com.bh.admin.pojo.goods.TopicSavemoneyLog;
import com.bh.admin.pojo.user.Member;
import com.bh.utils.PageBean;

public interface TopicSaveMoneyLogService {

	    //添加
	   Integer add(TopicSavemoneyLog entity, Member member);
	   //列表
	   PageBean<TopicSavemoneyLog> listPage(TopicSavemoneyLog entity);
	
	
}
