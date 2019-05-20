package com.bh.admin.mapper.user;

import java.util.List;

import com.bh.admin.pojo.user.WalletLog;


public interface WalletLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletLog record);

    int insertSelective(WalletLog record);

    WalletLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletLog record);

    int updateByPrimaryKey(WalletLog record);
    
    int updateByOrderNo(WalletLog record);
    //根据orderNo获取订 WalletLog 的金额
    WalletLog getByOrderNo(WalletLog record);
    //根据用户id获取信息
	List<WalletLog> getByMid(WalletLog walletLog);
	//列表管理
	 List<WalletLog> listPage(WalletLog entity);
	 
	 
	 //个人钱包记录
	 List<WalletLog> WalletRecord(WalletLog entity);
}