package com.bh.user.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsShareLogMapper;
import com.bh.goods.pojo.GoodsShareLog;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberUserService;
import com.bh.user.mapper.MemberBankCardMapper;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.TbBankMapper;
import com.bh.user.pojo.BankSimple;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserSimple;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.TbBank;
import com.bh.utils.MD5Util1;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.RegExpValidatorUtils;

@Service
public class MemberUserServiceImpl implements MemberUserService{
	
	@Autowired
	private MemberUserMapper memberUserMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private GoodsShareLogMapper goodsShareLogMapper;
	
	@Autowired
	private MemberBankCardMapper memberBankCardMapper;
	
	@Autowired
	private MemberSendMapper memberSendMapper;
	
	@Autowired
	private TbBankMapper tbBankMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	
	public Member updateByPrimaryKeySelective(MemberUser memberUser) throws Exception{
		Member member = new Member();
		member.setId(memberUser.getmId());
		if (StringUtils.isNotEmpty(memberUser.getAddress())) {
			member.setHeadimgurl(memberUser.getAddress());
			memberMapper.updateByPrimaryKeySelective(member);
		}
		int row = memberUserMapper.updateByPrimaryKeySelective(memberUser);
		Member member2 = memberMapper.selectByPrimaryKey(memberUser.getmId());
		return member2 ;
	}
	
	public int updateEmailBymId(MemberUser memberUser) throws Exception{
		int row = memberUserMapper.updateEmailBymId(memberUser);
		return row;
	}
	
	//更新Member用户的头像
	public int updateMemberImg(Member member) throws Exception{
		int row = memberMapper.updateByPrimaryKeySelective(member);
		return row ;
	}
	
	//根据mId查询对象
	public MemberUser selectByPrimaryKey(Integer mId) throws Exception{
		
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(mId);
		if (memberUser == null) {
			MemberUser memberUser2 = new MemberUser();
			memberUser2.setmId(mId);
			memberUser2.setAddtime(new Date());
			//获取签到规则： 1，签到 2,浇水 3,拼单 4单买 5,分享,6注册积分，7购物积分
			SeedScoreRule se = new SeedScoreRule();
			se.setScoreAction(6);
			List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
			if (rule.size() > 0) {
				//状态 0关 1开
				if (rule.get(0).getStatus().equals(1)) {
					memberUser2.setPoint(rule.get(0).getScore());
				}
			}
			memberUser2.setNote("test the double account:method=basemsg");
			MemberUser memberUser3 = memberUserMapper.selectByPrimaryKey(mId);
			if (memberUser3 == null) {
				memberUserMapper.insertSelective(memberUser2);
			}
			memberUser = memberUserMapper.selectByPrimaryKey(mId);
		}
		return memberUser;
	}
	
	/********2017-11-09cheng更新支付密码**********/
	public int updatePayCode(MemberUser memberUser) throws Exception{
		int row =0 ;
		String paycode = memberUser.getPaycode();
		
		paycode = MixCodeUtil.washStr(paycode);//过滤敏感词
		String code = MD5Util1.encode(paycode);//md5转译
		memberUser.setPaycode(code);
		row = memberUserMapper.updateByPrimaryKeySelective(memberUser);
		return row;
	}
	
	public MemberUser selectMember(MemberUser memberUser) throws Exception{
		MemberUser memberUser2 =null;
		memberUser2 = memberUserMapper.selectByPrimaryKey(memberUser.getmId());
		return memberUser2;
	}
	
	/********2017-12-09cheng用户认证1**********/
	public MemberUser toAuth1u(MemberUser memberUser) throws Exception{
		MemberUser memberUser2 = new MemberUser();
		
		memberUserMapper.updateByPrimaryKeySelective(memberUser);
		memberUser2 = memberUserMapper.selectByPrimaryKey(memberUser.getmId());
		return memberUser2;
	}
	
