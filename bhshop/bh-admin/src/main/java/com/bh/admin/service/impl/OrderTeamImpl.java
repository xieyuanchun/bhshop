package com.bh.admin.service.impl;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.goods.CouponLogMapper;
import com.bh.admin.mapper.goods.CouponMapper;
import com.bh.admin.mapper.goods.GoodsCartMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicDauctionLogMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.goods.TopicPrizeConfigMapper;
import com.bh.admin.mapper.goods.TopicPrizeLogMapper;
import com.bh.admin.mapper.goods.TopicSavemoneyLogMapper;
import com.bh.admin.mapper.goods.TopicSeckillLogMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberNoticeMapper;
import com.bh.admin.mapper.user.PromoteUserMapper;
import com.bh.admin.pojo.goods.Coupon;
import com.bh.admin.pojo.goods.CouponLog;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCart;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicDauctionLog;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.TopicPrizeConfig;
import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.admin.pojo.goods.TopicSavemoneyLog;
import com.bh.admin.pojo.goods.TopicSeckillLog;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.order.TeamLastOne;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberNotice;
import com.bh.admin.pojo.user.PromoteUser;
import com.bh.admin.service.JDOrderService;
import com.bh.admin.service.OrderTeamService;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.config.Contants;
import com.bh.utils.IDUtils;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.bh.utils.TopicUtils;
import com.github.pagehelper.PageHelper;


