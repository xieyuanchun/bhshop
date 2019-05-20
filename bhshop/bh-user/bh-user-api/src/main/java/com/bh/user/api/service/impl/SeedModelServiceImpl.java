package com.bh.user.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.order.mapper.OrderSeedMapper;
import com.bh.order.pojo.OrderSeed;
import com.bh.result.BhResult;
import com.bh.user.api.service.SeedGameService;
import com.bh.user.api.service.SeedModelService;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.SeedModelMapper;
import com.bh.user.pojo.LandList;
import com.bh.user.pojo.MSeed;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedModel;
import com.bh.user.pojo.SimpleLand;
import com.bh.user.pojo.SimpleSeed;
import com.bh.utils.RegExpValidatorUtils;

@Service
public class SeedModelServiceImpl implements SeedModelService {
	@Autowired
	private SeedModelMapper seedModelMapper;

	@Autowired
	private MemberUserMapper memberUserMapper;

	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;

	@Autowired
	private OrderSeedMapper orderSeedMapper;
	
	@Autowired
	private SeedGameService seedGameService;

	// 已累计签到的天数接口
	public List<SeedModel> attendanceDays(MemberUser memberUser) throws Exception {
		List<SeedModel> list = new ArrayList<>();
		
		int days = 0;
		OrderSeed orderSeed = new OrderSeed();
		orderSeed.setmId(memberUser.getmId());
		// 通过用户查询已经支付的种子列表
		List<OrderSeed> seedList = orderSeedMapper.selecOrderSeedByParam(orderSeed);
		if (seedList.size() > 0) {
			for (OrderSeed orderSeed2 : seedList) {
				SeedModel seedModel = new SeedModel();
				// 如果用户已有签到记录
				MemerScoreLog log = new MemerScoreLog();
				log.setmId(memberUser.getmId());
				log.setSsrId(Contants.rule1);
				log.setSmId(orderSeed2.getSmId());
				log.setOrderseedId(orderSeed2.getId());
				List<MemerScoreLog> list1 = memerScoreLogMapper.selectLogByParams(log);
				SeedModel sModel  = seedModelMapper.selectByPrimaryKey(orderSeed2.getSmId());
				// 用户总共签到的天数(累计加起来的天数)
				if (list1.size() > 0) {
					days = list1.size();
				} else {
					days = 0;
				}
				seedModel.setScore(days);// 累计签到的天数
				double realBonus = (double) sModel.getBonus() / 100;
				double realSalePrice = (double) sModel.getSalePrice() / 100;
				seedModel.setBonus(sModel.getBonus());// 分为单位(可领取的金额）
				seedModel.setSalePrice(sModel.getSalePrice());// 分为单位(购买种子的价格)
				seedModel.setSeedName(sModel.getSeedName());// 种子名称
				seedModel.setId(sModel.getId());
				seedModel.setGainPeriod(sModel.getGainPeriod());//// 收成周期 天为单位
				seedModel.setRealBonus(realBonus);// 元为单位(可领取的金额）
				seedModel.setRealSalePrice(realSalePrice);// 元为单位(购买种子的价格)
				//查询今天是否签到
			    int isAtt = isAttendances(log);
				if (isAtt ==0) {
					seedModel.setId(0);
				}else if (isAtt ==1) {
					seedModel.setId(1);
				}
				seedModel.setOrderSeedId(orderSeed2.getId());
				//将对象seedModel添加到集合中
				list.add(seedModel);
			}
		}
		return list;
	}

	// 是否签到：1，已签到，0为签到
	public int isAttendances(MemerScoreLog log) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<MemerScoreLog> list = new ArrayList<>();
		int row = 0;
		list = memerScoreLogMapper.selectLogByParams(log);// m_id,ssrId
		log.setCreateTime(new Date());

