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
import com.bh.goods.mapper.TopicPrizeLogMapper;
import com.bh.goods.mapper.TopicTypeMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicType;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.product.api.service.TopicPrizeService;
import com.bh.product.api.util.TopicUtils;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.Wallet;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class TopicPrizeImpl implements TopicPrizeService{
    @Autowired
    private TopicGoodsMapper topicGoodsMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private TopicPrizeLogMapper topicPrizeLogMapper;
    @Autowired
    private GoodsCategoryMapper categoryMapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicTypeMapper topicTypeMapper;
    @Autowired
    private  WalletMapper  walletMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper; 
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Value(value = "${pageSize}")
	private String pageSize;

	/**
	 * @Description: 抽奖活动立即报名
	 * @author xieyc
	 * @date 2018年1月17日 下午3:35:28 
	 */
	public int addTopicPrize(TopicGoods topicGoods){
		int row=0;
		String goodsIds=topicGoods.getGoodsIds();
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodsIds)){
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
	 * @Description: 抽奖活动报名列表
	 * @author xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	public PageBean<TopicGoods> listTopicPrize(TopicGoods topicGoods) {
		PageHelper.startPage(Integer.parseInt(topicGoods.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicGoods> listTopicPrize = topicGoodsMapper.listPage(topicGoods);
		if(listTopicPrize!=null&&listTopicPrize.size()>0){
			for(TopicGoods t : listTopicPrize){
				GoodsCategory category = categoryMapper.selectByPrimaryKey(t.getCid());
				if(category!=null){
					t.setcName(category.getName());
				}
			
				Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
				t.setGoodsStatus(goods.getStatus());//商品状态
				t.setGoodsName(goods.getName());//商品名字
				t.setGoodsImage(goods.getImage());//图片地址
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(t.getGoodsId());
				if(skuList!=null && skuList.size()>0){
					t.setRealPrice((double)skuList.get(0).getTeamPrice()/100);
				}
				Topic topic = topicMapper.selectByPrimaryKey(t.getActId());
				t.setTopicName(topic.getName());//专题名字
				
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				t.setTypeName(type.getName());//专题类型名字
				
				t.setStartTime(topic.getStartTime());//开始时间
				t.setEndTime(topic.getEndTime());//结束时间
			}
		}
		PageBean<TopicGoods> pageBean = new PageBean<>(listTopicPrize);
		return pageBean;
	}
	
	/**
	 * @Description: 移动端抽奖活动商品列表
	 * @author xieyc
	 * @date 2018年1月18日 下午3:00:12 
	 */
	public Map<String,Object> apiTopicGoodsList(TopicGoods entity,Member member) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicGoods> list = topicGoodsMapper.getByType(entity.getType());
		Boolean isJoin=null;//是否参加过该活动
		Map<String,Object> map=new HashMap<String,Object>();
		if(list!=null && list.size()>0){
			for(TopicGoods topicGoods : list){
				if(member!=null){
					TopicPrizeLog topicPrizeLog=topicPrizeLogMapper.getByMidAndTgId(member.getId(),topicGoods.getId());
					if(topicPrizeLog!=null){
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
				
				topicGoods.setStartTime(topic.getStartTime());//开始时间
				
				topicGoods.setEndTime(topic.getEndTime());//结束时间
				
				topicGoods.setWaitTime(TopicUtils.getEndTime(topic.getEndTime()));//多久后活动结束
							
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				
				topicGoods.setGoodsImage(goods.getImage());//商品图片
				topicGoods.setGoodsName(goods.getName());//商品名字
				
				topicGoods.setNum(goods.getTeamNum());//活动人数
				
				int joinNum=topicPrizeLogMapper.logNumByTgId(topicGoods.getId());//查询已经参数的人数
				
				topicGoods.setJoinNum(joinNum);//参加人数
				
				topicGoods.setSurplusNum(goods.getTeamNum()-joinNum);//剩余名额
				
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				if(skuList.size()>0){
					topicGoods.setRealPrice((double)skuList.get(0).getTeamPrice()/100);//设置商品价格
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
	 * @Description: 我的抽奖活动列表
	 * @author xieyc
	 * @date 2018年1月18日 下午3:26:06 
	 */
	public PageBean<Map<String, Object>> apiMyTopicPrize(TopicGoods entity, Integer mId) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<TopicPrizeLog> list = topicPrizeLogMapper.getByMid(mId);
		if(list.size()>0){
			for(TopicPrizeLog log : list){
				Map<String, Object> map = new LinkedHashMap<>();
				
				TopicGoods topicGoods = topicGoodsMapper.selectByPrimaryKey(log.getTgId());
				Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
				List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(topicGoods.getGoodsId());
				map.put("goodsName", goods.getName());
				map.put("goodsImage", goods.getImage());
				if(skuList.size()>0){
					map.put("goodsName", skuList.get(0).getGoodsName());
				}		
				
				Topic topic = topicMapper.selectByPrimaryKey(topicGoods.getActId());
				String waitTime = TopicUtils.getEndTime(topic.getEndTime());
				System.out.println("waitTime::::"+waitTime);
				map.put("waitTime", waitTime);//还差多久结束
				
				map.put("orderNo", log.getOrderNo());//订单号
				map.put("orderId", log.getOrderId());//订单id
				int isPrize=log.getIsPrize();//是否中奖
				map.put("isPrize",isPrize);
				if(isPrize==0){
					map.put("msg",waitTime+"后开奖");
				}else if(isPrize==1){
					map.put("msg","好可惜！差点就中了！");
				}else if(isPrize==2){
					map.put("msg","恭喜您！您已中奖！");
				}else if(isPrize==3){
					map.put("msg","人数不够，未开奖！");
				}
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
	 * @Description: 开奖接口：抽奖活动随机取一个抽奖号码,并更改抽奖初始化状态0（ 是否中奖 :0 初始化  1 否  2 是 3活动取消）
	 * @author xieyc
	 * @date 2018年1月18日 下午5:00:17  
	 */
	public Integer beganPrize(TopicPrizeLog entity) {
		Integer falg=null;//操作失败
		List<TopicPrizeLog> listTopicPrizeLog = topicPrizeLogMapper.listPage(entity);//只需传入tgId，其他为字段空
		TopicGoods topicGoods=topicGoodsMapper.selectByPrimaryKey(entity.getTgId());
		Goods goods = goodsMapper.selectByPrimaryKey(topicGoods.getGoodsId());
		int topicNum=goods.getTeamNum();//活动人数
		ArrayList<String> listPrizeNo = new ArrayList<String>();
		if (listTopicPrizeLog != null && listTopicPrizeLog.size() > 0) {
			if(topicNum>listTopicPrizeLog.size()){//参加人数少于配置人数不开将
				falg=0;//不开奖
				for (TopicPrizeLog topicPrizeLog : listTopicPrizeLog) {
					topicPrizeLog.setIsPrize(Contants.CANCEL_PRIZE);//3 活动取消
					topicPrizeLogMapper.updateByPrimaryKeySelective(topicPrizeLog);//更改抽奖记录状态为活动取消
					
					OrderTeam orderTeam=orderTeamMapper.getByOrderNo(topicPrizeLog.getOrderNo());
					orderTeam.setStatus(-1);//拼单失败
					orderTeamMapper.updateByPrimaryKey(orderTeam);//更改拼团状态为失败
					
					PayServiceImpl prizeRefund=new PayServiceImpl();
					prizeRefund.refund(topicPrizeLog.getOrderNo());//退款（金额原路返回）
					
				}
			}else if(topicNum==listTopicPrizeLog.size()){//开奖
				falg=1;//开奖
				for (TopicPrizeLog topicPrizeLog : listTopicPrizeLog) {
					listPrizeNo.add(topicPrizeLog.getPrizeNo());//将该活动的抽奖号码添加到list集合中
				}
				int index = (int) (Math.random() * listPrizeNo.size());// 随机取一个下标
				String luckyPrizeNo = listPrizeNo.get(index);// 本次活动中奖号码

				for (TopicPrizeLog topicPrizeLog : listTopicPrizeLog) {
					if (luckyPrizeNo.equals(topicPrizeLog.getPrizeNo())) {
						topicPrizeLog.setIsPrize(Contants.WIN_PRIZE);// 2中奖
					} else {
						this.depositWalle1(topicPrizeLog);//没中奖的将钱返回至钱包
						topicPrizeLog.setIsPrize(Contants.NOT_WIN_PRIZE); //1没有中奖
					}
					topicPrizeLogMapper.updateByPrimaryKeySelective(topicPrizeLog);
				}
			}
		}
		return falg;
	}	
	/**
	 * @Description: 抽奖活动将没中奖的人的钱返回至个人钱包
	 * @author xieyc
	 * @date 2018年1月18日 上午10:46:40
	 */
	public void depositWalle1(TopicPrizeLog topicPrizeLog) {
		int row = 0;
		//参加活动时都是单个商品支付，所以取第一个值
		List<OrderSku> orderSkuList =orderSkuMapper.getOrderSkuByOrderId(topicPrizeLog.getOrderId());
		Integer topicMoney=orderSkuList.get(0).getSkuSellPriceReal()/100;//活动价格
		// 查询某用户下所有钱包类型
		List<Wallet> listWallet = walletMapper.getWalletByUid(topicPrizeLog.getmId());
		if (listWallet != null && listWallet.size() > 0) {
			for (Wallet wallet : listWallet) {
				if (wallet.getType().equals("1")) {
					Integer monney = wallet.getMoney();// 原来该钱包的金额（单位分）
					wallet.setMoney(topicMoney+ monney);// 现在金额（单位分）
					wallet.setDes("新增抽奖活动中没中奖退款金额" + topicMoney  + "元");// 描述
					// wallet.setWithdrawCash(Contants.NOT_WITHDRAW);//不可以提现
					row = walletMapper.updateByPrimaryKeySelective(wallet);// 更新钱包
					break;
				}
			}
		}
		if (row == 0) {
			Wallet wallet = new Wallet();
			wallet.setUid(topicPrizeLog.getmId());// 用户id
			wallet.setWithdrawCash(Contants.NOT_WITHDRAW);// 不可以提现
			wallet.setType("1");// 设置类型
			wallet.setName("抽奖活动退款钱包");//钱包名称
			wallet.setPayPassword(null);// 支付密码
			wallet.setMoney(topicMoney);//设置金额
			wallet.setSalt(TopicUtils.getRandomString(32));// 盐值
			wallet.setDes("新增抽奖活动中没中奖退款金额" + topicMoney + "元");// 描述
			walletMapper.insertSelective(wallet);// 插入
		}
	}
}
