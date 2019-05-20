package com.print.service;

import java.util.List;

import com.bh.goods.pojo.GoodsSku;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;


public interface PrintsService {

	   /*根据ID获取订单信息*/
	OrderShop getOrderById(String id);
	
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
	
	int updatePrintByPrimaryKey(OrderShop record);
}




