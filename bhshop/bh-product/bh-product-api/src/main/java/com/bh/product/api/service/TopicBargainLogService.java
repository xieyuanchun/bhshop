package com.bh.product.api.service;

import com.bh.goods.pojo.TopicBargainLog;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface TopicBargainLogService {
   //添加
   int add(TopicBargainLog entity, Member member);
   //获取
   TopicBargainLog get(Integer id);
   //列表
   PageBean<TopicBargainLog> listPage(TopicBargainLog entity);
   //扫描活动有效期
   int checkTimeChangeStatus();
   
   
   
   //添加
   int wxAdd(TopicBargainLog entity, Member member);
}
