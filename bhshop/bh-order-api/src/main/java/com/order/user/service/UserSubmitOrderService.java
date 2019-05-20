package com.order.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bh.order.pojo.Order;
import com.bh.user.pojo.Member;

public interface UserSubmitOrderService {
	
	
	//用户提交订单前需要做最后的校验工作
	public Map<String, Object> rendOrder(Map<String, String> map, Member member, HttpServletRequest request);
	
	//生成订单
	Order insertOrderBySelective(Order order, String fz, String teamNo) throws Exception;
	
	//获得优惠劵信息
	public Map<String, Object> selectCouponMsg(Integer shopId,String couponLogStr);
	
	public Order getOrder(Order myOrder);
	public Order getOrder1(Order myOrder);
	
	
	/**
	 * @Description: 优惠卷抵扣金额(在使用优惠劵的前提下)
	 * @author 程凤云
	 * @date 2018年06月13日
	 * @param ouponType:优惠劵的类型:1普通券(满减)，2免邮券，3红包券
	 * @param catId:分类id
	 * @param couponPrice:优惠劵的金额
	 * @param orderShopId:优惠劵的类型:1普通券(满减)，2免邮券，3红包券
	 * @param shopId:商家的id
	 * @param logId:couponLog表的id
	 * @param gDeliveryPrice:邮费
	 */
	public void couponMony(Integer ouponType,Long catId,Integer couponPrice,Integer orderShopId,Integer shopId,Integer logId,Integer gDeliveryPrice,Integer orderPrice);
	/**
	 * @Description: 计算skuPayPrice的值
	 * @author 程凤云
	 * @date 2018年06月14日
	 * @param orderId:订单的id(也就是order_main表的id)
	 */
	public void updatePayPrice(Integer orderId);
}
