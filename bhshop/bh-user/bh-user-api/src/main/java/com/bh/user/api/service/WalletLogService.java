package com.bh.user.api.service;
import java.util.List;

import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;

public interface WalletLogService {

	   //森惠-钱包-充值功能  zlk WalletLog
	   int addWalletLog(WalletLog record);
	   
	   //更新WalletLog 状态zlk WalletLog
	   int updateByOrderNo(WalletLog record);
	   //根据用户id获取信息
	   List<WalletLog> getByMid(WalletLog walletLog);
	   //列表
	   PageBean<WalletLog> listPage(WalletLog entity);
	   
	   
	   PageBean<WalletLog> WalletRecord(WalletLog entity);
}
