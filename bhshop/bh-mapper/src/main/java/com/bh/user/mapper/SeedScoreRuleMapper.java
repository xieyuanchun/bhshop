package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.SeedScoreRule;

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