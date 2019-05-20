package com.bh.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;

public interface OrderMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    /*******************************************************************    舒            采                  佳******************************************************************************/
    /*scj-商家后台订单列表*/   
    List<Order> orderList(Integer id, @Param("status") Integer status, String order_no, Date startTime, Date endTime, Integer isRefund);
    
    /*scj-商家后台全部订单列表*/   
    List<Order> orderAllList(Integer id, String order_no, Date startTime, Date endTime);  
    
    /*scj-平台后台订单列表*/   
    List<Order> allList(@Param("status") Integer status, String order_no, Date startTime, Date endTime);
     
    /*scj-批量删除*/
    int batchDelete(List<String> list);
    
    /*scj-批量抛单*/
    int batchCastSheet(List<String> list);
    
    /*审核退款*/
	int auditRefund(String id);
	
	 /*配送端订单列表*/
	List<Order> list(Integer isRefund, Integer dState, String orderNo);
	
	int listCount(Integer isRefund, Integer dState, String orderNo);
	
	/*可配送单列表*/
	List<Order> scopeList(Integer isRefund, Integer dState, String orderNo, Double distance);
	int scopeListCount(Integer isRefund, Integer dState, String orderNo, Double distance);
    
	/*配送端订单列表*/
	List<Order> waitList(Integer isRefund, Integer dState, String orderNo, Integer isShopCheckOut);
	
	int waitListCount(Integer isRefund, Integer dState, String orderNo, Integer isShopCheckOut);
	
	int OrderAccountByTime(@Param("shopId")Integer shopId, Date startTime, Date endTime ,Integer status);
	
	int OrderAccount(@Param("shopId")Integer shopId,Integer status);
	//程凤云
	int waitOrderAccount(Date startTime, Date endTime,@Param("shopId")Integer shopId);
	
	int jdOrderAccount(@Param("shopId")Integer shopId, Integer status);
	
	int OrderCountAllByTime(Date parse, Date parse2,@Param("shopId") Integer shopId);
	int OrderCountAll(@Param("shopId") Integer shopId);
	
	int OrderCountAllAndNotRefund(Integer shopId);
	
	int notComplish(Integer shopId);
	
	int OrderRefundAccount(Integer shopId);
	
	int backgroundOrderAccount(@Param("status") Integer status);
	
	/*用户总计订单（笔）*/
	int countBymId(Integer mId);
	
	/*用户总消费金额*/
	String sumMoneyBymId(Integer mId);
	
	/*用户本月订单（笔）*/
	int countMonthOrder(Integer mId);
	
	/*用户本月消费金额*/
	String sumMonthMoney(Integer mId);
	
	/*用户本月消费订单列表*/
	List<Order> selectBymIdAndMonth(Integer mId);
	
	List<Order> backGroundAllOrderList(Order entity);
	
	

    
    /*********************************************************************    程           凤                  云***************************************************************************/
    //2017-9-18
	//根据用户的mId查询该用户的order_main
	//List<Order> selectAllOrderByUser(Integer mId,Integer pageStart, Integer pageSize);
	List<Order> selectAllOrderByUser(Order order);//根据order_main查询
	Integer selectAllOrderByUserCount(Integer mId);
	//int updateOrderByPrimarykey(@Param("orderIds") List<String> orderIds);
	//2017-9-19 星期二
	//根据用户的mId和订单的Id查询该用户的order_main
	void updateOrderPrice(Order order);
	Order selectOneOrderByUserAndId(Order order);
	Order selectAllOrderByParams(Order order);//根据order_main查询
	Order selectOrderByCartId(Order order);
	Order selectOrderBymId(Order order);
	List<Order>  selectOrderByUser(Order order);
	Integer selectTotalAbilityByUser(Order order);
	List<Order> selectOrderBymAddressId(Order order);
	List<Order> selectCountByCardId(Order order);
	/** xxj
	 * 更新订单状态
	 * @param orderNo
	 * @param status
	 */
	void updateStatusByOrderNo(Order record);
	/**
	 * author xxj
     * 根据订单号获取订单
	 * @param orderNo
	 * @return
	 */
	Order getOrderByOrderNo(String orderNo);
	//查询所有平台订单
	List<Order> getAllOrder();

	/** xxj
	 * 更新快递公司信息
	 * @param orderNo
	 * @param status
	 */
	void updateExpressByOrderNo(Order record);
	
	
	List<Order> getMonetByMIdList(@Param("id") Integer id,@Param("startTime") Date startTime,@Param("endTime")Date endTime);

	List<Order> getByTimeAndMid(Order findOrder);

	String getOrderIdByMid(int srcUserId);

	
	
}