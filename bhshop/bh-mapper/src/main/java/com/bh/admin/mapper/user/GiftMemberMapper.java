package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.GiftMember;

public interface GiftMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftMember record);

    int insertSelective(GiftMember record);

    GiftMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GiftMember record);

    int updateByPrimaryKey(GiftMember record);
    
    List<GiftMember> pageList(GiftMember g);
}