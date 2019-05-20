package com.bh.admin.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.order.OrderSeedMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberSeedMapper;
import com.bh.admin.mapper.user.MemerScoreLogMapper;
import com.bh.admin.mapper.user.SeedModelMapper;
import com.bh.admin.pojo.order.OrderSeed;
import com.bh.admin.pojo.user.BaseGameMsg;
import com.bh.admin.pojo.user.LandList;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberSeed;
import com.bh.admin.pojo.user.MemerScoreLog;
import com.bh.admin.pojo.user.SeedGame;
import com.bh.admin.pojo.user.SeedModel;
import com.bh.admin.pojo.user.SeedParam;
import com.bh.admin.pojo.user.SimpleLand;
import com.bh.admin.pojo.user.SimpleSeed;
import com.bh.admin.pojo.user.StoreHouse;
import com.bh.admin.service.SeedGameService;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import com.bh.utils.RegExpValidatorUtils;


@Service
public class SeedGameServiceImpl implements SeedGameService{
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberSeedMapper memberSeedMapper;
	@Autowired
	private SeedModelMapper seedModelMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	@Autowired
	private OrderSeedMapper orderSeedMapper;
	
	public Member selectMember(Integer mId) throws Exception{
		return memberMapper.selectByPrimaryKey(mId);
	}
	
	
	public SeedGame selectMemberMsg(Member member) throws Exception{
		SeedGame seedGame = new SeedGame();
		seedGame.setmId(member.getId());
		//获得游戏的基本信息
		BaseGameMsg baseGameMsg = selectBaseGameMsg(member);
		seedGame.setBaseGameMsg(baseGameMsg);
		//获得土地
		LandList landList = selectLandList(member);
		seedGame.setLandList(landList);
		//获得植物列表
		List<SimpleSeed> simpleSeedList = selectListSimpleSeed(member);
		seedGame.setSeedList(simpleSeedList);
		return seedGame;
	}
	
	//获得土地
	public LandList selectLandList(Member member){
		LandList landList = new LandList();
		List<SimpleLand> usable = new ArrayList<>();
		List<SimpleLand> Unavailable = new ArrayList<>();
		//可用的土地
		SeedModel seedModel = new SeedModel();
		seedModel.setType(1);
		seedModel.setStatus(2);
		List<SeedModel> seedModels = seedModelMapper.selectUserLand(seedModel);
		if (seedModels.size()>0) {
			for (SeedModel seedModel2 : seedModels) {
				SimpleLand simpleLand = new SimpleLand();
				simpleLand.setId(seedModel2.getId());
				simpleLand.setIsFree(1);
				simpleLand.setLandImage(seedModel2.getImage());
				simpleLand.setLandName(seedModel2.getSeedName());
				simpleLand.setRand(seedModel2.getRand());
				usable.add(simpleLand);
			}
		}
		List<MemberSeed> memberSeedLand = memberSeedMapper.selectUserLand(member.getId());
		if (memberSeedLand.size()>0) {
			for (MemberSeed memberSeed : memberSeedLand) {
				SeedModel seedModel2 = seedModelMapper.selectByPrimaryKey(memberSeed.getSmId());
				SimpleLand simpleLand = new SimpleLand();
				simpleLand.setId(memberSeed.getId());
				simpleLand.setIsFree(1);
				simpleLand.setLandImage(seedModel2.getImage());
				simpleLand.setLandName(seedModel2.getSeedName());
				simpleLand.setRand(seedModel2.getRand());
				usable.add(simpleLand);
			}
		}
		
		//不可用得土地
		SeedModel seedModel1 = new SeedModel();
		seedModel1.setType(1);
		seedModel1.setStatus(2);
		List<SeedModel> seedModels2 = seedModelMapper.selectUnuserLand(seedModel1);
		if (seedModels.size()>0) {
			for (SeedModel seedModel2 : seedModels2) {
				SimpleLand simpleLand = new SimpleLand();
				simpleLand.setId(seedModel2.getId());
				simpleLand.setIsFree(0);
				simpleLand.setLandImage(seedModel2.getImage());
				simpleLand.setLandName(seedModel2.getSeedName());
				simpleLand.setRand(seedModel2.getRand());
				Unavailable.add(simpleLand);
			}
		}
		landList.setUsable(usable);
		landList.setUnavailable(Unavailable);
		return landList;
	}
	
