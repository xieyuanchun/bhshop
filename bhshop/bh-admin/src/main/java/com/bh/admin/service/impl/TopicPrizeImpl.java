package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.goods.TopicPrizeLogMapper;
import com.bh.admin.mapper.goods.TopicTypeMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.WalletMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.admin.pojo.goods.TopicType;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.Wallet;
import com.bh.admin.service.TopicPrizeService;
import com.bh.admin.util.TopicUtils;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
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

	@Override
	public Map<String, Object> apiTopicGoodsList(TopicGoods entity, Member member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageBean<Map<String, Object>> apiMyTopicPrize(TopicGoods entity, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
}
