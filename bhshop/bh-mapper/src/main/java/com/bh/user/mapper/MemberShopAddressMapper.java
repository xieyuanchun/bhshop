package com.bh.user.mapper;

import com.bh.user.pojo.MemberShopAddress;

public interface MemberShopAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopAddress record);

    int insertSelective(MemberShopAddress record);

    MemberShopAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopAddress record);

    int updateByPrimaryKey(MemberShopAddress record);
}