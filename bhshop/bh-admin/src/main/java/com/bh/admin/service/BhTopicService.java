package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.UserAttendance;
import com.bh.admin.pojo.user.MemerScoreLog;
import com.bh.utils.PageBean;

public interface BhTopicService {
	
	PageBean<TopicGoods> selectGoodsListByBhBean(TopicGoods goods) throws Exception;
	
	public String waiTime() throws Exception;
	public List<Topic> selectTopicImage();
	
	//查询所有的可用滨惠豆抵消的商品
	PageBean<GoodsSku> selectDouGoods(GoodsSku goodsSku) throws Exception;
	
	//是否签到：1，已签到，0为签到
	UserAttendance isAttendances(MemerScoreLog log) throws Exception;
	
	//签到的接口
	int attendances(MemerScoreLog log) throws Exception;
}
