package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.GoodsBrand;
import com.bh.utils.PageBean;

public interface GoodsBrandService {
	
	/*by scj 根据分类id查询所有品牌*/
	List<GoodsBrand> selectByCatid(String catId) throws Exception;
	
	/*获取分页列表*/
	PageBean<GoodsBrand> pageAll(String name, String currentPage, String pageSize) throws Exception;
	
	/*添加品牌*/
	int addGoodsBrand(String name, String logo, String sortnum, String catId) throws Exception;
	
	/*添加京东品牌*/
	int addJdGoodsBrand(String id, String name, String logo, String sortnum, String catId) throws Exception;
	
	/*修改品牌*/
	int editGoodsBrand(String id, String name, String logo, String sortnum, String catId) throws Exception;
	
	/*删除*/
	int deleteGoodsBrand(String id) throws Exception;
	
	/*详情*/
	GoodsBrand details(String id) throws Exception;
	
	/*批量删除*/
	int batchDelete(String id) throws Exception;
	
	List<GoodsBrand> selectAll() throws Exception;
	
}
