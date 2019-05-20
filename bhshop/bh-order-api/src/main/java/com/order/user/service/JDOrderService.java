package com.order.user.service;


import java.util.List;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.StockParams;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;

public interface JDOrderService {
	
	BhResult returnStock(String isFromCart,String cartIds,String area,Member member)throws Exception;
		
	//6.26.2 批量获取库存接口（建议订单详情页、下单使用）
	BhResult getNewStockById(String cartIds,String area) throws Exception;
	
	Order selectOrderByIds(Order order) throws Exception;
	
	//当购物车的id为空时,需要通过Order 的id判断库存
	 BhResult getNewStockByOrderId(String orderId) throws Exception;
	 
	 public int updateJDOrderId(Order order) throws Exception;
	 
	 OrderStock orderTrack(OrderShop orderShop,Long l) throws Exception;
	 
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
     
     
     //判断该用户购买的商品是否超过数据库设定的上限
     /**
      * 程凤云
      * isFromCart:字符串类型，取0和1值，0时是从购物车过来，值为1时是从订单列表过来
      * cartIds：如果isFromCart为0时是购物车的id,如果isFromCart为1时是订单的id
      * member:当前登录的用户
      * 返回值：如果为0可以继续购买，如果为1时提示"该用户购买该商品已达上限"
      * **/
     BhResult memberBuyGoodsIsLimit(String cartIds,String isFromCart,Member member);
     /**
      * 如果返回的值为0未达到购买的上限，如果是1表示达到购买的上限
      * */
     public BhResult isCount(Integer mId,Integer goodsId,Integer num);
 	 
     
     public BhResult getAreaLimit(String addressId,Member member,List<String> ids);
     
     public BhResult getAreaLimitByBH(String addressId,Member member,List<String> ids);
     
   //查找京东的库存 返回值：200有货 ，201无货 ，400报错
 	public BhResult getJDStockById(String area,List<StockParams> jdIds);
 	
 	//2018-5-25
 	public BhResult getBHSto(Integer goodsId, String provname, Integer storeNums, Integer num) ;
 	//京东库存
 	public BhResult getJDStock(String prov,String city,String area,List<StockParams> jdIds);
 	 //京东限购地址
    public BhResult getAreaByJD(String skuIds, String prov, String city, String town, String four);


	void jdOrderInsert();

	JdResult<OrderStock> jdOrderTrack(String jdOrderId); 
}
