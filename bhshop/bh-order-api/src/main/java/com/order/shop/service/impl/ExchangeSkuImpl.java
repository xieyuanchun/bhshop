package com.order.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.order.mapper.ExchangeSkuMapper;
import com.bh.order.pojo.ExchangeGood;
import com.order.shop.service.ExchangeSkuService;

@Service
public class ExchangeSkuImpl implements ExchangeSkuService {

	
	@Autowired
	private ExchangeSkuMapper mapper;
	
	
	
	@Override
	public List<ExchangeGood> get(int id) {
		// TODO Auto-generated method stub
		return mapper.get(id);
	}

}
