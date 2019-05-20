package com.bh.goods.mapper;

import java.util.List;
import com.bh.goods.pojo.TopicGoods;

public interface TopicGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicGoods record);

    int insertSelective(TopicGoods record);

    TopicGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicGoods record);

    int updateByPrimaryKey(TopicGoods record);
    
    List<TopicGoods> listPage(TopicGoods record);
    
    int countByActId(Integer actId);
    
    List<TopicGoods> listPageTopicBargain(TopicGoods record);
    
    List<TopicGoods> getByActId(Integer actId);
    
    List<TopicGoods> getByActIdAndNotDelete(Integer actId);
    
    //根据活动类型获取商品列表--1竞价活动，2专题活动，3砍价活动'
    List<TopicGoods> getByType(Integer type);
    
    List<TopicGoods> selectByGoodsId(Integer goodsId);

	
	//cheng
	//查询所有的商品
	List<TopicGoods> selectGoodsByParams(TopicGoods topicGoods);
	List<TopicGoods> selectTopicGoodsBySaveMoney(TopicGoods topicGoods);
	List<TopicGoods> selectByPaiMaiGoodsId(Integer goodsId);


	List<TopicGoods> todayListTopicSeckill(TopicGoods topicGoods);//今日秒杀活动报名列表 xxj
	List<TopicGoods> tomorrowListTopicSeckill(TopicGoods topicGoods);//明日秒杀活动报名列表 xxj
	List<TopicGoods> houdayListTopicSeckill(TopicGoods topicGoods);//后日秒杀活动报名列表 xxj

	List<TopicGoods> todayBeginNoEndSeckill(TopicGoods topicGoods);

	List<TopicGoods> todayNoBeginSeckill(TopicGoods topicGoods);

	List<TopicGoods> todayBeginNoEndSeckillDesc(TopicGoods topicGoods);

}