@Service
@Transactional
public class OrderTeamImpl implements OrderTeamService{
	@Autowired
	private OrderTeamMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private GoodsCartMapper cartMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	@Autowired
	private TopicPrizeLogMapper topicPrizeLogMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private TopicSeckillLogMapper seckillMapper;
	@Autowired
	private TopicSavemoneyLogMapper topicSavemoneyLogMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
    private TopicPrizeConfigMapper topicPrizeConfigMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private MemberNoticeMapper memberNoticeMapper;
	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private TopicDauctionLogMapper dauctionLogMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	
	/**
	 * 后台拼团订单管理列表
	 */
	@Override
	public PageBean<OrderTeam> pageList(String pageSize, String currentPage, String teamNo, String status, int shopId, String orderNo)
			throws Exception {	
		OrderTeam entity = new OrderTeam();
		entity.setTeamNo(teamNo);
		if(StringUtils.isNotEmpty(status)){
			entity.setStatus(Integer.parseInt(status));
		}
		entity.setShopId(shopId);
		entity.setOrderNo(orderNo);
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderTeam> list = mapper.pageList(entity);
		if(list.size()>0){
			for(OrderTeam team : list){
				if(team.getType()==0){
					Member member = memberMapper.selectByPrimaryKey(team.getmId());
					if(member!=null){
						//team.setmName(member.getUsername()); //用户昵称
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(member.getUsername(), "utf-8"));//用户昵称
						//end
					}
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
					if(promoteUser!=null){
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(promoteUser.getName(), "utf-8"));//用户昵称
						//end
					}
				}
				GoodsSku sku = skuMapper.selectByPrimaryKey(team.getGoodsSkuId());
				JSONObject jsonObj = new JSONObject(sku.getValue()); //获取sku商品信息
				String value = jsonObj.getString("value");
				JSONArray image = jsonObj.getJSONArray("url");
				String imageUrl = (String) image.get(0);
				team.setImageUrl(imageUrl);//商品图片
				team.setGoodsSku(value); //商品规格
				
				double price = (double)sku.getTeamPrice() / 100;
				team.setPrice(price);//商品团购价
				Goods goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
				team.setGoodsName(goods.getName());//商品名称
				team.setTeamNum(goods.getTeamNum()); //开团人数
				
				int groupCount = mapper.groupCount(team.getTeamNo());
				int num = goods.getTeamNum()-groupCount;
				team.setWaitNum(num); //离开团还差多少人
				
				Order order = orderMapper.getOrderByOrderNo(team.getOrderNo());
				if(order!=null){ //正常单
		    		List<OrderSku> orderSku = orderSkuMapper.getByOrderId(order.getId());
		        	team.setNum(orderSku.get(0).getSkuNum());//商品数量
		        	double totalPrice = orderSku.get(0).getSkuNum() * price;
		        	team.setTotalPrice(totalPrice); //订单总价
		    	}else{ //促团单
		    		team.setNum(1);//商品数量
		    		double totalPrice = 1 * price;
		    		team.setTotalPrice(totalPrice); //订单总价
		    	}
			}
		}
		PageBean<OrderTeam> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 移动端商品详情获取团购人数和前十条列表
	 */
	@Override
	public Map<String, Object> getGroupNumAndList(String goodsId, Member user) throws Exception {
		List<OrderTeam> list = mapper.getGroupingList(Integer.parseInt(goodsId));
		int groupCount = 0;
		if(list.size()>0){
			for(OrderTeam team : list){
				//cheng
				List<OrderTeam> lastList = mapper.selectLastOne(team.getTeamNo());
				if (lastList.size()>0) {
					TeamLastOne teamLastOne = new TeamLastOne();
					Member member1 = memberMapper.selectByPrimaryKey(lastList.get(0).getmId());
					if(member1!=null){
						if(StringUtils.isNotEmpty(member1.getHeadimgurl())){
							teamLastOne.setHeadimgurl(member1.getHeadimgurl()); //主单用户头像
						}else{
							teamLastOne.setHeadimgurl(Contants.headImage); //默认头像
						}
						teamLastOne.setUsername(member1.getUsername()); //主单用户昵称
					}else{
						PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(lastList.get(0).getmId());
						if(promoteUser!=null){
							teamLastOne.setHeadimgurl(promoteUser.getHeadImg()); //主单用户头像
							teamLastOne.setUsername(promoteUser.getName()); //主单用户昵称
						}
					}
					team.setTeamLastOne(teamLastOne);
				}
				//end
				
				
				if(user!=null){
					OrderTeam orderTeam = mapper.getByMidAndTeamNo(user.getId(), team.getTeamNo());
					if(orderTeam!=null){
						Order order = orderMapper.getOrderByOrderNo(orderTeam.getOrderNo());
						if(order!=null){
							team.setOrderId(order.getId());
						}
					}
				}
				Member member = memberMapper.selectHeadAndName(team.getmId());
				if(member!=null){
					if (StringUtils.isNotEmpty(member.getHeadimgurl())) {
						team.setHeadimgurl(member.getHeadimgurl()); //用户头像
					}else{
						team.setHeadimgurl(Contants.headImage);
					}
				
					team.setUsername(member.getUsername()); //用户昵称
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
					if(promoteUser!=null){
						team.setHeadimgurl(promoteUser.getHeadImg()); //用户头像
						team.setUsername(promoteUser.getName()); //用户昵称
					}
				}
				
				groupCount = mapper.groupCount(team.getTeamNo());
				Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodsId));
				int num = goods.getTeamNum()-groupCount;
				team.setWaitNum(num);
				
				long a = new Date().getTime();
				long b = team.getEndTime().getTime();
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
					String waitTime = hours+":"+min+":"+sencond;
					team.setWaitTime(waitTime);
				}
			}
		}
		
		int num = mapper.getGroupCount(Integer.parseInt(goodsId));
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("num", num);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 后台拼团订单详情
	 */
	@Override
	public OrderTeam getDetails(String id) throws Exception {
		OrderTeam team = mapper.selectByPrimaryKey(Integer.parseInt(id));
		
		Member member = memberMapper.selectByPrimaryKey(team.getmId());
		if(member!=null){
			//team.setmName(member.getUsername()); //用户昵称
			//20185.5.23 zlk
			team.setmName(URLDecoder.decode(member.getUsername(), "utf-8"));//用户昵称
			//end
		}else{
			PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
			if(promoteUser!=null){
				//team.setmName(promoteUser.getName()); //用户昵称
				//20185.5.23 zlk
				team.setmName(URLDecoder.decode(promoteUser.getName(), "utf-8"));//用户昵称
				//end
			}
		}
		GoodsSku sku = skuMapper.selectByPrimaryKey(team.getGoodsSkuId());
		JSONObject jsonObj = new JSONObject(sku.getValue()); //获取sku商品信息
		String value = jsonObj.getString("value");
		JSONArray image = jsonObj.getJSONArray("url");
		String imageUrl = (String) image.get(0);
		team.setImageUrl(imageUrl);//商品图片
		team.setGoodsSku(value); //商品规格
		
		double price = (double)sku.getTeamPrice() / 100;
		team.setPrice(price);//商品团购价
		Goods goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
		team.setGoodsName(goods.getName());//商品名称
		team.setTeamNum(goods.getTeamNum()); //开团人数
		
		int groupCount = mapper.groupCount(team.getTeamNo());
		int num = goods.getTeamNum()-groupCount;
		team.setWaitNum(num); //离开团还差多少人
		
		Order order = orderMapper.getOrderByOrderNo(team.getOrderNo());
		
		double totalPrice=0;//总价
		double realgDeliveryPrice =0;//邮费
		double realCouponsPrice=0;//优惠卷抵扣金额
		double realSavePrice =0;//滨惠豆抵扣金额
    	if(order!=null){ //正常单
    		
    		List<OrderShop> orderShopList= orderShopMapper.getByOrderId(order.getId());
    	    realgDeliveryPrice = (double)orderShopList.get(0).getgDeliveryPrice()/100;//邮费“分”转化成“元”
    	    
    		realSavePrice = (double) orderShopList.get(0).getSavePrice() / 100;// 滨惠豆的折扣“分”转化成“元”
    		if(orderShopList.get(0).getCouponId()>0){//xieyc
 			    realCouponsPrice = (double)orderShopList.get(0).getCouponPrice()/100;//优惠卷的折扣“分”转化成“元”
 			    team.setRealCouponsPrice(realCouponsPrice);
 			}else{
 				team.setRealCouponsPrice(0);
 			}
    		
    		List<OrderSku> orderSku = orderSkuMapper.getByOrderId(order.getId());
        	team.setNum(orderSku.get(0).getSkuNum());//商品数量
            totalPrice = orderSku.get(0).getSkuNum() * price;
    	}else{ //促团单
    		team.setNum(1);//商品数量
    		totalPrice = 1 * price;
    	}
    	team.setTotalPrice(totalPrice); //订单总价
    	team.setRealCouponsPrice(realCouponsPrice);//优惠卷价格
    	team.setRealSavePrice(realSavePrice);//滨惠豆价格
    	team.setRealPrice(totalPrice-realCouponsPrice-realSavePrice);//金额（总价-优惠卷-滨惠豆）
    	team.setRealgDeliveryPrice(realgDeliveryPrice);//邮费
		return team;
	}
	
	@Override
	public int testInsert(String teamNo) throws Exception {
		int row = 0;
		int orderId = 656;
		Order order = orderMapper.selectByPrimaryKey(orderId);
		OrderTeam oTeam = mapper.getByMidAndTeamNo(order.getmId(), teamNo);
		if(oTeam==null){
			/*插入拼团订单数据*/
			OrderTeam orderTeam = new OrderTeam();
			orderTeam.setCreateTeamTime(new Date());
			orderTeam.setCreateTime(new Date());
			
			GoodsCart cart = cartMapper.selectByPrimaryKey(1471);
			orderTeam.setGoodsSkuId(cart.getGskuid());
			
			orderTeam.setmId(order.getmId());
			
			orderTeam.setOrderNo(order.getOrderNo());
			Goods goods = goodsMapper.selectByPrimaryKey(cart.getgId());
			if(!com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(teamNo)){ //判断是开团还是参团
				List<OrderTeam> teamList = mapper.getByTeamNo(teamNo);
				if(teamList.size()>0){
					orderTeam.setEndTime(teamList.get(0).getEndTime());
				}
				orderTeam.setTeamNo(teamNo);
				orderTeam.setIsOwner(0);
				
				int groupCount = mapper.groupCount(teamNo);
				int num = goods.getTeamNum()-groupCount;
				if(num==1){
					if(teamList.size()>0){
						for(OrderTeam team : teamList){
							team.setStatus(1);
							mapper.updateByPrimaryKeySelective(team);
						}
					}
					orderTeam.setStatus(1);
				}else{
					orderTeam.setStatus(0);
				}
				
			}else{
				orderTeam.setStatus(0);
				orderTeam.setTeamNo(IDUtils.getOrderNo());
				orderTeam.setIsOwner(1);
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				long now = System.currentTimeMillis();
				
				
				int milliSecond = 0;
				if(goods.getTimeUnit()==1){ //分钟
					milliSecond = goods.getTeamEndTime() * 60000;
				}
				if(goods.getTimeUnit()==2){ //小时
					milliSecond = goods.getTeamEndTime() * 60 * 60000;
				}
				if(goods.getTimeUnit()==3){ //天数
					milliSecond = goods.getTeamEndTime() * 24 * 60 * 60000;
				}
				
				long thisTime = now + milliSecond;
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(thisTime);
				String endTime = formatter.format(calendar.getTime());
				orderTeam.setEndTime(formatter.parse(endTime));
			}
			row = mapper.insertSelective(orderTeam);
		}
		return row;
	}
	
	/**
	 * 团购单的插入
	 */
	@Override
	public int insertGroupOrder(String orderNo, String teamNo) throws Exception {
		int row = 0;
		Order order = orderMapper.getOrderByOrderNo(orderNo);
		List<OrderShop> shopList = orderShopMapper.getByOrderId(order.getId());
		List<OrderSku> skuList = orderSkuMapper.getByOrderShopId(shopList.get(0).getId());
		
		OrderTeam oTeam = mapper.getByMidAndTeamNo(order.getmId(), teamNo);
		if(oTeam==null){
			/*插入拼团订单数据*/
			OrderTeam orderTeam = new OrderTeam();
			orderTeam.setCreateTime(new Date());
			
			orderTeam.setGoodsSkuId(skuList.get(0).getSkuId());
			
			orderTeam.setmId(order.getmId());
			
			orderTeam.setOrderNo(order.getOrderNo());
			Goods goods = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
			orderTeam.setGoodsId(skuList.get(0).getGoodsId());
			if(!com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(teamNo)){ //判断是开团还是参团
				List<OrderTeam> teamList = mapper.getByTeamNo(teamNo);
				if(teamList.size()>0){
					orderTeam.setEndTime(teamList.get(0).getEndTime());
					orderTeam.setCreateTeamTime(teamList.get(0).getCreateTeamTime());
				}
				orderTeam.setTeamNo(teamNo);
				orderTeam.setIsOwner(0);
				
				int groupCount = mapper.groupCount(teamNo);
				int num = goods.getTeamNum()-groupCount;
				if(num==1){
					if(teamList.size()>0){
						for(int i=0;i<teamList.size();i++){
							OrderTeam myTeam = new OrderTeam();
							myTeam.setId(teamList.get(i).getId());
							myTeam.setStatus(1);
							myTeam.setEndTime(new Date());
							mapper.updateByPrimaryKeySelective(myTeam);
							
							//cheng type:0人工下单  1系统自动下单
							if (teamList.get(i).getType().equals(0)) {
								//0人工下单  1系统自动下单
								Order order2 = new Order();
								order2 = orderMapper.getOrderByOrderNo(teamList.get(i).getOrderNo());
								OrderSku orderSku = new OrderSku();
								orderSku.setOrderId(order2.getId());
								List<OrderSku> list = orderSkuMapper.selectJdSupport(orderSku);
								if (list.size() > 0) {
									jdOrderService.updateJDOrderId(order2);
								}
								System.out.println("调用微信消息模板消息发送......");
								wechatTemplateMsgService.sendGroupTemplate(String.valueOf(teamList.get(i).getmId()), String.valueOf(order2.getId()), String.valueOf(teamList.get(i).getGoodsId()));
							}
							
							//cheng拼单消息推送
							if (i!=0) {
								insertMemberNotice(teamList.get(i).getmId(),teamList.get(i).getOrderNo());
							}
						}
					}
					orderTeam.setStatus(1);
					orderTeam.setEndTime(new Date());
					insertMemberNotice(order.getmId(),orderNo);
					wechatTemplateMsgService.sendGroupTemplate(String.valueOf(order.getmId()), String.valueOf(order.getId()), String.valueOf(skuList.get(0).getGoodsId()));
				}else{
					orderTeam.setStatus(0);
				}
				
			}else{
				orderTeam.setCreateTeamTime(new Date());
				orderTeam.setStatus(0);
				orderTeam.setTeamNo(IDUtils.getOrderNo());
				orderTeam.setIsOwner(1);
				long now = System.currentTimeMillis();
				
				
				int milliSecond = 0;
				if(goods.getTimeUnit()==1){ //分钟
					milliSecond = goods.getTeamEndTime() * 60000;
				}
				if(goods.getTimeUnit()==2){ //小时
					milliSecond = goods.getTeamEndTime() * 60 * 60000;
				}
				if(goods.getTimeUnit()==3){ //天数
					milliSecond = goods.getTeamEndTime() * 24 * 60 * 60000;
				}
				
				long thisTime = now + milliSecond;
				Date endTime = new Date();  
				endTime.setTime(thisTime);
				orderTeam.setEndTime(endTime);
				insertMemberNotice(order.getmId(),orderNo);
			}
			row = mapper.insertSelective(orderTeam);
		}
		return row;
	}
	
	/**
	 * 参团预览获取用户头像
	 */
	@Override
	public OrderTeam getGroupUserHead(String teamNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 判断当前用户是否在团中
	 */
	@Override
	public boolean isInGroup(Member member, String teamNo) throws Exception {
		boolean flag = false;
		if(member!=null){
			OrderTeam orderTeam = mapper.getByMidAndTeamNo(member.getId(), teamNo);
			if(orderTeam!=null){
				flag = true;
			}
		}
		return flag;
	}
	
	
	/**
	 * @Description: 抽奖活动新增日志
	 * @author xieyc
	 * @date 2018年1月18日 上午9:31:21 
	 */
	public Integer add(TopicPrizeLog entity) throws Exception{
		Order order=orderMapper.getOrderByOrderNo(entity.getOrderNo());   
		this.insertGroupOrder(entity,order);//走拼图向order_team插入记录
		entity.setmId(order.getmId());//用户id
		entity.setOrderId(order.getId());//订单号
		entity.setIsPrize(Contants.WIN_PRIZE_INITIALIZE);//刚参加活动时，先设置中奖状态为初始化
		entity.setPrizeNo(TopicUtils.generatePrizeNum());//中奖号
		entity.setAddTime(new Date());//新增日期
		Integer returnRow=topicPrizeLogMapper.insertSelective(entity);
		return returnRow;
	}
	
	/**
	 * @Description: 向order_team插入记录
	 * @author xieyc
	 * @date 2018年1月23日 下午4:31:34 
	 */
	private void insertGroupOrder(TopicPrizeLog entity,Order order) throws Exception{
		//查询某个活动的配置
		TopicPrizeConfig topicPrizeConfig=topicPrizeConfigMapper.selectByTgid(entity.getTgId());
		//参加人数
		int joinNum=topicPrizeLogMapper.logNumByTgId(entity.getTgId());

		TopicGoods topicGoods=topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
		
		Topic topic=topicMapper.selectByPrimaryKey(topicGoods.getActId());
		
		String teamNo=entity.getTopicNo();//团号
		
		List<OrderShop> shopList = orderShopMapper.getByOrderId(order.getId());
		List<OrderSku> skuList = orderSkuMapper.getByOrderShopId(shopList.get(0).getId());
		OrderTeam oTeam = mapper.getByMidAndTeamNo(order.getmId(),teamNo);
		if(oTeam==null){
			//插入拼团订单数据
			OrderTeam orderTeam = new OrderTeam();
			orderTeam.setCreateTime(new Date());//下单时间
			
			orderTeam.setGoodsSkuId(skuList.get(0).getSkuId());//商品skuId
			
			orderTeam.setmId(order.getmId());//用户id
			
			orderTeam.setOrderNo(order.getOrderNo());//订单号

			if(!StringUtils.isEmpty(teamNo)){ //参团
				List<OrderTeam> teamList = mapper.getByTeamNo(teamNo);
				if(teamList!=null && teamList.size()>0){
					orderTeam.setCreateTeamTime(teamList.get(0).getCreateTeamTime());//创团时间
				}
				orderTeam.setTeamNo(teamNo);//团号
				orderTeam.setIsOwner(0);//是否发起人
				int num =topicPrizeConfig.getNum() - joinNum;//剩余名额
				if(num==1){//最后一个名额
					orderTeam.setEndTime(new Date());
					orderTeam.setStatus(1);
					mapper.insertSelective(orderTeam);
					if(teamList!=null && teamList.size()>0){
						for(OrderTeam otherTeam : teamList){//拼团成功的时候修改该拼团号的其他拼团订单的状态和开团时间
							otherTeam.setStatus(1);
							otherTeam.setEndTime(orderTeam.getEndTime());
							mapper.updateByPrimaryKeySelective(otherTeam);
						}
					}
				}else{//还有好多名额
					if(teamList!=null && teamList.size()>0){
						orderTeam.setEndTime(teamList.get(0).getEndTime());
					}
					orderTeam.setStatus(0);
					mapper.insertSelective(orderTeam);
				}
			}else{//开团
				orderTeam.setCreateTeamTime(new Date());
				orderTeam.setStatus(0);
				orderTeam.setTeamNo(TopicUtils.getOrderNo());
				orderTeam.setIsOwner(1);
				orderTeam.setEndTime(topic.getEndTime());//开团时间
				mapper.insertSelective(orderTeam);
			}
			
		}
	}
    
    @Override
	public Integer addTopicSaveMoney(TopicSavemoneyLog entity) throws Exception{
    	Integer returnRow=null;
    	Order order = orderMapper.getOrderByOrderNo(entity.getOrderNo());
    	List<OrderSku> skuList = orderSkuMapper.getByOrderId(order.getId());
		TopicSavemoneyLog topicPrizeLog=topicSavemoneyLogMapper.getByMidAndTgId(order.getmId(),entity.getTgId());
		if(StringUtils.isNotEmpty(entity.getActNo())&&topicPrizeLog==null){//没参加过活动，有邀请码，参加该团
			entity.setmId(order.getmId());
			entity.setAddTime(new Date());
			
			TopicGoods topicGoods  = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
			Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
			
			OrderTeam orderTeam2  = mapper.getByTeamNoAndOwner(entity.getMyNo()); //团主
			
			OrderTeam  orderTeam = new OrderTeam();
			orderTeam.setmId(order.getmId());
			orderTeam.setCreateTime(new Date());
			orderTeam.setEndTime(topic.getEndTime());  //活动结束时间
			orderTeam.setIsOwner(0);
			orderTeam.setTeamNo(entity.getMyNo());
			orderTeam.setOrderNo(entity.getOrderNo());
			orderTeam.setGoodsSkuId(entity.getGoodsSkuId());
			orderTeam.setStatus(1);
			orderTeam.setCreateTeamTime(orderTeam2.getCreateTeamTime()); //开团时间
			orderTeam.setType(1);
			mapper.insertSelective(orderTeam);
			returnRow=topicSavemoneyLogMapper.insertSelective(entity);
			int joinNum=topicSavemoneyLogMapper.logNumByTgId(entity.getId());//查询已经参数的人数
			
			//根据TopicSavemoneyLog 的goods_sku_id 获取goods_sku表信息 ，goods_sku的goods_id 获取goods表信息的拼团数
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
			Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());  //团参加限定人数
			if(goods.getTeamNum().equals(joinNum)){//把当前参团的人状态改为拼团成功
				mapper.updateByTeamNo(entity.getMyNo());
			}
			
			
		}else if(StringUtils.isEmpty(entity.getActNo())){//没有邀请码，自己开个团
           
			entity.setActNo(MixCodeUtil.sjs()); //生成4位随机数作为 邀请码
			entity.setMyNo(MixCodeUtil.getCode(8)); //生成8位随机数作为 活动专属码
			entity.setmId(order.getmId());
			entity.setAddTime(new Date());
			
			TopicGoods topicGoods  = topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
			Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
			
			
			OrderTeam  orderTeam = new OrderTeam();
			orderTeam.setmId(order.getmId());
			orderTeam.setCreateTime(new Date());
			orderTeam.setEndTime(topic.getEndTime());//活动结束时间
			orderTeam.setIsOwner(1);
			orderTeam.setTeamNo(entity.getMyNo());
			orderTeam.setOrderNo(entity.getOrderNo());
			orderTeam.setGoodsSkuId(skuList.get(0).getSkuId());
			orderTeam.setStatus(1);
			orderTeam.setCreateTeamTime(new Date());//开团时间
			orderTeam.setType(1);
			mapper.insertSelective(orderTeam);
			
			returnRow=topicSavemoneyLogMapper.insertSelective(entity);
		}
		return returnRow;
	}
    
  //秒杀活动日志的插入
  	@Transactional
  	@Override
  	public int addSeckillLog(TopicSeckillLog entity) throws Exception{
  		Order order=orderMapper.getOrderByOrderNo(entity.getOrderNo());   
		List<OrderSku> orderSkuList =orderSkuMapper.getOrderSkuByOrderId(order.getId());
		if(orderSkuList!=null&& orderSkuList.size()>0){
			entity.setGoodsSkuId(orderSkuList.get(0).getSkuId());
		}
		entity.setmId(order.getmId());//用户id
		entity.setActNo(MixCodeUtil.getOrderNo());
		entity.setAddTime(new Date());
		return seckillMapper.insertSelective(entity);
  	}

	/*@Override
	public PageBean<OrderTeam> listPage(OrderTeam entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<OrderTeam> list = mapper.listPage(entity);
		PageBean<OrderTeam> pageBean = new PageBean<>(list);
		if(list.size()>0){
			for(OrderTeam team : list){
				if(team.getType()==0){
					Member member = memberMapper.selectByPrimaryKey(team.getmId());
					if(member!=null){
						team.setmName(member.getUsername()); //用户昵称
					}
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
					if(promoteUser!=null){
						team.setmName(promoteUser.getName());//用户昵称
					}
				}
				GoodsSku sku = skuMapper.selectByPrimaryKey(team.getGoodsSkuId());
				JSONObject jsonObj = new JSONObject(sku.getValue()); //获取sku商品信息
				String value = jsonObj.getString("value");
				JSONArray image = jsonObj.getJSONArray("url");
				String imageUrl=null;
				if(image!=null){
					imageUrl = (String) image.get(0);
					
				}
				team.setImageUrl(imageUrl);//商品图片
				team.setGoodsSku(value); //商品规格
				double price = (double)sku.getTeamPrice() / 100;
				team.setPrice(price);//商品团购价
				Goods goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
				if (goods == null) {
					goods.setTeamNum(1);
					goods.setName("");
				}
				team.setGoodsName(goods.getName());//商品名称
				team.setTeamNum(goods.getTeamNum()); //开团人数
				
				int groupCount = mapper.groupCount(team.getTeamNo());
				int num = goods.getTeamNum()-groupCount;
				team.setWaitNum(num); //离开团还差多少人
				
				Order order = orderMapper.getOrderByOrderNo(team.getOrderNo());
				if(order!=null){ //正常单
		    		List<OrderSku> orderSku = orderSkuMapper.getByOrderId(order.getId());
		        	team.setNum(orderSku.get(0).getSkuNum());//商品数量
		        	double totalPrice = orderSku.get(0).getSkuNum() * price;
		        	team.setTotalPrice(totalPrice); //订单总价
		    	}else{ //促团单
		    		team.setNum(1);//商品数量
		    		double totalPrice = 1 * price;
		    		team.setTotalPrice(totalPrice); //订单总价
		    	}
			}
		}
		return pageBean;
	}
	*/
	public void insertMemberNotice(Integer mId,String orderNo){
		try {
			Member member = memberMapper.selectByPrimaryKey(mId);
			//2018-02-28cheng开团需要推送消息
			MemberNotice notice = new MemberNotice();
			java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date date = new Date();
		    //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date));
		    String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
			String string =member.getUsername() + "在1秒前发起了拼单";
			notice.setmId(member.getId());
			notice.setMessage(string);
			notice.setMsgType(2);
			notice.setIsRead(0);
			notice.setAddTime(new Date());
			notice.setUpdateTime(new Date());
			notice.setLastExcuTime(sdf.parse(d));
			Order order = orderMapper.getOrderByOrderNo(orderNo);
			if (order !=null) {
				OrderSku param = new OrderSku();
				param.setOrderId(order.getId());
				List<OrderSku> orderSku = orderSkuMapper.selectOrderSkuByOrderId(param);
				if (orderSku.size() > 0) {
					notice.setGoodsId(orderSku.get(0).getGoodsId());
					notice.setGoodsskuId(orderSku.get(0).getSkuId());
				}
			}
			memberNoticeMapper.insertSelective(notice);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	//拍卖日志插入
	@Override
	public Integer insertDauctionLog(TopicDauctionLog entity) throws Exception {
		entity.setAddTime(new Date());
		Order order = orderMapper.getOrderByOrderNo(entity.getOrderNo());
		if(order!=null){
			entity.setmId(order.getmId());
			entity.setPrice(order.getOrderPrice());
			List<OrderSku> orderSku = orderSkuMapper.getByOrderId(order.getId());
			if(orderSku.size()>0){
				entity.setGoodsSkuId(orderSku.get(0).getSkuId());
				List<TopicGoods> topicGoodsList = topicGoodsMapper.selectByGoodsId(orderSku.get(0).getGoodsId());
				if(topicGoodsList.size()>0){
					entity.setTgId(topicGoodsList.get(0).getId());
				}
			}
		}
		return dauctionLogMapper.insertSelective(entity);
	}
	
	
	
	
	/**
	 * 程凤云
	 * **/
	@Override
	public PageBean<OrderTeam> listPage(OrderTeam entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<OrderTeam> list = mapper.listPage(entity);
		PageBean<OrderTeam> pageBean = new PageBean<>(list);
		if(list.size()>0){
			for(OrderTeam team : list){
				if(team.getType()==0){
					Member member = memberMapper.selectByPrimaryKey(team.getmId());
					if(member!=null){
						//team.setmName(member.getUsername()); //用户昵称
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(member.getUsername(), "utf-8"));//用户昵称
						//end
					}
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
					if(promoteUser!=null){
						//team.setmName(promoteUser.getName());//用户昵称
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(promoteUser.getName(), "utf-8"));//用户昵称
						//end
					}
				}
				JSONObject jsonObj = new JSONObject(team.getGoodsSku()); //获取sku商品信息
				String value = jsonObj.getString("value");
				JSONArray image = jsonObj.getJSONArray("url");
				String imageUrl=null;
				if(image!=null){
					imageUrl = (String) image.get(0);
					
				}
			    team.setImageUrl(imageUrl);//商品图片
				team.setGoodsSku(value); //商品规格
				int groupCount = mapper.groupCount(team.getTeamNo());
				int num = team.getTeamNum()-groupCount;
				team.setWaitNum(num); //离开团还差多少人
				
				
			}
		}
		return pageBean;
	}

	@Override
	public Map<String, Object> listPageSecond(OrderTeam entity) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		entity.setPage((Integer.valueOf(entity.getCurrentPage())-1)*Contants.PAGE_SIZE);
		entity.setPageSize(Contants.PAGE_SIZE);
		int listAll = mapper.getAll(entity);
		List<OrderTeam> list =mapper.listPageByLimit(entity); 
		if(list.size()>0){
			for(OrderTeam team : list){
				if(team.getType()==0){
					Member member = memberMapper.selectByPrimaryKey(team.getmId());
					if(member!=null){
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(member.getUsername(), "utf-8"));//用户昵称
						//end
					}
				}else{
					PromoteUser promoteUser = promoteUserMapper.selectByPrimaryKey(team.getmId());
					if(promoteUser!=null){
						//20185.5.23 zlk
						team.setmName(URLDecoder.decode(promoteUser.getName(), "utf-8"));//用户昵称
						//end
					}
				}
				net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(team.getGoodsSku()); //获取sku商品信息
				String value = jsonObj.getString("value");
				net.sf.json.JSONArray image = jsonObj.getJSONArray("url");
				if(image!=null){
					team.setImageUrl(String.valueOf(image.get(0)));//商品图片
				}
				team.setGoodsSku(value); //商品规格
				int groupCount = mapper.groupCount(team.getTeamNo());
				team.setWaitNum(team.getTeamNum()-groupCount); //离开团还差多少人
			}
		}
		map.put("list", list);// 当前页数据
	    int pages = listAll / entity.getPageSize(); 
		if (listAll % entity.getPageSize() != 0) {
			pages = pages + 1;
		}
		map.put("total", listAll);//总数据
		map.put("pages", pages);//总页数
		map.put("size", list.size());//当前页的数据（几条）
		map.put("pageNum", entity.getCurrentPage());//当前页数
		map.put("pageSize", Contants.PAGE_SIZE);//当前页条数（默认十条）
		return map;
	}
	
}
