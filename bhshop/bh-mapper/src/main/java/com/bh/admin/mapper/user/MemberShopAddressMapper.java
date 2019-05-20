package com.bh.admin.mapper.user;

import com.bh.admin.pojo.user.MemberShopAddress;

public interface MemberShopAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopAddress record);

    int insertSelective(MemberShopAddress record);

    MemberShopAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopAddress record);

    int updateByPrimaryKey(MemberShopAddress record);
}