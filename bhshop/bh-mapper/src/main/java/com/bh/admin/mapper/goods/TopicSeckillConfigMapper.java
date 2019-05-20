package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.TopicSeckillConfig;

public interface TopicSeckillConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicSeckillConfig record);

    int insertSelective(TopicSeckillConfig record);

    TopicSeckillConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicSeckillConfig record);

    int updateByPrimaryKey(TopicSeckillConfig record);
  
	TopicSeckillConfig selectByTgid(Integer id);
}