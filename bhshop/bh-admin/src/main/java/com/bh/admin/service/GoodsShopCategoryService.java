package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsShopCategory;
import com.bh.utils.PageBean;

public interface GoodsShopCategoryService {
	/*新增分类*/
	int selectParentInsert(String name, String reid, String sortnum, String image, int shopId, String series, String isLast) throws Exception;
	
	/*删除分类*/
	int selectdelete(String id) throws Exception;
	
	/*分类详情*/
	GoodsShopCategory selectById(Integer id) throws Exception;
	
	/*查询分类列表*/
	PageBean<GoodsShopCategory> selectByFirstReid(String name, String currentPage, String pageSize, String reId)throws Exception;
	
	/*修改分类*/
	int updateCategory(String id, String name, String sortnum, String image, String reid) throws Exception;
	
	/*根据父类id获取分类*/
	List<GoodsShopCategory> selectByParent(String reid) throws Exception;
	
	/*获取所有分类*/
	List<GoodsShopCategory> selectAll() throws Exception;
	
	/*获取三级分类*/
	List<GoodsShopCategory> selectThreeLevel() throws Exception;
	
	/*获取最后一级分类*/
	List<GoodsShopCategory> selectLastLevel() throws Exception;
	
	/*获取所有分类列表*/
	List<GoodsShopCategory> selectAllList()throws Exception;
	
	/*获取所有分类列表(树形结构)*/
	List<GoodsShopCategory> selectLinkedList(int shopId)throws Exception;
	
	/*商家后台分类列表管理(树形结构)*/
	PageBean<GoodsShopCategory> selectLinkedPage(String currentPage, int pageSize, String name, int shopId)throws Exception;
	
}
