package com.bh.user.api.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bh.user.api.service.WalletWithdrawService;
import com.bh.user.mapper.WalletWithdrawMapper;
import com.bh.user.pojo.WalletWithdraw;
@Service
public class WalletWithdrawServiceImpl implements WalletWithdrawService{
    @Autowired
    WalletWithdrawMapper  walletWithdrawMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	@Override
	public int add(WalletWithdraw entity) {
		// TODO Auto-generated method stub
		return walletWithdrawMapper.insertSelective(entity);
	}
}
