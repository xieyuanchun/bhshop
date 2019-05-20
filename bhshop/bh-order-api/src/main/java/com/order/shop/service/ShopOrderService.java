package com.order.shop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSku;
import com.bh.utils.PageBean;

public interface ShopOrderService {
	Order getShopOrderById(String id) throws Exception;
	
	/*scj-商家后台订单列表*/
	PageBean<Order> orderList(String id, String order_no, String status, String currentPage, String pageSize, String startTime, String endTime) throws Exception;
	
	/*scj-平台后台全部订单列表*/
	PageBean<Order> pageAll(String order_no, String status, String currentPage, String pageSize, String startTime, String endTime) throws Exception;
	
	/*订单详情*/
	Order getOrderGoodsDetails(String id, String adminId, String fz) throws Exception;
	
	/*scj-商家后台接单操作*/
	int orderReceiving(String id, String adminId) throws Exception;
	
	/*scj-商家后台针对整个订单抛单*/
	Order castSheet(String id, String userJson, String deliveryPrice) throws Exception;
	
	/*scj-商家后台针对订单商品抛单*/
	Order castSheetOne(String id) throws Exception;
	
	/*scj-订单商品列表*/
	PageBean<OrderSku> getOrderGoodsList(int adminId, String orderId, String currentPage, String pageSize) throws Exception;
	
	/*scj-批量删除*/
	List<Order> batchDelete(String id) throws Exception;
	
	/*scj-退款商品列表*/
	PageBean<OrderRefundDoc> getRefundGoodsList(String orderId, String currentPage, String pageSize) throws Exception;
	
	/*scj-商家取消订单*/
	Order cancelOrder(String id, String adminId) throws Exception;
	
	/*scj-批量抛单*/
	List<Order> batchCastSheet(String id) throws Exception;
	
	/*审核退款*/
	Order auditRefund(String id, String status, String adminId) throws Exception;
	
	/*scj-配送端订单列表*/
	PageBean<Order> deliveryWaitList(String orderNo, String fz, String currentPage, String pageSize, int mId) throws Exception;
	
	/*scj-配送端抢单*/
	Order robOrder(String id, String sId) throws Exception;
	
	/*scj-配送端商品已送达*/
	Order alreadyDelivery(String id) throws Exception;
	
	/*scj-后台结算模块订单统计*/
	Map<Integer, Object> OrderAccount(int adminId) throws Exception;
	
	/*scj-后台订单&商品模块订单统计*/
	Map<Integer, Object> OrderWaitAccount(int adminId) throws Exception;
	
	/*scj-后台退款模块订单统计*/
	Map<Integer, Object> OrderRefundAccount(int adminId) throws Exception;
	
	/*scj-后台交易金额统计*/
	Map<String, String> OrderMoneyAccount(int adminId) throws Exception;
	
	/*scj-后台订单数量统计管理*/
	Map<String, Object> OrderNumAccount(int adminId) throws Exception;
	
	/*scj-平台后台订单数量统计*/
	Map<String, Object> backgroundOrderAccount() throws Exception;

}
