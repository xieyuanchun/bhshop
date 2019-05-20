package com.bh.user.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.Wallet;
import com.bh.user.vo.MemberVo;
import com.bh.utils.MD5Util1;
import com.bh.utils.TopicUtils;
import com.bh.config.Contants;
import com.bh.goods.mapper.CouponAndGiftMapper;
import com.bh.goods.mapper.CouponGiftMapper;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponAndGift;
import com.bh.goods.pojo.CouponGift;
import com.bh.goods.pojo.CouponLog;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	public MemberMapper memberMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberSendMapper memberSendMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private CouponGiftMapper couponGiftMapper;
	@Autowired
	private CouponAndGiftMapper couponAndGiftMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;

	// 普通用户注册
	@Override
	public int insertSelective(Member member, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer type = member.getType();
		int row = memberMapper.insertSelective(member);

		if (type == 1) {
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member.getId());
			memberUser.setAddtime(new Date());
			// 获取签到规则： 1，签到 2,浇水 3,拼单, 4单买 5,分享,6注册积分，7购物积分
			SeedScoreRule se = new SeedScoreRule();
			se.setScoreAction(6);
			List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
			if (rule.size() > 0) {
				// 状态 0关 1开
				if (rule.get(0).getStatus().equals(1)) {
					memberUser.setPoint(rule.get(0).getScore());

					MemerScoreLog log = new MemerScoreLog();
					log.setCreateTime(new Date());
					log.setmId(member.getId());
					log.setIsDel(0);
					log.setSmId(-2);
					log.setSsrId(1);
					log.setScore(rule.get(0).getScore());
					log.setOrderseedId(0);
					memerScoreLogMapper.insertSelective(log);
				}
			}
			memberUser.setNote("普通用户注册=用户名+密码");
			memberUserMapper.insertSelective(memberUser);

			// 插入钱包的信息2018-3-15
			Wallet entity = new Wallet();
			entity.setPayPassword("123456");
			// 32位盐值
			entity.setSalt(TopicUtils.getRandomString(32));
			// 用户id
			entity.setUid(member.getId());
			// 钱包名称
			entity.setName("个人账户余额");
			// 钱包类型
			entity.setType("1");
			// 钱包说明
			entity.setDes("个人钱包");
			// 密码加密
			String salt = "";
			String first = MD5Util1.encode(entity.getPayPassword());
			salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			entity.setPayPassword(second); // 加密后的密码
			// 判断是否有数据
			List<Wallet> list = walletMapper.getWalletByUid(entity.getUid());
			if (list.size() < 1) {
				walletMapper.insertSelective(entity);
			}

		}

		if (type == 2) {
			MemberShop memberShop = new MemberShop();
			memberShop.setmId(member.getId());
			memberShop.setAddtime(new Date());
			memberShop.setStatus(3);
			memberShopMapper.insertSelective(memberShop);
		}
		if (type == 3) {
			MemberSend memberSend = new MemberSend();
			memberSend.setmId(member.getId());
			memberSend.setTime(new Date());
			memberSend.setStatus(1);// 状态0为已经审核1未待审核
			int r = memberSendMapper.insertSelective(memberSend);
			System.out.println("r = " + r);
		}

		// 程凤云 2018-7-23
		List<MemberUser> myMemberList = memberUserMapper.selectUserPoint(member.getId());
		MemberUser myMemberUser = new MemberUser();
		if (myMemberList.size() < 1) {
			// 获取签到规则： 1，签到 2,浇水 3,拼团 4单买 5,分享,6注册积分，7购物积分
			SeedScoreRule se = new SeedScoreRule();
			se.setScoreAction(6);
			List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
			if (rule.size() > 0) {
				// 状态 0关 1开
				if (rule.get(0).getStatus().equals(1)) {
					myMemberUser.setPoint(rule.get(0).getScore());
					MemerScoreLog log = new MemerScoreLog();
					log.setCreateTime(new Date());
					log.setmId(member.getId());
					log.setIsDel(0);
					log.setSmId(-2);
					log.setSsrId(1);
					log.setScore(rule.get(0).getScore());
					log.setOrderseedId(0);
					memerScoreLogMapper.insertSelective(log);
				}
			}
			myMemberUser.setmId(member.getId());
			myMemberUser.setAddtime(new Date());
			myMemberUser.setNote("普通用户注册=用户名+密码");
			memberUserMapper.insertSelective(myMemberUser);
		}

		return row;
	}

	// 普通用户注册
	@Override
	@Transactional
	public int updateByType(Member member) throws Exception {
		Integer type = member.getType();

		int row = memberMapper.updateByPrimaryKeySelective(member);

		if (type == 2) {
			MemberShop memberShop = new MemberShop();
			memberShop.setmId(member.getId());
			memberShop.setAddtime(new Date());
			memberShopMapper.insertSelective(memberShop);
		}
		if (type == 3) {
			MemberSend memberSend = new MemberSend();
			memberSend.setmId(member.getId());
			memberSendMapper.insertSelective(memberSend);
		}
		return row;
	}

	@Override
	public int insertUser(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	// 检查用户名是否重复
	public int checkuser(Member member) throws Exception {
		int row = 0;
		row = memberMapper.checkuser(member);
		return row;
	}

	public BhResult login(Member member, HttpServletResponse response, HttpServletRequest request) throws Exception {
		BhResult bhResult = null;

		List<Member> mem = memberMapper.login(member);
		if (mem.size() > 0) {
			Member m = mem.get(0);
			String first = MD5Util1.encode(member.getPassword());
			// salt = MD5Util1.genCodes(first);
			first = first + m.getSalt();
			String second = MD5Util1.encode(first);
			if ((m.getPassword()).equals(second)) {
				m.setPassword(MD5Util1.encode("show_passwd"));
				bhResult = new BhResult(200, "密码正确", m);
				List<MemberUser> memberUser = memberUserMapper.selectUserPoint(m.getId());
				MemberVo memberVo = new MemberVo();
				memberVo.setId(m.getId());

				if (memberUser.size()>0) {
					if (memberUser.get(0).getStatus().equals(2)) {
						bhResult = new BhResult(400, "该账号已禁用,请与管理员联系.", null);
					} else {
						bhResult = new BhResult(200, "密码正确", memberVo);
					}
				} else {
					bhResult = new BhResult(200, "密码正确", memberVo);
				}
			} else {
				bhResult = new BhResult(400, "密码不正确", null);
			}

		} else {
			bhResult = new BhResult(400, "该用户名不存在", null);
		}
		return bhResult;
	}

	/**
	 * @Description: 用户注册成功赠送大礼包逻辑
	 * @author xieyc
	 * @date 2018年6月5日 上午9:48:49
	 */
	public void giveCouponGift(int mId) {
		String giftName = Contants.NEWUSERCOUPONGIFTNAME;
		CouponGift couponGift = couponGiftMapper.getGiftByName(giftName);// 根据礼包名字获取大礼包
		if (couponGift != null && couponGift.getGiftStatus() == 0) {
			CouponAndGift findCouponAndGift = new CouponAndGift();
			findCouponAndGift.setcGId(couponGift.getId());
			List<CouponAndGift> couponAndGiftList = couponAndGiftMapper.selectByCGid(findCouponAndGift);
			for (CouponAndGift couponAndGift : couponAndGiftList) {
				Coupon coupon = couponMapper.selectByPrimaryKey(couponAndGift.getcId());
				if (coupon != null && coupon.getStatus() == 1 && coupon.getStock() > 0) {// 没有被禁用,优惠卷剩余量>0
					for (int i = 0; i < couponAndGift.getcNum(); i++) {
						CouponLog saveCouponLog = new CouponLog();
						saveCouponLog.setmId(mId);// 当前领取的客户的id
						saveCouponLog.setCouponId(coupon.getId());// CouponId
						int shopId = coupon.getShopId();
						if (shopId == 0) {
							shopId = 1;
						}
						saveCouponLog.setShopId(shopId); // 商家id
						couponLogMapper.insertSelective(saveCouponLog);// 插入礼包记录

						coupon.setStock(coupon.getStock() - 1);
						couponMapper.updateByPrimaryKeySelective(coupon);// 更新优惠卷剩余数
					}
				}
			}
		}
	}

	// 找回密码
	public int updatepwd(Member member) throws Exception {
		// 更新前检查该号码是否已经注册过 xieyc
		List<Member> listMember = memberMapper.selectByPhone(member.getPhone());
		if (listMember.size() == 0) {
			return -1;// 该号码未注册,请先注册
		}
		int row = memberMapper.updatepwd(member);
		return row;
	}

	public Member selectById(Member member) throws Exception {
		Member m = memberMapper.selectByPrimaryKey(member.getId());
		return m;
	}

	public Member updateByParams(Member member) throws Exception {
		memberMapper.updateByPrimaryKeySelective(member);
		Member m = memberMapper.selectByPrimaryKey(member.getId());
		return m;
	}

	// 2017-9-28通过手机号查找用户
	public List<Member> selectMemberByPhone(Member member) throws Exception {
		List<Member> m = new ArrayList<>();
		m = memberMapper.selecMemberByPhone(member);
		return m;
	}

	public int selectCountByPhone(Member member) throws Exception {
		int row = 0;
		row = memberMapper.selectCountByPhone(member);
		return row;
	}

}
