package com.bh.user.mapper;

import com.bh.user.pojo.Areas;

public interface AreasMapper {
    int deleteByPrimaryKey(Integer areaId);

    int insert(Areas record);

    int insertSelective(Areas record);

    Areas selectByPrimaryKey(Integer areaId);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);
}