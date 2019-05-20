package com.bh.admin.service;


import java.util.List;

import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderSendInfo;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.jd.bean.order.OrderStock;
import com.bh.result.BhResult;



public interface JDOrderService {
		
	//6.26.2 批量获取库存接口（建议订单详情页、下单使用）
	BhResult getNewStockById(String cartIds,String area,Member member) throws Exception;
	
	Order selectOrderByIds(Order order) throws Exception;
	
	//当购物车的id为空时,需要通过Order 的id判断库存
	 BhResult getNewStockByOrderId(String orderId) throws Exception;
	 
	 public int updateJDOrderId(Order order) throws Exception;
	 
	 OrderStock orderTrack(OrderShop orderShop,Long ha) throws Exception;
	 
	   /*根据ID获取订单信息*/
     OrderShop getOrderById(String id);
     
     /*根据订单号获取ordermain表的信息*/
     Order getOrderMainById(String id);
     
     /*根据order_main 表的m_addr_id获取 member_user_address表信息*/
 	 MemberUserAddress getUserById(Integer id);
 	 
 	 /*获取order_sku信息*/
 	 List<OrderSku> getOrderSkuByOrderId(Integer orderId);
 	 
 	 /*获取good_sku信息*/
 	 GoodsSku getGoodSkuBySkuId(Integer orderId);
 	 
 	 /*根据goods_id 获取good表的信息*/
 	 Goods  getGoodsByGoodsId(Integer goodsId);
 	 
 	 
 	 /*把配送员信息保存在数据库*/
 	 int addOrderSendInfo(OrderSendInfo orderSendInfo);
 	 
 	 /*根据orderShopId查询当前的数据库有没有当前的订单*/
 	 List<OrderSendInfo> selectByjdOrderId(OrderSendInfo record);
 	 
 	 /*修改信息*/
 	 int updateByPrimaryKeySelective(OrderSendInfo record);

 	//获取订单的物流信息（非京东）
     OrderShop getOrderLogistics(String id);
 	 
 	 
 	 
}
