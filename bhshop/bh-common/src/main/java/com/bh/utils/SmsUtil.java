package com.bh.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bh.bean.Sms;
import com.bh.result.BhResult;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SmsUtil {
	/**
	 * 阿里大于短信发送(demo没用到)
	 * @param sms
	 * @return
	 */
	public static BhResult aliPush(Sms sms) {
		BhResult result = new BhResult(); 
		String url = "http://gw.api.taobao.com/router/rest";
		String appkey = "23441256";
		String secret = "1a690d0375fc604dc28ea6d881e0c006";
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("滨惠商城");
		JSONObject paramJson = new JSONObject();
		paramJson.put("order", sms.getSmsContent());
		req.setSmsParamString(paramJson.toJSONString());
		System.out.println(paramJson.toJSONString());
		req.setRecNum(sms.getPhoneNo());
		req.setSmsTemplateCode("SMS_98260054");
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			JSONObject jsonObject = JSONObject.parseObject(rsp.getBody());
			if (jsonObject.containsKey("error_response")) {// 存在异常
				JSONObject bodyJson = (JSONObject) jsonObject.get("error_response");
				if (bodyJson != null) {
					System.out.println("err-->" + bodyJson.get("sub_msg").toString());
					result.setStatus(-1);
					result.setMsg(bodyJson.get("sub_msg").toString());
				}
			} else {
				System.out.println("success");
				result.setStatus(200);
				result.setMsg("短信发送成功");
			}
		} catch (Exception e) {
			System.out.println("track");
			e.printStackTrace();
			result.setStatus(-1);
			result.setMsg("短信发送异常:" + e.getMessage());
		}

		return result;
		/**
		 * 响应示例
		 * 
		 * { "alibaba_aliqin_fc_sms_num_send_response":{ "result":{ } } }
		 */
		/**
		 * 异常示例 {"error_response":{"code":50,"msg":"Remote service error"
		 * ,"sub_code":"isv.invalid-parameter","sub_msg":"非法参数"}}
		 */

		/**
		 * 错误码解释 错误码 错误描述 解决方案 isv.OUT_OF_SERVICE 业务停机 登陆www.alidayu.com充值
		 * isv.PRODUCT_UNSUBSCRIBE 产品服务未开通 登陆www.alidayu.com开通相应的产品服务
		 * isv.ACCOUNT_NOT_EXISTS 账户信息不存在 登陆www.alidayu.com完成入驻
		 * isv.ACCOUNT_ABNORMAL 账户信息异常 联系技术支持 isv.SMS_TEMPLATE_ILLEGAL 模板不合法
		 * 登陆www.alidayu.com查询审核通过短信模板使用 isv.SMS_SIGNATURE_ILLEGAL 签名不合法
		 * 登陆www.alidayu.com查询审核通过的签名使用 isv.MOBILE_NUMBER_ILLEGAL 手机号码格式错误
		 * 使用合法的手机号码 isv.MOBILE_COUNT_OVER_LIMIT 手机号码数量超过限制
		 * 批量发送，手机号码以英文逗号分隔，不超过200个号码 isv.TEMPLATE_MISSING_PARAMETERS 短信模板变量缺少参数
		 * 确认短信模板中变量个数，变量名，检查传参是否遗漏 isv.INVALID_PARAMETERS 参数异常 检查参数是否合法
		 * isv.BUSINESS_LIMIT_CONTROL 触发业务流控限制
		 * 短信验证码，使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时，10条/天。
		 * 一个手机号码通过阿里大于平台只能收到40条/天。 短信通知，使用同一签名、同一模板，对同一手机号发送短信通知，允许每天50条（自然日）。
		 * isv.INVALID_JSON_PARAM JSON参数不合法
		 * JSON参数接受字符串值。例如{"code":"123456"}，不接收{"code":123456} isp.SYSTEM_ERROR
		 * - - isv.BLACK_KEY_CONTROL_LIMIT 模板变量中存在黑名单关键字。如：阿里大鱼
		 * 黑名单关键字禁止在模板变量中使用，若业务确实需要使用，建议将关键字放到模板中，进行审核。
		 * isv.PARAM_NOT_SUPPORT_URL 不支持url为变量 域名和ip请固化到模板申请中
		 * isv.PARAM_LENGTH_LIMIT 变量长度受限 变量长度受限 请尽量固化变量中固定部分
		 * isv.AMOUNT_NOT_ENOUGH 余额不足 因余额不足未能发送成功，请登录管理中心充值后重新发送
		 */

		/**
		 * 请求参数 名称 类型 是否必须 示例值 更多限制 描述 extend String 可选 123456
		 * 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，
		 * 用户可以根据该会员ID识别是哪位会员使用了你的应用 sms_type String 必须 normal 短信类型，传入值请填写normal
		 * sms_free_sign_name String 必须 阿里大于
		 * 短信签名，传入的短信签名必须是在阿里大于“管理中心-验证码/短信通知/推广短信-配置短信签名”中的可用签名。如“阿里大于”
		 * 已在短信签名管理中通过审核，则可传入”阿里大于“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大于】欢迎使用阿里大于服务。
		 * sms_param Json 可选 {"code":"1234","product":"alidayu"}
		 * 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“
		 * 验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234",
		 * "product":"alidayu"} rec_num String 必须 13000000000
		 * 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，
		 * 一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
		 * sms_template_code String 必须 SMS_585014
		 * 短信模板ID，传入的模板必须是在阿里大于“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
		 */

	}
	/** 
	 * @Description: 共同部分(单条短信发送)
	 * @author xieyc
	 * @date 2018年2月6日 下午6:31:16 
	 */
	public static BhResult aliPushCom(String smsParamString,String recNum,String smsTemplateCode) {
		BhResult result = new BhResult();
		String smsType="normal";
		String smsFreeSignName="滨惠商城";//短信签名
		String url = "http://gw.api.taobao.com/router/rest";
		String appkey = "23441256";
		String secret = "1a690d0375fc604dc28ea6d881e0c006";
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType(smsType);
		req.setSmsFreeSignName(smsFreeSignName);
		req.setSmsParamString(smsParamString);
		req.setRecNum(recNum);
		req.setSmsTemplateCode(smsTemplateCode);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			JSONObject jsonObject = JSONObject.parseObject(rsp.getBody());
			if (jsonObject.containsKey("error_response")) {// 存在异常
				JSONObject bodyJson = (JSONObject) jsonObject.get("error_response");
				if (bodyJson != null) {
					System.out.println("err-->" + bodyJson.get("sub_msg").toString());
					result.setStatus(-1);
					result.setMsg(bodyJson.get("sub_msg").toString());
				}
			} else {
				System.out.println("success");
				result.setStatus(200);
				result.setMsg("短信发送成功");
			}
		} catch (Exception e) {
			System.out.println("track");
			e.printStackTrace();
			result.setStatus(-1);
			result.setMsg("短信发送异常:" + e.getMessage());
		}
		return result;
	}
	/**
	 * 
	 * @Description: 滨惠商城用户注册短信：【滨惠商城】亲爱的会员，您的手机注册验证码为${code}，10分钟内有效；如非本人操作，请忽略本条短信。
	 * @author xieyc
	 * @date 2018年2月5日 下午7:37:14
	 */
	public static BhResult aliPushUserReg(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("code", sms.getSmsContent());
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_126361552";
		return  aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	
	/**
	 * @Description: 滨惠商城商家入驻成功短信通知：【滨惠商城】您的商户资料已经通过审核,您用户名是:${username}，默认密码是:${password},请及时登录修改密码，请保存好不要随意给其他人!
	 * @author xieyc
	 * @date 2018年2月5日 下午7:37:14
	 */
	public static BhResult aliPushShopReg(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("username","BH"+sms.getSmsContent());//登入名
		paramJson.put("password","123456");//默认密码
		String smsParamString=paramJson.toJSONString(); 
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_141581074";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	/**
	 * 
	 * @Description: 滨惠拼团京东商品价格变动短信：【滨惠商城】检测到京东商品价格有变动，涉及商品数量有${num}，请留言！
	 * @author xieyc
	 * @date 2018年2月5日 下午7:37:14
	 */
	public static BhResult aliPushJDPrcieChange(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("num", sms.getSmsContent());
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_124330069";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	
	
	/**
	 * 
	 * @Description: 商品下架短信通知
	 * 【滨惠拼团】亲爱的${shopName}会员,你店铺的${goodsName}已被下架，原因：${msg}。如有疑问请联系客服！
	 * @author xieyc
	 * @date 2018年2月5日 下午7:37:14
	 */
	public static BhResult aliPushGoodsSoldOutMsg(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("shopName",sms.getShopName());
		paramJson.put("goodsName",sms.getGoodsName());
		paramJson.put("msg", sms.getMsg());
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_128635764";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	
	/**
	 * 
	 * @Description: 打印验证码 短信通知 zlk
	 * @date 2018年3月24日 下午12:37:14
	 */
	public static BhResult aliPushPrintCode(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("orderShopNo",sms.getOrderShopNo()+" "+"");//打印的商家订单号
		paramJson.put("verification",sms.getVerification());//验证码
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_129760148";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	
	/**
	 * @Description: 商家入驻 提交审核发送短信
	 * @author xieyc
	 * @date 2018年8月2日 下午5:58:12 
	 */
	public static BhResult aliPushSubmitEnterAudit(Sms sms) {
		JSONObject paramJson = new JSONObject();
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_141605905";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	/**
	 * @Description: 商家入驻拒绝入驻短信通知
	 * @author xieyc
	 * @date 2018年8月2日 下午5:58:12 
	 */
	public static BhResult aliPushRejectionNotice(Sms sms) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("note",sms.getSmsContent()+" ");//打印的商家订单号
		String smsParamString=paramJson.toJSONString();
		String recNum=sms.getPhoneNo();
		String smsTemplateCode="SMS_141605266";
		return aliPushCom(smsParamString,recNum,smsTemplateCode);
	}
	
	
	public static void main(String[] args) {
		String phone= "18806522884,13307227632";
	    Random r = new Random();
		String phone1[]=phone.split(",");
		for (String string : phone1) {
			Sms sms = new Sms();
			String num ="";
			for(int i=0;i<4;i++) {
				num+=r.nextInt(10)+"";
			}
			sms.setPhoneNo(string);
			sms.setSmsContent(num);
			aliPushUserReg(sms);
		}
		
		
	}
}
