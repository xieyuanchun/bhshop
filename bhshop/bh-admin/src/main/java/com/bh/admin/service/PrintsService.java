package com.bh.admin.service;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.BhDictItem;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUserAddress;




public interface PrintsService {
	List<OrderTeam> selectOrderTeam(String orderNo) throws Exception;
	

	   /*根据商家订单号获取订单信息*/
	OrderShop getOrderById(String id,Integer shopId);
	
	 /*根据OrderShopID获取商品名称*/
	List<OrderSku>  getByOrderShopId(String id);
	
	/*根据订单号获取ordermain表的信息*/
	Order getOrderMainById(String id);
	
	/*根据order_main 表的m_addr_id获取 member_user_address表信息*/
	MemberUserAddress getUserById(Integer id);
	
	//根据order_sku表的sku_id 获取 GoodsSku表的信息
	GoodsSku getGoodsSku(Integer id);
	
	//根据order 表的shopId 获取 MemberShop的信息
	MemberShop getMemberShopById(Integer id);
	
	int updateByPrimaryKey(OrderShop record);
	
	void updateExpressByOrderNo(Order record);
	
	void updateExpressByOrderNo1(OrderShop orderShop1);
	
	int updatePrintByPrimaryKey(OrderShop record);
	
	   /*根据平台订单号获取订单信息*/
	OrderShop getByOrderNo(String orderNo,Integer shopId);
	
	Map<String,Object> getCode(Map<String,Object> mp);
	
	String getTaobao(Map<String,Object> mp);
	
	List<OrderRefundDoc> getOrderRefundDoc(Integer id);


	List<BhDictItem> getExpressInfo();
}




