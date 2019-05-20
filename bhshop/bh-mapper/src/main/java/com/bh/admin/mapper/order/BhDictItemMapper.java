package com.bh.admin.mapper.order;

import java.util.List;

import com.bh.admin.pojo.order.BhDictItem;

public interface BhDictItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BhDictItem record);

    int insertSelective(BhDictItem record);

    BhDictItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BhDictItem record);

    int updateByPrimaryKey(BhDictItem record);

	BhDictItem getByItemName(String expressName);


	List<BhDictItem> getListByDicId(int i);
}