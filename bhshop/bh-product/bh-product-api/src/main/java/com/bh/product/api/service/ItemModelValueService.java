package com.bh.product.api.service;

import com.bh.goods.pojo.ItemModelValue;

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
