package com.bh.user.mapper;

import com.bh.user.pojo.MemberUser;

public interface MemberUserMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberUser record);

    int insertSelective(MemberUser record);

    MemberUser selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberUser record);

    int updateByPrimaryKey(MemberUser record);
}