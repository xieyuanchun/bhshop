package com.bh.util.enterprise;

import java.net.URLEncoder;
import com.bh.util.enterprise.pojo.PhoneVerifyPojo;
import com.bh.util.enterprise.pojo.PhoneVerifyPojoResult;
import com.bh.utils.recharge.HttpUtil;
import net.sf.json.JSONObject;

/**
 * 
 * @Description: 手机号实名认证(未开通，暂时不可以用)
 * @author xieyc
 * @date 2018年7月30日 下午7:19:29
 *
 */
public class PhoneVerify {
	public static final String APPKEY = "11e462bfb968f2c4";// 你的appkey
	public static final String URL = "http://api.jisuapi.com/mobileverify/verify";
	public static final String typeid = "1";// 证件类型，默认为身份证

	public static PhoneVerifyPojo phoneVerify(PhoneVerifyPojoResult entity) throws Exception {
		PhoneVerifyPojo phoneVerifyPojo=null;
		String result = null;
		String url = URL + "?appkey=" + APPKEY + "&realname=" + URLEncoder.encode(entity.getRealname(), "utf-8") + "&idcard="
				+ entity.getIdcard() + "&typeid=" +  typeid + "&mobile=" + entity.getMobile();
		try {
			result = HttpUtil.sendGet(url, "utf-8");
			JSONObject json = JSONObject.fromObject(result);
			phoneVerifyPojo=(PhoneVerifyPojo)JSONObject.toBean(json,PhoneVerifyPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phoneVerifyPojo;
	}
	   /**
	 	* @Description: phoneVerifyPojo.getStatus()的状态码（为0的时候表示验证成功）
	    * API错误码：
	    *	201	手机号为空
		*	202	证件号为空
		*	203	真实姓名为空
		*	204	身份证号不正确
		*	205	真实姓名包含特殊字符
		*	206	手机号不正确
		*	207	证件类型不正确
		*	208	不支持的手机号
		*	210	没有信息
		* 系统错误码：
		*	101	APPKEY为空或不存在
		*	102	APPKEY已过期
		*	103	APPKEY无请求此数据权限
		*	104	请求超过次数限制
		*	105	IP被禁止
		*	106	IP请求超过限制
		*	107	接口维护中
		*	108	接口已停用
		* @author xieyc
		* @date 2018年7月31日 上午10:26:05 
		*/
	public static void main(String[] args) {
		try {
			PhoneVerifyPojoResult entity=new PhoneVerifyPojoResult();
			entity.setRealname("谢元春");
			entity.setIdcard("");
			entity.setMobile("13682220372");
			PhoneVerifyPojo phoneVerifyPojo=PhoneVerify.phoneVerify(entity);
			System.out.println(phoneVerifyPojo.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
