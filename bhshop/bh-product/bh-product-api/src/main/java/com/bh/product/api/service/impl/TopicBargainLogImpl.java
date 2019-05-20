package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicBargainConfigMapper;
import com.bh.goods.mapper.TopicBargainLogMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicBargainConfig;
import com.bh.goods.pojo.TopicBargainLog;
import com.bh.goods.pojo.TopicGoods;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.TopicBargainLogService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.pojo.Member;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
@Service
public class TopicBargainLogImpl implements TopicBargainLogService{
	@Autowired
	private TopicBargainLogMapper mapper;
	@Autowired
	private TopicBargainConfigMapper configMapper;
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
	private OrderSkuMapper orderSkuMapper;
	
	
	
	//砍价日志的插入
	@Transactional
	@Override
	public int add(TopicBargainLog entity, Member member) {
		int row = 0;
		Integer bargainPrice = 0;
		Integer balance = 0;
		List<TopicBargainLog> list = null;
		TopicBargainConfig config = configMapper.selectByTgid(entity.getTgId());
		TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
		Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
		int count = mapper.countBytgId(entity.getTgId(), entity.getBargainNo());
		
		
		if(StringUtils.isEmptyOrWhitespaceOnly(entity.getBargainNo())){//判断是否发起人
			List<TopicBargainLog> t = mapper.selectByTgIdAndMidAndOwner(entity.getTgId(), member.getId(), 1);
			if(t.size()>0){
				return 888;
			}
			entity.setIsOwner(1);
			entity.setBargainNo(MixCodeUtil.getOrderNo());
			
			if(count+1==config.getNum()){ //判断是否最后一人砍价
				bargainPrice = goods.getTeamPrice();
				balance = 0;
			}else{
				entity.setStatus(0); //砍价中
				bargainPrice = (int)getBarginPrice(goods.getTeamPrice());//砍价金额
				balance = goods.getTeamPrice()-bargainPrice;//余额
			}
		}else{
			entity.setBargainNo(entity.getBargainNo());
			entity.setIsOwner(0);
			
			if(count==config.getNum()){//判断活动是否结束
				return 999;
			}
			TopicBargainLog log = mapper.selectByBargainNoAndMid(entity.getBargainNo(), member.getId());
			if(log!=null){//判断重复砍价
				return 888;
			}
			
			list = mapper.selectByBargainNo(entity.getBargainNo());
			if(count+1==config.getNum()){ //判断是否最后一人砍价
				bargainPrice = list.get(0).getActBalance();
				balance = 0;
			}else{
				entity.setStatus(0); //砍价中
				bargainPrice = (int)getBarginPrice(list.get(0).getActBalance());//砍价金额
				balance = list.get(0).getActBalance()-bargainPrice;//余额
			}
		}
		
		entity.setBargainPrice(bargainPrice);
		entity.setActBalance(balance);
		entity.setmId(member.getId());
		entity.setAddTime(new Date());
		row = mapper.insertSelective(entity);
		
		if(count+1==config.getNum()){  //砍价成功更改状态
			list = mapper.selectByBargainNo(entity.getBargainNo());
			TopicBargainLog log = mapper.selectByBargainNoAndOwner(entity.getBargainNo());
			for(TopicBargainLog b : list){
				b.setStatus(1);
				row = mapper.updateByPrimaryKeySelective(b);
			}
			row = insertOrder(log.getmId(), entity.getAddressId(), entity.getSkuId());
		}
		return row;
	}
	
	//砍价订单的生成
	private int insertOrder(int mId, int addressId, int skuId){
		int row = 0;
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuId);
		if(goodsSku!=null){
			Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
			Order order = new Order();
			order.setmId(mId);
			order.setOrderNo(MixCodeUtil.getOrderNo());
			order.setPaymentId(3); //默认微信支付
			order.setPaymentStatus(2);
			order.setDeliveryId(2);
			order.setDeliveryStatus(0);
			if(goods!=null){
				order.setShopId(goods.getShopId());
			}
			order.setStatus(2);
			order.setAddtime(new Date());
			order.setmAddrId(addressId);
			row = orderMapper.insertSelective(order); //平台订单的插入
			
			OrderShop orderShop = new OrderShop();
			orderShop.setmId(mId);
			orderShop.setShopId(goods.getShopId());
			orderShop.setOrderId(order.getId());
			orderShop.setOrderNo(order.getOrderNo());
			orderShop.setShopOrderNo(MixCodeUtil.getOrderNo());
			orderShop.setStatus(2);
			row = orderShopMapper.insertSelective(orderShop); //商家订单的插入
			
			OrderSku orderSku = new OrderSku();
			orderSku.setOrderId(order.getId());
			orderSku.setOrderShopId(orderShop.getId());
			orderSku.setGoodsId(goods.getId());
			if(goodsSku.getGoodsName()!=null){
				orderSku.setGoodsName(goodsSku.getGoodsName());
			}else{
				orderSku.setGoodsName(goods.getName());
			}
			orderSku.setSkuId(goodsSku.getId());
			orderSku.setSkuNo(goodsSku.getSkuNo());
			org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue()); //商品基本信息
			org.json.JSONArray personList = jsonObj.getJSONArray("url");
			String url = (String) personList.get(0);
			orderSku.setSkuImage(url);
			orderSku.setSkuNum(1);
			orderSku.setSkuMarketPrice(goodsSku.getMarketPrice());
			orderSku.setSkuSellPriceReal(goodsSku.getSellPrice());
			orderSku.setSkuValue(goodsSku.getValue());
			orderSku.setShopId(goods.getShopId());
			
