package com.bh.goods.mapper;

import com.bh.goods.pojo.BargainRecord;

public interface BargainRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BargainRecord record);

    int insertSelective(BargainRecord record);

    BargainRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BargainRecord record);

    int updateByPrimaryKey(BargainRecord record);

	BargainRecord getByOrderNo(String orderNo);
}