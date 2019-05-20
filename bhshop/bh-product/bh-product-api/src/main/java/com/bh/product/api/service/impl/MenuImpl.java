package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.goods.mapper.AdsMapper;
import com.bh.goods.mapper.AdsRelationMapper;
import com.bh.goods.mapper.MenuMapper;
import com.bh.goods.pojo.Ads;
import com.bh.goods.pojo.AdsRelation;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.Menu;
import com.bh.product.api.service.MenuService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class MenuImpl implements MenuService{
	@Autowired
	private MenuMapper mapper;
	@Autowired
	private AdsRelationMapper  adsRelationmapper;
	@Autowired
	private AdsMapper  adsMapper;
	
	/**
	 * 菜单的新增
	 */
	@Override
	public int addMenu(String name, String image, String content, String link, String sortnum, String series, String adsId, String reid) throws Exception {
		Menu menu = new Menu();
		menu.setCreatetime(new Date());
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			menu.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			menu.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			menu.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			menu.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			menu.setSortnum(Integer.parseInt(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(series)){
			menu.setSeries(Integer.parseInt(series));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(reid)){
			menu.setReid(Integer.parseInt(reid));
		}
		int row = mapper.insertSelective(menu);
		
		String [] stringArr= adsId.toString().split(",");
		for(int i=0; i<stringArr.length; i++){
			AdsRelation adsRelation = new AdsRelation();
			adsRelation.setAdsId(Integer.parseInt(stringArr[i]));
			adsRelation.setAdsType(Contants.adsType); //2菜单广告
			adsRelation.setTargetId(menu.getId());
			adsRelationmapper.insertSelective(adsRelation);
			
			Ads ads = adsMapper.selectByPrimaryKey(Integer.parseInt(stringArr[i]));
			ads.setStatus(1); //使用中
			adsMapper.updateByPrimaryKeySelective(ads);
		}	
		return row;
	}
	
	/**
	 * 移动端获取所有一级菜单
	 */
	@Override
	public List<Menu> menuList() throws Exception {
		List<Menu> list = mapper.selectFirstLevel();
		return list;
	}
	
	/**
	 * 菜单的修改
	 */
	@Override
	public int editMenu(String id, String name, String image, String content, String link, String sortnum, String adId, String series, String reid)
			throws Exception {
		Menu menu = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			menu.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			menu.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			menu.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			menu.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			menu.setSortnum(Integer.parseInt(sortnum));
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(series)){
			menu.setSeries(Integer.parseInt(series));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(reid)){
			menu.setReid(Integer.parseInt(reid));
		}
		
		int row = mapper.updateByPrimaryKeySelective(menu);
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(adId)){
			List<AdsRelation> list = adsRelationmapper.selectByTargetId(Integer.parseInt(id));
			if(list.size()>0){
				for(AdsRelation relation : list){
					adsRelationmapper.deleteByPrimaryKey(relation.getId());
					
					Ads ads = adsMapper.selectByPrimaryKey(relation.getAdsId());
					ads.setStatus(0); //正常
					adsMapper.updateByPrimaryKeySelective(ads);
				}
			}
			String [] stringArr= adId.toString().split(",");
			for(int i=0; i<stringArr.length; i++){
				AdsRelation adsRelation = new AdsRelation();
				adsRelation.setAdsId(Integer.parseInt(stringArr[i]));
				adsRelation.setAdsType(Contants.adsType); //2菜单广告
				adsRelation.setTargetId(menu.getId());
				adsRelationmapper.insertSelective(adsRelation);
				
				Ads ads = adsMapper.selectByPrimaryKey(Integer.parseInt(stringArr[i]));
				ads.setStatus(1); //使用中
				adsMapper.updateByPrimaryKeySelective(ads);
			}	
		}
		
		return row;
	}
	
	/**
	 * 菜单分页列表
	 */
	@Override
	public PageBean<Menu> menuPageList(String currentPage, int pageSize) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Menu> list = mapper.getFirstLevel();
		if(list.size()>0){
			nextList(list);
		}
		PageBean<Menu> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	public List<Menu> nextList(List<Menu> list){
		for(Menu menu: list){
			StringBuffer buffer = new StringBuffer();
			List<AdsRelation> relationList = adsRelationmapper.selectByTargetId(menu.getId());
			if(relationList.size()>0){
				for(AdsRelation relation : relationList){
					buffer.append(relation.getAdsId()+",");
				}
				String str = buffer.toString().substring(0, buffer.toString().length()-1);
				menu.setAdStr(str); //设置关联的广告id
			}
			
			List<Menu> childList = mapper.getListByReid(menu.getId());
			if(childList.size()>0){
				for(Menu menuTwo : childList){
					menuTwo.setParentName(menu.getName()); //设置父级名称
				}
				menu.setChildList(childList);
				nextList(childList);
			}
		}
		return list;
	}
	
	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public int batchDelete(String id) throws Exception{
		int row = 0;
		List<String> result = Arrays.asList(id.split(",")); //string转list
		for(int j=0; j<result.size(); j++){ //判断所选菜单是否有下级
			List<Menu> menuList = mapper.getListByReid(Integer.parseInt(result.get(j)));
			if(menuList.size()>0){
				row = 998;
				return row ; 
			}
		}
		
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
	         list.add(str[i]);
	         List<AdsRelation> relationList = adsRelationmapper.selectByTargetId(Integer.parseInt(str[i]));
			 if(list.size()>0){
				for(AdsRelation relation : relationList){
					adsRelationmapper.deleteByPrimaryKey(relation.getId());
					Ads ads = adsMapper.selectByPrimaryKey(relation.getAdsId());
					ads.setStatus(0); //0正常
					adsMapper.deleteByPrimaryKey(ads.getId());
				}
			}
		}
		row = mapper.batchDelete(list);
		return row;
	}
	
	/**
	 * 移动端根据一级菜单获取二级菜单
	 */
	@Override
	public List<Menu> menuTwoLevelList(String reid) throws Exception {
		return mapper.selectTwoLevel(Integer.parseInt(reid));
	}
	
	/**
	 * 后台根据父类id获取下级分页列表
	 */
	@Override
	public PageBean<Menu> getListByReid(String currentPage, int pageSize, String reid) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Menu> list = mapper.getListByReid(Integer.parseInt(reid));
		PageBean<Menu> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 获取所有菜单列表
	 */
	@Override
	public List<Menu> selectLinkedList() throws Exception {
		List<Menu> list = mapper.getListByReid(0);
		if(list.size()>0){
			next(list);
		}
		return list;
	}
	
	private List<Menu> next(List<Menu> list){
		for(Menu entity : list){
			List<Menu> m = mapper.getListByReid(entity.getId());
			if(m.size()>0){
				entity.setChildList(m);
				nextList(m);
			}
		}
		return list;
	}
	
}