			row = orderSkuMapper.insertSelective(orderSku);
			
		}
		return row;
	}

	@Override
	public TopicBargainLog get(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}
	
	//砍价日志管理列表
	@Override
	public PageBean<TopicBargainLog> listPage(TopicBargainLog entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicBargainLog> list = mapper.listPage(entity);
		if(list.size()>0){
			for(TopicBargainLog log : list){
				if(log.getmId()!=null){
					Member member = memberMapper.selectByPrimaryKey(log.getmId());
					if(member!=null){
						log.setmName(member.getUsername());//用户名称
					}
				}
				TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(log.getTgId());
				if(topicGoods!=null){
					Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
					log.setGoodsName(goods.getName());//商品名称
					Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
					log.setTopicName(topic.getName());
				}
				double realactBalance = (double)log.getActBalance()/100;
				log.setRealactBalance(realactBalance);
				
				double realBargainPrice = (double)log.getBargainPrice()/100;
				log.setRealBargainPrice(realBargainPrice);
			}
		}
		PageBean<TopicBargainLog> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	//获取砍价金额
	private double getBarginPrice(int price){
		Random random = new Random();
		double rate = (double)(random.nextInt(25) + 26)/100; //随机砍价比例25%~50%
	    return Double.parseDouble(String.format("%.2f", price*rate));
	}
	
	//扫描砍价活动有效期
	@Override
	public int checkTimeChangeStatus(){
		int row = 0;
		List<Topic> topicList = topicMapper.getBargainTopic();
		if(topicList.size()>0){
			for(Topic t : topicList){
				List<TopicGoods> topicGoodsList = topicGoodsMapper.getByActId(t.getId());
				if(topicGoodsList.size()>0){
					for(TopicGoods tGoods : topicGoodsList){
						long a = new Date().getTime();
						long b = t.getEndTime().getTime();
						if(a > b){ //活动已结束
							List<TopicBargainLog> list = mapper.selectByTgidAndStatus(tGoods.getId());
							if(list.size()>0){
								for(TopicBargainLog entity : list){
									entity.setStatus(2); //失败
									row = mapper.updateByPrimaryKeySelective(entity);
								}
							}
						}
					}
				}
			}
		}
		return row;
	}
	
	/**
	 * wx砍价操作
	 */
	@Override
	public int wxAdd(TopicBargainLog entity, Member member) {
		int row = 0;
		Integer bargainPrice = 0;
		Integer balance = 0;
		List<TopicBargainLog> list = null;
		//TopicBargainConfig config = configMapper.selectByTgid(entity.getTgId());
		TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
		Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
		List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(goods.getId());
		int count = mapper.countBytgId(entity.getTgId(), entity.getBargainNo());
		
		if(StringUtils.isEmptyOrWhitespaceOnly(entity.getBargainNo())){//判断是否发起人
			List<TopicBargainLog> t = mapper.selectByTgIdAndMidAndOwner(entity.getTgId(), member.getId(), 1);
			if(t.size()>0){
				return 888;
			}
			entity.setIsOwner(1);
			entity.setBargainNo(MixCodeUtil.getOrderNo());
			
			if(count+1==goods.getTeamNum()){ //判断是否最后一人砍价
				if(skuList.size()>0){
					bargainPrice = skuList.get(0).getTeamPrice();
				}else{
					bargainPrice = goods.getTeamPrice();
				}
				balance = 0;
			}else{
				entity.setStatus(0); //砍价中
				if(skuList.size()>0){
					bargainPrice = (int)getBarginPrice(skuList.get(0).getTeamPrice());//砍价金额
					balance = skuList.get(0).getTeamPrice()-bargainPrice;//余额
				}else{
					bargainPrice = (int)getBarginPrice(goods.getTeamPrice());//砍价金额
					balance = goods.getTeamPrice()-bargainPrice;//余额
				}
			}
		}else{
			entity.setBargainNo(entity.getBargainNo());
			entity.setIsOwner(0);
			TopicBargainLog log = mapper.selectByBargainNoAndOpenId(entity.getBargainNo(), entity.getOpenId());
			if(log!=null){//判断重复砍价
				return 888;
			}
			list = mapper.selectByBargainNo(entity.getBargainNo());
			if(count+1==goods.getTeamNum()){ //判断是否最后一人砍价
				bargainPrice = list.get(0).getActBalance();
				balance = 0;
			}else{
				entity.setStatus(0); //砍价中
				bargainPrice = (int)getBarginPrice(list.get(0).getActBalance());//砍价金额
				balance = list.get(0).getActBalance()-bargainPrice;//余额
			}
		}
		
		entity.setBargainPrice(bargainPrice);
		entity.setActBalance(balance);
		if(member!=null){
			entity.setmId(member.getId());
		}
		entity.setAddTime(new Date());
		row = mapper.insertSelective(entity);
		
		if(count+1==goods.getTeamNum()){  //砍价成功更改状态
			list = mapper.selectByBargainNo(entity.getBargainNo());
			TopicBargainLog log = mapper.selectByBargainNoAndOwner(entity.getBargainNo());
			for(TopicBargainLog b : list){
				b.setStatus(1);
				row = mapper.updateByPrimaryKeySelective(b);
			}
			row = insertOrder(log.getmId(), entity.getAddressId(), entity.getSkuId());
		}
		return row;
	}
}
