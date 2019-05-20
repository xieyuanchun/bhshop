package com.bh.user.api.controller;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.pojo.VilidatePhone;
import com.bh.utils.JsonUtils;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.RegExpValidatorUtils;

@Controller
public class JavaMail {
	private static final Logger logger = LoggerFactory.getLogger(JavaMail.class);
	@Value("${EMAILCONTANT}")
	private String EMAILCONTANT;

	// 使用JavaMail技术发送邮件
	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	@ResponseBody
	public BhResult sendMail(@RequestBody Map<String, String> map, HttpServletRequest request, HttpSession session1) {
		BhResult bhResult = null;
		try {
			String email = map.get("email");
			if (RegExpValidatorUtils.isEmail(email)) {
				// 1.创建Session对象：连接服务器
				// 参数一：代表连接服务器所需配置
				// 参数二：登录所需的用户名和密码
				Properties props = new Properties();
				// 服务器地址(端口默认为25)
				props.setProperty("mail.host", "smtp.mxhichina.com");

				// 是否为验证登录
				props.setProperty("mail.smtp.auth", "true");
				Session session = Session.getDefaultInstance(props, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("chengfengyun@zhiyesoft.com", "cheng18877827237*");
					}

				});

				// 打开debug:看到整个邮件发送的过程
				session.setDebug(false);

				// 2.写邮件
				MimeMessage mail = new MimeMessage(session);

				// 发件人
				mail.setFrom(new InternetAddress("chengfengyun@zhiyesoft.com"));
				// 收件人
				// 参数一：收件类型
				// TO: 收件人
				// CC: 抄送人
				// BCC: 密送人
				// A->B(TO) C(CC) D(BCC)
				// 参数二：收件人地址
				mail.setRecipient(RecipientType.TO, new InternetAddress(email));
				// user.setValidateCode(MD5Util.encode2hex(email));

				StringBuffer sb = new StringBuffer("点击下面链接激活账号，24小时生效，否则重新获取，链接只能使用一次，请尽快激活！</br>");
				sb.append("尊敬的滨惠  用户：</br>" +

				"您好！此电子邮件地址正在用于恢复某个 滨惠  帐号。如果是您本人启动了该恢复流程，请输入下方显示的数字验证码。</br>" +

				"如果您并未启动帐号恢复流程，并且有滨惠  帐号与此电子邮件地址相关联，则可能是其他人在尝试访问您的帐号。请勿将此验证码转发给或提供给任何人。请访问您帐号的登录与安全设置，确保您的帐号安全无虞。</br>");
				String code = MixCodeUtil.getCode(6);
				sb.append("<strong>" + " <div style='text-align:center;font-size:30px'>" + code + "</div>"
						+ "</strong> </br>");
				sb.append("此致</br> " + "滨惠 开发团队敬上</br>");
				sb.append("此电子邮件地址无法接收回复。如需更多信息，请访问" + Contants.BIN_HUI_URL + "/binhui/home 帮助中心。");
				// 主题
				mail.setSubject("滨惠  验证码");

				// 正文
				// 参数一：内容
				// 参数二：指定内容类型"<font color='red' size='+6'>这是邮件的正文3</font><br/>"
				mail.setContent(sb.toString(), "text/html;charset=utf-8");
				VilidatePhone vilidatePhone = new VilidatePhone();
				vilidatePhone.setPhone(email);
				vilidatePhone.setCode(Integer.parseInt(code));
				request.getSession(false).setAttribute(EMAILCONTANT, vilidatePhone);
				request.getSession(false).setMaxInactiveInterval(86400);// 60s就是1分钟
				// 3.发送

				Transport.send(mail);
				bhResult = new BhResult(200, "邮件发送成功", null);

			} else {
				bhResult = new BhResult(400, "邮箱格式错误！", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######sendMail#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	// 使用JavaMail技术发送邮件
	@RequestMapping(value = "/checkMail", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkMail(@RequestBody Map<String, String> map, HttpServletRequest request, HttpSession session1) {
		BhResult bhResult = null;
		try {
			String code = map.get("code");
			String email = map.get("email");
			if (StringUtils.isEmpty(code)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else if (StringUtils.isEmpty(email)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				if (RegExpValidatorUtils.isEmail(email)) {
					Object o = request.getSession(false).getAttribute(EMAILCONTANT);
					if (o != null) {
						String value = JsonUtils.objectToJson(o);
						VilidatePhone vilidatePhone = JsonUtils.jsonToPojo(value, VilidatePhone.class);
						if ((vilidatePhone.getCode().equals(Integer.parseInt(code)))
								&& (vilidatePhone.getPhone().equals(email))) {
							bhResult = new BhResult(200, "验证码正确", null);
						} else if (!vilidatePhone.getCode().equals(code)) {
							bhResult = new BhResult(400, "验证码不正确", null);
						} else if (!email.equals(vilidatePhone.getPhone())) {
							bhResult = new BhResult(400, "手机号不正确", null);
						}
					} else {
						bhResult = new BhResult(500, "手机验证过期了", null);
					}
				} else {
					bhResult = new BhResult(400, "邮箱格式错误！", null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkMail#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}
}
