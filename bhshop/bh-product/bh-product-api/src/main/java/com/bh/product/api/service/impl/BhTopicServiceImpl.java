package com.bh.product.api.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MyBeanPojo;
import com.bh.goods.pojo.OneWeek;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.UserAttendance;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.BhTopicService;
import com.bh.product.api.service.QianDaoService;
import com.bh.product.api.util.TopicUtils;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.ScoreRuleExtMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.MyTime;
import com.bh.user.pojo.ScoreRuleExt;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;

@Service
public class BhTopicServiceImpl implements BhTopicService {
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;

	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;
	@Autowired
	private QianDaoService qianDaoService;

	public PageBean<TopicGoods> selectGoodsListByBhBean(TopicGoods entity) throws Exception {

		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.SIZE, true);
		List<TopicGoods> list = topicGoodsMapper.selectGoodsByParams(entity);
		if (list.size() > 0) {
			for (TopicGoods t : list) {
				Goods goods = goodsMapper.selectByPrimaryKey(t.getGoodsId());
				if (goods != null) {
					List<GoodsSku> goodsSku = goodsSkuMapper.selectListByGoodsIdAndStatus(goods.getId());
					GoodsSku goodsSku2 = goodsSku.get(0);
					double realPrice = (double) goodsSku2.getTeamPrice() / 100;
					double marketRealPrice = (double) goodsSku2.getMarketPrice() / 100;
					t.setRealPrice(realPrice);
					t.setPrice(marketRealPrice);
					t.setGoodsName(goods.getName());
					Object value = JsonUtils.stringToObject(goodsSku2.getValue());
					t.setValueObj(value);
					t.setSid(goodsSku2.getId());
					org.json.JSONArray personList = null;
					org.json.JSONObject jsonObj = null;
					String url = null;
					jsonObj = new org.json.JSONObject(goodsSku2.getValue()); // 获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					for (int m = 0; m < personList.length(); m++) {
						url = (String) personList.get(0);

					}
					t.setGoodsImage(url);
					// 金额的半价(分)
					Integer num = Math.round(goodsSku2.getTeamPrice() / 2);
					int num1 = (int) (num * Contants.saveMoney * 100);
					t.setNum(num1);
				}
			}
		}
		String time = topicMapper.selectStartTime();
		System.out.println("time-->" + time);
		PageBean<TopicGoods> pageBean = new PageBean<>(list);
		return pageBean;
	}

	public String waiTime() throws Exception {
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String starttime = topicMapper.selectStartTime();
		String endtime = topicMapper.selectendtime();
		if ((starttime != null) && (endtime != null)) {
			return RegExpValidatorUtils.getEndTime(formatter.parse(starttime), formatter.parse(endtime));
		}
		return null;
	}

	public List<Topic> selectTopicImage() {
		List<Topic> list = new ArrayList<>();
		list = topicMapper.selectImage();
		return list;
	}

	// 查询所有的可用滨惠豆抵消的商品
	public PageBean<GoodsSku> selectDouGoods(GoodsSku goodsSku) throws Exception {
		PageHelper.startPage(Integer.parseInt(goodsSku.getCurrentPage()), Contants.SIZE, true);
		List<GoodsSku> list = new ArrayList<>();
		list = goodsSkuMapper.selectDouGoods(goodsSku);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Goods goods = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
				double realPrice = (double) list.get(i).getTeamPrice() / 100;
				double marketRealPrice = (double) list.get(i).getMarketPrice() / 100;
				list.get(i).setRealPrice(realPrice);// 商品的价格
				list.get(i).setMarketRealPrice(marketRealPrice);// 商品的销售价格
				list.get(i).setGoodsName(goods.getName());// 商品的名称
				Object value = JsonUtils.stringToObject(list.get(i).getValue());
				list.get(i).setValueObj(value);// 商品的规格属性

				org.json.JSONArray personList = null;
				org.json.JSONObject jsonObj = null;
				String url = null;
				jsonObj = new org.json.JSONObject(list.get(i).getValue()); // 获取sku商品信息
				personList = jsonObj.getJSONArray("url");
				for (int m = 0; m < personList.length(); m++) {
					url = (String) personList.get(0);
				}
				int groupCount = 0; //团购商品销量统计
				String countT = orderSkuMapper.getGoodsGroupSale(goods.getId());
				if(countT!=null){
					groupCount = Integer.parseInt(countT)+goods.getFixedSale();
				}else{
					groupCount = goods.getFixedSale();
				}
				list.get(i).setSale(groupCount);
				list.get(i).setImage(url);// 商品的图片

				TopicGoods topicGoods = new TopicGoods();
				topicGoods.setGoodsId(list.get(i).getGoodsId());
				List<TopicGoods> topicGoodsList = topicGoodsMapper.selectGoodsByParams(topicGoods);
				// 如果是滨惠豆，价格低扣半价
				if (topicGoodsList.size() > 0) {
					// 滨惠豆可以抵扣？%
					list.get(i).setDiscounted(50);
				} else {
					// 如果不是超级滨惠豆，抵扣的钱=goodsSku表的score字段(分)
					// ，滨惠豆可抵扣=score/list.get(i).getSellPrice*100
					if (list.get(i).getScore() > 0) {
						double score = (double) list.get(i).getScore()  / list.get(i).getTeamPrice() * 100;
						//System.out.println("score-->" + score);
						list.get(i).setDiscounted(RegExpValidatorUtils.formatdouble(score));
					} else {
						list.get(i).setDiscounted(0);
					}
				}
			}
		}

		PageBean<GoodsSku> pageBean = new PageBean<>(list);
		return pageBean;
	}

	// 是否签到：1，已签到，0为签到
	public UserAttendance isAttendances(MemerScoreLog log) throws Exception {
		List<MemberUser> memberUser = memberUserMapper.selectUserPoint(log.getmId());
		UserAttendance attendance = new UserAttendance();
		if (memberUser.size()>0) {
			attendance.setPoint(memberUser.get(0).getPoint());
		}else{
			attendance.setPoint(0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 用户签到记录
		List<MemerScoreLog> list=new ArrayList<>();
		list = memerScoreLogMapper.selectLogByUserattends(log);
		log.setCreateTime(new Date());
	//	List<OneWeek> oneWeeks = selectOneWeek(log);
		List<OneWeek> oneWeeks=qianDaoService.selectOneWeek(log);
		attendance.setOneWeek(oneWeeks);
		// 没有记录
		if (list.size() < 1) {
			// 0未签到,1已签到
			attendance.setIsAttendance(0);
			// 连续签到的次数
			attendance.setTimes(0);
		} else {
			// 如果已经签到过，判断是今天签到的，如果是则提示"已经签到了"
			MemerScoreLog log2 = list.get(0);
			String d1 = sdf.format(new Date());// 第一个时间
			String d2 = sdf.format(log2.getCreateTime());// 第二个时间
			boolean flage = false;
			flage = d1.equals(d2);
			if (flage) {// 已签到
				attendance.setIsAttendance(1);
			} else {
				attendance.setIsAttendance(0);
			}
			attendance.setTimes(list.get(0).getTimes());

		}
		return attendance;
	}

	// 签到的接口
	public int attendances(MemerScoreLog log) throws Exception {
		List<MemberUser> memberUser = memberUserMapper.selectUserPoint(log.getmId());
		int score =0;
		if (memberUser.size()>0) {
			score= memberUser.get(0).getPoint();
		}
		
		// 获取签到规则
		SeedScoreRule se = new SeedScoreRule();
		se.setScoreAction(1);
		List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
		if (rule.size() < 1) {
			try {
				insertRule(rule);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		boolean isGetScore = false;
		boolean isUpdateScore = false;
		int a = 0;
		if (rule.size() > 0) {
			// 获取status的开关设置:状态 0关 1开
			if (rule.get(0).getStatus().equals(1)) {
				// 如果是连续签到：times = times+1
				List<ScoreRuleExt> exts = selectScoreRuleExt(1);
				if (exts.size() > 0) {
					a = exts.get(0).getExtValue();
					log.setScore(a);
				}else{
					a = rule.get(0).getScore();
				}
				isGetScore = true;
				isUpdateScore = true;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<MemerScoreLog> list = new ArrayList<>();
		int row = 0;
		list = memerScoreLogMapper.selectLogByUserattends(log);
		log.setCreateTime(new Date());
		// 如果是第一次签到：
		if (list.size() < 1) {// 如果原来表中该用户没有数据
			MemerScoreLog scoreLog = new MemerScoreLog();
			scoreLog.setCreateTime(new Date());
			scoreLog.setmId(log.getmId());
			scoreLog.setSmId(0);
			scoreLog.setSsrId(1);
			scoreLog.setTimes(1);// 总共累计一次签到
			scoreLog.setScore(a);
			row = memerScoreLogMapper.insertSelective(scoreLog);
		} else {

			MemerScoreLog log2 = list.get(0);
			String d1 = sdf.format(new Date());// 第一个时间
			String d2 = sdf.format(log2.getCreateTime());// 第二个时间
			// System.out.println( "判断是否是今天" +d1.equals(d2));// 判断是否是同一天
			boolean flage = false;
			flage = d1.equals(d2);
			// 如果flag=true,判断如果是今天签到的，则提示“已经签到了”则今天已经签到过了
			if (flage) {
				row = 2;
			} else {
				log.setScore(a);
				// 如果今天与上一次签到相差1天，则为连续签到
				int leiji = RegExpValidatorUtils.daysBetween(log2.getCreateTime(), new Date());
				if (leiji == 1) {
					log.setTimes(log2.getTimes() + 1);
					if (isGetScore) {
						// 如果是连续签到：times = times+1
						List<ScoreRuleExt> exts = selectScoreRuleExt(log2.getTimes() + 1);
						if (exts.size() > 0) {
							a = exts.get(0).getExtValue();
							log.setScore(a);
						}
					}
				} else if (leiji > 1) {
					// 如果断开了,则需要重新签到:times=1
					log.setTimes(1);
				}
				log.setSmId(0);
				row = memerScoreLogMapper.insertSelective(log);
			}
		}
		if (isUpdateScore) {
			score = score + a;
			MemberUser memberUser2 = new MemberUser();
			memberUser2.setmId(log.getmId());
			memberUser2.setPoint(score);
			memberUserMapper.updatePointBymId(memberUser2);
		}
		return row;
	}

	public List<OneWeek> selectOneWeek(MemerScoreLog log) {
		List<OneWeek> oneWeek = new ArrayList<>();
		// 定义输出日期格式.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		// 获取签到规则
		SeedScoreRule se = new SeedScoreRule();
		se.setScoreAction(1);
		List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
		try {
			if (rule.size()<1) {
				insertRule(rule);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean isGetScore = false;
		int a = 0;
		if (rule.size() > 0) {
			// 获取status的开关设置:状态 0关 1开
			if (rule.get(0).getStatus().equals(1)) {
				a = rule.get(0).getScore();
				isGetScore = true;
			}
		}
		
		/*********xieyc  modifyStart************/
		Date changeDay=null;
		//查询某个用户当前日期前4天的签到记录
		for(int i=1;i<=4;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - i);  
		    changeDay = calendar.getTime();  //当前日期的前i天
		    System.out.println(changeDay);
		    int count=memerScoreLogMapper.getLogByMidAndTime(log.getmId(),changeDay);
			if(count==0){//为0代表该天无签到记录
				break;
			}
		}
		List<String> days = TopicUtils.dateToWeekByDay(changeDay);//获取某一天的后7天时间
		/*********xieyc  modifyEnd************/
	
		for (int i = 0; i < days.size(); i++) {
			// 2017-1-23的形式
			// 一周共7天
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			OneWeek oneWeek2 = new OneWeek();

			// 遍历一周的时间
			StringBuffer sb = new StringBuffer();
			String[] a1 = days.get(i).split("-");
			sb.append(a1[1].toString()).append(".").append(a1[2].toString());
			oneWeek2.setDay(sb.toString());
			
			// 现在时间
			Date d = new Date();
			String date = sdf.format(d);
			String[] strs = date.split("-");
			StringBuffer sb1 = new StringBuffer();
			sb1.append(strs[1].toString()).append(".").append(strs[2].toString());
			String now = sb1.toString();


			// 如果签到设为开的时候
			if (isGetScore) {
				MemerScoreLog memerScoreLog = new MemerScoreLog();
				memerScoreLog.setmId(log.getmId());
				try {
					System.out.println("day.get(i)-->" + days.get(i));
					Date da = null;
					da = sdf2.parse(days.get(i));
					memerScoreLog.setParam(sdf2.format(da));

					List<MemerScoreLog> logList = memerScoreLogMapper.selectLogByUserattends(memerScoreLog);
					if (logList.size() > 0) {
						oneWeek2.setScore(logList.get(0).getScore());
						// 是否签到0未签到，1已签到
						oneWeek2.setIsAttence(1);
					} else {
						//设置签到状态：未签到
						oneWeek2.setIsAttence(0);
						MemerScoreLog log2 = new MemerScoreLog();
						log2.setmId(log.getmId());
						//查询用户积分记录
						List<MemerScoreLog> nextAtt = memerScoreLogMapper.selectLogByUserattends(log2);
						if (nextAtt.size() > 0) {
							//如果有的话则算出最后一次签到时间与对应的时间差
							int xiangcha = RegExpValidatorUtils.daysBetween(nextAtt.get(0).getCreateTime(), sdf2.parse(days.get(i)));
							//如果相差的是整数，则证明是将来的时间
							if (xiangcha > 0) {
								List<ScoreRuleExt> extList = selectScoreRuleExt(nextAtt.get(0).getTimes() + xiangcha);
								if (extList.size() > 0) {
									if(extList.size()-i-1<0){
										oneWeek2.setScore(extList.get(0).getExtValue());
									}else{
										oneWeek2.setScore(extList.get(extList.size()-i-1).getExtValue());
									}
								}else{
									ScoreRuleExt record = new ScoreRuleExt();
									record.setSrId(1);
									List<ScoreRuleExt> ext = scoreRuleExtMapper.selectScoreRuleExtBysrIdAndKey(record);
									if (ext.size() > 0) {
										//是否N天连续以上0否，1是
										Integer isSeri = ext.get(0).getIsSeries();
										if (isSeri == 1) {
											oneWeek2.setScore(ext.get(0).getExtValue());
										}else{
											oneWeek2.setScore(a);
										}
									}else{
										oneWeek2.setScore(a);
									}
								}
							}else if (xiangcha < 0) {
								//如果是负数，则是过去的时间
								/*List<ScoreRuleExt> extList = selectScoreRuleExt(nextAtt.get(0).getTimes()+1);
								if (extList.size() > 0) {
									oneWeek2.setScore(extList.get(0).getExtValue());
								}*/
								oneWeek2.setScore(0);
							}
						}else{
							//2018-3-27添加该代码，原因是如果nextAtt为空，score积分也就为空了
							List<ScoreRuleExt> extList = selectScoreRuleExt(i+1);
							if (extList.size() > 0) {
								oneWeek2.setScore(extList.get(0).getExtValue());
							}else{
								ScoreRuleExt record = new ScoreRuleExt();
								record.setSrId(1);
								List<ScoreRuleExt> ext = scoreRuleExtMapper.selectScoreRuleExtBysrIdAndKey(record);
								if (ext.size() > 0) {
									//是否N天连续以上0否，1是
									Integer isSeri = ext.get(0).getIsSeries();
									if (isSeri == 1) {
										oneWeek2.setScore(ext.get(0).getExtValue());
									}else{
										oneWeek2.setScore(a);
									}
								}else{
									oneWeek2.setScore(a);
								}
							}
							//结束
						}
						
						
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// 如果签到设为关的时候
				oneWeek2.setScore(0);
			}

			
			
			oneWeek.add(oneWeek2);
		}

		return oneWeek;
	}

	public List<ScoreRuleExt> selectScoreRuleExt(int day) {
		ScoreRuleExt record = new ScoreRuleExt();
		record.setSrId(1);
		record.setExtKey(day);
		List<ScoreRuleExt> extList = scoreRuleExtMapper.selectScoreRuleExtBysrIdAndKey(record);
		return extList;
	}
	
	public void insertRule(List<SeedScoreRule> list) {
		if (list.size() < 1) {
			for(int i=1;i<=7;i++){
				SeedScoreRule rule3 = new SeedScoreRule();
				rule3.setId(i);
				rule3.setScoreAction(i);
				rule3.setScore(0);
				rule3.setStatus(0);
				switch (rule3.getId()) {
				case 1:
					rule3.setName("签到积分");
					ScoreRuleExt ext = new ScoreRuleExt();
					ext.setSrId(1);
					ext.setExtKey(1);
					ext.setExtValue(1);
					ext.setIsDel(0);
					ext.setIsSeries(0);
					scoreRuleExtMapper.insertSelective(ext);
					break;
				case 2:
					rule3.setName("浇水");
					break;
				case 3:
					rule3.setName("拼单");
				    break;
				case 4:
					rule3.setName("单买");
					break;
				case 5:
					rule3.setName("分享积分");
					ScoreRuleExt ext1 = new ScoreRuleExt();
					ext1.setSrId(5);
					ext1.setExtKey(1);
					ext1.setExtValue(1);
					ext1.setIsDel(0);
					ext1.setIsSeries(0);
					scoreRuleExtMapper.insertSelective(ext1);
					break;
				case 6:
					rule3.setName("注册积分");
					break;
				case 7:
					rule3.setName("购物积分");
					break;
				default:
					rule3.setName("");
					break;
				}
				SeedScoreRule r = seedScoreRuleMapper.selectByPrimaryKey(i);
				if (r == null) {
					seedScoreRuleMapper.insertSelective(rule3);
				}else {
					seedScoreRuleMapper.updateByPrimaryKeySelective(rule3);
				}
			}
		}
	}
	
	
	
	//滨惠豆明细列表
	public Map<String, Object> selectBeanList(Integer mId,String currentPage,String pageSize,String log,String time)throws Exception{
		List<MyBeanPojo> myBeanPojoList=new ArrayList<>();
		Integer size=Integer.parseInt(pageSize);//每页的数量
		Integer cuPage=(Integer.parseInt(currentPage)-1)*size;
		List<MemerScoreLog> logList=memerScoreLogMapper.selectBeanList(mId,cuPage,size,log,time);
		if (logList.size()>0) {
			//购物的赠送积分的图片(确认收货的时候才有) 钱的图片
			String zeng_image="goods/3917755D06E84484ABAD68A3E4EB5036.png";
			String zeng_imagePro="goods/68395860-e4dd-445b-ae25-2c6592f8df3e.png";
			String zeng_myimage="";
			//下单的抵积分的图片 购的图片
			String go_image="goods/58CDB1ECE6C6495292140409CAA8B913.png";
			String go_imagePro="goods/b2d93bcb-eca9-4956-ba5f-c4cd110b5ed7.png";
			String go_myimage="";
			//用户签到的积分图片 √的图片
			String qi_image="goods/AE5AA91BF07446C1B073531CAB68A1D8.png";
			String qi_imagePro="goods/dede0f18-757f-4bb2-8f86-fd342275fa0f.png";
			String qi_myimage="";
			
			boolean flag=Contants.isPro;
			if (flag) {
				go_myimage=Contants.bucketHttps+go_imagePro;
				qi_myimage=Contants.bucketHttps+qi_imagePro;
				zeng_myimage=Contants.bucketHttps+zeng_imagePro;
			}else{
				go_myimage=Contants.bucketHttps+go_image;
				qi_myimage=Contants.bucketHttps+qi_image;
				zeng_myimage=Contants.bucketHttps+zeng_image;
			}
			
			for (MemerScoreLog memerScoreLog : logList) {
				MyBeanPojo myBeanPojo=new MyBeanPojo();
				myBeanPojo.setId(memerScoreLog.getId());
				myBeanPojo.setScore(memerScoreLog.getScore()+"");
				//0代表签到积分,-1代表抵扣积分,-2代表注册积分,-3代表分享积分,-4代表购物积分(确认收货送的积分),-5代表取消订单返回的滨惠豆
				Integer smId=memerScoreLog.getSmId();
				switch (smId) {
				case 0:
					myBeanPojo.setMsg("签到领滨惠豆");
					myBeanPojo.setImage(qi_myimage);
					myBeanPojo.setFuhao("+");
					break;
				case -1:
					myBeanPojo.setMsg("滨惠豆抵钱");
					myBeanPojo.setImage(go_myimage);
					myBeanPojo.setFuhao("-");
					OrderSku orderSku=orderSkuMapper.selectOrderSkuMsg(memerScoreLog.getOrderseedId());
					if (orderSku!=null) {
						myBeanPojo.setImage(orderSku.getSkuImage());
						myBeanPojo.setOrderNo(orderSku.getSkuNo());
					}
				
					break;
				case -2:
					myBeanPojo.setMsg("新用户注册赠送滨惠豆");
					myBeanPojo.setImage(zeng_myimage);
					myBeanPojo.setFuhao("+");
					break;
				case -3:
					myBeanPojo.setMsg("拼单分享赠送滨惠豆");
					myBeanPojo.setImage(zeng_myimage);
					myBeanPojo.setFuhao("+");
					break;
				case -4:
					myBeanPojo.setMsg("滨惠豆宝盒-平台购物赠送滨惠豆");
					myBeanPojo.setImage(zeng_myimage);
					myBeanPojo.setFuhao("+");
					break;
				case -5:
					myBeanPojo.setMsg("取消订单返滨惠豆 ");
					myBeanPojo.setImage(go_myimage);
					myBeanPojo.setFuhao("+");
					OrderSku orderSku1=orderSkuMapper.selectOrderSkuMsg(memerScoreLog.getOrderseedId());
					if (orderSku1!=null) {
						myBeanPojo.setImage(orderSku1.getSkuImage());
						myBeanPojo.setOrderNo(orderSku1.getSkuNo());
					}
					break;
				default:
					break;
				}
				
				//时间的改变
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				String t = sdf.format(memerScoreLog.getCreateTime());// 第一个时间
				myBeanPojo.setTime(t);
				myBeanPojoList.add(myBeanPojo);
			}
		}
		Map<String, Object> myMap=new HashMap<>();
		myMap.put("list", myBeanPojoList);
		return myMap;
	}
	
	public List<MyTime> searchTime()throws Exception{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format =new SimpleDateFormat("yyyy年M月");
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        
         
        //过去三个月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -2);
        Date m2 = c.getTime();
        String mon2 = format.format(m2);
        List<MyTime> myTimeList=new ArrayList<>();
        for(int i=0;i<=3;i++){
        	MyTime myTime=new MyTime();
        	if (i==0) {
        		myTime.setId(3);
        		myTime.setMsg("近3个月");
			}else if (i==1) {
				myTime.setId(0);
        		myTime.setMsg("本月");
			}else if (i==2) {
				myTime.setId(1);
        		myTime.setMsg(mon);
			}else if (i==3) {
				myTime.setId(2);
        		myTime.setMsg(mon2);
			}
        	myTimeList.add(myTime);
        }
		return myTimeList;
	}
}
