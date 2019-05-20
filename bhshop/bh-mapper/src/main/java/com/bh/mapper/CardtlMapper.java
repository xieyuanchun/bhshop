package com.bh.mapper;

import com.bh.pojo.Cardtl;

public interface CardtlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cardtl record);

    int insertSelective(Cardtl record);

    Cardtl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cardtl record);

    int updateByPrimaryKey(Cardtl record);
}