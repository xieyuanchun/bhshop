package com.bh.goods.mapper;

import com.bh.goods.pojo.TopicSeckillConfig;

public interface TopicSeckillConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicSeckillConfig record);

    int insertSelective(TopicSeckillConfig record);

    TopicSeckillConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicSeckillConfig record);

    int updateByPrimaryKey(TopicSeckillConfig record);
  
	TopicSeckillConfig selectByTgid(Integer id);
}