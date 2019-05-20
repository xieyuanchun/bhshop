package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.PromoteUser;

public interface PromoteUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromoteUser record);

    int insertSelective(PromoteUser record);

    PromoteUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromoteUser record);

    int updateByPrimaryKey(PromoteUser record);
    
    
    List<PromoteUser> selectAllIds();
    
    //cheng
    List<PromoteUser> selectAllPUser(PromoteUser entity);
}