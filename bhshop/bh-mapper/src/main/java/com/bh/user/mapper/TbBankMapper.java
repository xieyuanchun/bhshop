package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.TbBank;

public interface TbBankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbBank record);

    int insertSelective(TbBank record);

    TbBank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbBank record);

    int updateByPrimaryKey(TbBank record);
    
    List<TbBank> selectTbBankByNo(TbBank re);
    
    TbBank getByBankName(String name);
}