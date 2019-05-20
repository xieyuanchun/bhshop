package com.bh.admin.mapper.order;

import com.bh.admin.pojo.order.JdOrderMain;

public interface JdOrderMainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdOrderMain record);

    int insertSelective(JdOrderMain record);

    JdOrderMain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdOrderMain record);

    int updateByPrimaryKey(JdOrderMain record);

	JdOrderMain getJdOrderMainByJdSkuId(long parseLong, int parseInt);

	JdOrderMain getByOrderSkuId(Integer id);


}