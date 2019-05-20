package com.bh.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.NavigationMapper;
import com.bh.admin.pojo.goods.Navigation;
import com.bh.admin.service.NavigationService;
import com.github.pagehelper.PageHelper;


@Service
public class NavigationServiceImpl implements NavigationService{
	@Autowired
	private NavigationMapper navigationMapper;
	
	public List<Navigation> selectList(Integer usingObject,String currentPage,String pageSize)throws Exception{
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		return navigationMapper.selectList(usingObject);
	}
	
	public int insertSelect(Navigation navigation)throws Exception{
		return navigationMapper.insertSelective(navigation);
	}
	public int updateSelect(Navigation navigation)throws Exception{
		return navigationMapper.updateByPrimaryKeySelective(navigation);
	}
}
