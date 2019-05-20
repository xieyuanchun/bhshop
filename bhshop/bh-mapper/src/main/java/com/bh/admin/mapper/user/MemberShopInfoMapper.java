package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.MemberShopInfo;
import com.bh.admin.pojo.user.ShopCashDeposit;
import com.bh.admin.pojo.user.ShopInfoByBusiness;
import com.bh.admin.pojo.user.ShopInfoByPerson;

public interface MemberShopInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopInfo record);

    int insertSelective(MemberShopInfo record);

    MemberShopInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopInfo record);

    int updateByPrimaryKey(MemberShopInfo record);

	List<ShopInfoByPerson> shopInfoListByPerson(MemberShopInfo entity);

	List<ShopInfoByBusiness> shopInfoListByBusiness(MemberShopInfo entity);

	List<ShopCashDeposit> getShopCashDeposit(MemberShopInfo entity);

	MemberShopInfo getByShopId(Integer getmId);
}