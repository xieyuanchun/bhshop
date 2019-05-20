package com.wechat.controller;

import com.alipay.api.internal.util.codec.Base64;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.mapper.*;
import com.bh.user.pojo.*;
import com.bh.utils.TopicUtils;
import com.bh.utils.pay.WXPayUtil;
import com.wechat.service.WechatServices;
import com.wechat.util.AesCbcUtil;
import com.wechat.util.SignUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

//微信自动回复
@Controller
@RequestMapping("/wetchat")
public class WeChatController {
	private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);
	private String DNBX_TOKEN = "zhiyesoft";

	@Autowired
	WechatServices wechatService;
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	MemberUserMapper memberUserMapper;
	@Autowired
	WalletMapper  walletMapper;
    @Autowired
    private MemerScoreLogMapper memerScoreLogMapper;
	
	
	//微信接入自动回复
	@RequestMapping(value="/communication",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void communication(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//将请求、响应的编码均设置为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = response.getWriter();
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		try{
			if(isGet){
		       String signature  = request.getParameter("signature");//微信加密签名
		       String timestamp  = request.getParameter("timestamp");//时间戳
		       String nonce   = request.getParameter("nonce");//随机数
		       String echostr   = request.getParameter("echostr");//随机字符串
		       System.out.println("echostr-->"+echostr);
		       System.out.println("nonce-->"+nonce);
		       System.out.println("timestamp-->"+timestamp);
		       System.out.println("signature-->"+signature);
		    
		    // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		       if (SignUtil.checkSignateure(DNBX_TOKEN, signature, timestamp, nonce)) { 
		    	   logger.info("Connect the weixin server is successful.");
		    	   response.getWriter().write(echostr);
		       }else{
		    	   logger.info("Failed to verify the signature!");
		       }
			}else{
		      
				String respMessage = null;
		        try {
		            respMessage = wechatService.weixinPost(request);
		            pw.write(respMessage);
		            logger.info("The request completed successfully");
		            logger.info("to weixin server "+respMessage);
		          } catch (Exception e) {
		        	//e.printStackTrace();
		        	logger.info("eee "+respMessage);
		        	logger.info("Failed to convert the message from weixin!"); 
		          }    
	                
		    }
		
		}catch(Exception e){
			logger.info("#######Exception#######");
			//e.printStackTrace();
		}finally{
			pw.close();
		}
		
	}
		
	/**
	 * @Description: 公众号 授权登录
	 * 				涉及到手机号与微信号整合过程：step1:手机号与微信都在商城注册了（合并）(保留手机号用户id)==>头像和昵号变微信的
	 * 										step2:微信号存在账号手机不存在==》微信号绑定没有注册过的手机号（微信号用户id）
	 * 									    step3:微信已经绑定手机了（直接登入）
	 * 									    step4:手机号存在账号，微信不存在==》手机号绑定没有注册过的微信号（手机号用户id）==》头像和昵号变微信的
	 * 									    step5:手机号与微信在商城都不存在账号：新用户注册过程
	 *
	 * @author zlk
	 * @date 2018年8月31日 下午1:59:18
	 */
	@RequestMapping(value = "/authLogin")
	public void authLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			if (StringUtils.isNotBlank((String) request.getParameter("callBackUrl"))
					&& StringUtils.isNotBlank((String) request.getParameter("json"))) {
				session.setAttribute("callBackUrl",
						URLDecoder.decode((String) request.getParameter("callBackUrl"), "utf-8"));
				session.setAttribute("json", URLDecoder.decode((String) request.getParameter("json"), "utf-8"));
				session.setAttribute("phone", URLDecoder.decode((String) request.getParameter("phone"), "utf-8"));
			}
			String openid = request.getParameter("openid");
			logger.info("authLogin takeOpenid"+openid);//传入的openid,没用到
			String code = request.getParameter("code");
			logger.info("authLogin code"+code);
			
			if (StringUtils.isBlank(code)) { // 判断请求回来 的code是否有值
				String REDIRECT_URI = Contants.BIN_HUI_URL + "/bh-order-api/wetchat/authLogin";// 当前的接口
				// 请求code 的url
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				url = url.replace("APPID", URLEncoder.encode(Contants.appId, "utf-8"));
				url = url.replace("REDIRECT_URI", URLEncoder.encode(REDIRECT_URI, "utf-8"));
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.sendRedirect(url);
			}			
			if (StringUtils.isNotBlank(code)) {				
				Map<String, Object> map = WXPayUtil.getAccessToken(Contants.appId, Contants.appSecret,code); //用code获取access_token、openid
				String lang = "zh_CN";
				Map<String, Object> mapUser = WXPayUtil.getUser(String.valueOf(map.get("access_token")),
						String.valueOf(map.get("openid")), lang);//用access_token, openid,lang获取当前登录的用户信息
				
				if (!String.valueOf(mapUser.get("errmsg")).equals(" invalid openid ")) {
					String json = (String) session.getAttribute("json");
					String phone = (String) session.getAttribute("phone");
					logger.info("authLogin phone"+phone);
					logger.info("authLogin json"+json);
					logger.info("authLogin openid"+String.valueOf(map.get("openid")));
					 						
					wechatService.saveOrUpdateUserLogin(String.valueOf(map.get("openid")),mapUser,phone,json,request,response);
					
					logger.info("authLogin callBackUrl"+(String) session.getAttribute("callBackUrl"));	
					response.sendRedirect((String) session.getAttribute("callBackUrl"));// 授权成功跳转	
				}else{
					response.sendRedirect(Contants.BIN_HUI_URL + "/binhuiApp/login");//授权失败跳转到登录页
				}	
			}else{
				response.sendRedirect(Contants.BIN_HUI_URL + "/binhuiApp/login");//授权失败跳转到登录页
			}
		} catch (Exception e) {
			logger.error("authLogin exception"+e.getMessage());
			e.printStackTrace();
		}
	}

	//小程序 授权登录zlk 2018.3.17
	@RequestMapping("/mAuthLogin")
	@ResponseBody
	public BhResult mAuthLogin(@RequestBody Map<String,Object> map,HttpServletRequest request, HttpServletResponse response) {
		BhResult  result;
		Base64 base64 = new Base64();
		
		try{
			 //1.获取前端传来的code
			 String code =map.get("code").toString(); 
			 JSONObject jb = JSONObject.fromObject(map.get("userInfo"));
//			 System.out.println("jb========>"+jb);
//			 System.out.println("code========>"+code);
			
		  
			 Member m = new Member();
			 if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(code)){
			        //2.用code 获取session_key、openid
				    //小程序 APP_ID
				    final String APP_ID="wxa4b6bd7f420e9fb2";    
				    //小程序 APP_SECRET
				    final String APP_SECRET = "bb831a4aa207431c2486b850ad09dd93";
			        Map<String, Object> map3 =  WXPayUtil.getOpenId(APP_ID, APP_SECRET, code);
//				    Map<String, Object> map3 =  WXPayUtil.getUnionid(APP_ID, APP_SECRET, code);
			        //System.out.println("map3========>"+map3.toString());
			        //System.out.println("openid========>"+map3.get("openid"));
			
				    Member member = new Member();
				    member.setsOpenid(map3.get("openid").toString());
//				    member.setUnionid(map3.get("unionid").toString());
				   // System.out.println("unionId9:  "+map.get("encryptedData") +"  "+map3.get("session_key")+"  "+String.valueOf(map.get("iv")));
				    String result1 = AesCbcUtil.decrypt(String.valueOf(map.get("encryptedData")), String.valueOf(map3.get("session_key")),String.valueOf(map.get("iv")), "UTF-8");  
			        
				   // System.out.println("unionId9:  "+result1);
				    if (null != result1 && result1.length() > 0) {
		                JSONObject userInfoJSON = JSONObject.fromObject(result1);
		                Map userInfo = new HashMap();
		                userInfo.put("openId", userInfoJSON.get("openId"));
		                userInfo.put("nickName", userInfoJSON.get("nickName"));
		                userInfo.put("gender", userInfoJSON.get("gender"));
		                userInfo.put("city", userInfoJSON.get("city"));
		                userInfo.put("province", userInfoJSON.get("province"));
		                userInfo.put("country", userInfoJSON.get("country"));
		                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
		                userInfo.put("unionId", userInfoJSON.get("unionId"));
		                System.out.println("unionId9:  "+userInfoJSON.get("unionId"));
		            }
				    
				    
				    //根据sopenid 获取数据
				    List<Member> list = memberMapper.selectByUnionid(member);
				    if(list.size()>0){
			        	     if(StringUtils.isBlank(list.get(0).getsOpenid())||!list.get(0).getUsername().equals(jb.get("nickName"))||!list.get(0).getHeadimgurl().equals(jb.get("avatarUrl"))){
			        	        String nickName=URLEncoder.encode(jb.get("nickName").toString(),"utf-8");
			        	    	 list.get(0).setUsername(nickName);
			        	         list.get(0).setHeadimgurl(jb.get("avatarUrl").toString());
			        	         list.get(0).setsOpenid(map3.get("openid").toString());
			        	         memberMapper.updateByPrimaryKeySelective(list.get(0));
			        	     }
				             //存在数据
			        	     HttpSession sesson = request.getSession();
			        	     m = memberMapper.selectByPrimaryKey(list.get(0).getId());
			        	     m.setWaitTime("DTL_SESSION_ID");
			        	     m.setTeamNo(request.getSession().getId());
                             m.setUsername(new String(URLDecoder.decode(m.getUsername().toString(),"utf-8")));
                             sesson.setAttribute(Contants.USER_INFO_ATTR_KEY,m);
				             
				    }else{
					         //不存在数据
				    	String nickName=URLEncoder.encode(jb.get("nickName").toString(),"utf-8");
					         member.setUsername(nickName);
				             member.setHeadimgurl(jb.get("avatarUrl").toString());
				             
				             //保存当前的用户数据到表中
				             memberMapper.insertSelective(member);
				                                                      
			        	     HttpSession sesson = request.getSession();
			        	     m = memberMapper.selectByPrimaryKey(member.getId());
			        	     m.setWaitTime("DTL_SESSION_ID");
			        	     m.setTeamNo(request.getSession().getId());
			        	     String userName=new String(URLDecoder.decode(m.getUsername().toString(),"utf-8"));
			        	     m.setUsername(userName);
			        	    
                   
                             sesson.setAttribute(Contants.USER_INFO_ATTR_KEY,m);
                           
				             
				             //保存MemberUser信息
                             MemberUser memberUser2 = new MemberUser();
                             memberUser2.setmId(member.getId());
                             memberUser2.setAddtime(new Date());
                             //获取签到规则： 1，签到 2,浇水 3,拼团 4单买 5,分享,6注册积分，7购物积分
                             SeedScoreRule se = new SeedScoreRule();
                             se.setScoreAction(6);
                             List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
                             if (rule.size() > 0) {
                                //状态 0关 1开
                                if (rule.get(0).getStatus().equals(1)) {
                                        memberUser2.setPoint(rule.get(0).getScore());
                                        
                                        MemerScoreLog  log=new MemerScoreLog();
            	          				log.setCreateTime(new Date());
            	          				log.setmId(member.getId());
            	          				log.setIsDel(0);
            	          				log.setSmId(-2);
            	          				log.setSsrId(1);
            	          				log.setScore(rule.get(0).getScore());
            	          				log.setOrderseedId(0);
            	          				memerScoreLogMapper.insertSelective(log);
            	                    
                                 }
                             }
                             memberUser2.setNote("test the double account:WeChat Test");
                             memberUserMapper.insertSelective(memberUser2);
                             
                                      
                             
                             //保存信息到Wallet 表

               			     Wallet entity = new Wallet();
               			     //32位盐值
               			     entity.setSalt(TopicUtils.getRandomString(32));
               			     //用户id
               			     entity.setUid(member.getId());
               			     //钱包名称
               			     entity.setName("个人账户余额");
               			     //钱包类型
               			     entity.setType("1");
               			     //钱包说明
               			       entity.setDes("个人钱包");
               			     entity.setMoney(0);

               			     //判断是否有数据
               			     List<Wallet> list2 = walletMapper.getWalletByUid(entity.getUid());
               			     if(list2!=null&&list2.size()>0){
               				
               			     }else{
               				    walletMapper.insertSelective(entity);
               			     }
				    }
				    
			  }else{
				 return result = new BhResult(400,"参数不能为空",null);
			  }
			  result = new BhResult(200,"成功",m);
		}catch(Exception e){
			logger.info("#######Exception#######");
			e.printStackTrace();
			 result = new BhResult(400,"失败",null);
		}
		return result;
		
	}
	
	//公众号 授权登录zlk 2018.4.12   modelAndView 跳转方式
	@RequestMapping(value="/modelAndView")
	public ModelAndView  modelAndView(HttpServletRequest request, HttpServletResponse response) {
		  
		try{
			  //重复请求code
			  String code = request.getParameter("code");
              //判断请求回来 的code是否有值
			  if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){

			        String appid= Contants.appId;
			        //当前的接口
			        String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-order-api/wetchat/modelAndView";
			        //请求code 的url
			        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
			        //替换字符串，用 java.net.URLEncoder.encode(appid, "utf-8") 防止线上环境乱码
			        url  = url.replace("APPID", URLEncoder.encode(appid, "utf-8"));
			        url  = url.replace("REDIRECT_URI", URLEncoder.encode(REDIRECT_URI, "utf-8"));
			        //1.请求接口获取code
			        response.setHeader("Access-Control-Allow-Origin", "*");
			        response.sendRedirect(url);
			 }
			 System.out.println("code========>"+code);
			
			 if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(code)){
			        //2.用code 获取access_token、openid
			        Map<String, Object> map3 =  WXPayUtil.getAccessToken(Contants.appId, Contants.appSecret, code);
			        System.out.println("access_token========>"+map3.get("access_token"));
			        System.out.println("openid========>"+map3.get("openid"));
			
			        //3.用access_token, openid, lang获取当前登录的用户信息
			        String lang = "zh_CN";			 
			        Map<String, Object> map2 = WXPayUtil.getUser(String.valueOf(map3.get("access_token")),String.valueOf(map3.get("openid")), lang);
              
			        System.out.println("==============>"+map2.get("nickname"));
			       
			        if(!String.valueOf(map2.get("errmsg")).equals(" invalid openid ")){
				 
				        Member member = new Member();
				        member.setOpenid(String.valueOf(map3.get("openid")));
				        member.setUnionid(String.valueOf(map3.get("unionid")));
				        //根据openid 获取数据
				        List<Member> list = memberMapper.selectByOpenId(member);
//					    List<Member> list3 = memberMapper.selectByUnionid(member);
				        if(list.size()>0){
				        	 if(StringUtils.isBlank(list.get(0).getUnionid())||!list.get(0).getUsername().equals(new String(map2.get("nickname").toString().getBytes("ISO-8859-1"),"UTF-8"))||!list.get(0).getHeadimgurl().equals(String.valueOf(map2.get("headimgurl")))){
				        	     list.get(0).setUsername(new String(map2.get("nickname").toString().getBytes("ISO-8859-1"),"UTF-8"));
				        	     list.get(0).setHeadimgurl(String.valueOf(map2.get("headimgurl")));
				        	     list.get(0).setUnionid(String.valueOf(map3.get("unionid")));
				        	     memberMapper.updateByPrimaryKeySelective(list.get(0));
				        	 }
					         //存在数据
	                         HttpSession sesson = request.getSession(false);
	                         Member m = memberMapper.selectByPrimaryKey(list.get(0).getId());
	                         sesson.setAttribute(Contants.USER_INFO_ATTR_KEY,m);
				             response.sendRedirect(Contants.BIN_HUI_URL+"/binhuiApp/home");
				        }else{
					         //不存在数据
					         member.setUsername( new String(map2.get("nickname").toString().getBytes("ISO-8859-1"),"UTF-8"));
				             member.setHeadimgurl(String.valueOf(map2.get("headimgurl")));
				             //保存当前的用户数据到表中
				             memberMapper.insertSelective(member);
				             HttpSession sesson = request.getSession(false);
	                         Member m = memberMapper.selectByPrimaryKey(member.getId());
	                         sesson.setAttribute(Contants.USER_INFO_ATTR_KEY,m);
	                           
	                         try{  //保存MemberUser信息
	                              MemberUser memberUser2 = new MemberUser();
	                              memberUser2.setmId(m.getId());
	                              memberUser2.setAddtime(new Date());
	                              //获取签到规则： 1，签到 2,浇水 3,拼团 4单买 5,分享,6注册积分，7购物积分
	                              SeedScoreRule se = new SeedScoreRule();
	                              se.setScoreAction(6);
	                              List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
	                              if (rule.size() > 0) {
	                                 //状态 0关 1开
	                                 if (rule.get(0).getStatus().equals(1)) {
	                                         memberUser2.setPoint(rule.get(0).getScore());
	                                         
	                                         MemerScoreLog  log=new MemerScoreLog();
	             	          				log.setCreateTime(new Date());
	             	          				log.setmId(member.getId());
	             	          				log.setIsDel(0);
	             	          				log.setSmId(-2);
	             	          				log.setSsrId(1);
	             	          				log.setScore(rule.get(0).getScore());
	             	          				log.setOrderseedId(0);
	             	          				memerScoreLogMapper.insertSelective(log);
	             	                    
	                                  }
	                              }
	                              memberUser2.setNote("test the double account:WeChat Test");
	                              memberUserMapper.insertSelective(memberUser2);

                                  //保存信息到Wallet 表

	                			  Wallet entity = new Wallet();
	                			  //32位盐值
	                			  entity.setSalt(TopicUtils.getRandomString(32));
	                			  //用户id
	                			  entity.setUid(m.getId());
	                			  //钱包名称
	                			  entity.setName("个人账户余额");
	                			  //钱包类型
	                			  entity.setType("1");
	                			  //钱包说明
	                			  entity.setDes("个人钱包");
	                			  entity.setMoney(0);

	                			  //判断是否有数据
	                			  List<Wallet> list2 = walletMapper.getWalletByUid(entity.getUid());
	                			  if(list2!=null&&list2.size()>0){
	                				
	                			  }else{
	                				walletMapper.insertSelective(entity);
	                			  }
	                		
	                          }catch(Exception e){
	                    	      e.printStackTrace();
	                          }
	                          
	                          //授权成功跳转到首页
	                         String url = Contants.BIN_HUI_URL+"/binhuiApp/home";
	                         return new ModelAndView(url);  
				        }
			         }else{
			        	     //授权失败跳转到登录页
			        	 String url = Contants.BIN_HUI_URL+"/binhuiApp/login";
			        	 return new ModelAndView(url);            
			         }
			 }
		}catch(Exception e){
			logger.info("#######Exception#######");
			e.printStackTrace();
			
		}
		
		 String url = Contants.BIN_HUI_URL+"/binhuiApp/home";
         return new ModelAndView(url);    
	}
	
	/**
	 * @Description: 储存过程测试
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/testProcedure")
	@ResponseBody
	public BhResult testProcedure(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			String srcUserId=map.get("srcUserId");
			String destUserId=map.get("destUserId");
			wechatService.wxMergePhone(Integer.valueOf(srcUserId),Integer.valueOf(destUserId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 保存整合记录
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/saveMergeLog")
	@ResponseBody
	public BhResult saveMergeLog(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			String srcUserId=map.get("srcUserId");
			String destUserId=map.get("destUserId");
			String openid =map.get("openid");
			
			wechatService.saveMergeLog(Integer.valueOf(srcUserId),Integer.valueOf(destUserId),openid);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
}
