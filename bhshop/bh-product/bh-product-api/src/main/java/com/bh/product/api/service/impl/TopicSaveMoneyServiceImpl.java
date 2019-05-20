package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.mapper.TopicSavemoneyConfigMapper;
import com.bh.goods.mapper.TopicSavemoneyLogMapper;
import com.bh.goods.mapper.TopicTypeMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicSavemoneyConfig;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.goods.pojo.TopicType;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.TopicSaveMoneyService;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class TopicSaveMoneyServiceImpl implements TopicSaveMoneyService{

	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Autowired
	private TopicSavemoneyConfigMapper topicSavemoneyConfigMapper;
	
	@Autowired
	private TopicSavemoneyLogMapper topicSavemoneyLogMapper;
	
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	
    @Autowired
	private GoodsSkuMapper goodsSkuMapper;
	    
    @Autowired
	private TopicMapper topicMapper;
	   
    @Autowired
	private TopicTypeMapper topicTypeMapper;
	
    @Autowired
   	private OrderShopMapper orderShopMapper;
       
	@Autowired
	private OrderSkuMapper orderSkuMapper;
    
	@Value(value = "${pageSize}")
	private String pageSize;

	
	
	@Override
	public int addTopicSaveMoney(TopicGoods topicGoods) {
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
	 * @Description: 惠省钱活动报名列表
	 * @author 
	 * @date 
	 */
	@Override
	public PageBean<TopicGoods> listTopicSaveMoney(TopicGoods topicGoods) {
		   PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		   List<TopicGoods> listTopicSaveMoney = topicGoodsMapper.listPage(topicGoods);
		   if(listTopicSaveMoney!=null&&listTopicSaveMoney.size()>0){
				for(TopicGoods t : listTopicSaveMoney){
					GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
					if(category!=null){
						topicGoods.setcName(category.getName());
					}
					List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(t.getGoodsId());
					Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
					t.setGoodsStatus(goods.getStatus());//商品状态
					t.setGoodsName(goods.getName());//商品名字
					t.setGoodsImage(goods.getImage());//图片地址
					if(skuList.size()>0){
						t.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
					}else{
						t.setRealPrice((double)goods.getTeamPrice()/100);
					}
													
					Topic topic = topicMapper.selectByPrimaryKey(t.getActId());
					t.setTopicName(topic.getName());//专题名字
					
					TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
					t.setTypeName(type.getName());//专题类型名字
					
					t.setStartTime(topic.getStartTime());//开始时间
					t.setEndTime(topic.getEndTime());//结束时间
				}
			}
			PageBean<TopicGoods> pageBean = new PageBean<>(listTopicSaveMoney);
			return pageBean;
	}


	/**
	 * @Description: 移动端惠省钱活动商品列表
	 * @author zlk
	 * @date 
	 */
	@Override
	public Map<String,Object> apiTopicGoodsList(TopicGoods entity,Member member) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicGoods> list = topicGoodsMapper.getByType(entity.getType()); //根据类型获取主题商品
		System.out.println("==========="+list.size());
		Boolean isJoin=null;//是否参加过该活动
		Map<String,Object> map=new HashMap<String,Object>();
		if(list!=null && list.size()>0){
			for(TopicGoods topicGoods : list){
			
			 if(member!=null){
				TopicSavemoneyLog topicSavemoneyLog = topicSavemoneyLogMapper.getByMidAndTgId(member.getId(),topicGoods.getId());
				if(topicSavemoneyLog!=null){
					isJoin=true;//参加过
				}else{
					isJoin=false;//没参加过
				}
                topicGoods.setIsJoin(isJoin);//是否参加过该活动
		      }
		       // int joinNum=topicSavemoneyLogMapper.logNumByTgId(topicGoods.getId());//查询已经参数的人数
		        //topicGoods.setJoinNum(joinNum);//参加人数
                //根据TopicSavemoneyLog 的goods_sku_id 获取goods_sku表信息 ，goods_sku的goods_id 获取goods表信息的拼团数
     		    //GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(entity.getGoodsSkuId());
     		    //Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());  //团参加限定人数
                //goods.getTeamNum()//团参加限定人数
			  
		
		    	TopicSavemoneyLog topicSavemoneyLog2 = topicSavemoneyLogMapper.getByTgId(topicGoods.getId());
		    	
		        topicGoods.setMyNo(topicSavemoneyLog2.getMyNo());//惠省钱团号
		    	
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
				topicGoods.setTopicName(topic.getName());//专题名字
				
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				topicGoods.setTypeName(type.getName());//专题类型名字
				
				topicGoods.setStartTime(topic.getStartTime());//开始时间
				
				topicGoods.setEndTime(topic.getEndTime());//结束时间
				
				topicGoods.setWaitTime(this.getEndTime(topic.getEndTime()));//多久后活动结束
							
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				
				int finishNum = 0; 
				String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
				if(countT!=null){
					finishNum = Integer.parseInt(countT);
				}
				topicGoods.setJoinNum(finishNum);//已拼多少件
				
				topicGoods.setGoodsImage(goods.getImage());
				topicGoods.setGoodsName(goods.getName());
				
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				System.out.println("========="+skuList.get(0).getGoodsName()+skuList.get(0).getId());
				if(skuList.size()>0){
					topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);//设置商品价格
				}else{
					topicGoods.setRealPrice((double)goods.getTeamPrice()/100);
				}
			 
			}
			
			Topic topicThumbs = topicMapper.selectByPrimaryKey(list.get(0).getActId());
			map.put("banner",topicThumbs.getThumbs());//活动banner图
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
	
		map.put("topicGoods",pageBean);
		
		return map;
	}



	/**
	 * @Description: 我的惠省钱活动列表
	 * @author 
	 * @date 
	 */
	@Override
	public PageBean<Map<String, Object>> apiMyTopicSaveMoney(TopicGoods entity,Integer mId) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<TopicSavemoneyLog> list = topicSavemoneyLogMapper.getByMid(mId);
		System.out.println("========================="+list.size());
		if(list!=null&&list.size()>0){
			for(TopicSavemoneyLog log : list){
				
				TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(log.getTgId());
				System.out.println("====="+topicGoods.getId());
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				Map<String, Object> map = new LinkedHashMap<>();
				
				if(skuList.size()>0){
					JSONObject jsonObj = new JSONObject(skuList.get(0).getValue()); 
					JSONArray personList = jsonObj.getJSONArray("url");
					String url = (String) personList.get(0);
					map.put("goodsImage", url);
					if(skuList.get(0).getGoodsName()!=null){
						map.put("goodsName", skuList.get(0).getGoodsName());
						map.put("price",skuList.get(0).getTeamPrice()/100);
					}else{
						map.put("goodsName", goods.getName());
						map.put("price",goods.getTeamPrice()/100);
					}
				}else{
					map.put("goodsImage", goods.getImage());
					map.put("goodsName", goods.getName());
					map.put("price",goods.getTeamPrice()/100);
				}
				
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
				String waitTime = getEndTime(topic.getEndTime());
				System.out.println("waitTime::::"+waitTime);
				map.put("waitTime", waitTime);//还差多久结束
			    
				mapList.add(map);
			}
		}
		PageParams<Map<String, Object>> params = new PageParams<>();
		PageBean<Map<String, Object>> pageBean = new PageBean<>();
	    params.setCurPage(Integer.parseInt(entity.getCurrentPage()));
	    params.setPageSize(Contants.PAGE_SIZE);
	    params.setResult(mapList);
	    PageBean<Map<String, Object>> retPage = pageBean.getPageResult(params);
		return retPage;
	}

	
	/**
	 * @Description: 
	 * @author 
	 * @date 
	 */
	private String getEndTime(Date endTime){
		String waitTime = null;
		long a = new Date().getTime();
		long b = endTime.getTime();
		if(b>a){
			int c = (int)((b-a) / 1000);
			System.out.println(c);
			String hours = c / 3600+"";
			if(Integer.parseInt(hours)<10){
				hours = "0"+ hours;
			}
			String min = c % 3600 /60+"";
			if(Integer.parseInt(min)<10){
				min = "0"+ min;
			}
			String sencond = c % 3600 % 60+"";
			if(Integer.parseInt(sencond)<10){
				sencond = "0"+ sencond;
			}
		   waitTime = hours+":"+min+":"+sencond;
		}
		return waitTime;
	}


    //团活动的结果
	@Override
	public Integer doSaveMoney(TopicSavemoneyLog entity) {
		   Integer row = null;
		   List<TopicSavemoneyLog> listTopicSavemoneyLog = topicSavemoneyLogMapper.listPage(entity);//只需传入tgId，其他为字段空,当前参团人数
		 
		 //根据TopicSavemoneyLog 的goods_sku_id 获取goods_sku表信息 ，goods_sku的goods_id 获取goods表信息的拼团数
		   GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(entity.getGoodsSkuId());
		   Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());  //团参加限定人数
		   if(listTopicSavemoneyLog!=null&&listTopicSavemoneyLog.size()>0){
			   if(goods.getTeamNum()>listTopicSavemoneyLog.size()){//参加人数不足，关闭活动
				   row = 0;
				   //金额原路返回
				   boolean bool = false;
				   List<Object> boolList = new ArrayList<Object>();
				   System.out.println("金额原路返回");
				   for(TopicSavemoneyLog topicSavemoneyLog : listTopicSavemoneyLog){
					   PayServiceImpl payServiceImpl = new PayServiceImpl();
					   bool = payServiceImpl.refund(topicSavemoneyLog.getOrderNo());
					   boolList.add(bool);
				   }
				   for(TopicSavemoneyLog topicSavemoneyLog : listTopicSavemoneyLog){//把参加的客户订单的状态改为拼单失败-1
					   OrderShop orderShop = new OrderShop();
					   orderShop.setOrderNo(topicSavemoneyLog.getOrderNo());
					   orderShop.setStatus(-1); //把状态改为待发货，证明用户购买成功
					   orderShopMapper.updateStatusByOrderNo(orderShop);
					   
				   }
				   System.out.println("退款的状态:"+boolList.toString());
			   }else if(goods.getTeamNum().equals(listTopicSavemoneyLog.size())){//开始活动
				   row = 1;
				   for(TopicSavemoneyLog topicSavemoneyLog : listTopicSavemoneyLog){//把参加的客户订单的状态改为拼团成功2
					   OrderShop orderShop = new OrderShop();
					   orderShop.setOrderNo(topicSavemoneyLog.getOrderNo());
					   orderShop.setStatus(2); //把状态改为待发货，证明用户购买成功
					   orderShopMapper.updateStatusByOrderNo(orderShop);
					   
				   }
			   }
		   }
		   return row;
	}


	
	
	
}
