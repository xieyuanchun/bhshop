package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.mapper.TopicSeckillLogMapper;
import com.bh.goods.mapper.TopicTypeMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicSeckillLog;
import com.bh.goods.pojo.TopicType;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.TopicSeckillService;
import com.bh.product.api.util.TopicUtils;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class TopicSeckillImpl implements TopicSeckillService{
    @Autowired
    private TopicGoodsMapper topicGoodsMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsCategoryMapper categoryMapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicTypeMapper topicTypeMapper;
    @Autowired
    private TopicSeckillLogMapper topicSeckillLogMapper;
    @Autowired
	private OrderMapper orderMapper;
    @Autowired
	private OrderSkuMapper orderSkuMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	/**
	 * @Description: 秒杀活动立即报名
	 * @author xxj
	 * @date 2018年1月17日 下午3:35:28 
	 */
	public int addTopicSeckill(TopicGoods topicGoods){
		int row=0;
		String goodIds=topicGoods.getGoodsIds();
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodIds)){
			String[] ids=topicGoods.getGoodsIds().split(",");
			for (String goodsId : ids) {
				TopicGoods newTopicGoods=new TopicGoods();
				newTopicGoods.setActId(topicGoods.getActId());//专题id
				newTopicGoods.setcTime(new Date());//提交时间
				newTopicGoods.setuTime(new Date());//修改时间
				newTopicGoods.setListorder(topicGoods.getListorder());//备注
				newTopicGoods.setRemark(topicGoods.getRemark());//排序
				Goods goods = goodsMapper.selectByPrimaryKey(Integer.valueOf(goodsId));
				newTopicGoods.setGoodsId(Integer.valueOf(goodsId));
				newTopicGoods.setSid(goods.getShopId());//商家id
				newTopicGoods.setCid(goods.getCatId());//产品分类id
				row=topicGoodsMapper.insertSelective(newTopicGoods);
			}
		}
		return  row;
	}

	/**
	 * @Description: 秒杀活动报名列表
	 * @author xxj
	 * @date 2018年1月17日 下午4:07:32 
	 */
	public PageBean<TopicGoods> listTopicSeckill(TopicGoods topicGoods) {
		PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicGoods> listTopicSeckill = topicGoodsMapper.listPage(topicGoods);
		if(listTopicSeckill!=null&&listTopicSeckill.size()>0){
			for(TopicGoods t : listTopicSeckill){
				GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
				if(category!=null){
					t.setcName(category.getName());
				}
				Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
				t.setGoodsImage(goods.getImage());
				t.setGoodsName(goods.getName());
				t.setGoodsStatus(goods.getStatus());//商品状态
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(t.getGoodsId());
				if(skuList!=null&&skuList.size()>0){
					t.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
				}
				Topic topic = topicMapper.selectByPrimaryKey(t.getActId());
				t.setTopicName(topic.getName());
				
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				t.setTypeName(type.getName());
				
				t.setStartTime(topic.getStartTime());
				t.setEndTime(topic.getEndTime());
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(listTopicSeckill);
		return pageBean;
	}

	/**
	 * @Description: 移动端今日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	public  Map<String,Object> todayListTopicSeckill(TopicGoods topicGoods,Member member) {
		PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		Map<String,Object> map=new HashMap<String,Object>();
		List<TopicGoods> listTopicSeckill = topicGoodsMapper.todayListTopicSeckill(topicGoods);
		if(listTopicSeckill!=null&&listTopicSeckill.size()>0){
			//查询今日活动商品中已经开始但还没结束的商品（按开始时间将序）
			List<TopicGoods> listDesc = topicGoodsMapper.todayBeginNoEndSeckillDesc(topicGoods);
			if(listDesc!=null&&listDesc.size()>0){
				Topic topic=topicMapper.selectByPrimaryKey(listDesc.get(0).getActId());
				map.put("todayStart",TopicUtils.getDayAndHour(topic.getStartTime()));
			}
			//查询今日活动商品中已经开始但还没结束的商品（按结束时间升序）
			List<TopicGoods> listAsc = topicGoodsMapper.todayBeginNoEndSeckill(topicGoods);
			if(listAsc!=null&&listAsc.size()>0){
				Topic topic=topicMapper.selectByPrimaryKey(listAsc.get(0).getActId());
				map.put("waitEndTime",TopicUtils.getEndTime(topic.getEndTime()));
			}
			this.setValue(listTopicSeckill,member);//处理返回数据
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(listTopicSeckill);
		map.put("topicGoods",pageBean);
		//查询今日活动商品中活动还没开始的商品（每日必抢）
		List<TopicGoods> listNobeginSeckill = topicGoodsMapper.todayNoBeginSeckill(topicGoods);
		ArrayList<String> arr=new ArrayList<String>();
		if(listNobeginSeckill!=null && listNobeginSeckill.size()>0){
			for (TopicGoods seckillTopicGoods : listNobeginSeckill) {
				StringBuffer sb=new StringBuffer();
				double goodsPrice=0;
				String goodsName=null;
				Goods goods = goodsMapper.selectByPrimaryKey(seckillTopicGoods.getGoodsId());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(seckillTopicGoods.getGoodsId());
				if(skuList!=null&&skuList.size()>0){
					goodsName=skuList.get(0).getGoodsName();
					goodsPrice=skuList.get(0).getSellPrice()/100;
				}else{
					goodsName=goods.getName();
					goodsPrice=goods.getSellPrice()/100;
				}
				Topic topic=topicMapper.selectByPrimaryKey(seckillTopicGoods.getActId());
				String startTime=TopicUtils.getDayAndHour(topic.getStartTime());
				Calendar now = Calendar.getInstance();  
				int day=now.get(Calendar.DAY_OF_MONTH);  
				sb.append(goodsName);
				sb.append(goodsPrice+",");
				sb.append(day+"日");
				sb.append(startTime+"抢");
				arr.add(sb.toString());
			}
		}
		map.put("recommend",arr);
		return map;
	}
	
	/**
	 * @Description: 移动端明日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	public Map<String,Object>  tomorrowListTopicSeckill(TopicGoods topicGoods,Member member) {
		PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		
		List<TopicGoods> listTopicSeckill = topicGoodsMapper.tomorrowListTopicSeckill(topicGoods);
		
		Map<String,Object> map=new HashMap<String,Object>();
		if(listTopicSeckill!=null&&listTopicSeckill.size()>0){
			Topic topic=topicMapper.selectByPrimaryKey(listTopicSeckill.get(0).getActId());
			map.put("tomorrowStart",TopicUtils.getDayAndHour(topic.getStartTime()));//明天开始时间
			map.put("waitTime",TopicUtils.getEndTime(topic.getStartTime()));
			this.setValue(listTopicSeckill,member);//处理返回数据
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(listTopicSeckill);
		map.put("topicGoods",pageBean);
		return map;
	}
	
	/**
	 * @Description: 移动端后日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	public Map<String,Object> houdayListTopicSeckill(TopicGoods topicGoods,Member member) {
		PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicGoods> listTopicSeckill = topicGoodsMapper.houdayListTopicSeckill(topicGoods);
		Map<String,Object> map=new HashMap<String,Object>();
		if(listTopicSeckill!=null&&listTopicSeckill.size()>0){
			Topic topic=topicMapper.selectByPrimaryKey(listTopicSeckill.get(0).getActId());
			map.put("houdayStart",TopicUtils.getDayAndHour(topic.getStartTime()));//后天开始时间
			map.put("waitTime",TopicUtils.getEndTime(topic.getStartTime()));
			this.setValue(listTopicSeckill,member);//处理返回数据
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(listTopicSeckill);
		map.put("topicGoods",pageBean);
		return map;

	}

	/**
	 * @Description: 处理秒杀数据
	 * @author xieyc
	 * @date 2018年1月23日 上午9:51:46 
	 */
	private void setValue(List<TopicGoods> listTopicSeckill,Member member){
		Boolean isJoin=null;
		for(TopicGoods topicGoods : listTopicSeckill){
			if(member!=null){
				//查询某个用户的某个活动的记录
				TopicSeckillLog memberSeckillLog=topicSeckillLogMapper.getByMidAndTgId(member.getId(),topicGoods.getId());
				if(memberSeckillLog!=null){
					isJoin=true;//参加过
				}else{
					isJoin=false;//没参加过
				}
				topicGoods.setIsJoin(isJoin);//是否参加过该活动
			}
			
			Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
			topicGoods.setTopicName(topic.getName());//专题名字
			
			TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
			topicGoods.setTypeName(type.getName());//专题类型名字
			
			topicGoods.setTopicStartTime(TopicUtils.getDayAndHour(topic.getStartTime()));//活动几点开始
					
			topicGoods.setStartTime(topic.getStartTime());//开始时间
			
			topicGoods.setEndTime(topic.getEndTime());//结束时间
			
			topicGoods.setWaitTime(TopicUtils.getEndTime(topic.getEndTime()));//多久后活动结束
		
			
			Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
			topicGoods.setGoodsImage(goods.getImage());
			topicGoods.setGoodsName(goods.getName());
			//根据goods_id查询sku，并按库存降序
			List<GoodsSku> skuList = goodsSkuMapper.getListByGoodsIdAndOrderByStore(topicGoods.getGoodsId());
			int saleNums=0;//卖出数量
			//查询某个活动的全部记录
			List<TopicSeckillLog> listALLog=topicSeckillLogMapper.getSeckillLog(topicGoods.getId());
			if(skuList!=null&&skuList.size()>0){//商品展示页显示sku中库存最多
				if(listALLog!=null && listALLog.size()>0){
					for (TopicSeckillLog topicSeckillLog : listALLog) {
						String orderNo=topicSeckillLog.getOrderNo();
						Order order=orderMapper.getOrderByOrderNo(orderNo);
						if(order!=null){
							int skuId=skuList.get(0).getId();//库存最多的skuId
							//查询目前库存最多的销售数量
							List<OrderSku> orderSkuList =orderSkuMapper.getOrderSkuByOrderIdAndSkuId(order.getId(),skuId);
							saleNums+=orderSkuList.get(0).getSkuNum();
						}
					}
				}
				topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);//设置商品价格
				topicGoods.setMarketPrice((double)skuList.get(0).getMarketPrice()/100);//市场价格
				topicGoods.setStoreNums(skuList.get(0).getStoreNums());//库存
				topicGoods.setSaleNums(saleNums);//卖出数量
				topicGoods.setSurplusStoreNums(skuList.get(0).getStoreNums()-saleNums);//剩余库存
			}
		}	
	}	

}
