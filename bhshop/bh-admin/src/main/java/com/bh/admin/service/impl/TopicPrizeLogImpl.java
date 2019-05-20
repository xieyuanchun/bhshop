package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.goods.TopicPrizeLogMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicPrizeLogService;
import com.bh.admin.util.TopicUtils;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class TopicPrizeLogImpl implements TopicPrizeLogService{
	@Autowired
	private TopicPrizeLogMapper topicPrizeLogMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;

	/**
	 * @Description: 抽奖活动新增日志
	 * @author xieyc
	 * @throws Exception 
	 * @date 2018年1月18日 上午9:31:21 
	 */
	public Integer add(TopicPrizeLog entity)  {
		Order order=orderMapper.getOrderByOrderNo(entity.getOrderNo());   
		this.insertGroupOrder(entity,order);//走拼团向order_team插入记录
		entity.setmId(order.getmId());//用户id
		entity.setOrderId(order.getId());//订单号
		entity.setIsPrize(Contants.WIN_PRIZE_INITIALIZE);//刚参加活动时，先设置中奖状态为初始化
		entity.setPrizeNo(TopicUtils.generatePrizeNum());//中奖号
		entity.setAddTime(new Date());//新增日期
		Integer returnRow=topicPrizeLogMapper.insertSelective(entity);
		return returnRow;
	}

	
	/**
	 * @Description: 抽奖日志管理列表
	 * @author xieyc
	 * @date 2018年1月18日 上午9:46:36 
	 */
	public PageBean<TopicPrizeLog> listPage(TopicPrizeLog entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicPrizeLog> list = topicPrizeLogMapper.listPage(entity);
		if(list!=null&&list.size()>0){
			for(TopicPrizeLog log : list){
				Member member = memberMapper.selectByPrimaryKey(log.getmId());
				if(member!=null){
					log.setmName(member.getUsername());//用户名称
				}
				TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(log.getTgId());
				if(topicGoods!=null){
					Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
					topicGoods.setGoodsImage(goods.getImage());
					topicGoods.setGoodsName(goods.getName());
					List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
					if(skuList!=null&&skuList.size()>0){
						topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
					}else{
						topicGoods.setRealPrice((double)goods.getTeamPrice()/100);
					}
					log.setTopicGood(topicGoods);//活动配置
					Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
					log.setTopicName(topic.getName());
				}
			}
		}
		PageBean<TopicPrizeLog> pageBean = new PageBean<>(list);
		return pageBean;
	}	
	
	/**
	 * @Description: 向order_team插入记录
	 * @author xieyc
	 * @date 2018年1月23日 下午4:31:34 
	 */
	private void insertGroupOrder(TopicPrizeLog entity,Order order) {
		//参加人数
		int joinNum=topicPrizeLogMapper.logNumByTgId(entity.getTgId());

		TopicGoods topicGoods=topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
		
		Topic topic=topicMapper.selectByPrimaryKey(topicGoods.getActId());
		
		String teamNo=entity.getTopicNo();//团号
		
		List<OrderShop> shopList = orderShopMapper.getByOrderId(order.getId());
		List<OrderSku> skuList = orderSkuMapper.getByOrderShopId(shopList.get(0).getId());
		OrderTeam oTeam = orderTeamMapper.getByMidAndTeamNo(order.getmId(),teamNo);
		Goods goods = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
		
		if(oTeam==null){
			//插入拼团订单数据
			OrderTeam orderTeam = new OrderTeam();
			orderTeam.setCreateTime(new Date());//下单时间
			
			orderTeam.setGoodsSkuId(skuList.get(0).getSkuId());//商品skuId
			
			orderTeam.setmId(order.getmId());//用户id
			
			orderTeam.setOrderNo(order.getOrderNo());//订单号

			if(!StringUtils.isEmptyOrWhitespaceOnly(teamNo)){ //参团
				List<OrderTeam> teamList = orderTeamMapper.getByTeamNo(teamNo);
				if(teamList!=null && teamList.size()>0){
					orderTeam.setCreateTeamTime(teamList.get(0).getCreateTeamTime());//创团时间
				}
				orderTeam.setTeamNo(teamNo);//团号
				orderTeam.setIsOwner(0);//是否发起人
				int num =goods.getTeamNum() - joinNum;//剩余名额
				if(num==1){//最后一个名额
					orderTeam.setEndTime(new Date());
					orderTeam.setStatus(1);
					orderTeamMapper.insertSelective(orderTeam);
					if(teamList!=null && teamList.size()>0){
						for(OrderTeam otherTeam : teamList){//拼团成功的时候修改该拼团号的其他拼团订单的状态和开团时间
							otherTeam.setStatus(1);
							otherTeam.setEndTime(orderTeam.getEndTime());
							orderTeamMapper.updateByPrimaryKeySelective(otherTeam);
						}
					}
				}else{//还有好多名额
					if(teamList!=null && teamList.size()>0){
						orderTeam.setEndTime(teamList.get(0).getEndTime());
					}
					orderTeam.setStatus(0);
					orderTeamMapper.insertSelective(orderTeam);
				}
			}else{//开团
				orderTeam.setCreateTeamTime(new Date());
				orderTeam.setStatus(0);
				orderTeam.setTeamNo(TopicUtils.getOrderNo());
				orderTeam.setIsOwner(1);
				orderTeam.setEndTime(topic.getEndTime());//开团时间
				orderTeamMapper.insertSelective(orderTeam);
			}
			
		}
	}
	
	
}
