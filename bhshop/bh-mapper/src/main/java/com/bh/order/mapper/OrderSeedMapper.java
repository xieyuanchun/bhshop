package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.OrderSeed;

public interface OrderSeedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSeed record);

    int insertSelective(OrderSeed record);

    OrderSeed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSeed record);

    int updateByPrimaryKey(OrderSeed record);
    
    int updateByStatus(OrderSeed seed);
    
    //cheng
    List<OrderSeed> selecOrderSeedByParam(OrderSeed orderSeed);
    int updateOrderSeedBymId(OrderSeed orderSeed);
    OrderSeed selectSeedByOrderNo(String orderNo);
}