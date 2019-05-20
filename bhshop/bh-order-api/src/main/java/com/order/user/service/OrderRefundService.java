package com.order.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsComment;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderSkuReturn;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

public interface OrderRefundService {
	
	BhResult showRefundGoods(OrderSku o) throws Exception;
	
	int refundgoods(OrderRefundDoc refundDoc) throws Exception;
	OrderShop selectOrderShopStatus(Integer orderSkuId) throws Exception;
	PageBean<OrderShop> selectAllOrderShopList(OrderShop orderShop,Integer page,Integer rows) throws Exception;
	PageBean<OrderShop> selectAllOrderShopList1(OrderShop orderShop,Integer page,Integer rows) throws Exception;
	PageBean<OrderSku> selectAllOrderShopList2(OrderSku orderSku,Integer page,Integer rows) throws Exception;
	OrderRefundDoc selectOrderRefundByOrderSkuId(Integer id) throws Exception;
	/***********已退款商品列表的展示**********/
	PageBean<OrderRefundDoc> showrefundedlist(OrderRefundDoc doc,Integer page,Integer rows) throws Exception;
	GoodsComment selectCommentById(GoodsComment comment) throws Exception;
	Goods selectGoodsById(Integer id) throws Exception;
	int updateOrderRefundByParams(OrderRefundDoc doc) throws Exception;
	OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception;
	int insertOrderRefundStep(Integer docId) throws Exception;
	
	//查询该商品是否申请过售后服务
	Integer selectIsRefund(Integer orderSkuId);
	Integer selectOShopStatus(Integer orderSkuId);
	List<OrderRefundDoc> selectOrderRefundDoc(Integer orderSkuId);
}
