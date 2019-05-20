package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.TopicDauction;

public interface TopicDauctionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicDauction record);

    int insertSelective(TopicDauction record);

    TopicDauction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicDauction record);

    int updateByPrimaryKey(TopicDauction record);
    
    
    List<TopicDauction> selectByTgId(int tgId);
}