package com.bh.jd.enums;

import com.bh.jd.config.JDConfig;

public enum GoodsEnum {
		GET_PAGE_NUM(JDConfig.BASE_URL+"/api/product/getPageNum"),
		GET_SKU(JDConfig.BASE_URL+"/api/product/getSku"),
		GET_DETAIL(JDConfig.BASE_URL+"/api/product/getDetail"),
		GET_IS_COD(JDConfig.BASE_URL+"/api/product/getIsCod"),
		GET_SKU_GIFT(JDConfig.BASE_URL+"/api/product/getSkuGift"),
		GET_FREIGHT(JDConfig.BASE_URL+"/api/order/getFreight"),
	    GET_CATEGORYS(JDConfig.BASE_URL+"/api/product/getCategorys"),
	    SEARCH(JDConfig.BASE_URL+"/api/search/search"),
	    CHECK(JDConfig.BASE_URL+"/api/product/check"),
	    GET_YANBAO_SKU(JDConfig.BASE_URL+"/api/product/getYanbaoSku"),
	    GET_CATEGORY(JDConfig.BASE_URL+"/api/product/getCategory"),
	    GET_SIMILAR_SKU(JDConfig.BASE_URL+"/api/product/getSimilarSku"),
	    
	    
	    
		GET_SKU_BY_PAGE(JDConfig.BASE_URL+"/api/product/getSkuByPage"),
	    GET_SKU_STATUS(JDConfig.BASE_URL+"/api/product/skuState"),
	    GET_SKU_IMAGE(JDConfig.BASE_URL+"/api/product/skuImage"),
	    GET_COMMENTSUMMARYS(JDConfig.BASE_URL+"/api/product/getCommentSummarys"),
	    GET_CHECK_AREA_LIMIT(JDConfig.BASE_URL+"/api/product/checkAreaLimit"),
	    GET_SELL_PRICE(JDConfig.BASE_URL+"/api/price/getSellPrice"),

	    
	
	    GET_NEW_STOCK_BY_ID(JDConfig.BASE_URL+"/api/stock/getNewStockById"),
	    SUBMIT_ORDER(JDConfig.BASE_URL+"/api/order/submitOrder"),
	    CONFIRM_ORDER(JDConfig.BASE_URL+"/api/order/confirmOrder"),
	    CANCEL(JDConfig.BASE_URL+"/api/order/cancel"),
	    DOPAY(JDConfig.BASE_URL+"/api/order/doPay"),
	    PROMISE_CALENDAR(JDConfig.BASE_URL+"/api/order/promiseCalendar"),
	    SELECT_JD_ORDERID_THIRD_ORDER(JDConfig.BASE_URL+"/api/order/selectJdOrderIdByThirdOrder"),
	    SELECT_JD_ORDER(JDConfig.BASE_URL+"/api/order/selectJdOrder"),
	    ORDER_TRACK(JDConfig.BASE_URL+"/api/order/orderTrack"),
	    GET_BALANCE(JDConfig.BASE_URL+"/api/price/getBalance"),
	    SELECT_JINCAI_CREDIT(JDConfig.BASE_URL+"/api/price/selectJincaiCredit"),
	    GET_BALANCE_DETAIL(JDConfig.BASE_URL+"/api/price/getBalanceDetail"),
	    CheckDlokOrder(JDConfig.BASE_URL+"/api/checkOrder/checkDlokOrder"),
	   
	 
	    //2018.6.25 zlk 批量获取库存接口
	    GET_StockById(JDConfig.BASE_URL+"/api/stock/getStockById"),
	    

	    GET_PRICE(JDConfig.BASE_URL+"/api/price/getPrice"),
	    GET_JD_PRICE(JDConfig.BASE_URL+"/api/price/getJdPrice"),
	    GET(JDConfig.BASE_URL+"/api/message/get"),
	    DEL(JDConfig.BASE_URL+"/api/message/del"),
	
		//获得地址
	    GET_PROVINCE(JDConfig.BASE_URL+"/api/area/getProvince"),
	    GET_CITY(JDConfig.BASE_URL+"/api/area/getCity"),
	    GET_COUNTY(JDConfig.BASE_URL+"/api/area/getCounty"),
	    GET_TOWN(JDConfig.BASE_URL+"/api/area/getTown"),
	    CHECK_AREA(JDConfig.BASE_URL+"/api/area/checkArea");

		

		private String method;
		private GoodsEnum(String method) {
			this.method = method;

		}
		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}
}
