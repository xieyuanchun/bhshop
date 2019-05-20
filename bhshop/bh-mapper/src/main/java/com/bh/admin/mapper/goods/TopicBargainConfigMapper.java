package com.bh.admin.mapper.goods;

import com.bh.admin.pojo.goods.TopicBargainConfig;

public interface TopicBargainConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicBargainConfig record);

    int insertSelective(TopicBargainConfig record);

    TopicBargainConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicBargainConfig record);

    int updateByPrimaryKey(TopicBargainConfig record);
    
    
    
    
    
    TopicBargainConfig selectByTgid(Integer tgId);
    
}