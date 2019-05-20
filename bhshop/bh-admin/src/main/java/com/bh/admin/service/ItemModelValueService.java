package com.bh.admin.service;

import com.bh.admin.pojo.goods.ItemModelValue;

public interface ItemModelValueService {
	//插入
	int insert(ItemModelValue entity) throws Exception;
	
	//编辑
	int edit(ItemModelValue entity) throws Exception;
	
	//获取
	ItemModelValue get(ItemModelValue entity)throws Exception;
	
	//删除
	int delete (ItemModelValue entity)throws Exception;
}
