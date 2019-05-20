package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyLog;



public interface TopicSavemoneyLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicSavemoneyLog record);

    int insertSelective(TopicSavemoneyLog record);

    TopicSavemoneyLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicSavemoneyLog record);

    int updateByPrimaryKey(TopicSavemoneyLog record);
    
    int logNumByTgId(Integer id);
    
    TopicSavemoneyLog getByTgId(Integer tgId);
    
    List<TopicSavemoneyLog> getByMid(Integer mId);
    
    List<TopicSavemoneyLog> listPage(TopicSavemoneyLog entity);
    
    TopicSavemoneyLog getByMidAndTgId(Integer id, Integer tgId);
    
    //cheng
    List<TopicSavemoneyLog> selectLogByParam(TopicSavemoneyLog log);
    
    List<TopicSavemoneyLog> getByMyNoAndMid(TopicSavemoneyLog entity);
}