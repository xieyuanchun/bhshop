package com.bh.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.ExchangeSkuMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.pojo.goods.ExchangeGroup;
import com.bh.admin.pojo.goods.ExchangeSku;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.service.ExchangeSkuService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class ExchangeSkuImpl implements ExchangeSkuService {

	@Autowired
	private ExchangeSkuMapper mapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	
	@Override
	public int add(ExchangeSku e) {
		// TODO Auto-generated method stub
		e.setAddtime(new Date());
		return mapper.insertSelective(e);
	}

	@Override
	public int update(ExchangeSku e) {
		// TODO Auto-generated method stub
		e.setEdittime(new Date());
		return mapper.updateByPrimaryKeySelective(e);
	}

	@Override
	public Map<String,Object> listPage(ExchangeSku e) {
		PageHelper.startPage(Integer.parseInt(e.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		List<ExchangeSku> list = mapper.pageList(e);
	    PageBean<ExchangeSku> pageBean = new PageBean<>(list);
	    map.put("ExchangeSku", pageBean);
		return map;
	}

	@Override
	public int delete(ExchangeSku e) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(e.getId());
	}

	@Override
	public List<GoodsSku> getByjdsku(GoodsSku g) {
		// TODO Auto-generated method stub
		return goodsSkuMapper.getByJdSkuNo(g);
	}

}
