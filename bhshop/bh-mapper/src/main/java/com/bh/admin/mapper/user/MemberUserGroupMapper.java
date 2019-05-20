package com.bh.admin.mapper.user;

import com.bh.admin.pojo.user.MemberUserGroup;

public interface MemberUserGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberUserGroup record);

    int insertSelective(MemberUserGroup record);

    MemberUserGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberUserGroup record);

    int updateByPrimaryKey(MemberUserGroup record);
}