package com.bh.product.api.service;

import com.bh.goods.pojo.ItemModel;
import com.bh.utils.PageBean;

public interface ItemModelService {
	//插入
	int insert(ItemModel entity) throws Exception;
	
	//编辑
	int edit(ItemModel entity) throws Exception;
	
	//获取
	ItemModel get(ItemModel entity)throws Exception;
	
	//删除
	int delete (ItemModel entity)throws Exception;
	
	//列表
	PageBean<ItemModel> listPage (ItemModel entity)throws Exception;
	
	//根据分类加载模型
	ItemModel getByCatId(ItemModel entity)throws Exception;
}
