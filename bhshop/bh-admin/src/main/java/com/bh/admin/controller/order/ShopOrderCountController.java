package com.bh.admin.controller.order;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.GoodsAttr;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.BusBankCard;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.service.ShopOrderCountService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.bean.BankCard;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.util.enterprise.QueryBusinessInfo;
import com.bh.util.enterprise.pojo.QueryBusinessInfoPojo;
import com.bh.utils.AliMarketUtil;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;


/**
 * @Description: 商家订单统计
 * @author xieyc
 * @date 2018年1月4日 上午10:23:45
 */
@Controller
@RequestMapping("/shopOrderCount")
public class ShopOrderCountController {
	@Autowired
	private ShopOrderCountService shopOrderCountService;

	@Value(value = "${pageSize}")
	private String pageSize;
	
	
	
	/**
	 * @Description: 测试用(更新goods的销量)
	 * @author xieyc
	 * @date 2018年1月4日 上午10:54:54
	 */
	@RequestMapping("/test")
	@ResponseBody
	public BhResult test(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			QueryBusinessInfoPojo queryBusinessInfoPojo= QueryBusinessInfo.queryBusinessInfo(map.get("companyName"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(queryBusinessInfoPojo);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}

	/**
	 * 
	 * @Description: 统计：进账列表--根据条件查询商家订单列表
	 * @author xieyc
	 * @date 2018年1月4日 上午10:54:54
	 */
	@RequestMapping("/getShopOrderList")
	@ResponseBody
	public BhResult getShopOrderList(@RequestBody Map<String, String> map,@RequestHeader(value = "token") String token) {
		BhResult result = null;
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Object sId = mapUser.get("shopId");
			Integer shopId = 1;
			if(sId!=null){
			    shopId = (Integer)sId;
			}
			String id = map.get("id"); // 商家订单id
			String order_id = map.get("order_id");// 订单id
			String order_no = map.get("order_no");// 订单号
			String payment_no = map.get("payment_no");// 支付单号（第三方支付交易号）
			String payment_id = map.get("payment_id");// 支付方式ID
			String status = map.get("status"); // 订单状态-1待付款，2待发货，3已发货，4已收货,5待评价,6已取消,7已评价、8已删除',9退款单
			String currentPage = map.get("currentPage");// 当前第几页
			if (StringUtils.isEmptyOrWhitespaceOnly(currentPage)) {
				currentPage = "1";
			}
			PageBean<OrderShop> pageOrderShop = shopOrderCountService.getShopOrderList(id, order_id, order_no,
					payment_no, payment_id, status, currentPage, pageSize, shopId);

			if (pageOrderShop != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageOrderShop);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * @Description: 统计：根据id查询某个商家订单详情
	 * @author xieyc
	 * @date 2018年1月4日 上午20:52:54
	 */
	@RequestMapping("/getOrderShopDetails")
	@ResponseBody
	public BhResult getOrderShopDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id"); //商家订单id
		    OrderShop orderShop = shopOrderCountService.getOrderShopDetails(id);
			   if (orderShop != null) {
				   result = new BhResult(BhResultEnum.SUCCESS, orderShop);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	/**
	 * @Description: 统计-出账列表
	 * @author xieyc
	 * @date 2018年1月5日 下午2:55:10
	 */
	@RequestMapping("/countRefundList")
	@ResponseBody
	public BhResult countRefundList(@RequestBody Map<String, String> map,@RequestHeader(value="token") String token) {
		BhResult result = null;
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Object sId = mapUser.get("shopId");
			Integer shopId = 1;
			if(sId!=null){
			    shopId = (Integer)sId;
			}
			String id = map.get("id"); //退款id
			String order_id = map.get("order_id");// 订单id
			String order_no = map.get("order_no");// 订单号
			String payment_no = map.get("payment_no");// 支付单号（第三方支付交易号）
			String status = map.get("status"); // 订单状态-0:退款中 1:退款失败 2:退款成功
			String startTime = map.get("startTime");// 查询条件：起始时间
			String endTime = map.get("endTime");// 查询条件：结束时间
			String currentPage = map.get("currentPage");// 当前第几页
			if (StringUtils.isEmptyOrWhitespaceOnly(currentPage)) {
				currentPage = "1";
			}
			PageBean<OrderRefundDoc> page = shopOrderCountService.countRefundList(id, order_id, order_no, payment_no,
					status, currentPage, pageSize, startTime, endTime,shopId);
			if (page != null) {
				result = new BhResult(BhResultEnum.SUCCESS, page);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description: 统计-出账列表中某条记录的详细列表
	 * @author xieyc
	 * @date 2018年1月4日 上午20:52:54
	 */
	@RequestMapping("/getCountRefundDetails")
	@ResponseBody
	public BhResult getCountRefundDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id"); //商家订单id
		    OrderRefundDoc orderRefundDoc = shopOrderCountService.getCountRefundDetail(id);
			   if (orderRefundDoc != null) {
				   result = new BhResult(BhResultEnum.SUCCESS, orderRefundDoc);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	/**
	 * @Description: 统计--概要
	 * @author xieyc
	 * @date 2018年1月5日 下午7:13:47
	 */
	@RequestMapping("/countOutline")
	@ResponseBody
	public BhResult countOutline(@RequestHeader(value="token") String token,@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String startTime = map.get("startTime");// 查询条件：起始时间
			String endTime = map.get("endTime");// 查询条件：结束时间
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Object sId = mapUser.get("shopId");
			Integer shopId = 1;
			if(sId!=null){
			    shopId = (Integer)sId;
			}
			Map<String, Object> returnMap = shopOrderCountService.countOutline(startTime,endTime,shopId);
			if (returnMap == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, returnMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description: 统计--7日内店铺销售TOP10
	 * @author xieyc
	 * @date 2018年1月6日 下午2:16:21
	 */
	@RequestMapping("/getTopTenShopList")
	@ResponseBody
	public BhResult getTopTenShopList() {
		BhResult result = null;
		try {
			PageBean<OrderSku> pageShops = shopOrderCountService.getTopTenShopList();
			if (pageShops == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, pageShops);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * @Description: 统计--7日内商品销售TOP10
	 * @author xieyc
	 * @date 2018年1月6日 下午2:16:21 
	 */
	@RequestMapping("/getTopTenGoodsList")
	@ResponseBody
	public BhResult getTopTenGoodsList(HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer shopId = (Integer)mapOne.get("shopId");
		    if(shopId == null){
		    	shopId = 1;
		    }
			PageBean<OrderSku> pageGoods = shopOrderCountService.getTopTenGoodsList(shopId);
			if (pageGoods == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, pageGoods);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
	/**
	 * @Description: 移动端查询某个商家的所有订单（进账记录）  
	 * @author xieyc 作废
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetShopOrderList")
	@ResponseBody
	public BhResult mgetShopOrderList(HttpServletRequest request,@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				PageBean<OrderShop> orderShopList = shopOrderCountService.mgetShopOrderList(currentPage, pageSize, shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(orderShopList);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 移动端查询某个商家的退款订单（出账记录）
	 * @author xieyc   作废
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetRefundList")
	@ResponseBody
	public BhResult mgetRefundList(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				PageBean<OrderRefundDoc> orderShopList = shopOrderCountService.mgetRefundList(currentPage, pageSize,shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(orderShopList);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	/**
	 * @Description: 折线图数据展示
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetAmountEveryDay")
	@ResponseBody
	public BhResult mgetAmountEveryDay(HttpServletRequest request,@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String day = map.get("day");// 当前第多少天的数据（7or30）
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				Map<String,Double> mapList = shopOrderCountService.mgetAmountEveryDay(day, shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(mapList);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 某个商家绑定的银行卡展示
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetBusBankCard")
	@ResponseBody
	public BhResult mgetBusBankCard(HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				BusBankCard busBankCard=new BusBankCard();
				busBankCard.setmId(shopId.intValue());
				List <BusBankCard> bankCardList = shopOrderCountService. mgetBusBankCard(busBankCard);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(bankCardList);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 更新商家绑定的某张银行信息
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mUpdateBusBankCard")
	@ResponseBody
	public BhResult  mUpdateBusBankCard(HttpServletRequest request,@RequestBody BusBankCard busBankCard) {
		BhResult r = null; 
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				BankCard input = new BankCard();
				input.setName(busBankCard.getRealName());//真实姓名
				input.setPhoneNo(busBankCard.getPhone());//预留号码
				input.setCardNo(busBankCard.getBankCardNo());// 银行卡号
				input.setIdNo(busBankCard.getIdNo());// 身份证号
				BankCard ret = AliMarketUtil.verifyBankCard(input);//验证银行卡
				if (ret == null) {
					r = new BhResult(400, "认证失败", null);
				} else {
					if (ret.getRespCode().equals("0000")) {
						busBankCard.setBankName(ret.getBankName());
						busBankCard.setBankCode(ret.getBankCode());
						busBankCard.setBankKind(ret.getBankKind());
						busBankCard.setBankType(ret.getBankType());
						int row = shopOrderCountService.mUpdateBusBankCard(busBankCard);
						r=new BhResult(200, "绑定银行卡成功", row);
					} else {
						r = new BhResult(400, ret.getRespMessage(), null);
					}
				}	
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 获取商家某个的银行信息
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetBusBank")
	@ResponseBody
	public BhResult  mgetBusBank(HttpServletRequest request,@RequestBody Map<String, String> map) {
		String id = map.get("id");// 银行卡id
		BhResult r = null; 
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				BusBankCard busBankCard = shopOrderCountService.mgetBusBank(id);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(busBankCard);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	/**
	 * @Description: 移动端查询某个商家进账和出账记录
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	@RequestMapping("/mgetCountDetailList")
	@ResponseBody
	public BhResult mgetCountDetailList(HttpServletRequest request,@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			String isFalg=map.get("isFalg");// 0 进账和出账列表（全部）   1进账    2出账
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				PageBean<OrderShop> orderShopList = shopOrderCountService.mgetCountDetailList(currentPage, pageSize, shopId.intValue(),Integer.valueOf(isFalg));
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(orderShopList);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
}

