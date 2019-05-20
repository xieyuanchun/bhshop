package com.bh.user.api.service.impl;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bh.user.api.service.WalletService;
import com.bh.user.mapper.MemberBankCardMapper;
import com.bh.user.mapper.TbBankMapper;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.TbBank;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class WalletServiceImpl implements WalletService{
    @Autowired
    private WalletMapper walletMapper;
    
    @Autowired
    private WalletLogMapper walletLogMapper;
    
    @Autowired
    private MemberBankCardMapper memberBankCardMapper;
    
    @Autowired
    private TbBankMapper tbBankMapper;
    
	@Value(value = "${pageSize}")
	private String pageSize;
	@Override
	public int add(Wallet entity) {
		// TODO Auto-generated method stub
		return walletMapper.insertSelective(entity);
	}

	@Override
	public int update(Wallet entity) {
		// TODO Auto-generated method stub
		return walletMapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public Wallet get(Integer id) {
		// TODO Auto-generated method stub
		return walletMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageBean<Wallet> listPage(Wallet entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		// TODO Auto-generated method stub
		List<Wallet> list = walletMapper.listPage(entity);
		PageBean<Wallet> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return walletMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int addWalletLog(WalletLog record) {
		// TODO Auto-generated method stub
		return walletLogMapper.insertSelective(record);
	}

	@Override
	public int updateByUid(Wallet entity) {
		// TODO Auto-generated method stub
		return walletMapper.updateByUid(entity);
	}

	@Override
	public int updateByOrderNo(WalletLog record) {
		// TODO Auto-generated method stub
		return walletLogMapper.updateByOrderNo(record);
	}

	@Override
	public List<Wallet> getByUid(Wallet entity) {
		// TODO Auto-generated method stub
		return walletMapper.getWalletByUid(entity.getUid());
	}

	@Override
	public List<MemberBankCard> getCardByMid(Wallet entity) {
		// TODO Auto-generated method stub
		MemberBankCard  m = new MemberBankCard();
		m.setmId(entity.getUid());
		return memberBankCardMapper.getByMid(m);
	}

	@Override
	public TbBank getByBankName(String name) {
		// TODO Auto-generated method stub
		return tbBankMapper.getByBankName(name);
	}

	
}
