package com.bh.user.api.service;
import java.util.List;

import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.TbBank;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;

public interface WalletService {
   //添加
   int add(Wallet entity);
   //更新
   int update(Wallet entity);
   //获取
   Wallet get(Integer id);
   //列表
   PageBean<Wallet> listPage(Wallet entity);
   //删除
   int delete(Integer id);
 
   //森惠-钱包-充值功能  zlk WalletLog
   int addWalletLog(WalletLog record);
   
   //更新
   int updateByUid(Wallet entity);
   
   //更新WalletLog 状态zlk WalletLog
   int updateByOrderNo(WalletLog record);
   //根据用户id 获取信息
   List<Wallet> getByUid(Wallet entity);
   //根据用户id 获取钱包信息和银行卡信息
   List<MemberBankCard> getCardByMid(Wallet entity);
   
   //获取当前的银行卡logo
   TbBank getByBankName(String name);
}
