package com.bh.admin.controller.goods;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.admin.enums.LocationEnum;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicGoodsService;
import com.bh.admin.service.TopicService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.bh.wxpayjsapi.service.WxLoginApi;


@RestController
@RequestMapping("/topic")
public class TopicController {
	@Autowired
	private TopicService topicService;
	@Autowired
	private TopicGoodsService topicGoodsService;

	// 添加
	@RequestMapping("/add")
	public BhResult add(@RequestBody Topic entity) {
		BhResult result = null;
		try {
			int row = topicService.add(entity);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/*@RequestMapping("/add")
	public BhResult add(@RequestBody Topic entity) {
		BhResult r = null;
		try {
		    //row的值为0时未进行插入操作,值为1时插入成功,值为2时同个时间段内已存在一个活动
			int row = topicService.add(entity);
			if (row == 1) {
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
			}else if (row == 0) {
				r = new BhResult(BhResultEnum.FAIL, null);
			}else if (row == 2) {
				r = new BhResult(400, "同个时间段内已存在一个活动", null);
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
*/
	// 更新
/*	@RequestMapping("/update")
	public BhResult update(@RequestBody Topic entity) {
		BhResult r = null;
		try {
			int row = topicService.update(entity);
//			if(row==999){
//				r = new BhResult();
//				r.setStatus(BhResultEnum.TOPIC_GOING.getStatus());
//				r.setMsg(BhResultEnum.TOPIC_GOING.getMsg());
//				r.setData(row);
//				return r;
//			}
			if (row == 0) {
				r = new BhResult();
				r.setStatus(BhResultEnum.FAIL.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(null);
			}else if (row == 1) {
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(null);
			}else if (row == 2) {
				r = new BhResult();
				r.setStatus(400);
				r.setMsg("同个时间段内已存在一个活动");
				r.setData(null);
			}
			
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}*/
	
	
	// 更新
		@RequestMapping("/update")
		public BhResult update(@RequestBody Topic entity) {
			BhResult r = null;
			try {
				int row = topicService.update(entity);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
				return r;
			} catch (Exception e) {
				e.printStackTrace();
				r = BhResult.build(500, "操作异常!");
				return r;
			}
	
		}

	// 获取
	@RequestMapping("/get")
	public BhResult get(@RequestBody Topic entity) {
		BhResult r = null;
		try {
			Topic e = topicService.get(entity.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(e);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}

		
	}
	// 列表
	@RequestMapping("/listPage")
	public BhResult listPage(@RequestBody Topic entity) {
		BhResult r = null;
		try {
			PageBean<Topic> page = topicService.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}

	}
	
	@RequestMapping("/selectListByType")
	public BhResult selectListByType(@RequestBody Topic entity) {
		BhResult r = null;
		try {
			List<Topic> page = topicService.selectListByType(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}

	// 删除
	@RequestMapping("/delete")
	public BhResult delete(@RequestBody Topic entity) { 
		BhResult r = null;
		try {
			int row = topicService.delete(entity.getId());
			if(row==999){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GOING.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GOING.getMsg());
				r.setData(row);
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
	 * 活动商品添加
	 * SCJ-20180110-05
	 * @param entity
	 * @return
	 */
	@RequestMapping("/addTopicGoods")
	public BhResult addTopicGoods(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			topicGoodsService.add(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * 活动商品修改
	 * SCJ-20180110-06
	 * @param entity
	 * @return
	 */
	@RequestMapping("/updateTopicGoods")
	public BhResult updateTopicGoods(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			int row = topicGoodsService.update(entity);
			if(row==999){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GOING.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GOING.getMsg());
				r.setData(row);
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
	 * 活动商品详情
	 * SCJ-20180110-07
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getTopicGoods")
	public BhResult getTopicGoods(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			TopicGoods t = topicGoodsService.get(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	/**
	 * 活动商品删除
	 * SCJ-20180110-08
	 * @param entity
	 * @return
	 */
	@RequestMapping("/deleteTopicGoods")
	public BhResult deleteTopicGoods(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			int row = topicGoodsService.delete(entity);
			if(row==999){
				r = new BhResult();
				r.setStatus(BhResultEnum.TOPIC_GOING.getStatus());
				r.setMsg(BhResultEnum.TOPIC_GOING.getMsg());
				r.setData(row);
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
	 * 活动商品列表
	 * SCJ-20180110-09
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPageTopicGoods")
	public BhResult listPageTopicGoods(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			PageBean<TopicGoods> t= topicGoodsService.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * 活动商品审核
	 * SCJ-20180110-10
	 * @param entity
	 * @return
	 */
	@RequestMapping("/audit")
	public BhResult audit(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			int row = topicGoodsService.audit(entity);
			if(row == 666){
				r = new BhResult();
				r.setStatus(BhResultEnum.GOODS_NOT_UP.getStatus());
				r.setMsg(BhResultEnum.GOODS_NOT_UP.getMsg());
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * 活动添加显示位置选择
	 * SCJ-20180112-1
	 * @param entity
	 * @return
	 */
	@RequestMapping("/location")
	public BhResult location() { 
		BhResult r = null;
		try {
			List<Map<String,Object>> list = LocationEnum.getLocationList();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	
	/**
	 * 砍价活动配置
	 * SCJ-20180113-01
	 * @param entity
	 * @return
	 */
	@RequestMapping("/addTopicBargain")
	public BhResult addTopicBargain(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			int row = topicGoodsService.addTopicBargain(entity);
			if(row == 999){
				r = new BhResult();
				r.setStatus(BhResultEnum.GOODS_EXCIT.getStatus());
				r.setMsg(BhResultEnum.GOODS_EXCIT.getMsg());
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * 砍价活动列表
	 * SCJ-20180113-02
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPageTopicBargain")
	public BhResult listPageTopicBargain(@RequestBody TopicGoods entity) { 
		BhResult r = null;
		try {
			PageBean<TopicGoods> page = topicGoodsService.listPageTopicBargain(entity);
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
	 * SCJ-20180116-04
	 * 移动端砍价活动专区商品列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/apiTopicGoodsList")
	public BhResult apiTopicGoodsList(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			PageBean<TopicGoods> t= topicGoodsService.apiTopicGoodsList(entity, member);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * SCJ-20180118-03
	 *  砍价详情
	 * @param entity
	 * @return
	 */
	@RequestMapping("/bargainAfterPage")
	public BhResult bargainAfterPage(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			Map<String, Object> t= topicGoodsService.bargainAfterPage(entity, member.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	
	/**
	 * SCJ-20180118-03
	 * 砍价详情
	 * @param entity
	 * @return
	 */
	@RequestMapping("/wxBargainAfterPage")
	public BhResult wxBargainAfterPage(HttpServletResponse response, HttpServletRequest request) { 
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			String bargainNo = request.getParameter("bargainNo");
			String mId = request.getParameter("mId");
			String id = request.getParameter("id");
			com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject();
			if(StringUtils.isNotEmpty(bargainNo)){
				jsonObj.put("bargainNo", bargainNo);
			}else{
				jsonObj.put("bargainNo", null);
			}
			if(StringUtils.isNotEmpty(mId)){
				jsonObj.put("mId", mId);
			}else{
				jsonObj.put("mId", null);
			}
			if(StringUtils.isNotEmpty(id)){
				jsonObj.put("id", id);
			}else{
				jsonObj.put("id", null);
			}
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/topic/wxBargainAfterPage";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
				url  = url.replace("STATE", java.net.URLEncoder.encode(jsonObj.toString(), "utf-8"));
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			    return r;
			}else{
				String state = request.getParameter("state");
				com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(state);
				com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
		        String openid = "";
		        if (jsonTexts.get("openid")!=null) {
		        	openid = jsonTexts.get("openid").toString();
		        }
			    TopicGoods entity = new TopicGoods();
			    entity.setOpenId(openid);
			    if(obj.get("id")!=null){
			    	entity.setId(obj.getInteger("id"));
			    }
			    if(obj.get("bargainNo")!=null){
			    	entity.setBargainNo(obj.get("bargainNo").toString());
			    }
			    if(obj.get("mId")!=null){
			    	entity.setmId(obj.getInteger("mId"));
			    }
				Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
				Map<String, Object> t= topicGoodsService.wxBargainAfterPage(entity, member);
				net.sf.json.JSONObject BACK = net.sf.json.JSONObject.fromObject(t);
				String url = Contants.BIN_HUI_URL+"/binhuiApp/helpfriendcut?back=BACK";
				url  = url.replace("BACK", java.net.URLEncoder.encode(BACK.toString(), "utf-8"));
				response.sendRedirect(url);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(t);
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	/**
	 * SCJ-20180118-03
	 * 砍价详情
	 * @param entity
	 * @return
	 */
	@RequestMapping("/wxBargainDetails")
	public BhResult wxBargainDetails(HttpServletRequest request, HttpServletResponse response) { 
		BhResult r = null;
		try {
			String id = request.getParameter("tgId");
			String openid = request.getParameter("openid");
			String bargainNo = request.getParameter("bargainNo");
			TopicGoods entity = new TopicGoods();
			entity.setId(Integer.parseInt(id));
			entity.setOpenId(openid);
			if(StringUtils.isNotEmpty(bargainNo)){
				entity.setBargainNo(bargainNo);
			}
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String, Object> t= topicGoodsService.wxBargainAfterPage(entity, member);
			net.sf.json.JSONObject BACK = net.sf.json.JSONObject.fromObject(t);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			String url = Contants.BIN_HUI_URL+"/binhuiApp/helpfriendcut?back=BACK";
			url  = url.replace("BACK", java.net.URLEncoder.encode(BACK.toString(), "utf-8"));
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
	 * SCJ-20180119-01
	 * 授权登录获取用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/authorize")
	public BhResult authorize(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/topic/getOpenid";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
		
				getOpenid(code, request, response);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		}  catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	//获取openId
	@RequestMapping("/getOpenid")
    public BhResult getOpenid(String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		BhResult r = null;
		try {
			System.out.println("##########---getOpenid--start--##############");
			System.out.println("**********code---->"+code+"<----end***");
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        System.out.println("**********accessToken---->"+accessToken+"<----end***");
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
	        System.out.println("**********openid---->"+openid+"<----end***");
			request.getSession().setAttribute("openid", openid);
			String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(userInfo);
			System.out.println("##########---getOpenid--end--##############");
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
    }
	
	
	/**
	 * SCJ-20180119-01
	 * 授权登录获取用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/authorizeTwo")
	public BhResult authorizeTwo(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/topic/getOpenidTwo";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
				System.out.println("##############-----else come in----------######################");
				getOpenid(code, request, response);
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
	
	//获取openId
	@RequestMapping("/getOpenidTwo")
    public BhResult getOpenidTwo(String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		BhResult r = null;
		try {
			String retUrl = request.getSession().getServletContext().getRealPath("/");
			//String retUrl = request.getHeader("Referer");
			//StringBuffer retUrl = request.getRequestURL();
			System.out.println(retUrl);
			System.out.println("##########---getOpenid--start--##############");
			System.out.println("**********code---->"+code+"<----end***");
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        System.out.println("**********accessToken---->"+accessToken+"<----end***");
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
	        System.out.println("**********openid---->"+openid+"<----end***");
			request.getSession().setAttribute("openid", openid);
			String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(userInfo);
			System.out.println("##########---getOpenid--end--##############");
			String url = retUrl+"?userInfo="+userInfo;
			response.sendRedirect(url);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
    }

	/**
	 * SCJ-20180118-02
	 *  我的砍价列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/apiMyTopic")
	public BhResult apiMyTopic(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			PageBean<Map<String, Object>> t= topicGoodsService.apiMyTopic(entity, member.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * SCJ-201800327-02
	 * 移动端荷兰拍卖专区商品列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/apiDautionGoodsList")
	public BhResult apiDautionGoodsList(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			PageBean<TopicGoods> t= topicGoodsService.apiDautionGoodsList(entity, member);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	
	/**
	 * SCJ-201800410-02
	 * 活动的下架
	 * @param entity
	 * @return
	 */
	@RequestMapping("/downTopic")
	public BhResult downTopic(@RequestBody Topic entity) { 
		BhResult r = null;
		try {
			topicGoodsService.downTopic(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

}
