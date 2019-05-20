package com.bh.goods.mapper;

import com.bh.goods.pojo.ActivityTime;

public interface ActivityTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityTime record);

    int insertSelective(ActivityTime record);

    ActivityTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityTime record);

    int updateByPrimaryKey(ActivityTime record);
}