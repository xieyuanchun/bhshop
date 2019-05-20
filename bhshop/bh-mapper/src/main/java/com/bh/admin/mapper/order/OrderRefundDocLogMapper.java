package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.OrderRefundDocLog;

public interface OrderRefundDocLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefundDocLog record);

    int insertSelective(OrderRefundDocLog record);

    OrderRefundDocLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefundDocLog record);

    int updateByPrimaryKeyWithBLOBs(OrderRefundDocLog record);

    int updateByPrimaryKey(OrderRefundDocLog record);
}