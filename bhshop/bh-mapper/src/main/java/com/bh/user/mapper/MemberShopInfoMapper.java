package com.bh.user.mapper;

import java.util.List;

import com.bh.admin.pojo.user.ShopInfoByBusiness;
import com.bh.admin.pojo.user.ShopInfoByPerson;
import com.bh.user.pojo.MemberShopInfo;

public interface MemberShopInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopInfo record);

    int insertSelective(MemberShopInfo record);

    MemberShopInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopInfo record);

    int updateByPrimaryKey(MemberShopInfo record);

	MemberShopInfo selectByOpenidAndType(String openid, int type);

	List <MemberShopInfo> getByOpenid(String openid);

	List<ShopInfoByPerson> shopInfoListByPerson(com.bh.admin.pojo.user.MemberShopInfo entity);

	List<ShopInfoByBusiness> shopInfoListByBusiness(com.bh.admin.pojo.user.MemberShopInfo entity);
}