package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.TopicDauction;

public interface TopicDauctionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicDauction record);

    int insertSelective(TopicDauction record);

    TopicDauction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicDauction record);

    int updateByPrimaryKey(TopicDauction record);
    
    
    List<TopicDauction> selectByTgId(int tgId);
}