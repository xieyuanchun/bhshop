package com.order.user.controller;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.OrderCleanAccount;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.Track;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.pojo.*;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.PageBean;
import com.order.annotaion.OrderLogAnno;
import com.order.user.service.SimpleOrderService;
import com.order.user.service.UserOrderService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class SimpleOrderController {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOrderController.class);
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private JDOrderController jdOrderC;
	@Autowired
	private SimpleOrderService simpleOrderService;

	/** 根据 id查找order_main的记录(pc端用) */
	@RequestMapping(value = "/selectordermainbyid")
	@ResponseBody
	public BhResult selectOrdermainById(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			if (StringUtils.isEmpty(id)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				Order order = new Order();
				order.setId(Integer.parseInt(id));
				order.setStatus(2);
				Order order2 = new Order();
				order2 = userOrderService.selectOrderById(order);
				bhResult = new BhResult(200, "查询成功", order2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectordermainbyid#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 当鼠标onmouseover‘一键购’:在直商品页面接点购买(pc端用) */
	@RequestMapping(value = "/selectDefaultAddress")
	@ResponseBody
	public BhResult selectDefaultAddress(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			MemberUserAddress address = new MemberUserAddress();
			address.setmId(member.getId());
			// 1对应true,0对应false
			address.setIsDefault(true);
			MemberUserAddress address1 = userOrderService.selecUseraddresstBySelect(address);
			if (address1 != null) {
				bhResult = new BhResult(200, "查询成功", address1);
			} else {
				bhResult = new BhResult(400, "查询成功，默认地址无,快去设置默认地址吧", address1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectDefaultAddress#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 显示用户的所有地址(pc端) */
	@RequestMapping(value = "/selectUserAddress")
	@ResponseBody
	public BhResult selectUserAddress(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			List<MemberUserAddress> address1 = userOrderService.selectUserAllAddress(member.getId());
			if (address1 != null) {
				bhResult = new BhResult(200, "查询成功", address1);
			} else {
				bhResult = new BhResult(400, "查询成功，默认地址无,快去设置默认地址吧", address1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectUserAddress#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 根据地址主键查询地址 (pc端) */
	@RequestMapping(value = "/selectUserAddressByPrimarykey")
	@ResponseBody
	public BhResult selectUserAddressByPrimarykey(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String addressId = map.get("addressId");
			MemberUserAddress address1 = userOrderService.selectUserAllAddressByPrimarykey(Integer.parseInt(addressId));
			if (address1 != null) {
				bhResult = new BhResult(200, "查询成功", address1);
			} else {
				bhResult = new BhResult(400, "查询成功，默认地址无,快去设置默认地址吧", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectUserAddressByPrimarykey#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 回显cartId */
	@RequestMapping(value = "/addorderbycart")
	@ResponseBody
	public BhResult addOrderByCart(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		BhResult bhResult = null;
		// cartIds以字符串+逗号的形式返回
		String id = map.get("cartIds");
		try {
			if (StringUtils.isEmpty(id)) {
				bhResult = new BhResult(400, "购物车的id不能为空", null);
			} else {
				bhResult = simpleOrderService.getGoodsByCartId(id);
				if (bhResult.getStatus() == 400) {
					return bhResult = new BhResult(400, Contants.GOODS_NUM, id);
				} else {
					bhResult = new BhResult(200, "返回成功", id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addorderbycart#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 提交订单:填写并核对订单信息(pc端和移动端) */
	@RequestMapping(value = "/showorderbycart", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showOrderByCart(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		// 注意:如果是从商品详情过来的话cartIds传的是地址懒的cartIds的值,orderId=0,如果是从订单列表的贷付款的过来的话cartIds='',orderIds=订单的id
		// cartIds以字符串+逗号的形式传参
		String id = map.get("cartIds");
		// 订单的id
		String orderId = map.get("orderId");
		// 是否拼团:0不是拼团,1拼团
		String fz = map.get("fz");
		String teamNo = map.get("teamNo");
		try {
			if (StringUtils.isEmpty(id) && StringUtils.isEmpty(orderId)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				CleanAccount cleanAccount = null;
				// 注意:如果是从商品详情过来的话cartIds传的是地址懒的cartIds的值,orderId=0
				if (StringUtils.isNotBlank(id) && id != null && id != "" && !id.equals("null")) {
					cleanAccount = userOrderService.selecOnePreOrderInfo(id, String.valueOf(member.getId()), fz,
							teamNo);
					bhResult = new BhResult(400, "该商品未有对应的sku的id", null);
				} else {
					// 如果是从订单列表的贷付款的过来的话cartIds='',orderIds=订单的id
					OrderShop orderShop = new OrderShop();
					orderShop.setOrderId(Integer.parseInt(orderId));
					orderShop.setmId(member.getId());
					cleanAccount = userOrderService.orderCancleBuy(orderShop, fz);

					bhResult = new BhResult(200, "返回成功", cleanAccount);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showorderbycart#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 订单列表（全部订单） */
	@RequestMapping(value = "/orderlist", method = RequestMethod.POST)
	@ResponseBody
	public BhResult orderShopList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		// 获取用户ID
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);

		String page = map.get("page");// 第几页
		String rows = map.get("rows");// 每页的大小

		if (page == null || page.equals("")) {
			page = String.valueOf(Contants.PAGE);
		}
		if (rows == null || rows.equals("")) {
			rows = String.valueOf(Contants.SIZE);
		}

		OrderShop orderShop = new OrderShop();
		// 订单状态1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除、9待分享
		String status = map.get("status");
		// 订单号的查询
		String orderNo = map.get("orderNo");
		// 按商品名称查询
		String goodsName = map.get("goodsName");
		if (StringUtils.isNotEmpty(orderNo)) {
			orderShop.setOrderNo(orderNo);
		}
		// 1下单，2已付，3待评价，4完成
		if (status != null && !("").equals(status)) {
			orderShop.setStatus(Integer.parseInt(status));
		}
		if (StringUtils.isNotEmpty(goodsName)) {
			orderShop.setGoodsName(goodsName);
		}
		orderShop.setmId(member.getId());
		try {
			PageBean<OrderShop> pageBean = userOrderService.selectAllOrderShopList(orderShop, Integer.parseInt(page),
					Integer.parseInt(rows));
			if (pageBean != null) {
				bhResult = new BhResult(200, "查询成功", pageBean);
			} else {
				bhResult = new BhResult(400, "查询成功,暂无数据", pageBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######orderlist#######" + e);
			bhResult = new BhResult(500, "查询失败", null);
		}
		return bhResult;
	}

	/** 订单详情 */
	@RequestMapping(value = "/orderdetail")
	@ResponseBody
	public BhResult orderDetail(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		/********* xieyc add ************/
		OrderShop os = orderShopMapper.selectByPrimaryKey(Integer.parseInt(map.get("id")));
		if (member.getId().intValue() != os.getmId().intValue()) {// 商家订单号id对应的mId与登入mid不对应时提示用微信登入(适用于微信推送消息跳转到订单详情页)
			bhResult = new BhResult(100, "请用微信登入!", null);
		}
		if (bhResult != null) {
			return bhResult;
		}
		/********* xieyc add ************/
		try {
			// 1:接收前台传参
			String orderId = map.get("id");
			OrderShop orderShop = new OrderShop();
			orderShop.setId(Integer.parseInt(orderId));
			orderShop.setmId(member.getId());
			OrderShop list = userOrderService.selectOrderShopBySelectSingle(orderShop);
			OrderShop orderShop1 = new OrderShop();
			orderShop1.setmId(member.getId());
			Track track = new Track();
			track.setMsgTime("");
			track.setOperator("");
			try {
				// 调用移动端 订单 物流信息接口
				BhResult bhResult2 = jdOrderC.orderTrack(map, request, response);
				OrderStock orderStock2 = (OrderStock) bhResult2.getData();
				if (orderStock2.getJd().equals("0")) {
					// 速达、商家自配
					list.setJd("0");
					list.setOrderTrack(orderStock2.getOrderTrack());
					track.setContent(list.getOrderTrack().get(0).getContent());
				} else if (orderStock2.getJd().equals("1")) {
					// 其他物流
					list.setJd("1");
					list.setLogistics(orderStock2.getLogistics());
					track.setContent(orderStock2.getLogistics() + "");
				} else if (orderStock2.getJd().equals("2")) {
					// 京东物流
					list.setJd("2");
					list.setOrderTrack(orderStock2.getOrderTrack());
					track.setContent(
							orderStock2.getOrderTrack().get(orderStock2.getOrderTrack().size() - 1).getContent());
					track.setMsgTime(
							orderStock2.getOrderTrack().get(orderStock2.getOrderTrack().size() - 1).getMsgTime() + "");
					track.setOperator(
							orderStock2.getOrderTrack().get(orderStock2.getOrderTrack().size() - 1).getOperator() + "");
				}
				list.setTrack(track);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			bhResult = new BhResult(200, "查询成功", list);

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("#######orderdetail#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	
	
	// 用户取消订单
	@OrderLogAnno("取消订单")
	@RequestMapping(value = "/cancelorder")
	@ResponseBody
	public BhResult orderCancel(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			// 1:接收前台传参,订单的id
			String Id = map.get("id");
			OrderShop orderShop = new OrderShop();
			if (member != null) {
				orderShop.setmId(member.getId());
				orderShop.setStatus(Contants.IS_CANCEL);
				orderShop.setId(Integer.valueOf(Id));
				userOrderService.updateBhdAndCouponSecond(orderShop);
				int row = userOrderService.updateOrderStatus(orderShop, Id);
				if (row == 1) {
					bhResult = new BhResult(200, "成功", null);
				} else {
					bhResult = new BhResult(400, "无数据", null);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cancelorder#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	// 用户删除订单
	@RequestMapping(value = "/deleteorder")
	@ResponseBody
	public BhResult deleteOrder(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);

		BhResult bhResult = null;

		try {
			// 1:接收前台传参,订单的id
			String orderId = map.get("id");
			OrderShop orderShop = new OrderShop();
			if (member != null) {
				orderShop.setmId(member.getId());
				orderShop.setStatus(Contants.IS_DELETE);
				int row = userOrderService.updateOrderStatus(orderShop, orderId);
				if (row == 1) {
					bhResult = new BhResult(200, "删除成功", null);
				} else {
					bhResult = new BhResult(400, "删除失败", null);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######deleteorder#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 一键购 */
	@OrderLogAnno("一键购1 ")
	@RequestMapping(value = "/oneorderbuy")
	@ResponseBody
	public BhResult oneOrderBuy1(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);

		BhResult bhResult = null;

		try {
			String goodId = map.get("goodId");
			String skuId = map.get("skuId");
			String num = map.get("num");
			String fz = map.get("fz"); // 1一键购
			String teamNo = map.get("teamNo");

			if (StringUtils.isEmpty(goodId)) {
				return bhResult = new BhResult(400, "商品id不能为空", null);
			} else if (StringUtils.isEmpty(skuId)) {
				return bhResult = new BhResult(400, "商品的属性ID不能为空", null);
			} else if (StringUtils.isEmpty(num)) {
				return bhResult = new BhResult(400, "商品的数量不能为空", null);
			} else if (StringUtils.isNotEmpty(num) && Integer.parseInt(num) < 1) {
				return bhResult = new BhResult(400, Contants.GOODS_NUM, null);
			} else {
				// 判断商品拼团人数是否超额 2018.5.7 zlk
				if (StringUtils.isNotBlank(fz) && fz.equals("1")) {
					int value = userOrderService.teamNumExcess(teamNo, goodId);
					if (value < 1) {
						return bhResult = new BhResult(400, "参团人数已满", null);
					}
				}
				// end
				// row:0代表可进行到下一步，1(普通商品)代表用户已参团，2(惠省钱商品)代表用在此专区的参团次数已达上限，请前往开团
				int row = userOrderService.getByMidAndTeamNo(member.getId(), teamNo, goodId);
				if (row == 1) {
					bhResult = new BhResult(400, "您已参团", null);
				} else if (row == 2) {
					bhResult = new BhResult(400, "您在此专区的参团次数已达上限，请前往开团", null);
				} else {
					GoodsCart goodsCart = new GoodsCart();
					goodsCart.setIsDel(3);
					goodsCart.setmId(member.getId());
					goodsCart.setAddtime(new Date());
					goodsCart.setNum(Integer.parseInt(num));
					goodsCart.setgId(Integer.parseInt(goodId));
					goodsCart.setGskuid(Integer.parseInt(skuId));

					GoodsSku goodsSku = new GoodsSku();
					MemberShop memberShop = new MemberShop();
					goodsSku = userOrderService.getGoodsSkuById(Integer.parseInt(skuId));// 通过商品的goodsId获取它的Sku
					goodsCart.setGoodsSkus(goodsSku);
					Goods good = new Goods();
					good = userOrderService.selectBygoodsId(Integer.parseInt(goodId));
					goodsCart.setGoodName(good.getName());
					goodsCart.setShopId(good.getShopId());
					memberShop = userOrderService.selectMemberShopByGoodId(good.getShopId());
					goodsCart.setShopName(memberShop.getShopName());
					goodsCart.setSellPrice(goodsSku.getSellPrice());
					goodsCart.setTeamPrice(goodsSku.getTeamPrice());
					goodsCart.setgImage(good.getImage().split(",")[0]);
					GoodsCart g = userOrderService.insertGoodsCart(goodsCart);
					Integer cartId = g.getId();

					Map<String, Object> iMap = new LinkedHashMap<>();
					iMap.put("cartId", cartId);
					iMap.put("fz", fz);
					iMap.put("teamNo", teamNo);
					bhResult = new BhResult(200, "一键购成功", iMap);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######oneorderbuy#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 用户确认收货 */
	@RequestMapping(value = "/confirmorder")
	@ResponseBody
	public BhResult confirmOrder(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			String orderId = map.get("id");
			OrderShop orderShop = new OrderShop();
			if (member != null) {
				orderShop.setmId(member.getId());
				orderShop.setStatus(Contants.shopStatu5);
				orderShop.setReceivedtime(new Date());
				int row = userOrderService.updateOrderStatus(orderShop, orderId);
				if (row == 1) {
					bhResult = new BhResult(200, "确认收货成功", orderShop);
				} else {
					bhResult = new BhResult(200, "确认收货成功", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######confirmorder#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/*********** 订单数量 **********/
	@RequestMapping(value = "/getOrderInfo")
	@ResponseBody
	public BhResult getOrderInfo(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			OrderShop orderShop = new OrderShop();
			orderShop.setmId(member.getId());
			OrderInfoPojo info = userOrderService.totalShopOrderNum(orderShop);
			bhResult = new BhResult(200, "查询成功", info);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getOrderInfo#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/*** 订单取消后再次购买 ***/
	@RequestMapping(value = "/ordercanclebuy")
	@ResponseBody
	public BhResult orderCancleBuy(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			String orderId = map.get("id");
			String fz = map.get("fz");
			OrderShop orderShop = new OrderShop();
			orderShop.setOrderId(Integer.parseInt(orderId));
			orderShop.setmId(member.getId());
			CleanAccount list = userOrderService.orderCancleBuy(orderShop, fz);
			if (list != null) {
				bhResult = new BhResult(200, "查询成功", list);
			} else {
				bhResult = new BhResult(400, "无数据", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######ordercanclebuy#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/*** 订单取消后再次购买前获得订单好 ***/
	@RequestMapping(value = "/getorderid")
	@ResponseBody
	public BhResult getOrderId(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String orderId = map.get("orderId");
			bhResult = new BhResult(200, "查询成功", orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getorderid#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/*** 通过order_main查询下面的项目 ***/
	@RequestMapping(value = "/oneOrderMain")
	@ResponseBody
	public BhResult selectOneOrderMain(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");// 通过orderMain的id查询
			OrderShop orderShop = new OrderShop();
			orderShop.setOrderId(Integer.parseInt(id));
			List<OrderShop> list = userOrderService.selectOrderMainById(orderShop);
			if (list != null) {
				bhResult = new BhResult(200, "查询成功", list);
			} else {
				bhResult = new BhResult(400, "无数据", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######oneOrderMain#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 2018年5月19日 */
	@RequestMapping(value = "/showorderbycart1", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showOrderByCart1(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		// 注意:如果是从商品详情过来的话cartIds传的是地址懒的cartIds的值,orderId=0,如果是从订单列表的贷付款的过来的话cartIds='',orderIds=订单的id
		String id = map.get("cartIds");
		// 订单的id
		String orderId = map.get("orderId");
		// 是否拼团:0不是拼团,1拼团
		String fz = map.get("fz");
		String teamNo = map.get("teamNo");
		
		try {
			if (StringUtils.isEmpty(id) && StringUtils.isEmpty(orderId)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				OrderCleanAccount cleanAccount = null;
				
				// 注意:如果是从商品详情过来的话cartIds传的是地址懒的cartIds的值,orderId=0
				if (StringUtils.isNotBlank(id) && id != null && id != "" && !id.equals("null")) {
					cleanAccount = simpleOrderService.selecOnePreOrderInfo(id, member.getId().toString(), fz,
							teamNo);
					bhResult = new BhResult(200, "返回成功", cleanAccount);
				} else {
					// 如果是从订单列表的贷付款的过来的话cartIds='',orderIds=订单的id
					OrderShop orderShop = new OrderShop();
					orderShop.setOrderId(Integer.parseInt(orderId));
					orderShop.setmId(member.getId());
					cleanAccount = simpleOrderService.orderCancleBuy(orderShop, fz);
					bhResult = new BhResult(200, "返回成功", cleanAccount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showorderbycart1#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 订单列表（全部订单）,从order_shop中查找 */
	@RequestMapping(value = "/orderlistOptimize", method = RequestMethod.POST)
	@ResponseBody
	public BhResult orderlistOptimize(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		// 获取用户ID
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		String page = map.get("page");// 第几页
		String rows = map.get("rows");// 每页的大小
		if (page == null || page.equals("")) {
			page = String.valueOf(Contants.PAGE);
		}
		if (rows == null || rows.equals("")) {
			rows = String.valueOf(Contants.SIZE);
		}
		OrderShop orderShop = new OrderShop();
		// 订单状态1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除、9待分享
		String status = map.get("status");
		// 订单号的查询
		String orderNo = map.get("orderNo");
		// 按商品名称查询
		String goodsName = map.get("goodsName");
		if (StringUtils.isNotEmpty(orderNo)) {
			orderShop.setOrderNo(orderNo);
		}
		// 1下单，2已付，3待评价，4完成
		if (status != null && !("").equals(status)) {
			orderShop.setStatus(Integer.parseInt(status));
		}
		if (StringUtils.isNotEmpty(goodsName)) {
			orderShop.setGoodsName(goodsName);
		}
		orderShop.setmId(member.getId());
		try {
			Map<String, Object> myMap = simpleOrderService.selectOrderList(orderShop, Integer.parseInt(page),
					Integer.parseInt(rows));
			bhResult = new BhResult(200, "查询成功", myMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######orderlistOptimize#######" + e);
			bhResult = new BhResult(500, "查询失败", null);
		}
		return bhResult;
	}

	// 订单详情
	@RequestMapping(value = "/orderdetailOptimize")
	@ResponseBody
	public BhResult orderDetailOptimize(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		/********* xieyc add ************/
		OrderShop os = orderShopMapper.selectByPrimaryKey(Integer.parseInt(map.get("id")));
		if (member.getId().intValue() != os.getmId().intValue()) {// 商家订单号id对应的mId与登入mid不对应时提示用微信登入(适用于微信推送消息跳转到订单详情页)
			bhResult = new BhResult(100, "请用微信登入!", null);
		}
		if (bhResult != null) {
			return bhResult;
		}
		/********* xieyc add ************/
		try {
			// 接收前台传参
			String id = map.get("id");
			OrderShop orderShop = new OrderShop();
			orderShop.setId(Integer.parseInt(id));
			orderShop.setmId(member.getId());			
			MyOrderShopDetail detail = simpleOrderService.selectOrderShopBySelectSingle(orderShop);
			bhResult = new BhResult(200, "查询成功", detail);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######orderdetailOptimize#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}


	
	
}
