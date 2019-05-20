package com.bh.user.api.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.domain.NewsfeedWithMeInfo;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsFavoriteMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.MemberUserAccessLogMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderExpressTypeMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderPaymentMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.OrderInfoPojo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.api.service.UserFavoriteService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopFavoriteMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopFavorite;
import com.bh.user.pojo.MySimilarityGood;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.sf.jsqlparser.expression.operators.relational.Between;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService{
	
	@Autowired
	private GoodsFavoriteMapper goodsFavoriteMapper;
	
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private MemberShopMapper memberShopMember;
	
	@Autowired
	private MemberSendMapper MemberSendMapper;

	@Autowired
	private GoodsCartMapper goodsCartMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderSkuMapper orderSkuMapper;

	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;

	@Autowired
	private MemberShopMapper memberShopMapper;

	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	@Autowired
	private OrderExpressTypeMapper orderExpressTypeMapper;

	@Autowired
	private OrderPaymentMapper orderPaymentMapper;

	@Autowired
	private OrderShopMapper orderShopMapper;

	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;
	
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	@Autowired
	private MemberShopFavoriteMapper memberShopFavoriteMapper;
	
	@Autowired
	private MemberUserAccessLogMapper memberUserAccessLogMapper;
	
	/**
	 * 判断商品是否被用户收藏
	 */
	public int isFavorite(GoodsFavorite favorite1) throws Exception {
		int flag = 0;
		
		
		return flag;
	}
	
	
	/*2017-11-20 添加用户收藏商品*/
	public int addGoodFavorite(GoodsFavorite favorite1) throws Exception{
		int row =0;
		GoodsFavorite favorite = goodsFavoriteMapper.selectGoodsfavoriteByParams(favorite1);
		Timestamp now = new Timestamp(new Date().getTime());
		if (favorite !=null) {
			//已经存在
			favorite.setAddtime(now);
			row = goodsFavoriteMapper.updateByPrimaryKeySelective(favorite);
		}else{
			//如何flag=-1，不存在
			favorite1.setAddtime(now);
			row = goodsFavoriteMapper.insertSelective(favorite1);
		}
		return row;
	}
	
	/*******2017-11-20 显示用户收藏的分类名称*************/
	public List<GoodsCategory> showGoodsCategory(GoodsCategory goodsCategory) throws Exception{
		List<GoodsCategory> category = new ArrayList<>();
		category = goodsCategoryMapper.selectLastLevel1(goodsCategory);
		return category;
	}
	
	/*******2017-11-20 显示用户收藏列表*************/
	public PageBean<GoodsFavorite> showGoodsFavorite(GoodsFavorite favorite,Integer page,Integer rows) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		List<GoodsFavorite> list = new ArrayList<>();
		PageHelper.startPage(page, rows, true);
		list = goodsFavoriteMapper.selectGoodsfavorite(favorite.getmId(),favorite.getCategoryId(),favorite.getGoodName());
		for(int i=0;i<list.size();i++){
		Goods goods = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
			List<GoodsSku> goodsSku2 = goodsSkuMapper.selectListByGoodsId(list.get(i).getGoodsId());
			if (goodsSku2.size()>0) {
				GoodsSku goodsSku = goodsSku2.get(0);
				Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(good.getShopId());
				
				
				list.get(i).setGoodName(good.getName());
				double realsellPrice =0;
				if (goods.getSaleType()==3) {
					realsellPrice=(double)goodsSku.getTeamPrice()/100;
				}else{
					realsellPrice=(double)goodsSku.getSellPrice()/100;
				}
				list.get(i).setRealsellPrice(realsellPrice);
				Object obj = JsonUtils.stringToObject(goodsSku.getValue());
				list.get(i).setValueObj(obj);
				// 设置图片路径 取一张
				org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
				org.json.JSONArray personList = jsonObj.getJSONArray("url");
				list.get(i).setImage((String) personList.get(0));
				list.get(i).setShopName(memberShop.getShopName());
			}
			//设置分类的id
			list.get(i).setCategoryId(goods.getCatId());
		}
		PageInfo<GoodsFavorite> info = new PageInfo<>(list);

		PageBean<GoodsFavorite> pageBean = null;
		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());

		return pageBean;
	}
	
	/**2017-11-20根据商品的id和sku的id来删除用户收藏商品*/
	public int deleteGoodFavorite(GoodsFavorite favorite1) throws Exception{
		int row =0;
		String ids = favorite1.getShopName();
		List<String> idsf = JsonUtils.stringToList(ids);
		row = goodsFavoriteMapper.deleteByBatch(idsf);
		//GoodsFavorite favorite = goodsFavoriteMapper.selectGoodsfavoriteByParams(favorite1);
		/*if (favorite !=null) {
			//已经存在
			//favorite1.setId(favorite.getId());
			row = goodsFavoriteMapper.deleteByBatch(idsf);
		}else{
			row = 2;
		}*/
		return row;
	}
	
	//显示用户收藏的商家的分类名称
	public List<GoodsCategory> showShopCategory(GoodsCategory g) throws Exception{
		List<GoodsCategory> list = new ArrayList<>();
		list = goodsCategoryMapper.selectGoodsCategoryById(g);
		return list;
	}
	
	public List<GoodsCategory> selectHistoryCategory(GoodsCategory g) throws Exception{
		List<GoodsCategory> list = new ArrayList<>();
		list = goodsCategoryMapper.selectHistoryCategory(g);
		return list;
	}
	
	//显示商家收藏的列表String param
	public PageBean<MemberShopFavorite> showShopFavorite(MemberShopFavorite favorite,Integer page,Integer rows,Integer page1,Integer size1) throws Exception{
		List<MemberShopFavorite> list = new ArrayList<>();
		PageHelper.startPage(page, rows, true);
		list = memberShopFavoriteMapper.selectByparams(favorite.getmId(),favorite.getShopId(),favorite.getShopName());
		for(int i=0;i<list.size();i++){//在7天内
			Integer attention = memberShopFavoriteMapper.selectCountByShopId(list.get(i).getShopId());
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(list.get(i).getShopId());
			//List<Goods> goodsList = new ArrayList<>();
			//List<Goods> goodsList2 = new ArrayList<>();
			Goods good1 = new Goods();
			Goods good2 = new Goods();
			
			good1.setShopId(list.get(i).getShopId());
			good2.setShopId(list.get(i).getShopId());
			good1.setIsHotShop(1);
			good2.setIsNewShop(1);
			
			//PageHelper.startPage(page1, size1, true);
			//goodsList = goodsMapper.selectGoodsByShopId(good1);
			
			//list.get(i).setHotGoods(goodsList);
			//list.get(i).setNewGoods(goodsList2);
			
			Integer mark = memberShop.getLevel();
			double mark1 = (double) mark;
			double attention1=(double)attention;
	
			list.get(i).setShopName(memberShop.getShopName());
			list.get(i).setLogo(memberShop.getLogo());
			list.get(i).setDescription(memberShop.getDescription());
			list.get(i).setAttention(attention1);
			list.get(i).setMark(mark1);
		}
		PageInfo<MemberShopFavorite> info = new PageInfo<>(list);

		PageBean<MemberShopFavorite> pageBean = null;
		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());

		return pageBean;
	}
	
	//取消店铺的的收藏，可单个或者批量
	public int deleteShopFavorite(MemberShopFavorite favorite) throws Exception{
		int row =0;
		String ids = favorite.getShopName();
		List<String> idsf = JsonUtils.stringToList(ids);
		row = memberShopFavoriteMapper.deleteByBatch(idsf);
		return row;
	}

	//寻找相似的商品
	public Map<String, Object> selectSimilarity(Goods goods,Integer page,Integer size) throws Exception{
		Map<String, Object> myMap=new HashMap<>();
		//分页查询
		goods.setPageSize(10);//每页的数量
		goods.setCurrentPageIndex((Integer.valueOf(page)-1)*size);
		List<MySimilarityGood> mySimilarityGoodList=new ArrayList<>();
		List<Goods> list = goodsMapper.selectSimilarity(goods);
		if (list.size() >0) {
			for(int i=0;i<list.size();i++){
				MySimilarityGood mySimilarityGood=new MySimilarityGood();
				List<GoodsSku> skus = goodsSkuMapper.selectListByGoodsId(list.get(i).getId());
				if (skus.size()>0) {
					GoodsSku goodsSku = skus.get(0);
					// 设置图片路径 取一张
					org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					//销售模式 1单卖 2拼团单卖皆可 3只拼团
					if (list.get(i).getSaleType()==1) {
						mySimilarityGood.setRealPrice(MoneyUtil.fen2Yuan(goodsSku.getSellPrice()+""));
					}else{
						mySimilarityGood.setRealPrice(MoneyUtil.fen2Yuan(goodsSku.getTeamPrice()+""));
					}
					mySimilarityGood.setImage((String) personList.get(0));
				}
				mySimilarityGood.setId(list.get(i).getId());
				mySimilarityGood.setName(list.get(i).getName());
				mySimilarityGoodList.add(mySimilarityGood);
			}
		}
		myMap.put("list", mySimilarityGoodList);
		return myMap;
	}
	
	/**
	 * 显示用户的历史浏览记录
	 * ***/
	public PageBean<MemberUserAccessLog> showhistoryList(MemberUserAccessLog history,Integer page,Integer size) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<MemberUserAccessLog> list = new ArrayList<>();
		PageHelper.startPage(page, size, true);
		list = memberUserAccessLogMapper.selectHistoryList(history);
		if (list.size() >0) {
			Set<String> setTime = new HashSet<>();
			for(int i=0;i<list.size();i++){
				String s = sdf.format(list.get(i).getAddtime());
				String s1 = s.substring(0, s.length()-8);
				list.get(i).setSubAddtime(s1);
				setTime.add(s1);
			}
			
			List<String> setTime1=new ArrayList<>(setTime);
			for(int m=0;m<setTime1.size();m++){
				for(int i=0;i<list.size();i++){
					String timee = sdf.format(list.get(i).getAddtime());
					String time1 = timee.substring(0, timee.length()-8);
					if (time1.equals(setTime1.get(m))) {
						Date d2 = new Date();
						Date d1 = new Date();
						
						d1 = list.get(i).getAddtime();
						int time =daysBetween(d1,d2);
						if (time ==0) {
							list.get(i).setNote("今天");
						}else if (time == 1) {
							list.get(i).setNote("昨天");
						}else {
							list.get(i).setNote(null);
						}
						
					
						List<GoodsSku> skus = goodsSkuMapper.selectListByGoodsId(list.get(i).getGoodsId());
						if (skus.size()>0) {
							GoodsSku goodsSku = skus.get(0);
							Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
							double realPrice =(double) goodsSku.getTeamPrice()/100;
							goodsSku.setRealPrice(realPrice);
							goodsSku.setValueObj(JsonUtils.stringToObject(goodsSku.getValue()));
							// 设置图片路径 取一张
							org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
							org.json.JSONArray personList = jsonObj.getJSONArray("url");
							good.setImage((String) personList.get(0));
							good.setRealPrice(realPrice);
							list.get(i).setGoods(good);
							list.get(i).setGoodsSku(goodsSku);
						}else{
							Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
							double realPrice =(double) good.getTeamPrice()/100;
							good.setRealPrice(realPrice);
							list.get(i).setGoods(good);
							list.get(i).setGoodsSku(null);
						}
					}else{
						Date d2 = new Date();
						Date d1 = new Date();
						
						d1 = list.get(i).getAddtime();
						int time =daysBetween(d1,d2);
						if (time ==0) {
							list.get(i).setNote("今天");
						}else if (time == 1) {
							list.get(i).setNote("昨天");
						}else {
							list.get(i).setNote(null);
						}
						
					
						List<GoodsSku> skus = goodsSkuMapper.selectListByGoodsId(list.get(i).getGoodsId());
						if (skus.size()>0) {
							GoodsSku goodsSku = skus.get(0);
							Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
							double realPrice =(double) goodsSku.getTeamPrice()/100;
							goodsSku.setRealPrice(realPrice);
							goodsSku.setValueObj(JsonUtils.stringToObject(goodsSku.getValue()));
							// 设置图片路径 取一张
							org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
							org.json.JSONArray personList = jsonObj.getJSONArray("url");
							good.setImage((String) personList.get(0));
							good.setRealPrice(realPrice);
							list.get(i).setGoods(good);
							list.get(i).setGoodsSku(goodsSku);
						}else{
							Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
							double realPrice =(double) good.getTeamPrice()/100;
							good.setRealPrice(realPrice);
							list.get(i).setGoods(good);
							list.get(i).setGoodsSku(null);
						}
					}
				}
			}
		}
		
		PageInfo<MemberUserAccessLog> info = new PageInfo<>(list);
		PageBean<MemberUserAccessLog> pageBean = null;
		pageBean = new PageBean<>(list);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());
		return pageBean;
	}
	
	 public static int daysBetween(Date smdate,Date bdate) throws ParseException    
	    {    
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        smdate=sdf.parse(sdf.format(smdate));  
	        bdate=sdf.parse(sdf.format(bdate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));           
	}  
	 
		/**
		 *显示收藏的商品的数量以及店铺的数量
		 * ***/
		public OrderInfoPojo selectFavoriteNum(Integer mId) throws Exception{
			OrderInfoPojo info = new OrderInfoPojo();
			GoodsFavorite goodsFavorite = new GoodsFavorite();
			MemberShopFavorite memberShopFavorite = new MemberShopFavorite();
			goodsFavorite.setmId(mId);
			memberShopFavorite.setmId(mId);
			int goodsFavoriteNum=0;
			int shopFavoriteNum=0;
			goodsFavoriteNum = goodsFavoriteMapper.selectGoodsfavoriteNumber(goodsFavorite);
			shopFavoriteNum = memberShopFavoriteMapper.selectShopfavoriteNumber(memberShopFavorite);
			info.setRefund(goodsFavoriteNum);
			info.setSendmerchanNumber(shopFavoriteNum);
			return info;
		}
		
		public Goods selectOneGoodGavoritenum(GoodsFavorite favorite) throws Exception{
			GoodsFavorite fa = goodsFavoriteMapper.selectByPrimaryKey(favorite.getId());
			Goods good = goodsMapper.selectByPrimaryKey(fa.getGoodsId());
			List<GoodsSku> skus = goodsSkuMapper.selectListByGoodsId(good.getId());
			if (skus.size()>0) {
				GoodsSku goodsSku = skus.get(0);
				// 设置图片路径 取一张
				org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
				org.json.JSONArray personList = jsonObj.getJSONArray("url");
				good.setImage((String) personList.get(0));
				double realPrice = (double)goodsSku.getTeamPrice()/100;
				good.setRealPrice(realPrice);
				List<GoodsSku> sku = new ArrayList<>();
				goodsSku.setRealPrice(realPrice);
				goodsSku.setImage((String) personList.get(0));
				goodsSku.setValueObj(JsonUtils.stringToObject(goodsSku.getValue()));
				sku.add(goodsSku);
				good.setSkuList(sku);
			}else{
				double realPrice = (double)good.getTeamPrice()/100;
				good.setRealPrice(realPrice);
			}
			return good;
		}

}
