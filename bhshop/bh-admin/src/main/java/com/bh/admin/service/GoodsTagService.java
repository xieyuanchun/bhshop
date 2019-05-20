package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsTag;
import com.bh.utils.PageBean;

public interface GoodsTagService {
	int add(GoodsTag entity) throws Exception;
	
	int update(GoodsTag entity) throws Exception;
	
	GoodsTag get(GoodsTag entity) throws Exception;
	
	int delete(GoodsTag entity) throws Exception;
	
	PageBean<GoodsTag> listPage(GoodsTag entity) throws Exception;
	
	List<GoodsTag> selectAll() throws Exception;
}
