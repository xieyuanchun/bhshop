package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.Menu;
import com.bh.utils.PageBean;

public interface MenuService {
	
	/*菜单的新增*/
	int addMenu(String name, String image, String content, String link, String sortnum, String series, String adsId, String reid) throws Exception;
	
	/*菜单的修改*/
	int editMenu(String id, String name, String image, String content, String link, String sortnum, String adId, String series, String reid) throws Exception;
	
	/*菜单分页列表*/
	PageBean<Menu> menuPageList(String currentPage, int pageSize) throws Exception;
	
	/*批量删除*/
	int batchDelete(String id) throws Exception;
	
	/*后台根据父类id获取下级分页列表*/
	PageBean<Menu> getListByReid(String currentPage, int pageSize, String reid) throws Exception;
	
	/*移动端获取所有一级菜单*/
	List<Menu> menuList() throws Exception;
	
	/*移动端根据一级菜单获取二级菜单*/
	List<Menu> menuTwoLevelList(String reid) throws Exception;
	
	/*获取所有菜单列表(树形结构)*/
	List<Menu> selectLinkedList()throws Exception;
	
	
}
