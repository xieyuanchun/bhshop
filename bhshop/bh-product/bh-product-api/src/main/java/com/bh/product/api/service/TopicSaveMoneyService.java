package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface TopicSaveMoneyService {

	
	int addTopicSaveMoney(TopicGoods topicGoods);
	
	
	PageBean<TopicGoods> listTopicSaveMoney(TopicGoods topicGoods);//惠省钱活动报名列表
	
	Map<String,Object> apiTopicGoodsList(TopicGoods entity,Member member);//移动端惠省钱商品列表
	
	PageBean<Map<String, Object>> apiMyTopicSaveMoney(TopicGoods entity, Integer id);//我的惠省钱活动列表
	
	Integer doSaveMoney(TopicSavemoneyLog entity); //活动成功或者失败操作
	
	
}
