package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.TopicPrizeLog;

public interface TopicPrizeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicPrizeLog record);

    int insertSelective(TopicPrizeLog record);

    TopicPrizeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicPrizeLog record);

    int updateByPrimaryKey(TopicPrizeLog record);
  
	List<TopicPrizeLog> listPage(TopicPrizeLog entity);
	
	List<TopicPrizeLog> getByMid(Integer mId);

	int logNumByTgId(Integer id);

	TopicPrizeLog getByMidAndTgId(Integer id, Integer tgId);
}