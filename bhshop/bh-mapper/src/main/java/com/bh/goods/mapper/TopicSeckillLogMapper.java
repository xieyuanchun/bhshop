package com.bh.goods.mapper;

import java.util.List;


import com.bh.goods.pojo.TopicSeckillLog;

public interface TopicSeckillLogMapper {
	    int deleteByPrimaryKey(Integer id);
	    int insert(TopicSeckillLog record);
	    int insertSelective(TopicSeckillLog record);
	    TopicSeckillLog selectByPrimaryKey(Integer id);
	    int updateByPrimaryKeySelective(TopicSeckillLog record);
	    int updateByPrimaryKey(TopicSeckillLog record);
	    List<TopicSeckillLog> listPage(TopicSeckillLog record);
	    
	    
	    List<TopicSeckillLog> getSeckillLog(Integer tgId);
		TopicSeckillLog getByMidAndTgId(Integer mId, Integer tgId);
	
}