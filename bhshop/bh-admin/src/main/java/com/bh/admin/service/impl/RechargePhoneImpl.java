package com.bh.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.order.RechargePhoneMapper;
import com.bh.admin.pojo.order.RechargePhone;
import com.bh.admin.service.RechargePhoneService;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
@Transactional
public class RechargePhoneImpl implements RechargePhoneService{

	@Autowired
	private RechargePhoneMapper mapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	
	@Override
	public int add(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public PageBean<RechargePhone> listPage(RechargePhone r) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(r.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<RechargePhone> list = mapper.listPage(r);
		if(list.size()>0){
			for(RechargePhone p : list){
				double realMoney = (double)p.getAmount()/100;
				p.setAmount2(realMoney+"");
			}
		}
		PageBean<RechargePhone> pageBean = new PageBean<>(list);
		return pageBean;
	}
}
