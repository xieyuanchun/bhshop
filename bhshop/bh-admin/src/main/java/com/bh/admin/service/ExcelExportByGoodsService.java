package com.bh.admin.service;

import java.util.ArrayList;
import java.util.List;

import com.bh.admin.pojo.goods.ExportGoods;

import net.sf.json.JSONArray;

public interface ExcelExportByGoodsService {
	
	/*导出excel(叶凌春)-scj*/
	ArrayList<ArrayList<String>> excelExport(JSONArray list) throws Exception;
	
	
	/*导出excel(叶凌春)-scj*/
	ArrayList<ArrayList<String>> getData(int shopId, String name, String catId, String isHotShop,String isNewShop,String saleType,
			String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String isJd) 
					throws Exception;
	
	List<ExportGoods> exportListNew(int shopId, String name, String catId, String isHotShop,String isNewShop,String saleType,
			String status,String startPrice,String endPrice, String topicType, String id, String skuNo, String jdSkuNo,String isJd) 
					throws Exception;
}