	/********2017-12-11cheng用户认证2:添加银行卡**********/
	public int toAuth2(MemberBankCard card) throws Exception{
		int row =0;
		List<MemberBankCard> list = new ArrayList<>();
		
		list = memberBankCardMapper.selectMemberBankCartByParams(card);
		if (list.size() > 0) {
			row =2;
		}else{
			row = memberBankCardMapper.insertSelective(card);
		}
		return row;
	}
	
	
	//用户分享
	public int goodShare(GoodsShareLog log) throws Exception{
		int row =0;
		row = goodsShareLogMapper.insertSelective(log);
		return row;
	}
	
	
	//用户商城钱包接口
	public MemberUserSimple userBalance(Member member) throws Exception{
		MemberUserSimple simple = new MemberUserSimple();
		MemberBankCard card = new MemberBankCard();
		card.setmId(member.getId());
		List<MemberBankCard> cardList = memberBankCardMapper.selectMemberBankCartByParams(card);
		List<BankSimple> bankList = new ArrayList<>();
		for (MemberBankCard memberBankCard : cardList) {
			//对字符串处理:将指定位置到指定位置的字符以星号代替 
			String s1 = RegExpValidatorUtils.getStarString(memberBankCard.getBankCardNo(), 0, memberBankCard.getBankCardNo().length()-3);
			StringBuffer sb = new StringBuffer();
			sb.append(s1.substring(0, 4));
			sb.append(" ");
			sb.append(s1.substring(4, 8));
			sb.append(" ");
			sb.append(s1.substring(8, 12));
			sb.append(" ");
			sb.append(s1.substring(12, 16));
			sb.append(" ");
			sb.append(s1.substring(memberBankCard.getBankCardNo().length()-3, memberBankCard.getBankCardNo().length()));
			//System.out.println(sb.toString());
			//6217 8526 0000 5511 951
			BankSimple simple2 = new BankSimple();
			simple2.setBankCardNo(sb.toString());
			simple2.setBankCode(memberBankCard.getBankCode());
			simple2.setBankKind(memberBankCard.getBankKind());
			simple2.setBankName(memberBankCard.getBankName());
			simple2.setBankType(memberBankCard.getBankType());
			TbBank re = new TbBank();
			re.setBankType(memberBankCard.getBankCode());
			List<TbBank> tbBanks  = tbBankMapper.selectTbBankByNo(re);
			if (tbBanks.size()>0) {
				if (tbBanks.get(0).getImg().equals("0")) {
					simple2.setImg(Contants.DEFAULT_BANK_IMG);
				}else{
					simple2.setImg(tbBanks.get(0).getImg());
				}
			}else{
				simple2.setImg(Contants.DEFAULT_BANK_IMG);
			}
			bankList.add(simple2);
		}
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(member.getId());
		MemberSend memberSend = memberSendMapper.selectByPrimaryKey(member.getId());
		//速达钱包
		double balance1 = 0;
		if (memberSend == null) {
			balance1 =0;
		}else{
			balance1 = (double) memberSend.getBalance();
		}
		
		double balance = (double) memberUser.getBalance()/100;
		
		simple.setBalance(balance);
		simple.setSudabalance(balance1);
		simple.setBankSimple(bankList);
		return simple;
	}
	
