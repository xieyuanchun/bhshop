package com.bh.order.mapper;

import com.bh.order.pojo.OrderCollectionDoc;

public interface OrderCollectionDocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCollectionDoc record);

    int insertSelective(OrderCollectionDoc record);

    OrderCollectionDoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderCollectionDoc record);

    int updateByPrimaryKeyWithBLOBs(OrderCollectionDoc record);

    int updateByPrimaryKey(OrderCollectionDoc record);
    
    int updateByOrderId(OrderCollectionDoc ocd);
    
    OrderCollectionDoc selectByOrderId(Integer orderId);
}