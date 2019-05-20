package com.order.user.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.MBusEntity;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.IDUtils;
import com.bh.utils.IPUtils;
import com.bh.utils.SmsUtil;
import com.bh.utils.pay.HttpService;
import com.bh.utils.pay.WXPayUtil;
import com.google.gson.Gson;
import com.order.enums.UnionPayInterfaceEnum;
import com.order.user.service.SeedService;
import com.order.user.service.ShopMService;
import com.order.util.JedisUtil;
import com.order.util.smallAppPay.PayResult;
import com.order.util.smallAppPay.PayVo;
import com.order.vo.UnionPayVO;

import net.sf.json.JSONObject;

@Controller
public class ShopMController {
	private static final Logger logger = LoggerFactory.getLogger(ShopMController.class);
	@Autowired
	private SeedService seedService;
	@Autowired
	private ShopMService shopMService;
	
	@Autowired
	private MemberShopMapper memberShopMapper;

	/**
	 * CHENG-201712-05-01 商家支付押金2000元
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/deposit")
	@ResponseBody
	public BhResult Deposit(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JedisUtil jedisUtil= JedisUtil.getInstance();  
			JedisUtil.Strings strings=jedisUtil.new Strings();
			String token = map.get("openid");
			if (StringUtils.isNotBlank(token)) {
				//strings.set("openid",token);
			}
			if (StringUtils.isBlank(token)) {
				//token=strings.get("openid");
				token=request.getParameter("openid");
			}
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopMService.selectMemberShop(param);
			if (token == null) {
				return BhResult.build(400, "token不能为空", null);
			} else if ((memberShop1.get(0).getPayStatus() + "").equals("2")) {
				System.out.println(memberShop1.get(0).getmId());
				return BhResult.build(400, "该商家的押金已经交过了,请勿重复提交", null);
			} else {
				MemberShop mShop = new MemberShop();
				Integer mPrice = (int) (Double.parseDouble(Contants.MARGIN) * 10 * 10);

				mShop.setOrderNo(IDUtils.getOrderNo(null));

				mShop.setPayStatus(1);
				mShop.setOrderPrice(mPrice);
				mShop.setmId(memberShop1.get(0).getmId());
				shopMService.updateMemberShop(mShop);

				String orderBody = "null,0001";// 商家支付押金接口
				List<MemberShop> list = seedService.selectBhShop(1);
				MemberShop memberShop = new MemberShop();
				if (list.size() > 0) {
					memberShop = list.get(0);
				} else {
					list = seedService.selectBhShop(0);
					memberShop = list.get(0);
				}
				UnionPayVO vo = new UnionPayVO();
				vo.setOriginalAmount(mShop.getOrderPrice() + "");
				vo.setTotalAmount(mShop.getOrderPrice() + "");
				vo.setMd5Key(memberShop.getMd5Key());
				vo.setAttachedData(orderBody);
				vo.setMerOrderId(mShop.getOrderNo());
				vo.setOpenid(token);
				String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.DEPOSITPAY.getMethod(), vo);
				jsonStr = jsonStr.replaceAll("&", "&amp");
				logger.info("depositjsonStr:"+jsonStr);
				return BhResult.build(200, "操作成功", jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######deposit#######" + e);
			return null;
		}

	}

	/**
	 * CHENG-201712-05-01 商家免审核的押金-pc端的
	 * @param map()
	 * @return
	 */
	@RequestMapping("/promiseMoney")
	@ResponseBody
	public BhResult promiseMoney(HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}

			MemberShop memberShop = shopMService.selectMemberShopById(shopId);
			if (memberShop.getDepositStatus().equals(2)) {
				return new BhResult(400, "该商家已支付过", null);
			}
			String orderBody = "null,0002";// 免审核的押金
			// 金额的去向
			List<MemberShop> list = seedService.selectBhShop(1);
			MemberShop memberShop1 = new MemberShop();
			if (list.size() > 0) {
				memberShop1 = list.get(0);
			} else {
				list = seedService.selectBhShop(0);
				memberShop1 = list.get(0);
			}
			// (商家的支付前缀
			String orderNo = IDUtils.getOrderNo(memberShop1.getBusiPayPre());

