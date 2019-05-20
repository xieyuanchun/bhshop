package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.TbBank;


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