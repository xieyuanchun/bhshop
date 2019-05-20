package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.JdOrderSku;

public interface JdOrderSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdOrderSku record);

    int insertSelective(JdOrderSku record);

    JdOrderSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdOrderSku record);

    int updateByPrimaryKey(JdOrderSku record);

	List<JdOrderSku> getByJdMainId(Integer id);

	JdOrderSku getByOrderSkuId(Integer id);
}