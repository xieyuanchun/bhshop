package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.TopicPrizeConfig;

public interface TopicPrizeConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicPrizeConfig record);

    int insertSelective(TopicPrizeConfig record);

    TopicPrizeConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicPrizeConfig record);

    int updateByPrimaryKey(TopicPrizeConfig record);

	TopicPrizeConfig selectByTgid(Integer id);
}