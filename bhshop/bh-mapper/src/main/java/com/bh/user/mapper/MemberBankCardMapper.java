package com.bh.user.mapper;

import java.util.List;

import com.bh.user.pojo.MemberBankCard;

public interface MemberBankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberBankCard record);

    int insertSelective(MemberBankCard record);

    MemberBankCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberBankCard record);

    int updateByPrimaryKey(MemberBankCard record);
    
    
    //程凤云
    List<MemberBankCard> selectMemberBankCartByParams(MemberBankCard card);
    
    //zlk
    List<MemberBankCard> getByMid(MemberBankCard record);
}