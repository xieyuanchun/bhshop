package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.pojo.goods.ExportGoods;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.service.ExcelExportByGoodsService;
import com.bh.utils.JsonUtils;
import com.github.pagehelper.PageHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ExcelExportByGoodsServiceImpl implements ExcelExportByGoodsService{
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Override
	public ArrayList<ArrayList<String>> excelExport(JSONArray list) throws Exception {
		ArrayList<ArrayList<String>> arrayData = new ArrayList<>();
		if(list.size()>0){
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
				JSONObject ob = (JSONObject) it.next();
				Goods g = goodsMapper.selectByPrimaryKey(ob.getInt("id"));
				tradeData(arrayData, g);
			}	
		}{
			//如果无参数，则.....
		}
		return arrayData;
	}
	
	public ArrayList<ArrayList<String>> tradeData(ArrayList<ArrayList<String>> arrayData, Goods g){
		int sale = g.getSale()+g.getFixedSale();
		GoodsCategory category = goodsCategoryMapper.selectByPrimaryKey(g.getCatId());
		List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsIdAndStatus(g.getId());
		if(skuList.size()>0){
			for(GoodsSku sku : skuList){
				ArrayList<String> array = new ArrayList<>();
				array.add(0, g.getId()+"");  //商品ID
				array.add(1, g.getId()+"");  //父级商品ID
				array.add(2, sku.getSkuNo());//skuNo
				array.add(3, sku.getGoodsName());//商品(sku)名称
				array.add(4, sku.getValueTwo());//属性一
				array.add(5, sku.getValueThree());//属性二
				array.add(6, sku.getValueThree());//属性三
				array.add(7, category.getName()); //商品分类
				array.add(8, (double)sku.getMarketPrice()/100+""); //市场价
				array.add(9, (double)sku.getSellPrice()/100+""); //单买价
				array.add(10, (double)sku.getJdProtocolPrice()/100+""); //协议价
				array.add(11, (double)sku.getTeamPrice()/100+""); //团购价
				array.add(12, (double)sku.getStockPrice()/100+""); //进货价
				array.add(13, sku.getStoreNums()+""); //库存
				array.add(14, sale+""); //已售数量
				array.add(15, g.getSale()+""); //销量
				array.add(16, sku.getJdSkuNo()+""); //京东SKU编码
				arrayData.add(array);
			}
		}
		return arrayData;
	}
	
	
	@SuppressWarnings("unused")
	public ArrayList<ArrayList<String>> getData(int shopId, String name, String categoryId,String isHotShop,String isNewShop, String saleType, 
			String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String isJd)throws Exception {
		
		List<String> ids = new ArrayList<>();
		if (StringUtils.isNotEmpty(id)) {
			 ids = JsonUtils.stringToList(id);
		}
		List<ExportGoods> list = goodsMapper.selectExportGoods(shopId, name, categoryId, saleType, status,startPrice,
				endPrice, topicType, ids, skuNo, jdSkuNo,isJd,isHotShop,isNewShop);
		ArrayList<ArrayList<String>> arrayData  = new ArrayList<>();
		
		if(list.size()>0){
			for(ExportGoods exportGoods : list){
				ArrayList<String> array = new ArrayList<>();
				array.add(0, exportGoods.getId()+"");  //商品ID
				array.add(1, exportGoods.getId()+"");  //父级商品ID
				array.add(2, exportGoods.getGoodsName());//商品名称
				array.add(3, exportGoods.getSkuNo());//skuNo
				if (StringUtils.isBlank(exportGoods.getGoodsSkuName())) {
					array.add(4, exportGoods.getGoodsName());//商品sku名称
				}else{
					array.add(4, exportGoods.getGoodsSkuName());//商品sku名称
				}
				array.add(5, exportGoods.getValueOne());//属性一
				array.add(6, exportGoods.getValueTwo());//属性二
				array.add(7, exportGoods.getValueThree());//属性三
				array.add(8, exportGoods.getValueFour());//属性四
				array.add(9, exportGoods.getValueFive());//属性五
				array.add(10, exportGoods.getCategoryName()); //商品分类
				array.add(11, exportGoods.getMarketRealPrice()+""); //市场价
				array.add(12, exportGoods.getRealSellPrice()+""); //单买价
				array.add(13, exportGoods.getRealJdProtocolPrice()+""); //协议价
				array.add(14, exportGoods.getRealTeamPrice()+""); //团购价
				array.add(15, exportGoods.getRealStockPrice()+""); //进货价
				array.add(16, exportGoods.getRealJdBuyPrice()+""); //用户购买价
				array.add(17, exportGoods.getStoreNums()+""); //库存
				array.add(18, exportGoods.getHavedSale()+""); //已售数量
				array.add(19,exportGoods.getSale()+""); //销量
				array.add(20, exportGoods.getJdSkuNo()+""); //京东SKU编码
				arrayData.add(array);
			}
		}
		list.clear();
		list=null;
		return arrayData;
	}
	
	public List<ExportGoods> exportListNew(int shopId, String name, String categoryId,String isHotShop,String isNewShop, String saleType, 
		String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String isJd)throws Exception {
		List<String> ids = new ArrayList<>();
		if (StringUtils.isNotEmpty(id)) {
			 ids = JsonUtils.stringToList(id);
		}
		List<ExportGoods> list = goodsMapper.selectExportGoods(shopId, name, categoryId, saleType, status,startPrice,
				endPrice, topicType, ids, skuNo, jdSkuNo,isJd,isHotShop,isNewShop);
		return list;
	}
}
