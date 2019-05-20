package com.bh.admin.service;


import com.bh.admin.pojo.user.WalletWithdraw;
import com.bh.utils.PageBean;

public interface WalletWithdrawService {
    //列表
    PageBean<WalletWithdraw> listPage(WalletWithdraw entity);
    //修改
  	int update(WalletWithdraw entity);
}
