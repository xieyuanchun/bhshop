package com.bh.admin.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.bean.Sms;
import com.bh.result.BhResult;
import com.bh.user.pojo.*;
import com.bh.utils.SmsUtil;
 


@Controller
@RequestMapping("/SMS")
public class SMSController 


{
	 	
/*    //  短信
		@RequestMapping(value = "/send", method = RequestMethod.GET)
		@ResponseBody
	public BhResult send(String mobile)
	{
		BhResult res = null;
		String Url ="http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");

		int mobile_code = (int)((Math.random()*9+1)*100000);

	    String content = new String("你好,你的注册验证码是:"+mobile_code);
		
		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", "cf_zhihui076847"), //查看用户名请登录用户中心->验证码、通知短信->帐户及签名设置->APIID
			    new NameValuePair("password", "98ca5a57dfc2db4b0e29ce0a2cf05f40"),  //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
			    new NameValuePair("mobile",mobile), 
			    new NameValuePair("content", content),
		};
		method.setRequestBody(data);

		try {
			client.executeMethod(method);
			
			String SubmitResult =method.getResponseBodyAsString();
 
			
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

//			System.out.println(code);
//			System.out.println(msg);
//			System.out.println(smsid);

			 if("2".equals(code)){
				 
				 //mobile_code
				 //res = new BhResult(200, mobile_code, null);
				res = new BhResult(200, "发送成功，请耐心等候3分钟。 ", null);
			}

		} catch (Exception e) {
			res =  BhResult.build(400, "信息提示:网络繁忙,请稍后再试");
			LoggerUtil.error(e);
		}
		return res;
		
	}*/
	@Value("${SmsContent}")
	private String SmsContent;
	@Value("${SmsContentTime}")
	private String SmsContentTime;	//过期时间，默认是10分钟
	//  短信
		@RequestMapping(value = "/send", method = RequestMethod.GET)
		@ResponseBody
		public BhResult send(String mobile,HttpServletRequest request,HttpSession session)
		{
			Sms sms = new Sms();
			int mobile_code = (int)((Math.random()*9+1)*100000);
			sms.setPhoneNo(mobile);
			sms.setSmsContent(Integer.toString(mobile_code));
			
			//2017-9-22cheng
			VilidatePhone vilidatePhone = new VilidatePhone();
			vilidatePhone.setPhone(mobile);
			vilidatePhone.setCode(mobile_code);
			//将对象存进去
			request.getSession().setAttribute(SmsContent, vilidatePhone);
			request.getSession().setMaxInactiveInterval(Integer.parseInt(SmsContentTime) * 60);//60s就是1分钟
			BhResult bhres=SmsUtil.aliPushUserReg(sms);
			if (bhres.getStatus().equals(-1)) {
				bhres.setMsg("请勿频繁发送短信");
			}
			return bhres;
		}
		
	


}
 