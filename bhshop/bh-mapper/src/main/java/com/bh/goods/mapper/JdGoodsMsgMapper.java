package com.bh.goods.mapper;

import com.bh.goods.pojo.JdGoodsMsg;

public interface JdGoodsMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoodsMsg record);

    int insertSelective(JdGoodsMsg record);

    JdGoodsMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoodsMsg record);

    int updateByPrimaryKey(JdGoodsMsg record);
}