package com.bh.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xalan.xsltc.compiler.sym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.ExchangeGroupMapper;
import com.bh.admin.pojo.goods.CouponLog;
import com.bh.admin.pojo.goods.ExchangeGroup;
import com.bh.admin.service.ExchangeGroupService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class ExchangeGroupImpl implements ExchangeGroupService {

	@Autowired
	private ExchangeGroupMapper mapper;
	
	
	@Override
	public int add(ExchangeGroup e) {
		// TODO Auto-generated method stub
		e.setAddtime(new Date());
		int row;
		ExchangeGroup exchangeGroup=mapper.selectByName(e.getName());
		if(exchangeGroup!=null) {
			row=999;
		}else {
		    row=mapper.insertSelective(e);
		}
		return row;
	}

	@Override
	public int update(ExchangeGroup e) {
		// TODO Auto-generated method stub
		e.setEdittime(new Date());
		return mapper.updateByPrimaryKeySelective(e);
	}

	@Override
	public Map<String, Object> listPage(ExchangeGroup e) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(e.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExchangeGroup> list = mapper.pageList(e);
	    PageBean<ExchangeGroup> pageBean = new PageBean<>(list);
	    map.put("ExchangeGroup", pageBean);
		return map;
	}

	@Override
	public int delete(ExchangeGroup e) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(e.getId());
	}

	@Override
	public List<ExchangeGroup> list() {
		// TODO Auto-generated method stub
		return mapper.getAll();
	}


}
