package com.bh.admin.mapper.user;

import com.bh.admin.pojo.user.MemberUserAccountLog;

public interface MemberUserAccountLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberUserAccountLog record);

    int insertSelective(MemberUserAccountLog record);

    MemberUserAccountLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberUserAccountLog record);

    int updateByPrimaryKey(MemberUserAccountLog record);
}