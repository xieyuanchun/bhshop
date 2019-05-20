package com.bh.admin.service;

import java.util.Map;

import com.bh.admin.pojo.goods.TopicDauctionLog;
import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.admin.pojo.goods.TopicSavemoneyLog;
import com.bh.admin.pojo.goods.TopicSeckillLog;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.utils.PageBean;

public interface OrderTeamService {
	/*后台拼团订单管理列表*/
	PageBean<OrderTeam> pageList(String pageSize, String currentPage, String teamNo, String status, int shopId, String orderNo) throws Exception;
	
	/*后台拼团订单*/
	OrderTeam getDetails (String id) throws Exception;
	
	/*移动端商品详情获取团购人数和前十条列表*/
	Map<String, Object> getGroupNumAndList(String goodsId, Member member) throws Exception;
	
	int testInsert (String teamNo) throws Exception;
	
	/*判断当前用户是否在团中*/
	boolean isInGroup(Member member, String teamNo) throws Exception;
	
	/*团购单的插入*/
	int insertGroupOrder (String orderNo, String teamNo) throws Exception;
	
	OrderTeam getGroupUserHead(String teamNo) throws Exception;
	
	//添加
	Integer add(TopicPrizeLog entity) throws Exception;
	
	Integer addTopicSaveMoney(TopicSavemoneyLog entity) throws Exception;
	
	//添加
	int addSeckillLog(TopicSeckillLog entity) throws Exception;
	
	PageBean<OrderTeam> listPage(OrderTeam entity) throws Exception;
	
	void insertMemberNotice(Integer mId,String orderNo) throws Exception;
	
	Integer insertDauctionLog(TopicDauctionLog entity) throws Exception;

	Map<String, Object> listPageSecond(OrderTeam entity) throws Exception;
	
	
}
