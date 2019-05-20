package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.TopicGoods;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
public interface TopicSeckillService {

	int addTopicSeckill(TopicGoods topicGoods) throws Exception;//秒杀活动立即报名

	PageBean<TopicGoods> listTopicSeckill(TopicGoods topicGoods);//秒杀活动报名列表
	Map<String,Object> todayListTopicSeckill(TopicGoods topicGoods,Member member);//今日秒杀活动报名列表
	Map<String,Object> tomorrowListTopicSeckill(TopicGoods topicGoods,Member member);//明日秒杀活动报名列表
	Map<String,Object> houdayListTopicSeckill(TopicGoods topicGoods,Member member);//后日秒杀活动报名列表
	
	
}
