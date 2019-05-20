package com.order.shop.service;

import com.bh.goods.pojo.TopicDauctionLog;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.goods.pojo.TopicSeckillLog;
import com.bh.order.pojo.OrderTeam;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

import java.util.Map;

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
	
	//判断该商品促团时是否有货或者是否限制购买区域 2018-5-25
	int isTeamPromoteSucess(String TeamNo)throws Exception;

	void updateAuctionStatus(String out_trade_no);


}
