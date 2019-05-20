package com.bh.util.enterprise;

import java.net.URLEncoder;
import com.bh.util.enterprise.pojo.QueryBusinessInfoPojo;
import com.bh.utils.recharge.HttpUtil;
import net.sf.json.JSONObject;

/**
 * 
 * @Description: 查询企业工商信息
 * @author xieyc
 * @date 2018年7月30日 下午7:19:37
 *
 */
public class QueryBusinessInfo {

	public static final String APPKEY = "11e462bfb968f2c4";// 你的appkey
	public static final String URL = "http://api.jisuapi.com/enterprise2/query";
	
	public static QueryBusinessInfoPojo queryBusinessInfo(String company) throws Exception {
		QueryBusinessInfoPojo queryBusinessInfoPojo=null;
		String result = null;
		String url = URL + "?appkey=" + APPKEY + "&company=" + URLEncoder.encode(company, "utf-8");

		try {
			result = HttpUtil.sendGet(url, "utf-8");
			JSONObject json = JSONObject.fromObject(result);
			queryBusinessInfoPojo=(QueryBusinessInfoPojo)JSONObject.toBean(json,QueryBusinessInfoPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryBusinessInfoPojo;
	}
	
	/**
	 * 
	 * @Description: queryBusinessInfoPojo.getStatus()的状态码（为0的时候表示验证成功）
	 * 				API错误码：
	 *					201	公司名称、信用代码和注册号都为空
	 *		        	202	公司不存在（扣次数）
	 *					203	关键词为空
	 *					210	没有信息
	 *				系统错误码：
	 *					101	APPKEY为空或不存在
	 *					102	APPKEY已过期	
	 *					103	APPKEY无请求此数据权限
	 *					104	请求超过次数限制
	 *					105	IP被禁止
	 *					106	IP请求超过限制
	 *					107	接口维护中
	 *					108	接口已停用
	 * 
	 * @author xieyc
	 * @date 2018年7月31日 上午11:52:23 
	 *
	 */
	public static void main(String[] args) {
		try {
			QueryBusinessInfoPojo queryBusinessInfoPojo= QueryBusinessInfo.queryBusinessInfo("广东宏景科技有限公司");
			System.out.println(queryBusinessInfoPojo.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
