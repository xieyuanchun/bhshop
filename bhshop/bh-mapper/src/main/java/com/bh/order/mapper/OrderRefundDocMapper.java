package com.bh.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.order.pojo.OrderRefundDoc;

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
    
    OrderRefundDoc getByOrderShopId(Integer orderShopId);
    
    List<OrderRefundDoc> getByShopId(Integer shopId, Integer status);
    
    /*后台退款订单列表*/
    List<OrderRefundDoc> orderRefundList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    
    /*后台退货订单列表*/
    List<OrderRefundDoc> refundGoodList(@Param("shopId")Integer shopId, @Param("status") String status, String order_no, Date startTime, Date endTime);
    
    int OrderRefundAccount( Date startTimes, Date endTimes,@Param("shopId")Integer shopId,  @Param("status") Integer status);
    
    //cheng
    List<OrderRefundDoc> selectRefundList(OrderRefundDoc doc);
    OrderRefundDoc selectByOrderSkuId(OrderRefundDoc doc);
    OrderRefundDoc selectByOrderSkuId1(Integer orderSkuId);
    int selectCountByOrderShopId(@Param("orderShopId") int orderShopId);
    List<OrderRefundDoc> selectOrderRefundDoc(OrderRefundDoc doc);
    //商家今日的退款(包含退货退款) 的钱
    Integer selectMThisDayRefundMoney(Integer shopId);
    //商家这个月的退款(包含退货退款) 
    Integer selectMThisMonthRefundMoney(Integer shopId);
    Integer selectMRe(@Param("shopId")Integer shopId,@Param("group")String group);
    //判断失败的单的数量
    int selectFailTeamCount(OrderRefundDoc doc);
    //退款的数量
    int selectRefundCount(OrderRefundDoc doc);
    
    
    
    //统计-出账列表  xieyc
	List<OrderRefundDoc> countRefundList(@Param("id")Integer id, @Param("order_id") Integer order_id, @Param("order_no") String order_no, @Param("payment_no") String payment_no,
			@Param("status") Integer status, Date startTime, Date endTime,@Param("shop_id") Integer shop_id);
	//查询退款成功订单 xieyc
	List<OrderRefundDoc> getRefundOrder(@Param("shop_id") Integer shop_id, @Param("status") Integer status);
	
	//获取某个商家在该7日类下单的订单,并且在该7日类退款成功的金额
	List<OrderRefundDoc> getRefundOrderWithDay(Integer shop_id,Integer status,Date endTime,Date startTime);

	List<OrderRefundDoc> mgetRefundList(Integer shopId);
	
	//根据order_sku_id 获取信息 zlk 2018.4.20
	OrderRefundDoc getByOrderSkuId(Integer orderShopId);
	
	//查询该商品是否申请过售后服务
	Integer selectIsRefund(@Param("orderSkuId")Integer orderSkuId);
	Integer selectOShopStatus(@Param("orderSkuId")Integer orderSkuId);
	List<OrderRefundDoc> selectRefundType(@Param("orderSkuId")Integer orderSkuId);

	int countByOrderId(Integer id);

	int countMoneyByOrderShopId(Integer id);
	
	int updateLogistics(OrderRefundDoc orderRefundDoc);
	
	int updateStatus(OrderRefundDoc orderRefundDoc);
	
	List<OrderRefundDoc> selectByStatus();
	
	int updateTime(OrderRefundDoc ord);
	
	
}