		if (list.size() < 1) {// 0未签到
			row = 0;
		} else {// 如果已经签到过，判断是今天签到的，如果是则提示“已经签到了”
			MemerScoreLog log2 = list.get(0);
			String d1 = sdf.format(new Date());// 第一个时间
			String d2 = sdf.format(log2.getCreateTime());// 第二个时间
			// System.out.println( "判断是否是今天" +d1.equals(d2));// 判断是否是同一天
			boolean flage = false;
			flage = d1.equals(d2);
			if (flage) {// 已签到
				row = 1;
			} else {
				row = 0;
			}
		}
		return row;
	}

	// 签到的接口
	public int attendances(MemerScoreLog log) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<MemerScoreLog> list = new ArrayList<>();
		OrderSeed orderSeed = orderSeedMapper.selectByPrimaryKey(log.getOrderseedId());
		int row = 0;
		list = memerScoreLogMapper.selectLogByParams(log);
		log.setCreateTime(new Date());
		//如果是第一次签到：
		if (list.size() < 1) {// 如果原来表中该用户没有数据
			MemerScoreLog scoreLog = new MemerScoreLog();
			scoreLog.setCreateTime(new Date());
			scoreLog.setmId(log.getmId());
			scoreLog.setOrderseedId(log.getOrderseedId());
			scoreLog.setSmId(orderSeed.getSmId());
			scoreLog.setSsrId(1);
			scoreLog.setTimes(1);// 总共累计一次签到
			row = memerScoreLogMapper.insertSelective(scoreLog);
		} else {
			// 如果已经签到过，判断是今天签到的，如果是则提示“已经签到了”
			MemerScoreLog log2 = list.get(0);
			String d1 = sdf.format(new Date());// 第一个时间
			String d2 = sdf.format(log2.getCreateTime());// 第二个时间
			// System.out.println( "判断是否是今天" +d1.equals(d2));// 判断是否是同一天
			boolean flage = false;
			flage = d1.equals(d2);
			// 如果flag=true,则今天已经签到过了,给出相应的提示
			if (flage) {
				row = 2;
			} else {
				// 如果今天与上一次签到相差1天，则为连续签到
				int leiji = RegExpValidatorUtils.daysBetween(log2.getCreateTime(), new Date());
				if (leiji == 1) {
					log.setTimes(log2.getTimes() + 1);
					// 如果是连续签到：times = times+1
					log.setTimes(log2.getTimes() + 1);
				} else if (leiji > 1) {
					//如果断开了,则需要重新签到:times=1
					log.setTimes(1);
				}
				log.setSmId(orderSeed.getSmId());
				log.setSsrId(Contants.rule1);
				row = memerScoreLogMapper.insertSelective(log);
			}
		}
		return row;
	}

	// 用户的收益
	public BhResult getBalance(MemerScoreLog log) throws Exception {
		BhResult bhResult = null;
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(log.getmId());
		if (memberUser != null) {
			OrderSeed orderSeed = orderSeedMapper.selectByPrimaryKey(log.getOrderseedId());
			SeedModel seedModel = seedModelMapper.selectByPrimaryKey(orderSeed.getSmId());
			Integer days = seedModel.getGainPeriod();// 收成周期 天为单位
			MemerScoreLog memerScoreLog = new MemerScoreLog();
			memerScoreLog.setmId(log.getmId());
			memerScoreLog.setSmId(log.getSmId());// 种子模型ID
			List<MemerScoreLog> list = memerScoreLogMapper.selectLogByParams(memerScoreLog);
			
			// 签到的天数
			if (list.size() >= days) {
			/*	MemberSeed memberSeed = new MemberSeed();
				//收成周期 天为单位
				memberSeed.setGainRate(seedModel.getGainPeriod());
				//收成时间
				memberSeed.setGetTime(new Date());
				//用户ID
				memberSeed.setmId(log.getmId());
				//种子模型ID
				memberSeed.setSmId(log.getmId());
				//领取的积分
				memberSeed.setMytimes(score);
				//0未付款1签到中2已收益
				memberSeed.setStatus(2);
				memberSeed.setBouns(bouns);
				*/
	
				// 更新log表的记录
				MemerScoreLog l = new MemerScoreLog();
				l.setmId(log.getmId());
				l.setSmId(log.getSmId());
				l.setOrderseedId(log.getOrderseedId());
				memerScoreLogMapper.deleteScoreByParams(l);
			
				// 更新orderSeed表的Status =3
				OrderSeed orderSeed1 = new OrderSeed();
				orderSeed1.setmId(log.getmId());
				orderSeed1.setStatus(3);
				orderSeedMapper.updateOrderSeedBymId(orderSeed1);
				
				bhResult = new BhResult(201, "领取成功", seedModel);
			} else {
				bhResult = new BhResult(202, "领取成功", null);
			}
		}

		return bhResult;
	}

	
	// 判断用户是否购买种子
	public BhResult isBuy(MemberUser memberUser) throws Exception {
		BhResult bhResult = null;
		MemberUser memberUser1 = memberUserMapper.selectByPrimaryKey(memberUser.getmId());
		// 根据mId判断用户是否存在
		if (memberUser1 == null) {
			bhResult = new BhResult(400, "抱歉,该用户不存在", null);
		} else {
			Member member = new Member();
			member.setId(memberUser.getmId());
			LandList landList =  seedGameService.selectLandList(member);
			//已购买的种子
			List<SimpleSeed> simpleSeedList = seedGameService.selectListSimpleSeed(member);
			//可用的土地
			List<SimpleLand> simpleLandList = landList.getUsable();
			if (simpleSeedList.size() >= simpleLandList.size()) {
				//不可再购买
				bhResult = new BhResult(400, "不可继续购买种子", null);
			}else{
				bhResult = new BhResult(200, "可继续购买", null);
				//可再次购买
			}
			
		}
		return bhResult;
	}

	public MemberUser selectMemberUserBymId(Integer mId) throws Exception {
		MemberUser memberUser = new MemberUser();
		memberUser = memberUserMapper.selectByPrimaryKey(mId);
		return memberUser;
	}

	public List<OrderSeed> selectOrderSeedBymId(OrderSeed orderSeed) throws Exception {
		List<OrderSeed> orderSeeds = orderSeedMapper.selecOrderSeedByParam(orderSeed);
		return orderSeeds;
	}

	
	//通过种子订单号查询该用户的信息
	public BhResult getUsermsgByOrderNo(String orderNo) throws Exception{
		BhResult bhResult = null;
		OrderSeed orderSeed = orderSeedMapper.selectSeedByOrderNo(orderNo);
		MSeed ms = new MSeed();
		if (orderSeed != null) {
			ms.setmId(orderSeed.getmId());
		}
		
		bhResult = new BhResult(200, "查询成功", ms);
		return bhResult;
	}

}
