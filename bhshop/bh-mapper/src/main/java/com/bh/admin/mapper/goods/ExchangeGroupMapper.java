package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.ExchangeGroup;

public interface ExchangeGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeGroup record);

    int insertSelective(ExchangeGroup record);

    ExchangeGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeGroup record);

    int updateByPrimaryKey(ExchangeGroup record);
    
    List<ExchangeGroup> pageList(ExchangeGroup e);
    
    List<ExchangeGroup> getAll();
    
    ExchangeGroup selectByName(String name);
}