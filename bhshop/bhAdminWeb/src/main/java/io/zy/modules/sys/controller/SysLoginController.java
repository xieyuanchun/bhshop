package io.zy.modules.sys.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import io.zy.common.annotation.SysLog;
import io.zy.common.utils.R;
import io.zy.common.utils.RedisUtils;
import io.zy.common.utils.ShiroUtils;
import io.zy.modules.sys.entity.SysUserEntity;
import io.zy.modules.sys.service.SysUserService;
import io.zy.modules.sys.service.SysUserTokenService;

import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private RedisUtils redisUtils;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@SysLog("用户登录")
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestParam String username, @RequestParam Map<String,Object> params)throws IOException {
		String password = (String) params.get("password");
		String captchaIn = (String) params.get("captcha");
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captchaIn.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}
      //   Map<String,Object> map = new HashMap<String,Object>();
		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		
	//	map.put("user", user);
		user.setPassword("");
		r.put("user", user);
		String token = (String)r.get("token");
		System.out.println("token--->"+token);
		redisUtils.set(token, user);
		/*if (StringUtils.isNotBlank(token)) {
			String ret = redisUtils.get(token);
			if (StringUtils.isNotBlank(ret)) {
				ShiroUtils.setSessionAttribute("token", token);
			}
		}*/
		
		//map.put("r", r);
		return r;
	}
	
}
