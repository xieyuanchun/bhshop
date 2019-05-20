package com.bh.admin.controller.goods;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.admin.service.MBusMSGService;
import com.bh.admin.pojo.user.MBusEntity;


@Controller
public class MBusMSGController {
	@Autowired
	private MBusMSGService mBusMSGService;
	
	
	@RequestMapping(value = "/mlogin", method = RequestMethod.POST)
	@ResponseBody
	public BhResult login(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult result = null;
		try {
			String username = map.get("username");
			String password = map.get("password");
			String kaptcha = map.get("kaptcha");
			String o = (String) request.getSession().getAttribute("VERIFY_CODE");
			System.out.println("在mbusMSGController类中的request.getSession().getAttribute(VERIFY_CODE)------->" + o);
			System.out.println("--------->o=" + o);
			//是否为1，如果是1的话不验证kaptcha图形验证码，否则需要验证验证码
			String isCheck = map.get("isCheck");
			if ((StringUtils.isNotEmpty(isCheck)) && (isCheck.equals("1"))) {
				
			}else{
				if(StringUtils.isEmpty(o)){
				      return result = new BhResult(400, "验证码已失效", null);
			    }
			    if(!o.equalsIgnoreCase(kaptcha)){
				      return result = new BhResult(400, "验证码不正确", null);
			    }
			}
			
			//用户信息
			MBusEntity user = mBusMSGService.queryByUserName(username);

			//账号不存在、密码错误
			if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
				return result = new BhResult(400, "账号或密码不正确", null); 
			}

			//账号锁定
			if(user.getStatus() == 0){
				return result = new BhResult(400, "账号已被锁定,请联系管理员", null); 
			}
			
			else{
			/*	if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}*/
				HttpSession session = request.getSession();
				session.setAttribute(Contants.MUSER, user);
				return result = new BhResult(200, "登录成功", null); 
			}
			
       /* Map<String,Object> map = new HashMap<String,Object>();
		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		
	map.put("user", user);
			map.put("r", r);
			return map;*/
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return result;
	}
	
	
	@RequestMapping(value = "/isMLogin", method = RequestMethod.POST)
	@ResponseBody
	public BhResult IsMLogin(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user == null) {
				result = new BhResult(100,"您还未登录,请重新登录",null);
			}
			else{
				result = new BhResult(200,"该商家已登陆",user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value = "/mlogout", method = RequestMethod.POST)
	@ResponseBody
	public BhResult log(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			request.getSession().removeAttribute(Contants.MUSER);
			request.getSession().invalidate();
			bhResult = new BhResult(200, "用户退出成功", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bhResult;
	}
	
	@RequestMapping(value = "/mcheckMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkMsg(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String username = map.get("username");
			String phone = map.get("phone");
			bhResult = mBusMSGService.checkMsg(username, phone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bhResult;
	}
	
	@RequestMapping(value = "/mforgetpwd", method = RequestMethod.POST)
	@ResponseBody
	public BhResult mforgetpwd(@RequestBody Map<String, String> map, HttpServletResponse response,
			HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String username = map.get("username");
			String phone = map.get("phone");
			String pwd = map.get("password");
			bhResult = mBusMSGService.updatePwd(username, phone,pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bhResult;
	}
	
	


}
