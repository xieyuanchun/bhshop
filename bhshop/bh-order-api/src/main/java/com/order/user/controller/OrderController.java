package com.order.user.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.bean.Alipay;
import com.bh.config.AlipayConfig;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.ActZoneGoodsMapper;
import com.bh.goods.mapper.BargainRecordMapper;
import com.bh.goods.mapper.CashDepositMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.BargainRecord;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderCollectionDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.Wallet;
import com.bh.utils.AlipayUtil;
import com.bh.utils.IPUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.pay.HttpService;
import com.bh.utils.pay.WXPayUtil;
import com.google.gson.Gson;
import com.order.annotaion.OrderLogAnno;
import com.order.enums.PayInterfaceEnum;
import com.order.enums.UnionPayInterfaceEnum;
import com.order.sys.service.SysService;
import com.order.user.service.PayCallbackService;
import com.order.user.service.SimpleOrderService;
import com.order.user.service.UserOrderService;
import com.order.user.service.UserSubmitOrderService;
import com.order.util.smallAppPay.PayResult;
import com.order.util.smallAppPay.PayVo;
import com.order.vo.UnionPayAppVO;
import com.order.vo.UnionPayVO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Value("${SESSION_BH_CART_KEY}")
	private String SESSION_BH_CART_KEY;

	@Value("${USERINFO}")
	private String USERINFO;
	//
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private SimpleOrderService simpleOrderService;
	@Autowired
	private SysService sysService;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private PayCallbackService payCallbackService;
	@Autowired
	private UserSubmitOrderService userSubmitOrderService;
	@Autowired
	private BargainRecordMapper  bargainRecordMapper;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;

	
	/*** 测试用 立即支付 */
	@OrderLogAnno(value = "立即支付", debug = true)
	@RequestMapping(value = "/paymentnow")
	@ResponseBody
	public BhResult paymentNow(String orderNo, String totalAmount, String subject, String body,
			HttpServletRequest request, HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			// 1:接收前台传参,订单的id
			Alipay alipay = new Alipay();
			alipay.setOutTradeNo(orderNo);// 订单号
			alipay.setTotalAmount(totalAmount);// 总金额
			alipay.setSubject(subject);// 创建订单标题
			alipay.setBody(body);// 创建订单描述
			bhResult = AlipayUtil.createPageTrade(alipay);

			Order order = new Order();
			order.setOrderNo(orderNo);
			order.setId(2);
			order.setStatus(1);
			order.setmId(66);
			bhResult.setData(order);

			response.setContentType("text/html;charset=" + AlipayConfig.charset);
			response.getWriter().write(bhResult.getMsg());// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();

			return bhResult;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######paymentnow#######" + e);
			return bhResult;
		}

	}

	/** 提交订单:在直商品页面接点购买 */
	@OrderLogAnno("立即下单")
	@RequestMapping(value = "/addorder")
	@ResponseBody
	public BhResult addOrder(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		
		BhResult bhResult = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(USERINFO);
			bhResult = new BhResult();
			//mykey:-1     0
			//order:null  order
			
			Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
			Object myKeyObj=myMap.get("mykey");
			if (myKeyObj.equals("-1")) {
				return bhResult=new BhResult(400, myMap.get("order").toString(), null);
			}  else if (myKeyObj.equals("0")) {
				Order order2=(Order) myMap.get("order");
				double orderPrice = (double) order2.getOrderPrice() / 100;// 订单总金额单位分int
				Alipay alipay = new Alipay();
				alipay.setOutTradeNo(order2.getOrderNo());// 订单号
				alipay.setTotalAmount(String.valueOf(orderPrice));// 总金额
				alipay.setSubject("创建订单标题");// 创建订单标题
				alipay.setBody("创建订单描述");// 创建订单描述
				bhResult = AlipayUtil.createPageTrade(alipay);
				bhResult.setStatus(200);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addorder#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 支付宝下单接口 -app端 */
	@OrderLogAnno("支付宝app支付")
	@RequestMapping(value = "/aliAppAay")
	@ResponseBody
	public BhResult aliAppAay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		BhResult bhResult = null;
		try {
			bhResult = new BhResult();
			Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
			Object myKeyObj=myMap.get("mykey");
			if (myKeyObj.equals("-1")) {
				return bhResult=new BhResult(400,myMap.get("order").toString(), null);
			}else if (myKeyObj.equals("0")) {
				Order order2=(Order) myMap.get("order");
				String orderBody = "";
				Integer fz = order2.getGroupFz();
				if (fz == null) {
					orderBody = "null,1"; // 非拼团模式
				} else if (fz.intValue() == 1) {
					if (StringUtils.isBlank(order2.getTeamNo())) {
						orderBody = "null,2"; // 创团
					} else {
						orderBody = order2.getTeamNo() + ",3"; // 拼团
					}
				} 

				double orderPrice = (double) order2.getOrderPrice() / 100;// 订单总金额单位分int
				Alipay alipay = new Alipay();
				alipay.setOutTradeNo(order2.getOrderNo());// 订单号
				alipay.setTotalAmount(String.valueOf(orderPrice));// 总金额
				alipay.setSubject("创建订单标题");// 创建订单标题
				alipay.setBody(orderBody);// 创建订单描述
				bhResult = AlipayUtil.createAppTrade(alipay);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######aliAppAay#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}


	/** 提交订单:在直商品页面接点购买 */
	@RequestMapping(value = "/wxshoworder")
	@ResponseBody
	public BhResult wxShowOrder(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			bhResult = new BhResult();
			Order order2 = new Order();
			String orderNo = map.get("orderNo");
			if (StringUtils.isEmpty(orderNo)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				Order order = new Order();
				order2.setOrderNo(orderNo);
				order = userOrderService.getOrderByOrderNo(orderNo);
				bhResult = new BhResult(BhResultEnum.SUCCESS, order.getStatus());
			}

			bhResult.setStatus(200);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######wxshoworder#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}


	@RequestMapping(value = "/wxAppPay")
	@ResponseBody
	public BhResult wxAppPay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Member member = (Member) request.getSession(false).getAttribute(USERINFO);
			Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
			//mykey:-1     0
			//order:null  order
			Object myKeyObj=myMap.get("mykey");
			if (myKeyObj.equals("-1")) {
				return BhResult.build(400, myMap.get("order").toString(), null);
			}else if (myKeyObj.equals("0")) {
				
				Order order2=(Order) myMap.get("order");
				if (order2.getCouponNum() != null && order2.getCouponNum() == 1) { // 判断优惠劵是否过期
					return BhResult.build(400, "优惠劵已过期", null);
				}
				String outTradeNo = order2.getOrderNo();
				String orderBody = "";
				Integer fz = order2.getGroupFz();
				if (fz == null) {
					orderBody = "null,1"; // 非拼团模式
				} else if (fz.intValue() == 1) {
					if (StringUtils.isBlank(order2.getTeamNo())) {
						orderBody = "null,2"; // 创团
					} else {
						orderBody = order2.getTeamNo() + ",3"; // 拼团
					}
				} else if (fz.intValue() == 2) { // 秒杀
					orderBody = "null,5," + order2.getTgId();
				} else if (fz.intValue() == 3) {// 抽奖
					if (StringUtils.isBlank(order2.getTeamNo())) {
						orderBody = "null,7," + order2.getTgId();
					} else {
						orderBody = order2.getTeamNo() + ",8," + order2.getTgId();
					}
				} else if (fz.intValue() == 4) {// 惠省钱
					if (StringUtils.isBlank(order2.getTeamNo())) {
						if (StringUtils.isBlank(order2.getActNo())) {
							orderBody = "null,9," + order2.getTgId() + ",null";
						} else {
							orderBody = "null,9," + order2.getTgId() + "," + order2.getActNo();
						}
					} else {
						if (!StringUtils.isBlank(order2.getActNo())) {
							orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + ",null";
						} else {
							orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + "," + order2.getActNo();
						}
					}
				}
				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(order2.getShopId());// xieyc
				//程凤云：多商家（多余2或者2以上）支付时md5取滨惠自营的
				if (order2.getId()!=null) {
					List<OrderShop> shopList = simpleOrderService.selectOrderShopListByOrderId(order2.getId());
					if (shopList.size()>1) {
						memberShop = memberShopMapper.selectByPrimaryKey(1);
					}
				}
				//程凤云-结束
				UnionPayAppVO vo = new UnionPayAppVO();
				vo.setOriginalAmount(order2.getOrderPrice() + "");
				vo.setTotalAmount(order2.getOrderPrice() + "");
				vo.setMd5Key(memberShop.getMd5Key());
				vo.setAttachedData(orderBody);
				vo.setMerOrderId(outTradeNo);
				String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXAPPPAY.getMethod(), vo);
				return BhResult.build(200, "操作成功", jsonStr);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######wxAppPay#######" + e);
			return BhResult.build(500, "操作失败", null);
		}
		return null;

	}

	@RequestMapping(value = "/toWxQrPay")
	@ResponseBody
	public synchronized BhResult toWxQrPay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		try {
			Map<String, Object> myMap = userSubmitOrderService.rendOrder(map, member, request);
			// 商品数量小于1
			// mykey:-1 0
			// order:null order
			Object myKeyObj = myMap.get("mykey");
			if (myKeyObj.equals("-1")) {
				return null;
			} else {
				Order order2 = (Order) myMap.get("order");
				double price = (double) order2.getOrderPrice() / 100;
				String outTradeNo = order2.getOrderNo();
				String orderTit = "订单标题";
				String orderBody = "订单描述";

				HttpPost httpPost = new HttpPost("http://39.104.48.52:8866/shop/order/pay");
				CloseableHttpClient client = HttpClients.createDefault();
				JSONObject jsonParam = new JSONObject();
				jsonParam.put("merchantOrderNo", order2.getOrderNo());// 支付订单号
				jsonParam.put("amount", MoneyUtil.IntToDouble(order2.getOrderPrice()) + "");// 支付金额
				jsonParam.put("payNotifyUrl", Contants.BIN_HUI_URL + "/bh-order-api/payCallback/afterSucceedPay");// 回调地址
				jsonParam.put("returnUrl", "");
				jsonParam.put("attachedData", orderBody);
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
				HttpResponse resp = client.execute(httpPost);
				Integer statusCode = null;
				if (resp != null) {
					statusCode = resp.getStatusLine().getStatusCode();
					if (statusCode != null && statusCode == 200) {
						JSONObject jsonbject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
						if (jsonbject.getInt("code") == 0) {
							return BhResult.build(200, "下单成功");
						}
					}
				}
				return BhResult.build(400, "下单失败");

			/*	StringBuffer sb = new StringBuffer();
				sb.append(PayInterfaceEnum.TOWXQRPAY.getMethod());
				sb.append("?orderTit=" + orderTit);
				sb.append("&orderBody=" + orderBody);
				sb.append("&price=" + price);
				sb.append("&outTradeNo=" + outTradeNo);
				String codeUrl = HttpService.doGet(sb.toString());
				HashMap<String, String> retMap = new HashMap<String, String>();
				retMap.put("codeUrl", codeUrl);
				retMap.put("orderNo", outTradeNo);
				return retMap;*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######toWxQrPay#######" + e);
			return null;
		}

	}

	public static void main(String args[]) throws Exception{
		PayVo payVo = new PayVo();
		payVo.setOut_trade_no(String.valueOf(System.currentTimeMillis()));
		payVo.setAttach("订单描述");
		payVo.setTotal_fee(1 + "");
		PayResult  payResult = new PayResult();
		payVo.setSpbill_create_ip("47.106.109.81");
		payVo.setBody("body");
		payVo.setOpenId("ooz-Z5RFqwBH0WXJF1p3p6jXGa_0");
		JSONObject ret = payResult.getPayResult(payVo);
		System.out.println("smallApp ret--->"+ret);
	
	}


	// 银联支付 unionpay
	@ResponseBody
	@RequestMapping(value = "/wxJspay")
	public BhResult wxJspay(HttpServletRequest request, HttpServletResponse response) {
		try {
			String json = request.getParameter("json");
			Member member = (Member) request.getSession(false).getAttribute(USERINFO);
			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(json, Map.class);
			
			Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
			Object mykey=myMap.get("mykey");
			if (mykey.equals("-1")) {
				return BhResult.build(400, myMap.get("order").toString(), null);
			}else if(mykey.equals("0")){
				//判断是否调用滨惠豆接口
				/*Set<Integer> goodsIdSet = new HashSet<>();
				List<OrderSku> orderSkuList=orderSkuMapper.selectByOrderNo(String.valueOf(((Order)myMap.get("order")).getOrderNo()));
				if(orderSkuList.size()>0&&orderSkuList!=null) {
					for (OrderSku orderSku : orderSkuList) {
						goodsIdSet.add(orderSku.getGoodsId());
					}
					List<Integer> isFreeList = actZoneGoodsMapper.selectIsFreePostage(goodsIdSet);
					if (isFreeList != null) {
						if (isFreeList.size() > 0 && isFreeList.size() == 1 && "1".equals(isFreeList.get(0)+"")) {
							BhResult myBhResult=simpleOrderService.getSimpleBhBean(map, member, request);
							if (myBhResult.getStatus()==400) {
								return BhResult.build(400, myBhResult.getMsg(), null);
							}
						}
					}
				}*/
				
				BhResult myBhResult=simpleOrderService.getSimpleBhBean(map, member, request);
				if (myBhResult.getStatus()==400) {
					return BhResult.build(400, myBhResult.getMsg(), null);
				}
				Order order2=(Order)myMap.get("order");
				//判断订单商品是否失效
				List<OrderShop> list = orderShopMapper.getByOrderId(order2.getId());
				for(int i = 0;i<list.size();i++) {
				   if(list.get(i).getStatus()==10||list.get(i).getStatus()==11||list.get(i).getStatus()==12) {
					    return BhResult.build(400, "订单已失效，请重拍", null);
				   }else {
					    //判断订单是否包换已下架商品
				        int num = orderSkuMapper.getByGoodsStatus(list.get(i).getId());
				        if(num>0) {
				    	      return BhResult.build(400, "商品已下架", null);
				        }else {
				    	      //商品没下架，判断订单是否包含有sku删除的
				    	      //List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(order2.getId());
				        	  List<OrderSku> orderSkuList = orderSkuMapper.getSkuListByOrderShopId(list.get(i).getId());
				        	  for(OrderSku o:orderSkuList) {
				    		       int numsku = orderSkuMapper.getByGoodsSkuStatus(o.getOrderShopId());
				    		       if(numsku>0) {
				    			     return BhResult.build(400, "商品已下架", null);
				    		       }
				    	      }
				        }
				   }
				}
				//如果是钱包支付 ，判断钱包金额是否大于订单金额 zlk
				//根据  用户id 获取 钱包信息
				if (order2.getPaymentId() == 7) {
					int walletMoney = 0;//钱包金额
					List<Wallet> wallet = walletMapper.getWalletByUid(order2.getmId());
					if (wallet.size() > 0) {
						walletMoney = wallet.get(0).getMoney();
					}
					if (order2.getOrderPrice() > walletMoney) {
						return BhResult.build(400, "钱包余额不足", null);
					}
				}
				if (order2.getCouponNum() != null && order2.getCouponNum() == 1) { // 判断优惠劵是否过期
					return BhResult.build(400, "优惠劵已过期", null);
				}
				
				String outTradeNo = order2.getOrderNo();
				String orderBody = "";
				Integer fz = order2.getGroupFz();
				if (fz == null) {
					orderBody = "null,1"; // 非拼团模式
				} else if (fz.intValue() == 1) {
					if (StringUtils.isBlank(order2.getTeamNo())) {
						orderBody = "null,2"; // 创团
					} else {
						orderBody = order2.getTeamNo() + ",3"; // 拼团
						//2018.5.10 zlk 
						int value =userOrderService.teamNumExcess(order2.getTeamNo(), "");
					    if(value<1) {
						    return BhResult.build(400, "参团人数已满", null);
					    }
						//end
					}
				} else if (fz.intValue() == 2) { // 秒杀
					orderBody = "null,5," + order2.getTgId();
				} else if (fz.intValue() == 3) {// 抽奖
					if (StringUtils.isBlank(order2.getTeamNo())) {
						orderBody = "null,7," + order2.getTgId();
					} else {
						orderBody = order2.getTeamNo() + ",8," + order2.getTgId();
					}
				} else if (fz.intValue() == 4) {// 惠省钱
					if (StringUtils.isBlank(order2.getTeamNo())) {
						if (StringUtils.isBlank(order2.getActNo())) {
							orderBody = "null,9," + order2.getTgId() + ",null";
						} else {
							orderBody = "null,9," + order2.getTgId() + "," + order2.getActNo();
						}
					} else {
						if (!StringUtils.isBlank(order2.getActNo())) {
							orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + ",null";
						} else {
							orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + "," + order2.getActNo();
						}
					}
				} else if (fz.intValue() == 5) {// 拍卖
					orderBody = "null,11";
				}
				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(order2.getShopId());// xieyc
				//程凤云：多商家（多余2或者2以上）支付时md5取滨惠自营的
				if (order2.getId()!=null) {
					List<OrderShop> shopList = simpleOrderService.selectOrderShopListByOrderId(order2.getId());
					if (shopList.size()>1) {
						memberShop = memberShopMapper.selectByPrimaryKey(1);
					}
				}
				//程凤云-结束	
				int amount=0;
				
				UnionPayVO vo = new UnionPayVO();
				/******************xieyc *******************/
				if(order2.getFz()==5){//=5为荷兰式拍卖
					BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order2.getOrderNo());//拍卖记录
					// 获取某个用户某个商品的某一期交纳的保证金是多少
					CashDeposit findCashDeposit = new CashDeposit();// 查询条件
					findCashDeposit.setmId(bargainRecord.getUserId());
					findCashDeposit.setGoodsId(bargainRecord.getGoodsId());
					findCashDeposit.setCurrentPeriods(bargainRecord.getCurrentPeriods());
					findCashDeposit.setIsrefund(0);
					CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);
					amount=order2.getOrderPrice()-cashDeposit.getDepositPrice();
					vo.setOriginalAmount(order2.getOrderPrice()-cashDeposit.getDepositPrice() + "");
					vo.setTotalAmount(order2.getOrderPrice()-cashDeposit.getDepositPrice()+ "");
				}else{
					amount=order2.getOrderPrice();
					vo.setOriginalAmount(order2.getOrderPrice() + "");
					vo.setTotalAmount(order2.getOrderPrice() +"");
				}
				/******************xieyc *******************/
				vo.setMd5Key(memberShop.getMd5Key());// xieyc(add)
				vo.setAttachedData(orderBody);
				vo.setMerOrderId(outTradeNo);			
								
				String jsonStr ="";
				if (Integer.parseInt(vo.getTotalAmount())>0&&order2.getPaymentId()==3) {//微信支付
					jsonStr= HttpService.doPostJson(UnionPayInterfaceEnum.WXJSPAY.getMethod(), vo);
					jsonStr = jsonStr.replaceAll("&", "&amp");
					
					//同步支付记录表
					OrderCollectionDoc ocd=new OrderCollectionDoc();
					ocd.setOrderId(order2.getId());
					ocd.setAddtime(new Date());
					ocd.setPaymentId(order2.getPaymentId());
					orderCollectionDocMapper.updateByOrderId(ocd);
					
					System.out.println("jsonStr replaceAll---->" + jsonStr);
					
				}else if(Integer.parseInt(vo.getTotalAmount())>0&&order2.getPaymentId()==7) {//钱包支付
					//钱包支付
					String[] strs = orderBody.split(",");
					payCallbackService.paySesessUnion(order2.getOrderNo(), "", strs);
                    Map<String,Object> map2 = new HashMap<String,Object>();
					map2.put("orderSign", vo.getMerOrderId());
					
					//同步支付记录表
					OrderCollectionDoc ocd=new OrderCollectionDoc();
					ocd.setOrderId(order2.getId());
					ocd.setAddtime(new Date());
					ocd.setPaymentId(order2.getPaymentId());
					orderCollectionDocMapper.updateByOrderId(ocd);
					
					
					return BhResult.build(200, "操作成功", map2);
				}else if(Integer.parseInt(vo.getTotalAmount())<=0 && order2.getPaymentId()==8){//免支付
					String[] strs = orderBody.split(",");
					payCallbackService.paySesessUnion(order2.getOrderNo(), "", strs);
					jsonStr ="0";
				}else if(order2.getPaymentId()==9){
					HttpPost httpPost = new HttpPost("http://39.104.48.52:8866/shop/order/pay");
					CloseableHttpClient client = HttpClients.createDefault();
					JSONObject jsonParam = new JSONObject();
					jsonParam.put("merchantOrderNo", order2.getOrderNo());//支付订单号
					jsonParam.put("amount",MoneyUtil.IntToDouble(amount)+"");//支付金额
					jsonParam.put("payNotifyUrl", Contants.BIN_HUI_URL+"/bh-order-api/payCallback/afterSucceedPay");//回调地址
					jsonParam.put("returnUrl","");
					jsonParam.put("attachedData",orderBody);
					StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
					entity.setContentEncoding("UTF-8");
					entity.setContentType("application/json");
					httpPost.setEntity(entity);
					HttpResponse resp = client.execute(httpPost);
					Integer statusCode=null;
					if (resp != null) {
						statusCode = resp.getStatusLine().getStatusCode();
						if (statusCode != null && statusCode == 200) {
							JSONObject jsonbject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
							if(jsonbject.getInt("code")==0){
								return BhResult.build(200, "操作成功", jsonStr);
							}
						}
					}
					return BhResult.build(400, "支付失败", null);
				}else{
					return BhResult.build(400, "支付方式不对", null);
				}
				return BhResult.build(200, "操作成功", jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######wxJspay#######" + e);
			return null;
			// TODO: handle exception
		}
		return null;

	}
	
	
	
	
	   // 原生小程序支付
		@ResponseBody
		@RequestMapping(value = "/wxSmallAppPay")
		public BhResult wxSmallAppPay(HttpServletRequest request, HttpServletResponse response) {
			try {
				String json = request.getParameter("json");
				Member member = (Member) request.getSession(false).getAttribute(USERINFO);
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(json, Map.class);
				String code = map.get("code");
				System.out.println("code---->"+code);
				Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
				Object mykey=myMap.get("mykey");
				if (mykey.equals("-1")) {
					return BhResult.build(400, myMap.get("order").toString(), null);
				}else {
					Order order2=(Order) myMap.get("order");
					// double price = (double) order2.getOrderPrice() / 100;
					String outTradeNo = order2.getOrderNo();
					String orderBody = "";
					Integer fz = order2.getGroupFz();
					// System.out.println("fz---->" + fz);
					// System.out.println("teamNo---->" + order2.getTeamNo());
					if (fz == null) {
						orderBody = "null,1"; // 非拼团模式
					} else if (fz.intValue() == 1) {
						if (StringUtils.isBlank(order2.getTeamNo())) {
							orderBody = "null,2"; // 创团
						} else {
							orderBody = order2.getTeamNo() + ",3"; // 拼团
						}
					} else if (fz.intValue() == 2) { // 秒杀
						orderBody = "null,5," + order2.getTgId();
					} else if (fz.intValue() == 3) {// 抽奖
						if (StringUtils.isBlank(order2.getTeamNo())) {
							orderBody = "null,7," + order2.getTgId();
						} else {
							orderBody = order2.getTeamNo() + ",8," + order2.getTgId();
						}
					} else if (fz.intValue() == 4) {// 惠省钱
						if (StringUtils.isBlank(order2.getTeamNo())) {
							if (StringUtils.isBlank(order2.getActNo())) {
								orderBody = "null,9," + order2.getTgId() + ",null";
							} else {
								orderBody = "null,9," + order2.getTgId() + "," + order2.getActNo();
							}
						} else {
							if (!StringUtils.isBlank(order2.getActNo())) {
								orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + ",null";
							} else {
								orderBody = order2.getTeamNo() + ",10," + order2.getTgId() + "," + order2.getActNo();
							}
						}
					} else if (fz.intValue() == 5) {// 拍卖
						orderBody = "null,11";
						//程凤云添加代码:判断拍卖商品是否过了活动时间如果为0代表已过时间
						int row = simpleOrderService.isOverTopicTime(order2.getId());
						if (row == 0) {
							return BhResult.build(400, "活动已结束,请选择其他商品", null);
						}
					}

					String openid = WXPayUtil.getSmallAppOpenid(Contants.sAppId, Contants.sAppSecret, code);
					PayVo payVo = new PayVo();
					payVo.setOpenId(openid);
					payVo.setOut_trade_no(outTradeNo);
					payVo.setAttach(orderBody);
					payVo.setTotal_fee(order2.getOrderPrice() + "");
					String spbill_create_ip = IPUtils.getIpAddr(request);//终端IP   
					payVo.setSpbill_create_ip(spbill_create_ip);
					payVo.setBody("订单描述");
					PayResult  payResult = new PayResult();
					JSONObject ret = payResult.getPayResult(payVo);

					System.out.println("smallApp ret--->"+ret);
					if (Integer.parseInt(payVo.getTotal_fee())>0) {
						return BhResult.build(200, "操作成功", ret.toString());
					}else{
						String[] strs = orderBody.split(",");
						payCallbackService.paySesessUnion(order2.getOrderNo(), "", strs);
						return BhResult.build(200, "操作成功", "0");
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#######wxSmallAppPay#######" + e);
				return null;
				// TODO: handle exception
			}

		}


	/**
	 * xxj
	 * 根据订单号一键促团
	 * @return
	 */
	@RequestMapping("/promoteTeamByOrderNo")
	@ResponseBody
	public BhResult promoteTeamByOrderNo(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult r = null;
	   try {
		    String orderNo = map.get("orderNo");
		    sysService.promoteTeamByOrderNo(orderNo);
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			logger.error("#######promoteTeamByOrderNo#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	/**
	 * xxj
	 * 根据商品ID一键促团
	 * @return
	 */
	@RequestMapping("/promoteTeamByGoodsId")
	@ResponseBody
	public BhResult promoteTeamByGoodsId(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult r = null;
	   try {
		    String goodsIdStr = map.get("goodsId");
		    sysService.promoteTeamByGoodsId(Integer.parseInt(goodsIdStr));
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			logger.error("#######promoteTeamByGoodsId#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	public static void main14(String[] a ) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("cartIds", "1");
		List<String> cartIds= java.util.Arrays.asList(map.get("cartIds").split(","));
		for (int i = 0; i < cartIds.size(); i++) {
			System.out.println(cartIds.get(i));
		}
		
	}

}
