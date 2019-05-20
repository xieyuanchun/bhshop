package com.bh.user.mapper;

import java.util.List;
import java.util.Map;

import com.bh.user.pojo.Member;

public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    int insertSelective(Member record);

    Member selectByPrimaryKey(Integer id);
    List<Member> selectAll();

    int updateByPrimaryKeySelective(Member record);

    int updateByPrimaryKey(Member record);
    
    
    /******SCJ***********/
    List<Member> orderArrange();
    
    Member selectHeadAndName(Integer id);
    
    List<Member> selectByPhone(String phone);
    
    
    
    
   //*******************************************************以下是chengfengyun的code***************************************************//
    //检查用户是否存在
    int checkuser(Member member);
    
    int insertuser(Member member);
    
    List<Member> login (Member member);
    
    int updatepwd(Member member);
    
    //2017-9-28通过member的phone找该member
    List<Member> selecMemberByPhone(Member member);
    List<Member> selectByParam(Member member);
    int selectCountByPhone(Member member);
    //2018-4-23修改用户昵称
    int selectUsername(Member member);
    Member selectMemberMsg(Integer id);

    //zlk 2018.3.18
    List<Member> selectByOpenId(Member member);
    
    //zlk 2018.4.10
    List<Member> selectByUnionid(Member member);

	List<Member> getByPhone(String newPhone);


	void mergeUserByWxAndPhone(int old_mId, int new_mId);
}