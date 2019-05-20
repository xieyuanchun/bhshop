package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.InteractiveRecord;


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