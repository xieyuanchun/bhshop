package com.bh.order.mapper;

import java.util.List;

import com.bh.order.pojo.ExchangeGood;
import com.bh.order.pojo.ExchangeSku;


public interface ExchangeSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSku record);

    int insertSelective(ExchangeSku record);

    ExchangeSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSku record);

    int updateByPrimaryKey(ExchangeSku record);
    
    List<ExchangeGood> get(int id);
}