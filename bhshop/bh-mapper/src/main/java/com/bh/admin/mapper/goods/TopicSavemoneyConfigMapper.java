package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.TopicPrizeConfig;
import com.bh.admin.pojo.goods.TopicSavemoneyConfig;



public interface TopicSavemoneyConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicSavemoneyConfig record);

    int insertSelective(TopicSavemoneyConfig record);

    TopicSavemoneyConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicSavemoneyConfig record);

    int updateByPrimaryKey(TopicSavemoneyConfig record);
    
    TopicSavemoneyConfig selectByTgid(Integer id);
}