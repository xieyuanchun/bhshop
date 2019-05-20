package com.order.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.CartGoodsList;
import com.bh.goods.pojo.CartList;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.LoseEfficacyGoods;
import com.bh.utils.JsonUtils;
import com.mysql.fabric.xmlrpc.base.Array;
import com.order.user.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private GoodsCartMapper goodsCartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	
	public Goods getShopId(Integer goodsId) throws Exception{
		return goodsMapper.selectByPrimaryKey(goodsId);
	}
	
	public GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart) throws Exception {
		return goodsCartMapper.selectGoodsCartBySelect(goodsCart);
	}
	 public int updateGoodsCartByPrimaryKeyAndgId1(List<String> ids)throws Exception{
		return goodsCartMapper.updateGoodsCartByPrimaryKeyAndgId2(ids);
	 }
	public int updateGoodsCartByPrimaryKeyAndgId2(List<GoodsCart> goodsCart) throws Exception {
		int row=0;
		for (int i = 0; i < goodsCart.size(); i++) {
			row = goodsCartMapper.updateGoodsCartByPrimaryKeyAndgId1(goodsCart.get(i));
		}
		return row;
	}
	
	/******* 2017-9-27******************************************/
	// mId不为null， 添加如果gId相同
	public int updateGoodsCartBymIdAndgoodId(GoodsCart goodsCart,Integer isInsert) throws Exception {
		int row = 0;
		//isInsert:取0时更新，取1是插入
		if (isInsert==0) {
			row = goodsCartMapper.updateGoodsCartBymIdAndgoodId(goodsCart);
		}else{
			row = goodsCartMapper.insertSelective(goodsCart);
		}
		return row;
	}
	public int insertSelectiveByBatch(List<GoodsCart> list){		
		return goodsCartMapper.insertSelectiveByBatch(list);
	}
	// 9月26 根据用户的mId查询购物车
	public List<GoodsCart> selectCoodsCartByUserId(GoodsCart goodsCart) throws Exception {
		List<GoodsCart> list = null;
		list = goodsCartMapper.selectCoodsCartByUserId(goodsCart);
		return list;
	}
	//先查询有多少个商家:已登录
	public List<CartList> selectShopMsg(GoodsCart goodsCart)throws Exception{
		 return goodsCartMapper.selectShopMsg(goodsCart);	
	}
	//该商家有多少个商品：已登录
	public	List<CartGoodsList> selectGoodsMsg(GoodsCart goodsCart)throws Exception{
		return goodsCartMapper.selectCartGoodsList(goodsCart);
	}
	//未登录的用户查询购物车
	public List<CartList> selectShopMsgNotLogin(List<GoodsCartPojo> pojo)throws Exception{
		return goodsCartMapper.selectShopMsgNotLogin(pojo);
	}
	public List<CartGoodsList> selectGoodsMsgNotLogin(List<GoodsCartPojo> pojo,Integer shopId)throws Exception{
		return goodsCartMapper.selectGoodsMsgNotLogin(pojo, shopId);
	}
	
	/**
	 * @Description: 失效商品列表
	 * @author xieyc
	 * @date 2018年4月20日 上午11:43:45
	 */
	public List<LoseEfficacyGoods> loseEfficacyList(GoodsCart goodsCart) {
		//查询失效的记录(商品没上架或者库存不足,但已经在购物车的商品)
		List<GoodsCart> listLoseCart=goodsCartMapper.getLoseEfficacyCart(goodsCart.getmId());
		List<LoseEfficacyGoods> loseGoodsList = new ArrayList<>();
		for (GoodsCart gc : listLoseCart) {
			LoseEfficacyGoods lose = new LoseEfficacyGoods();
			GoodsSku sku=goodsSkuMapper.selectByPrimaryKey(gc.getGskuid());
			JSONObject jsonObj = new JSONObject(sku.getValue());
		    JSONArray personList = jsonObj.getJSONArray("url");
		    Object value = JsonUtils.stringToObject(sku.getValue());
		    lose.setValueObj(value);//规格
		    String url = (String) personList.get(0);
		    lose.setUrl(url);//图片
			Goods goods=goodsMapper.selectByPrimaryKey(sku.getGoodsId());
			lose.setGoodName(goods.getName());//名字
			lose.setCatId(goods.getCatId());//分类Id
			StringBuffer sb=new StringBuffer();
			if(goods.getStatus().intValue()!=5){
				sb.append("商品已经下架");
			}
			if(sku.getStoreNums().intValue()==0){
				if(sb.length()>0){
					sb.append("且");
				}
				sb.append("商品库存不足");
			}
			lose.setMsg(sb.toString());//失效原因
			lose.setId(gc.getId());
			loseGoodsList.add(lose);
		}
		return loseGoodsList;
	}

	/**
	 * @Description: 清空失效商品
	 * @author xieyc
	 * @date 2018年4月20日 上午11:43:45
	 */
	public int emptyLoseEfficacyGoods(Integer mId) {
		int row=0;
		//查询失效的记录(商品没上架或者库存不足,但已经在购物车的商品)
		List<GoodsCart> listLoseCart=goodsCartMapper.getLoseEfficacyCart(mId);
		for (GoodsCart gcUpdate : listLoseCart) {
			gcUpdate.setIsDel(2);
			gcUpdate.setAddtime(new Date());
			row=goodsCartMapper.updateByPrimaryKeySelective(gcUpdate);
		}
		return row;
	}
	public int totalCartNum(Integer mId) throws Exception {
		int num = 0;
		num = goodsCartMapper.totalCartNum(mId);
		return num;
	}
	
	
	//2018-5-22
	public	List<GoodsCartListShopIdList> selectCartList(GoodsCart cart) throws Exception{
		return goodsCartMapper.selectGoodsList(cart);
	}
	public List<GoodsCart> selectCartListByShopId(GoodsCart cart) throws Exception{
		return goodsCartMapper.selectCartListByShopId(cart);
	}
	
	
	public int updateGoodsCartBymIdAndgoodId2(GoodsCart goodsCart,Integer isInsert) throws Exception {
		int row = 0;
		//isInsert:取0时更新，取1是插入
		if (isInsert==0) {
			row = goodsCartMapper.updateByPrimaryKeySelective(goodsCart);
		}else{
			row = goodsCartMapper.insertSelective(goodsCart);
		}
		return row;
	}

}
