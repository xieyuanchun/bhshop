package com.order.shop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.GoodsSku;
import com.bh.order.pojo.MyOrderSkuPojo;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.pojo.MemberNotice;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.PageBean;
import com.github.pagehelper.Page;

import net.sf.json.JSONObject;

public interface OrderMainService {
	Order getShopOrderById(String id) throws Exception;
	
	/*scj-商家后台订单列表*/
	PageBean<OrderShop> orderList(int shopId, String order_no, String status, String currentPage, String pageSize,
			String startTime, String endTime, String isJd) throws Exception;
	
	/*scj-商家后台退款订单列表*/
	PageBean<OrderRefundDoc> orderRefundList(int shopId, String order_no, String status, String currentPage, String pageSize, String startTime, String endTime) throws Exception;
	
	/*scj-平台后台全部订单列表*/
	PageBean<Order> pageAll(String order_no, String status, String currentPage, String pageSize, String startTime, String endTime) throws Exception;
	
	/*商家后台订单详情*/
	OrderShop getOrderGoodsDetails(String id) throws Exception;
	
	/*商家后台退款订单详情*/
	OrderShop getOrderRefundDetails(String id) throws Exception;
	
	/*平台后台订单详情*/
	Order getOrderDetails(String id) throws Exception;
	
	/*scj-商家后台针对整个订单抛单*/
	OrderShop castSheet(String id, String deliveryPrice,String userJson) throws Exception;
	
	/*scj-商家取消订单*/
	OrderShop cancelOrder(String id) throws Exception;
	
	/*scj-批量抛单*/
	List<Order> batchCastSheet(String id) throws Exception;
	
	/*审核退款*/
	OrderShop auditRefund(String id, String status, int userId, String refuseReason,String username) throws Exception;
	
	/*scj-后台结算模块订单统计*/
	Map<Integer, Object> OrderAccount(int shopId) throws Exception;
	
	/*scj-后台订单&商品模块订单统计*/
	Map<Integer, Object> OrderWaitAccount(int shopId) throws Exception;
	
	/*scj-后台退款模块订单统计*/
	Map<String, Object> OrderRefundAccount(int shopId,String startTime, String endTime) throws Exception;
	
	/*scj-后台交易金额统计*/
	Map<String, String> OrderMoneyAccount(int shopId) throws Exception;
	
	/*scj-后台订单数量统计管理*/
	Map<String, Object> OrderNumAccount(int shopId,String startTime, String endTime) throws Exception;
	
	/*scj-后台订单数量统计管理（移动端后台-下四）*/
	Map<String, Object> mOrderNumAccount(int shopId) throws Exception;
	
	/*scj-后台订单数量统计管理（移动端后台-上三）*/
	Map<String, Object> mNumAccount(int shopId) throws Exception;
	
	/*scj-后台订单详情（移动端后台-上三）*/
	OrderShop mOrderDetails(OrderShop entity) throws Exception;
	
	/*scj-发货走速达流程（移动端后台）*/
	OrderShop mSportTech(String id, String deliveryPrice) throws Exception;
	
	/*scj-发货走商家自配（移动端后台）*/
	int mDistributeByself(String id, long shopId) throws Exception;
	
	/*scj-后台商家订单管理(移动端后台)*/
	PageBean<OrderShop> mOrderListPage(OrderShop entity) throws Exception;
	
	Map<String, Object> jdOrderCount(int shopId) throws Exception;
	
	/*scj-平台后台订单数量统计*/
	Map<String, Object> backgroundOrderAccount() throws Exception;
	
	/*scj-后台首页订单交易总额和订单数量统计*/
	Map<String, Object> totalNumAccount(int shopId, String startTime, String endTime) throws Exception;
	
	/*scj-商家后台配送订单列表*/
	PageBean<OrderSendInfo> orderSendPage(int shopId, String shopOrderNo, String dStatus, String currentPage, String pageSize) throws Exception;
	
	/*商家后台配送单详情*/
	OrderSendInfo getOrderSendDetails(String id) throws Exception;
	
	/*商家后台配送单审核*/
	int auditOrderSend(String id) throws Exception;
	
	/*商家后台订单添加备注*/
	int insertRemark(String id, String remark) throws Exception;
	
	/*商家后台订单自配*/
	int deliveryOwner(String id, int shopId) throws Exception;
	
	/*商家订单处会员信息*/
	Map<String, Object> getMemerUserInfo(String mId) throws Exception;
	
	List<MemberNotice> selectMemberNoticeList(MemberNotice notice);
	
	
	
	PageBean<OrderShop> backGroundOrderList(OrderShop entity) throws Exception;
	PageBean<OrderShop> bgJdOrderList(OrderShop entity) throws Exception;
	
	PageBean<Order> backGroundAllOrderList(Order entity) throws Exception;
	
	

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
	
	//拼单失败
	void teamFail(String orderNo);
	
	//获取订单的物流信息（非京东）
    OrderShop getOrderLogistics(String id);
    
    Order selectByPrimaryKey(Integer id);

	Order getOrderByOrderNo(String orderNo);
    
    List<Map<String, Object>> getAllShopName()throws Exception;

	PageBean<OrderSku> getSaleOrderList(Integer shopId, String startTime, String endTime)throws Exception;
	
	MyOrderSkuPojo selectOrderDetail(Integer id);
	
	MyOrderSkuPojo writeLogistics(Integer orderSkuId);
	
	int saveLogistics(OrderRefundDoc orderRefundDoc);
	
	MyOrderSkuPojo selectLogistics(Integer orderSkuId);

	int moveLogistics(Integer valueOf);

	JSONObject queryLogistics(String expressNo, String type) throws Exception;
	


	
}
