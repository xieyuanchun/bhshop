package com.bh.order.mapper;

import com.bh.order.pojo.OrderDeliveryDoc;

public interface OrderDeliveryDocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliveryDoc record);

    int insertSelective(OrderDeliveryDoc record);

    OrderDeliveryDoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliveryDoc record);

    int updateByPrimaryKeyWithBLOBs(OrderDeliveryDoc record);

    int updateByPrimaryKey(OrderDeliveryDoc record);
}