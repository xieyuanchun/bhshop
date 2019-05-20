package com.order.user.service;

import java.util.List;

import com.bh.goods.pojo.CommentSimple;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsCommentWithBLOBs;
import com.bh.goods.pojo.MyGoodsComment;
import com.bh.goods.pojo.MyWaitcommentlist;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.utils.PageBean;

public interface CommentService {
	
	//添加商品评价
	int insertGoodsComment(List<CommentSimple> commentSimpleList,Integer mId) throws Exception;
	/****该商品评论*****/
	List<GoodsComment> getListComment(List<String> ids);
	//获得OrderSku
	OrderSku selectOrderSkuById(Integer id) throws Exception;
	OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception;
	/**********显示待评价的订单的列表*************/
	PageBean<OrderShop> showWaitCommentList(OrderShop orderShop,Integer page ,Integer rows) throws Exception;
	List<MyWaitcommentlist> selectMyWaitCommentList(Integer mId)throws Exception;
	
	/**********显示已评价商品的列表*************/
	List<MyGoodsComment> showCommentList(GoodsComment comment,Integer page ,Integer rows) throws Exception;
	
	
}
