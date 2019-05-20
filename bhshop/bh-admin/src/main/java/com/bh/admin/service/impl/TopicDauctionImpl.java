package com.bh.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicDauctionMapper;
import com.bh.admin.mapper.goods.TopicDauctionPriceMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicDauction;
import com.bh.admin.pojo.goods.TopicDauctionPrice;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.service.TopicDauctionService;
import com.bh.utils.MoneyUtil;
@Service
public class TopicDauctionImpl implements TopicDauctionService{
	@Autowired
	private TopicDauctionMapper mapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private TopicDauctionPriceMapper dauctionPriceMapper;

	@Override
	public int insert(TopicDauction entity) throws Exception {
		int row = 0;
		String str = getTopicStr(entity.getActId());
		if(str!=null){
	    	String[] arr = str.split(",");
	    	for(int i=0; i<arr.length; i++){
	    		if(entity.getGoodsId()==Integer.parseInt(arr[i])){
	    			return 999;
	    		}
	    	}
	    }
		List<Topic> topicList = topicMapper.getByGoodsIdAndStatus(entity.getGoodsId());
		if(topicList.size()>0){
			return 888;
		}
		/*List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(entity.getGoodsId());
		if(skuList.get(0).getAuctionPrice()==0){
			return 777;
		}*/
		TopicGoods topicGoods = new TopicGoods();
		topicGoods.setActId(entity.getActId());
		topicGoods.setGoodsId(entity.getGoodsId());
		topicGoods.setKuNums(entity.getKuNums());
		
		Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
		if(goods!=null){
			topicGoods.setCid(goods.getCatId());
		}
		topicGoods.setcTime(new Date());
		topicGoods.setuTime(new Date());
		topicGoods.setSid(1);
		topicGoodsMapper.insertSelective(topicGoods);//插入商品明细
		
		System.out.println(goods.getId());
		goods.setTopicGoodsId(topicGoods.getId());
		goodsMapper.updateByPrimaryKeySelective(goods);
		
		entity.setTgId(topicGoods.getId());
		entity.setScopePrice(MoneyUtil.yuan2Fen(entity.getRealScopePrice().toString()));
		entity.setLowPrice(MoneyUtil.yuan2Fen(entity.getRealLowPrice().toString()));
		entity.setDauctionPrice(MoneyUtil.yuan2Fen(entity.getRealDauctionPrice().toString()));
		row = mapper.insertSelective(entity);
		
		Topic topic = topicMapper.selectByPrimaryKey(entity.getActId());
		int dauctionPrice = (int)(entity.getRealDauctionPrice()*100); //起拍价
		int lowPrice = (int)(entity.getRealLowPrice()*100); //最低价
		int downPrice= (int)(entity.getRealScopePrice()*100); //降价幅度
		Long mills = topic.getEndTime().getTime()-topic.getStartTime().getTime(); 
		int b = (int)(mills/60/1000/entity.getTimeSection());
		int a = (dauctionPrice - lowPrice)%downPrice==0 ? b : b+1;
		Date startTime = topic.getStartTime();
		Date date = new Date();
		boolean flag = false;
		for(int i=0 ;i < a+1; i++){
			if(dauctionPrice>lowPrice){
				if(date.getTime() < topic.getEndTime().getTime()){
					TopicDauctionPrice price = new TopicDauctionPrice();
					if(i==0){
						price.setCurTime(startTime);
					}else{
						price.setCurTime(date);
					}
					price.setPrice(dauctionPrice);
					price.setGoodsId(entity.getGoodsId());
					price.setTgId(topicGoods.getId());
					price.setDauctionId(entity.getId());
					row = dauctionPriceMapper.insertSelective(price);
					
					
					TopicDauctionPrice dP = dauctionPriceMapper.selectByPrimaryKey(price.getId());
					Long timeMillis = dP.getCurTime().getTime()+entity.getTimeSection()*60*1000;
					date = new Date(timeMillis);
					
					dauctionPrice = dauctionPrice - (int)(entity.getRealScopePrice()*100);
				}else{
					TopicDauctionPrice price = new TopicDauctionPrice();
					price.setCurTime(topic.getEndTime());
					price.setPrice(dauctionPrice);
					price.setGoodsId(entity.getGoodsId());
					price.setTgId(topicGoods.getId());
					price.setDauctionId(entity.getId());
					row = dauctionPriceMapper.insertSelective(price);
					flag = true;
				}
			}else{
				TopicDauctionPrice priceLast = new TopicDauctionPrice();
				if(date.getTime() < topic.getEndTime().getTime()){
					priceLast.setCurTime(date);
				}else{
					priceLast.setCurTime(topic.getEndTime());
				}
				priceLast.setGoodsId(entity.getGoodsId());
				priceLast.setTgId(topicGoods.getId());
				priceLast.setDauctionId(entity.getId());
				priceLast.setPrice((int)(entity.getRealLowPrice()*100));
				row = dauctionPriceMapper.insertSelective(priceLast);
				flag = true;
			}
			if(flag==true){
				return row;
			}
		}
		return row;
	}
	
