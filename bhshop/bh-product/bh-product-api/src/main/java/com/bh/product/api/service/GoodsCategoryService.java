package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.goods.pojo.MyGoods;
import com.bh.goods.pojo.MyGoodsCategoryPojo;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;

public interface GoodsCategoryService {

	int selectParentInsert(String name, String reid, String sortnum, String image, String flag, String isLast, String series) throws Exception;
	
	/*删除分类*/
	/*int selectdelete(String id) throws Exception;*/
	String selectdelete(String id) throws Exception;
	
	int selectupdate(GoodsCategory categorymap) throws Exception;
	
	GoodsCategory selectById(String id) throws Exception;
	
	/*查询分类列表*/
	PageBean<GoodsCategory> selectByFirstReid(String name, String currentPage, String pageSize, String reId)throws Exception;
	
	/*修改分类*/
	int updateCategory(String id, String name, String sortnum, String image, String flag) throws Exception;
	
	/*根据父类id获取分类*/
	List<GoodsCategory> selectByParent(String reid) throws Exception;
	
	/*首页分类列表*/
	List<GoodsCategory> homePageList() throws Exception;
	
	/*获取所有分类*/
	List<GoodsCategory> selectAll() throws Exception;
	
	/*商品添加时获取所有分类*/
	List<GoodsCategory> selectAllByCatId(String catId) throws Exception;
	
	/*排序*/
	int changeSortNum(String id, String sortnum) throws Exception;
	
	/*获取三级分类*/
	List<GoodsCategory> selectThreeLevel() throws Exception;
	
	/*获取最后一级分类*/
	List<GoodsCategory> selectLastLevel() throws Exception;
	
	/*首页分类显示前6条商品*/
	List<MyGoodsCategoryPojo> selectTopSixGoodsBycatId() throws Exception;
	
	/*移动端根据父类id获取下级分类及商品数量*/
	List<GoodsCategory> selectCategoryAndNumByParent(String reid) throws Exception;
	
	/*获取所有分类列表*/
	List<GoodsCategory> selectAllList()throws Exception;
	
	/*获取所有分类列表(树形结构)*/
	List<GoodsCategory> selectLinkedList()throws Exception;
	
	/*平台后台分类列表管理(树形结构)*/
	PageBean<GoodsCategory> selectLinkedPage(String currentPage, int pageSize, String name)throws Exception;
	
	/*根据分类id递归查询所有子分类*/
	List<GoodsCategory> getAllCateList(long reid);
	
	/*移动端获取所有一级菜单*/
	List<GoodsCategory> getFirstLevelList();
	
	/*移动端分类模块根据分类获取下级分类及商品*/
	Map<String, Object> getCateListAndGoods(int pageSize, String currentPage, String catId);
	/*2018.5.30 zlk 移动端分类模块根据分类获取下级分类及商品*/
	Map<String, Object> getCateListAndGood(int pageSize, String currentPage, String catId);
	
	
	/*京东商品分类摘取*/
	int insertJdCategory(JSONArray list) throws Exception;
	
	/*根据商品分类查品牌*/
	List<GoodsBrand> getBrandByCategory(Long catId);
	
	/*查所有分类*/
	List<GoodsCategory> getAllCategory();
	
	
	List<GoodsCategory> getByLevel(GoodsCategory entity);
	
	List<GoodsCategory> getByReid(GoodsCategory entity);


	PageBean<Goods> getGoodsByCatId(String currentPage, String catId);


	List<GoodsCategory> selectLinkedByCatIdOne(String string);
	
}
