package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.OrderRefundDocStep;



public interface OrderRefundDocStepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefundDocStep record);

    int insertSelective(OrderRefundDocStep record);

    OrderRefundDocStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefundDocStep record);

    int updateByPrimaryKey(OrderRefundDocStep record);
    
    List<OrderRefundDocStep> getByOrderRefundDocId(OrderRefundDocStep record);
}