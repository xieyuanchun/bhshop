package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.JdOrderSku;

public interface JdOrderSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdOrderSku record);

    int insertSelective(JdOrderSku record);

    JdOrderSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdOrderSku record);

    int updateByPrimaryKey(JdOrderSku record);

	JdOrderSku getByOrderSkuId(Integer id);

	JdOrderSku getByJdMainIdAndSkuId(Integer id, long long1);

	List<JdOrderSku> getByJdMainId(Integer id);
}