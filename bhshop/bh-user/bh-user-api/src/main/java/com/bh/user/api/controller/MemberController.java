package com.bh.user.api.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.result.BhResult;

import com.bh.user.api.service.MemberService;
import com.bh.user.pojo.*;
import com.bh.user.pojo.Member;
import com.bh.user.vo.MemberVo;
import com.bh.utils.JsonUtils;
import com.bh.utils.MD5Util1;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.WxJsApiUtil;

@Controller
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Value("${ONLINETIME}")
	private Integer ONLINETIME;// 用户登录成功sessio过期时间
	@Value("${SmsContent}")
	private String SmsContent;// 一个对象：手机号+手机验证码
	@Value("${SmsContentTime}")
	private String SmsContentTime;// 过期时间，默认是1分钟
	@Value("${USERINFO}")
	private String USERINFO; // 用户信息
	@Value("${VERIFY_CODE}")
	private String VERIFY_CODE;
	@Value("${VERIFY_CODE_TIME}")
	private Integer VERIFY_CODE_TIME;

	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	@ResponseBody
	public BhResult createuser(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult result = null;
		try {

			// 1为普通会员,2为商家
			String username = map.get("username");
			String pwd = map.get("password");
			String headimgurl = map.get("headimgurl");
			String phone = map.get("phone");
			String password = MixCodeUtil.washStr(pwd);
			String type = map.get("type");
			String salt = "";
			String im = map.get("im");
			Member member = new Member();
			if (StringUtils.isEmpty(username)) {
				result = new BhResult(400, "用户名不能为空", null);
			} else {
				member.setUsername(username);
			}
			if (StringUtils.isEmpty(password)) {
				result = new BhResult(400, "密码不能为空", null);
			} else {
				String first = MD5Util1.encode(password);
				salt = MD5Util1.genCodes(first);
				first = first + salt;
				String second = MD5Util1.encode(first);
				member.setPassword(second);
				member.setSalt(salt);
			}
			if (StringUtils.isEmpty(im)) {
				member.setIm("0");
			} else {
				member.setIm(im);
			}
			if (StringUtils.isEmpty(type)) {
				result = new BhResult(400, "注册的用户类型不能为空", null);
			} else {
				member.setType(Integer.parseInt(type));
			}
			if (StringUtils.isEmpty(phone)) {
				result = new BhResult(400, "注册的手机号不能为空", null);
			} else {
				Member member2 = new Member();
				member2.setPhone(phone);
				List<Member> member3 = memberService.selectMemberByPhone(member2);
				if (member3.size() > 0) {
					result = new BhResult(400, "该手机号已被注册过", null);
				} else {
					member.setPhone(phone);
				}

			}
			if (!StringUtils.isEmpty(headimgurl)) {
				member.setHeadimgurl(headimgurl);
			}
			if (org.apache.commons.lang.StringUtils.isEmpty(type)) {
				member.setType(1);
			}
			member.setIsNew(1); // 新用户标志

			int row = memberService.insertSelective(member, request, response);
			if (row == 0) {
				result = new BhResult(400, "请求失败", null);
			} else {
				result = new BhResult(200, "添加成功", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######createuser#######" + e);
			result = BhResult.build(500, "数据库更新失败!");
		}
		return result;
	}

	@RequestMapping(value = "/checkuser", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkuser(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			// 检查用户名是否存在
			String username = map.get("username");
			Member member = new Member();
			if (StringUtils.isEmpty(username)) {
				result = new BhResult(400, "请传用户名||用户名不能为空", null);
			} else {
				member.setUsername(username);
			}
			int row = memberService.checkuser(member);
			if (row == 0) {
				result = new BhResult(200, "该用户名可用", null);
			} else {
				result = new BhResult(400, "该用名已存在", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkuser#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public BhResult login(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult result = null;
		String username = map.get("username");
		String pwd = map.get("password");
		String password = "";
		String type = map.get("type");// 用户类型1为普通会员2为商家3配送员
		try {
			password = MixCodeUtil.washStr(pwd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######login#######" + e);
		}

		Member member = new Member();
		member.setUsername(username);
		member.setPassword(password);
		if (StringUtils.isEmpty(type)) {
			type = "1";
			member.setType(1);
		} else {
			member.setType(Integer.parseInt(type));
		}
		try {
			result = memberService.login(member, response, request);
			if (result.getStatus() == 200) {
				MemberVo memberVo = (MemberVo) result.getData();
				Member member2 = new Member();
				member2.setId(memberVo.getId());
				Member realM = memberService.selectById(member2);
				HttpSession session = request.getSession(true);
				memberVo.setLoginStatus(session.getId());
				session.setAttribute(USERINFO, realM);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("#######login#######" + e);
		}

		return result;
	}

	@RequestMapping(value = "/loginphone", method = RequestMethod.POST)
	@ResponseBody
	public BhResult loginphone(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult result = null;
		String phone = map.get("phone");
		try {
			Member member = new Member();
			member.setPhone(phone);
			int row = memberService.checkuser(member);
			if (row == 0) {
				result = new BhResult(300, "该手机号名不存在", null);
			} else if (row == 1) {
				result = new BhResult(200, "该手机号已存在", null);
			} else if (row > 1) {
				result = new BhResult(400, "该手机号已存在多个", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######loginphone#######" + e);
			result = new BhResult(500, "操作失败", null);
		}
		return result;
	}

	@RequestMapping(value = "/loginphonecode", method = RequestMethod.POST)
	@ResponseBody
	public BhResult loginphonecode(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpSession session) {
		BhResult result = null;
		String mobile = map.get("mobile");
		String code = map.get("code");
		try {
			Object o = request.getSession(true).getAttribute(SmsContent);
			if (o != null) {
				String value = JsonUtils.objectToJson(o);
				VilidatePhone vilidatePhone = JsonUtils.jsonToPojo(value, VilidatePhone.class);
				if ((vilidatePhone.getCode().equals(Integer.parseInt(code)))
						&& (vilidatePhone.getPhone().equals(mobile))) {
					Member member = new Member();
					member.setPhone(mobile);
					List<Member> realM = memberService.selectMemberByPhone(member);// 将member的Id放入redis中，并设置时间3*60*5
					request.getSession(true).setAttribute(USERINFO, String.valueOf(realM.get(0).getId()));
					result = new BhResult(200, "通过手机登录成功", realM);
				} else if (!vilidatePhone.getCode().equals(code)) {
					result = new BhResult(400, "验证码不正确", null);
				} else if (!mobile.equals(vilidatePhone.getPhone())) {
					result = new BhResult(400, "手机号不正确", null);
				}
			} else {
				// 验证码错误
				result = new BhResult(500, "验证码错误", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######loginphonecode#######" + e);
			result = new BhResult(500, "手机验证过期了", null);
		}
		return result;
	}

	@RequestMapping(value = "/cheng")
	@ResponseBody
	public BhResult cheng() {
		BhResult bhResult = null;
		try {
			bhResult = new BhResult(100, "需要重新登录", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cheng#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	// 退出
	@RequestMapping(value = "/member/logout", method = RequestMethod.POST)
	@ResponseBody
	public BhResult log(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if(session!=null) {
				session.removeAttribute(USERINFO);
				session.invalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######logout#######" + e);
		}
		return new BhResult(200, "操作成功", null);
	}

	/** 忘记密码 */
	@RequestMapping(value = "/forgetpwd", method = RequestMethod.POST)
	@ResponseBody
	public BhResult forgetpwd(@RequestBody Map<String, String> map) {
		BhResult bhResult = null;
		try {
			String username = map.get("username");
			String password = map.get("password");
			String phone = map.get("phone");
			Member member = new Member();
			String first = MD5Util1.encode(password);
			String salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			member.setPassword(second);
			member.setSalt(salt);
			member.setPhone(phone);
			member.setUsername(username);
			member.setIsNew(1);

			int row = memberService.updatepwd(member);
			if (row == 1) {
				bhResult = new BhResult(200, "密码更新成功", null);
			} else if (row == -1) {
				bhResult = new BhResult(300, "该号码未注册，请先注册！", null);
			} else {
				bhResult = new BhResult(400, "密码更新失败", null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("#######forgetpwd#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 商家入驻或者普通用户成为配送员的接口 */
	@RequestMapping(value = "/member/enterShop", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertshopMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			String type = map.get("type");
			Member member = new Member();
			member.setId(Integer.parseInt(mId));
			member.setType(Integer.parseInt(type));
			int row = memberService.updateByType(member);
			if (row == 1) {
				result = new BhResult(200, "入驻成功", null);
			} else {
				result = new BhResult(400, "入驻失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######enterShop#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	/** 检查电话是否存在 */
	@RequestMapping(value = "/checkphone", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkphone(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			// 检查用户名是否存在
			String phone = map.get("phone");
			Member member = new Member();
			if (StringUtils.isEmpty(phone)) {
				result = new BhResult(400, "手机号不能为空", null);
			} else {
				member.setPhone(phone);
			}
			int row = memberService.checkuser(member);
			if (row == 0) {
				result = new BhResult(200, "该手机号名可用", null);
			} else {
				result = new BhResult(400, "该手机号已存在", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkphone#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}
	/**
	 *  * 获取媒体文件  *   * @param accessToken  *      接口访问凭证  * @param media_id
	 *  *      媒体文件id  *
	 */
	@RequestMapping("/baseimg")
	@ResponseBody
	public static WangEditorInfo downloadMedia(@RequestBody Map<String, String> map, HttpServletRequest request) {
		// String serverId =
		// "P8lLdFpw8gP-NSQdKnCSDpUjCoNjtVIUycoK6zXnTy0iSWP1GZzvP_xKN9d6H-SD,5ZZlVYxINtNAQgB8oA2X4BUzrOqyaRR16LSyROsB38NYSB5TguOP7J-EqpSoNzeW,7vfErr0Ge-M986bNPc2xH3ODJz1PAVEnTio9nynPzv-BTmz_EqPQ6P5sgWOKrT-W";
		String serverId = map.get("serverId");
		String accessToken = WxJsApiUtil.getAccessToken(Contants.appId, Contants.appSecret);
		System.out.println("accessToken--------------->" + accessToken);
		HttpURLConnection conn = null;
		WangEditorInfo w = new WangEditorInfo();
		ArrayList<String> list = new ArrayList<>();
		try {
			List<String> singleServerId = JsonUtils.stringToList(serverId);
			for (String string : singleServerId) {
				String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
				requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", string);
				
				System.out.println("requestUrl----------------------------->" + requestUrl);
				
				
				URL url = new URL(requestUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				byte[] buff = new byte[100];
				int rc = 0;
				while ((rc = bis.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				String path = request.getSession(false).getServletContext().getRealPath("/");// 获得files目录的绝对路径
				
				System.out.println("path------------------>" + path);
				byte[] filebyte = swapStream.toByteArray();// 字节数组
				String key1 = "base64images/";// 主键识别
				String returnString = RegExpValidatorUtils.downloadMedia(path, filebyte, key1);
				
				
				System.out.println("returnString----------------------->" + returnString);
				if (returnString.equals("0")) {

				} else {
					list.add(returnString);
				}
			}
		} catch (Exception e) {
			logger.error("#######baseimg#######" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		w.setData(list);
		return w;
	}
	
	
	
	

}
