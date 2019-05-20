package com.bh.product.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.MemberUserAccessLogMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.product.api.service.MemberUserAccessLogService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
@Transactional
public class MemberUserAccessLogImpl implements MemberUserAccessLogService{
	@Autowired
	private MemberUserAccessLogMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper skuMapper;
	
	/**
	 * 移动、PC获取用户最近浏览商品记录
	 */
	@Override
	public PageBean<MemberUserAccessLog> getPageBymId(Member member, String currentPage, int pageSize) throws Exception {
		int mId = 1;
		if(member!=null){
			mId=member.getId();
		}
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<MemberUserAccessLog> accessList = mapper.getListBymId(mId);
		for(MemberUserAccessLog accessLog : accessList){
			Goods goods = goodsMapper.selectByPrimaryKey(accessLog.getGoodsId());
			if(goods!=null){
				setGoodsGetter(goods);
				accessLog.setGoods(goods);
			}
		}
		PageBean<MemberUserAccessLog> pageBean = new PageBean<>(accessList);
		return pageBean;
	}
	
	/*public PageBean<Goods> getPageBymId(Member member, String currentPage, int pageSize) throws Exception {
		int mId = 0;
		List<Goods> list =  null;
		if(member!=null){ //登录情况下
			mId = member.getId();
			PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
			List<MemberUserAccessLog> accessList = mapper.getListBymId(mId);
			for(MemberUserAccessLog accessLog : accessList){
				Goods goods = goodsMapper.selectByPrimaryKey(accessLog.getGoodsId());
				list.add(goods);
			}
		}else{ //非登陆情况下
			PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
			list = goodsMapper.getListByVisit();
		}
		PageBean<Goods> pageBean = new PageBean<>(list);
		return pageBean;
	}*/
	
	/**
	 * 价格的转化及sku与主表的赋值
	 * @param list
	 * @return
	 */
	public List<Goods> setGetter(List<Goods> list){
		List<GoodsSku> skuList = null;
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;
		
		for(int i=0; i<list.size(); i++){	
			skuList = skuMapper.selectListByGoodsId(list.get(i).getId());
			if(skuList.size()>0){
				for(int j=0; j<skuList.size(); j++){
					List<String> imageList = new ArrayList<>();
					double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
					list.get(i).setRealPrice(realPrice);
					double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
					list.get(i).setMarkRealPrice(marketRealPrice);
					list.get(i).setSkuId(skuList.get(0).getId());
					
					jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
					personList = jsonObj.getJSONArray("url");
					if(j<1){
						for(int m = 0; m < personList.length(); m++){
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						}
						list.get(i).setImageList(imageList); //添加商品sku轮播图
					}
				}
				list.get(i).setImage(url); //设置第一条sku图为商品主图
			}else{
				double realPrice = (double)list.get(i).getSellPrice()/100; //价格“分”转化成“元”
				list.get(i).setRealPrice(realPrice);
				double marketRealPrice = (double)list.get(i).getMarketPrice()/100; //价格“分”转化成“元”
				list.get(i).setMarkRealPrice(marketRealPrice);
			}
		}
		return list;
	}
	
	public Goods setGoodsGetter(Goods goods){
		String url = null;
		String urlAll = null;
		org.json.JSONArray personList = null;
		org.json.JSONObject jsonObj = null;
		List<GoodsSku> skuList = skuMapper.selectListByGoodsIdAndStatus(goods.getId());
		if(skuList.size()>0){
			for(int j=0; j<skuList.size(); j++){
				List<String> imageList = new ArrayList<>();
				double realPrice = (double)skuList.get(0).getSellPrice()/100; //价格“分”转化成“元”
				goods.setRealPrice(realPrice);
				
				double realTeamPrice = (double)skuList.get(0).getTeamPrice()/100; //团购价“分”转化成“元”
				goods.setRealTeamPrice(realTeamPrice);
				
				double marketRealPrice = (double)skuList.get(0).getMarketPrice()/100; //价格“分”转化成“元”
				goods.setMarkRealPrice(marketRealPrice);
				goods.setSkuId(skuList.get(0).getId());
				
				jsonObj = new org.json.JSONObject(skuList.get(0).getValue()); //获取sku商品信息
				personList = jsonObj.getJSONArray("url");
				if(j<1){
					for(int m = 0; m < personList.length(); m++){
						try {
							url = (String) personList.get(0);
							urlAll = (String) personList.get(m);
							imageList.add(m, urlAll);
						} catch (Exception e) {
							url=null;
						}
					}
					goods.setImageList(imageList); //添加商品sku轮播图
				}
			}
			goods.setImage(url); //设置第一条sku图为商品主图
		}else{
			double realPrice = (double)goods.getSellPrice()/100; //价格“分”转化成“元”
			goods.setRealPrice(realPrice);
			double marketRealPrice = (double)goods.getMarketPrice()/100; //价格“分”转化成“元”
			goods.setMarkRealPrice(marketRealPrice);
			double realTeamPrice = (double)goods.getTeamPrice()/100; //团购价“分”转化成“元”
			goods.setRealTeamPrice(realTeamPrice);
		}
		return goods;
	}
}
