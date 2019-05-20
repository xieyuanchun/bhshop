package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.Topic;
import com.bh.utils.PageBean;

public interface TopicService {
   //添加
   int add(Topic entity);
   //更新
   int update(Topic entity);
   //获取
   Topic get(Integer id);
   //列表
   PageBean<Topic> listPage(Topic entity);
   //删除
   int delete(Integer id);
   
   int getValid(Topic entity);
   
   List<Topic> selectListByType(Topic entity);
}
