package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.MessageQ;

public interface MessageQMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageQ record);

    int insertSelective(MessageQ record);

    MessageQ selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageQ record);

    int updateByPrimaryKey(MessageQ record);
}