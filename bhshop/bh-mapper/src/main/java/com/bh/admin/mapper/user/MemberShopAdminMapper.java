package com.bh.admin.mapper.user;

import com.bh.admin.pojo.user.MemberShopAdmin;

public interface MemberShopAdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopAdmin record);

    int insertSelective(MemberShopAdmin record);

    MemberShopAdmin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopAdmin record);

    int updateByPrimaryKey(MemberShopAdmin record);
}