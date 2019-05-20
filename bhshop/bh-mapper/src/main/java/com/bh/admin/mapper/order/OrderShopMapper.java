package com.bh.admin.mapper.order;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.order.MyOrderShop;
import com.bh.admin.pojo.order.OrderShop;


public interface OrderShopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderShop record);

    int insertSelective(OrderShop record);

    OrderShop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderShop record);

    int updateByPrimaryKey(OrderShop record);
    
    OrderShop selectByOrderNo(@Param("id")String id,@Param("shopId")String shopId);
    
    List<OrderShop> selectByOrderNos(@Param("orderNo")String orderNo,@Param("shopId")String shopId);
    
    OrderShop selectByShopOrderNo(String shopOrderNo);
    
    OrderShop selectByTeamOrderNo(String orderNo);
    
    //OrderShop selectByGoodsIdTeam(String goodsId);
    
    OrderShop getByOrderIdAndShopId(Integer orderId, Integer shopId);
    
    List<OrderShop> getByOrderIdAndStatus(Integer orderId);
    
    List<OrderShop> getByOrderId(Integer orderId);
    
    List<OrderShop> getByShopId(@Param("shopId")Integer shopId, @Param("status") Integer status);
    
    List<OrderShop> getAlreadyByShopId(@Param("shopId")Integer shopId);
    
    
    List<OrderShop> getOrderByDstate(Integer dState);
    
    
    List<OrderShop> sendAccount(Integer sId, Integer dState);
    
    /*scj-商家后台订单列表*/   
    List<OrderShop> orderShopList(@Param("shopId")Integer shopId, @Param("status") Integer status, String order_no, Date startTime, Date endTime, @Param("isJd") String isJd);
    
    List<OrderShop> orderShopAllList(@Param("shopId")Integer shopId, String order_no, Date startTime, Date endTime, @Param("isJd") String isJd);
    
    
    /*配送端高低价类型订单排序*/
    List<OrderShop> getDeliveryPriceTypeOrder( @Param("list") List<String> list, @Param("fz") Integer fz, String lng, String lat);
    
    List<OrderShop> backGroundOrderList(OrderShop entity);
    List<OrderShop> bgJdOrderList(OrderShop entity);
    int jdOrderCount(@Param("shopId")Integer shopId);
    
    int mOrderAccount(OrderShop record);
    List<OrderShop> mOrderListPage(OrderShop record);
    
    int mAccount(OrderShop record);
    
    
    
    /****************chengfengyunbegin********************/
    // ****************2017-10-26 星期四 ********************/
    /*****批量插入到ordershop表*******/
    int insertOrderShopByBatch(List<OrderShop> list);
    
    // ****************2017-10-27 星期五 ********************/
    /*****通过order_id 查询 ordershop表*******/
    List<OrderShop> selectOrderShopByOrderIds(OrderShop orderShop);//OrderShop(orderId)
    
    /*****通过m_id 和group_id 查询 ordershop表*******/
    List<OrderShop> selectOrderShopBySelect(OrderShop orderShop);
    List<OrderShop> selectOrderShopByRefunt(OrderShop orderShop);
    int updateOrderShopBySelect(OrderShop orderShop);
    List<OrderShop> selectOrderShopByOrderId(OrderShop orderShop);
    OrderShop selectByOrderShopId(OrderShop orderShop);
    List<OrderShop> selectOrderShopByOrder(OrderShop orderShop);
    int updateOrderShopByParams(OrderShop orderShop);
    List<OrderShop> selectOrderShopParams(OrderShop orderShop);
    List<OrderShop> selectOrderShopByOrderNo(OrderShop orderShop);
    List<OrderShop> selectOrderShopByStatus2(OrderShop orderShop);
    List<OrderShop> selectOrderShopByOrderIds1(OrderShop orderShop);//OrderShop(orderId)
    void updateOrderPrice(OrderShop orderShop);
    //后台管理系统的待分享订单的列表
    List<OrderShop> selectShareOrderListByShopId(OrderShop orderShop);
    int selectShareCountByShopId(OrderShop orderShop);
    
    //查询移动端商家今日的收入
    Integer selectMShopThisDayInCome(@Param("shopId")Integer shopId);
   //查询移动端商家这个月的收入
    Integer selectMShopThisMonthInCome(@Param("shopId")Integer shopId);
    List<MyOrderShop> MyBackGroundOrderList(OrderShop entity);
    List<MyOrderShop> getSimpleShareOrderList(OrderShop entity);
    int orderShopCount(@Param("status") Integer status,@Param("shopId") Integer shopId);
    int selectShareCount(@Param("shopId") Integer shopId);
    List<MyOrderShop> selectJDOrderList(OrderShop entity);
    int selectJdOrderListCount(@Param("status") Integer status,@Param("shopId") Integer shopId);
    /****************chengfengyunend********************/
    
    
    void updateStatusByOrderNo(OrderShop orderShop);
    //进账统计--根据条件查询商家订单列表  xieyc
	List<OrderShop> getShopOrderList(@Param("id") Integer id, @Param("order_id") Integer order_id,@Param("order_no") String order_no, @Param("payment_no") String payment_no,
			@Param("payment_id") Integer payment_id, @Param("status") Integer status,@Param("shopId") Integer shopId);
	
	//统计--7日内店铺销售TOP10
	List<OrderShop> getTopTenShopList(Date startTime, Date endTime);
	//查询商家已经支付订单
	List<OrderShop> getShopOrder(@Param("shopId")Integer shopId,Date startTimes,Date endTimes);
	/* zlk 根据商家订单号获取信息*/
	OrderShop getByOrderNo(OrderShop orderShop);

	int mCountorderShopNum(int shopId);

	List<OrderShop> mgetShopOrder(int shopId);

	double mgetShopOrderEveryDay(@Param("shopId")int shopId, @Param("today")Date today);

	List<OrderShop> mgetByShopId(int shopId);

	List<OrderShop> mgetCountDetailList(@Param("shopId")int shopId, @Param("isRefund") Integer isRefund);

	List<OrderShop> getAlreadyByShopIdByTime(Date parse, Date parse2,@Param("shopId")int shopId);

	int selectShareCountByShopIdByTime(Date startTimes, Date endTimes, @Param("shopId")int shopId);
    /*2018.6.1 zlk 根据平台订单号获取信息*/
	OrderShop getByOrderNos(OrderShop orderShop);

	List<OrderShop> getAlreadyByShopIdByTimes(OrderShop orderShop);

	List<OrderShop> getAllRefund(@Param("startTime") Date startTime,@Param("endTime") Date endTime, @Param("shopId") Integer shopId);
	List<OrderShop> getAllRefund1(@Param("shopId") Integer shopId);

	int getAllOrderShopNum(@Param("startTime") Date startTime,@Param("endTime") Date endTime, @Param("shopId") Integer shopId);
	int getAllOrderShopNum1(@Param("shopId") Integer shopId);

	List<OrderShop> getCanWithdraList(Integer shopId);

	List<OrderShop> getSettleList(OrderShop findOrderShop);

	List<OrderShop> getTrandeList(OrderShop findTrandeList);
	
	List<OrderShop> getTrandeList1(OrderShop findTrandeList);
	
    
}