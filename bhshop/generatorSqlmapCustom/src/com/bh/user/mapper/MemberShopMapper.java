package com.bh.user.mapper;

import com.bh.user.pojo.MemberShop;

public interface MemberShopMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberShop record);

    int insertSelective(MemberShop record);

    MemberShop selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberShop record);

    int updateByPrimaryKeyWithBLOBs(MemberShop record);

    int updateByPrimaryKey(MemberShop record);
}