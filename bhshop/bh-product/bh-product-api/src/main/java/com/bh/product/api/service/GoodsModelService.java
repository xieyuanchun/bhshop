package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsImage;
import com.bh.goods.pojo.GoodsModel;
import com.bh.goods.pojo.GoodsModelAttr;
import com.bh.utils.PageBean;

public interface GoodsModelService {
	
	/*用户端查询条件标签*/
	List<GoodsModelAttr> selectBycatId(String catid) throws Exception;
	
	/*商家后台获取所有商品属性*/
	List<GoodsModel> selectAllModel(String catid) throws Exception;
	
	/*平台后台商品属性列表*/
	PageBean<GoodsModel> selectAllList(String currentPage, String pageSize, String name, String catid) throws Exception;
	
	/*商品模型的添加*/
	String insertModel(String name, String catId) throws Exception;
	
	/*商品模型的删除*/
	int deleteModel(String id) throws Exception;
	
	/*商品模型的修改*/
	String editModel(String id, String name, String catId) throws Exception;
	
	
}
