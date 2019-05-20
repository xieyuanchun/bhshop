package com.order.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.MD5Util1;
import com.bh.utils.SmsUtil;
import com.mysql.jdbc.StringUtils;
import com.order.user.service.ShopMService;

@Service
public class ShopMServiceImpl implements ShopMService{
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberSendMapper sendMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
    @Autowired
    private WalletMapper walletMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	
	public List<MemberShop> selectMemberShop(MemberShop memberShop) throws Exception {
		List<MemberShop> list = new ArrayList<>();
		list = memberShopMapper.selectMemberShopByParams(memberShop);
		return list;
	}
	
	
	//商家信息的更新
	public int updateMemberShop(MemberShop memberShop) throws Exception{
		int row = 0;
		row = memberShopMapper.updateMemberShop(memberShop);
		return row;
	}
	
	//押金支付成功
	public	int updateMemberShopByOrderNo (MemberShop memberShop) throws Exception{
		int row = 0;
		row = memberShopMapper.updateMemberShopByOrderNo(memberShop);
		return row;
	}
	
	public MemberShop selectMemberShopById(Integer shopId) throws Exception{
		return memberShopMapper.selectByPrimaryKey(shopId);
	}
	
	
	//更新订单号(未支付)
	public int updateMemberShopByDespo(MemberShop memberShop) throws Exception{
		int row = 0;
		row = memberShopMapper.updateByPrimaryKeySelective(memberShop);
		return row;
	}
	
	
	//免审核押金支付成功
	public	int updateMemberShopByDescNo (MemberShop memberShop) throws Exception{
		int row = 0;
		memberShop.setDepositTime(new Date());
		row = memberShopMapper.updateMemberShopByDescNo(memberShop);
		return row;
	}
	
	
	//通过depositNo查询该订单是否支付成功
	public MemberShop checkIsPaySeccuss(String depositNo) throws Exception{
		MemberShop memberShop =  new MemberShop();
		memberShop = memberShopMapper.checkIsPaySeccuss(depositNo);
		return memberShop;
	}
	

	public List<Wallet> getByUid(Wallet entity) {
		// TODO Auto-generated method stub
		return walletMapper.getWalletByUid(entity.getUid());
	}
	public int addWalletLog(WalletLog record) {
		// TODO Auto-generated method stub
		return walletLogMapper.insertSelective(record);
	}


	
	public void insertAdminAndMemberSend(MemberShop memberShop, Integer getmId) {
		this.insertMemberSend(memberShop);// 插入配送员
		this.insertAdmin(memberShop.getmId());// 插入sys_user记录
	}
	
	public int insertMemberSend(MemberShop entity)  {
		int row = 0;
		Member m = new Member();
		m.setUsername(entity.getLinkmanPhone());
		List<Member> list = memberMapper.login(m);
		if(list.size()>0){
			MemberSend memberSend = sendMapper.selectByPrimaryKey(list.get(0).getId());
			if(memberSend==null){
				MemberSend send = new MemberSend();
				send.setmId(list.get(0).getId());
				if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getShopName())){
					send.setFullName(entity.getShopName());
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAddress())){
					send.setAddress(entity.getAddress());
				}
				send.setTime(new Date());
				send.setScope(Contants.SEND_SCOPE);
				send.setTool(3);
				send.setStatus(1);
				row = sendMapper.insertSelective(send); //插入配送员信息
			}
		}else{
			Member member = new Member();
			member.setType(2);
			if(entity.getShopName()!=null){
				member.setUsername(entity.getShopName());
			}else{
				member.setUsername("匿名");
			}
			member.setPhone(entity.getLinkmanPhone());
			if(entity.getLogo()!=null){
				member.setHeadimgurl(entity.getLogo());
			}
			String first = MD5Util1.encode("123456");
			String salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			member.setPassword(second);
			member.setSalt(salt);
			row = memberMapper.insertSelective(member); //插入登录信息
			
			MemberUser user  = new MemberUser();
			user.setmId(member.getId());
			user.setAddtime(new Date());
			//获取签到规则： 1，签到 2,浇水 3,拼单 4单买 5,分享,6注册积分，7购物积分
			SeedScoreRule se = new SeedScoreRule();
			se.setScoreAction(6);
			List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
			if (rule.size() > 0) {
				//状态 0关 1开
				if (rule.get(0).getStatus().equals(1)) {
					user.setPoint(rule.get(0).getScore());
				}
			}
			user.setNote("商家自配账号");
			row = memberUserMapper.insertSelective(user);//插入用户信息
			
			MemberSend send = new MemberSend();
			send.setmId(member.getId());
			if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getShopName())){
				send.setFullName(entity.getShopName());
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(entity.getAddress())){
				send.setAddress(entity.getAddress());
			}
			send.setTime(new Date());
			send.setScope(Contants.SEND_SCOPE);
			send.setTool(3);
			send.setStatus(1);
			row = sendMapper.insertSelective(send); //插入配送员信息
		}		
		return row;
	}
	
	public int insertAdmin(Integer mId)  {
		int row = 0;
		//where shopId=?
		row = memberShopMapper.selectBymId(mId);
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(mId);
		//是否存在pos机，1有,2无(需要提交押金，可退回押金)，3无(不需要提交押金)
		if (row == 0) {
			MemberShop memberShop3 = new MemberShop();
			memberShop3 = memberShopMapper.selectByPrimaryKey(mId);
			MemberShop memberShop2 = new MemberShop();
			memberShop2.setmId(memberShop3.getmId());

		if (memberShop3.getLinkmanPhone() == null) {
			memberShop2.setLinkmanPhone("18820079141");
		} else {
			memberShop2.setLinkmanPhone(memberShop3.getLinkmanPhone());
		}

		if (memberShop3.getLinkmanPhone() == null) {
			memberShop2.setLinkmanName("BH"+memberShop3.getShopName());
		} else {
			memberShop2.setLinkmanName("BH"+memberShop3.getLinkmanPhone());
		}

		String salt = RandomStringUtils.randomAlphanumeric(20);
		memberShop2.setAddress(new Sha256Hash("123456", salt).toHex());
		memberShop2.setLogo(salt);
		memberShop2.setAddtime(new Date());
		row = memberShopMapper.insertAdmin(memberShop2);

	   }
		return row;
	}
	
}
