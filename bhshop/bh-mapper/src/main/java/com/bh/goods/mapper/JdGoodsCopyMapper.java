package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.JdGoodsCopy;

public interface JdGoodsCopyMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(JdGoodsCopy record);

    int insertSelective(JdGoodsCopy record);

    JdGoodsCopy selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(JdGoodsCopy record);

    int updateByPrimaryKey(JdGoodsCopy record);
    
    
    List<JdGoodsCopy> selectAll();
}