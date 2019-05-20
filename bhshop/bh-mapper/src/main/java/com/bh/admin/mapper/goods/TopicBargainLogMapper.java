package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.TopicBargainLog;

public interface TopicBargainLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicBargainLog record);

    int insertSelective(TopicBargainLog record);

    TopicBargainLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicBargainLog record);

    int updateByPrimaryKey(TopicBargainLog record);
    
    
    
    int countBytgId(Integer tgId, String bargainNo);
    
    TopicBargainLog selectByBargainNoAndMid(String bargainNo, Integer mId);
    
    TopicBargainLog selectByBargainNoAndOpenId(String bargainNo, String openId);
    
    TopicBargainLog selectByBargainNoAndOwner(String bargainNo);
    
    List<TopicBargainLog> selectByTgIdAndMidAndOwner(Integer tgId, Integer mId, Integer isOwner);
    
    List<TopicBargainLog> selectByTgIdAndOpenIdAndOwner(Integer tgId, String openId, Integer isOwner);
    
    List<TopicBargainLog> selectByMidAndOwner(Integer mId, Integer isOwner);
    
    List<TopicBargainLog> listPage(TopicBargainLog record);
    
    List<TopicBargainLog> selectByBargainNo(String bargainNo);
    
    //当天帮砍次数统计
    int bargainCount(String openId);
    
    //检测活动过期时使用
    List<TopicBargainLog> selectByTgidAndStatus(Integer tgId);
    
}