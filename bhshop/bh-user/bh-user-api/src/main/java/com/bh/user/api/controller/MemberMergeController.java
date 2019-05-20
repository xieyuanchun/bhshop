package com.bh.user.api.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberMergeService;
import com.bh.user.pojo.Member;
import com.bh.utils.pay.WXPayUtil;


@Controller
@RequestMapping("/memberMerge")
public class MemberMergeController {
	private static final Logger logger = LoggerFactory.getLogger(MemberMergeController.class);
	@Autowired
	private MemberMergeService memberMergeService;
	
	
	
	/**
	 * 
	 * @Description: 判断当前微信的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	@RequestMapping("/getOpenid")
	@ResponseBody
	public void getOpenid(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			if (StringUtils.isNotBlank((String) request.getParameter("callBackUrl"))) {
				session.setAttribute("callBackUrl",
						URLDecoder.decode((String) request.getParameter("callBackUrl"), "utf-8"));
			}			
			String openid=null;
			String access_token=null;
			String code = request.getParameter("code");//微信活返回code值，用code获取openid
			if(StringUtils.isBlank(code)){
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				String REDIRECT_URI = Contants.BIN_HUI_URL + "/bh-user-api/memberMerge/getOpenid";// 你的回调页
				url = url.replace("APPID", URLEncoder.encode(Contants.appId, "utf-8"));
				url = url.replace("REDIRECT_URI", URLEncoder.encode(REDIRECT_URI, "utf-8"));
				response.sendRedirect(url);
			}else{
				Map<String, Object> map = WXPayUtil.getAccessToken(Contants.appId, Contants.appSecret, code);
				openid=String.valueOf(map.get("openid"));
				access_token=String.valueOf(map.get("access_token"));
			}
			if(StringUtils.isNotBlank(openid)){
				String callBackUrl=(String) session.getAttribute("callBackUrl")+"&openid="+openid+"&code="+code+"&access_token="+access_token;
				response.sendRedirect(callBackUrl);// 授权成功跳转	
			}
		} catch (Exception e) {
			logger.error("getOpenid" + e.getMessage());
			e.printStackTrace();
		}
	}	
	/**
	 * 
	 * @Description: 判断当前微信的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	@RequestMapping("/wxBindingInfo")
	@ResponseBody
	public BhResult wxBindingInfo(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String openid = map.get("openid");
			if(StringUtils.isNotBlank(openid)){
				Map<Object, Object> returnMap = memberMergeService.wxBindingInfo(openid);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(returnMap);
			}else{
				r = BhResult.build(400, "传入的openid不能为空!");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("wxBindingInfo" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 判断手机号的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	@RequestMapping("/phoneBindingInfo")
	@ResponseBody
	public BhResult wxBindingInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			Map<Object, Object> returnMap = memberMergeService.phoneBindingInfo(map.get("phone"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("phoneBindingInfo" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * 
	 * @Description: 判断登入用户的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29 
	 */
	@RequestMapping("/bindingInfo")
	@ResponseBody
	public BhResult bindingInfo(HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				Map<Object,Object> map=memberMergeService.bindingInfo(member.getId());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(map);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("bindingInfo" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * 
	 * @Description: 更新手机号前判断该微信是否可以换手机
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	@RequestMapping("/changePhoneBefore")
	@ResponseBody
	public BhResult changePhoneBefore(HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				int row=memberMergeService.changePhoneBefore(member.getId());
				if(row==-1){
					r = BhResult.build(400, "请用微信号登入!");
				}else if(row==-2){
					r = BhResult.build(400, "为了您的账号安全,30天内不可更改绑定手机号!");
				}else if(row==-3){
					r = BhResult.build(400, "未知异常，请联系管理员!");
				}else{
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("changePhoneBefore" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * 
	 * @Description: 微信号更新手机号码
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	@RequestMapping("/changePhone")
	@ResponseBody
	public BhResult changePhone(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String newPhone=map.get("newPhone");
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				int row=memberMergeService.changePhone(newPhone,member.getId());
				if(row==-1){
					r = BhResult.build(400, "请用微信号登入!");
				}else if(row==-2){
					r = BhResult.build(400, "为了您的账号安全,30天内不可更改绑定手机号!");
				}else if(row==-3){
					r = BhResult.build(400, "该手机已经被注册!");
				}else{
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("changePhone" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	
	/**
	 * @Description: 手机绑定微信接口(解绑以后的绑定再次绑定引发的合并)
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	@RequestMapping("/phoneBindingWx")
	@ResponseBody
	public BhResult phoneBindingWx(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			logger.info("phoneBindingWx phone"+map.get("phone"));
			logger.info("phoneBindingWx openid"+map.get("openid"));
			Map<String, Object> mapWx = WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret); //用code获取access_token、openid				
			int row = memberMergeService.svaePhoneBindingWx(map.get("phone"),mapWx,map.get("openid"));
			if(row==-1){
				r = BhResult.build(400, "该微信已经绑定了手机!");
			}else if(row==-2){
				r = BhResult.build(400, "微信授权失败!");
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("phoneBindingWx" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * @Description: 微信绑定手机（微信绑定手机(解绑以后再去绑定手机的时候，发现这个手机号是已经注册过的这个时候调用这个接口)（作废）
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	@RequestMapping("/wxBindingPhone")
	@ResponseBody
	public BhResult wxBindingPhone(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			Map<String, Object> mapWx = WXPayUtil.getAccessToken(Contants.appId, Contants.appSecret, map.get("code")); //用code获取access_token、openid				
			int row = memberMergeService.saveWxBindingPhone(map.get("phone"),mapWx);
			if(row==-1){
				r = BhResult.build(400, "手机已经被其他微信绑定!");
			}else if(row==-2){
				r = BhResult.build(400, "微信和手机都没有注册!");
			}else if(row==-3){
				r = BhResult.build(400, "微信授权失败!");
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("phoneBindingWx" + e.getMessage());
			e.printStackTrace();
		}
		return r;//
	}	
	/**
	 * 
	 * @Description: 解绑手机号(作废)
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	@RequestMapping("/unBindPhone")
	@ResponseBody
	public BhResult unBindPhone(HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				int row=memberMergeService.unBindPhone(member.getId());
				if(row==-1){
					r = BhResult.build(400, "该用户不允许解绑!");
				}else{
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("unBindPhone" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	
	
	
}
