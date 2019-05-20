package com.bh.admin.service;


import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.utils.PageBean;

public interface TopicPrizeLogService {
   //添加
   Integer add(TopicPrizeLog entity);
   //列表
   PageBean<TopicPrizeLog> listPage(TopicPrizeLog entity);
  
}
