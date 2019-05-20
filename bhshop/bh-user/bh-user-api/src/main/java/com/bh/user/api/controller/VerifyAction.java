package com.bh.user.api.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.pojo.VilidatePhone;
import com.bh.utils.JsonUtils;


@Controller
public class VerifyAction {
	private static final Logger logger = LoggerFactory.getLogger(VerifyAction.class);
	@Value("${SmsContent}")
	private String SmsContent;// 一个对象：手机号+手机验证码
	@Value("${VERIFY_CODE}")
	private String VERIFY_CODE;
	@Value("${VERIFY_CODE_TIME}")
	private String VERIFY_CODE_TIME;	//过期时间，默认是15分钟
	/** 定义验证码图片的宽度 */
	private static final int IMG_WIDTH = 80;
	/** 定义验证码图片的高度 */
	private static final int IMG_HEIGHT = 22;
	/** 定义一个Random对象 */
	private static Random random = new Random();
	/** 定义字体对象 */
	private static Font font = new Font("宋体", Font.BOLD, 22);
	
	// 2017-9-22
	@RequestMapping(value = "/checkphonecode", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkPhoneCode(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpSession session) {
		try {
			BhResult result = null;
			//手机号
			String mobile = map.get("mobile");
			//验证码
			String code = map.get("code");
			//获得session的对象
			Object o = request.getSession(false).getAttribute(SmsContent);
			if (o != null) {
				String value = JsonUtils.objectToJson(o);
				VilidatePhone vilidatePhone = JsonUtils.jsonToPojo(value, VilidatePhone.class);
				if ((vilidatePhone.getCode().equals(Integer.parseInt(code))) && (vilidatePhone.getPhone().equals(mobile))) {
					result = new BhResult(200, "验证码正确", null);
				} else if (!vilidatePhone.getCode().equals(code)) {
					result = new BhResult(400, "验证码不正确", null);
				} else if (!mobile.equals(vilidatePhone.getPhone())) {
					result = new BhResult(400, "手机号不正确", null);
				}
			} else {
				result = new BhResult(500, "手机验证过期了", null);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkphonecode#######" + e);
			return BhResult.build(BhResultEnum.VISIT_FAIL);
		}
	}

	//zlk 2018.6.20
	@RequestMapping(value = "/mCheckPhoneCode", method = RequestMethod.POST)
	@ResponseBody
	public BhResult mCheckPhoneCode(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpSession session) {
		try {
			BhResult result = null;
			//验证码
			String code = new Sha256Hash(map.get("code"),"YKD1vVuPJEbrJdRyNeEr").toHex();
			//验证码
		    String mobile_code = map.get("mobile_code");
			if (mobile_code.equals(code)) {
					result = new BhResult(200, "验证码正确", null);
			} else {
					result = new BhResult(400, "验证码不正确", null);
			} 
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######mCheckPhoneCode#######" + e);
			return BhResult.build(BhResultEnum.VISIT_FAIL);
		}
	}

	
	
	
	
	@RequestMapping(value ="/validate.jpg",method = RequestMethod.GET)
	@ResponseBody
	public void getViladate (HttpServletResponse response,HttpServletRequest request) throws Exception{
		/** 获取HttpServletResponse */
		/** 设置响应的内容类型 */
		response.setContentType("images/jpeg");
		/** 创建一个图片缓冲流对象 */
		BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
		/** 获取到画笔 */
		Graphics g = image.getGraphics();
		/** 填充一个矩形框 */
		g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
		/** 设置画笔的颜色 */
		g.setColor(Color.BLACK);
		/** 绘制一个矩形框 */
		g.drawRect(0, 0, IMG_WIDTH - 1, IMG_HEIGHT - 1);
		/** 绘制干扰线 */
		for (int i = 0; i < 50; i++){
			/** 设置画笔的颜色(颜色是随机生成) */
			g.setColor(new Color(180 + random.nextInt(75), 
								 180 + random.nextInt(75),
								 180 + random.nextInt(75)));
			// 第一点
			int x1 = 2 + random.nextInt(IMG_WIDTH - 4);
			int y1 = 2 + random.nextInt(IMG_HEIGHT - 4);
			// 第二点
			int x2 = 2 + random.nextInt(IMG_WIDTH - 4);
			int y2 = 2 + random.nextInt(IMG_HEIGHT - 4);
			g.drawLine(x1, y1, x2, y2);
		}
		
		/** 绘制验证码(随机生成四个验证码) */
		g.setFont(font); // 设置字体
		String code = ""; // 保存最终生成的验证码
		for (int i = 0; i < 4; i++){
			String temp = generatorVerify();
			code += temp;
			/** 设置画笔的颜色(颜色是随机生成) */
			g.setColor(new Color(random.nextInt(20), 
								 random.nextInt(40),
								 random.nextInt(20)));
			
			int offsetLeft = transferFrom(g);
			
			g.drawString(temp, 17 * i + offsetLeft, 18);
		}
		HttpSession sesson = request.getSession(false);
		sesson.setAttribute(VERIFY_CODE, code);
		
		/** 消毁画笔 */
		g.dispose();
		/** 输出 */
		response.setHeader("Cache-Control", "no-store, no-cache");

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}
	/**
	 * 画笔位置倾斜方法
	 * @param g
	 * @return
	 */
	private int transferFrom(Graphics g) {
		Graphics2D gr = (Graphics2D)g;
		AffineTransform tr =  gr.getTransform();
		// 随机生成倾斜率
		double shx = Math.random();
		// 保证倾斜率在(0.25-0.55)之间
		if (shx < 0.25) shx = 0.25;
		if (shx > 0.55) shx = 0.55;
		// 随机向右是左倾斜
		int temp = random.nextInt(2);
		int offsetLeft = 2;
		if (temp == 0){
			shx = 0 - shx;
			offsetLeft = 10;
		}
		tr.setToShear(shx, 0);
		gr.setTransform(tr);
		return offsetLeft;
	}
	/**
	 * 随机生成一个验证码(大写字母、小写字母、数字、汉字)
	 * @return
	 */
	private String generatorVerify(){
		/** 随机生成0-3之间的数字 */
		int witch = (int)Math.round((Math.random() * 2));
		witch = 2;
		switch (witch){
			case 0: // 生成大写字母(A-Z|65-90)
				long temp = Math.round(Math.random() * 25 + 65);
				return String.valueOf((char)temp);
			case 1: // 生成小写字母(a-z|97-122)
				temp = Math.round(Math.random() * 25 + 97);
				return String.valueOf((char)temp);
			case 2: // 生成数字(0-9)
				return String.valueOf(Math.round(Math.random() * 9));
			default: // 生成汉字(0x4E00-0x9FBF)
				temp = Math.round(Math.random() * 500 + 0x4E00);
				return String.valueOf((char)temp);
		}
	}
	
	
	// 2017-10-9  验证图形验证码是否正确
	@RequestMapping(value = "/checkverifycode", method = RequestMethod.GET)
	@ResponseBody
	public BhResult checkVerifyCode(String code, HttpServletRequest request,
			HttpSession session) {
		try {
			BhResult result = null;
			String code1 = code;//传过来的图形验证码
			String o = (String) request.getSession(false).getAttribute(VERIFY_CODE);
			if (o != null && !o.equals("")) {
				if (o.equalsIgnoreCase(code1)) {
					result = new BhResult(200, "验证码正确", null);
				} else {
					result = new BhResult(400, "图形验证码不正确", null);
				}
			} else {
				result = new BhResult(500, "验证码不能为空", null);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkverifycode#######" + e);
			return BhResult.build(BhResultEnum.VISIT_FAIL);
		}
	}
		
}
