package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsSku;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;

public interface GoodsSkuService {
	
	/*根据商品id查询所有sku*/
	List<GoodsSku> getListByGoodsId(String goodsId) throws Exception;
	
	/*goodsSku的添加*/
	int addGoodsSku(String goodsId, String value, String storeNums, String marketPrice, String sellPrice, String  weight, String minimum) throws Exception;
	
	/*goodsSku的修改*/
	int editGoodsSku(String id, String value, String storeNums, String marketPrice, String sellPrice, String  weight, String minimum, String teamPrice,
			String jdPrice, String jdBuyPrice, String jdOldBuyPrice, String jdProtocolPrice, String jdSupport, String jdSkuNo, String jdUpc,
			String goodsSkuName, String score, String keyOne, String valueOne, String keyTwo, String valueTwo, String keyThree, String valueThree,Integer userId) throws Exception;
	
	/*goodsSku的删除*/
	int deleteGoodsSku(String id) throws Exception;
	
	/*批量新增goodsSku*/
	int batchAddGoodsSku(String goodsId, JSONArray list) throws Exception;
	
	/*批量修改goodsSku*/
	int batchUpdateGoodsSku(JSONArray list) throws Exception;
	
	List<GoodsSku> bargainUse(int goodsId) throws Exception;
	
	//程凤云
	int batchUpdateGoodsSkuBySkuId(JSONArray list,String goodsId) throws Exception;
} 
