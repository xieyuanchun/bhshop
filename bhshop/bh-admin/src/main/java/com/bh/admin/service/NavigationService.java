package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.Navigation;

public interface NavigationService {

	List<Navigation> selectList(Integer usingObject,String currentPage,String pageSize) throws Exception;
	int insertSelect(Navigation navigation)throws Exception;
	int updateSelect(Navigation navigation)throws Exception;
}
