package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.JdGoodsMsg;

public interface JdGoodsMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoodsMsg record);

    int insertSelective(JdGoodsMsg record);

    JdGoodsMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoodsMsg record);

    int updateByPrimaryKey(JdGoodsMsg record);
    
    
    List<JdGoodsMsg> listPage(JdGoodsMsg record);
}
