package com.bh.goods.mapper;

import com.bh.goods.pojo.TopicPrizeConfig;
import com.bh.goods.pojo.TopicSavemoneyConfig;



public interface TopicSavemoneyConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicSavemoneyConfig record);

    int insertSelective(TopicSavemoneyConfig record);

    TopicSavemoneyConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicSavemoneyConfig record);

    int updateByPrimaryKey(TopicSavemoneyConfig record);
    
    TopicSavemoneyConfig selectByTgid(Integer id);
}