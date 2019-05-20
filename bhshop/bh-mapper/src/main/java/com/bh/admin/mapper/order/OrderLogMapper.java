package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.OrderLog;


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