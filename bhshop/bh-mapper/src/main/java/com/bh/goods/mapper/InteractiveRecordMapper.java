package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.InteractiveRecord;

public interface InteractiveRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InteractiveRecord record);

    int insertSelective(InteractiveRecord record);

    InteractiveRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InteractiveRecord record);

    int updateByPrimaryKey(InteractiveRecord record);

	List<InteractiveRecord> getListByMsgId(Integer id);

	List<InteractiveRecord> getList(Integer id, Integer valueOf);
}