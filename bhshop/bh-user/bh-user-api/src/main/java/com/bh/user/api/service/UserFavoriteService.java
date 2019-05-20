package com.bh.user.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.order.pojo.OrderInfoPojo;
import com.bh.user.pojo.MemberShopFavorite;
import com.bh.utils.PageBean;

public interface UserFavoriteService {
	
	/**2017-11-20 添加用户收藏商品*/
	int addGoodFavorite(GoodsFavorite favorite) throws Exception;
	//取消商品的收藏，可单个或者批量
	int deleteGoodFavorite(GoodsFavorite favorite) throws Exception;
	//显示用户收藏列表
	PageBean<GoodsFavorite> showGoodsFavorite(GoodsFavorite favorite,Integer page,Integer rows) throws Exception;
	//显示用户收藏的分类名称
	List<GoodsCategory> showGoodsCategory(GoodsCategory goodsCategory) throws Exception;
	//显示用户收藏的商家的分类名称
	List<GoodsCategory> showShopCategory(GoodsCategory g) throws Exception;
	//
	List<GoodsCategory> selectHistoryCategory(GoodsCategory g) throws Exception;
	//显示商家收藏的列表:String param
	PageBean<MemberShopFavorite> showShopFavorite(MemberShopFavorite favorite,Integer page,Integer rows,Integer page1,Integer size1) throws Exception;
	//取消店铺的的收藏，可单个或者批量
	int deleteShopFavorite(MemberShopFavorite favorite) throws Exception;

	
	Map<String, Object> selectSimilarity(Goods goods,Integer page,Integer size) throws Exception;
	/**
	 * 显示用户的历史浏览记录
	 * ***/
	PageBean<MemberUserAccessLog> showhistoryList(MemberUserAccessLog history,Integer page,Integer size) throws Exception;
	/**
	 *显示收藏的商品的数量以及店铺的数量
	 * ***/
	OrderInfoPojo selectFavoriteNum(Integer mId) throws Exception;
	
	Goods selectOneGoodGavoritenum(GoodsFavorite favorite) throws Exception;

	
}
