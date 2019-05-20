package com.bh.product.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsTagMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsTag;
import com.bh.product.api.service.GoodsTagService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class GoodsTagImpl implements GoodsTagService{
	@Autowired
	private GoodsTagMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public int add(GoodsTag entity) throws Exception {
		return mapper.insertSelective(entity);
	}

	@Override
	public int update(GoodsTag entity) throws Exception {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public GoodsTag get(GoodsTag entity) throws Exception {
		return mapper.selectByPrimaryKey(entity.getId());
	}

	@Override
	public int delete(GoodsTag entity) throws Exception {
		int row = 0;
		Goods goods = new Goods();
		goods.setTagIds(entity.getId()+"");
		List<Goods> goodsList = goodsMapper.selectByTagId(goods);
		if(goodsList.size()>0){
			row = 999;
		}else{
			row = mapper.deleteByPrimaryKey(entity.getId());
		}
		return row;
	}

	@Override
	public PageBean<GoodsTag> listPage(GoodsTag entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<GoodsTag> list = mapper.listPage();
		PageBean<GoodsTag> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public List<GoodsTag> selectAll() throws Exception {
		List<GoodsTag> list = mapper.listPage();
		return list;
	}

}
