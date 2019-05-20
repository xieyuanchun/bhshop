package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.TopicDauctionLog;

public interface TopicDauctionLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicDauctionLog record);

    int insertSelective(TopicDauctionLog record);

    TopicDauctionLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicDauctionLog record);

    int updateByPrimaryKey(TopicDauctionLog record);
    
    
    
    List<TopicDauctionLog> listPage(TopicDauctionLog record);
}