package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.BusBankCard;


public interface BusBankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusBankCard record);

    int insertSelective(BusBankCard record);

    BusBankCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusBankCard record);

    int updateByPrimaryKey(BusBankCard record);
    
    
    List<BusBankCard> selectMemberBankCartByParams(BusBankCard card);
}