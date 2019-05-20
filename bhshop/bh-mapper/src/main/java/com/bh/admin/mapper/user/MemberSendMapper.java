package com.bh.admin.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.user.MemberSend;


public interface MemberSendMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(MemberSend record);

    int insertSelective(MemberSend record);

    MemberSend selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(MemberSend record);

    int updateByPrimaryKey(MemberSend record);
    
    
    /****************scj*******************/
    List<MemberSend> pageList(String name, @Param("status") String status, @Param("type") String type);
    
    
    
    /****************************************** 以下是chengfengyun***************************************************************/
    List<MemberSend> selectAllSendByStatus(Integer pageSize,Integer currentPage);
    int selectAllSendByStatusCount();
    int updateSendStatus(MemberSend memberSend);
    List<MemberSend> selectMemberSendByParams(MemberSend memberSend);
}