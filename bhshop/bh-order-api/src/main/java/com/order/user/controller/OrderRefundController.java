package com.order.user.controller;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.bh.config.Contants;
import com.bh.goods.mapper.ActivityTimeMapper;
import com.bh.goods.mapper.AspUserGuessMapper;
import com.bh.goods.pojo.ActivityTime;
import com.bh.goods.pojo.AspUserGuess;
import com.bh.goods.pojo.Goods;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderCollectionDoc;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.order.annotaion.OrderLogAnno;
import com.order.enums.RefundReasonEnum;
import com.order.user.service.OrderRefundService;

@Controller
@RequestMapping("/order")
public class OrderRefundController {
	private static final Logger logger = LoggerFactory.getLogger(OrderRefundController.class);
	@Value("${USERINFO}")
	private String USERINFO;

	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private AspUserGuessMapper aspUserGuessMapper;
	@Autowired
	private ActivityTimeMapper activityTimeMapper;
	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;

	/**
	 * chengfengyun-2017-11-16 退款商品
	 */
	@RequestMapping(value = "/showrefundname")
	@ResponseBody
	public BhResult showrefundName(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String orderSkuId = map.get("orderSkuId");
			OrderSku orderSku=orderSkuMapper.selectByPrimaryKey(Integer.valueOf(orderSkuId));
			Order order=orderMapper.selectByPrimaryKey(orderSku.getOrderId());
			if (StringUtils.isEmpty(orderSkuId)) {
				bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList());
			}else{
				//查看该该商品是否申请过售后服务
				Integer oSkuStatus=orderRefundService.selectIsRefund(Integer.parseInt(orderSkuId));
				if (oSkuStatus !=null) {
					//未申请过售后服务或者或者之前的信息未记录过
					if (oSkuStatus == 0) {
						List<OrderRefundDoc> myList=orderRefundService.selectOrderRefundDoc(Integer.parseInt(orderSkuId));
						//已申请过售后服务
						if (myList.size()>0) {
							//refund_type:售后类型 1:退款 2:换货 3:退款退货
							if (myList.get(0).getRefundType() ==1) {
								bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList2());
							}else if (myList.get(0).getRefundType() ==2) {
								bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList5Or7());
							}else if (myList.get(0).getRefundType() ==3) {
								bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList5Or7());
							}
						}else{
							//未申请过售后服务
							int oShopStatus=orderRefundService.selectOShopStatus(Integer.parseInt(orderSkuId));
							bhResult = refundName(oShopStatus,order);
						}
					}
					else{
						bhResult =refundName(oSkuStatus,order);
					}
				}else{
					bhResult = new BhResult(400, "该商品不支持售后服务", null);
				}
			}
		

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showrefundname#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}
	public BhResult refundName(Integer oSkuStatus,Order order) {
		BhResult bhResult = null;
		try {
			if (oSkuStatus !=null) {
				if (oSkuStatus == 0) {
					
				}else if (oSkuStatus == 2||oSkuStatus==9) {
					bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList2());
				}
				else if (oSkuStatus == 5||oSkuStatus==7) {
					//待评价
					//范进红新增2018.8.24
					OrderCollectionDoc orderCollectionDoc=orderCollectionDocMapper.selectByOrderId(order.getId());
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
					String applyTime=sf.format(orderCollectionDoc.getAddtime());
					ActivityTime activityTime=activityTimeMapper.selectByPrimaryKey(1);
					String startTime=sf.format(activityTime.getStartTime());
					String endTime =sf.format(activityTime.getEndTime());
					String nowTime =sf.format(new Date());//当前时间
					int result=applyTime.compareTo(startTime); //大于
					int result1=endTime.compareTo(applyTime); //大于
					int result2=nowTime.compareTo(endTime); //大于
					AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(order.getmId());
					if(result>0&&result1>0&&result2>0) {//支付时间大于活动开始时间并且小于结束时间并且现在时间（发起退款的时间）大于结算时间
					    if(aspUserGuess!=null) {
					    	bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList5()); //只能换货
						}else {
							bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList5Or7());
						}
					}else{
						bhResult = new BhResult(200, "查询成功", RefundReasonEnum.getRefundReasonList5Or7());
					}
				}else{
					bhResult = new BhResult(400, "该商品不支持售后服务", null);
				}
			}else{
				bhResult = new BhResult(400, "该商品不支持售后服务", null);
			}
		
		

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######refundName#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * chengfengyun-2017-11-14 退款商品
	 */
	@RequestMapping(value = "/showrefundgoods")
	@ResponseBody
	public BhResult showRefundGoods(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			// 获得参数
			String orderSkuId = map.get("orderSkuId");
			if (StringUtils.isEmpty(orderSkuId)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			} else {
				OrderSku orderRefundDoc = new OrderSku();
				orderRefundDoc.setId(Integer.parseInt(orderSkuId));
				bhResult = orderRefundService.showRefundGoods(orderRefundDoc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showrefundgoods#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * chengfengyun-2017-11-14 退款
	 */
	@RequestMapping(value = "/refundgoods")
	@ResponseBody
	@OrderLogAnno("用户申请退款")
	public BhResult RefundGoods(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BhResult bhResult = null;
		try {
			// 获得参数
			String orderSkuId = map.get("orderSkuId");
			String reason = map.get("reason");
			String note1 = map.get("note");
			String mName1 = map.get("mName");
			String mPhone1 = map.get("mPhone");
			String afterSaleReasons = map.get("afterSaleReasons");
			String specifications = map.get("specifications"); //规格
			String voucherImage = map.get("voucherImage"); //凭证图片
			String img = map.get("img");
			if (StringUtils.isEmpty(orderSkuId)) {
				bhResult = new BhResult(400, "参数不能为空", null);
			}

			else {
				OrderShop orderShop = orderRefundService.selectOrderShopStatus(Integer.parseInt(orderSkuId));
				Goods goods = orderRefundService.selectGoodsById(Integer.parseInt(orderSkuId));
				OrderRefundDoc orderRefundDoc = new OrderRefundDoc();
				if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(note1)) {
					String note = URLEncoder.encode(note1, "utf-8");
					orderRefundDoc.setNote(note);
					
				}
				if (StringUtils.isNotEmpty(reason)) {
					orderRefundDoc.setRefundType(Integer.parseInt(reason));
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher isNum = pattern.matcher(reason);
					if (isNum.matches()) {
						switch (Integer.parseInt(reason)) {
						case 1:
							orderRefundDoc.setReason(RefundReasonEnum.REFUND_MENEY.getReason());
							break;
						case 2:
							orderRefundDoc.setReason(RefundReasonEnum.REFUND_GOODS.getReason());
							break;
						case 3:
							orderRefundDoc.setReason(RefundReasonEnum.REFUND_MENEY_GOODS.getReason());
							break;
						}
					} else {
						orderRefundDoc.setReason(reason);
					}

				}
				if (StringUtils.isNotEmpty(mName1)) {
					String mName= URLEncoder.encode(mName1, "utf-8");
					orderRefundDoc.setmName(mName);
				}
				if (StringUtils.isNotEmpty(mPhone1)) {
					String mPhone = URLEncoder.encode(mPhone1, "utf-8");
					orderRefundDoc.setmPhone(mPhone);
				}
				if (StringUtils.isNotEmpty(img)) {
					orderRefundDoc.setImg(img);
				}
				if (StringUtils.isNotEmpty(specifications)) {
					orderRefundDoc.setSpecifications(specifications);
				}
				if (StringUtils.isNotEmpty(voucherImage)) {
					orderRefundDoc.setVoucherImage(voucherImage);
				}
				if (StringUtils.isNotEmpty(afterSaleReasons)) {
						if(Integer.valueOf(afterSaleReasons)==1) {
							orderRefundDoc.setAfterSaleReasons("多拍/错拍/不想要");
						}
						if(Integer.valueOf(afterSaleReasons)==2) {
							orderRefundDoc.setAfterSaleReasons("未按约定时间发货");
						}
                    	if(Integer.valueOf(afterSaleReasons)==3) {
							orderRefundDoc.setAfterSaleReasons("七天无理由换货");
						}
						if(Integer.valueOf(afterSaleReasons)==4) {
							orderRefundDoc.setAfterSaleReasons("尺码拍错/不喜欢/效果差");
						}
						if(Integer.valueOf(afterSaleReasons)==5) {
							orderRefundDoc.setAfterSaleReasons("质量问题");
						}
						if(Integer.valueOf(afterSaleReasons)==6) {
							orderRefundDoc.setAfterSaleReasons("颜色/款式/外貌与商品描述不符");
						}
						if(Integer.valueOf(afterSaleReasons)==7) {
							orderRefundDoc.setAfterSaleReasons("买家发错货");
						}
                    	if(Integer.valueOf(afterSaleReasons)==8) {
							orderRefundDoc.setAfterSaleReasons("颜色/款式/外貌与商品描述不符");
						}
						if(Integer.valueOf(afterSaleReasons)==9) {
							orderRefundDoc.setAfterSaleReasons("卖家发错货");
						}
						if(Integer.valueOf(afterSaleReasons)==10) {
							orderRefundDoc.setAfterSaleReasons("假冒品牌");
						}
						if(Integer.valueOf(afterSaleReasons)==11) {
							orderRefundDoc.setAfterSaleReasons("收到商品少件/破损等");
						}
						if(Integer.valueOf(afterSaleReasons)==12) {
							orderRefundDoc.setAfterSaleReasons("7天无理由退款退货");
						}
				}
				orderRefundDoc.setOrderSkuId(Integer.parseInt(orderSkuId));
				orderRefundDoc.setmId(member.getId());

				int row = 0;
				Date d2 = new Date(); // 结束时间（大时间）
				OrderRefundDoc d = new OrderRefundDoc();
				d = orderRefundService.selectOrderRefundByOrderSkuId(Integer.parseInt(orderSkuId));
				if (d == null) {
					if (orderShop.getmId().equals(member.getId())) {
						// orderShop的status商家订单状态：1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除
						if (Integer.parseInt(reason) ==1) {//退款=1
							// 如果状态是2的话，直接退款,否则需要判断时间
							row = orderRefundService.refundgoods(orderRefundDoc);
						} 
						else if (Integer.parseInt(reason) ==2) {// 5待评价 退货=2
							Date d1 = new Date();
							if (orderShop.getReceivedtime() != null) {
								//如果商品是待评价，则时间取收货时间
								d1 = orderShop.getReceivedtime();
								String t1 = sdf.format(d1);
								d1 = sdf1.parse(t1);
							} else {
								d1 = new Date();
								String t1 = sdf.format(d1);
								d1 = sdf1.parse(t1);
							}

							int time = daysBetween(d1, d2);
							// -1不限时间退货 0不接受退货 其它表示接受退货天数
							if (goods.getRefundDays() == 0) {
								row = 4;// 不支持退货
							} else if (goods.getRefundDays() == -1) {
								row = orderRefundService.refundgoods(orderRefundDoc);
							} else {
								if (time <= goods.getRefundDays()) {
									row = orderRefundService.refundgoods(orderRefundDoc);
								} else {
									row = 3;
								}
							}

						} else if (Integer.parseInt(reason) ==3) {// 7已评价 退款退货=3
							Date d1 = new Date();
							if(orderShop.getReceivedtime() !=null ){
								d1 = orderShop.getReceivedtime();// 开始时间（小时间）
							}else{
								d1 = new Date();
							}

							int time = daysBetween(d1, d2);
							// -1不限时间退货 0不接受退货 其它表示接受退货天数
							if (goods.getRefundDays() == 0) {
								row = 5;// 不支持退货
							} else if (goods.getRefundDays() == -1) {
								row = orderRefundService.refundgoods(orderRefundDoc);
							} else {
								if (time <= goods.getRefundDays()) {
									row = orderRefundService.refundgoods(orderRefundDoc);
								} else {
									row = 3;
								}
							}

						}else{
							row = 8;
						}
					} else {
						row = 6;
					}
				} else {
					
					//如果已存在
					orderRefundDoc.setId(d.getId());
					orderRefundDoc.setAddtime(new Date());
					orderRefundDoc.setStatus(0);
					orderRefundDoc.setDisposeTime(null);
					Integer sta = d.getStatus();
					//退款状态，0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中 8：换货成功 9：换货失败 10客服通过退款审核 11收货客服审核通过
					if ((sta ==1) || (sta ==3)||(sta ==6)||(sta ==9)) { //如果1和3
						if (orderShop.getmId().equals(member.getId())) {
							// orderShop的status商家订单状态：1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除
							if (Integer.parseInt(reason) ==1) {//退款=1
								//如果状态是2的话，直接退款,否则需要判断时间								
								row = orderRefundService.updateOrderRefundByParams(orderRefundDoc);
							} 
							else if (Integer.parseInt(reason) ==2 ) {// 5待评价 退货=2
								Date d1 = new Date();
								if (orderShop.getReceivedtime() != null) {
									d1 = orderShop.getReceivedtime();
									String t1 = sdf.format(d1);
									d1 = sdf1.parse(t1);
								} else {
									d1 = new Date();
									String t1 = sdf.format(d1);
									d1 = sdf1.parse(t1);
								}

								int time = daysBetween(d1, d2);
								// -1不限时间退货 0不接受退货 其它表示接受退货天数
								if (goods.getRefundDays() == 0) {
									row = 4;// 不支持退货
								} else if (goods.getRefundDays() == -1) {
									row = orderRefundService.updateOrderRefundByParams(orderRefundDoc);
								} else {
									if (time <= goods.getRefundDays()) {
										row = orderRefundService.updateOrderRefundByParams(orderRefundDoc);
									} else {
										row = 3;
									}
								}

							} else if (Integer.parseInt(reason) ==3) {// 7已评价 退款退货=3
								
								Date d1 = new Date();
								if(orderShop.getReceivedtime() != null){
									d1 =orderShop.getReceivedtime();// 开始时间（小时间）
								}

								int time = daysBetween(d1, d2);
								// -1不限时间退货 0不接受退货 其它表示接受退货天数
								if (goods.getRefundDays() == 0) {
									row = 5;// 不支持退货
								} else if (goods.getRefundDays() == -1) {
									row = orderRefundService.updateOrderRefundByParams(orderRefundDoc);
								} else {
									if (time <= goods.getRefundDays()) {
										row = orderRefundService.updateOrderRefundByParams(orderRefundDoc);
									} else {
										row = 3;
									}
								}

							}else{
								row = 8;
							}
							
						} else {
							row = 6;
						}
					}else{
						row = 7;
					}
					
				}

				if (row == 1) {
					bhResult = new BhResult(200, "操作成功", 1);
				} else if (row == 0) {
					bhResult = new BhResult(400, "操作失败", 0);
				} else if (row == 3) {
					bhResult = new BhResult(400, "已过了退款退货时间", 3);
				} else if (row == 4) {
					bhResult = new BhResult(400, "不支持换货", 4);
				} else if (row == 5) {
					bhResult = new BhResult(400, "不支持退款退货", 5);
				} else if (row == 6) {
					bhResult = new BhResult(400, "该商品不属于该该用户", 6);
				} else if (row ==7) {
					bhResult = new BhResult(400, "该商品已申请过售后服务，故不能再次添加申请", 7);
				} else if (row == 8) {
					bhResult = new BhResult(400, "该商品申请的售后服务类型不正确，故不能添加申请", 8);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######refundgoods#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * chengfengyun-2017-11-15星期三-移动端退款售后申请列表 退款
	 */
	@RequestMapping(value = "/refundlistbymove1")
	@ResponseBody
	public BhResult refundListByMove(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;
		try {
			String p = ""; // "page";
			String r = ""; // "size";
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
			// 获得参数
			OrderShop orderShop = new OrderShop();
			orderShop.setmId(member.getId());
			orderShop.setStatus(Contants.shopStatu5);

			PageBean<OrderShop> pageBean = orderRefundService.selectAllOrderShopList(orderShop, page, rows);
			bhResult = new BhResult(200, "返回成功", pageBean);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######refundlistbymove1#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * chengfengyun-2017-11-15星期三-移动端退款售后申请列表 退款
	 */
	@RequestMapping(value = "/refundlistbymove")
	@ResponseBody
	public BhResult refundListByMove1(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;
		try {
			String p = ""; // "page";
			String r = ""; // "size";
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
			// 获得参数
			OrderShop orderShop = new OrderShop();
			orderShop.setmId(member.getId());
			orderShop.setStatus(Contants.shopStatu5);

			PageBean<OrderShop> pageBean = orderRefundService.selectAllOrderShopList1(orderShop, page, rows);
			bhResult = new BhResult(200, "返回成功", pageBean);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######refundlistbymove#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	@RequestMapping(value = "/refundlistbymove2")
	@ResponseBody
	public BhResult refundListByMove2(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;
		try {
			String p = ""; // "page";
			String r = ""; // "size";
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
			// 获得参数
			OrderSku orderSku = new OrderSku();
			orderSku.setGoodsId(member.getId());

			PageBean<OrderSku> pageBean = orderRefundService.selectAllOrderShopList2(orderSku, page, rows);
			bhResult = new BhResult(200, "返回成功", pageBean);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######refundlistbymove2#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	@RequestMapping(value = "/showrefundedlist")
	@ResponseBody
	public BhResult showrefundedlist(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;
		try {
			String p = map.get("page"); // "page";
			String r = map.get("row"); // "size";
			Integer page = 0;
			Integer rows = 0;

			String reason = map.get("reason");
			String shop_order_no = map.get("shop_order_no");
			String status = map.get("status");
			String addtime = map.get("addtime");
			String addtime1 = map.get("addtime1");

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
			// 获得参数
			OrderRefundDoc doc = new OrderRefundDoc();
			doc.setmId(member.getId());
			if (StringUtils.isNotEmpty(reason)) {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(reason);
				if (isNum.matches()) {
					switch (Integer.parseInt(reason)) {
					case 1:
						doc.setReason(RefundReasonEnum.REFUND_MENEY.getReason());
						break;
					case 2:
						doc.setReason(RefundReasonEnum.REFUND_GOODS.getReason());
						break;
					case 3:
						doc.setReason(RefundReasonEnum.REFUND_MENEY_GOODS.getReason());
						break;
					}
				} else {
					doc.setReason(reason);
				}
			}
			if (StringUtils.isNotEmpty(shop_order_no)) {
				doc.setmName(shop_order_no);
			}
			if (StringUtils.isNotEmpty(status)) {

				doc.setStatus(Integer.parseInt(status));
			}
			if (StringUtils.isNotEmpty(addtime)) {
				// doc.setAddtime(addtime);
			}
			if (StringUtils.isNotEmpty(addtime1)) {

			}

			PageBean<OrderRefundDoc> pageBean = orderRefundService.showrefundedlist(doc, page, rows);
			bhResult = new BhResult(200, "返回成功", pageBean);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showrefundedlist#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	// 订单详情
	@RequestMapping(value = "/backrefuund")
	@ResponseBody
	public BhResult backRefuund(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);

		BhResult bhResult = null;

		try {
			// 1:接收前台传参
			String orderId = map.get("id");
			OrderShop orderShop = new OrderShop();

			orderShop.setId(Integer.parseInt(orderId));
			orderShop.setmId(member.getId());

			OrderShop list = orderRefundService.selectOrderShopBySelectSingle(orderShop);
			if (list.getOrderSku() == null) {
				bhResult = new BhResult(400, "无需要售后的商品" + "", null);
			} else {
				bhResult = new BhResult(200, "查询成功", list);

			}

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("#######backrefuund#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

}
