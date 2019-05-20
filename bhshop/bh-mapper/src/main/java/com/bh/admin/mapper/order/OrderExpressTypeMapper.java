package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.OrderExpressType;


public interface OrderExpressTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderExpressType record);

    int insertSelective(OrderExpressType record);

    OrderExpressType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderExpressType record);

    int updateByPrimaryKey(OrderExpressType record);
    
    List<OrderExpressType> selectAllOrderExpressType();
}