	//获得基本的游戏信息
	public BaseGameMsg selectBaseGameMsg(Member member) {
		BaseGameMsg baseGameMsg = new BaseGameMsg();
		Member member2 = memberMapper.selectByPrimaryKey(member.getId());
		//头像
		baseGameMsg.setHeadimgurl(member2.getHeadimgurl());
		//用户名
		baseGameMsg.setUsername(member2.getUsername());
		//用户的id
		baseGameMsg.setmId(member2.getId());
		
		//用户的金币(初始化为500)
		/*int gold = 0;
		gold =	memberSeedMapper.selectScore(member.getId());
		if (StringUtils.isEmpty(gold+"")) {
			gold=Contants.GOLD;
		}*/
		//金币
		baseGameMsg.setGold(0);
		int exp = memberSeedMapper.selectScore(member.getId());
		//用户的经验
		int e = exp/Contants.RANK_NUM;
		//用户的当前等级
		baseGameMsg.setCurrentRank(e+1);
		//用户的经验
		baseGameMsg.setExperience(exp-((baseGameMsg.getCurrentRank()-1)*Contants.RANK_NUM));
		//当前等级需要的经验，计算方式如下:
		//（N+1）*（200点）
		baseGameMsg.setCurrentRankExexperience((baseGameMsg.getCurrentRank())*Contants.RANK_NUM);
		return baseGameMsg;
	}
	
