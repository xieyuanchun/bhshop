package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsProperty;
import com.bh.utils.PageBean;

public interface GoodsPropertyService {
	int add(GoodsProperty entity) throws Exception;
	
	int update(GoodsProperty entity) throws Exception;
	
	GoodsProperty get(GoodsProperty entity) throws Exception;
	
	int delete(GoodsProperty entity) throws Exception;
	
	PageBean<GoodsProperty> listPage(GoodsProperty entity) throws Exception;
	
	List<GoodsProperty> selectByType(GoodsProperty entity) throws Exception;
	
	//cheng
	List<GoodsProperty> selectAllPro(GoodsProperty enGoodsProperty) throws Exception;
}
