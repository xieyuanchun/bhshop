package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsTagMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsTag;
import com.bh.admin.service.GoodsTagService;
import com.bh.config.Contants;
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
		row = mapper.deleteByPrimaryKey(entity.getId());
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
