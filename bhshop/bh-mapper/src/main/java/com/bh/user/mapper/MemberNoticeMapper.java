package com.bh.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.user.pojo.MemberNotice;

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