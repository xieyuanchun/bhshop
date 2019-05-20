package com.bh.admin.service;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.GoodsAttr;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.BusBankCard;
import com.bh.utils.PageBean;


public interface ShopOrderCountService {
	
	//进账统计--根据条件查询商家订单列表
	PageBean<OrderShop> getShopOrderList(String id, String order_id, String order_no, String payment_no,
			String payment_id, String status, String currentPage, String pageSize,Integer shopId) throws Exception;
	//统计：根据id查询某个商家订单详情
	OrderShop getOrderShopDetails(String id) throws Exception;
	
	
	//统计-出账列表
	PageBean<OrderRefundDoc> countRefundList(String id, String order_id, String order_no, String payment_no,
			String status, String currentPage, String pageSize, String startTime, String endTime,Integer shopId) throws Exception;

	//统计-出账列表中某条记录的详细列表
	OrderRefundDoc getCountRefundDetail(String id) throws Exception;
	
	//统计--概要
	Map<String, Object> countOutline(String startTime, String endTime, Integer shopId) throws Exception;
	
	//统计--7日内店铺销售TOP10
	PageBean<OrderSku> getTopTenShopList() throws Exception;
	//统计--7日内商品销售TOP10
	PageBean<OrderSku> getTopTenGoodsList(Integer shopId) throws Exception;
	void test();
	PageBean<OrderShop> mgetCountDetailList(String currentPage, String pageSize, int shopId, int integer) throws Exception;
	PageBean<OrderRefundDoc> mgetRefundList(String currentPage, String pageSize, int intValue) throws Exception;
	Map<String,Double> mgetAmountEveryDay(String day, int intValue);
	List<BusBankCard> mgetBusBankCard(BusBankCard busBankCard);
	PageBean<OrderShop> mgetShopOrderList(String currentPage, String pageSize, int intValue);
	int mUpdateBusBankCard(BusBankCard busBankCard);
	BusBankCard mgetBusBank(String id);
	
	
	
	
	


	
	

	


}
