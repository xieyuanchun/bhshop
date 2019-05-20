package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.AdsRelation;

public interface AdsRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdsRelation record);

    int insertSelective(AdsRelation record);

    AdsRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdsRelation record);

    int updateByPrimaryKey(AdsRelation record);
    
    
    
    List<AdsRelation> selectByTargetId(Integer targetId);
    
    AdsRelation selectByAdsId(Integer adsId);
}