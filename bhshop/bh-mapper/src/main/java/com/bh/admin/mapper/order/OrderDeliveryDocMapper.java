package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.OrderDeliveryDoc;

public interface OrderDeliveryDocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliveryDoc record);

    int insertSelective(OrderDeliveryDoc record);

    OrderDeliveryDoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliveryDoc record);

    int updateByPrimaryKeyWithBLOBs(OrderDeliveryDoc record);

    int updateByPrimaryKey(OrderDeliveryDoc record);
}