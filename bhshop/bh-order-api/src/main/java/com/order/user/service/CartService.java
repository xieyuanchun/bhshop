package com.order.user.service;

import java.util.List;
import com.bh.goods.pojo.CartGoodsList;
import com.bh.goods.pojo.CartList;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.goods.pojo.LoseEfficacyGoods;

public interface CartService {
	Goods getShopId(Integer goodsId) throws Exception;
	
	 /**2017-10-24根据是否删除、用户的id、商品的id、skuid的id去查找**/
	 GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart) throws Exception;
	 int updateGoodsCartByPrimaryKeyAndgId1(List<String> ids)throws Exception;
	 int updateGoodsCartByPrimaryKeyAndgId2(List<GoodsCart> goodsCart) throws Exception;
	//更新购物车
	int updateGoodsCartBymIdAndgoodId(GoodsCart goodsCart,Integer isInsert) throws Exception;
	//更新购物车
	int updateGoodsCartBymIdAndgoodId2(GoodsCart goodsCart,Integer isInsert) throws Exception;
	//isInsert:取0时更新，取1是插入
	int insertSelectiveByBatch(List<GoodsCart> list);
	//9月26 根据用户的mId查询购物车
	List<GoodsCart> selectCoodsCartByUserId(GoodsCart goodsCart) throws Exception;
	//先查询有多少个商家:已登录
	List<CartList> selectShopMsg(GoodsCart goodsCart)throws Exception;
	//该商家有多少个商品：已登录
	List<CartGoodsList> selectGoodsMsg(GoodsCart goodsCart)throws Exception;
	//未登录
	List<CartList> selectShopMsgNotLogin(List<GoodsCartPojo> pojo)throws Exception;
	List<CartGoodsList> selectGoodsMsgNotLogin(List<GoodsCartPojo> pojo,Integer shopId)throws Exception;
	//失效商品列表
	List<LoseEfficacyGoods> loseEfficacyList(GoodsCart goodsCart);
	int emptyLoseEfficacyGoods(Integer id);
	
	int totalCartNum(Integer mId) throws Exception;
	
	//2018-5-22
	List<GoodsCartListShopIdList> selectCartList(GoodsCart cart) throws Exception;
	List<GoodsCart> selectCartListByShopId(GoodsCart cart) throws Exception;
}
