package com.bh.admin.controller.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.enums.UnionPayInterfaceEnum;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.service.SeedService;
import com.bh.admin.service.ShopMService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.vo.UnionPayVO;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.utils.IDUtils;
import com.bh.utils.pay.HttpService;



@Controller
public class ShopMController {

	@Autowired
	private SeedService seedService;
	@Autowired
	private ShopMService shopMService;

	/**
	 * CHENG-201712-05-01 商家支付押金2000元
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/deposit")
	@ResponseBody
	public BhResult Deposit(HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = (String) request.getSession().getAttribute("token");
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopMService.selectMemberShop(param);
			if (token == null) {
				return BhResult.build(400, "token不能为空", null);
			} else if ((memberShop1.get(0).getPayStatus() + "").equals("2")) {
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
				String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.DEPOSITPAY.getMethod(), vo);

				System.out.println("jsonStr打印--->" + jsonStr);
				jsonStr = jsonStr.replaceAll("&", "&amp");
				return BhResult.build(200, "操作成功", jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 商家免审核的押金-pc端的
	 * 
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
			// IDUtils.getOrderNo(商家的支付前缀)
			String orderNo = IDUtils.getOrderNo(memberShop1.getBusiPayPre());

			UnionPayVO vo = new UnionPayVO();
			vo.setOriginalAmount(Contants.PROMISE_MONEY);
			vo.setTotalAmount(Contants.PROMISE_MONEY);
			vo.setMd5Key(memberShop1.getMd5Key());
			vo.setAttachedData(orderBody);
			vo.setMerOrderId(orderNo);
			String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.promiseMoney.getMethod(), vo);
			System.out.println("jsonStr打印--->" + jsonStr);
			// jsonStr = jsonStr.replaceAll("&", "&amp");
			// hamburger".substring(4, 8) returns "urge"
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
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 商家免审核的押金-pc端的
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/promiseMoney1")
	@ResponseBody
	public BhResult promiseMoney1(HttpServletRequest request, HttpServletResponse response) {
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
			// IDUtils.getOrderNo(商家的支付前缀)
			String orderNo = IDUtils.getOrderNo(memberShop1.getBusiPayPre());

			UnionPayVO vo = new UnionPayVO();
			vo.setOriginalAmount(Contants.PROMISE_MONEY);
			vo.setTotalAmount(Contants.PROMISE_MONEY);
			vo.setMd5Key(memberShop1.getMd5Key());
			vo.setAttachedData(orderBody);
			vo.setMerOrderId(orderNo);
			String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.promiseMoney.getMethod(), vo);
			System.out.println("jsonStr打印--->" + jsonStr);
			// jsonStr = jsonStr.replaceAll("&", "&amp");
			// hamburger".substring(4, 8) returns "urge"
			jsonStr.substring(1, jsonStr.length() - 1);
			String jString = jsonStr.substring(1, jsonStr.length() - 1);
			MemberShop mShop = new MemberShop();
			mShop.setmId(shopId);
			mShop.setDeposit(Integer.parseInt(vo.getOriginalAmount()));
			mShop.setDepositStatus(1);
			mShop.setDepositNo(orderNo);
			shopMService.updateMemberShopByDespo(mShop);
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("jString", jString);
			map1.put("depositNo", mShop.getDepositNo());

			return BhResult.build(200, "操作成功", map1);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 该商家是否已入住-pc端的
	 * 
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
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201712-05-01 该商家是否已入住 -pc端的
	 * 
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
			bhResult = new BhResult(500, "操作失败", null);
			// TODO: handle exception
		}
		return bhResult;
	}

	// 移动端的信息
	/**
	 * CHENG-201812-3-23 该商家是否已入住-移动端
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/mIsPromise")
	@ResponseBody
	public BhResult mIsPromise(HttpServletRequest request, HttpServletResponse response) {
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
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
			return new BhResult(500, "操作失败", null);
			// TODO: handle exception
		}
	}

	/**
	 * CHENG-201712-05-01 商家商品免审核的接口-移动端 * @param map()
	 * 
	 * @return
	 */
	@RequestMapping("/mdeposit")
	@ResponseBody
	public BhResult mDeposit(HttpServletRequest request, HttpServletResponse response) {
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			//如果user为null,则该商家未登陆
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
				//通过shopId获取该商家
				MemberShop memberShop = shopMService.selectMemberShopById(i2);
				if (memberShop.getDepositStatus() == null) {
					memberShop.setDepositStatus(1);
				}
				//支付状态（1代表未支付，2代表已支付）
				if (memberShop.getDepositStatus().equals(2)) {
					return new BhResult(400, "该商家已支付过", null);
				} else {
					MemberShop mShop = new MemberShop();
					//免审核押金的金额
					Integer mPrice = (int) (Double.parseDouble(Contants.PROMISE_MONEY));
					mShop.setDepositNo(IDUtils.getOrderNo(null));
					mShop.setDepositStatus(1);
					mShop.setDeposit(mPrice);
					mShop.setmId(i2);
					shopMService.updateMemberShopByDespo(mShop);

					String orderBody = "null,0002";// 商家支付押金接口
					List<MemberShop> list = seedService.selectBhShop(1);
					MemberShop memberShop1 = new MemberShop();
					//为了获得Md5Key
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
					System.out.println("jsonStr打印--->" + jsonStr);
					jsonStr = jsonStr.replaceAll("&", "&amp");
					return BhResult.build(200, "操作成功", jsonStr);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}

	}

}
