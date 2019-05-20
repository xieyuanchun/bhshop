package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.OrderPayment;


public interface OrderPaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderPayment record);

    int insertSelective(OrderPayment record);

    OrderPayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderPayment record);

    int updateByPrimaryKey(OrderPayment record);
    
    List<OrderPayment> selectAllOrderPayment();
    
    List<OrderPayment> selectAllOrderPayments(OrderPayment orderPayment);
    
    //cheng
    int updateOrderPaymentStatus(OrderPayment orderPayment);
}