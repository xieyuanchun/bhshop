package com.bh.user.mapper;

import com.bh.user.pojo.MemberShopAdminLog;

public interface MemberShopAdminLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopAdminLog record);

    int insertSelective(MemberShopAdminLog record);

    MemberShopAdminLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopAdminLog record);

    int updateByPrimaryKey(MemberShopAdminLog record);
}