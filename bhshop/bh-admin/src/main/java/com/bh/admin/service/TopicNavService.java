package com.bh.admin.service;


import com.bh.admin.pojo.goods.TopicNav;
import com.bh.utils.PageBean;

public interface TopicNavService {
   //添加
   int add(TopicNav entity);
   //更新
   int update(TopicNav entity);
   //获取
   TopicNav get(Integer id);
   //列表
   PageBean<TopicNav> listPage(TopicNav entity);
   //删除
   int delete(Integer id);
}
