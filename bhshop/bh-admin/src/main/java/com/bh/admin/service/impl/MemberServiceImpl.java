package com.bh.admin.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberSendMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.mapper.user.SeedScoreRuleMapper;
import com.bh.admin.mapper.user.WalletMapper;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberSend;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.admin.pojo.user.Wallet;
import com.bh.utils.FromCookie;
import com.bh.utils.MD5Util1;
import com.bh.utils.TopicUtils;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.admin.service.MemberService;

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
	
	//普通用户注册
	@Override
	public int insertSelective(Member member) throws Exception {
		Integer type = member.getType();
		
		int row = memberMapper.insertSelective(member);
		
		if (type ==1 ) {
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member.getId());
			memberUser.setAddtime(new Date());
			//获取签到规则： 1，签到 2,浇水 3,拼单, 4单买 5,分享,6注册积分，7购物积分
			SeedScoreRule se = new SeedScoreRule();
			se.setScoreAction(6);
			List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
			if (rule.size() > 0) {
				//状态 0关 1开
				if (rule.get(0).getStatus().equals(1)) {
					memberUser.setPoint(rule.get(0).getScore());
				}
			}
			memberUser.setNote("普通用户注册=用户名+密码");
			memberUserMapper.insertSelective(memberUser);
			
			
			//插入钱包的信息2018-3-15
			Wallet entity = new Wallet();
			entity.setPayPassword("123456");
			//32位盐值
			entity.setSalt(TopicUtils.getRandomString(32));
			//用户id
			entity.setUid(member.getId());
			//钱包名称
			entity.setName("个人账户余额");
			//钱包类型
			entity.setType("1");
			//钱包说明
			entity.setDes("个人钱包");
			//密码加密
			String salt = "";
			String first = MD5Util1.encode(entity.getPayPassword());
			salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			entity.setPayPassword(second); //加密后的密码
			//判断是否有数据
			List<Wallet> list = walletMapper.getWalletByUid(entity.getUid());
			if(list.size()<1){
				walletMapper.insertSelective(entity);
			}
			
		}
		
		if(type == 2){
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
			memberSend.setStatus(1);//状态0为已经审核1未待审核
			int r = memberSendMapper.insertSelective(memberSend);
			System.out.println("r = " +r);
		}
		return row;
	}
	
	//普通用户注册
		@Override
		@Transactional
		public int updateByType(Member member) throws Exception {
			Integer type = member.getType();
			
			int row = memberMapper.updateByPrimaryKeySelective(member);
					
			if(type == 2){
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
		int row =0;
				row = memberMapper.checkuser(member);
		return row;
	}

	public BhResult login(Member member,HttpServletResponse response,HttpServletRequest request) throws Exception {
		BhResult bhResult = null;
		String salt ="";
		
		List<Member> mem = memberMapper.login(member);
		if (mem.size()>0 ) {
			Member m = mem.get(0);
			String first = MD5Util1.encode(member.getPassword());
			//salt = MD5Util1.genCodes(first);
			first = first + m.getSalt();
			String second = MD5Util1.encode(first);
			if ((m.getPassword()).equals(second)) {
					bhResult = new BhResult(200, "密码正确",m);
					MemberUser memberUser = memberUserMapper.selectByPrimaryKey(m.getId());
					if (memberUser !=null) {
						if (memberUser.getStatus().equals(2)) {
							bhResult = new BhResult(400, "该账号已禁用,请与管理员联系.",null);
						}else{
							bhResult = new BhResult(200, "密码正确",m);
						}
					}else{
						bhResult = new BhResult(200, "密码正确",m);
					}
			}else{
				bhResult = new BhResult(400, "密码不正确",null);
			}
			
		}else{							
			bhResult = new BhResult(400, "该用户名不存在",null);				
		}
		return bhResult;
	}
	
	//找回密码
	public int updatepwd(Member member) throws Exception {
		//更新前检查该号码是否已经注册过   xieyc
		List<Member> listMember=memberMapper.selectByPhone(member.getPhone());
		if(listMember.size()==0){
			return -1;//该号码未注册,请先注册
		}
		int row=memberMapper.updatepwd(member);
		return row;
	}
	
	public Member selectById(Member member) throws Exception{
		Member m = memberMapper.selectByPrimaryKey(member.getId());
		return m;
	}
	
	public Member updateByParams(Member member) throws Exception {
		int row = memberMapper.updateByPrimaryKeySelective(member);
		Member m = memberMapper.selectByPrimaryKey(member.getId());
		return m;
	}
	
	//2017-9-28通过手机号查找用户
	public List<Member> selectMemberByPhone(Member member) throws Exception{
		List<Member> m = new ArrayList<>();
		m = memberMapper.selecMemberByPhone(member);
		return m;
	}
	
	 public int selectCountByPhone(Member member) throws Exception{
		 int row = 0;
		 row = memberMapper.selectCountByPhone(member);
		 return row;
	 }

}
