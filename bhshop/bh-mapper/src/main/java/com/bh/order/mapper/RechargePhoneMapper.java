package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.RechargePhone;



public interface RechargePhoneMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargePhone record);

    int insertSelective(RechargePhone record);

    RechargePhone selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargePhone record);

    int updateByPrimaryKey(RechargePhone record);
    
    List<RechargePhone> listPage(RechargePhone record);
    
    List<RechargePhone> getByOrderNo(RechargePhone record);
    
    List<RechargePhone> getOrderDetails(RechargePhone record);
}