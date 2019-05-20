package com.bh.admin.service;

import com.bh.admin.pojo.goods.AdsShop;
import com.bh.utils.PageBean;

public interface AdsShopService {
	/*店铺轮播图的新增*/
	int addShopAds(String name, String image, String content, String link, String sortnum, String isPc, int shopId,  String status) throws Exception;
	
	/*店铺轮播图的修改*/
	int editShopAds(String id, String name, String image, String content, String link, String sortnum, String goodsId, String status, String isPc) throws Exception;
	
	/*店铺轮播图的删除*/
	int delectShopAds(String id) throws Exception;
	
	/*店铺轮播图分页列表*/
	PageBean<AdsShop> pageList(String currentPage, int pageSize, String name, String isPc, int shopId) throws Exception;
	
	/*店铺轮播图详情*/
	AdsShop shopAdsDetails(String id) throws Exception;
	
}
