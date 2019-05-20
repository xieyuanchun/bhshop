package com.bh.user.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.AdsShopMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsShopCategoryMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.AdsShop;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MyHotGoodsShopPojo;
import com.bh.goods.pojo.MyNewGoodsShopPojo;
import com.bh.user.api.service.MallShopService;
import com.bh.user.mapper.MemberShopFavoriteMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopFavorite;
import com.bh.user.pojo.MyAdsShopPojo;
import com.bh.user.pojo.ShopMainPage;
import com.bh.user.pojo.SimpleShop;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class MallShopServiceImpl implements MallShopService {
	
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Autowired
	private MemberShopMapper memberShopMapper;
	
	@Autowired
	private GoodsShopCategoryMapper goodsShopCategoryMapper;
	
	@Autowired
	private MemberShopFavoriteMapper memberShopFavoriteMapper;
	
	@Autowired
	private AdsShopMapper adsShopMapper;
	
	/** 2017-11-28配送端订单列表 */
	public List<GoodsShopCategory> goodsShopCategory(GoodsShopCategory g) throws Exception{
		List<GoodsShopCategory> list = new ArrayList<>();
		list = goodsShopCategoryMapper.selectByUserAndParams(g);
		return list;
	}
	
	/**2017-11-29 店铺的信息 **/
	public SimpleShop showShopMsg(Integer shopId,Member member) throws Exception{
		SimpleShop simpleShop2 = new SimpleShop();
		if (member == null) {
			simpleShop2.setFavorite(0);
			simpleShop2.setFavoriteName(String.valueOf(0));
		}else{
			MemberShopFavorite favorite = memberShopFavoriteMapper.findByShopIdAndMid(shopId, member.getId());
			if (favorite == null) {
				simpleShop2.setFavorite(0);
				simpleShop2.setFavoriteName(String.valueOf(0));
			}else{
				simpleShop2.setFavorite(1);
				simpleShop2.setFavoriteName(String.valueOf(1));
			}
		}
		MemberShop memberShop = memberShopMapper.selectShopMsg(shopId);
		if (memberShop!=null) {
			simpleShop2.setShopName( memberShop.getShopName());
			simpleShop2.setLogo(memberShop.getLogo());
		}
		return simpleShop2;
	}
	
	/*****2017-11-29 店铺的广告*****/
	public List<MyAdsShopPojo> shopads(AdsShop adsShop) throws Exception{
		List<MyAdsShopPojo> list = new ArrayList<>();
		list = adsShopMapper.selectAdsShopByParams(adsShop);
		 return list;
	}
	
	public Map<String, Object> list(Integer shopId) throws Exception{
		List<MyHotGoodsShopPojo> goodsList=new ArrayList<>();
		goodsList = goodsMapper.selectGoodsByShopId(shopId);
		if (goodsList.size() > 0) {
			for (MyHotGoodsShopPojo myHotGoodsShopPojo : goodsList) {
				myHotGoodsShopPojo.setImage(RegExpValidatorUtils.returnNewString(myHotGoodsShopPojo.getImage()));
			}
		}
		List<MyNewGoodsShopPojo> newGoodsShop = new ArrayList<>();
		newGoodsShop = goodsMapper.selectGoodsByShopId2(shopId);
		Map<String, Object> myMap=new HashMap<>();
		//新品
		myMap.put("newGoodsShop", newGoodsShop);
		//热门
		myMap.put("hotGoodsShop", goodsList);
		return myMap;
		
	}
	
	public Map<String, Object> alllist(Goods goods ,Integer page,Integer size) throws Exception{
		goods.setPageSize(size);//每页的数量
		goods.setCurrentPage(page+"");
		goods.setCurrentPageIndex((Integer.valueOf(goods.getCurrentPage())-1)*goods.getPageSize());
       List<ShopMainPage> goodsList = new ArrayList<>();
		goodsList = goodsMapper.selectGoodsByShopId1(goods);
		if (goodsList.size() > 0) {
			for (ShopMainPage shopMainPage : goodsList) {
				String image=shopMainPage.getImage();
				shopMainPage.setImage(RegExpValidatorUtils.returnNewString(image));
				shopMainPage.setRealPrice(MoneyUtil.fen2Yuan(shopMainPage.getRealPrice()));
			}
		}
		Map<String, Object> map=new HashMap<>();
		map.put("list", goodsList);
		return map;
		
	}
	
	
	public PageBean<Goods> alllistPc(Goods goods ,Integer page,Integer size,String fz) throws Exception{
		   List<Goods> goodsList = new ArrayList<>();
		   Integer currentPages = page;//当前第几页
			Integer pageSizes = size;//每页显示几条
			Integer pageStart = (currentPages-1) * pageSizes;//从第几条开始
			List<GoodsShopCategory> list = null;
			List<String> string = new ArrayList<>();
			if (goods.getShopCatId() !=null) {
				list = getAllCateList(goods.getShopCatId());
				for (GoodsShopCategory goodsShopCategory : list) {
					string.add(String.valueOf(goodsShopCategory.getId()));
				}
			}else{
				string =null;
			}
			
			
			
			int total = goodsMapper.selectCountGoodsByShopId3(goods.getShopId(), string,goods.getName());//总条数
			goodsList = goodsMapper.selectGoodsByShopId3(pageStart,size,goods.getShopId(),string,goods.getName(), fz);
			
		
			
			if (goodsList.size() > 0) {
				for(int m=0;m<goodsList.size();m++){
					
					List<GoodsSku> skus = goodsSkuMapper.selectListByGoodsId(goodsList.get(m).getId());
					if (skus.size() > 0) {
						GoodsSku goodsSku = skus.get(0);
						Goods good = goodsMapper.selectByPrimaryKey(goodsList.get(m).getId());
						goodsList.get(m).setName(good.getName());
						// 设置图片路径 取一张
						org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
						org.json.JSONArray personList = jsonObj.getJSONArray("url");
						goodsList.get(m).setImage((String) personList.get(0));
						double realSellPrice =(double) goodsSku.getSellPrice()/100;
						goodsList.get(m).setRealPrice(realSellPrice);
						for (int k =0;k<skus.size();k++) {
							skus.get(k).setValueObj(JsonUtils.stringToObject(skus.get(k).getValue()));
							double re =(double) skus.get(k).getSellPrice()/100;
							skus.get(k).setRealPrice(re);
							org.json.JSONObject f = new org.json.JSONObject(skus.get(k).getValue());
							org.json.JSONArray d = f.getJSONArray("url");
							skus.get(k).setImage((String)d.get(0));
						}
					}else{
						double realSellPrice =(double) goodsList.get(m).getSellPrice()/100;
						goodsList.get(m).setRealPrice(realSellPrice);
					}
					goodsList.get(m).setSkuList(skus);
				}
			}
			
			int pages = total / pageSizes;//总页数
			pages = total % pageSizes > 0 ? (pages+1) : pages;
			int size1 = goodsList.size() == pageSizes ?  pageSizes : goodsList.size();
			PageBean<Goods> pageBean = new PageBean<>(goodsList);
			pageBean.setPageNum(currentPages);
			pageBean.setList(goodsList);
			pageBean.setTotal(total);
			pageBean.setPages(pages);
			pageBean.setPageSize(pageSizes);
			pageBean.setSize(size1);
			return pageBean;

	}
	
	
	@Override
	public List<GoodsShopCategory> getAllCateList(Integer reid){
		GoodsShopCategory myGc = goodsShopCategoryMapper.selectByPrimaryKey(reid);
		List<GoodsShopCategory> catesList = getCatesByReid(reid);
		List<GoodsShopCategory> retList = new ArrayList<>();
		getCateTreeList(catesList,retList);
		retList.add(0, myGc);
		return retList;
	}

	private List<GoodsShopCategory> getCateTreeList(List<GoodsShopCategory> cateList,List<GoodsShopCategory> retList){
		List<GoodsShopCategory> subMenuList = new ArrayList<GoodsShopCategory>();
		for(GoodsShopCategory entity : cateList){
		  getCateTreeList(getCatesByReid(entity.getId()),retList);
		  retList.add(entity);  
		  subMenuList.add(entity);
		}
		return subMenuList;
	}
	
	
	private List<GoodsShopCategory> getCatesByReid(Integer reid){
		return goodsShopCategoryMapper.selectAllByReid(reid);
	}
}
