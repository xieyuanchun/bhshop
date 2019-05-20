package com.bh.admin.service;

import com.bh.admin.pojo.goods.TopicSeckillLog;
import com.bh.utils.PageBean;

public interface TopicSeckillLogService {
   //添加
   Integer add(TopicSeckillLog entity);
   //获取
   TopicSeckillLog get(Integer id);
   //列表
   PageBean<TopicSeckillLog> listPage(TopicSeckillLog entity);

}
