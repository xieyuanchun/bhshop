package com.bh.goods.mapper;

import com.bh.goods.pojo.TopicBargainConfig;

public interface TopicBargainConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicBargainConfig record);

    int insertSelective(TopicBargainConfig record);

    TopicBargainConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicBargainConfig record);

    int updateByPrimaryKey(TopicBargainConfig record);
    
    
    
    
    
    TopicBargainConfig selectByTgid(Integer tgId);
    
}