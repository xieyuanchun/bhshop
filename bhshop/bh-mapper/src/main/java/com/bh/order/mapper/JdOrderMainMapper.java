package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.JdOrderMain;

public interface JdOrderMainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdOrderMain record);

    int insertSelective(JdOrderMain record);

    JdOrderMain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdOrderMain record);

    int updateByPrimaryKey(JdOrderMain record);

	JdOrderMain getJdOrderMainByJdSkuId(Long jdSkuId, Integer id);

	JdOrderMain getByOrderSkuId(Integer id);

	List<JdOrderMain> getUpdateJdOrderMain();
}