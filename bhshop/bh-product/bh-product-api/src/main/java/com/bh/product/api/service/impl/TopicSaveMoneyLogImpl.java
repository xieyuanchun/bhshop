package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.mapper.TopicSavemoneyConfigMapper;
import com.bh.goods.mapper.TopicSavemoneyLogMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyConfig;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.product.api.service.TopicSaveMoneyLogService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.pojo.Member;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class TopicSaveMoneyLogImpl implements TopicSaveMoneyLogService{

	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private TopicSavemoneyConfigMapper topicSavemoneyConfigMapper;
	@Autowired
	private TopicSavemoneyLogMapper topicSavemoneyLogMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	
	@Override
	public Integer add(TopicSavemoneyLog entity, Member member) {
		Integer returnRow=null;
		TopicSavemoneyLog topicPrizeLog=topicSavemoneyLogMapper.getByMidAndTgId(member.getId(),entity.getTgId());
		if(StringUtils.isNotEmpty(entity.getActNo())&&topicPrizeLog==null){//没参加过活动，有邀请码，参加该团
			Order order = orderMapper.getOrderByOrderNo(entity.getOrderNo());   //查询订单信息
			List<OrderSku> listOrderSku = orderSkuMapper.getByOrderId(order.getId()); //查询OrderSku信息
			entity.setGoodsSkuId(listOrderSku.get(0).getSkuId());
			entity.setmId(member.getId());
			entity.setAddTime(new Date());
			
			TopicGoods topicGoods  = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
			Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
			
			OrderTeam orderTeam2  = orderTeamMapper.getByTeamNoAndOwner(entity.getMyNo()); //团主
			
			OrderTeam  orderTeam = new OrderTeam();
			orderTeam.setmId(member.getId());
			orderTeam.setCreateTime(new Date());
			orderTeam.setEndTime(topic.getEndTime());  //活动结束时间
			orderTeam.setIsOwner(0);
			orderTeam.setTeamNo(entity.getMyNo());
			orderTeam.setOrderNo(entity.getOrderNo());
			orderTeam.setGoodsSkuId(entity.getGoodsSkuId());
			orderTeam.setStatus(1);
			orderTeam.setCreateTeamTime(orderTeam2.getCreateTeamTime()); //开团时间
			orderTeam.setType(1);
			orderTeamMapper.insertSelective(orderTeam);
			returnRow=topicSavemoneyLogMapper.insertSelective(entity);
			int joinNum=topicSavemoneyLogMapper.logNumByTgId(entity.getId());//查询已经参数的人数
			
			//根据TopicSavemoneyLog 的goods_sku_id 获取goods_sku表信息 ，goods_sku的goods_id 获取goods表信息的拼团数
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(entity.getGoodsSkuId());
			Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());  //团参加限定人数
			if(goods.getTeamNum().equals(joinNum)){//把当前参团的人状态改为拼团成功
				orderTeamMapper.updateByTeamNo(entity.getMyNo());
			}
			
			
		}else if(StringUtils.isEmpty(entity.getActNo())){//没有邀请码，自己开个团
           
			Order order = orderMapper.getOrderByOrderNo(entity.getOrderNo());   //查询订单信息
			List<OrderSku> listOrderSku = orderSkuMapper.getByOrderId(order.getId()); //查询OrderSku信息
			entity.setGoodsSkuId(listOrderSku.get(0).getSkuId());
			
			entity.setActNo(MixCodeUtil.sjs()); //生成4位随机数作为 邀请码
			entity.setMyNo(MixCodeUtil.getCode(12)); //生成12位随机数作为 专属码(团号)
			entity.setmId(member.getId());
			entity.setAddTime(new Date());
			
			TopicGoods topicGoods  = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
			Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
			
			
			OrderTeam  orderTeam = new OrderTeam();
			orderTeam.setmId(member.getId());
			orderTeam.setCreateTime(new Date());
			orderTeam.setEndTime(topic.getEndTime());//活动结束时间
			orderTeam.setIsOwner(1);
			orderTeam.setTeamNo(entity.getMyNo());
			orderTeam.setOrderNo(entity.getOrderNo());
			orderTeam.setGoodsSkuId(entity.getGoodsSkuId());
			orderTeam.setStatus(1);
			orderTeam.setCreateTeamTime(new Date());//开团时间
			orderTeam.setType(1);
			orderTeamMapper.insertSelective(orderTeam);
			
			returnRow=topicSavemoneyLogMapper.insertSelective(entity);
		}
		return returnRow;
	}

	
	//惠省钱log列表
	@Override
	public PageBean<TopicSavemoneyLog> listPage(TopicSavemoneyLog entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicSavemoneyLog> list = topicSavemoneyLogMapper.listPage(entity);
		if(list!=null&&list.size()>0){
			for(TopicSavemoneyLog log : list){
				Member member = memberMapper.selectByPrimaryKey(log.getmId());
				if(member!=null){
					log.setmName(member.getUsername());//用户名称
				}
				TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(log.getTgId());
				if(topicGoods!=null){
					Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
					log.setGoodsName(goods.getName());//主题商品名称
					Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
					log.setTopicName(topic.getName());//主题名称
					List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
					topicGoods.setGoodsImage(goods.getImage());
					topicGoods.setGoodsName(goods.getName());
					if(skuList.size()>0){
						topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);//单件商品价格
					}else{
						topicGoods.setRealPrice((double)goods.getTeamPrice()/100);
					}
					log.setTopicGood(topicGoods);//活动配置
				}
				
			}
		}
		PageBean<TopicSavemoneyLog> pageBean = new PageBean<>(list);
		return pageBean;
	}

}
