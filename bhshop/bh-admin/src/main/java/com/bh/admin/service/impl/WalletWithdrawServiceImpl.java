package com.bh.admin.service.impl;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.user.MemberBankCardMapper;
import com.bh.admin.mapper.user.WalletLogMapper;
import com.bh.admin.mapper.user.WalletMapper;
import com.bh.admin.mapper.user.WalletWithdrawMapper;
import com.bh.admin.pojo.user.Wallet;
import com.bh.admin.pojo.user.WalletLog;
import com.bh.admin.pojo.user.WalletWithdraw;
import com.bh.admin.service.WalletWithdrawService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class WalletWithdrawServiceImpl implements WalletWithdrawService{
    @Autowired
    WalletWithdrawMapper  walletWithdrawMapper;
    @Autowired
    private WalletMapper walletMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	

	@Override
	public PageBean<WalletWithdraw> listPage(WalletWithdraw entity) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		
		List<WalletWithdraw> list  = walletWithdrawMapper.listPage(entity);
		for(int i=0;i<list.size();i++){
			//转换金额
			double money = list.get(i).getAmount();
			list.get(i).setMoney2(money/10/10+"");

		}
		PageBean<WalletWithdraw> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int update(WalletWithdraw entity) {
		// TODO Auto-generated method stub
		WalletLog record = new WalletLog();
		record.setId(entity.getWalletLogId());
		if(entity.getStatus()==1){//通过申请
		    record.setStatus(1);
		}else if(entity.getStatus()==2){//拒绝申请
			record.setStatus(2);
			WalletWithdraw w = walletWithdrawMapper.selectByPrimaryKey(entity.getId());
			List<Wallet> list = walletMapper.getWalletByUid(w.getmId()); //获取Wallet信息
			list.get(0).setMoney(list.get(0).getMoney()+entity.getAmount()); //返回金额
			walletMapper.updateByPrimaryKeySelective(list.get(0)); //更改金额
		}
		walletLogMapper.updateByPrimaryKeySelective(record); //修改walletLog信息

		return walletWithdrawMapper.updateByPrimaryKeySelective(entity); //更改walletWithdraw表
	}

}