			UnionPayVO vo = new UnionPayVO();
			vo.setOriginalAmount(Contants.PROMISE_MONEY);
			vo.setTotalAmount(Contants.PROMISE_MONEY);
			vo.setMd5Key(memberShop1.getMd5Key());
			vo.setAttachedData(orderBody);
			vo.setMerOrderId(orderNo);
			String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.promiseMoney.getMethod(), vo);
			jsonStr.substring(1, jsonStr.length() - 1);
			String jString = jsonStr.substring(1, jsonStr.length() - 1);
			MemberShop mShop = new MemberShop();
			mShop.setmId(shopId);
			mShop.setDeposit(Integer.parseInt(vo.getOriginalAmount()));
			mShop.setDepositStatus(1);
			mShop.setDepositNo(orderNo);
			shopMService.updateMemberShopByDespo(mShop);
			return BhResult.build(200, "操作成功", jString);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######promiseMoney#######" + e);
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 该商家是否已入住-pc端的
	 * @param map()
	 * @return
	 */
	@RequestMapping("/isPromise")
	@ResponseBody
	public BhResult isPromise(HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			MemberShop memberShop = shopMService.selectMemberShopById(shopId);
			if (memberShop.getDepositStatus() == null) {
				memberShop.setDepositStatus(1);
			}

			return BhResult.build(200, "操作成功", memberShop.getDepositStatus());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######isPromise#######" + e);
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 该商家是否已入住 -pc端的
	 * @param map()
	 * @return
	 */
	@RequestMapping("/checkIsPay")
	@ResponseBody
	public BhResult checkIsPay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String depositNo = map.get("depositNo");
			if (StringUtils.isEmpty(depositNo)) {
				bhResult = new BhResult(400, "depositNo参数不能为空", null);
			} else {
				MemberShop memberShop = new MemberShop();
				memberShop = shopMService.checkIsPaySeccuss(depositNo);
				bhResult = new BhResult(200, "操作成功", memberShop.getDepositStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkIsPay#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
			// TODO: handle exception
		}
		return bhResult;
	}

	/**
	 * CHENG-201812-3-23 该商家是否已入住-移动端  (移动端的信息)
	 * @param map()
	 * @return
	 */
	@RequestMapping("/mIsPromise")
	@ResponseBody
	public BhResult mIsPromise(HttpServletRequest request, HttpServletResponse response) {
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			if (user == null) {
				return new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}
				Long l1 = user.getShopId();
				long l2 = l1;
				int i1 = (int) l2;
				Integer i2 = i1;
				MemberShop memberShop = shopMService.selectMemberShopById(i2);
				if (memberShop.getDepositStatus() == null) {
					memberShop.setDepositStatus(1);
				}
				return new BhResult(200, "操作成功", memberShop.getDepositStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######mIsPromise#######" + e);
			return new BhResult(500, "操作失败", null);
			// TODO: handle exception
		}
	}

	/**
	 * CHENG-201712-05-01 商家商品免审核的接口-移动端 * @param map()
	 * @return
	 */
	@RequestMapping("/mdeposit")
	@ResponseBody
	public BhResult mDeposit(HttpServletRequest request, HttpServletResponse response) {
		try {
			MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			// 如果user为null,则该商家未登陆
			if (user == null) {
				return new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}
				Long l1 = user.getShopId();
				long l2 = l1;
				int i1 = (int) l2;
				Integer i2 = i1;
				// 通过shopId获取该商家
				MemberShop memberShop = shopMService.selectMemberShopById(i2);
				if (memberShop.getDepositStatus() == null) {
					memberShop.setDepositStatus(1);
				}
				// 支付状态（1代表未支付，2代表已支付）
				if (memberShop.getDepositStatus().equals(2)) {
					return new BhResult(400, "该商家已支付过", null);
				} else {
					MemberShop mShop = new MemberShop();
					// 免审核押金的金额
					Integer mPrice = (int) (Double.parseDouble(Contants.PROMISE_MONEY));
					mShop.setDepositNo(IDUtils.getOrderNo(null));
					mShop.setDepositStatus(1);
					mShop.setDeposit(mPrice);
					mShop.setmId(i2);
					shopMService.updateMemberShopByDespo(mShop);

					String orderBody = "null,0002";// 商家支付押金接口
					List<MemberShop> list = seedService.selectBhShop(1);
					MemberShop memberShop1 = new MemberShop();
					// 为了获得Md5Key
					if (list.size() > 0) {
						memberShop1 = list.get(0);
					} else {
						list = seedService.selectBhShop(0);
						memberShop1 = list.get(0);
					}

					UnionPayVO vo = new UnionPayVO();
					vo.setOriginalAmount(mShop.getDeposit() + "");
					vo.setTotalAmount(mShop.getDeposit() + "");
					vo.setMd5Key(memberShop1.getMd5Key());
					vo.setAttachedData(orderBody);
					vo.setMerOrderId(mShop.getDepositNo());
					String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXPROMISEMONEY.getMethod(), vo);
					jsonStr = jsonStr.replaceAll("&", "&amp");
					return BhResult.build(200, "操作成功", jsonStr);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######mdeposit#######" + e);
			return null;
			// TODO: handle exception
		}

	}

	// 钱包-充值功能
	@RequestMapping("/addWalletLog")
	@ResponseBody
	public BhResult addWalletLog(HttpServletRequest request, HttpServletResponse response) {
		BhResult r = null;
		try {
			String json = request.getParameter("json");
			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(json, Map.class);
			String code = map.get("code");
			String moneyParam = map.get("money");
			WalletLog entity = new WalletLog();
			entity.setMoney2(moneyParam);

			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			// 自己生成订单号
			IDUtils iDUtils = new IDUtils();
			if (StringUtils.isBlank(entity.getMoney2()) || entity.getMoney2().equals("0")
					|| entity.getMoney2().equals("0.0")) {// 金额空
				return BhResult.build(400, "充值金额不能为0", null);
			}
			Wallet wallet = new Wallet();
			wallet.setUid(member.getId());
			List<Wallet> list = shopMService.getByUid(wallet);// 获取当前的用户的数据
			if (list != null && list.size() > 0) {

			} else {// 没数据提示，输入密码
				return BhResult.build(400, "请设置密码", null);
			}

			// 前端传来充值金额
			double amount2 = Double.valueOf(entity.getMoney2());

			WalletLog walletLog = new WalletLog();
			walletLog.setAddTime(new Date()); // 时间
			walletLog.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2 * 10 * 10))); // 金额
			walletLog.setmId(member.getId()); // 登录者id
			walletLog.setOrderNo(iDUtils.getOrderNo("3769")); // 订单号
			walletLog.setInOut(0); // 进账

			shopMService.addWalletLog(walletLog); // 保存一条充值记录到WalletLog表

			String openid = WXPayUtil.getSmallAppOpenid(Contants.sAppId, Contants.sAppSecret, code);
			PayVo payVo = new PayVo();
			payVo.setOpenId(openid);
			payVo.setOut_trade_no(walletLog.getOrderNo());
			String orderBody = "null,0000" + "," + member.getId();
			payVo.setAttach(orderBody);
			payVo.setTotal_fee(walletLog.getAmount() + "");
			String spbill_create_ip = IPUtils.getIpAddr(request);// 终端IP
			payVo.setSpbill_create_ip(spbill_create_ip);
			payVo.setBody("订单描述");
			PayResult payResult = new PayResult();
			JSONObject ret = payResult.getPayResult(payVo);
			return BhResult.build(200, "操作成功", ret.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addWalletLog#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

}
