package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.UserAttendance;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.MyTime;
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
	
	//滨惠豆明细列表
	Map<String, Object> selectBeanList(Integer mId ,String currentPage,String pageSize,String log,String time)throws Exception;
	
	List<MyTime> searchTime()throws Exception;
}
