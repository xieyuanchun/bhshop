package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.TopicCate;
import com.bh.utils.PageBean;

public interface TopicCateService {
	
	/*新增专题分类*/
	int add(TopicCate entity) throws Exception;
	
	/*修改专题分类*/
	int update(TopicCate entity) throws Exception;
	
	/*删除专题分类*/
	int delete(TopicCate entity) throws Exception;
	
	/*获取专题分类*/
	TopicCate get(TopicCate entity) throws Exception;
	
	/*专题分类列表*/
	PageBean<TopicCate> listPage(TopicCate entity) throws Exception;
	
	/*新增时加载所有分类*/
	List<TopicCate> listAll() throws Exception;
	
	/*专题活动添加所有分类*/
	List<TopicCate> getLinkedAll() throws Exception;
}
