package com.bh.order.mapper;

import com.bh.order.pojo.MsnApply;

public interface MsnApplyMapper {
    int deleteByPrimaryKey(Integer apymsnId);

    int insert(MsnApply record);

    int insertSelective(MsnApply record);

    MsnApply selectByPrimaryKey(Integer apymsnId);

    int updateByPrimaryKeySelective(MsnApply record);

    int updateByPrimaryKey(MsnApply record);
}