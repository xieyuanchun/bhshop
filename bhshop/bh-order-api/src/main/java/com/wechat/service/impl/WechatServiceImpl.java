package com.wechat.service.impl;

import com.bh.config.Contants;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.order.mapper.OrderMapper;
import com.bh.user.mapper.*;
import com.bh.user.pojo.*;
import com.bh.utils.EmojiFilter;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.utils.TopicUtils;
import com.wechat.service.WechatServices;
import com.wechat.util.MessageUtil;
import com.wechat.vo.Article;
import com.wechat.vo.NewsMessage;
import com.wechat.vo.TextMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WechatServiceImpl implements WechatServices {
	private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);
	
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
    @Autowired
	private GoodsCartMapper goodsCartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	public CouponLogMapper couponLogMapper;
	@Autowired
	public OrderMapper orderMapper;
	@Autowired
	public MergeUserLogMapper mergeUserLogMapper;
	/**
	 * 处理微信发来的请求 , 微信自动回复
	 * @param request
	 * @return
	 */
	@Override
	public String weixinPost(HttpServletRequest request) {
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
		 
		      
		      logger.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);
		 
		      // 文本消息
		      if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
		        //这里根据关键字执行相应的逻辑
		        if(content.equals("你好")){
		        	content = "欢迎关注滨惠商城!";
		        }
		         
		        //自动回复
		        TextMessage text = new TextMessage();
		        text.setContent("这里是滨惠商城，" + content);
		        text.setToUserName(fromUserName);
		        text.setFromUserName(toUserName);
		        text.setCreatTime(new Date().getTime() + "");
		        text.setMsgType(msgType);
		         
		        respMessage = MessageUtil.textMessageToXml(text);
		     // 事件推送
		      }else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
		        String eventType = requestMap.get("Event");// 事件类型
		        // 订阅
		        if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
		           
		          TextMessage text = new TextMessage();
		          text.setContent("欢迎关注，滨惠商城");
		          text.setToUserName(fromUserName);
		          text.setFromUserName(toUserName);
		          text.setCreatTime(new Date().getTime() + "");
		          text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		           
		          respMessage = MessageUtil.textMessageToXml(text);
		          
			      
			      //保存当前的用户数据到表中
		          Member member = new Member();
			      member.setOpenid(fromUserName);
		          memberMapper.insertSelective(member);
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
                  
		       // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
		        }else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅
		           
		       // 自定义菜单点击事件
		        }else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
		          String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
		          if (eventKey.equals("商户注册")) {
		            //图文信息
		        	NewsMessage  nm = new NewsMessage();
		        	nm.setToUserName(fromUserName);
		        	nm.setFromUserName(toUserName); 
		        	nm.setCreatTime(new Date().getTime()+"");
		        	nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		        	
		        	Article ac = new Article();
		        	ac.setPicUrl("http://pic36.photophoto.cn/20150828/0007020110187315_b.jpg");
		        	ac.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/registerstatus?openId="+fromUserName);
		        	ac.setTitle("滨惠商城商户注册");
		        	ac.setDescribe("详情");
		        	List<Article> list = new ArrayList<Article>();
		        	list.add(ac);
		        	nm.setArticleCount(list.size());
		        	nm.setList(list);
		            respMessage = MessageUtil.newsMessageToxml(nm);
		          }
		        }
		      }
		    }
		    catch (Exception e) {
		    	logger.info("error......");
		    	//e.printStackTrace();
		    }
		    return respMessage;
	}

	/**
	 * @Description: 公众号 授权登录
	 * @author zlk
	 * @date 2018年8月31日 下午1:59:18
	 */
	public void saveOrUpdateUserLogin(String openid,Map<String, Object> mapUser, String phone, String json,
			HttpServletRequest request,HttpServletResponse response) {
		try {			
			Member member = new Member();
			member.setOpenid(openid);
			member.setUnionid(String.valueOf(mapUser.get("unionid")));
			
			List<Member> list = memberMapper.selectByOpenId(member);// 根据openid获取数据

			String username = null;
			boolean isContainsEmoji = EmojiFilter
					.containsEmoji(new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"));
			if (isContainsEmoji) {
				username = URLEncoder.encode(
						new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"), "utf-8");
			} else {
				username = new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8");
			}						
			if (list.size() > 0) {// 该微信已经注册
				if (StringUtils.isNotBlank(phone)) {// phone为null表示该用户微信号已经绑定过手机号了
					List<Member> listPhone = memberMapper.getByPhone(phone);
					if (listPhone.size() > 0) {// 手机号与微信号互整
						if(listPhone.get(0).getOpenid().equals("0")||StringUtils.isNotBlank(list.get(0).getPhone())){//如果手机号被已经绑定或微信已经绑定手机了
							//TODO 储存过程																			//不让它再次整合
							logger.info("authLogin step"+1);
							this.saveMergeLog(list.get(0).getId(), listPhone.get(0).getId(),list.get(0).getOpenid());
							memberMapper.mergeUserByWxAndPhone(list.get(0).getId(),listPhone.get(0).getId());//调用储存过程合并数据						
							member.setUsername(username);//微信昵号
							member.setHeadimgurl(String.valueOf(mapUser.get("headimgurl")));// 头像
							member.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号时间
							member.setId(listPhone.get(0).getId());//更新原来的手机号对应记录	
							memberMapper.updateByPrimaryKeySelective(member);// 更新用户信息
							this.authLoginDispose(listPhone.get(0).getId(), json, request);// 登入数据处理（购物车、钱包、签到）
						}else{
							response.sendRedirect(Contants.BIN_HUI_URL + "/binhuiApp/login");//授权失败跳转到登录页
						}
					} else {// 微信号绑定没有注册过的手机号
						logger.info("authLogin step"+2);
						member.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号时间
						member.setPhone(phone);
						member.setId(list.get(0).getId());//更新原来的微信号对应记录
						memberMapper.updateByPrimaryKeySelective(member);// 更新用户信息
						this.authLoginDispose(list.get(0).getId(), json, request);// 登入数据处理（购物车、钱包、签到）
					}
				}else{//只是登入
					logger.info("authLogin step"+3);
					member.setId(list.get(0).getId());//更新原来的微信号对应记录
					memberMapper.updateByPrimaryKeySelective(member);// 更新用户信息					
					this.authLoginDispose(list.get(0).getId(), json, request);// 登入数据处理（购物车、钱包、签到）
				}
			} else {// 该微信未注册
				member.setUsername(username);//微信昵号
				member.setHeadimgurl(String.valueOf(mapUser.get("headimgurl")));// 头像
				if (StringUtils.isNotBlank(phone)) {
					member.setPhone(phone);
				}
				List<Member> listPhone = memberMapper.getByPhone(phone);
				if (listPhone.size() > 0) {//手机号整合没有注册过的微信号
					logger.info("authLogin step"+4);
					member.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号时间
					member.setId(listPhone.get(0).getId());
					memberMapper.updateByPrimaryKeySelective(member);
					this.authLoginDispose(listPhone.get(0).getId(), json, request);// 登入数据处理（购物车、钱包、签到）==>这个所有数据读是重新插入
				} else {// 新用户注册（手机号与微信号都是没有注册过的）
					logger.info("authLogin step"+5);
					member.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号时间
					memberMapper.insertSelective(member); // 保存当前的用户数据到表中
					this.authLoginDispose(member.getId(), json, request);// 登入数据处理（购物车、钱包、签到）==>这个所有数据读是重新插入
				}
			}
		} catch (Exception e) {
			logger.error("authLogin"+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: 登入处理
	 * @author ZLK
	 * @date 2018年9月3日 下午1:36:09
	 *
	 */
	public void authLoginDispose(int mId, String json, HttpServletRequest request) {
		try {
			// 存在数据
			HttpSession sesson = request.getSession();
			Member m = memberMapper.selectByPrimaryKey(mId);
			sesson.setAttribute(Contants.USER_INFO_ATTR_KEY, m);

			if (StringUtils.isNotBlank(mId + "")) {
				// 程凤云 2018-7-23
				List<MemberUser> myMemberList = memberUserMapper.selectUserPoint(mId);
				MemberUser myMemberUser = new MemberUser();
				if (myMemberList.size() == 0) {
					// 获取签到规则： 1，签到 2,浇水 3,拼团 4单买 5,分享,6注册积分，7购物积分
					SeedScoreRule se = new SeedScoreRule();
					se.setScoreAction(6);
					List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
					if (rule.size() > 0) {
						// 状态 0关 1开
						if (rule.get(0).getStatus().equals(1)) {
							myMemberUser.setPoint(rule.get(0).getScore());
							MemerScoreLog log = new MemerScoreLog();
							log.setCreateTime(new Date());
							log.setmId(mId);
							log.setIsDel(0);
							log.setSmId(-2);
							log.setSsrId(1);
							log.setScore(rule.get(0).getScore());
							log.setOrderseedId(0);
							memerScoreLogMapper.insertSelective(log);
						}
					}
					myMemberUser.setmId(mId);
					myMemberUser.setAddtime(new Date());
					myMemberUser.setNote("test the double account:WeChat Test");
					memberUserMapper.insertSelective(myMemberUser);
				}
				List<Wallet> list2 = walletMapper.getWalletByUid(mId);
				if (list2.size() == 0) {
					Wallet entity = new Wallet();
					entity.setSalt(TopicUtils.getRandomString(32)); // 32位盐值
					entity.setUid(mId); // 用户id
					entity.setName("个人账户余额");// 钱包名称
					entity.setType("1");// 钱包类型
					entity.setDes("个人钱包"); // 钱包说明
					entity.setMoney(0);
					walletMapper.insertSelective(entity);
				}

				// 购物车
				if (!json.equals("0")) {
					List<GoodsCartPojo> goodsCartList = JsonUtils.jsonToList(json, GoodsCartPojo.class);

					List<GoodsCart> notExistCart = new ArrayList<>();
					for (GoodsCartPojo pojo : goodsCartList) {

						Goods goods = goodsMapper.selectByPrimaryKey(pojo.getId());
						if (goods != null) {// 如果商家存在
							// 设置属性
							GoodsCart gCart = new GoodsCart();
							gCart.setmId(m.getId());
							gCart.setgId(pojo.getId());
							gCart.setIsDel(0);
							gCart.setGskuid(pojo.getSkuId());
							gCart.setShopId(goods.getShopId());
							gCart.setAddtime(new Date());
							// 查询该商品是否在数据库里面
							GoodsCart goodsCart = goodsCartMapper.selectGoodsCartBySelect(gCart);
							if (goodsCart != null) {
								// 如果在则更新数量
								gCart.setId(goodsCart.getId());
								gCart.setNum(pojo.getNum() + goodsCart.getNum());
								goodsCartMapper.updateByPrimaryKeySelective(gCart);
							} else {
								// 如果不在则insert信息
								gCart.setNum(pojo.getNum());
								notExistCart.add(gCart);
							}
						}
					}
					// 如果未存在的，则批量insert数据库
					if (notExistCart.size() > 0) {
						goodsCartMapper.insertSelectiveByBatch(notExistCart);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Description: 合并微信记录
	 * @author xieyc
	 * @date 2018年9月5日 下午7:12:25 
	 * @param   
	 * @return  
	 *
	 */
	public int saveMergeLog(Integer srcUserId, Integer destUserId, String openid) {
		MergeUserLog saveMergeUserLog=new MergeUserLog();
		saveMergeUserLog.setDestUserId(destUserId);//要合并到的用户
		saveMergeUserLog.setSrcOpenid(openid);//合并的微信的openid
		saveMergeUserLog.setSrcUserId(srcUserId);//被合并的用户
		String 	couponLogId=couponLogMapper.getCouponLogIdByMid(srcUserId);
		String 	orderId=orderMapper.getOrderIdByMid(srcUserId);
		saveMergeUserLog.setCouponId(couponLogId);//合并的优惠卷id
		saveMergeUserLog.setOrderId(orderId);//合并的订单id
		saveMergeUserLog.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
		saveMergeUserLog.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
		mergeUserLogMapper.insert(saveMergeUserLog);
		return 0;
	}
	/**
	 * 
	 * @Description: 微信整合手机（测试用）
	 * @author xieyc
	 * @date 2018年9月3日 下午6:14:57 
	 *
	 */
	public void wxMergePhone(int old_mId,int new_mId){
		memberMapper.mergeUserByWxAndPhone(old_mId,new_mId);
	}
	

}
