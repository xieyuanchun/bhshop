package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.OrderLog;

public interface OrderLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLog record);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);
    
    
    List<OrderLog> getAllList(OrderLog record);
    
    int countAll(String orderNo, String action);
}