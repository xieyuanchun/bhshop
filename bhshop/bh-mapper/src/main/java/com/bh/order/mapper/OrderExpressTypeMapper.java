package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.OrderExpressType;

public interface OrderExpressTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderExpressType record);

    int insertSelective(OrderExpressType record);

    OrderExpressType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderExpressType record);

    int updateByPrimaryKey(OrderExpressType record);
    
    List<OrderExpressType> selectAllOrderExpressType();
}