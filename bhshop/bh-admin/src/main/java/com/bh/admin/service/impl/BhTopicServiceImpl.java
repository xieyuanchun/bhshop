package com.bh.admin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.TopicGoodsMapper;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.mapper.user.MemerScoreLogMapper;
import com.bh.admin.mapper.user.ScoreRuleExtMapper;
import com.bh.admin.mapper.user.SeedScoreRuleMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.OneWeek;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.goods.UserAttendance;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemerScoreLog;
import com.bh.admin.pojo.user.ScoreRuleExt;
import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.admin.service.BhTopicService;
import com.bh.admin.util.TopicUtils;
import com.bh.config.Contants;
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

				int groupCount = 0; // 团购商品销量统计
				String countT = orderSkuMapper.getGoodsGroupSale(list.get(i).getGoodsId());
				if(countT!=null){
					groupCount = Integer.parseInt(countT);
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
						double score = (double) list.get(i).getScore() * 10 / list.get(i).getTeamPrice();
						System.out.println("score-->" + score);
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
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(log.getmId());
		UserAttendance attendance = new UserAttendance();
		attendance.setPoint(memberUser.getPoint());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 用户签到记录
		List<MemerScoreLog> list=new ArrayList<>();
		list = memerScoreLogMapper.selectLogByUserattends(log);
		log.setCreateTime(new Date());
		List<OneWeek> oneWeeks = selectOneWeek(log);
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
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(log.getmId());
		int score = memberUser.getPoint();
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

			
			if (sb.toString().equals(now)) {
				oneWeek2.setIsToday(1);
			} else {
				oneWeek2.setIsToday(0);
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
}
