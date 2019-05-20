package com.order.user.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.CommentSimple;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.MyGoodsComment;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.JsonUtils;

import com.bh.utils.PageBean;
import com.order.user.service.CommentService;
import com.order.user.service.UserOrderService;

@Controller
@RequestMapping("/order")
public class CommentController {
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	@Value("${USERINFO}")
	private String USERINFO;

	@Autowired
	private CommentService commentService;
	@Autowired
	private UserOrderService userOrderService;

	/******** 待评价的商品列表的显示 **********/
	@RequestMapping(value = "/waitcommentlist", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showWaitCommentList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		try {
			// 获取用户ID
			OrderShop orderShop = new OrderShop();
			orderShop.setmId(member.getId());
			orderShop.setStatus(Contants.shopStatu5);// 等待评价
			PageBean<OrderShop> orderShop2 = new PageBean<>();
			orderShop2 = commentService.showWaitCommentList(orderShop, Contants.PAGE, Contants.SIZE);
			bhResult = new BhResult(200, "查询成功", orderShop2);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######waitcommentlist#######" + e);
			bhResult = new BhResult(500, "查询失败", null);
		}
		return bhResult;
	}

	/******** 待评价的商品列表的显示 2018-6-12 **********/
	@RequestMapping(value = "/waitcommentlistNew", method = RequestMethod.POST)
	@ResponseBody
	public BhResult waitcommentlistNew(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		try {
			bhResult = new BhResult(200, "查询成功", commentService.selectMyWaitCommentList(member.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######waitcommentlistNew#######" + e);
			bhResult = new BhResult(500, "查询失败", null);
		}
		return bhResult;
	}

	/****************************** 点击点进去后的移动端的显示 ***************************/
	@RequestMapping(value = "/showcommentbymove", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showCommentByMove(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		String orderSkuId = map.get("orderSkuId");
		try {
			if (StringUtils.isNotEmpty(orderSkuId)) {
				// 获取用户ID
				OrderSku orderShop2 = new OrderSku();
				orderShop2 = commentService.selectOrderSkuById(Integer.parseInt(orderSkuId));
				bhResult = new BhResult(200, "查询成功", orderShop2);
			} else {
				bhResult = new BhResult(400, "参数不能为空", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showcommentbymove#######" + e);
			bhResult = new BhResult(500, "查询失败", null);
		}
		return bhResult;
	}

	// 订单详情
	@RequestMapping(value = "/showcommentbypc")
	@ResponseBody
	public BhResult orderDetail(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;

		try {
			// 1:接收前台传参
			String orderId = map.get("id");
			OrderShop orderShop = new OrderShop();

			orderShop.setId(Integer.parseInt(orderId));
			orderShop.setmId(member.getId());

			OrderShop list = commentService.selectOrderShopBySelectSingle(orderShop);
			if (list != null) {
				// bhResult = new BhResult(200, "查询成功", list);
				// bhResult = new BhResult(BhResultEnum.SUCCESS,list);
				bhResult = new BhResult(200, "查询成功", list);
			} else {
				bhResult = new BhResult(400, "无数据", null);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showcommentbypc#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	// 用户能评价的前提是：order_main的status字段为4;order_sku的d_send的配送状态为3，是吧？
	@RequestMapping(value = "/addcomment", method = RequestMethod.POST)
	@ResponseBody
	public BhResult addComment(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		String json = map.get("json");

		try {

			if (member != null) {
				if (StringUtils.isNotEmpty(json)) {

					// 判断是否为空
					List<CommentSimple> commentSimple = JsonUtils.jsonToList(json, CommentSimple.class);
					int row = 0;
					List<String> ids = new ArrayList<>();
					for (CommentSimple commentSimple2 : commentSimple) {
						ids.add(String.valueOf(commentSimple2.getId()));
					}
					if (commentSimple.size() > 1) {
						row = commentService.insertGoodsComment(commentSimple, member.getId());
					} else if (commentSimple.size() == 1) {
						List<GoodsComment> list = commentService.getListComment(ids);
						if (list.size() > 0) {// 如果在评论表里面找到，则已评论
							row = 2;
						} else {
							row = commentService.insertGoodsComment(commentSimple, member.getId());
						}
					}
					if (row == 0) {
						bhResult = new BhResult(BhResultEnum.FAIL, "评价失败");
					} else if (row == 1) {
						bhResult = new BhResult(200, "评价成功", null);
					} else if (row == 2) {
						bhResult = new BhResult(400, "该商品已评价", null);
					} else {

					}

				}

			} else {
				bhResult = new BhResult(100, "未登录", member);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addcomment#######" + e);
			bhResult = new BhResult(500, "评论失败了，请重新评论", null);
		}
		return bhResult;
	}

	/**
	 * chengfengyun-2017-11-10 已评价商品列表
	 */
	@RequestMapping("/showcomment")
	@ResponseBody
	public BhResult showComment(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		String p = map.get("page");
		String r = map.get("size");
		try {

			Integer page = 0;
			Integer rows = 0;

			if (p == null || p.equals("")) {
				page = Contants.PAGE;
			} else {
				page = Integer.parseInt(p);
			}
			if (r == null || r.equals("")) {
				rows = Contants.SIZE;
			} else {
				rows = Integer.parseInt(r);
			}
			GoodsComment comment = new GoodsComment();
			comment.setmId(member.getId());
			List<MyGoodsComment> list = commentService.showCommentList(comment, page, rows);
			Map<String, Object> my = new HashMap<>();
			my.put("list", list);
			bhResult = new BhResult(200, "查询成功", my);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showcomment#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/** 提交订单:在直商品页面接点购买 */
	@RequestMapping(value = "/selectOrderByOrderNo")
	@ResponseBody
	public BhResult selectOrderByOrderNo(@RequestBody Map<String, String> map, HttpServletRequest request,
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
				bhResult = new BhResult(BhResultEnum.SUCCESS, order);
			}

			bhResult.setStatus(200);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectOrderByOrderNo#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

}