	//用户选择银行卡
	public List<BankSimple> selectBankCard(Integer mId) throws Exception{
		List<BankSimple> list = new ArrayList<>();
		MemberBankCard card = new MemberBankCard();
		card.setmId(mId);
		List<MemberBankCard> cardList = memberBankCardMapper.selectMemberBankCartByParams(card);
		List<BankSimple> bankList = new ArrayList<>();
		for (MemberBankCard memberBankCard : cardList) {
			//对字符串处理:将指定位置到指定位置的字符以星号代替 
			String s1 = RegExpValidatorUtils.getStarString(memberBankCard.getBankCardNo(), 0, memberBankCard.getBankCardNo().length()-3);
			StringBuffer sb = new StringBuffer();
		/*	sb.append(s1.substring(0, 4));
			sb.append(" ");
			sb.append(s1.substring(4, 8));
			sb.append(" ");
			sb.append(s1.substring(8, 12));
			sb.append(" ");
			sb.append(s1.substring(12, 16));
			sb.append(" ");*/
			sb.append(s1.substring(memberBankCard.getBankCardNo().length()-3, memberBankCard.getBankCardNo().length()));
			//System.out.println(sb.toString());
			//6217 8526 0000 5511 951
			BankSimple simple2 = new BankSimple();
			simple2.setBankCardNo(sb.toString());
			simple2.setBankCode(memberBankCard.getBankCode());
			simple2.setBankKind(memberBankCard.getBankKind());
			simple2.setBankName(memberBankCard.getBankName());
			simple2.setBankType(memberBankCard.getBankType());
			
			TbBank re = new TbBank();
			re.setBankType(memberBankCard.getBankCode());
			List<TbBank> tbBanks  = tbBankMapper.selectTbBankByNo(re);
			if (tbBanks.size()>0) {
				if (tbBanks.get(0).getImg().equals("0")) {
					simple2.setImg(Contants.DEFAULT_BANK_IMG);
				}else{
					simple2.setImg(tbBanks.get(0).getImg());
				}
			}else{
				simple2.setImg(Contants.DEFAULT_BANK_IMG);
			}
			bankList.add(simple2);
		}
		return bankList ;
	}
	
	
	public List<TbBank> selectTbBankList(TbBank tbBank) throws Exception{
		List<TbBank> list = tbBankMapper.selectTbBankByNo(tbBank);
		return list;
	}
	
	public Member getMember(Integer Id) throws Exception{
		return memberMapper.selectByPrimaryKey(Id);
	}
	
	public Member getMemberById(Integer id) throws Exception{
		return memberMapper.selectMemberMsg(id);
	}
	
	//2018-4-23修改昵称
	public	BhResult updateUsername(Member member) throws Exception{
		BhResult bhResult = new BhResult(400, "该昵称已存在,请使用其他的昵称。", null);
		if (StringUtils.isNotEmpty(member.getUsername())) {
			if ((member.getId()!=null) && (member.getUsername()!=null)) {
				Member member2 = memberMapper.selectByPrimaryKey(member.getId());
				if (!member2.getUsername().equals(member.getUsername())) {
					int count = memberMapper.selectUsername(member);
					if (count<1) {
						memberMapper.updateByPrimaryKeySelective(member);
						bhResult = new BhResult(200, "操作成功", null);
					}
				}else{
					bhResult = new BhResult(200, "操作成功", null);
				}
			}
		}else{
			bhResult = new BhResult(400, "用户昵称不能为空", null);
		}
		return bhResult;
	}
	
	public MemberUser selectUserPoint(Integer mId) throws Exception{
		List<MemberUser> list =memberUserMapper.selectUserPoint(mId);
		if (list.size()>0) {
			return list.get(0);
		}else{
			// 程凤云 2018-7-23
			List<MemberUser> myMemberList = memberUserMapper.selectUserPoint(mId);
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
						log.setmId(mId);
						log.setIsDel(0);
						log.setSmId(-2);
						log.setSsrId(1);
						log.setScore(rule.get(0).getScore());
						log.setOrderseedId(0);
						memerScoreLogMapper.insertSelective(log);
					}
				}
				myMemberUser.setmId(mId);
				myMemberUser.setAddtime(new Date());
				myMemberUser.setNote("test the double account:basemsg");
				memberUserMapper.insertSelective(myMemberUser);
			}

			
			
			
			
			
			
			
			
			MemberUser memberUser=new MemberUser();
			memberUser.setPoint(0);
			memberUser.setAddtime(new Date());
			return memberUser;
		}
	}
	
}
