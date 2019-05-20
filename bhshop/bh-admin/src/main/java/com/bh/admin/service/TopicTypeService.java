package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.TopicType;
import com.bh.utils.PageBean;

public interface TopicTypeService {
	
	//添加活动类型
	int addTopicType(TopicType topicType);
    //更新活动类型
	int updateTopicType(TopicType topicType);
	//根据条件查询活动类型列表
	PageBean<TopicType> getTopicTypeList(String currentPage, String pageSize, String id, String name, String status);
	//删除活动类型
	Integer deleteTopicType(String id);
	
	//专题活动添加获取所有活动分类
	List<TopicType> listAll(TopicType entity);
	TopicType getTopicTypeById(String id);
	
	
	
}
