package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.AdsRelation;


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