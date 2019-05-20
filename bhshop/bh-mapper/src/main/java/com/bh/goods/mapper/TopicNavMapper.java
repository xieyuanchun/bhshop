package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.TopicNav;

public interface TopicNavMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicNav record);

    int insertSelective(TopicNav record);

    TopicNav selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicNav record);

    int updateByPrimaryKey(TopicNav record);

	List<TopicNav> listPage(TopicNav entity);
}