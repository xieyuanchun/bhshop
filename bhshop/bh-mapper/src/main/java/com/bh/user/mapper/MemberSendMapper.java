package com.bh.user.mapper;

import java.util.List;


import com.bh.user.pojo.MemberSend;

public interface MemberSendMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberSend record);

    int insertSelective(MemberSend record);

    MemberSend selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberSend record);

    int updateByPrimaryKey(MemberSend record);
       
    
    
    /****************************************** 以下是chengfengyun***************************************************************/
    List<MemberSend> selectAllSendByStatus(Integer pageSize,Integer currentPage);
    int selectAllSendByStatusCount();
    int updateSendStatus(MemberSend memberSend);
    List<MemberSend> selectMemberSendByParams(MemberSend memberSend);
}