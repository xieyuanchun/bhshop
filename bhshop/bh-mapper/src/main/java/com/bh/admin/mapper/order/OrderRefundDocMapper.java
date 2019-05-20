package com.bh.admin.mapper.order;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.order.OrderRefundDoc;



public interface OrderRefundDocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefundDoc record);

    int insertSelective(OrderRefundDoc record);

    OrderRefundDoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefundDoc record);

    int updateByPrimaryKeyWithBLOBs(OrderRefundDoc record);

    int updateByPrimaryKey(OrderRefundDoc record);
    
    
    List<OrderRefundDoc> getRefundGoodsList(Integer orderId);
    
    int countRefundGoodsList(Integer orderId);
    
    List<OrderRefundDoc> getByOrderShopId(Integer orderShopId);
    
    OrderRefundDoc getByOrderShopId1(Integer orderShopId,Integer orderSkuId);
    
    List<OrderRefundDoc> getByShopId(Integer shopId, Integer status);
    
    /*后台退款订单列表(客服)*/
    List<OrderRefundDoc> orderRefundList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    /*后台退款订单列表(财务)*/
    List<OrderRefundDoc> orderFRefundList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
   
    /*后台退货退款订单列表(客服)*/
    List<OrderRefundDoc> refundGoodList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    
    /*后台退货退款订单列表(财务)*/
    List<OrderRefundDoc> refundFGoodList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    
    
    /*后台换货订单列表*/
    List<OrderRefundDoc> changeGoodList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    
    int OrderRefundAccount( Date startTimes, Date endTimes,@Param("shopId")Integer shopId,  @Param("status") Integer status);
    
    //cheng
    List<OrderRefundDoc> selectRefundList(OrderRefundDoc doc);
    OrderRefundDoc selectByOrderSkuId(OrderRefundDoc doc);
    int selectCountByOrderShopId(@Param("orderShopId") int orderShopId);
    List<OrderRefundDoc> selectOrderRefundDoc(OrderRefundDoc doc);
    //商家今日的退款(包含退货退款) 的钱
    Integer selectMThisDayRefundMoney(@Param("shopId")Integer shopId);
    //商家这个月的退款(包含退货退款) 
    Integer selectMThisMonthRefundMoney(@Param("shopId")Integer shopId);
    Integer selectMRe(@Param("shopId")Integer shopId,@Param("group")String group);
    OrderRefundDoc getStatusByOrderShopId(Integer orderShopId);
    
    
    
    //统计-出账列表  xieyc
	List<OrderRefundDoc> countRefundList(@Param("id")Integer id, @Param("order_id") Integer order_id, @Param("order_no") String order_no, @Param("payment_no") String payment_no,
			@Param("status") Integer status, Date startTime, Date endTime,@Param("shop_id") Integer shop_id);
	//查询退款成功订单 xieyc
	List<OrderRefundDoc> getRefundOrder(@Param("shop_id") Integer shop_id, @Param("status") Integer status);
	
	//获取某个商家在该7日类下单的订单,并且在该7日类退款成功的金额
	List<OrderRefundDoc> getRefundOrderWithDay(Integer shop_id,Integer status,Date endTime,Date startTime);

	List<OrderRefundDoc> mgetRefundList(Integer shopId);

	List<OrderRefundDoc> getRefundOrderByTime(Integer shopId, Integer refundStatus, Date startTime, Date endTime);
	
	//根据order_sku_id 获取信息 zlk 2018.4.20
	List<OrderRefundDoc> getByOrderSkuId(Integer orderShopId);

	List<OrderRefundDoc> getByOrderShopIdByTime(Integer id, Date parse, Date parse2);

	double getRealAmount(Integer id);

	int getRefundAmount(@Param("startTime") Date startTime,@Param("endTime")  Date endTime, @Param("shopId") Integer shopId);

	int getRefundMoneyByOrderShopId(Integer id);

	int getRefunMoneyByShopId(Integer shopId);

	int getSucessRefundMoneyByOrderShopId(Integer id);
	
}