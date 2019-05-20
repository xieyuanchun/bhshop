package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.service.ShopMService;


@Service
public class ShopMServiceImpl implements ShopMService{
	@Autowired
	private MemberShopMapper memberShopMapper;
	
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
}
