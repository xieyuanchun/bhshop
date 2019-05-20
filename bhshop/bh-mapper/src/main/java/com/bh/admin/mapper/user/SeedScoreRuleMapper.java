package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.SeedScoreRule;


public interface SeedScoreRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SeedScoreRule record);

    int insertSelective(SeedScoreRule record);

    SeedScoreRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SeedScoreRule record);

    int updateByPrimaryKey(SeedScoreRule record);
    
    
    
    
    /**************cheng*********/
    List<SeedScoreRule> selectRuleByParams(SeedScoreRule rule);
    
    
}