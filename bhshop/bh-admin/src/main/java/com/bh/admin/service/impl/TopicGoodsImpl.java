package com.bh.admin.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.config.Contants;
import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicBargainConfigMapper;
import com.bh.admin.mapper.goods.TopicBargainLogMapper;
import com.bh.admin.mapper.goods.TopicDauctionMapper;
import com.bh.admin.mapper.goods.TopicDauctionPriceMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.goods.TopicTypeMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicBargainConfig;
import com.bh.admin.pojo.goods.TopicBargainLog;
import com.bh.admin.pojo.goods.TopicDauction;
import com.bh.admin.pojo.goods.TopicDauctionPrice;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.TopicType;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicGoodsService;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
@Service
public class TopicGoodsImpl implements TopicGoodsService{
    @Autowired
    private TopicGoodsMapper mapper;
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
    private TopicBargainConfigMapper bargainConfigMapper;
    @Autowired
    private TopicBargainLogMapper bargainLogMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private TopicGoodsMapper topicGoodsMapper;
    @Autowired
	private TopicDauctionMapper dauctionMapper;
    @Autowired
	private TopicDauctionPriceMapper dauctionPriceMapper;
    
	@Value(value = "${pageSize}")
	private String pageSize;

	@Override
	public int add(TopicGoods entity) {
		int row = 0;
		String[] str = entity.getgIdStr().split(",");
		for(int i=0; i<str.length; i++){
			entity.setcTime(new Date());
			entity.setuTime(new Date());
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(str[i]));
			entity.setSid(goods.getShopId());
			entity.setCid(goods.getCatId());
			entity.setGoodsId(Integer.parseInt(str[i]));
			row = mapper.insertSelective(entity);
		}
		return row;
	}

	@Override
	public int update(TopicGoods entity) {
		TopicGoods topicGoods = mapper.selectByPrimaryKey(entity.getId());
		Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
		long a = new Date().getTime();
		long b = topic.getEndTime().getTime();
		long c = topic.getStartTime().getTime();
		if(b>a && a>c){
			return 999;
		}
		entity.setuTime(new Date());
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public TopicGoods get(TopicGoods entity) {
		TopicGoods t = mapper.selectByPrimaryKey(entity.getId());
		Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
		if(goods!=null){
			t.setGoodsName(goods.getName());
		}
		GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
		if(category!=null){
			t.setcName(category.getName());
		}
		return t;
	}

	@Override
	public PageBean<TopicGoods> listPage(TopicGoods entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicGoods> list = mapper.listPage(entity);
		if(list.size()>0){
			for(TopicGoods t : list){
				GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
				if(category!=null){
					t.setcName(category.getName());
				}
				
				Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
				t.setGoodsStatus(goods.getStatus());//商品状态
				t.setGoodsImage(goods.getImage());
				t.setGoodsName(goods.getName());
				//List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(t.getGoodsId());
				List<TopicDauction> dauction = dauctionMapper.selectByTgId(t.getId());
				if(dauction.size()>0){
					t.setPrice((double)dauction.get(0).getDauctionPrice()/100);
				}
				Topic topic = topicMapper.selectByPrimaryKey(t.getActId());
				t.setTopicName(topic.getName());
				
				t.setType(topic.getType());
				
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				t.setTypeName(type.getName());
				
				t.setStartTime(topic.getStartTime());
				t.setEndTime(topic.getEndTime());
				
				List<TopicDauction> dauctionList = dauctionMapper.selectByTgId(t.getId());
				if(dauctionList.size()>0){
					t.setTimeSection(dauctionList.get(0).getTimeSection());
					t.setRealLowPrice((double)dauctionList.get(0).getLowPrice()/100);
					t.setRealScopePrice((double)dauctionList.get(0).getScopePrice()/100);
				}
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int delete(TopicGoods entity) {
		TopicGoods topicGoods = mapper.selectByPrimaryKey(entity.getId());
		Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
		long a = new Date().getTime();
		long b = topic.getEndTime().getTime();
		long c = topic.getStartTime().getTime();
		if(b>a && a>c){
			return 999;
		}
		topicGoods.setIsDelete(1);
		return mapper.updateByPrimaryKeySelective(topicGoods);
	}

	@Override
	public int audit(TopicGoods entity) {
		int row = 0;
		TopicGoods topicGoods = mapper.selectByPrimaryKey(entity.getId());
		if(topicGoods!=null){
			Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
			if(goods.getStatus()!=5){
				return 666;
			}
			topicGoods.setStatus(entity.getStatus());
			topicGoods.setRemark(entity.getRemark());
			row = mapper.updateByPrimaryKeySelective(topicGoods);
		}
		return row;
	}
	
	
	//砍价活动配置
	@Override
	public int addTopicBargain(TopicGoods entity) {
		int row = 0;
		String[] arr1 = entity.getgIdStr().split(",");
		
		String s2 = getTopicStr(entity.getActId()); //重复提交判断
	    if(s2!=null){
	    	String[] arr2 = s2.split(",") ; 
			for(int i=0; i<arr1.length; i++){
				for (int j = 0; j < arr2.length; j++) {
					if( Integer.parseInt(arr1[i]) == Integer.parseInt(arr2[j])){
						return 999;
					}
				}
			}
	    }
		for(int i=0; i<arr1.length; i++){
			TopicGoods topic = new TopicGoods();
			topic.setActId(entity.getActId());
			topic.setcTime(new Date());
			topic.setuTime(new Date());
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(arr1[i]));
			topic.setSid(goods.getShopId());
			topic.setCid(goods.getCatId());
			topic.setGoodsId(Integer.parseInt(arr1[i]));

			row = mapper.insertSelective(topic);
			
			TopicBargainConfig config = new TopicBargainConfig();
		    config.setNum(entity.getNum());
		    List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(goods.getId());
		    if(skuList.size()>0){
		    	config.setActPrice(skuList.get(0).getTeamPrice());
			    config.setActBalance(skuList.get(0).getTeamPrice());
		    }else{
		    	config.setActPrice(goods.getTeamPrice());
			    config.setActBalance(goods.getTeamPrice());
		    }
		    config.setTgId(topic.getId());
		    row = bargainConfigMapper.insertSelective(config);
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
	
	//砍价活动商品列表
	@Override
	public PageBean<TopicGoods> listPageTopicBargain(TopicGoods entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicGoods> list = mapper.listPage(entity);
		if(list.size()>0){
			for(TopicGoods t : list){
				GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
				if(category!=null){
					t.setcName(category.getName());
				}
				
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(t.getGoodsId());
				Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
				t.setGoodsStatus(goods.getStatus());//商品状态
				if(skuList.size()>0){
					t.setPrice((double)skuList.get(0).getTeamPrice()/100);
				}else{
					t.setPrice((double)goods.getTeamPrice()/100);
				}
				t.setGoodsImage(goods.getImage());
				t.setGoodsName(goods.getName());
				
				
				Topic topic = topicMapper.selectByPrimaryKey(t.getActId());
				t.setTopicName(topic.getName());
				
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				t.setTypeName(type.getName());
				
				t.setStartTime(topic.getStartTime());
				t.setEndTime(topic.getEndTime());
				
				
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 移动端砍价活动专区商品列表
	 */
	@Override
	public PageBean<TopicGoods> apiTopicGoodsList(TopicGoods entity, Member member) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicGoods> list = mapper.getByType(entity.getType());
		if(list.size()>0){
			for(TopicGoods topicGoods : list){
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				if(skuList.size()>0){
					topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
					org.json.JSONObject jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					topicGoods.setGoodsImage((String) personList.get(0));
					topicGoods.setGoodsName(skuList.get(0).getGoodsName());
				}else{
					topicGoods.setGoodsImage(goods.getImage());
					topicGoods.setGoodsName(goods.getName());
					topicGoods.setRealPrice((double)goods.getTeamPrice()/100);
				}
				
				if(member!=null){//判断重复砍价
					List<TopicBargainLog> t = bargainLogMapper.selectByTgIdAndMidAndOwner(topicGoods.getId(), member.getId(), 1);
					if(t.size()>0){
						topicGoods.setIsBargain(true);
					}else{
						topicGoods.setIsBargain(false);
					}
				}
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 砍价详情
	 */
	@Override
	public Map<String, Object> bargainAfterPage(TopicGoods entity, int mId) {
		Map<String, Object> userInfo = new LinkedHashMap<>(); //用户信息
		Member member = memberMapper.selectByPrimaryKey(mId);
		
		if(StringUtils.isEmptyOrWhitespaceOnly(entity.getmId()+"")){//判断是否从分享链接进入详情页
			userInfo.put("mId", member.getId());
			userInfo.put("nick", member.getUsername());
			userInfo.put("head", member.getHeadimgurl());
		}else{
			Member m = memberMapper.selectByPrimaryKey(entity.getmId());
			if(m!=null){
				userInfo.put("mId", m.getId());
				userInfo.put("nick", m.getUsername());
				userInfo.put("head", m.getHeadimgurl());
			}
		}
		
		Map<String, Object> goodsInfo = new LinkedHashMap<>(); //商品信息
		TopicGoods  topicGoods = mapper.selectByPrimaryKey(entity.getId());
		setGetter(topicGoods, goodsInfo);
		
		Map<String, Object> bargainInfo = new LinkedHashMap<>();//砍价信息
		TopicBargainLog t = new TopicBargainLog();
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getBargainNo())){//判断是否从分享链接进入详情页
			t = bargainLogMapper.selectByBargainNoAndOwner(entity.getBargainNo());
		}else{
			List<TopicBargainLog> list = bargainLogMapper.selectByTgIdAndMidAndOwner(entity.getId(), mId, 1);
			t = list.get(0);
		}
		TopicBargainConfig config = bargainConfigMapper.selectByTgid(entity.getId());
		List<TopicBargainLog> list = bargainLogMapper.selectByBargainNo(t.getBargainNo());
		List<Map<String, Object>> bargainList = new ArrayList<>();//帮砍详细列表
		if(list.size()>0){
			double alreadPrice = (double)(config.getActPrice()-list.get(0).getActBalance())/100;
			bargainInfo.put("alreadPrice", alreadPrice);//已砍多少元
			bargainInfo.put("balance", (double)list.get(0).getActBalance()/100);//还差多少元
			bargainInfo.put("bargainNo", t.getBargainNo());
			
			for(TopicBargainLog log : list){
				Member m = memberMapper.selectByPrimaryKey(log.getmId());
				Map<String, Object> bargain = new LinkedHashMap<>();
				bargain.put("head", m.getHeadimgurl());
				bargain.put("nick", m.getUsername());
				bargain.put("bargainPrice", (double)log.getBargainPrice()/100);
				bargainList.add(bargain);
			}
		}
		Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
		String waitTime = getEndTime(topic.getEndTime());
		bargainInfo.put("waitTime", waitTime);//还差多久结束
		
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("userInfo", userInfo);
		map.put("goodsInfo", goodsInfo);
		map.put("bargainInfo", bargainInfo);
		map.put("bargainList", bargainList);
		
		return map;
	}
	
	//砍价商品基本信息
	private Map<String, Object> setGetter(TopicGoods topicGoods, Map<String, Object> goodsInfo){
		goodsInfo.put("id", topicGoods.getId());
		Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
		List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
		if(skuList.size()>0){
			org.json.JSONObject jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
			org.json.JSONArray personList = jsonObj.getJSONArray("url");
			goodsInfo.put("image", (String) personList.get(0));
			if(skuList.get(0).getGoodsName()!=null){
				goodsInfo.put("name", skuList.get(0).getGoodsName());
			}else{
				goodsInfo.put("name", goods.getName());
			}
			goodsInfo.put("price", (double)skuList.get(0).getTeamPrice()/100);
		}else{
			goodsInfo.put("image", goods.getImage());
			goodsInfo.put("name", goods.getName());
			goodsInfo.put("price", (double)goods.getTeamPrice()/100);
		}
		return goodsInfo;
	}
	
	//活动倒计时
	private String getEndTime(Date endTime){
		String waitTime = null;
		long a = new Date().getTime();
		long b = endTime.getTime();
		if(b>a){
			int c = (int)((b-a) / 1000);
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
	
	/**
	 * 我的砍价列表
	 * @param mId
	 * @return
	 */
	@Override
	public PageBean<Map<String, Object>> apiMyTopic(TopicGoods entity, int mId) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<TopicBargainLog> list = bargainLogMapper.selectByMidAndOwner(mId, 1);
		if(list.size()>0){
			for(TopicBargainLog log : list){
				Map<String, Object> map = new LinkedHashMap<>();
				TopicGoods topicGoods = mapper.selectByPrimaryKey(log.getTgId());
				map.put("id", topicGoods.getId());
				
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				if(skuList.size()>0){
					org.json.JSONObject jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //商品基本信息
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					String url = (String) personList.get(0);
					map.put("goodsImage", url);
					if(skuList.get(0).getGoodsName()!=null){
						map.put("goodsName", skuList.get(0).getGoodsName());
					}else{
						map.put("goodsName", goods.getName());
					}
				}else{
					map.put("goodsImage", goods.getImage());
					map.put("goodsName", goods.getName());
				}
				
				TopicBargainConfig config = bargainConfigMapper.selectByTgid(log.getTgId());
				map.put("price", (double)config.getActPrice()/100);
				
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
				String waitTime = getEndTime(topic.getEndTime());
				map.put("waitTime", waitTime);//还差多久结束
				
				List<TopicBargainLog> logList = bargainLogMapper.selectByBargainNo(log.getBargainNo());
				map.put("balance", (double)logList.get(0).getActBalance()/100);//还差多少元
				
				//String status = getBargainStatus(log.getStatus());
				map.put("status", log.getStatus()); //砍价状态
				
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
	
	//砍价状态
	private String getBargainStatus(int status){
		String str = null;
		if(status==0){
			str="砍价中";
		}else if(status==1){
			str="砍价成功";
		}else if(status==2){
			str="砍价失败";
		}else{
			str="活动已结束";
		}
		return str;
	}
	
	
	/**
	 * 砍价详情
	 */
	@Override
	public Map<String, Object> wxBargainAfterPage(TopicGoods entity, Member member) {
		Map<String, Object> goodsInfo = new LinkedHashMap<>(); //商品信息
		TopicGoods  topicGoods = mapper.selectByPrimaryKey(entity.getId());
		setGetter(topicGoods, goodsInfo);
		
		Map<String, Object> bargainInfo = new LinkedHashMap<>();//砍价信息
		TopicBargainLog t = new TopicBargainLog();
		if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getBargainNo())){//判断是否从分享链接进入详情页
			t = bargainLogMapper.selectByBargainNoAndOwner(entity.getBargainNo());
		}else{
			List<TopicBargainLog> list = bargainLogMapper.selectByTgIdAndMidAndOwner(entity.getId(), member.getId(), 1);
			t = list.get(0);
		}
		Map<String, Object> userInfo = new LinkedHashMap<>(); //发起者用户信息
		userInfo.put("mId", t.getmId());
		userInfo.put("nick", t.getNickName());
		userInfo.put("head", t.getHeadImgUrl());
		
		TopicBargainConfig config = bargainConfigMapper.selectByTgid(entity.getId());
		List<TopicBargainLog> list = bargainLogMapper.selectByBargainNo(t.getBargainNo());
		List<Map<String, Object>> bargainList = new ArrayList<>();//帮砍详细列表
		if(list.size()>0){
			double alreadPrice = (double)(config.getActPrice()-list.get(0).getActBalance())/100;
			bargainInfo.put("alreadPrice", alreadPrice);//已砍多少元
			bargainInfo.put("balance", (double)list.get(0).getActBalance()/100);//还差多少元
			bargainInfo.put("bargainNo", t.getBargainNo());
			bargainInfo.put("status", t.getStatus());
			
			for(TopicBargainLog log : list){
				Map<String, Object> bargain = new LinkedHashMap<>();
				bargain.put("head", log.getHeadImgUrl());
				bargain.put("nick", log.getNickName());
				bargain.put("bargainPrice", (double)log.getBargainPrice()/100);
				bargainList.add(bargain);
			}
		}
		Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
		String waitTime = getEndTime(topic.getEndTime());
		bargainInfo.put("waitTime", waitTime);//还差多久结束
		
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("userInfo", userInfo);
		map.put("goodsInfo", goodsInfo);
		map.put("bargainInfo", bargainInfo);
		map.put("bargainList", bargainList);
	
		List<TopicBargainLog> bLog = bargainLogMapper.selectByTgIdAndOpenIdAndOwner(entity.getId(), entity.getOpenId(), 1);
		if(bLog.size()>0){
			map.put("fz", 1); //发起者进详情
		}else{
			TopicBargainLog tbLog= bargainLogMapper.selectByBargainNoAndOpenId(entity.getBargainNo(), entity.getOpenId());
			int bargainCount = bargainLogMapper.bargainCount(entity.getOpenId()); //没人每天最多帮砍5次
			int count = bargainLogMapper.countBytgId(entity.getId(), entity.getBargainNo());
			if(tbLog!=null || bargainCount>5 || count==config.getNum()){ //fz-1发起人进详情页（叫朋友砍），2朋友进入详情页（我也要参加），3朋友进详情页（帮砍一刀）
				map.put("fz", 2);
			}else{
				map.put("fz", 3);
			}
		}
		return map;
	}
	
	
	/**
	 * 移动端拍卖专区商品列表
	 */
	@Override
	public PageBean<TopicGoods> apiDautionGoodsList(TopicGoods entity, Member member) {
		int second = 0;
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicGoods> list = mapper.getByType(entity.getType());
		if(list.size()>0){
			for(TopicGoods topicGoods : list){
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				
				Map<String, Object> map = new HashMap<>();
				List<TopicDauctionPrice> dauctionList = dauctionPriceMapper.selectByTgId(topicGoods.getId());
				if(dauctionList.size()>0){
					TopicGoods tGoods = topicGoodsMapper.selectByPrimaryKey(dauctionList.get(0).getTgId());
					if(tGoods!=null){
						Topic topic = topicMapper.selectByPrimaryKey(tGoods.getActId());
						map.put("startTime", topic.getStartTime());
						map.put("endTime", topic.getEndTime());
						
						List<TopicDauction> dauction = dauctionMapper.selectByTgId(tGoods.getId());
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
				topicGoods.setDauctionDetail(map);
				
				topicGoods.setType(goods.getTopicType());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				if(skuList.size()>0){
					topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
					topicGoods.setRealMarketPrice((double)skuList.get(0).getMarketPrice()/100);
					org.json.JSONObject jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					topicGoods.setGoodsImage((String) personList.get(0));
					if(skuList.get(0).getGoodsName()!=null){
						topicGoods.setGoodsName(skuList.get(0).getGoodsName());
					}else{
						topicGoods.setGoodsName(goods.getName());
					}
				}else{
					topicGoods.setGoodsImage(goods.getImage());
					topicGoods.setGoodsName(goods.getName());
					topicGoods.setRealPrice((double)goods.getTeamPrice()/100);
				}
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());//活动倒计时
				long startTime = topic.getStartTime().getTime();
				long endTime = topic.getEndTime().getTime();
				long now = System.currentTimeMillis();
				if(startTime>now && endTime > now){ //未开始
					second = (int) ((startTime-now)/1000);
					topicGoods.setCountDown(second);
				}else if(now>startTime && endTime > now){ //进行中
					topicGoods.setCountDown(0);
				}else{
					topicGoods.setCountDown(-1);
				}
				
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 下架活动
	 */
	@Override
	public int downTopic(Topic entity) {
		int row = 0;
		Topic topic = topicMapper.selectByPrimaryKey(entity.getId());
		if(topic!=null){
			topic.setStatus(1);//下架
			row = topicMapper.updateByPrimaryKeySelective(topic);
			List<TopicGoods> tgList = mapper.getByActId(topic.getId());
			if(tgList.size()>0){
				for(TopicGoods topicGoods : tgList){
					Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
					goods.setTopicType(0);
					goods.setTopicGoodsId(null);
					goods.setSaleType(2);
					if(goods.getTeamNum()==0){
						goods.setTeamNum(2);
						goods.setTeamEndTime(1);
						goods.setTimeUnit(3);
					}
					row = goodsMapper.updateByPrimaryKeyBySelf(goods);
				}
			}
		}
		return row;
	}

}
