package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bh.goods.pojo.Goods;
import com.bh.jd.bean.goods.CategoryList;
import com.bh.jd.bean.goods.CategoryVo;
import com.bh.jd.bean.goods.SearchResult;
import com.bh.result.BhResult;

import net.sf.json.JSONArray;

public interface JDGoodsService {
	
	BhResult getSkuByPage(String pageNum,String pageNo) throws Exception;
	
	CategoryList getCategorys(String pageNo, String pageSize, String parentId, String catClass) throws Exception;
	CategoryVo getCategory(String cid) throws Exception;
	
	SearchResult search(String keyword, String catId, String pageIndex, String pageSize, String min, String max, String brands) throws Exception;
	
	/*京东商品摘取*/
	List<Goods> getJdGoods(String sku) throws Exception;
	
	/*京东商品价格变更*/
	List<Long> getPriceChange() throws Exception;
	
	/*京东商品价格变更消息提示*/
	String notice() throws Exception;
	
	/*京价格变更发送短信*/
	int sendMessage() throws Exception;
	
	/*获取商品组合sku属性值*/
	List<String> getSkuValue(String sku) throws Exception;
	
	/*京东规格参数copy*/
	int insertJdParam(JSONArray list, String goodsId) throws Exception; 
	
	int batchAddJdGoods() throws Exception; 
	
	/*CategoryList getCategorys1(String pageSize,String pageNo) throws Exception;*/
	
	/*List<CategoryVo> getCategorys1(String pageSize,String pageNo) throws Exception;*/
	
	int insertModelValue(String htmlStr, Integer goodsId, String catName,String categroyId);
	
	int insertItemModelVaue(String htmlStr, Integer goodsId);
	
	SearchResult jdSkuNoSearch (String skuNo) throws Exception;
	
	Map<String, Object> batchAddJdGoods(String goodsId, String jdSkuNo) throws Exception; 
	
}
