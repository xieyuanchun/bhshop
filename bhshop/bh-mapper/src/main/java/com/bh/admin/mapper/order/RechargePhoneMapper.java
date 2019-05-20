package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.RechargePhone;

public interface RechargePhoneMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargePhone record);

    int insertSelective(RechargePhone record);

    RechargePhone selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargePhone record);

    int updateByPrimaryKey(RechargePhone record);
    
    List<RechargePhone> listPage(RechargePhone record);
}
