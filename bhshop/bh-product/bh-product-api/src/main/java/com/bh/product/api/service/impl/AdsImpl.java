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
import com.bh.goods.pojo.Menu;
import com.bh.product.api.service.AdsService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class AdsImpl implements AdsService{
	@Autowired
	private AdsMapper mapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private AdsRelationMapper adsRelationMapper;
	
	/**
	 * 移动端二级页面广告
	 */
	@Override
	public List<Ads> selectById(String id) {
		List<AdsRelation> relationList = adsRelationMapper.selectByTargetId(Integer.parseInt(id));
		StringBuffer buffer = new StringBuffer();
		String adIds = null;
		if(relationList.size()>0){
			for(AdsRelation relation : relationList){
				buffer.append(relation.getAdsId()+",");
			}
			adIds = buffer.toString().substring(0,buffer.toString().length()-1);
		}
		List<String> list = new ArrayList<String>();
		if(adIds!=null){
			String[] str = adIds.split(",");
			for (int i = 0; i < str.length; i++) {
			         list.add(str[i]);	
			}
		}else{
			return null;
		}
		return mapper.batchSelect(list);
	}
	
	/**
	 * 首页广告轮播图
	 */
	@Override
	public List<Ads> selectListByIsMain(String isPc) throws Exception {
		List<Ads> list = mapper.selectListByIsMain(Integer.parseInt(isPc));
		return list;
	}
	
	/**
	 * 后台广告管理列表
	 */
	@Override
	public PageBean<Ads> pageList(String isPc, String isMain, String name, String fz, String currentPage, int pageSize) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<Ads> list = null;
		list = mapper.pageList(isPc, isMain, name, fz);
		PageBean<Ads> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 广告的新增
	 */
	@Override
	public int addAds(String name, String image, String content, String link,String sLink,String sortnum, String isMain, String isPc)
			throws Exception {
		Ads ads = new Ads();
		ads.setCreatetime(new Date());
		ads.setStatus(0); //初始化，正常
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			ads.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			ads.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			ads.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			ads.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sLink)){
			ads.setsLink(sLink);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			List<Ads> adsList = mapper.selectListBySortNum(Integer.parseInt(sortnum));
			if(adsList.size()>0){
				List<Ads> list = mapper.selectAll(); //获取所有排序号，从小到大排序
				if(list.size()>0){
					for(int i=0; i<list.size()-1; i++){
						if(list.get(i).getSortnum()+1<list.get(i+1).getSortnum()){ //原有排序找空位插入
							adsList.get(0).setSortnum(list.get(i).getSortnum()+1);
							break;
						}else{
							adsList.get(0).setSortnum(list.get(list.size()-1).getSortnum()+1);
						}
					}
					mapper.updateByPrimaryKeySelective(adsList.get(0));
				}
			}
			ads.setSortnum(Integer.parseInt(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isMain)){
			ads.setIsMain(Byte.parseByte(isMain));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPc)){
			ads.setIsPc(Integer.parseInt(isPc));
		}
		return mapper.insertSelective(ads);
	}
	
	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public String batchDelete(String id) throws Exception{
		int row = 0;
		String name = null;
		String msg = null;
		int reid = 0;
		StringBuffer buffer = new StringBuffer("该广告已被");
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		for(String ids : list){
			List<Menu> menuList = menuMapper.selectByAdid(Integer.parseInt(ids));
			if(menuList.size()==0){
				row = mapper.deleteByPrimaryKey(Integer.parseInt(ids));
				if(row>0){
					msg = "删除成功";
				}else{
					msg = "删除失败";
				}
				return msg;
			}else{
				for(int i=0; i<menuList.size(); i++){
					name = menuList.get(0).getName();
					reid = menuList.get(0).getReid();
				}
				if(reid>0){
					Menu menu = menuMapper.selectByPrimaryKey(reid);
					buffer.append(menu.getName());
					buffer.append("下的");
				}
				buffer.append(name);
				buffer.append("菜单所绑定");
				msg = buffer.toString();
			}
		}
		return msg;
	}
	
	public int compare(String strOne, String strTwo){ //比较两个字符串是否完全不同
		String[] arr1 = strOne.split(",") ;
        String[] arr2 = strTwo.split(",") ; 	
		for(int i=0; i<arr1.length; i++){
			for (int j = 0; j < arr2.length; j++) {
				if( Integer.parseInt(arr1[i]) == Integer.parseInt(arr2[j])){
					return Integer.parseInt(arr2[j]);
				}
			}
		}
		return 0;
	}
	
	/**
	 * 添加菜单时加载广告列表
	 */
	@Override
	public List<Ads> getList(String menuId) throws Exception {
		List<Ads> list = mapper.getList();
		if(!StringUtils.isEmptyOrWhitespaceOnly(menuId)){
			List<AdsRelation> relationList = adsRelationMapper.selectByTargetId(Integer.parseInt(menuId));
			if(relationList.size()>0){
				for(AdsRelation relation : relationList){
					Ads ads = mapper.selectByPrimaryKey(relation.getAdsId());
					list.add(ads);
				}
			}
		}
		return list;
	}
	
	/**
	 * 广告的编辑
	 */
	@Override
	public int editAds(String id, String name, String image, String content, String link,String sLink, String sortnum, String isMain, String isPc)
			throws Exception {
		int row = 0;
		Ads ads = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			ads.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			ads.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			ads.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			ads.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sLink)){
			ads.setsLink(sLink);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum) && ads.getSortnum()!=Integer.parseInt(sortnum)){
			List<Ads> adsList = mapper.selectListBySortNum(Integer.parseInt(sortnum));
			if(adsList.size()>0){
				List<Ads> list = mapper.selectAll(); //获取所有排序号，从小到大排序
				if(list.size()>0){
					for(int i=0; i<list.size()-1; i++){
						if(list.get(i).getSortnum()+1<list.get(i+1).getSortnum()){ //原有排序找空位插入
							adsList.get(0).setSortnum(list.get(i).getSortnum()+1);
							break;
						}else{
							adsList.get(0).setSortnum(list.get(list.size()-1).getSortnum()+1);
						}
					}
					mapper.updateByPrimaryKeySelective(adsList.get(0));
				}
			}
			ads.setSortnum(Integer.parseInt(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isMain)){
			if(ads.getIsMain()==Byte.parseByte(isMain)){ //正常情况
				ads.setIsMain(Byte.parseByte(isMain));
			}else{ 
				if(ads.getIsMain()==0){ //pc改移动
					ads.setStatus(0); //0正常
					ads.setIsMain(Byte.parseByte(isMain));
				}else{ //移动改pc
					ads.setStatus(0); //0正常
					ads.setIsMain(Byte.parseByte(isMain));
					AdsRelation relation = adsRelationMapper.selectByAdsId(ads.getId());
					if(relation!=null){
						adsRelationMapper.deleteByPrimaryKey(relation.getId());
					}
				}
			}
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPc)){
			ads.setIsPc(Integer.parseInt(isPc));
		}
		row = mapper.updateByPrimaryKeySelective(ads);
		return row;
	}
	
	/**
	 * 广告的删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteAds(String id) throws Exception {
		int row = 0;
		AdsRelation relation = adsRelationMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(relation!=null){
			adsRelationMapper.deleteByPrimaryKey(relation.getId());
		}
		row = mapper.deleteByPrimaryKey(Integer.parseInt(id));	
		return row;
	}
	
	/**
	 * 广告的禁用和启用
	 */
	@Override
	public int startAds(String id, String status) throws Exception {
		int row = 0;
		AdsRelation relation = adsRelationMapper.selectByAdsId(Integer.parseInt(id));
		if(relation!=null){
			adsRelationMapper.deleteByPrimaryKey(relation.getId());
		}
		Ads ads = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(ads!=null){
			ads.setStatus(Integer.parseInt(status));
			row = mapper.updateByPrimaryKeySelective(ads);
		}
		return row;
	}

	/**
	 * 添加广告加载排序号
	 */
	@Override
	public List<String> getSortNum() throws Exception {
		StringBuffer buffer = new StringBuffer();
		for(int i=1; i<Contants.SORTNUM_FIFTY; i++){ //序号原集-50
			buffer.append(i+",");
		}
		String s2 = buffer.toString().substring(0,buffer.toString().length()-1);
		String s1 = null;
		List<String> str = null;
		StringBuffer listBuffer = new StringBuffer();
		List<Ads> list = mapper.selectAll();
		if(list.size()>0){
			for(Ads ads : list){
				listBuffer.append(ads.getSortnum()+",");
			}
			s1 = listBuffer.toString().substring(0, listBuffer.toString().length()-1);
		}
		
		if(s1==null){
			str = Arrays.asList(s2.split(","));
		}else{
			String[] arr1 = s2.split(",");
	        String arr2[] = s1.split(","); 
	        
	        for (int i = 0; i < arr2.length; i++){
	            for (int j = 0; j < arr1.length; j++){
	                if (arr1[j].equals(arr2[i])){
	                    arr1[j] = "" ;
	                }
	            }
	        }
	        StringBuffer sb = new StringBuffer();
	        for (int j = 0; j < arr1.length; j++){
	            if (!"".equals(arr1[j]) ){
	                sb.append(arr1[j] + ",");
	            }
	        }
	        String end = sb.toString().substring(0, sb.toString().length()-1);
	        str = Arrays.asList(end.split(","));
		}
		return str;
	}
	
	/**
	 * 修改广告时加载排序号
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<String> getChangeSortNum() throws Exception {
		StringBuffer buffer = new StringBuffer();
		for(int i=1; i<Contants.SORTNUM_FIFTY; i++){ //序号原集-50
			buffer.append(i+",");
		}
		String s2 = buffer.toString().substring(0,buffer.toString().length()-1);
		List<String> str = Arrays.asList(s2.split(","));
		return str;
	}
	
	/**
	 * 当前最大排序号
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getBigSortNum() throws Exception {
		int sortnum = 0;
		List<Ads> list = mapper.getListOrderbySortNum();
		if(list.size()>0){
			sortnum = list.get(0).getSortnum();
		}
		return sortnum;
	}
	
}
