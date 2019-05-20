package com.bh.user.mapper;

import com.bh.user.pojo.MemberUserGroup;

public interface MemberUserGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberUserGroup record);

    int insertSelective(MemberUserGroup record);

    MemberUserGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberUserGroup record);

    int updateByPrimaryKey(MemberUserGroup record);
}