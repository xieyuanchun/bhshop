package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.OrderCollectionDoc;

public interface OrderCollectionDocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCollectionDoc record);

    int insertSelective(OrderCollectionDoc record);

    OrderCollectionDoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderCollectionDoc record);

    int updateByPrimaryKeyWithBLOBs(OrderCollectionDoc record);

    int updateByPrimaryKey(OrderCollectionDoc record);
}