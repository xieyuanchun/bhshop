package com.bh.user.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.AdsShop;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MyAdsShopPojo;
import com.bh.user.pojo.SimpleShop;
import com.bh.utils.PageBean;


public interface MallShopService {
	
	/** 2017-11-29配送端订单列表 */
	List<GoodsShopCategory> goodsShopCategory(GoodsShopCategory g) throws Exception;
	/**2017-11-29 店铺的信息 **/
	SimpleShop showShopMsg(Integer shopId,Member member) throws Exception;
	/*****2017-11-29 店铺的广告*****/
	List<MyAdsShopPojo> shopads(AdsShop adsShop) throws Exception;
	Map<String, Object> list(Integer shopId) throws Exception;
	Map<String, Object> alllist(Goods goods ,Integer page,Integer size) throws Exception;
	PageBean<Goods> alllistPc(Goods goods ,Integer page,Integer size,String fz) throws Exception;
	List<GoodsShopCategory> getAllCateList(Integer reid) throws Exception;
}
