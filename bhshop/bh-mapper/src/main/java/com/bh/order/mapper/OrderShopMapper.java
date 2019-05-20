package com.bh.order.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.bh.order.pojo.MyOrderFail;
import com.bh.order.pojo.OrderShop;

public interface OrderShopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderShop record);

    int insertSelective(OrderShop record);

    OrderShop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderShop record);

    int updateByPrimaryKey(OrderShop record);
    
    List<OrderShop> selectByOrderShopId1(Integer orderId);
    
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
    //查询单价*数量
    int selectSumPrice(@Param("orderId")Integer orderId);
    
    //查询移动端商家今日的收入
    Integer selectMShopThisDayInCome(Integer shopId);
   //查询移动端商家这个月的收入
    Integer selectMShopThisMonthInCome(Integer shopId);
    //根据订单的id修改订单的金额
    int updateOrderNoByOrderId(OrderShop orderShop);
    List<OrderShop> selectByOrderId(@Param("orderId")Integer orderId);
    /****************chengfengyun end********************/
    
    
    void updateStatusByOrderNo(OrderShop orderShop);
    //进账统计--根据条件查询商家订单列表  xieyc
	List<OrderShop> getShopOrderList(@Param("id") Integer id, @Param("order_id") Integer order_id,@Param("order_no") String order_no, @Param("payment_no") String payment_no,
			@Param("payment_id") Integer payment_id, @Param("status") Integer status,@Param("shopId") Integer shopId);
	
	//统计--7日内店铺销售TOP10
	List<OrderShop> getTopTenShopList(Date startTime, Date endTime);
	//查询商家已经支付订单
	List<OrderShop> getShopOrder(@Param("shop_id")Integer shopId);

	OrderShop getByOrderNo(OrderShop orderShop);

	int mCountorderShopNum(int shopId);

	List<OrderShop> mgetShopOrder(int shopId);

	double mgetShopOrderEveryDay(@Param("shopId")int shopId, @Param("today")Date today);

	List<OrderShop> mgetByShopId(int shopId);

	List<OrderShop> mgetCountDetailList(@Param("shopId")int shopId, @Param("isRefund") Integer isRefund);

	List<OrderShop> getAlreadyByShopIdByTime(Date parse, Date parse2,@Param("shopId")int shopId);

	int selectShareCountByShopIdByTime(Date startTimes, Date endTimes, @Param("shopId")int shopId);
    //zlk 2018.4.11
	List<OrderShop> getByStatus();
	
	OrderShop selectByOrderNo(OrderShop orderShop);
	List<OrderShop> selectIdByOrderId(@Param("orderId")Integer orderId,@Param("shopId")String shopId);
    
	//用户的订单列表
	List<OrderShop> selectOrderList(@Param("status")Integer status,@Param("mId")Integer mId,@Param("goodsName")String goodsName,@Param("currentPageIndex")Integer currentPageIndex,@Param("pageSize")Integer pageSize);
	List<OrderShop> selectOrderListByShare(@Param("status")Integer status,@Param("mId")Integer mId,@Param("goodsName")String goodsName,@Param("currentPageIndex")Integer currentPageIndex,@Param("pageSize")Integer pageSize);
	
	//待付款订单
	List<MyOrderFail> selectByStatus();
	
	int updateByStatus(OrderShop o);
	
	List<OrderShop>  selectByOrderNo1(String orderNo);

	List<OrderShop> getJdOrderShop();

	List<OrderShop> systemReceivedOrder();

	void updateBatch(List<OrderShop> list);
	
	List<OrderShop> selectByOrderIds(Integer orderId);


	List<OrderShop> getNullLogistics(@Param("id") Integer id);



 }