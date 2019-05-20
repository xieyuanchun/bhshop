package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.AdsShopMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.AdsShop;
import com.bh.goods.pojo.Goods;
import com.bh.product.api.service.AdsShopService;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopAdmin;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class AdsShopImpl implements AdsShopService{
	@Autowired
	private AdsShopMapper mapper;
	@Autowired
	private MemberShopAdminMapper adminMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private MemberShopMapper shopMapper;
	
	/**
	 * 店铺轮播图的添加
	 */
	@Override
	public int addShopAds(String name, String image, String content, String link, String sortnum, String isPc,
			int shopId, String goodsId, String status) throws Exception {
		int row = 0;
		
		AdsShop adsShop = new AdsShop();
		adsShop.setCreatetime(new Date());
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			adsShop.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			adsShop.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			adsShop.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			adsShop.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			adsShop.setSortnum(Integer.parseInt(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPc)){
			adsShop.setIsPc(Integer.parseInt(isPc));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodsId)){
			adsShop.setGoodsId(Integer.parseInt(goodsId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(status)){
			adsShop.setStatus(Byte.parseByte(status));
		}
		
		adsShop.setShopId(shopId);
		
		row = mapper.insertSelective(adsShop);
		return row;
	}
	
	/**
	 * 店铺轮播图的修改
	 */
	@Override
	public int editShopAds(String id, String name, String image, String content, String link, String sortnum,
			String goodsId, String status, String isPc) throws Exception {
		int row = 0;
		
		AdsShop adsShop = mapper.selectByPrimaryKey(Integer.parseInt(id));
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(content)){
			adsShop.setContent(content);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
			adsShop.setName(name);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(image)){
			adsShop.setImage(image);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(link)){
			adsShop.setLink(link);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sortnum)){
			adsShop.setSortnum(Integer.parseInt(sortnum));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(goodsId)){
			adsShop.setGoodsId(Integer.parseInt(goodsId));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(status)){
			adsShop.setStatus(Byte.parseByte(status));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(isPc)){
			adsShop.setIsPc(Integer.parseInt(isPc));
		}
		
		row = mapper.updateByPrimaryKeySelective(adsShop);
		return row;
	}
	
	/**
	 * 店铺轮播图的删除
	 */
	@Override
	public int delectShopAds(String id) throws Exception {
		int row = 0;
		AdsShop adsShop = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(adsShop!=null){
			row = mapper.deleteByPrimaryKey(adsShop.getId());
		}
		return row;
	}
	
	/**
	 * 店铺轮播图分页列表
	 */
	@Override
	public PageBean<AdsShop> pageList(String currentPage, int pageSize, String name, String isPc, int shopId) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		
		List<AdsShop> list = mapper.pageList(name, isPc, shopId);
		for(AdsShop adsShop : list){
			Goods goods = goodsMapper.selectByPrimaryKey(adsShop.getGoodsId());
			adsShop.setGoodsName(goods.getName());
			
			MemberShop shop = shopMapper.selectByPrimaryKey(adsShop.getShopId());
			adsShop.setShopName(shop.getShopName());
			
			AdsShop ads = mapper.selectByPrimaryKey(adsShop.getId());
			ads.setGoodsName(goods.getName());
			ads.setShopName(shop.getShopName());
			adsShop.setDetails(ads);
		}
		PageBean<AdsShop> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 店铺轮播图详情
	 */
	@Override
	public AdsShop shopAdsDetails(String id) throws Exception {
		AdsShop adsShop = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(adsShop!=null){
			Goods goods = goodsMapper.selectByPrimaryKey(adsShop.getGoodsId());
			adsShop.setGoodsName(goods.getName());
			
			MemberShop shop = shopMapper.selectByPrimaryKey(adsShop.getShopId());
			adsShop.setShopName(shop.getShopName());
		}
		return adsShop;
	}

}
