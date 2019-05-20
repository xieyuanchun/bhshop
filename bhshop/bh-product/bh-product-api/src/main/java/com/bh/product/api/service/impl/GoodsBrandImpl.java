package com.bh.product.api.service.impl;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoodsBrandMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.product.api.service.GoodsBrandService;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class GoodsBrandImpl implements GoodsBrandService{
	@Autowired
	private GoodsBrandMapper mapper;
	@Autowired
	private GoodsCategoryMapper categoryMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	/**
	 * 根据分类Id查询所有品牌
	 */
	@Override
	public List<GoodsBrand> selectByCatid(String catId) throws Exception {
		List<GoodsBrand> list = null;
		list = mapper.selectByCatid(Long.parseLong(catId));
		return list;
	}
	
	/**
	 * 获取所有品牌列表
	 */
	@Override
	public PageBean<GoodsBrand> pageAll(String name, String currentPage, String pageSize) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<GoodsBrand> list = mapper.selectPageList(name);
		if(list.size()>0){
			for(GoodsBrand goodsBrand : list){
				String str = goodsBrand.getCatId();
				List<String> result = Arrays.asList(str.split(",")); //string转list
				StringBuffer buffer = new StringBuffer();
				for(int i=0; i<result.size(); i++){
					GoodsCategory category = categoryMapper.selectByPrimaryKey(Long.parseLong(result.get(i)));
					if(category!=null){
						buffer.append(category.getName()+",");
					}
				}
				if(buffer.toString().length()>0){
					goodsBrand.setCategoryName((buffer.toString().substring(0,buffer.toString().length()-1)));
				}
			}
		}	
		PageBean<GoodsBrand> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * 新增品牌
	 */
	@Override
	public int addGoodsBrand(String name, String logo, String sortnum, String catId) throws Exception {
		int row = 0;
		GoodsBrand goodsBrand = new  GoodsBrand();
		List<GoodsBrand> list = mapper.selectByName(name);//判断品牌是否存在
		if(list.size()>0){
			row = 1000;
		}else{
			goodsBrand.setId(Long.parseLong(MixCodeUtil.createOutTradeNo()));
			goodsBrand.setName(name);
			if(!StringUtils.isEmptyOrWhitespaceOnly(logo)){
				goodsBrand.setLogo(logo);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				goodsBrand.setSortnum(Short.parseShort(sortnum));
			}
			goodsBrand.setCatId(catId);
			row = mapper.insertSelective(goodsBrand);
		}
		return row;
	}
	
	/**
	 * 新增京东品牌
	 */
	@Override
	public int addJdGoodsBrand(String id, String name, String logo, String sortnum, String catId) throws Exception {
		int row = 0;
		GoodsBrand goodsBrand = new  GoodsBrand();
		GoodsBrand brand = mapper.selectByNameAndJd(name, 1);//判断品牌是否存在
		if(brand!=null){
			String[] s1 = brand.getCatId().split(",");
			for(int i=0; i<s1.length; i++){
				if(Integer.parseInt(catId)==Integer.parseInt(s1[i])){
					row=1;
				}else{
					String catIdStr = brand.getCatId()+","+catId;
					brand.setCatId(catIdStr);
					row = mapper.updateByPrimaryKeySelective(brand);
				}
			}
		}else{
			goodsBrand.setId(Long.parseLong(id));
			goodsBrand.setName(name);
			if(!StringUtils.isEmptyOrWhitespaceOnly(logo)){
				goodsBrand.setLogo(logo);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				goodsBrand.setSortnum(Short.parseShort(sortnum));
			}
			goodsBrand.setCatId(catId);
			goodsBrand.setIsJd(1);
			row = mapper.insertSelective(goodsBrand);
		}
		return row;
	}
	
	/**
	 * 品牌的删除
	 */
	@Override
	public int deleteGoodsBrand(String id) throws Exception {
		return  mapper.deleteByPrimaryKey(Long.parseLong(id));
	}
	
	/**
	 * 品牌详情
	 */
	@Override
	public GoodsBrand details(String id) throws Exception {
		GoodsBrand goodsBrand = mapper.selectByPrimaryKey(Long.parseLong(id));
		String str = goodsBrand.getCatId();
		List<String> result = Arrays.asList(str.split(",")); //string转list
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<result.size(); i++){
			GoodsCategory category = categoryMapper.selectByPrimaryKey(Long.parseLong(result.get(i)));
			if(category!=null){
				buffer.append(category.getName()+",");
			}
		}
		if(buffer.toString().length()>0){
			goodsBrand.setCategoryName((buffer.toString().substring(0,buffer.toString().length()-1)));
		}
		return goodsBrand;
	}
	
	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public int batchDelete(String id) throws Exception {
		int row = 0;
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			Goods goods = new Goods();
			goods.setBrandId(Long.parseLong(str[i]));
			List<Goods> goodsList = goodsMapper.selectByBrandId(goods);
			if(goodsList.size()>0){
				return 999;
			}
			list.add(str[i]);
		}
		row = mapper.batchDelete(list);
		return row;
	}
	
	/**
	 * 获取所有品牌
	 */
	@Override
	public List<GoodsBrand> selectAll() throws Exception {
		return mapper.selectAll();
	}
	
	/**
	 * 品牌的修改
	 */
	@Override
	public int editGoodsBrand(String id, String name, String logo, String sortnum, String catId) throws Exception {
		int row = 0;
		List<GoodsBrand> list = mapper.selectByNameNotMy(name, Long.parseLong(id));
		if(list.size()>0){ //判断是否重名
			row = 1000;
		}else{
			GoodsBrand goodsBrand = mapper.selectByPrimaryKey(Long.parseLong(id));
			if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
				goodsBrand.setName(name);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(logo)){
				goodsBrand.setLogo(logo);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
				goodsBrand.setSortnum(Short.parseShort(sortnum));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(catId)){
				goodsBrand.setCatId(catId);
			}
			row = mapper.updateByPrimaryKeySelective(goodsBrand);
		}
		return row;
	}
	
}
