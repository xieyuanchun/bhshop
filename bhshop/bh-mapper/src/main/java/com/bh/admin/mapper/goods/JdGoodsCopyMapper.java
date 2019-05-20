package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.JdGoodsCopy;

public interface JdGoodsCopyMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(JdGoodsCopy record);

    int insertSelective(JdGoodsCopy record);

    JdGoodsCopy selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(JdGoodsCopy record);

    int updateByPrimaryKey(JdGoodsCopy record);
    
    
    List<JdGoodsCopy> selectAll();
}