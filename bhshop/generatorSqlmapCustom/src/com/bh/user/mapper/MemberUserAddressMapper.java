package com.bh.user.mapper;

import com.bh.user.pojo.MemberUserAddress;

public interface MemberUserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberUserAddress record);

    int insertSelective(MemberUserAddress record);

    MemberUserAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberUserAddress record);

    int updateByPrimaryKey(MemberUserAddress record);
}