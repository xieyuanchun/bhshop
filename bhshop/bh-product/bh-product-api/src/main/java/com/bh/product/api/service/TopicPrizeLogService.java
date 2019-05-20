package com.bh.product.api.service;


import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.utils.PageBean;

public interface TopicPrizeLogService {
   //添加
   Integer add(TopicPrizeLog entity);
   //列表
   PageBean<TopicPrizeLog> listPage(TopicPrizeLog entity);
  
}
