package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.OrderRefundDocStep;

public interface OrderRefundDocStepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefundDocStep record);

    int insertSelective(OrderRefundDocStep record);

    OrderRefundDocStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefundDocStep record);

    int updateByPrimaryKey(OrderRefundDocStep record);
    
    List<OrderRefundDocStep> getByOrderRefundDocId(OrderRefundDocStep record);
}