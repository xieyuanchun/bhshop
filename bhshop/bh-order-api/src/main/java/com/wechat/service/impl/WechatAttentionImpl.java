package com.wechat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.Wallet;
import com.bh.utils.TopicUtils;
import com.bh.utils.pay.WXPayUtil;
import com.wechat.service.WechatAttentionService;
import com.wechat.util.MessageUtil;
import com.wechat.vo.TextMessage;

@Service
public class WechatAttentionImpl implements WechatAttentionService {

	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private WalletMapper walletMapper;

	public static Logger log = Logger.getLogger(WechatAttentionImpl.class);

	@Override
	public String processRequest(HttpServletRequest request, HttpServletResponse response) {
		String respMessage = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.xmlToMap(request);

			// 发送方帐号(open_id)
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content = requestMap.get("Content");
			System.out.println("打印输入--------------------------->" + "FromUserName is:" + fromUserName
					+ ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreatTime(new Date().getTime() + "");
			textMessage.setMsgType(msgType);

			String respContent = "";

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 事件处理开始
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					// 关注
					respContent = "该公众号是测试用途\n";
					String code = request.getParameter("code");
					if (StringUtils.isEmpty(code)) {
						String appid = Contants.appId;
						// 当前的接口
						String REDIRECT_URI = Contants.BIN_HUI_URL + "/bh-order-api/wetchat/attention";
						// 请求code 的url
						String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
						// 替换字符串，用 java.net.URLEncoder.encode(appid, "utf-8")
						// 防止线上环境乱码
						url = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8"));
						url = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
						// 1.请求接口获取code
						response.setHeader("Access-Control-Allow-Origin", "*");
						response.sendRedirect(url);
					} else {
						// 2.用code 获取access_token、openid
						Map<String, Object> map3 = WXPayUtil.getAccessToken(Contants.appId, Contants.appSecret, code);
						// 3.用access_token, openid, lang获取当前登录的用户信息
						String lang = "zh_CN";
						Map<String, Object> map2 = WXPayUtil.getUser(String.valueOf(map3.get("access_token")),
								String.valueOf(map3.get("openid")), lang);
						if (!String.valueOf(map2.get("errmsg")).equals(" invalid openid ")) {

							Member member = new Member();
							member.setOpenid(String.valueOf(map3.get("openid")));
							member.setUnionid(String.valueOf(map3.get("unionid")));
							// 根据openid 获取数据
							List<Member> list = memberMapper.selectByOpenId(member);
							// List<Member> list3 =
							// memberMapper.selectByUnionid(member);
							if (list.size() > 0) {
								if (org.apache.commons.lang.StringUtils.isBlank(list.get(0).getUnionid())
										|| !list.get(0).getUsername().equals(new String(
												map2.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"))
										|| !list.get(0).getHeadimgurl()
												.equals(String.valueOf(map2.get("headimgurl")))) {
									list.get(0).setUsername(new String(
											map2.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"));
									list.get(0).setHeadimgurl(String.valueOf(map2.get("headimgurl")));
									list.get(0).setUnionid(String.valueOf(map3.get("unionid")));
									memberMapper.updateByPrimaryKeySelective(list.get(0));
								}
							} else {
								// 不存在数据
								member.setUsername(
										new String(map2.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"));
								member.setHeadimgurl(String.valueOf(map2.get("headimgurl")));
								// 保存当前的用户数据到表中
								memberMapper.insertSelective(member);
								try { // 保存MemberUser信息
									MemberUser memberUser2 = new MemberUser();
									memberUser2.setmId(member.getId());
									memberUser2.setAddtime(new Date());
									// 获取签到规则： 1，签到 2,浇水 3,拼团 4单买
									// 5,分享,6注册积分，7购物积分
									SeedScoreRule se = new SeedScoreRule();
									se.setScoreAction(6);
									List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
									if (rule.size() > 0) {
										// 状态 0关 1开
										if (rule.get(0).getStatus().equals(1)) {
											memberUser2.setPoint(rule.get(0).getScore());
										}
									}
									memberUser2.setNote("test the double account:微信关注");
									memberUserMapper.insertSelective(memberUser2);

									// 保存信息到Wallet 表

									Wallet entity = new Wallet();
									// 32位盐值
									entity.setSalt(TopicUtils.getRandomString(32));
									// 用户id
									entity.setUid(member.getId());
									// 钱包名称
									entity.setName("个人账户余额");
									// 钱包类型
									entity.setType("1");
									// 钱包说明
									entity.setDes("个人钱包");
									entity.setMoney(0);

									// 判断是否有数据
									List<Wallet> list2 = walletMapper.getWalletByUid(entity.getUid());
									if (list2 != null && list2.size() > 0) {

									} else {
										walletMapper.insertSelective(entity);
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

								// 授权成功跳转到首页
								// response.sendRedirect(Contants.BIN_HUI_URL+"/binhuiApp/home?fourRandom="+getFourRandom());
							}
						}

					}

				} else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息

				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
				}
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}
}
