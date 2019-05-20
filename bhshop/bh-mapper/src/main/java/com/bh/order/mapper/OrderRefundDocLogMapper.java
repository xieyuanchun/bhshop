package com.bh.order.mapper;

import com.bh.order.pojo.OrderRefundDocLog;

public interface OrderRefundDocLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefundDocLog record);

    int insertSelective(OrderRefundDocLog record);

    OrderRefundDocLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefundDocLog record);

    int updateByPrimaryKeyWithBLOBs(OrderRefundDocLog record);

    int updateByPrimaryKey(OrderRefundDocLog record);
}