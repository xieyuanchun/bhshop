package com.bh.order.mapper;

import com.bh.order.pojo.BhDictItem;

public interface BhDictItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BhDictItem record);

    int insertSelective(BhDictItem record);

    BhDictItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BhDictItem record);

    int updateByPrimaryKey(BhDictItem record);

	BhDictItem getByItemName(String expressName);
}