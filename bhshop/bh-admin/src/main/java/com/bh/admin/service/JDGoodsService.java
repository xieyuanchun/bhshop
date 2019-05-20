package com.bh.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.JdGoods;
import com.bh.jd.bean.goods.CategoryList;
import com.bh.jd.bean.goods.CategoryVo;
import com.bh.jd.bean.goods.SearchResult;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

import net.sf.json.JSONArray;

public interface JDGoodsService {
	
	BhResult getSkuByPage(String pageNum,String pageNo) throws Exception;
	
	CategoryList getCategorys(String pageNo, String pageSize, String parentId, String catClass) throws Exception;
	
	CategoryList synchroCategorys() throws Exception;
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
	
	//京东商品池商品入库
	int exportJdGoods() throws Exception; 
	
	//京东商品列表
	PageBean<JdGoods> jdGoodsListPage(JdGoods entity) throws Exception; 
	
	
	ArrayList<ArrayList<String>> excelExport(Integer jdSkuNo, String poolNum, String goodsName, String brandName, String catId, Integer isUp,
			   Integer isDelete, Integer isGet, Double startJdPrice, Double endJdPrice, Double startStockPrice, Double endStockPrice) throws Exception;
	
	//根据京东商品池下标入库
	int exportJdGoodsByNum(String poolNum) throws Exception; 
	
	//根据京东商品池下标入库(补全)
	int exportJdGoodsByNumAll(String poolNum) throws Exception; 
	
	/*商品摘取（优）*/
	Map<String, Object> downJdGoods(String sku) throws Exception;
	
	//京东商品池商品入库(skuNo先入)
	int exportJdSkuNo() throws Exception; 
	
	//京东商品池商品详细信息入库(根据skuNo更新京东商品表)
	int detailsByJdSkuNo() throws Exception;
	
	String getJdStock(String jdSkuNo, String one, String two, String three, String four) throws Exception;
	
	int testChange(String jdSkuNo) throws Exception;
	
	int syncUpOrDownStatus(String jdSkuNo) throws Exception;
	
	int syncDeleteStatus(String jdSkuNo) throws Exception;
	
	int syncDifferStatus() throws Exception;
	
	//同步京东商品表价格
	int syncJdGoodsPrice() throws Exception;

	int updateGoodSkuByJdSkuNo(String jdskuNo);
}
