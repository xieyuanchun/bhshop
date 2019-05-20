package com.bh.admin.service;

import com.bh.admin.pojo.goods.MessageQSet;
import com.bh.utils.PageBean;

public interface MessageQSetService {
	
	/*设置接受消息人*/
	int insertQSet(String sendTo, String type, String remark) throws Exception;
	
	/*接收消息账号管理*/
	PageBean<MessageQSet> pageList(String currentPage, int pageSize, String type) throws Exception;
	
	/*修改接受消息人*/
	int updateQSet(String id, String sendTo, String remark, String type) throws Exception;
	
	/*删除接受消息人*/
	int deleteQSet(String id) throws Exception;
}
