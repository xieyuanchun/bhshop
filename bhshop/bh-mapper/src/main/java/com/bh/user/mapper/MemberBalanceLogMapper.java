package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.MemberBalanceLog;

public interface MemberBalanceLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberBalanceLog record);

    int insertSelective(MemberBalanceLog record);

    MemberBalanceLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberBalanceLog record);

    int updateByPrimaryKey(MemberBalanceLog record);
    
    
    
    List<MemberBalanceLog> listPage(MemberBalanceLog record);
}