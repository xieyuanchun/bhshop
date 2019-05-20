package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.ExchangeSku;

public interface ExchangeSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSku record);

    int insertSelective(ExchangeSku record);

    ExchangeSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSku record);

    int updateByPrimaryKey(ExchangeSku record);
    
    List<ExchangeSku> pageList(ExchangeSku record);
}