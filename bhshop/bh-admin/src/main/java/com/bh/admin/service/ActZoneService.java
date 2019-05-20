package com.bh.admin.service;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.ActZone;
import com.bh.admin.pojo.goods.GoodsBrand;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.GoodsShopCategory;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;

public interface ActZoneService {
    //添加专区名
	int selectParentInsert(String isNormalShow,String name, String reid, String sortnum, String image, String isLast, String levelnum,String freePostage,String isCart,String isCoupon,String isRefund,String isLockScore) throws Exception;
	//添加分类名
	int selectTaxonomyInsert(String name, String reid, String sortnum, String isLast, String levelnum) throws Exception;
	//删除分类
	int selectTaxonomyDelete(Integer id) throws Exception;
	//修改分类
	int selectTaxonomyUpdate(Integer id,String name, String levelnum, String sortnum,Integer reid) throws Exception;
	
	/*删除分类*/
	/*int selectdelete(String id) throws Exception;*/
	String selectdelete(String id) throws Exception;
	

	ActZone selectById(String id) throws Exception;
	
	/*查询分类列表*/
	PageBean<ActZone> selectByFirstReid(String name, String currentPage, String pageSize, String reId)throws Exception;
	
	/*修改分类*/
	int updateCategory(String id, String name, String sortnum, String image,String freePostage,String isNormalShow,String isCart,String isCoupon,String isRefund,String isLockScore,String failuretime) throws Exception;
	
	/*根据父类id获取分类*/
	List<ActZone> selectByParent(String reid) throws Exception;
	
	/*首页分类列表*/
	//List<ActZone> homePageList() throws Exception;
	
	/*获取所有分类*/
	List<ActZone> selectAll() throws Exception;
	
	/*商品添加时获取所有分类*/
	List<ActZone> selectAllByCatId(String catId) throws Exception;
	
	/*排序*/
	int changeSortNum(String id, String sortnum) throws Exception;
	
	/*获取三级分类*/
	List<ActZone> selectThreeLevel() throws Exception;
	
	/*获取最后一级分类*/
	List<ActZone> selectLastLevel() throws Exception;
	
	/*首页分类显示前6条商品*/
	List<ActZone> selectTopSixGoodsBycatId() throws Exception;
	
	/*移动端根据父类id获取下级分类及商品数量*/
	List<ActZone> selectCategoryAndNumByParent(String reid) throws Exception;
	
	/*获取所有分类列表*/
	List<ActZone> selectAllList()throws Exception;
	
	/*获取所有分类列表(树形结构)*/
	List<ActZone> selectLinkedList()throws Exception;
	
	/*平台后台分类列表管理(树形结构)*/
	PageBean<ActZone> selectLinkedPage(String currentPage, int pageSize, String name)throws Exception;
	
	/*根据分类id递归查询所有子分类*/
	List<ActZone> getAllCateList(long reid);
	
	/*移动端获取所有一级菜单*/
	List<ActZone> getFirstLevelList();
	
	/*移动端分类模块根据分类获取下级分类及商品*/
	Map<String, Object> getCateListAndGoods(int pageSize, String currentPage, String catId);
	
	/*京东商品分类摘取*/
	//int insertJdCategory(JSONArray list) throws Exception;
	
	/*根据商品分类查品牌*/
	List<GoodsBrand> getBrandByCategory(Long catId);
	
	/*查所有分类*/
	List<ActZone> getAllCategory();
	
	
	List<ActZone> getByLevel(ActZone entity);
	
	List<ActZone> getByReid(ActZone entity);
}