	private String getTopicStr(int actId){
		String str = null;
		StringBuffer buffer = new StringBuffer();
		List<TopicGoods> list = topicGoodsMapper.getByActIdAndNotDelete(actId);
		if(list.size()>0){
			for(TopicGoods entity : list){
				buffer.append(entity.getGoodsId()+",");
			}
			str = buffer.toString().substring(0, buffer.toString().length()-1);
		}
		return str;
	}

	@Override
	public int edit(TopicDauction entity) throws Exception {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(TopicDauction entity) throws Exception {
		return mapper.deleteByPrimaryKey(entity.getId());
	}

	@Override
	public TopicDauction get(TopicDauction entity) throws Exception {
		return mapper.selectByPrimaryKey(entity.getId());
	}
	
	/**
	 * 定时检查变更商品价格
	 */
	@Override
	public int changeGoodsPrice() throws Exception {
		int row = 0;
		List<Topic> topicList = topicMapper.getTopicDauction();
		if(topicList.size()>0){
			for(int i=0; i<topicList.size(); i++){
				List<TopicGoods> list = topicGoodsMapper.getByActIdAndNotDelete(topicList.get(i).getId());
				if(list.size()>0){
					for(TopicGoods t : list){
						List<TopicDauction> dauction = mapper.selectByTgId(t.getId());//获取配置
						Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
						List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(goods.getId());
						List<TopicDauctionPrice> dacutionPriceList = dauctionPriceMapper.selectByGoodsId(t.getGoodsId());
						Date date = new Date();
						Integer thisPrice = dauction.get(0).getLowPrice() + dauction.get(0).getScopePrice();//最低价上限
						if(dacutionPriceList.size()>0){//判断是否第一次变价
							if(date.getTime()>topicList.get(i).getStartTime().getTime() && dacutionPriceList.get(0).getPrice()>dauction.get(0).getLowPrice()){
								
								long timeDiffer = date.getTime() - dacutionPriceList.get(0).getCurTime().getTime();//上次降价距当前时差
								if(timeDiffer>dauction.get(0).getTimeSection()*60*1000){
									GoodsSku goodsSku = skuList.get(0);
									Integer price = goodsSku.getAuctionPrice()-dauction.get(0).getScopePrice();//降价幅度
									if(thisPrice>dacutionPriceList.get(0).getPrice()){ //判断是否超出最低价
										goodsSku.setAuctionPrice(dauction.get(0).getLowPrice() );
									}else{
										goodsSku.setAuctionPrice(price);
									}
									row = goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
									
									TopicDauctionPrice dauctionPrice = new TopicDauctionPrice();
									dauctionPrice.setGoodsId(t.getGoodsId());
									dauctionPrice.setCurTime(new Date());
									dauctionPrice.setDauctionId(dauction.get(0).getId());
									dauctionPrice.setTgId(t.getId());
									Integer changePrice = dacutionPriceList.get(0).getPrice()-dauction.get(0).getScopePrice();
									if(thisPrice>dacutionPriceList.get(0).getPrice()){ //判断是否超出最低价
										dauctionPrice.setPrice(dauction.get(0).getLowPrice());
									}else{
										dauctionPrice.setPrice(changePrice);
									}
									row = dauctionPriceMapper.insertSelective(dauctionPrice);
								}
							}
						}else{
							if(date.getTime()>topicList.get(i).getStartTime().getTime()){
								TopicDauctionPrice dauctionPrice = new TopicDauctionPrice();
								dauctionPrice.setGoodsId(t.getGoodsId());
								dauctionPrice.setCurTime(new Date());
								dauctionPrice.setPrice(skuList.get(0).getAuctionPrice());
								dauctionPrice.setDauctionId(dauction.get(0).getId());
								row = dauctionPriceMapper.insertSelective(dauctionPrice);
							}
						}
					}
				}
			}
		}
		return row;
	}
	
	/**
	 * 移动端拍卖获取时间点和当前金额
	 */
	@Override
	public List<TopicDauctionPrice> getPointAndPrice(int goodsId) throws Exception {
		List<TopicDauctionPrice> list = dauctionPriceMapper.selectByGoodsId(goodsId);
		List<TopicDauctionPrice> map = new ArrayList<>();
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		if(list.size()==1){
			TopicDauctionPrice d = new TopicDauctionPrice();
			TopicDauction dauction = mapper.selectByPrimaryKey(list.get(0).getDauctionId());
			
			if(list.get(0).getPrice()>dauction.getLowPrice()){//判断是否最后一次变价
				Integer thisPrice = dauction.getLowPrice() + dauction.getScopePrice();//最低价上限
				Long timeMillis = list.get(0).getCurTime().getTime()+dauction.getTimeSection()*60*1000;
				Date date = new Date(timeMillis);
				String pointTimeNext = sdf2.format(date);
				d.setPointTime(pointTimeNext); //下个时间点
				
				if(thisPrice>list.get(0).getPrice()){
					d.setRealPrice((double)dauction.getLowPrice()/100);
				}else{
					Integer nextPrice = list.get(0).getPrice()-dauction.getScopePrice();
					Double nextRealPrice =  (double)nextPrice/100;
					d.setRealPrice(nextRealPrice); //下个价格
				}
				d.setCode(0);
				map.add(d);
			}
			
			String pointTime = sdf2.format(list.get(0).getCurTime());
			Double realPrice = (double)list.get(0).getPrice()/100;
			list.get(0).setPointTime(pointTime);
			list.get(0).setRealPrice(realPrice);
			list.get(0).setCode(1);
			map.add(list.get(0));
		}else if(list.size()>1){
			TopicDauctionPrice d = new TopicDauctionPrice();
			TopicDauction dauction = mapper.selectByPrimaryKey(list.get(0).getDauctionId());
			
			if(list.get(0).getPrice()>dauction.getLowPrice()){//判断是否最后一次变价
				Long timeMillis = list.get(0).getCurTime().getTime()+dauction.getTimeSection()*60*1000;
				Date date = new Date(timeMillis);
				//String strTime = df.format(date);
				String pointTimeNext = sdf2.format(date);
				d.setPointTime(pointTimeNext); //下个时间点
				
				Integer thisPrice = dauction.getLowPrice() + dauction.getScopePrice();//最低价上限
				if(thisPrice>list.get(0).getPrice()){
					d.setRealPrice((double)dauction.getLowPrice()/100);
				}else{
					Integer nextPrice = list.get(0).getPrice()-dauction.getScopePrice();
					Double nextRealPrice =  (double)nextPrice/100;
					d.setRealPrice(nextRealPrice); //下个价格
				}
				d.setCode(0);
				map.add(d);
			}
			
			String pointTime = sdf2.format(list.get(0).getCurTime());//当前时间点和价格
			Double realPrice = (double)list.get(0).getPrice()/100;
			list.get(0).setPointTime(pointTime);
			list.get(0).setRealPrice(realPrice);
			list.get(0).setCode(1);
			map.add(list.get(0));
			
			String pointTimeUp = sdf2.format(list.get(1).getCurTime());
			Double realPriceUp = (double)list.get(1).getPrice()/100;
			list.get(1).setPointTime(pointTimeUp);
			list.get(1).setRealPrice(realPriceUp);
			list.get(1).setCode(0);
			map.add(list.get(1));
			
			if(list.get(0).getPrice()<=dauction.getLowPrice()){
				String pointTimeUpUp = sdf2.format(list.get(2).getCurTime());
				Double realPriceUpUp = (double)list.get(2).getPrice()/100;
				list.get(2).setPointTime(pointTimeUpUp);
				list.get(2).setRealPrice(realPriceUpUp);
				list.get(2).setCode(0);
				map.add(list.get(2));
			}
		}
		return map;
	}
	
	/**
	 * 拍卖配置详情
	 */
	@Override
	public Map<String, Object> dauctionDetail(int goodsId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		List<TopicDauctionPrice> list = dauctionPriceMapper.selectByTgId(goods.getTopicGoodsId());
		if(list.size()>0){
			TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(list.get(0).getTgId());
			if(topicGoods!=null){
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
				map.put("startTime", topic.getStartTime());
				map.put("endTime", topic.getEndTime());
				
				List<TopicDauction> dauction = mapper.selectByTgId(topicGoods.getId());
				if(dauction.size()>0){
					map.put("dauctionPrice", (double)dauction.get(0).getDauctionPrice()/100);
					map.put("lowPrice", (double)dauction.get(0).getLowPrice()/100);
					map.put("scopePrice", (double)dauction.get(0).getScopePrice()/100);
					map.put("timeSection", dauction.get(0).getTimeSection());
				}else{
					map.put("dauctionPrice", null);
					map.put("lowPrice", null);
					map.put("scopePrice", null);
					map.put("timeSection", null);
				}
			}else{
				map.put("startTime", null);
				map.put("endTime", null);
			}
		}
		return map;
	}

}
