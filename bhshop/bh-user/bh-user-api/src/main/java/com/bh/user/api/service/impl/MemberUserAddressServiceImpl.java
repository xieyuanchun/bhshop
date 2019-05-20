package com.bh.user.api.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.user.api.service.MemberUserAddressService;
import com.bh.user.mapper.AreasMapper;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.pojo.Areas;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;

@Service
public class MemberUserAddressServiceImpl implements MemberUserAddressService {
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;

	@Autowired
	private AreasMapper areasMapper;

	@Autowired
	private MemberUserMapper memberUserMapper;

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;

	@Transactional
	public int insertSelective(MemberUserAddress memberUserAddress) throws Exception {
		int row = 0;
		int r = memberUserAddressMapper.selectCountBymIdAndDefaultId(memberUserAddress.getmId());
		if (r == 1) {
			//memberUserAddressMapper.updateBymIdAndAndDefaultId(memberUserAddress.getmId());
			//是否是京东地址，0否，1是
			if (memberUserAddress.getIsJd() ==0) {
				if (memberUserAddress.getProv() != null) {
					Areas area = areasMapper.selectByPrimaryKey(memberUserAddress.getProv());
					String prvName = area.getAreaName();
					if (!StringUtils.isEmpty(prvName)) {
						memberUserAddress.setProvname(prvName);
						Areas area1 = areasMapper.selectByPrimaryKey(memberUserAddress.getCity());
						if (StringUtils.isNotEmpty(area1.getAreaName())) {
							memberUserAddress.setCityname(area1.getAreaName());
							Areas area2 = areasMapper.selectByPrimaryKey(memberUserAddress.getArea());
							if (area2 != null) {
								memberUserAddress.setAreaname(area2.getAreaName());
							}
						}
					}
				}
				row = memberUserAddressMapper.insertSelective(memberUserAddress);
			}else if (memberUserAddress.getIsJd() ==1) {
				row = insertAddressByIsJd(memberUserAddress);
			}
		} else {
			row = insertAddressByIsJd(memberUserAddress);
			
		}

		return row;
	}

	public List<MemberUserAddress> selectAllAddressByisDel(MemberUserAddress address) throws Exception {
		List<MemberUserAddress> list = memberUserAddressMapper.selectAllAddressByisDel(address.getmId());
		return list;
	}

	// 根据mId和id更新默认的收货地址
	public int updateDefaultAddress(MemberUserAddress memberUserAddress) throws Exception {
		int row = 0;
		int r = memberUserAddressMapper.selectCountBymIdAndDefaultId(memberUserAddress.getmId());// sql预计:select
																									// count()
																									// from
																									// 表
																									// where
																									// is_default
																									// =
																									// 1
																									// and
																									// m_id
																									// =
																									// #{mId,jdbcType=INTEGER}
		if (r == 1) {
			memberUserAddressMapper.updateBymIdAndAndDefaultId(memberUserAddress.getmId());
			row = memberUserAddressMapper.updateDefaultAddress(memberUserAddress);
		} else {
			row = memberUserAddressMapper.updateDefaultAddress(memberUserAddress);
		}
		return row;
	}

	// 根据mId和id查找对象
	public MemberUserAddress selectByPrimaryKey(MemberUserAddress address) throws Exception {
		MemberUserAddress returnAddress = memberUserAddressMapper.selectByPrimaryKey(address.getId());
		return returnAddress;
	}

	// 修改地址
	public int updateAddress(MemberUserAddress memberUserAddress) throws Exception {
		int row =0;
		
		row = insertAddressByIsJd(memberUserAddress);
		
		
		return row;
	}

	// 删除地址
	public int deleteAddressById(MemberUserAddress address) throws Exception {// 将is_del=1
		int row =0;
		MemberUserAddress address2 = memberUserAddressMapper.selectByPrimaryKey(address.getId());
		if (address2.getIsDefault()) {//如果该地址是默认地址
			 row = memberUserAddressMapper.updateByisDelSelective(address);
			 List<MemberUserAddress> list = memberUserAddressMapper.selectAllAddressByisDel(address.getmId());
			if (list.size()>0) {
				 MemberUserAddress memberUserAddress = list.get(0);
				 memberUserAddressMapper.updateDefaultIdAddress(memberUserAddress.getId());
				 row=1;
			}else{
				row=1;
			}
			 
		}else{
			row = memberUserAddressMapper.updateByisDelSelective(address);
		}
		
		
		return row;
	}

	// 获得兴趣
	public List<GoodsCategory> selectGoodsCategoryByReid() throws Exception {
		long catid = 0;
		List<GoodsCategory> list = null;
		list = goodsCategoryMapper.selectByParent(catid);
		return list;
	}

	// 更新用户性别
	public int updateUserSex(MemberUser memberUser) throws Exception {
		int row = 0;
		row = memberUserMapper.updateByPrimaryKeySelective(memberUser);
		return row;
	}

	// 更新用户性别
	public Member selectMember(MemberUser memberUser) throws Exception {
		Member member = new Member();
		member = memberMapper.selectByPrimaryKey(memberUser.getmId());
		return member;
	}
	
	//查询默认地址
	public MemberUserAddress selectUserDefaultAddress(Integer mId) throws Exception{
		MemberUserAddress memberUserAddress = new MemberUserAddress();
		memberUserAddressMapper.selectAllAddressByisDel(mId);
		return memberUserAddress;
	}
	
	public int insertAddressByIsJd(MemberUserAddress memberUserAddress) throws Exception{
		int row =0;
		if (memberUserAddress.getProv() != null) {
			GoAddressArea area = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getProv());
			String prvName = area.getName();
			if (!StringUtils.isEmpty(prvName)) {
				memberUserAddress.setProvname(prvName);
				GoAddressArea area1 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getCity());
				if (StringUtils.isNotEmpty(area1.getName())) {
					memberUserAddress.setCityname(area1.getName());
					GoAddressArea area2 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getArea());
					if (area2 != null) {
						memberUserAddress.setAreaname(area2.getName());
						if (memberUserAddress.getFour() !=0) {
							GoAddressArea area3 = goAddressAreaMapper.selectByPrimaryKey(memberUserAddress.getFour());
							if (area3.getName() !=null) {
								memberUserAddress.setFourname(area3.getName());
							}
						}else{
							memberUserAddress.setFourname("");
						}
					}
				}
			}
			if (memberUserAddress.getId() == null) {
				row = memberUserAddressMapper.insertSelective(memberUserAddress);
			}else{
				memberUserAddress.setIsJd(1);
				row = memberUserAddressMapper.updateByPrimaryKeySelective(memberUserAddress);
			}
		}
		return row;
	}

}
