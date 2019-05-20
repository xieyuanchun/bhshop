package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.MemberUser;


public interface MemberUserMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberUser record);

    int insertSelective(MemberUser record);

    MemberUser selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberUser record);

    int updateByPrimaryKey(MemberUser record);
    
    
    
    List<MemberUser> selectByEmail(String email);
    
    /** ******************  cheng*********************/
    int updateEmailBymId(MemberUser memberUser);
    List<MemberUser> selectMemberUser(MemberUser user);
    List<MemberUser> selectMemberUserByParams(MemberUser memberUser);
    List<MemberUser> selectByFullName(MemberUser memberUser);
    int updatePointBymId(MemberUser memberUser);
    int updateByParams(MemberUser memberUser);
    MemberUser selectUserStatus(Integer mId);
}