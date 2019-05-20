package com.order.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.order.mapper.OrderSeedMapper;
import com.bh.order.pojo.OrderSeed;
import com.bh.user.mapper.MemberBalanceLogMapper;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSeedMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.SeedModelMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBalanceLog;
import com.bh.user.pojo.MemberSeed;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.SeedModel;
import com.bh.utils.JsonUtils;
import com.order.user.service.SeedService;

@Service
public class SeedServiceImpl implements SeedService{
	

	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;

	@Autowired
	private MemberUserMapper memberUserMapper;

	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	
	@Autowired
	private MemberSeedMapper memberSeedMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private SeedModelMapper seedModelMapper;

	@Autowired
	private OrderSeedMapper orderSeedMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberBalanceLogMapper memberBalanceLogMapper;
	
	
	//插入seedModel表
		public int insertSeedModel(SeedModel model) throws Exception{
				int row =0;
				row = seedModelMapper.insertSelective(model);
				return row;
		}
		
		
		//查询member
		public Member selectMemberBymId(Integer mId) throws Exception{
			Member member = new Member();
			member = memberMapper.selectByPrimaryKey(mId);
			return member;
		}
		
		public OrderSeed insertOrderSeed(OrderSeed seed) throws Exception{
			OrderSeed returnOrderSeed = new OrderSeed();
			SeedModel model = seedModelMapper.selectByPrimaryKey(seed.getSmId());
			seed.setOrderPrice(model.getSalePrice());
			orderSeedMapper.insertSelective(seed);
			returnOrderSeed = orderSeedMapper.selectByPrimaryKey(seed.getId());
			return returnOrderSeed;
		}


		@Override
		public int updateByStatus(OrderSeed seed) throws Exception {
			// TODO Auto-generated method stub
			//cheng
			int row = 1;
			row = orderSeedMapper.updateByStatus(seed);
			List<OrderSeed> orderSeed = orderSeedMapper.selecOrderSeedByParam(seed);
			MemberSeed memberSeed = new MemberSeed();
			if (orderSeed.size()>0) {
				SeedModel seedModel = seedModelMapper.selectByPrimaryKey(orderSeed.get(0).getSmId());
				memberSeed.setmId(orderSeed.get(0).getmId());
				memberSeed.setSmId(orderSeed.get(0).getSmId());
				memberSeed.setOrderseedId(orderSeed.get(0).getId());
				if ((seedModel !=null ) && (seedModel.getSmallImage()!=null)) {
					List<String> str = JsonUtils.stringToList(seedModel.getSmallImage());
					if (str.size()>0) {
						memberSeed.setGainRate(Math.round(1/str.size())*100);
					}
				}
				memberSeed.setType(seedModel.getType());
				memberSeed.setBouns(seedModel.getBonus());
				memberSeed.setGetTime(new Date());
				memberSeed.setStatus(1);
				//type=0种子，type=1土地
				if (seedModel.getType().equals(0)) {
					memberSeed.setMytimes(Contants.SOW_EXP);
				}else if(seedModel.getType().equals(1)){
					memberSeed.setMytimes(seedModel.getExperienceEverySeason());
					memberSeed.setNote(seedModel.getDescription());
				}
				int count = memberSeedMapper.selectMemberSeedByOrderSeedId(orderSeed.get(0).getId());
				if (count < 1) {
					memberSeedMapper.insertSelective(memberSeed);
				}
				
				//程凤云2018-3-24将信息insert到member_balance_log(会员收支日志表)
				MemberBalanceLog log = new MemberBalanceLog();
				//用户的id
				log.setmId(orderSeed.get(0).getmId());
				//单位分  支出为负数 收入则正数
				log.setMoney(orderSeed.get(0).getOrderPrice());
				//收支类型  -1支出 0收入
				log.setBalanceType(-1);
				//目标ID
				log.setTargetId(orderSeed.get(0).getId());
				//目标ID类型  1种子收支  2购买商品支出
				log.setTargetType(1);
				//发生时间
				log.setOcTime(new Date());
				memberBalanceLogMapper.insertSelective(log);
				//end
			}
			return row;
		}
		
		//查询滨惠自营店
		public List<MemberShop> selectBhShop(Integer isBhshop) throws Exception{
			//isBhshop:是否是滨惠自营店0不是,1是滨惠自营店
			List<MemberShop> list =memberShopMapper.selectMemberShop(isBhshop);
			return list;
		}
}
