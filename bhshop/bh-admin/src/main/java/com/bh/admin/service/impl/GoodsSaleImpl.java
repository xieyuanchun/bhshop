package com.bh.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.goods.GoodsSalePMapper;
import com.bh.admin.pojo.goods.GoodsSaleP;
import com.bh.admin.service.GoodsSaleService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
@Transactional
public class GoodsSaleImpl implements GoodsSaleService{

	@Autowired
	private GoodsSalePMapper goodsSalePMapper;
	
	
	@Override
	public int add(GoodsSaleP g) {
		// TODO Auto-generated method stub
		return goodsSalePMapper.insertSelective(g);
	}

	@Override
	public int update(GoodsSaleP g) {
		// TODO Auto-generated method stub
		return goodsSalePMapper.updateByPrimaryKeySelective(g);
	}

	@Override
	public int delete(GoodsSaleP g) {
		// TODO Auto-generated method stub
		return goodsSalePMapper.deleteByPrimaryKey(g.getId());
	}

	@Override
	public Map<String, Object> list(GoodsSaleP g) {
		// TODO Auto-generated method stub
		 PageHelper.startPage(Integer.parseInt(g.getCurrentPage()), Contants.PAGE_SIZE, true);
		
		 List<GoodsSaleP> list =  goodsSalePMapper.selectAll(g);
		 for(int i=0;i<list.size();i++){
			 
			list.get(i).setMin(list.get(i).getMin()/100); //把金额分转换成元
			
			list.get(i).setMax(list.get(i).getMax()/100); //把金额分转换成元
		 }
		 PageBean<GoodsSaleP> pageBean = new PageBean<>(list);
	     Map<String,Object> map = new HashMap<String,Object>();
	     map.put("GoodsSaleP", pageBean);
	     return map;
	     
	}

	@Override
	public List<GoodsSaleP> get(GoodsSaleP g) {
		// TODO Auto-generated method stub
		return goodsSalePMapper.selectAll(g);
	}

}
