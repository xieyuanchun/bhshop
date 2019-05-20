package com.bh.user.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.order.pojo.OrderPayment;
import com.bh.result.BhResult;
import com.bh.user.api.service.ShopService;
import com.bh.user.mapper.BusBankCardMapper;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.pojo.BusBankCard;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.utils.MD5Util1;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private MemberShopMapper memberShopMapper;

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

	@Autowired
	private BusBankCardMapper busBankCardMapper;

	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
	
	@Autowired
	private MemberSendMapper sendMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private MemberUserMapper memberUserMapper;
	
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	
	public int insertMemberShopByStepOne(MemberShop memberShop) throws Exception{
		int row = memberShopMapper.insertSelective(memberShop);
		return row;
	}

	public List<MemberShop> selectMemberShop(MemberShop memberShop) throws Exception {
		List<MemberShop> list = new ArrayList<>();
		list = memberShopMapper.selectMemberShopByParams(memberShop);
		return list;
	}

	@SuppressWarnings("null")
	public BhResult insertStep(MemberShop memberShop) throws Exception {
		BhResult bhResult = null;
		memberShop.setStep(1);
		MemberShop memberShop5 = new MemberShop();
		memberShop5.setToken(memberShop.getToken());
		memberShop5.setLinkmanPhone(memberShop.getLinkmanPhone());
		int count = memberShopMapper.selectMemberShopByPhone(memberShop5);
		// 通过联系人电话查询记录，查看该表里面是否有该联系人电话,如果还未有,则下一步;如果已有,则提示用户使用其他的联系电话

		int row = memberShopMapper.selectByUsername(memberShop.getLinkmanPhone());
		if ((count == 0) && (row == 0)) {
			// 最后，入库完成
			memberShop.setAddtime(new Date());
			if (memberShop.getCatId() == null) {
				List<GoodsCategory> list = goodsCategoryMapper.selectCategoryByreid();
				long c = list.get(0).getId();
				memberShop.setCatId(new Long(c).intValue());
			}
			MemberShop memberShop2 = new MemberShop();
			memberShop2.setToken(memberShop.getToken());
			List<MemberShop> memberShop3 = memberShopMapper.selectMemberShopByParams(memberShop2);
			memberShop.setStep(2);
			if (memberShop3.size() > 0) {
				memberShop.setmId(memberShop3.get(0).getmId());
				memberShopMapper.updateByPrimaryKeySelective(memberShop);
			} else {
				memberShopMapper.insertSelective(memberShop);
			}

			bhResult = new BhResult(200, "联系信息已入库", memberShop.getmId());

		} else {
			bhResult = new BhResult(400, "联系人手机号已存在,请使用其他的手机号", null);
		}

		return bhResult;
	}

	public MemberShop getMemberShop(Integer token) throws Exception {
		// 根据主键去查询该记录是否存在
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(token);
		// 返回该对象
		return memberShop;
	}

	// 移动端端商家入驻的第二步
	public BhResult updateMemberShop(MemberShop memberShop) throws Exception {
		BhResult bhResult = null;
		int row = memberShopMapper.updateByPrimaryKeySelective(memberShop);
		if (row == 0) {
			bhResult = new BhResult(400, "更新成功", memberShop.getmId());
		} else if (row == 1) {
			bhResult = new BhResult(200, "信息更新成功", null);
		}
		return bhResult;
	}

	/*******************************************************************************************************************************/
	public BhResult checkStepOne(MemberShop memberShop) throws Exception {
		BhResult bhResult = null;
		// 查询最大的列的值
		MemberShop memberShop2 = memberShopMapper.selectMaxColumn();
		Integer id = 0;
		// 如果商家未入驻
		if (memberShop.getmId().equals(0)) {
			// 如果该表还未有记录入库，id为1
			if (memberShop2 == null) {

				memberShop2.setStep(0);
				id = 1;
				memberShopMapper.insertSelective(memberShop);
			} else {
				// 如果该表有记录，则id=最大值+1
				id = memberShop2.getmId() + 1;
			}
			// 申请步骤
			memberShop.setStep(0);
			;
			memberShop.setmId(id);
			MemberShop memberShop4 = new MemberShop();
			memberShop4.setLinkmanName(memberShop.getLinkmanName());
			// 通过联系人姓名查询记录，查看该表里面是否有该联系人姓名
			List<MemberShop> memberShop3 = memberShopMapper.selectMemberShopByParams(memberShop4);
			// 如果还未有,则下一步;如果已有,则提示用户使用其他的名称
			if (memberShop3.size() < 1) {
				MemberShop memberShop5 = new MemberShop();
				memberShop5.setLinkmanPhone(memberShop.getLinkmanPhone());
				List<MemberShop> memberShop6 = memberShopMapper.selectMemberShopByParams(memberShop5);
				// 通过联系人电话查询记录，查看该表里面是否有该联系人电话,如果还未有,则下一步;如果已有,则提示用户使用其他的联系电话
				if (memberShop6.size() < 1) {
					MemberShop memberShop7 = new MemberShop();
					memberShop7.setLinkmanEmail(memberShop.getLinkmanEmail());
					List<MemberShop> memberShop8 = memberShopMapper.selectMemberShopByParams(memberShop7);
					// 通过联系人电话查询记录，查看该表里面是否有该联系人邮箱,如果还未有,则下一步;如果已有,则提示用户使用其他的邮箱
					if (memberShop8.size() < 1) {
						bhResult = new BhResult(200, "可以进行下一步", null);

					} else {
						bhResult = new BhResult(400, "联系人邮箱已存在,请使用其他的邮箱地址", null);
					}
				} else {
					bhResult = new BhResult(400, "联系人手机号已存在,请使用其他的手机号", null);
				}
			} else {
				bhResult = new BhResult(400, "联系人姓名已存在", null);
			}

		}
		// 商家已入驻，则更新信息就可以了
		else {
			memberShopMapper.updateByPrimaryKeySelective(memberShop);
		}
		return bhResult;
	}

	public BhResult insertMemberShop(MemberShop memberShop) throws Exception {
		BhResult bhResult = null;
		MemberShop memberShop4 = new MemberShop();
		memberShop4.setLinkmanName(memberShop.getLinkmanName());
		// 通过联系人姓名查询记录，查看该表里面是否有该联系人姓名
		List<MemberShop> memberShop3 = memberShopMapper.selectMemberShopByParams(memberShop4);
		// 如果还未有,则下一步;如果已有,则提示用户使用其他的名称
		if (memberShop3.size() < 1) {
			MemberShop memberShop5 = new MemberShop();
			memberShop5.setLinkmanPhone(memberShop.getLinkmanPhone());
			List<MemberShop> memberShop6 = memberShopMapper.selectMemberShopByParams(memberShop5);
			// 通过联系人电话查询记录，查看该表里面是否有该联系人电话,如果还未有,则下一步;如果已有,则提示用户使用其他的联系电话
			if (memberShop6.size() < 1) {
				MemberShop memberShop7 = new MemberShop();
				memberShop7.setLinkmanEmail(memberShop.getLinkmanEmail());
				List<MemberShop> memberShop8 = memberShopMapper.selectMemberShopByParams(memberShop7);
				// 通过联系人电话查询记录，查看该表里面是否有该联系人邮箱,如果还未有,则下一步;如果已有,则提示用户使用其他的邮箱
				if (memberShop8.size() < 1) {
					// 最后，入库完成
					bhResult = new BhResult(200, "可以入库,无重复信息", null);
				} else {
					bhResult = new BhResult(400, "联系人邮箱已存在,请使用其他的邮箱地址", null);
				}
			} else {
				bhResult = new BhResult(400, "联系人手机号已存在,请使用其他的手机号", null);
			}
		} else {
			bhResult = new BhResult(400, "联系人姓名已存在", null);
		}
		// 商家已入驻，则更新信息就可以了
		return bhResult;
	}

	public PageBean<MemberShop> selectShopList(MemberShop memberShop, Integer page, Integer size) throws Exception {
		List<MemberShop> list = new ArrayList<>();
		// 启用分页查询
		PageHelper.startPage(page, size, true);
		list = memberShopMapper.selectMemberShopList(memberShop);
		for (int i = 0; i < list.size(); i++) {
			List<BusBankCard> bankList = new ArrayList<>();
			BusBankCard busBankCard = new BusBankCard();
			busBankCard.setmId(list.get(i).getmId());
			bankList = busBankCardMapper.selectMemberBankCartByParams(busBankCard);
			list.get(i).setBankList(bankList);
			if (list.get(i).getProv() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getProv());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setProvName(area1.get(0).getName());
				}
			}

			if (list.get(i).getCity() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getCity());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setCityName(area1.get(0).getName());
				}
			}

			if (list.get(i).getArea() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getArea());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setAreaName(area1.get(0).getName());
				}
			}

			if (!list.get(i).getFour().equals(0)) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getFour());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setFourName(area1.get(0).getName());
				}
			}

			if (list.get(i).getLicenseProv() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getLicenseProv());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setLicenseProvName(area1.get(0).getName());
				}
			}

			if (list.get(i).getLicenseCity() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getLicenseCity());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setLicenseCityName(area1.get(0).getName());
				}
			}

			if (list.get(i).getLicenseTown() != null) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getLicenseTown());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setLicenseTownName(area1.get(0).getName());
				}
			}

			if (!list.get(i).getLicenseFour().equals(0)) {
				GoAddressArea record1 = new GoAddressArea();
				record1.setId(list.get(i).getLicenseFour());
				List<GoAddressArea> area1 = goAddressAreaMapper.selectById(record1);
				if (area1.size() > 0) {
					list.get(i).setLicenseFourName(area1.get(0).getName());
				}
			}

		}
		PageInfo<MemberShop> info = new PageInfo<>(list);

		PageBean<MemberShop> pageBean = null;
		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());
		return pageBean;
	}

	public int updateStep(MemberShop memberShop) throws Exception {
		int row = 0;
		row = memberShopMapper.updateStep(memberShop);
		return row;
	}

	public MemberShop selectMemberShopByPrimaryKey(Integer id) throws Exception {
		MemberShop memberShop = new MemberShop();
		memberShop = memberShopMapper.selectByPrimaryKey(id);
		return memberShop;
	}

	public BhResult insertStepByPc(MemberShop memberShop) throws Exception {
		BhResult bhResult = null;
		MemberShop memberShop5 = new MemberShop();
		memberShop5.setLinkmanPhone(memberShop.getLinkmanPhone());
		int count = memberShopMapper.selectMemberShopByPhone(memberShop5);
		// 通过联系人电话查询记录，查看该表里面是否有该联系人电话,如果还未有,则下一步;如果已有,则提示用户使用其他的联系电话
		int row = memberShopMapper.selectByUsername(memberShop.getLinkmanPhone());
		if ((count == 0) && (row == 0)) {
			// 最后，入库完成
			memberShop.setAddtime(new Date());
			 memberShopMapper.insertSelective(memberShop);
			 
			List<MemberShop> memberShop2 =  memberShopMapper.selectMemberShopList(memberShop);
			if (memberShop2.size()>0) {
				bhResult = new BhResult(200, "操作成功", memberShop2.get(0).getmId());
			}
			
		} else {
			bhResult = new BhResult(400, "联系人手机号已存在,请使用其他的手机号", null);
		}

		return bhResult;
	}

	public int insertAdmin(Integer mId) throws Exception {
		int row = 0;
		// where shopId=?
		row = memberShopMapper.selectBymId(mId);
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(mId);
		// 是否存在pos机，1有,2无(需要提交押金，可退回押金)，3无(不需要提交押金)
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
				memberShop2.setLinkmanName(memberShop3.getShopName());
			} else {
				memberShop2.setLinkmanName(memberShop3.getLinkmanPhone());
			}

			String salt = RandomStringUtils.randomAlphanumeric(20);
			memberShop2.setAddress(new Sha256Hash("123456", salt).toHex());
			memberShop2.setLogo(salt);
			memberShop2.setAddtime(new Date());
			row = memberShopMapper.insertAdmin(memberShop2);

		}
		return row;
	}

	public int insertBankCard(BusBankCard card) throws Exception {
		int row = 0;
		List<BusBankCard> list = new ArrayList<>();

		list = busBankCardMapper.selectMemberBankCartByParams(card);
		if (list.size() > 0) {
			row = 1;
		} else {
			row = busBankCardMapper.insertSelective(card);
		}
		return row;
	}

	public String selectUsernameBymId(Integer mId) throws Exception {
		// List<SysUserEntity> list = new ArrayList<>();
		// list = memberShopMapper.selectUsernameBymId(mId);
		return memberShopMapper.selectUsernameBymId(mId);
	}
	@Transactional
	@Override
	public int insertMemberSend(MemberShop entity) throws Exception {
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
	
	//将店铺设为自营店
	public	int updateShop(MemberShop memberShop) throws Exception{
		int row = 0;
		if (memberShop.getIsBhshop() !=null) {
			memberShopMapper.updateShop();
		}
		row = memberShopMapper.updateByPrimaryKeySelective(memberShop);
		return row ;
	}
}