	public List<SimpleSeed> selectListSimpleSeed(Member member) {
		//返回种子列表(多个集合)
		List<SimpleSeed> simpleSeedList = new ArrayList<>();
		try {
			MemberSeed memberSeed = new MemberSeed();
			memberSeed.setmId(member.getId());
			memberSeed.setType(0);
			List<MemberSeed> list = memberSeedMapper.selectMemberSeedByParams(memberSeed);
		    simpleSeedList = selectSimpleSeed(list);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return simpleSeedList;
	}
	
	//收割
	public BhResult shouge(SeedParam param) throws Exception{
		BhResult bhResult = null;
		List<String> ids = JsonUtils.stringToList(param.getId());
		for (String string : ids) {
			List<MemberSeed> memberSeedList = new ArrayList<>();
			MemberSeed mSeed = memberSeedMapper.selectByPrimaryKey(Integer.parseInt(string));
			memberSeedList.add(mSeed);
			List<SimpleSeed> list = selectSimpleSeed(memberSeedList);
			if (list.size()>0) {
				SimpleSeed simpleSeed = list.get(0);
				if (simpleSeed.getCurrentJieDuan() == 5) {
					MemberSeed ret = memberSeedMapper.selectByPrimaryKey(Integer.parseInt(string));
					SeedModel seedModel = seedModelMapper.selectByPrimaryKey(ret.getSmId());
					MemberSeed memberSeed = new MemberSeed();
					memberSeed.setId(ret.getId());
					memberSeed.setStatus(6);
					memberSeed.setMytimes(ret.getMytimes()+seedModel.getExperienceEverySeason());
					memberSeed.setGetTime(ret.getGetTime());
					memberSeedMapper.updateByPrimaryKeySelective(memberSeed);
					
					//更新orderSeed表的状态
					OrderSeed orderSeed = new OrderSeed();
					orderSeed.setId(ret.getOrderseedId());
					orderSeed.setStatus(3);
					orderSeedMapper.updateByPrimaryKeySelective(orderSeed);
					bhResult = new BhResult(200, "收割成功", seedModel.getExperienceEverySeason());
				}else{
					bhResult = new BhResult(400, list.get(0).getCurrentJieduanName(), null);
				}
			}
		}
		return bhResult;
	}
	
	
	
	//仓库列表
	public BhResult storeHouseList(SeedParam param) throws Exception{
		BhResult bhResult = null;
		List<StoreHouse> list = new ArrayList<>();
		MemberSeed memberSeed = new MemberSeed();
		memberSeed.setmId(param.getmId());
		List<MemberSeed> lMemberSeeds = memberSeedMapper.selectStoreHouseList(memberSeed);
		if (lMemberSeeds.size()>0) {
			for (MemberSeed memberSeed2 : lMemberSeeds) {
				int num = memberSeedMapper.countStoreNum(memberSeed2);
				SeedModel seedModel = seedModelMapper.selectByPrimaryKey(memberSeed2.getSmId());
				StoreHouse storeHouse = new StoreHouse();
				storeHouse.setId(memberSeed2.getId());
				storeHouse.setSeedName(seedModel.getSeedName());
				storeHouse.setImage(seedModel.getImage());
				storeHouse.setNum(num);
				double realSalePrice = (double) seedModel.getSalePrice()/100;
				double realBonus = (double) seedModel.getBonus()/100;
				storeHouse.setRealBonus(realBonus);
				storeHouse.setRealSalePrice(realSalePrice);
				storeHouse.setRand(seedModel.getRand());
				list.add(storeHouse);
			}
		}
		bhResult = new BhResult(200, "操作成功", list);
		return bhResult;
	}
	
	//是否需要浇水
	public int isNeedWater(Integer memberSeedId){
		int isNeed = 1;
		MemberSeed memberSeed = memberSeedMapper.selectByPrimaryKey(memberSeedId);
		MemerScoreLog log = new MemerScoreLog();
		log.setOrderseedId(memberSeed.getOrderseedId());
		log.setmId(memberSeed.getmId());
		List<MemerScoreLog> logList = memerScoreLogMapper.selectLogByParams(log);
		if (logList.size()>0) {
			Date oldTime = logList.get(0).getCreateTime();
			int between = RegExpValidatorUtils.hourBetween(new Date(), oldTime);
			//如果现在时间与上一次浇水的时间小于浇水的频率
			if (between <= Contants.WATERING_RATE) {
				isNeed = 0;
			}
		}
		return isNeed;
	}
	
	
	//给植物浇水的动作
	public BhResult waterAction(SeedParam param) throws Exception{
		BhResult bhResult = null;
		List<String> ids = JsonUtils.stringToList(param.getId());
		for (String string : ids) {
			int isNeed = isNeedWater(Integer.parseInt(string));
			if (isNeed == 0) {
				bhResult = new BhResult(400, "这块地不需要浇水啦", null);
			}else{
				
				MemberSeed memberSeed = memberSeedMapper.selectByPrimaryKey(Integer.parseInt(string));
				MemerScoreLog scoreLog = new MemerScoreLog();
				scoreLog.setCreateTime(new Date());
				scoreLog.setmId(memberSeed.getmId());
				scoreLog.setOrderseedId(memberSeed.getOrderseedId());
				scoreLog.setSmId(memberSeed.getSmId());
				scoreLog.setSsrId(2);
				scoreLog.setTimes(1);// 总共累计一次签到
				scoreLog.setScore(Contants.WATERING_EXP);
				memerScoreLogMapper.insertSelective(scoreLog);
				
				MemberSeed memberSeed2 = new MemberSeed();
				memberSeed2.setId(memberSeed.getId());
				memberSeed2.setMytimes(memberSeed.getMytimes()+Contants.WATERING_EXP);
				memberSeed2.setGetTime(memberSeed.getGetTime());
				memberSeedMapper.updateByPrimaryKeySelective(memberSeed2);
				bhResult = new BhResult(200, "浇水成功", Contants.WATERING_EXP);
			}
		}
		return bhResult;
	}
	
	public List<SimpleSeed> selectSimpleSeed(List<MemberSeed> list) {
		List<SimpleSeed> simpleSeedList = new ArrayList<>();
		if (list.size()>0) {
			for (MemberSeed memberSeed2 : list) {
				
				SeedModel seedModel3 = seedModelMapper.selectByPrimaryKey(memberSeed2.getSmId());
				//int between = RegExpValidatorUtils.daysBetween( memberSeed2.getGetTime(), new Date())+1;
				//两个时间相差多少个小时
				int between = RegExpValidatorUtils.hourBetween(new Date(),memberSeed2.getGetTime());
				Integer gainPeriod = seedModel3.getGainPeriod();
				String str = seedModel3.getSmallImage();
				if (str !=null) {
					//每个时间的阶段图
					List<String> strList = JsonUtils.stringToList(str);
					if (strList.size()>0) {
						double totalJieDuan = Double.parseDouble(strList.size()+"");
						double each = totalJieDuan/gainPeriod;
						int currentJieDuan = (int) Math.ceil(between*each);
						SimpleSeed simpleSeed = new SimpleSeed();
						if (currentJieDuan > strList.size()) {
							simpleSeed.setCurrentJieduanName("成熟结果期");
							simpleSeed.setCurrentJieDuan(strList.size());
							simpleSeed.setCurrentJisDuanImage(strList.get(strList.size()-1));
						}else if (currentJieDuan == 0) {
							simpleSeed.setCurrentJieduanName("发芽期");
							simpleSeed.setCurrentJieDuan(1);
							simpleSeed.setCurrentJisDuanImage(strList.get(0));
						}
						else{
							for(int i = 1;i<strList.size()+1;i++){
								if (i == currentJieDuan) {
									simpleSeed.setCurrentJieDuan(i);
									simpleSeed.setCurrentJisDuanImage(strList.get(i-1));
									switch (simpleSeed.getCurrentJieDuan()) {
									case 1:
										simpleSeed.setCurrentJieduanName("发芽期");
										break;
									case 2:
										simpleSeed.setCurrentJieduanName("小叶子期");
										break;
	                                case 3:
	                                	simpleSeed.setCurrentJieduanName("大叶子期");
										break;
									case 4:
										simpleSeed.setCurrentJieduanName("开花期");
										break;
	                                case 5:
	                                	simpleSeed.setCurrentJieduanName("成熟结果期");
										break;
									default:
										simpleSeed.setCurrentJieduanName("成熟结果期");
										break;
									}
								}
							}
						}
						simpleSeed.setId(memberSeed2.getId());
						simpleSeed.setTotalJieDuan(strList.size());
						int isNeed = isNeedWater(memberSeed2.getId());
						simpleSeed.setIsNeedWater(isNeed);
						simpleSeedList.add(simpleSeed);
					}
				}
			}

		}
		
		return simpleSeedList;
		
	}
	
}
