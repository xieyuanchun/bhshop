package com.bh.queryLogistics;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONObject;

/**
 * 文档地址： https://market.aliyun.com/products/56928004/cmapi021863.html?spm=5176.10695662.1996646101.searchclickresult.6a9061c20E0sea#sku=yuncode1586300000
 */
public class QueryLogistics {
	
	/**
	 * @Description:xieyc
	 * @author xieyc
	 * @date 2018年9月11日 下午8:10:42
	 */
	public static JSONObject getByExpressInfo(String expressNo,String type){
		JSONObject jsonObject = null;
		try {
			String host = "https://wuliu.market.alicloudapi.com";
			String path = "/kdi";
			String method = "GET";
			String appcode = "232d013ef8244587a9a4f69cb2fcca47";
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "APPCODE " + appcode);
			Map<String, String> querys = new HashMap<String, String>();
			querys.put("no", expressNo);
			querys.put("type", type);
			HttpResponse response2 = HttpUtils.doGet(host, path, method, headers, querys);
			String logistics = EntityUtils.toString(response2.getEntity());
			
			if (StringUtils.isNotBlank(logistics)) {
				jsonObject = JSONObject.fromObject(logistics);
			} else {
				jsonObject = JSONObject.fromObject("{\"status\":\"400\",\"msg\":\"没有物流信息！\",\"result\":\"\"}");
			}
		} catch (Exception e) {
			jsonObject = JSONObject.fromObject("{\"status\":\"400\",\"msg\":\"物流查询异常！\",\"result\":\"\"}");
			e.printStackTrace();
		}
		return jsonObject;
		
	}
	/**
	 * 
	 * @Description: 201	快递单号错误	快递单号错误
					 203	快递公司不存在	快递公司不存在
					 204	快递公司识别失败	快递公司识别失败
					 205	没有信息	没有信息
					 207	该单号被限制，错误单号	该单号被限制，错误单号；一个单号对应多个快递公司，请求须指定快递公司
	 * @author xieyc
	 * @date 2018年9月12日 下午4:51:18 
	 * @param   
	 * @return  
	 *
	 */
	public static void main(String[] args) {
		try {
			//圆通：801152902294896616  yuantong
			//申通：3374184131920  shentong
			//中通：264538212541  
			//顺丰：836447407698  SFEXPRESS
			JSONObject jsonObject=QueryLogistics.getByExpressInfo("264538212541","ZTO");
			System.out.println(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
