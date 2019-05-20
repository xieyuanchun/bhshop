package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.GiftMember;

public interface GiftMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftMember record);

    int insertSelective(GiftMember record);

    GiftMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GiftMember record);

    int updateByPrimaryKey(GiftMember record);

	List <GiftMember> getByGiftIdAndMid(Integer id, int mId);
}