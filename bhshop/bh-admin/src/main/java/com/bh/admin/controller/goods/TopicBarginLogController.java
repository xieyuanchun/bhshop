package com.bh.admin.controller.goods;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.admin.pojo.goods.TopicBargainLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicBargainLogService;
import com.bh.config.Contants;
import com.bh.config.SwiftpassConfig;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;
import com.bh.utils.pay.WXPayConstants;
import com.bh.wxpayjsapi.service.WxLoginApi;
import com.mysql.jdbc.StringUtils;

@RestController
@RequestMapping("/barginLog")
public class TopicBarginLogController {
	@Autowired
	private TopicBargainLogService service;
	
	
	/**
	 * SCJ-20180126-01
	 * 砍价授权
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bargain")
	public BhResult bargain(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			String tgId = request.getParameter("tgId");
			String bargainNo = request.getParameter("bargainNo");
			String skuId = request.getParameter("skuId");
			String addressId = request.getParameter("addressId");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tgId", tgId);
			if(!StringUtils.isEmptyOrWhitespaceOnly(bargainNo)){
				jsonObj.put("bargainNo", bargainNo);
			}else{
				jsonObj.put("bargainNo", null);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(skuId)){
				jsonObj.put("skuId", skuId);
			}else{
				jsonObj.put("skuId", null);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(addressId)){
				jsonObj.put("addressId", addressId);
			}else{
				jsonObj.put("addressId", null);
			}
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid= Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/barginLog/addBargain";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				//+"&tgId="+tgId+"&bargainNo="+bargainNo+"&skuId="+skuId+"&addressId="+addressId
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8"));
				url  = url.replace("STATE", java.net.URLEncoder.encode(jsonObj.toString(), "utf-8"));
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
				System.out.println("##############-----else come in----------######################");
				addBargain(code, state,request, response);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			System.out.println("##########---authorize-end---##############");
			return r;
		}  catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	/**
	 * SCJ-20180116-01
	 * 用户砍价操作
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/addBargain")
	public BhResult addBargain(String code, String state, HttpServletRequest request, HttpServletResponse response) {
		BhResult r = null;
		try {
			//String state = request.getParameter("state");
			JSONObject jsonObj = JSONObject.parseObject(state);
			Object bargainNo = jsonObj.get("bargainNo");
			Object tgId = jsonObj.get("tgId");
			Object addressId = jsonObj.get("addressId");
			Object skuId = jsonObj.get("skuId");
			System.out.println("------------------------------------------------------");
			System.out.println("-----------------addBargain----->"+bargainNo);
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
			//request.getSession().setAttribute("openid", openid);
			String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			JSONObject jsonObject = (JSONObject) JSON.parse(userInfo);
			
			TopicBargainLog entity = new TopicBargainLog();
			if(!StringUtils.isEmptyOrWhitespaceOnly(openid)){
				entity.setOpenId(openid);
			}
			if(jsonObject.get("nickname")!=null){
				entity.setNickName(jsonObject.get("nickname").toString());
			}
			if(jsonObject.get("headimgurl")!=null){
				entity.setHeadImgUrl(jsonObject.get("headimgurl").toString());
			}
			if(bargainNo!=null){
				entity.setBargainNo(jsonObj.get("bargainNo").toString());
			}
			if(tgId!=null){
				entity.setTgId(jsonObj.getInteger("tgId"));
			}
			if(skuId!=null){
				entity.setSkuId(jsonObj.getInteger("skuId"));
			}
			if(addressId!=null){
				entity.setAddressId(jsonObj.getInteger("addressId"));
			}
			
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			int row = service.wxAdd(entity, member);
			if(row == 888){
				String retUrl = Contants.BIN_HUI_URL+"/binhuiApp/friendcut?msg=true";
				response.sendRedirect(retUrl);
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GET.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GET.getMsg());
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			String url = Contants.BIN_HUI_URL+"/bh-product-api/topic/wxBargainDetails?tgId="+tgId+"&openid="+openid+"&bargainNo="+bargainNo;
			response.sendRedirect(url);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * SCJ-20180116-01
	 * 用户砍价操作
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	public BhResult add(@RequestBody TopicBargainLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			int row = service.add(entity, member);
			if(row == 999){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_END.getStatus());
				r.setMsg(BhResultEnum.TOPIC_END.getMsg());
				return r;
			}else if(row == 888){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GET.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GET.getMsg());
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	
	/**
	 * SCJ-20180116-01
	 * 用户砍价操作
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/wxAdd")
	public BhResult wxAdd(@RequestBody TopicBargainLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(StringUtils.isEmptyOrWhitespaceOnly(entity.getOpenId())){
				r = new BhResult();
				r.setStatus(BhResultEnum.WX_LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.WX_LOGIN_FAIL.getMsg());
				return r;
			}
			int row = service.wxAdd(entity, member);
			if(row == 999){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_END.getStatus());
				r.setMsg(BhResultEnum.TOPIC_END.getMsg());
				return r;
			}else if(row == 888){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GET.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GET.getMsg());
				return r;
			}
			else if(row == 777){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_OUT.getStatus());
				r.setMsg(BhResultEnum.TOPIC_OUT.getMsg());
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	
	/**
	 * SCJ-20180116-02
	 * 砍价日志管理列表
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/listPage")
	public BhResult listPage(@RequestBody TopicBargainLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			PageBean<TopicBargainLog> page = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * SCJ-20180116-03
	 * 扫描砍价活动有效期
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkTimeChangeStatus")
	public BhResult checkTimeChangeStatus() {
		BhResult r = null;
		try {
			int row = service.checkTimeChangeStatus();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

}




