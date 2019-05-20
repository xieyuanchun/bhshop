package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface TopicGoodsService {
   //添加
   int add(TopicGoods entity);
   //更新
   int update(TopicGoods entity);
   //获取
   TopicGoods get(TopicGoods entity);
   //列表
   PageBean<TopicGoods> listPage(TopicGoods entity);
   //删除
   int delete(TopicGoods entity);
   //审核
   int audit(TopicGoods entity);
   
   
   //砍价活动商品配置
   int addTopicBargain(TopicGoods entity);
   
   //砍价活动商品列表
   PageBean<TopicGoods> listPageTopicBargain(TopicGoods entity);
   
   //移动端砍价活动专区商品列表
   PageBean<TopicGoods> apiTopicGoodsList(TopicGoods entity, Member member);
   
   //砍价详情
   Map<String, Object> bargainAfterPage(TopicGoods entity, int mId);
   
   //我的砍价列表
   PageBean<Map<String, Object>> apiMyTopic(TopicGoods entity, int mId);
   
   //砍价详情
   Map<String, Object> wxBargainAfterPage(TopicGoods entity, Member member);
   
   //移动端荷兰拍卖专区商品列表
   PageBean<TopicGoods> apiDautionGoodsList(TopicGoods entity, Member member);
   
   //下架活动
   int downTopic(Topic entity);
}
