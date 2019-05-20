package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.Ads;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.Menu;
import com.bh.utils.PageBean;

public interface AdsService {
	
	/*根据id查询广告*/
	List<Ads> selectById(String id) throws Exception;
	
	/*首页轮播广告图*/
	List<Ads> selectListByIsMain(String isPc) throws Exception;
	
	/*后台广告管理列表*/
	PageBean<Ads> pageList(String isPc, String isMain, String fz, String name, String currentPage, int pageSize) throws Exception;
	
	/*广告的新增*/
	int addAds(String name, String image, String content, String link,String sLink,String sortnum, String isMain, String isPc) throws Exception;
	
	/*广告的编辑*/
	int editAds(String id ,String name, String image, String content, String link,String sLink, String sortnum, String isMain, String isPc) throws Exception;
	
	/*批量删除*/
	String batchDelete(String id) throws Exception;
	
	/*添加菜单时加载广告列表*/
	List<Ads> getList(String menuId) throws Exception;
	
	/*广告的删除*/
	int deleteAds(String id) throws Exception;
	
	/*广告的删除*/
	int startAds(String id, String status) throws Exception;
	
	/*添加广告加载排序号*/
	List<String> getSortNum() throws Exception;
	
	/*修改广告加载排序号*/
	List<String> getChangeSortNum() throws Exception;
	
	/*当前最大排序号*/
	int getBigSortNum() throws Exception;
	
}
