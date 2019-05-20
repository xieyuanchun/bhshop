package com.bh.admin.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.user.MemberNotice;


public interface MemberNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MemberNotice record);

    int insertSelective(MemberNotice record);

    MemberNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemberNotice record);

    int updateByPrimaryKey(MemberNotice record);
    
    int insertSelectiveByBatch(@Param("list")List<MemberNotice> list);
    
    List<MemberNotice> selectMemberNoticeList(MemberNotice memberNotice);
}