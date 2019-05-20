package com.bh.user.mapper;

import com.bh.user.pojo.MergeUserLog;

public interface MergeUserLogMapper {
    int deleteByPrimaryKey(Integer mergeId);

    int insert(MergeUserLog record);

    int insertSelective(MergeUserLog record);

    MergeUserLog selectByPrimaryKey(Integer mergeId);

    int updateByPrimaryKeySelective(MergeUserLog record);

    int updateByPrimaryKey(MergeUserLog record);
}