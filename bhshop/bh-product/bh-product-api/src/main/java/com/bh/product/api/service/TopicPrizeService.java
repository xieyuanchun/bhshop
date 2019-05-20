package com.bh.product.api.service;

import java.util.Map;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface TopicPrizeService {

	int addTopicPrize(TopicGoods topicGoods) throws Exception;//抽奖活动立即报名

	PageBean<TopicGoods> listTopicPrize(TopicGoods topicGoods);//抽奖活动报名列表

	Map<String,Object> apiTopicGoodsList(TopicGoods entity,Member member);//移动端抽奖活动商品列表

	PageBean<Map<String, Object>> apiMyTopicPrize(TopicGoods entity, Integer id);//我的抽奖活动列表

	Integer beganPrize(TopicPrizeLog entity);//抽奖活动随机取一个抽奖号码,并更改抽奖初始化状态0（ 是否中奖 :0 初始化  1 否  2 是）

	

}
