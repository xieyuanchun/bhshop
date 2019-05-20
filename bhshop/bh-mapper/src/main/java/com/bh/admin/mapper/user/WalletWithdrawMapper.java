package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.WalletWithdraw;


public interface WalletWithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletWithdraw record);

    int insertSelective(WalletWithdraw record);

    WalletWithdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletWithdraw record);

    int updateByPrimaryKey(WalletWithdraw record);
    
    List<WalletWithdraw> listPage(WalletWithdraw record);
}