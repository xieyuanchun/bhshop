package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.CashDeposit;

public interface CashDepositMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CashDeposit record);

    int insertSelective(CashDeposit record);

    CashDeposit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CashDeposit record);

    int updateByPrimaryKey(CashDeposit record);


    List<CashDeposit> getCashDeposit(CashDeposit findCashDeposit);
}