package com.bh.goods.mapper;

import com.bh.goods.pojo.JdGoods;

public interface JdGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoods record);

    int insertSelective(JdGoods record);

    JdGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoods record);

    int updateByPrimaryKey(JdGoods record);
}