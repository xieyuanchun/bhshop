package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.MemberUser;

public interface MemberUserMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberUser record);

    int insertSelective(MemberUser record);

    MemberUser selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberUser record);

    int updateByPrimaryKey(MemberUser record);
    
    
    MemberUser selectByMId(Integer orderShopId);
    
    List<MemberUser> selectByEmail(String email);
    
    /** ******************  cheng*********************/
    int updateEmailBymId(MemberUser memberUser);
    List<MemberUser> selectMemberUser(MemberUser user);
    List<MemberUser> selectMemberUserByParams(MemberUser memberUser);
    List<MemberUser> selectByFullName(MemberUser memberUser);
    int updatePointBymId(MemberUser memberUser);
    int updateByParams(MemberUser memberUser);
    List<MemberUser> selectUserPoint(Integer mId);
}