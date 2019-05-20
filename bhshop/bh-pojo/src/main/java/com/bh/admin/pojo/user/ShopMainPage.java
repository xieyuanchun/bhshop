package com.bh.admin.pojo.user;

import java.util.List;

import com.bh.goods.pojo.Goods;

public class ShopMainPage {
	  
	  private List<Goods> newGoodsShop;//新品
	  private List<Goods> hotGoodsShop;//热门
	 // private List<Goods> hotCakes;//热销
	public List<Goods> getNewGoodsShop() {
		return newGoodsShop;
	}
	public void setNewGoodsShop(List<Goods> newGoodsShop) {
		this.newGoodsShop = newGoodsShop;
	}
	public List<Goods> getHotGoodsShop() {
		return hotGoodsShop;
	}
	public void setHotGoodsShop(List<Goods> hotGoodsShop) {
		this.hotGoodsShop = hotGoodsShop;
	}
	
	
	  
}
