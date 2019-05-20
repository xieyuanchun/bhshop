package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsPropertyMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.GoodsProperty;
import com.bh.goods.pojo.GoodsSku;
import com.bh.product.api.service.GoodsPropertyService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class GoodsPropertyImpl implements GoodsPropertyService{
	@Autowired
	private GoodsPropertyMapper mapper;


	@Override
	public int add(GoodsProperty entity) throws Exception {
		int row =0;
		entity.setAddTime(new Date());
		List<GoodsProperty> list = mapper.selectGoodsPro(entity);
		if (list.size()>0) {
			row = 2;
		}else{
			row = mapper.insertSelective(entity);
		}
		return row;
	}

	@Override
	public int update(GoodsProperty entity) throws Exception {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public GoodsProperty get(GoodsProperty entity) throws Exception {
		return mapper.selectByPrimaryKey(entity.getId());
	}

	@Override
	public int delete(GoodsProperty entity) throws Exception {
		return mapper.deleteByPrimaryKey(entity.getId());
	}

	@Override
	public PageBean<GoodsProperty> listPage(GoodsProperty entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<GoodsProperty> list = mapper.listPage();
		PageBean<GoodsProperty> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public List<GoodsProperty> selectByType(GoodsProperty entity) throws Exception {
		List<GoodsProperty> list = mapper.selectByType(entity);
		return list;
	}
	
	public List<GoodsProperty> selectAllPro(GoodsProperty enGoodsProperty) throws Exception{
		List<GoodsProperty> list = new ArrayList<>();
		list = mapper.selectGoodsPro(enGoodsProperty);
		return list;
	}

}
