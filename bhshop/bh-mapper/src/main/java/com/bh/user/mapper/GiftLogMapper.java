package com.bh.user.mapper;

import java.util.Calendar;
import java.util.List;

import com.bh.user.pojo.GiftLog;

public interface GiftLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftLog record);

    int insertSelective(GiftLog record);

    GiftLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GiftLog record);

    int updateByPrimaryKey(GiftLog record);

	List<GiftLog> getGiftLog(GiftLog findGiftLog);
}