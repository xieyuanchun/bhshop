package com.bh.user.api.service;

import java.util.List;

import com.bh.goods.pojo.GoodsCategory;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;

public interface MemberUserAddressService {
	
	//插入
	int insertSelective(MemberUserAddress memberUserAddress) throws Exception;
	
	//根据mId选择
	List<MemberUserAddress> selectAllAddressByisDel(MemberUserAddress address) throws Exception;
	
	//根据mId和id更新默认的收货地址
	int updateDefaultAddress(MemberUserAddress address) throws Exception;
	
	
	//根据mId和id查找对象
	MemberUserAddress selectByPrimaryKey(MemberUserAddress address) throws Exception;
	
	//修改地址
	int updateAddress(MemberUserAddress address) throws Exception;
	
	//删除地址
	int deleteAddressById(MemberUserAddress address) throws Exception;//将is_del=1
	
	//获得兴趣
	List<GoodsCategory> selectGoodsCategoryByReid() throws Exception;
	
	//更新用户性别
	int updateUserSex(MemberUser memberUser) throws Exception;
	
	Member selectMember(MemberUser memberUser) throws Exception;
	
}
