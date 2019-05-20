package com.order.user.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.config.Contants;
import com.bh.goods.mapper.ActZoneGoodsMapper;
import com.bh.goods.mapper.BargainRecordMapper;
import com.bh.goods.mapper.CashDepositMapper;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.HollandDauctionLogMapper;
import com.bh.goods.mapper.TopicDauctionPriceMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicSavemoneyLogMapper;
import com.bh.goods.pojo.ActZoneGoods;
import com.bh.goods.pojo.BargainRecord;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.goods.pojo.TopicDauctionPrice;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderExpressTypeMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderPaymentMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSeedMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.BHSeed;
import com.bh.order.pojo.CleanAccount;
import com.bh.order.pojo.MyOrder;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderCollectionDoc;
import com.bh.order.pojo.OrderExpressType;
import com.bh.order.pojo.OrderInfoPojo;
import com.bh.order.pojo.OrderPayment;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSimple;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.user.mapper.MemberBalanceLogMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.SeedModelMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBalanceLog;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.StatusNameUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.order.user.controller.IDUtils;
import com.order.user.service.JDOrderService;
import com.order.user.service.SimpleOrderService;
import com.order.user.service.UserOrderService;
import com.order.user.service.UserSubmitOrderService;
import com.wechat.service.WechatTemplateMsgService;

@Service
public class UserOrderServiceImpl implements UserOrderService {
	private static final Logger logger = LoggerFactory.getLogger(UserOrderServiceImpl.class);
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private MemberShopMapper memberShopMember;

	@Autowired
	private GoodsCartMapper goodsCartMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderSkuMapper orderSkuMapper;

	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;

	@Autowired
	private MemberShopMapper memberShopMapper;

	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	@Autowired
	private OrderExpressTypeMapper orderExpressTypeMapper;

	@Autowired
	private OrderPaymentMapper orderPaymentMapper;

	@Autowired
	private OrderShopMapper orderShopMapper;

	@Autowired
	private OrderTeamMapper orderTeamMapper;

	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private TopicSavemoneyLogMapper topicSavemoneyLogMapper;
	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private MemberBalanceLogMapper memberBalanceLogMapper;
	@Autowired
	private SimpleOrderService simpleOrderService;
	@Autowired
	private TopicDauctionPriceMapper topicDauctionPriceMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	@Autowired
	private HollandDauctionLogMapper hollandDauctionLogMapper;
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	@Autowired
	private HollandDauctionLogMapper logMapper;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private UserSubmitOrderService userSubmitOrderService;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Autowired
	private BargainRecordMapper bargainRecordMapper;
	@Autowired
	private ActZoneGoodsMapper actZoneGoodsMapper;

	public Goods selectBygoodsId(Integer id) throws Exception {
		Goods goods = goodsMapper.selectByPrimaryKey(id);
		return goods;
	}

	// 插入购物车goodsCart
	public GoodsCart insertGoodsCart(GoodsCart goodsCart) throws Exception {
		MemberShop memberShop = memberShopMember.selectByPrimaryKey(goodsCart.getShopId());
		goodsCart.setShopName(memberShop.getShopName());

		goodsCartMapper.insertSelective(goodsCart);
		GoodsCart goodsCart2 = goodsCartMapper.selectByPrimaryKey(goodsCart.getId());
		return goodsCart2;
	}

	// 更新购物车
	@Transactional
	public GoodsCart updateByPrimaryKeySelective(GoodsCart goodsCart) throws Exception {

		goodsCartMapper.updateByPrimaryKeySelective(goodsCart);
		GoodsCart cart = goodsCartMapper.selectByPrimaryKey(goodsCart.getId());
		return cart;
	}

	public GoodsCart selectCoodsCartByPrimaryKey(Integer id) throws Exception {
		GoodsCart cart = goodsCartMapper.selectByPrimaryKey(id);
		return cart;
	}

	// 删除购物车
	@Transactional
	public List<GoodsCart> deleteCoodsCartByPrimaryKey(GoodsCart cart) throws Exception {
		goodsCartMapper.deleteByPrimaryKey(cart.getId());
		List<GoodsCart> list = null;
		list = goodsCartMapper.selectCoodsCartByUserId(cart);
		return list;
	}

	/*****************************************
	 * 2017-9-18
	 *****************************************************************************************************************/
	// 通过ids查询商品
	public List<Goods> selectById(String id) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		// goodsCartMapper.selectCoodsCartByIds(list);
		List<Goods> goods = goodsMapper.selectByPrimaryKeys(list);
		return goods;
	}

	// 通过id查询GoodsSku
	public GoodsSku selectGoodsSkuByGoodsId(String gskuId) throws Exception {
		GoodsSku list = null;
		list = goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(gskuId));
		return list;
	}

	// 用户取消订单
	@Transactional
	public int updateOrderById(Order o, String note, String mId, String skuId) throws Exception {
		int row = 0;
		Order order = orderMapper.selectByPrimaryKey(o.getId());
		Order updateOrder = new Order();

		// 支付状态 :0货到付款,1待付款（未支付）,2已付款（已支付）,3待退款,4退款成功,5退款失败 ，默认是1待支付
		Integer paymentStatus = order.getPaymentStatus();
		if (paymentStatus == 1) {
			updateOrder.setId(o.getId());
			updateOrder.setStatus(8);// 订单状态
										// 1生成订单,2支付订单,3已经发货,4完成订单,5待发货6退款,7部分退款8用户取消订单,9作废订单,10退款中',
			row = orderMapper.updateByPrimaryKeySelective(updateOrder);
		}
		if (paymentStatus == 2) {
			OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(Integer.parseInt(skuId));
			Goods goods = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
			updateOrder.setId(o.getId());
			updateOrder.setStatus(8);// 订单状态
										// 1生成订单,2支付订单,3已经发货,4完成订单,5待发货6退款,7部分退款8用户取消订单,9作废订单,10退款中',

			row = orderMapper.updateByPrimaryKeySelective(updateOrder);

			OrderRefundDoc orderRefundDoc = new OrderRefundDoc();
			orderRefundDoc.setNote(note);// 退款理由
			orderRefundDoc.setAddtime(new Date());// 时间
			orderRefundDoc.setOrderId(orderSku.getId());// 订单id,Sku的id
			orderRefundDoc.setmId(Integer.parseInt(mId));// 用户ID
			orderRefundDoc.setGoodsId(orderSku.getGoodsId());// 要退款的商品
			orderRefundDoc.setSkuId(orderSku.getSkuId());
			orderRefundDoc.setShopId(goods.getShopId());
			orderRefundDoc.setAmount(orderSku.getSkuSellPriceReal());// 退款金额单位分
			orderRefundDoc.setStatus(0);// 退款状态，0:申请退款 1:退款失败 2:退款成功,默认0
			row = orderRefundDocMapper.insertSelective(orderRefundDoc);
		}
		return row;
	}

	// 取消订单退还滨惠豆和优惠券
	@Override
	public void updateBhdAndCoupon(OrderShop orderShop) {
		try {
			OrderShop orderShop1 = orderShopMapper.selectByPrimaryKey(orderShop.getId());
			if (orderShop1.getStatus() == 10 || orderShop1.getStatus() == 11 || orderShop1.getStatus() == 12) {
				MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShop1.getmId());
				int point = memberUser.getPoint();

				int savePrice = orderShop1.getSavePrice();
				int myPoint = point + savePrice;
				memberUser.setPoint(myPoint);
				memberUserMapper.updateByPrimaryKeySelective(memberUser);
				List<OrderSku> orderSkuList = orderSkuMapper.getSelectByOrderNo(orderShop.getId().toString());
				for (OrderSku orderSku : orderSkuList) {
					MemerScoreLog log = new MemerScoreLog();
					log.setCreateTime(new Date());
					log.setmId(memberUser.getmId());
					log.setIsDel(0);
					log.setSmId(-5);
					// ssrId如果是-1表示减去，如果是1表示增加
					log.setSsrId(1);
					log.setScore(savePrice);
					log.setOrderseedId(orderSku.getId());
					memerScoreLogMapper.insertSelective(log);
				}
				Order o = orderMapper.getOrderByOrderNo(orderShop1.getOrderNo());
				String[] conpon = o.getCouponsId().split(",");

				for (String conId : conpon) {
					CouponLog c = new CouponLog();
					c.setId(Integer.valueOf(conId));
					CouponLog couponLog = couponLogMapper.selectByPrimaryKey(c.getId());
					if (couponLog != null && couponLog.getStatus() == 1) {

						c.setStatus(0);
						couponLogMapper.updateByPrimaryKeySelective(c);
						Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
						coupon.setUsed(coupon.getUsed() - 1);
						couponMapper.updateByPrimaryKey(coupon);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateBhdAndCouponSecond(OrderShop orderShop) {
		// TODO Auto-generated method stub
		try {
			MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShop.getmId());
			int point = memberUser.getPoint();
			OrderShop orderShop1 = orderShopMapper.selectByPrimaryKey(orderShop.getId());
			int savePrice = orderShop1.getSavePrice();
			int myPoint = point + savePrice;
			memberUser.setPoint(myPoint);
			memberUserMapper.updateByPrimaryKeySelective(memberUser);
			List<OrderSku> orderSkuList = orderSkuMapper.getSelectByOrderNo(orderShop.getId().toString());
			for (OrderSku orderSku : orderSkuList) {
				MemerScoreLog log = new MemerScoreLog();
				log.setCreateTime(new Date());
				log.setmId(memberUser.getmId());
				log.setIsDel(0);
				log.setSmId(-5);
				// ssrId如果是-1表示减去，如果是1表示增加
				log.setSsrId(1);
				log.setScore(savePrice);
				log.setOrderseedId(orderSku.getId());
				memerScoreLogMapper.insertSelective(log);
			}
			CouponLog c = new CouponLog();
			c.setId(orderShop1.getCouponId());
			CouponLog couponLog = couponLogMapper.selectByPrimaryKey(c.getId());
			if (couponLog != null && couponLog.getStatus() == 1) {
				c.setStatus(0);
				couponLogMapper.updateByPrimaryKeySelective(c);
				Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
				coupon.setUsed(coupon.getUsed() - 1);
				couponMapper.updateByPrimaryKey(coupon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用户删除订单2017-11-1
	public int updateOrderStatus(OrderShop orderShop, String orderId) throws Exception {
		int row = 0;
		orderShop.setId(Integer.parseInt(orderId));
		row = orderShopMapper.updateOrderShopBySelect(orderShop);
		OrderShop orderShop2 = orderShopMapper.selectByPrimaryKey(orderShop.getId());

		// 2018-5-24:确认收货赠送滨惠豆给用户
		try {
			if (orderShop.getStatus() == 5) {
				// 程凤云 2018-4-11添加
				List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop2.getOrderNo());

				if (teamList.size() > 0) {
					WXMSgTemplate template = new WXMSgTemplate();
					template.setOrderShopId(orderShop2.getId() + "");
					wechatTemplateMsgService.receiGroupGoodsGoodTemplate(template);
				}
				SeedScoreRule se = new SeedScoreRule();
				se.setScoreAction(7);
				List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
				if (rule.size() > 0) {
					// 状态 0关 1开
					if (rule.get(0).getStatus().equals(1)) {
						// 积分:100元得几个豆,
						List<OrderSku> orderSkuList = orderSkuMapper
								.selectOrderSkuByOrderShopId1(Integer.valueOf(orderId));
						for (OrderSku orderSku : orderSkuList) {
							if (orderSku.getRefund() == 0) {
								MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShop2.getmId());
								MemberUser memberUser2 = new MemberUser();
								memberUser2.setmId(orderShop2.getmId());
								/*
								 * double point = 0; double l = (double) orderSku.getSkuSellPriceReal()/ 100;
								 * point = Math.floor(l);
								 */
								// int point=0;
								int point = ((orderSku.getSkuSellPriceReal() - orderSku.getSavePrice()))*orderSku.getSkuNum() / 100;
								Integer Poine = point + memberUser.getPoint();
								memberUser2.setPoint(Poine);
								memberUserMapper.updatePointBymId(memberUser2);

								MemerScoreLog log = new MemerScoreLog();
								log.setCreateTime(new Date());
								log.setmId(orderShop2.getmId());
								log.setIsDel(0);
								log.setSmId(-4);
								log.setSsrId(1);
								log.setScore((int) point);
								log.setOrderseedId(orderShop2.getId());
								memerScoreLogMapper.insertSelective(log);
							}

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	/******
	 * 2017-9-25
	 *****************************************************************************************************************/
	public List<GoodsCart> selectCoodsCartByPrimaryKeyAndmId(GoodsCart cart) throws Exception {

		List<GoodsCart> list = null;
		list = goodsCartMapper.selectCoodsCartByUserId(cart);
		return list;
	}

	// 通过goodId查找
	public MemberShop selectMemberShopByGoodId(Integer goodId) throws Exception {
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(goodId);
		return memberShop;
	}

	// q
	public GoodsCart selectCoodsCartByGoodsId(GoodsCart goodsCart) {
		GoodsCart goodsCart2 = null;
		goodsCartMapper.selectCoodsCartByUserId(goodsCart2);
		return goodsCart2;

	}

	// 9月26 根据用户的mId查询购物车
	public List<GoodsCart> selectCoodsCartByUserId(GoodsCart goodsCart) throws Exception {
		List<GoodsCart> list = null;
		list = goodsCartMapper.selectCoodsCartByUserId(goodsCart);
		return list;
	}

	/******
	 * 2017-9-27
	 ********************************************************************/
	// mId不为null， 添加如果gId相同
	public int updateGoodsCartBymIdAndgoodId(GoodsCart goodsCart) throws Exception {
		int row = 0;
		row = goodsCartMapper.updateGoodsCartBymIdAndgoodId(goodsCart);
		return row;
	}

	// 9月27 根据用户的mId和商品的gId插入
	public int insertGoodCartByselect(GoodsCart goodsCart) throws Exception {
		int row = 0;
		row = goodsCartMapper.insertSelective(goodsCart);
		return row;
	}

	/*****************************************
	 * * 2017-9-28
	 ****************************************/
	// 批量插入goodCart 2017-9-28
	public int insertGoodCartByBatch(List<GoodsCart> goodsCarts) throws Exception {
		int row = 0;
		row = goodsCartMapper.insertGoodCartByBatch(goodsCarts);
		return row;
	}

	// 分页显示购物车201-9-28
	public PageBean<GoodsCart> getCartByPage(GoodsCart goodsCart, Integer page, Integer rows) throws Exception {
		List<GoodsCart> goodsCartsList = null;

		PageHelper.startPage(page, rows, true);

		PageInfo<GoodsCart> info = new PageInfo<>(goodsCartsList);
		PageBean<GoodsCart> pageBean = null;
		pageBean = new PageBean<>(goodsCartsList);
		pageBean.setTotal(info.getTotal());
		pageBean.setList(info.getList());
		// 返回pageBean
		return pageBean;
	}

	/*****************************************
	 * * 2017-9-29
	 **************************************************/
	// 更新购物车 2017-9-29
	@Transactional
	public int updateGoodsCartByPrimaryKey(GoodsCart goodsCart) throws Exception {
		int row = 0;
		row = goodsCartMapper.updateByPrimaryKeySelective(goodsCart);
		return row;
	}

	/** 根据的用户的id 和is_del=0 选择有多少个 */
	public int selectCountCartsByisDel(Integer mId) throws Exception {
		GoodsCart goodsCart = new GoodsCart();
		goodsCart.setmId(mId);
		goodsCart.setIsDel(0);
		List<GoodsCart> list = goodsCartMapper.selectCoodsCartByUserId(goodsCart);
		return list.size();
	}

	/****************************
	 * 2017-9-30
	 **********************************************************************************/
	/** 根据用户选择默认的地址 2017-9-30 ****/
	public MemberUserAddress selecUseraddresstBySelect(MemberUserAddress address) throws Exception {
		MemberUserAddress addre = null;
		addre = memberUserAddressMapper.selecUseraddresstBySelect(address);
		return addre;
	}

	/** 在购物车列表中点击‘去结算’ */
	public CleanAccount selecOnePreOrderInfo(String goodsCartIds, String mId, String fz, String teamNo)
			throws Exception {
		CleanAccount cleanAccount = new CleanAccount();

		List<MemberUserAddress> addre = new ArrayList<>();

		try {

			MemberUserAddress address = new MemberUserAddress();
			address.setmId(Integer.parseInt(mId));
			addre = memberUserAddressMapper.selectAllBymId(Integer.parseInt(mId));// 根据mId查找地址
			cleanAccount.setOrderCartIds(goodsCartIds);
			List<String> goodsCartId = JsonUtils.stringToList(goodsCartIds);
			List<GoodsCart> goodsCartsList = goodsCartMapper.selectCoodsCartByIdsandmId(goodsCartId);
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(goodsCartsList.get(0).getGskuid());
			// 前端取的是list里面的价格与邮费
			List<GoodsCartListShopIdList> list = getGoodsCart(goodsCartsList, fz, teamNo);
			List<OrderExpressType> orderExpressType = orderExpressTypeMapper.selectAllOrderExpressType();// 配送类型
			List<OrderPayment> orderPayments = orderPaymentMapper.selectAllOrderPayment();// 支付类型

			if ((goodsCartsList.size() == 1) && (goodsSku != null)) {
				// 商品的金额，不包含邮费
				double count = 0;
				// 商品的购买数量
				int num = goodsCartsList.get(0).getNum();
				// 商品的邮费
				double deliPrice = (double) goodsSku.getDeliveryPrice() / 100;
				if ((StringUtils.isNotEmpty(fz)) && (Integer.parseInt(fz) >= 1)) {
					// 5拍卖
					if (Integer.parseInt(fz) == 5) {
						double teamPrice = (double) returnDauPrice(goodsSku.getGoodsId()) / 100;
						count = count + teamPrice * num;
					} else {
						double teamPrice = (double) goodsSku.getTeamPrice() / 100;
						count = count + teamPrice * num;
					}
				} else {
					double sellprice1 = (double) goodsSku.getSellPrice() / 100;
					count = count + sellprice1 * num;
				}

				List<Goods> goodsList = goodsMapper.selectGoodsByCart(goodsCartId, Integer.parseInt(mId));

				// 存放数据的集合
				List<CouponLog> listConponLog2 = new ArrayList<CouponLog>();

				// 获取当前可以使用的优惠劵zlk
				CouponLog conponLog = new CouponLog();
				conponLog.setmId(Integer.parseInt(mId)); // 使用者id
				conponLog.setExpireTime(new Date());// 当前时间
				conponLog.setShopId(goodsList.get(0).getShopId());
				if (goodsList.get(0).getShopId() != 0 && goodsList.get(0).getShopId() != 1) {
					// 获取商家的优惠劵
					List<CouponLog> listConponLog = couponLogMapper.goodListOrderByAmount(conponLog);

					if (listConponLog.size() > 0) {

						for (int i = 0; i < listConponLog.size(); i++) {
							// 判断当前的商品的价格是否大于 或者 等于优惠劵的使用要求金额
							if (count * 100 > listConponLog.get(i).getNeed_amount()
									|| count * 100 == listConponLog.get(i).getNeed_amount()) {
								// 转换金额
								if (listConponLog.get(i).getStock() != null
										&& listConponLog.get(i).getSended() != null) {
									double stock = listConponLog.get(i).getStock();
									double sended = listConponLog.get(i).getSended();
									double num2 = stock / sended;
									listConponLog.get(i).setPercentage(num2 * 100 + "%");// 剩余百分比

									double needAmount = listConponLog.get(i).getNeed_amount();// 把分转成元
																								// ，满多少才能消费
									double needAmount2 = needAmount / 100;
									listConponLog.get(i).setNeedAmount(needAmount2 + "");

									double amount = listConponLog.get(i).getAmount();// 把分转成元
																						// ，优惠金额
									listConponLog.get(i).setAmount2(amount / 100 + "");
								}
								listConponLog2.add(listConponLog.get(i));// 把订单金额大于优惠劵所需金额
																			// 的
																			// 优惠劵信息添加进去
							}
						}
					}
				}
				List<CouponLog> listConponLog3 = couponLogMapper.getAllListOrderByAmount(conponLog);
				// 滨惠商城平台的添加进去
				if (listConponLog3.size() > 0) {
					for (int j = 0; j < listConponLog3.size(); j++) {
						// 判断当前的商品的价格是否大于 或者 等于优惠劵的使用要求金额
						if (count * 100 > listConponLog3.get(j).getNeed_amount()
								|| count * 100 == listConponLog3.get(j).getNeed_amount()) {
							// 转换金额
							if (listConponLog3.get(j).getStock() != null && listConponLog3.get(j).getSended() != null) {
								double stock = listConponLog3.get(j).getStock();
								double sended = listConponLog3.get(j).getSended();
								double num3 = stock / sended;
								listConponLog3.get(j).setPercentage(num3 * 100 + "%");// 剩余百分比

								double needAmount = listConponLog3.get(j).getNeed_amount();// 把分转成元
																							// ，满多少才能消费
								double needAmount2 = needAmount / 100;
								listConponLog3.get(j).setNeedAmount(needAmount2 + "");

								double amount = listConponLog3.get(j).getAmount();// 把分转成元
																					// ，优惠金额
								listConponLog3.get(j).setAmount2(amount / 100 + "");
							}
							listConponLog2.add(listConponLog3.get(j));// 把订单金额大于优惠劵所需金额
																		// 的
																		// 优惠劵信息添加进去
						}
					}
				}

				cleanAccount.setCouponLogList(listConponLog2);

				// isCart:0从购物车过来，1从订单过来
				BHSeed bhSeed = simpleOrderService.getBhSeed(goodsCartIds, mId, fz, 0);
				cleanAccount.setPrice(count);
				cleanAccount.setDeliveryPrice(deliPrice);
				cleanAccount.setCartIds(goodsCartIds);
				cleanAccount.setGoodsCartsList(list);
				cleanAccount.setGoodsNum(num);
				cleanAccount.setTotalCount(RegExpValidatorUtils.formatdouble(deliPrice + count));
				cleanAccount.setUserAddressesList(addre);
				cleanAccount.setOrderExpressType(orderExpressType);
				cleanAccount.setOrderPayment(orderPayments);
				cleanAccount.setBhSeed(bhSeed);
			} else {
				cleanAccount = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cleanAccount;
	}

	/***** 2017-10-9星期一 根据购物车cart的id批量更新 *****/
	public int updateGoodsCartByPrimaryKey(List<String> id) throws Exception {
		int row = 0;
		row = goodsCartMapper.updateGoodsCartByPrimaryKey(id);
		return row;
	}

	/***************************************************************************************
	 * 2017-10-10星期二 根据购物车cart的id批量更新
	 *****/
	public int updateGoodsCartByPrimaryKey2(Integer mId, List<String> id) throws Exception {
		int row = 0;
		row = goodsCartMapper.updateGoodsCartByPrimaryKey2(mId, id);
		return row;
	}

	public PageBean<GoodsCart> getPage(int page, int size, List<GoodsCart> goodsCartsList) throws Exception {
		Integer currentPages = page;// 当前第几页
		Integer pageSizes = size;// 每页显示几条

		List<GoodsCart> list = new ArrayList<>(goodsCartsList);

		int total = goodsCartsList.size();// 总条数
		int pages = total / pageSizes;// 总页数
		pages = total % pageSizes > 0 ? (pages + 1) : pages;
		int size1 = list.size() == pageSizes ? pageSizes : list.size();
		PageBean<GoodsCart> page1 = new PageBean<>(list);
		page1.setPageNum(currentPages);// 第几页
		page1.setList(list);// 结果集
		page1.setTotal(total);// 总记录数
		page1.setPages(pages);// 总页数
		page1.setPageSize(pageSizes);// 每页记录数
		page1.setSize(size1); // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性

		return page1;
	}

	// 2017-10-13
	public List<GoodsCart> selectGoodsCartShopIds(GoodsCart goodsCart) throws Exception {
		return goodsCartMapper.selectGoodsCartShopIds(goodsCart);
	}

	public List<GoodsCart> getGoodscart(GoodsCart g) throws Exception {
		return goodsCartMapper.selectCoodsCartByUserId(g);
	}

	// 通过Id找到它
	public GoodsSku getGoodsSkuById(Integer id) throws Exception {
		GoodsSku sku = new GoodsSku();
		sku = goodsSkuMapper.selectByPrimaryKey(id);
		return sku;
	}

	/****/
	public int updateGoodsCartByPrimaryKeyAndgId(Integer mId, Integer skuId, List<String> gId) throws Exception {
		int row = 0;
		try {

			row = goodsCartMapper.updateGoodsCartByPrimaryKeyAndgId(mId, skuId, gId);
			return row;
		} catch (Exception e) {
			System.out.println(e);
		}
		return row;
	}

	/******** 2017-10-23 ************/
	public int updateGoodsCartByPrimaryKeyAndgId1(List<GoodsCart> goodsCart) throws Exception {
		int row = 0;
		try {
			for (int i = 0; i < goodsCart.size(); i++) {
				row = goodsCartMapper.updateGoodsCartByPrimaryKeyAndgId1(goodsCart.get(i));
			}
			return row;
		} catch (Exception e) {
			System.out.println(e);
		}
		return row;
	}

	public List<GoodsCartListShopIdList> getGoodsCart(List<GoodsCart> cartList, String fz, String teamNo) {
		List<GoodsCartListShopIdList> list = removeDuplicate1(cartList, fz, teamNo);
		return list;

	}

	public List<GoodsCartListShopIdList> removeDuplicate1(List<GoodsCart> list, String fz, String teamNo) {
		List<GoodsCartListShopIdList> list2 = new ArrayList<>();
		try {
			GoodsCartListShopIdList oneList = new GoodsCartListShopIdList();
			if (list.size() == 1) {
				List<GoodsCart> g = new ArrayList<>();
				GoodsCart goodsCart = list.get(0);
				GoodsSku goodsSku = selectGoodsSkuByGoodsId(String.valueOf(list.get(0).getGskuid()));
				Goods good = selectBygoodsId(list.get(0).getgId());// 获取good
				MemberShop memberShop = selectMemberShopByGoodId(list.get(0).getShopId());// 获取shop
				if (memberShop == null) {
					memberShop = new MemberShop();
					memberShop.setmId(1);
					memberShop.setShopName("");
				}

				Object value = JsonUtils.stringToObject(goodsSku.getValue());
				goodsSku.setValueObj(value);

				// 2017-12-26,判断是京东商品还是滨惠商品
				// 是否是京东商品，0否，1是
				int isJDGoods = good.getIsJd();
				if (isJDGoods == 0) {
					// 滨惠是否有货：0有货，1无货
					int sto = goodsSku.getStoreNums() - goodsCart.getNum();
					if (sto < 0) {
						goodsCart.setIsStore(1);
						goodsCart.setStoreName(Contants.storeNumsNo);
					} else {
						goodsCart.setIsStore(0);
						goodsCart.setStoreName(Contants.storeNumsYes);
					}
				}
				// 如果京东商品,默认设置有货
				else if (isJDGoods == 1) {
					goodsCart.setIsStore(0);
					goodsCart.setStoreName(Contants.storeNumsYes);
				}
				double sellP = 0;
				if (StringUtils.isNotEmpty(fz)) {// 如果fz不为空，价格取拼单的价格
					oneList.setFz(Integer.parseInt(fz));
					sellP = (double) goodsSku.getTeamPrice() / 100;
					goodsCart.setSellPrice(goodsSku.getTeamPrice());
					if (Integer.parseInt(fz) == 5) {
						sellP = (double) returnDauPrice(good.getId()) / 100;
						goodsCart.setSellPrice(returnDauPrice(good.getId()));

					}
				} else {
					sellP = (double) goodsSku.getSellPrice() / 100;
					goodsCart.setSellPrice(goodsSku.getSellPrice());
				}
				goodsCart.setRealsellPrice(sellP);
				goodsCart.setShopName(memberShop.getShopName());
				// 设置图片
				org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
				org.json.JSONArray personList = jsonObj.getJSONArray("url");
				goodsCart.setgImage((String) personList.get(0));
				goodsCart.setAddtime(list.get(0).getAddtime());
				goodsCart.setGoodName(good.getName());
				goodsCart.setGoodsSkus(goodsSku);
				g.add(goodsCart);
				oneList.setGoodsCartLists(g);
				if (StringUtils.isNotEmpty(teamNo)) {
					oneList.setTeamNo(teamNo);
				}
				int num = list.get(0).getNum();
				double sellTotalPrice = num * sellP;
				double deliPrice = (double) goodsSku.getDeliveryPrice() / 100;
				// 商家的id
				oneList.setShopId(memberShop.getmId());
				// 商家的名称
				oneList.setShopName(memberShop.getShopName());
				// 价格
				oneList.setPrice(sellTotalPrice);
				// 购买的商品的数量
				oneList.setNum(num);
				// 邮费
				oneList.setDeliveryPrice(deliPrice);
				// 总价格
				oneList.setTotalPrice(RegExpValidatorUtils.formatdouble(sellTotalPrice + deliPrice));
				oneList.setTeamNo(teamNo);
				list2.add(oneList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list2;

	}

	// 插入orderSku
	@Transactional
	public int insertOrderSku1(GoodsCart goodsCart, Order orderId) throws Exception {
		int row = 0;

		try {

			Goods good = goodsMapper.selectByPrimaryKey(goodsCart.getgId());
			OrderSku orderSku = new OrderSku();
			// String gskuId = String.valueOf(list.get(i).getGskuid());
			GoodsSku goodsSkuLslist = new GoodsSku();

			goodsSkuLslist = selectGoodsSkuByGoodsId(String.valueOf(goodsCart.getGskuid()));

			orderSku.setOrderId(orderId.getId());// 订单id
			orderSku.setGoodsId(goodsCart.getgId());// 商品id
			orderSku.setGoodsName(good.getName());// 商品名称
			orderSku.setSkuImage(good.getImage());// 商品图片
			orderSku.setSkuNum(goodsCart.getNum());// 商品数量
			orderSku.setShopId(good.getShopId());// 商品所属店铺id
			orderSku.setIsRefund(0);// 是否退款0.为正常,1.退款中,2.退款完成

			orderSku.setIsSend(orderId.getDeliveryStatus() == 0 ? true : false);// 是否已发货
																				// 0、未发货;1、已发货',
			orderSku.setSkuMarketPrice(orderId.getSkuPrice());// 商品市场总价单位分
			orderSku.setSkuSellPriceReal(orderId.getSkuPriceReal());// 支付价格单位分

			// orderSku.setSkuMarketPrice(list.get(i).getSellPrice());// 市场价格单位分
			orderSku.setdState(0);// 配送状态0初始化，1待配送，2配送中，3已完成

			if (goodsSkuLslist == null) {
				orderSku.setSkuId(0);// skuid
				orderSku.setSkuNo("0");// sku编码
				orderSku.setSkuWeight(0);
				orderSku.setSkuValue("0");
			} else {
				orderSku.setSkuId(goodsSkuLslist.getId());// skuid
				orderSku.setSkuNo(goodsSkuLslist.getSkuNo());// sku编码
				orderSku.setSkuWeight(goodsSkuLslist.getWeight());// 商品重量
				orderSku.setSkuValue(goodsSkuLslist.getValue());// 规格属性数组
			}

			row = orderSkuMapper.insertSelective(orderSku);

			// row = orderSkuMapper.insertOrderSkuByBatch(orderSkusList);

		} catch (Exception e) {

		}
		return row;

	}

	/** ************ 2017-10-24 cheng ********/
	/** 2017-10-24根据是否删除、用户的id、商品的id、skuid的id去查找 **/
	public GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart) throws Exception {
		GoodsCart goodsCart2 = null;
		goodsCart2 = goodsCartMapper.selectGoodsCartBySelect(goodsCart);
		return goodsCart2;
	}

	/********* 2017-11-1星期三 **********/
	// 订单列表（全部订单--通过orderShop查询）
	public PageBean<OrderShop> selectAllOrderShopList(OrderShop orderShop, Integer page, Integer rows)
			throws Exception {
		try {
			List<OrderShop> orderShopList = new ArrayList<>();
			PageHelper.startPage(page, rows, true);
			if (orderShop.getStatus() == null) {
				orderShopList = orderShopMapper.selectOrderShopBySelect(orderShop);
			} else if (orderShop.getStatus() == 2) {// 1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除
				OrderShop orderShop2 = new OrderShop();
				orderShop2.setmId(orderShop.getmId());
				orderShop2.setStatus(2);
				orderShop2.setOrderNo(orderShop.getOrderNo());
				orderShop2.setGoodsName(orderShop.getGoodsName());
				orderShopList = orderShopMapper.selectOrderShopByStatus2(orderShop2);
			} else if (orderShop.getStatus() == 9) {
				orderShop.setStatus(null);
				// 待分享
				OrderShop orderSh = new OrderShop();
				orderSh.setmId(orderShop.getmId());
				orderShopList = orderShopMapper.selectOrderShopByOrderNo(orderSh);
			} else {
				orderShopList = orderShopMapper.selectOrderShopBySelect(orderShop);
			}

			for (int i = 0; i < orderShopList.size(); i++) {
				OrderShop orderShop2 = new OrderShop();
				orderShop2.setmId(orderShop.getmId());
				orderShop2.setOrderId(orderShopList.get(i).getOrderId());
				if (orderShop.getStatus() != null) {
					if (orderShop.getStatus() == 2) {
						orderShop2.setStatus(orderShopList.get(i).getStatus());
					} else {
						orderShop2.setStatus(orderShop.getStatus());
					}
				}
				// 判断是否拼单单-scj
				OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderShopList.get(i).getOrderNo());
				if (orderTeam != null) {
					orderShopList.get(i).setGroupOrder(true);
				} else {
					orderShopList.get(i).setGroupOrder(false);
				}
				// electOrderShopByOrderIds增加orderSku的关联查询
				List<OrderShop> orderShops = orderShopMapper.selectOrderShopByOrderIds(orderShop2);

				if (orderShopList.get(i).getStatus() == 9) {
					orderShopList.get(i).setStatus(2);// 将备货中(9)的订单状态改完 待发货（2）
														// // xieyc
				}

				for (int m = 0; m < orderShops.size(); m++) {
					MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.get(m).getShopId());
					// 获得商家名称
					if (memberShop != null) {
						orderShops.get(m).setShopName(memberShop.getShopName());
					}
					// 在orderNo是否在orderTeam中，如果在，则是团购表，需要在订单列表中展示
					Order order = orderMapper.selectByPrimaryKey(orderShops.get(m).getOrderId());
					List<OrderTeam> team = orderTeamMapper.selectOrderTeanByOrderNo(order.getOrderNo());
					orderShops.get(m).setWaitNum(0);
					if (team.size() > 0) {
						switch (team.get(0).getStatus()) {
						case -1:
							team.get(0).setStatusName("拼单失败");
							break;
						case 0:
							// 程凤云-2018-3-24
							int groupCount = orderTeamMapper.groupCount(team.get(0).getTeamNo());
							GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(team.get(0).getGoodsSkuId());
							if (goodsSku != null) {
								Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
								if (goods != null) {
									int num = goods.getTeamNum() - groupCount;
									orderShops.get(m).setWaitNum(num);
								}
							}
							// 结束

							team.get(0).setStatusName("拼单中");
							break;
						case 1:
							team.get(0).setStatusName("拼单成功");
							break;

						}
						orderShops.get(m).setTeamStatus(team.get(0).getStatus());
						orderShops.get(m).setTeamStatusName(team.get(0).getStatusName());
						// 团号
						orderShops.get(m).setFullName(team.get(0).getTeamNo());
					} else {
						orderShops.get(m).setTeamStatus(2);
						orderShops.get(m).setTeamStatusName("该单不是拼单单");
						orderShops.get(m).setWaitNum(0);
						orderShops.get(m).setFullName(null);
					}

					if (team.size() > 0) {
						if (team.get(0).getStatus() == 1) {
							orderShops.get(m).setMystatus("拼单成功");
						} else if (team.get(0).getStatus() == 0) {
							orderShops.get(m).setMystatus("拼单中");
						} else if (team.get(0).getStatus() == -1) {
							orderShops.get(m).setMystatus("拼单失败");
						} else {
							String name = StatusNameUtils.getMyStatus(orderShops.get(m).getStatus(), 1);
							orderShops.get(m).setMystatus(name);
						}
					} else {
						String name = StatusNameUtils.getMyStatus(orderShops.get(m).getStatus(), 1);
						orderShops.get(m).setMystatus(name);
					}
					Integer isRe = orderShops.get(m).getIsRefund();
					if (isRe == 2) {
						OrderRefundDoc doc = new OrderRefundDoc();
						doc.setmId(orderShop.getmId());
						doc.setOrderShopId(orderShops.get(m).getId());
						List<OrderRefundDoc> docList = orderRefundDocMapper.selectOrderRefundDoc(doc);
						if (docList.size() > 0) {
							orderShops.get(m).setMystatus("售后服务");
						} else {
							orderShops.get(m).setMystatus("售后服务");
						}
					}
					List<OrderSku> skuList = orderSkuMapper.selectIsRefund(orderShops.get(m).getId());
					OrderSku orderSku = new OrderSku();
					orderSku.setOrderId(orderShops.get(m).getOrderId());
					orderSku.setShopId(orderShops.get(m).getShopId());
					orderSku.setOrderShopId(orderShops.get(m).getId());
					// 该商家下面的所有商品的sku
					List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);
					if (skuList.size() > 0 && skuList.size() == orderSkuList.size()) {
						orderShops.get(m).setMystatus("已评价");
					}
					for (int n = 0; n < orderSkuList.size(); n++) {
						orderSkuList.get(n).setStatus(5);
						String value = orderSkuList.get(n).getSkuValue();
						Object object = JsonUtils.stringToObject(value);
						orderSkuList.get(n).setValueObj(object);
						// 商品的的价格
						double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						orderSkuList.get(n).setRealSellPrice(realSellPrice);

						OrderRefundDoc doc = new OrderRefundDoc();
						doc.setOrderSkuId(orderSkuList.get(n).getId());
						OrderRefundDoc doc2 = orderRefundDocMapper.selectByOrderSkuId(doc);
						int status = orderShops.get(m).getStatus();
						if (doc2 != null) {
							String name = StatusNameUtils.getRefundStatusName(doc2.getStatus(), doc2.getRefundType(),
									doc2.getExpressNo());
							orderSkuList.get(n).setMystatus(name);
							// 0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中
							// 8：换货成功 9：换货失败 10客服通过退款审核 11收货客服审核通过
							if (doc2.getStatus() == 1 || doc2.getStatus() == 3 || doc2.getStatus() == 6) {
								orderSkuList.get(n).setStatus(1);
							}
						} else {
							if (status == 2 || status == 5 || status == 7 || status == 9) {
								// isRefund '是否退款:0否，1部分退款，2全部退款',
								if (orderShops.get(m).getIsRefund() != 2) {
									orderSkuList.get(n).setStatus(1);
								}
							}
						}
						// status=5：待评价
						if (status == 5) {
							GoodsComment goodsComment = new GoodsComment();
							goodsComment.setOrderSkuId(orderSkuList.get(n).getId());
							List<GoodsComment> commentList = goodsCommentMapper.selectCommentsBySkuId(goodsComment);
							if (commentList.size() < 1) {
								orderSkuList.get(n).setIsShowCommentButton(1);
							}
						}
					}
					orderShops.get(m).setOrderSku(orderSkuList);
					double realgDe = (double) orderShops.get(m).getgDeliveryPrice() / 100;
					int pri = orderSkuMapper.getOrderAllPrice(orderShops.get(m).getId());
					double allPrice = (double) pri / 100;

					double realSavePrice = (double) orderShops.get(m).getSavePrice() / 100;
					int goodsTotalPrice = orderSkuMapper.selectGoodsTotalPrice(orderShops.get(m).getId());
					double totalproce = (double) goodsTotalPrice / 100;
					int goodsNum = orderSkuMapper.selectGoodsNum(orderShops.get(m).getId());
					orderShops.get(m).setRealgDeliveryPrice(realgDe);
					orderShops.get(m).setGoodsNumber(goodsNum);
					orderShops.get(m).setRealOrderPrice(RegExpValidatorUtils.formatdouble(totalproce));
					orderShops.get(m).setAllPrice(allPrice);
					orderShops.get(m).setRealSavePrice(realSavePrice);
				}
				orderShopList.get(i).setOrderShopList(orderShops);
				orderShopList.get(i).setShopOrderNo(null);

			}

			PageInfo<OrderShop> info = new PageInfo<>(orderShopList);

			PageBean<OrderShop> pageBean = null;

			pageBean = new PageBean<>(orderShopList);
			pageBean.setTotal(info.getTotal());
			pageBean.setList(info.getList());

			return pageBean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isTrue(int i) {

		return false;
	}

	// 2017-11-1 -订单详情
	@SuppressWarnings("unused")
	public OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception {
		try {
			OrderShop orderShop2 = new OrderShop();

			orderShop2.setId(orderShop.getId());
			OrderShop orderShops = new OrderShop();
			orderShops = orderShopMapper.selectByOrderShopId(orderShop2);

			OrderSku orderSku = new OrderSku();
			orderSku.setOrderId(orderShops.getOrderId());
			orderSku.setShopId(orderShops.getShopId());
			orderSku.setOrderShopId(orderShops.getId());
			Order order = new Order();
			order = orderMapper.selectByPrimaryKey(orderShops.getOrderId());
			orderShop.setCouponsPrice(MoneyUtil.fen2Yuan(order.getCouponsPrice().toString()));
			OrderSimple orderSimple = new OrderSimple();
			orderSimple.setFz(order.getFz());
			List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);
			double amount = orderShops.getCouponPrice();
			orderShops.setCouponsPrice(amount / 100 + "");
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.getShopId());
			orderShops.setShopName(memberShop.getShopName());
			if (orderShops.getStatus() == 9) {// 如果订单状态是备货中 ，改完待发货
				orderShops.setStatus(2);
			}
			if (orderShops.getIsRefund() != 2) {
				String name = StatusNameUtils.getMyStatus(orderShops.getStatus(), 1);
				orderShops.setMystatus(name);
			}
			List<OrderTeam> team = orderTeamMapper.selectOrderTeanByOrderNo(order.getOrderNo());
			if (team.size() > 0) {
				switch (team.get(0).getStatus()) {
				case -1:
					orderShops.setMystatus("拼单失败");
					break;
				case 0:
					orderShops.setMystatus("拼单中");
					team.get(0).setStatusName("拼单中");
					break;
				case 1:
					orderShops.setMystatus("拼单成功");
					team.get(0).setStatusName("拼单成功");
					break;
				}
			}
			Integer isRe = orderShops.getIsRefund();
			if (isRe == 2) {
				orderShops.setMystatus("售后服务");
			}
			List<OrderSku> skuList = orderSkuMapper.selectIsRefund(orderShops.getId());
			if (skuList.size() > 0 && skuList.size() == orderSkuList.size()) {
				orderShops.setMystatus("已评价");
			}
			for (int n = 0; n < orderSkuList.size(); n++) { // 对orderSku进行分解
				orderSkuList.get(n).setStatus(5);
				String value = orderSkuList.get(n).getSkuValue();
				Object object = JsonUtils.stringToObject(value);
				orderSkuList.get(n).setValueObj(object);// 将skuValue转为object
				double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
				orderSkuList.get(n).setRealSellPrice(realSellPrice);// 销售价格double=int/100

				OrderRefundDoc doc = new OrderRefundDoc();
				OrderRefundDoc doc2 = new OrderRefundDoc();
				doc.setOrderSkuId(orderSkuList.get(n).getId());
				doc2 = orderRefundDocMapper.selectByOrderSkuId(doc);
				int status = orderShops.getStatus();
				if (doc2 != null) {
					String name1 = StatusNameUtils.getRefundStatusName(doc2.getStatus(), doc2.getRefundType(),
							doc2.getExpressNo());
					orderSkuList.get(n).setMystatus(name1);
					// 0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中 8：换货成功
					// 9：换货失败 10客服通过退款审核 11收货客服审核通过
					if (doc2.getStatus() == 1 || doc2.getStatus() == 3 || doc2.getStatus() == 6
							|| doc2.getStatus() == 9) {
						orderSkuList.get(n).setStatus(1);
					}
					// 当退款售后状态为5,7时，前端显示物流按钮
					if (doc2.getStatus() == 5 || doc2.getStatus() == 7) {
						orderSkuList.get(n).setIsShowlogButton(1);
					}
				} else {
					if (status == 2 || status == 5 || status == 7 || status == 9) {
						// isRefund '是否退款:0否，1部分退款，2全部退款',
						if (orderShops.getIsRefund() != 2) {
							orderSkuList.get(n).setStatus(1);
						}
					}
				}
				// 拍卖商品不允许售后服务
				if (order.getFz() == 5) {
					orderSkuList.get(n).setStatus(5);
				}
				// 拼团中的商品不允许售后服务
				if (team.size() > 0) {
					if (team.get(0).getStatus() != 1) {
						orderSkuList.get(n).setStatus(5);
					}
				}
				// 兑换商品不允许售后服务
				if (order.getFz() == 6) {
					orderSkuList.get(n).setStatus(5);
				}
				// 是否显示去评论的按钮
				// status=5：待评价
				if (status == 5) {
					GoodsComment goodsComment = new GoodsComment();
					goodsComment.setOrderSkuId(orderSkuList.get(n).getId());
					List<GoodsComment> commentList = goodsCommentMapper.selectCommentsBySkuId(goodsComment);
					if (commentList.size() < 1) {
						orderSkuList.get(n).setIsShowCommentButton(1);
					}
				}
			}
			orderShops.setOrderSku(orderSkuList);

			double deductionPrice = 0;// 荷兰式抵扣
			if (order.getFz() == 5) {
				HollandDauctionLog hollandDauctionLog = logMapper.getLogByOrderNo(order.getOrderNo());// 拍卖记录
				if (hollandDauctionLog != null) {
					// 获取某个用户某个商品的某一期交纳的保证金是多少
					CashDeposit findCashDeposit = new CashDeposit();// 查询条件
					findCashDeposit.setmId(hollandDauctionLog.getmId());
					findCashDeposit.setGoodsId(hollandDauctionLog.getGoodsId());
					findCashDeposit.setCurrentPeriods(hollandDauctionLog.getCurrentPeriods());
					CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);
					int price = cashDeposit.getDepositPrice() - order.getOrderPrice();
					if (price <= 0) {
						deductionPrice = (double) cashDeposit.getDepositPrice() / 100;
					} else {
						deductionPrice = (double) order.getOrderPrice() / 100;
					}
				}
			}
			orderShops.setDeductionPrice(deductionPrice);// 抵扣金额

			double realgDe = (double) orderShops.getgDeliveryPrice() / 100;
			double realOrderPrice = (double) orderShops.getOrderPrice() / 100;
			double realSavePrice = (double) orderShops.getSavePrice() / 100;
			int goodsNum = orderSkuMapper.selectGoodsNum(orderShops.getId());
			int goodsTotalPrice = orderSkuMapper.selectGoodsTotalPrice(orderShops.getId());
			double totalproce = (double) goodsTotalPrice / 100;
			orderShops.setRealgDeliveryPrice(realgDe);
			orderShops.setAllPrice(RegExpValidatorUtils.formatdouble(totalproce));
			orderShops.setGoodsNumber(goodsNum);
			DecimalFormat df = new DecimalFormat("######0.00");
			orderShops.setRealOrderPrice(Double.parseDouble(df.format(realOrderPrice - deductionPrice)));
			orderShops.setRealSavePrice(realSavePrice);
			if (order.getOrderPrice() > 0) {
				orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
			} else {
				orderSimple.setPaymentIdName("4");
			}
			orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
			orderSimple.setId(order.getId());
			orderSimple.setPaymentId(order.getPaymentId());// 支付方式
			orderSimple.setPaymentStatus(order.getPaymentStatus());
			orderSimple.setAddtime(order.getAddtime());
			orderSimple.setOrderPrice(order.getOrderPrice());

			orderShops.setWaitTime("0");
			if ((orderShops.getStatus().equals(3)) && (orderShops.getdState().equals(4))) {
				String wait = RegExpValidatorUtils.getEndTime1(orderShops.getReceivedtime());
				if (wait != null) {
					if (wait.equals("00")) {
						OrderShop orderShop3 = new OrderShop();
						orderShop3.setId(orderShops.getId());
						orderShop3.setStatus(Contants.shopStatu5);
						orderShopMapper.updateByPrimaryKeySelective(orderShop3);
						// 程凤云 2018-4-11添加
						List<OrderTeam> teamList = orderTeamMapper
								.selectOrderTeanByOrderNoAndStatus(orderShops.getOrderNo());
						if (teamList.size() > 0) {
							WXMSgTemplate template = new WXMSgTemplate();
							template.setOrderShopId(orderShops.getId() + "");
							wechatTemplateMsgService.receiGroupGoodsGoodTemplate(template);
						}
						orderShops.setStatus(Contants.shopStatu5); // zlk把传过去的值状态改了
					} else {
						orderShops.setWaitTime(wait);
					}
				} else {
					OrderShop orderShop3 = new OrderShop();
					orderShop3.setId(orderShops.getId());
					orderShop3.setStatus(Contants.shopStatu5);
					orderShopMapper.updateByPrimaryKeySelective(orderShop3);
					// 程凤云 2018-4-11添加
					List<OrderTeam> teamList = orderTeamMapper
							.selectOrderTeanByOrderNoAndStatus(orderShops.getOrderNo());
					if (teamList.size() > 0) {
						WXMSgTemplate template = new WXMSgTemplate();
						template.setOrderShopId(orderShops.getId() + "");
						wechatTemplateMsgService.receiGroupGoodsGoodTemplate(template);
					}
					orderShops.setStatus(Contants.shopStatu5);// zlk把传过去的值状态改了
				}

			}

			double realOrderPrice1 = (double) order.getOrderPrice() / 100;
			double realGdel = (double) order.getDeliveryPrice() / 100;
			orderSimple.setG_deliveryPrice(realGdel);
			orderSimple.setRealOrderPrice(realOrderPrice1);
			/** 设置地址 */
			if (order.getmAddrId() != null) {
				// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
				MemberUserAddress memberUserAddress = new MemberUserAddress();
				memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
				orderSimple.setMemberUserAddress(memberUserAddress);
			} else {
				orderSimple.setMemberUserAddress(null);
			}
			orderShops.setOrderSimple(orderSimple);
			return orderShops;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		// return orderShopList;
	}

	/*************************
	 * 2017-11-2 星期二， 根据用户的id查询订单状态的数量
	 ****************************/
	public OrderInfoPojo totalShopOrderNum(OrderShop orderShop1) throws Exception {
		OrderInfoPojo orderInfoPojo = new OrderInfoPojo();

		// 1待付款（未支付）的数量
		OrderShop orderShop = new OrderShop();
		orderShop.setmId(orderShop1.getmId());
		orderShop.setStatus(1);
		List<OrderShop> pageBean1 = orderShopMapper.selectOrderShopBySelect(orderShop);
		if (pageBean1.size() > 0) {
			orderInfoPojo.setWatipayNumber(pageBean1.size());
		} else {
			orderInfoPojo.setWatipayNumber(0);
		}

		// 2待发货
		OrderShop orderShop2 = new OrderShop();
		orderShop2.setmId(orderShop1.getmId());
		orderShop2.setStatus(2);
		List<OrderShop> pageBean2 = orderShopMapper.selectOrderShopByStatus2(orderShop2);
		if (pageBean2.size() > 0) {
			orderInfoPojo.setWatimerchanNumber(pageBean2.size());
		} else {
			orderInfoPojo.setWatimerchanNumber(0);
		}

		// 3已发货，待收货
		OrderShop orderShop3 = new OrderShop();
		orderShop3.setmId(orderShop1.getmId());
		orderShop3.setStatus(Contants.shopStatu3);
		List<OrderShop> pageBean3 = orderShopMapper.selectOrderShopBySelect(orderShop3);
		if (pageBean3.size() > 0) {
			orderInfoPojo.setSendmerchanNumber(pageBean3.size());
		} else {
			orderInfoPojo.setSendmerchanNumber(0);
		}

		// 5待评价
		OrderShop orderShop4 = new OrderShop();
		orderShop4.setmId(orderShop1.getmId());
		orderShop4.setStatus(Contants.shopStatu5);
		List<OrderShop> pageBean4 = orderShopMapper.selectOrderShopBySelect(orderShop4);
		if (pageBean4.size() > 0) {
			orderInfoPojo.setWaticommentNumber(pageBean4.size());
		} else {
			orderInfoPojo.setWaticommentNumber(0);
		}

		// 是否退款:0否，1是
		OrderShop orderShop5 = new OrderShop();
		orderShop4.setmId(orderShop1.getmId());
		orderShop4.setIsRefund(1);
		List<OrderShop> page5ean4 = orderShopMapper.selectOrderShopByRefunt(orderShop5);
		if (page5ean4.size() > 0) {
			orderInfoPojo.setRefund(page5ean4.size());
		} else {
			orderInfoPojo.setRefund(0);
		}

		// 待分享的数量
		OrderShop orderShop6 = new OrderShop();
		orderShop6.setmId(orderShop1.getmId());
		List<OrderShop> page5ean5 = orderShopMapper.selectOrderShopByOrderNo(orderShop6);
		if (page5ean5.size() > 0) {
			orderInfoPojo.setShare(page5ean5.size());
		} else {
			orderInfoPojo.setShare(0);
		}

		return orderInfoPojo;
	}

	public int totalCartNum(Integer mId) throws Exception {
		int num = 0;
		num = goodsCartMapper.totalCartNum(mId);
		return num;
	}

	/*************************
	 * 2017-11-3 星期五， 订单取消后再次购买
	 ****************************/
	public CleanAccount orderCancleBuy(OrderShop orderShop, String fz) throws Exception {

		CleanAccount cleanAccount = new CleanAccount();

		List<OrderShop> orderShopList = new ArrayList<>();
		orderShopList = orderShopMapper.selectOrderShopByOrder(orderShop);// 获得一个order下面的orderShop
		int goodsNum = 0;
		double deliveryPrice = 0.0;
		double price1 = 0.0;
		Order order = orderMapper.selectByPrimaryKey(orderShop.getOrderId());
		cleanAccount.setOrderCartIds(order.getCartId());
		List<OrderExpressType> orderExpressType = orderExpressTypeMapper.selectAllOrderExpressType();// 配送类型
		List<OrderPayment> orderPayments = orderPaymentMapper.selectAllOrderPayment();// 支付类型

		List<MemberUserAddress> addre = new ArrayList<>();
		MemberUserAddress address = new MemberUserAddress();
		address.setmId(orderShop.getmId());
		address.setIsDefault(true);// DEFAULT '0' COMMENT '1为默认收货地址
		MemberUserAddress address2 = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());// 根据mId查找地址
		addre.add(address2);

		cleanAccount.setOrderExpressType(orderExpressType);
		cleanAccount.setOrderPayment(orderPayments);
		cleanAccount.setUserAddressesList(addre);
		cleanAccount.setCartIds(String.valueOf(orderShop.getOrderId()));
		double count = (double) order.getOrderPrice() / 100;
		cleanAccount.setTotalCount(count);
		List<GoodsCartListShopIdList> goodsCart1 = new ArrayList<>();
		for (int i = 0; i < orderShopList.size(); i++) {

			double totalPrice = (double) orderShopList.get(i).getOrderPrice() / 100;
			double gDe = (double) orderShopList.get(i).getgDeliveryPrice() / 100;

			GoodsCartListShopIdList goodsCartListShopIdList = new GoodsCartListShopIdList();
			OrderSku orderSku = new OrderSku();
			orderSku.setOrderId(orderShopList.get(i).getOrderId());
			orderSku.setShopId(orderShopList.get(i).getShopId());

			List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);

			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShopList.get(i).getShopId());
			goodsCartListShopIdList.setShopName(memberShop.getShopName());
			goodsCartListShopIdList.setDeliveryPrice(gDe);
			goodsCartListShopIdList.setTotalPrice(totalPrice);

			orderShopMapper.selectOrderShopByOrder(orderShop);
			double price = 0.0;
			int num = 0;
			List<GoodsCart> goodsCarts = new ArrayList<>();
			Set<String> set = new HashSet<>();
			for (int n = 0; n < orderSkuList.size(); n++) {
				set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));
				MemberShop memberShop1 = memberShopMapper.selectByPrimaryKey(orderSkuList.get(n).getShopId());
				Goods good = goodsMapper.selectByPrimaryKey(orderSkuList.get(n).getGoodsId());
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSkuList.get(n).getSkuId());
				GoodsCart goodsCart = new GoodsCart();
				double realsellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;

				// 2017-12-26,判断是京东商品还是滨惠商品
				// 是否是京东商品，0否，1是
				int isJDGoods = good.getIsJd();
				if (isJDGoods == 0) {
					// 滨惠是否有货：0有货，1无货
					int sto = goodsSku.getStoreNums() - orderSkuList.get(n).getSkuNum();
					if (sto < 0) {
						goodsCart.setIsStore(1);
						goodsCart.setStoreName(Contants.storeNumsNo);
					} else {
						goodsCart.setIsStore(0);
						goodsCart.setStoreName(Contants.storeNumsYes);
					}
				}
				// 如果京东商品,默认设置有货
				else if (isJDGoods == 1) {
					goodsCart.setIsStore(0);
					goodsCart.setStoreName(Contants.storeNumsYes);
				}

				goodsCart.setId(null);
				goodsCart.setmId(orderShop.getmId());
				goodsCart.setShopId(orderSkuList.get(n).getShopId());
				goodsCart.setSellPrice(orderSkuList.get(n).getSkuSellPriceReal());
				goodsCart.setNum(orderSkuList.get(n).getSkuNum());

				org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
				org.json.JSONArray personList = jsonObj.getJSONArray("url");
				Object value = JsonUtils.stringToObject(goodsSku.getValue());
				goodsSku.setValueObj(value);
				goodsCart.setgImage((String) personList.get(0));

				goodsCart.setShopName(memberShop1.getShopName());
				goodsCart.setgId(orderSkuList.get(n).getGoodsId());
				goodsCart.setGoodName(good.getName());
				goodsCart.setRealsellPrice(realsellPrice);
				goodsCart.setGoodsSkus(goodsSku);
				goodsCart.setGskuid(orderSkuList.get(n).getSkuId());

				double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
				orderSkuList.get(n).setRealSellPrice(realSellPrice);
				Integer num1 = orderSkuList.get(n).getSkuNum();
				double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
				double sellPrice1 = sellPrice * num1;
				price = price + sellPrice1;
				num = num + num1;
				goodsCarts.add(goodsCart);
			}
			price1 = price1 + price;
			goodsCartListShopIdList.setGoodsCartLists(goodsCarts);
			goodsNum = goodsNum + num;
			goodsCartListShopIdList.setNum(num);

			OrderSimple orderSimple = new OrderSimple();
			orderSimple.setId(order.getId());
			orderSimple.setPaymentId(order.getPaymentId());// 支付方式
			orderSimple.setPaymentStatus(order.getPaymentStatus());
			orderSimple.setAddtime(order.getAddtime());
			orderSimple.setMemberUserAddress(null);
			orderSimple.setOrderPrice(order.getOrderPrice());
			double realOrderPrice = (double) order.getOrderPrice() / 100;
			double realGdel = (double) order.getDeliveryPrice() / 100;
			orderSimple.setG_deliveryPrice(realGdel);
			orderSimple.setRealOrderPrice(realOrderPrice);
			/** 设置地址 */
			if (order.getmAddrId() != null) {
				// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
				MemberUserAddress memberUserAddress = new MemberUserAddress();
				memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
				orderSimple.setMemberUserAddress(memberUserAddress);
			} else {
				orderSimple.setMemberUserAddress(null);
			}
			System.out.println("i--->" + i);
			goodsCart1.add(goodsCartListShopIdList);
		}
		// isCart:0从购物车过来，1从订单过来
		BHSeed bhSeed = simpleOrderService.getBhSeed(order.getCartId(), String.valueOf(order.getmId()),
				String.valueOf(order.getFz()), 1);
		cleanAccount.setBhSeed(bhSeed);
		cleanAccount.setPrice(price1);
		cleanAccount.setGoodsCartsList(goodsCart1);
		cleanAccount.setGoodsNum(goodsNum);
		deliveryPrice = (double) order.getDeliveryPrice() / 100;
		cleanAccount.setDeliveryPrice(deliveryPrice);
		// 当是从订单列表的待付款过来展示时,设置优惠劵为空数组[]

		// 存放数据的集合
		List<CouponLog> listConponLog2 = new ArrayList<CouponLog>();
		// 获取商家的优惠劵
		// 获取当前可以使用的优惠劵zlk
		CouponLog conponLog = new CouponLog();
		conponLog.setmId(orderShop.getmId()); // 使用者id
		conponLog.setExpireTime(new Date());// 当前时间
		conponLog.setShopId(order.getShopId());
		if (order.getShopId() != 0 && order.getShopId() != 1) {
			List<CouponLog> listConponLog = couponLogMapper.goodListOrderByAmount(conponLog);

			if (listConponLog.size() > 0) {

				for (int i = 0; i < listConponLog.size(); i++) {
					// 判断当前的商品的价格是否大于 或者 等于优惠劵的使用要求金额
					if (count * 100 > listConponLog.get(i).getNeed_amount()
							|| count * 100 == listConponLog.get(i).getNeed_amount()) {
						// 转换金额
						if (listConponLog.get(i).getStock() != null && listConponLog.get(i).getSended() != null) {
							double stock = listConponLog.get(i).getStock();
							double sended = listConponLog.get(i).getSended();
							double num2 = stock / sended;
							listConponLog.get(i).setPercentage(num2 * 100 + "%");// 剩余百分比

							double needAmount = listConponLog.get(i).getNeed_amount();// 把分转成元
																						// ，满多少才能消费
							double needAmount2 = needAmount / 100;
							listConponLog.get(i).setNeedAmount(needAmount2 + "");

							double amount = listConponLog.get(i).getAmount();// 把分转成元
																				// ，优惠金额
							listConponLog.get(i).setAmount2(amount / 100 + "");
						}
						listConponLog2.add(listConponLog.get(i));// 把订单金额大于优惠劵所需金额
																	// 的
																	// 优惠劵信息添加进去
					}
				}
			}
		}

		// 获取平台的优惠劵
		List<CouponLog> listConponLog3 = couponLogMapper.getAllListOrderByAmount(conponLog);

		if (listConponLog3.size() > 0) {
			for (int j = 0; j < listConponLog3.size(); j++) {
				// 判断当前的商品的价格是否大于 或者 等于优惠劵的使用要求金额
				if (count * 100 > listConponLog3.get(j).getNeed_amount()
						|| count * 100 == listConponLog3.get(j).getNeed_amount()) {
					// 转换金额
					if (listConponLog3.get(j).getStock() != null && listConponLog3.get(j).getSended() != null) {
						double stock = listConponLog3.get(j).getStock();
						double sended = listConponLog3.get(j).getSended();
						double num3 = stock / sended;
						listConponLog3.get(j).setPercentage(num3 * 100 + "%");// 剩余百分比

						double needAmount = listConponLog3.get(j).getNeed_amount();// 把分转成元
																					// ，满多少才能消费
						double needAmount2 = needAmount / 100;
						listConponLog3.get(j).setNeedAmount(needAmount2 + "");

						double amount = listConponLog3.get(j).getAmount();// 把分转成元
																			// ，优惠金额
						listConponLog3.get(j).setAmount2(amount / 100 + "");
					}
					listConponLog2.add(listConponLog3.get(j));// 把订单金额大于优惠劵所需金额
																// 的
																// 优惠劵信息添加进去
				}

			}
		}
		cleanAccount.setCouponLogList(listConponLog2);
		return cleanAccount;

	}

	/**************** 2017-11-06星期二 根据用户的mId查询他的所有地址cheng ****************/
	public List<MemberUserAddress> selectUserAllAddress(Integer mId) throws Exception {
		List<MemberUserAddress> addresseList = new ArrayList<>();
		addresseList = memberUserAddressMapper.selectAllBymId(mId);
		return addresseList;
	}

	// 2017-11-06星期二 根据地址主键查询地址cheng
	public MemberUserAddress selectUserAllAddressByPrimarykey(Integer id) throws Exception {
		MemberUserAddress addresseList = new MemberUserAddress();
		addresseList = memberUserAddressMapper.selectByPrimaryKey(id);
		return addresseList;
	}

	// 通过orderMain的id查询,2017-11-13
	public List<OrderShop> selectOrderMainById(OrderShop orderShop) throws Exception {

		try {
			List<OrderShop> orderShopList = new ArrayList<>();

			orderShopList = orderShopMapper.selectOrderShopByOrder(orderShop);
			logger.debug("selectOrderMainById size=" + orderShopList.size());
			for (int i = 0; i < orderShopList.size(); i++) {

				double realDeliveryPrice = 0.0;// 总邮费
				int deliveryPrice = 0;

				Set<String> set = new HashSet<>();

				OrderSku orderSku = new OrderSku();
				orderSku.setOrderId(orderShopList.get(i).getOrderId());
				orderSku.setShopId(orderShopList.get(i).getShopId());
				List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);// 该商家下面的所有商品的sku

				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShopList.get(i).getShopId());
				orderShopList.get(i).setShopName(memberShop.getShopName());// 获得商家名称
				orderShopList.get(i).setOrderSku(orderSkuList);

				// orderShops.get(m).setMystatus(String.valueOf(orderShops.get(m).getStatus()));
				switch (orderShopList.get(i).getStatus()) {
				case 1:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu1);
					break;
				case 2:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu2);
					break;
				case 3:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu3);
					break;
				case 4:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu4);
					break;
				case 5:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu5);
					break;
				case 6:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu6);
					break;
				case 7:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu7);
					break;
				case 8:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu8);
					break;
				default:
					orderShopList.get(i).setMystatus(Contants.orderShopStatu1);
				}

				double price = 0.0;
				int goodsNum = 0;
				for (int n = 0; n < orderSkuList.size(); n++) {
					set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));

					String value = orderSkuList.get(n).getSkuValue();
					Object object = JsonUtils.stringToObject(value);
					orderSkuList.get(n).setValueObj(object);
					double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					orderSkuList.get(n).setRealSellPrice(realSellPrice);
					double markPrice = (double) orderSkuList.get(n).getSkuMarketPrice() / 100;
					orderSkuList.get(n).setRealMarketPrice(markPrice);
					Integer num = orderSkuList.get(n).getSkuNum();
					double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					double sellPrice1 = sellPrice * num;
					goodsNum = goodsNum + num;
					price = price + sellPrice1;
				}
				double realgDe = (double) orderShopList.get(i).getgDeliveryPrice() / 100;
				double realOrderPrice = (double) orderShopList.get(i).getOrderPrice() / 100;
				double totalproce = realOrderPrice - realgDe;
				orderShopList.get(i).setRealgDeliveryPrice(realgDe);
				orderShopList.get(i).setGoodsNumber(goodsNum);
				orderShopList.get(i).setRealOrderPrice(totalproce);
				orderShopList.get(i).setAllPrice(realOrderPrice);
				// 根据id获得goods商品的集合
				List<String> myList = new ArrayList<>(set);
				List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(myList);
				for (int n = 0; n < goodsList.size(); n++) {
					int deliveryPrice1 = goodsList.get(n).getDeliveryPrice();
					double dePrice = (double) deliveryPrice1 / 100;
					realDeliveryPrice = realDeliveryPrice + dePrice;
					deliveryPrice = deliveryPrice + deliveryPrice1;
				}

				orderShopList.get(i).setDeliveryPrice(deliveryPrice);
				orderShopList.get(i).setRealDeliveryPrice(realDeliveryPrice);

				orderShopList.get(i).setShopOrderNo(null);

				Order order = orderMapper.selectByPrimaryKey(orderShopList.get(i).getOrderId());
				OrderSimple orderSimple = new OrderSimple();
				orderSimple.setId(order.getId());
				orderSimple.setPaymentId(order.getPaymentId());// 支付方式
				orderSimple.setPaymentStatus(order.getPaymentStatus());
				orderSimple.setAddtime(order.getAddtime());
				orderSimple.setOrderPrice(order.getOrderPrice());
				orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
				orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
				double realOrderPrice1 = (double) order.getOrderPrice() / 100;
				double realGdel = (double) order.getDeliveryPrice() / 100;
				orderSimple.setG_deliveryPrice(realGdel);
				orderSimple.setRealOrderPrice(realOrderPrice1);
				/** 设置地址 */
				if (order.getmAddrId() != null) {
					// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
					MemberUserAddress memberUserAddress = new MemberUserAddress();
					memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
					orderSimple.setMemberUserAddress(memberUserAddress);
				} else {
					orderSimple.setMemberUserAddress(null);
				}

				orderShopList.get(i).setOrderSimple(orderSimple);

			}

			return orderShopList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 通过order_main id查找order_main的记录 2017-11-17 ****/
	public Order selectOrderById(Order order) throws Exception {
		Order order2 = new Order();
		order2 = orderMapper.selectAllOrderByParams(order);
		return order2;
	}

	public Order selectOrderById1(Order order1) throws Exception {
		// 为了获得shopId
		Order allOrder = orderMapper.selectByPrimaryKey(order1.getId());
		// 2018-4-26修改订单号
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(allOrder.getShopId());
		// order的编号
		if (allOrder.getFz() != 5 && memberShop != null) {// 拍卖的订单 订单号也不修改
			if (StringUtils.isNotEmpty(memberShop.getBusiPayPre())) {
				order1.setOrderNo(IDUtils.getOrderNo(memberShop.getBusiPayPre()));
			} else {
				order1.setOrderNo(IDUtils.getOrderNo(null));
			}
		}
		orderMapper.updateByPrimaryKeySelective(order1);
		try {
			if (allOrder.getFz() != 5) {
				// 修改orderShop
				OrderShop orderShop = new OrderShop();
				orderShop.setOrderId(order1.getId());
				orderShop.setOrderNo(order1.getOrderNo());
				orderShopMapper.updateOrderNoByOrderId(orderShop);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 更新order的信息
		Order order = new Order();
		order = orderMapper.selectByPrimaryKey(order1.getId());
		order = userSubmitOrderService.getOrder1(order);
		return order;
	}

	/**
	 * author xxj 更新订单状态
	 * 
	 * @return
	 */
	@Transactional
	public void updateStatusByOrderNo(Order order) {

		// 更新库存
		updateStoreNums(order);
		// 更新订单状态
		orderMapper.updateStatusByOrderNo(order);
		OrderShop orderShop = new OrderShop();
		orderShop.setStatus(order.getStatus());
		orderShop.setOrderNo(order.getOrderNo());
		orderShopMapper.updateStatusByOrderNo(orderShop);

		try {
			// 更新滨惠豆
			// 如果saveMoney=-1的话该单不是使用滨惠豆抵扣的单，否则该单是使用豆抵扣的单且滨惠豆抵金额的钱saveMoney，单位分
			/*
			 * Map<String, Object> myMap = updateUserStore(order);
			 * 
			 * OrderCollectionDoc cDoc = new OrderCollectionDoc(); cDoc.setAddtime(new
			 * Date()); cDoc.setAmount(order.getOrderPrice()); cDoc.setmId(order.getmId());
			 * cDoc.setOrderId(order.getId()); cDoc.setPaymentId(order.getPaymentId()); if
			 * (myMap!=null) {//TODO if ("1".equals(myMap.get("isUseCoupon"))) {
			 * cDoc.setCouponAmount((Integer)myMap.get("couponPrice")); } if
			 * ("1".equals(myMap.get("isUseBean"))) {
			 * cDoc.setBeanAmount((Integer)myMap.get("beanPrice")); } }
			 * 
			 * 
			 * // 插入订单收款单 orderCollectionDocMapper.insert(cDoc); // 程凤云2018-3-24添加代码
			 * insMemberBalancdLog(cDoc);
			 */
			Order order2 = orderMapper.selectByPrimaryKey(order.getId());
			Integer fz = order2.getFz().intValue();
			// 是否是团购单，0不是，1普通拼团单，2秒杀，3抽奖，4惠省钱，5拍卖
			if (fz != 1) {
				OrderSku orderSku = new OrderSku();
				orderSku.setOrderId(order2.getId());
				List<OrderSku> list = orderSkuMapper.selectJdSupport(orderSku);
				if (list.size() > 0) {
					jdOrderService.updateJDOrderId(order2);
				}
			} else if (fz == 1) {
				// 团单
				List<OrderTeam> orderTeam = orderTeamMapper.selectOrderTeanByOrderNo(order2.getOrderNo());
				if (orderTeam.size() > 0) {
					// -1 拼单失败 0拼单中 1成功
					if (orderTeam.get(0).getStatus().intValue() == 1) {
						OrderSku orderSku = new OrderSku();
						orderSku.setOrderId(order2.getId());
						List<OrderSku> list = orderSkuMapper.selectJdSupport(orderSku);
						if (list.size() > 0) {
							jdOrderService.updateJDOrderId(order2);
						}
					}
				}
			}
			// 钱包支付zlk
			if (order.getPaymentId() == 7) {
				Order order3 = orderMapper.selectByPrimaryKey(order.getId());

				int realPrice = order3.getOrderPrice();// 实际付款金额
				if (order3.getFz() == 5) {// =5为荷兰式拍卖
					BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order3.getOrderNo());// 拍卖记录
					// 获取某个用户某个商品的某一期交纳的保证金是多少
					CashDeposit findCashDeposit = new CashDeposit();// 查询条件
					findCashDeposit.setmId(bargainRecord.getUserId());
					findCashDeposit.setGoodsId(bargainRecord.getGoodsId());
					findCashDeposit.setCurrentPeriods(bargainRecord.getCurrentPeriods());
					findCashDeposit.setIsrefund(1);
					CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);
					realPrice = realPrice - cashDeposit.getDepositPrice();
				}
				// 根据 用户id 获取 钱包信息
				List<Wallet> wallet = walletMapper.getWalletByUid(order3.getmId());
				wallet.get(0).setMoney(wallet.get(0).getMoney() - realPrice);// 原金额-实际付款金额
				walletMapper.updateByPrimaryKey(wallet.get(0));

				// 自己生成订单号
				IDUtils iDUtils = new IDUtils();
				WalletLog walletLog = new WalletLog();
				walletLog.setAddTime(new Date()); // 时间
				walletLog.setAmount(realPrice); // 金额
				walletLog.setmId(order3.getmId()); // 登录者id
				walletLog.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE)); // 订单号
				walletLog.setInOut(1); // 出账
				walletLog.setStatus(1); // 状态 ：成功
				walletLog.setRemark("下单");// 2018.7.10 zlk
				walletLog.setOrderId(order3.getId());
				walletLogMapper.insertSelective(walletLog); // 保存一条充值记录到WalletLog表
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 更改库存-scj
	public void updateStoreNums(Order order) {
		List<OrderSku> list = orderSkuMapper.getOrderGoodsList(order.getId());
		if (list.size() > 0) {
			for (OrderSku sku : list) {
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(sku.getSkuId());
				goodsSku.setStoreNums(goodsSku.getStoreNums() - sku.getSkuNum());
				goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
				// cheng
				Goods goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
				goods.setSale(goods.getSale() + sku.getSkuNum());
				goodsMapper.updateByPrimaryKeySelective(goods);
			}
		}
	}

	// 更新滨惠豆和优惠劵的信息 -cheng
	private Map<String, Object> updateUserStore(Order order) {
		Map<String, Object> map = new HashMap<>();
		try {
			Order order2 = orderMapper.selectByPrimaryKey(order.getId());
			List<OrderShop> myOrderShop = orderShopMapper.getByOrderId(order2.getId());
			// 获取优惠劵金额程凤云
			if (order2.getCouponsId() != null && !order2.getCouponsId().equals("0")) {
				if (myOrderShop.size() > 0) {
					Integer mainPrice = 0;
					Integer mainDeliveryPrice = 0;
					Integer couponPrice = 0;
					for (OrderShop orderShop : myOrderShop) {
						OrderShop myOShop = new OrderShop();
						myOShop.setId(orderShop.getId());
						myOShop.setgDeliveryPrice(orderShop.getgDeliveryPrice());
						// 程凤云 根据coupon的id 去获取当前的记录
						Integer amount = 0;
						Integer shopDeliveryPrice = orderShop.getgDeliveryPrice();
						Map<String, Object> myCouponMap = userSubmitOrderService.selectCouponMsg(orderShop.getShopId(),
								order2.getCouponsId());
						if (myCouponMap != null && myCouponMap.get("couponId") != null
								&& myCouponMap.get("couponType") != null) {
							// 优惠券类型，1普通券，2免邮券，3红包券(使用卷的金额：当1和3的时候金额都是取amount字段)
							// 抵扣的金额
							amount = (Integer) myCouponMap.get("amount");
							Integer type = (Integer) myCouponMap.get("couponType");
							int couponLogId = (int) myCouponMap.get("couponId");
							Long catId = (Long) myCouponMap.get("catId");
							myOShop.setCouponId(couponLogId);
							if (type == 1) {
								// 使用普通卷 该商家商品价格之和上减去普通卷抵扣金额（这种情况不可能出现<0）
								Integer price = orderShop.getOrderPrice() - amount;
								myOShop.setOrderPrice(price);
								// 当抵扣的金额出现0时
								if (price <= 0) {
									myOShop.setOrderPrice(0);
									amount = orderShop.getOrderPrice();
								}
							} else if (type == 2) {
								// 使用免邮券 ： 该商家订单的邮费全免
								Integer price = orderShop.getOrderPrice() - orderShop.getgDeliveryPrice();
								myOShop.setOrderPrice(price);
								amount = shopDeliveryPrice;
							} else if (type == 3) {
								// 使用红包卷 该商家商品价格之和上减去红包金额（<0的时候 让该商家的订单金额 为0）
								Integer price = orderShop.getOrderPrice() - amount;
								myOShop.setOrderPrice(price);
								// 当抵扣的金额出现0时
								if (price <= 0) {
									myOShop.setOrderPrice(0);
									amount = orderShop.getOrderPrice();
								}
							}
							// 将该优惠劵使用的信息同步更新到couponLog表
							CouponLog myLog = new CouponLog();
							myLog.setId(couponLogId);
							myLog.setOrderId(orderShop.getId());
							myLog.setStatus(1);
							myLog.setUseTime(new Date());
							// 同步更新order_shop表的优惠劵信息
							myOShop.setCouponPrice(amount);
							myOShop.setgDeliveryPrice(shopDeliveryPrice);
							couponLogMapper.updateByPrimaryKeySelective(myLog);

							CouponLog couponLog = couponLogMapper.selectByPrimaryKey(couponLogId);
							Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
							coupon.setUsed(coupon.getUsed() + 1);// 使用量+1
							couponMapper.updateByPrimaryKeySelective(coupon);// 同步更新coupon表的优惠劵信息

							// 同步更新order_shop表的优惠劵信息
							orderShopMapper.updateByPrimaryKeySelective(myOShop);
							// 如果该商家的该订单使用了优惠劵
							mainPrice = mainPrice + myOShop.getOrderPrice();
							mainDeliveryPrice = mainDeliveryPrice + myOShop.getgDeliveryPrice();
							couponPrice = couponPrice + amount;
							if (type != 2) {
								userSubmitOrderService.couponMony(type, catId, (Integer) myCouponMap.get("amount"),
										orderShop.getId(), orderShop.getShopId(), couponLogId,
										orderShop.getgDeliveryPrice(), myOShop.getOrderPrice());
							}
						} else {
							// 如果该商家的该订单未使用优惠劵
							mainPrice = mainPrice + orderShop.getOrderPrice();
							mainDeliveryPrice = mainDeliveryPrice + shopDeliveryPrice;
							couponPrice = couponPrice + amount;
						}
					}
					Order myOrder = new Order();
					myOrder.setId(order2.getId());
					myOrder.setOrderPrice(mainPrice);
					myOrder.setDeliveryPrice(mainDeliveryPrice);
					myOrder.setCouponsPrice(couponPrice);
					orderMapper.updateByPrimaryKeySelective(myOrder);
					map.put("isUseCoupon", "1");
					map.put("couponPrice", couponPrice);
				}
			}

			// 1调用使用滨惠豆下的单，0不使用
			Order order3 = orderMapper.selectByPrimaryKey(order2.getId());
			if (order3.getIsBeans().equals(1)) {
				if (order3.getOrderPrice() > 0) {
					List<OrderShop> myOShop = orderShopMapper.getByOrderId(order3.getId());
					MemberUser memberUser = memberUserMapper.selectByMId(myOShop.get(0).getId());
					int mypoint = memberUser.getPoint();
					if (myOShop.size() > 0) {
						int savePrice = 0;
						int orderPrice = 0;
						int needScoreCount = 0;
						double sellPriceCount = 0;
						for (OrderShop orderShop2 : myOShop) {
							List<OrderShop> orderShopList = orderShopMapper
									.selectByOrderShopId1(orderShop2.getOrderId());
							List<OrderSku> orderSkuList = orderSkuMapper
									.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
							for (OrderSku orderSku : orderSkuList) {
								int num = orderSku.getSkuNum();
								GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
								Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
								if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
									int a = goodsSku.getSellPrice();
									int b = Integer.valueOf(goods.getDeductibleRate());
									needScoreCount = needScoreCount + a * b / 100 * num;
									sellPriceCount = sellPriceCount + a;
								}
							}
						}
						if (mypoint < needScoreCount) {
							needScoreCount = mypoint;
							for (OrderShop orderShop2 : myOShop) {
								int score2 = 0;
								System.out.println("sql9" + orderShop2.getShopId());
								List<OrderShop> orderShopList = orderShopMapper
										.selectByOrderShopId1(orderShop2.getOrderId());
								System.out.println("sql9" + orderShopList.get(0).getOrderId());
								int needScore = 0;
								List<OrderSku> orderSkuList = orderSkuMapper
										.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
								for (OrderSku orderSku : orderSkuList) {
									int num = orderSku.getSkuNum();
									GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
									Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
									if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
										int a = goodsSku.getSellPrice();
										// 四舍五入滨惠豆
										needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
												.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + needScore;
									}
								}
								if (needScore > mypoint) {
									score2 = mypoint;
								} else {
									score2 = needScore;
								}
								OrderShop orderShop = new OrderShop();
								orderShop.setId(orderShop2.getId());
								orderShop.setOrderPrice(orderShop2.getOrderPrice() - score2);
								orderShop.setSavePrice(score2);
								if (score2 > orderShop2.getOrderPrice()) {
									orderShop.setOrderPrice(0);
									orderShop.setSavePrice(orderShop2.getOrderPrice());
								}
								savePrice = savePrice + orderShop.getSavePrice();
								System.out.println("sql9" + savePrice);
								orderPrice = orderPrice + orderShop.getOrderPrice();
								orderShopMapper.updateOrderPrice(orderShop);
								List<String> cartIds = JsonUtils.stringToList(order3.getCartId());
								List<MyOrder> myOrderList = orderSkuMapper.selectSkuScore(orderShop2.getId(), cartIds);
								// 多余的积分
								if (myOrderList.size() > 0 && (score2 > orderShop2.getOrderPrice())) {
									// 更新orderSku表
									int myScore = orderShop.getSavePrice();
									for (MyOrder myOrder1 : myOrderList) {
										// int
										// score1=myOrder1.getSellPrice()*Integer.valueOf(myOrder1.getDeductibleRate())/100*myOrder1.getSkuNum();
										int num = myOrder1.getSkuNum();
										GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(myOrder1.getSkuId());
										Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
										if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
											int a = goodsSku.getSellPrice();
											// 四舍五入滨惠豆
											needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
													.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
										}
										myScore = myScore - needScore;
										OrderSku myOrderSku = new OrderSku();
										myOrderSku.setId(myOrder1.getId());
										if (myScore <= 0) {
											myOrderSku.setSavePrice(orderShop.getSavePrice()
													- orderSkuMapper.selectSavePrice(orderShop2.getId()));
											if (myOrderSku.getSavePrice() > 0) {
												orderSkuMapper.updateScore(myOrderSku);
												// 将滨惠更改到log表里面

												if (mypoint > needScore) {
													myOrderSku.setSavePrice(needScore);
													mypoint = mypoint - needScore;
												} else {
													myOrderSku.setSavePrice(mypoint);
													mypoint = 0;
												}
												MemerScoreLog log = new MemerScoreLog();
												log.setCreateTime(new Date());
												log.setmId(orderShop2.getmId());
												log.setIsDel(0);
												log.setSmId(-1);
												// ssrId如果是-1表示减去，如果是1表示增加
												log.setSsrId(-1);
												log.setScore(myOrderSku.getSavePrice());
												log.setOrderseedId(myOrder1.getId());
												memerScoreLogMapper.insertSelective(log);
											}
											break;
										} else {
											myOrderSku.setSavePrice(needScore);
											orderSkuMapper.updateScore(myOrderSku);
											// 将滨惠更改到log表里面
											MemerScoreLog log = new MemerScoreLog();

											if (mypoint > needScore) {
												myOrderSku.setSavePrice(needScore);
												mypoint = mypoint - needScore;
											} else {
												myOrderSku.setSavePrice(mypoint);
												mypoint = 0;
											}
											log.setCreateTime(new Date());
											log.setmId(orderShop2.getmId());
											log.setIsDel(0);
											log.setSmId(-1);
											// ssrId如果是-1表示减去，如果是1表示增加
											log.setSsrId(-1);
											log.setScore(myOrderSku.getSavePrice());
											log.setOrderseedId(myOrder1.getId());
											memerScoreLogMapper.insertSelective(log);
										}
									}
								} else if (myOrderList.size() > 0 && (score2 <= orderShop2.getOrderPrice())) {
									// 更新orderSku表

									for (MyOrder myOrder1 : myOrderList) {
										OrderSku myOrderSku = new OrderSku();
										// int
										// score1=myOrder1.getSellPrice()*Integer.valueOf(myOrder1.getDeductibleRate())/100*myOrder1.getSkuNum();
										int num = myOrder1.getSkuNum();
										GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(myOrder1.getSkuId());
										Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
										if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
											int a = goodsSku.getSellPrice();
											// 四舍五入滨惠豆
											needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
													.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
										}
										if (mypoint > needScore) {
											myOrderSku.setSavePrice(needScore);
											mypoint = mypoint - needScore;
										} else {
											myOrderSku.setSavePrice(mypoint);
											mypoint = 0;
										}
										myOrderSku.setId(myOrder1.getId());
										orderSkuMapper.updateScore(myOrderSku);

										// 将滨惠更改到log表里面
										MemerScoreLog log = new MemerScoreLog();
										log.setCreateTime(new Date());
										log.setmId(orderShop2.getmId());
										log.setIsDel(0);
										log.setSmId(-1);
										// ssrId如果是-1表示减去，如果是1表示增加
										log.setSsrId(-1);
										log.setScore(myOrderSku.getSavePrice());
										log.setOrderseedId(myOrder1.getId());
										memerScoreLogMapper.insertSelective(log);
									}
								}

							}
						} else {
							for (OrderShop orderShop2 : myOShop) {
								int score2 = 0;
								System.out.println("sql9" + orderShop2.getShopId());
								List<OrderShop> orderShopList = orderShopMapper
										.selectByOrderShopId1(orderShop2.getOrderId());
								System.out.println("sql9" + orderShopList.get(0).getOrderId());
								int needScore = 0;
								List<OrderSku> orderSkuList = orderSkuMapper
										.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
								for (OrderSku orderSku : orderSkuList) {
									int num = orderSku.getSkuNum();
									GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
									Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
									int a = goodsSku.getSellPrice();
									int b = Integer.valueOf(goods.getDeductibleRate());
									needScore = needScore + a * b * num / 100;
								}
								if (needScore > mypoint) {
									score2 = mypoint;
								} else {
									score2 = needScore;
								}
								OrderShop orderShop = new OrderShop();
								orderShop.setId(orderShop2.getId());
								orderShop.setOrderPrice(orderShop2.getOrderPrice() - score2);
								orderShop.setSavePrice(score2);
								if (score2 > orderShop2.getOrderPrice()) {
									orderShop.setOrderPrice(0);
									orderShop.setSavePrice(orderShop2.getOrderPrice());
								}
								savePrice = savePrice + orderShop.getSavePrice();
								System.out.println("sql9" + savePrice);
								orderPrice = orderPrice + orderShop.getOrderPrice();
								orderShopMapper.updateOrderPrice(orderShop);
								List<String> cartIds = JsonUtils.stringToList(order3.getCartId());
								List<MyOrder> myOrderList = orderSkuMapper.selectSkuScore(orderShop2.getId(), cartIds);
								// 多余的积分
								if (myOrderList.size() > 0 && (score2 > orderShop2.getOrderPrice())) {
									// 更新orderSku表
									int myScore = orderShop.getSavePrice();
									for (MyOrder myOrder1 : myOrderList) {
										int score1 = myOrder1.getSellPrice()
												* Integer.valueOf(myOrder1.getDeductibleRate()) / 100
												* myOrder1.getSkuNum();
										myScore = myScore - score1;
										OrderSku myOrderSku = new OrderSku();
										myOrderSku.setId(myOrder1.getId());
										if (myScore <= 0) {
											myOrderSku.setSavePrice(orderShop.getSavePrice()
													- orderSkuMapper.selectSavePrice(orderShop2.getId()));
											if (myOrderSku.getSavePrice() > 0) {
												orderSkuMapper.updateScore(myOrderSku);
												// 将滨惠更改到log表里面

												if (mypoint > needScore) {
													myOrderSku.setSavePrice(score1);
													mypoint = mypoint - score1;
												} else {
													myOrderSku.setSavePrice(mypoint);
													mypoint = 0;
												}
												MemerScoreLog log = new MemerScoreLog();
												log.setCreateTime(new Date());
												log.setmId(orderShop2.getmId());
												log.setIsDel(0);
												log.setSmId(-1);
												// ssrId如果是-1表示减去，如果是1表示增加
												log.setSsrId(-1);
												log.setScore(myOrderSku.getSavePrice());
												log.setOrderseedId(myOrder1.getId());
												memerScoreLogMapper.insertSelective(log);
											}
											break;
										} else {
											myOrderSku.setSavePrice(score1);
											orderSkuMapper.updateScore(myOrderSku);
											// 将滨惠更改到log表里面
											MemerScoreLog log = new MemerScoreLog();

											if (mypoint > score1) {
												myOrderSku.setSavePrice(score1);
												mypoint = mypoint - score1;
											} else {
												myOrderSku.setSavePrice(mypoint);
												mypoint = 0;
											}
											log.setCreateTime(new Date());
											log.setmId(orderShop2.getmId());
											log.setIsDel(0);
											log.setSmId(-1);
											// ssrId如果是-1表示减去，如果是1表示增加
											log.setSsrId(-1);
											log.setScore(myOrderSku.getSavePrice());
											log.setOrderseedId(myOrder1.getId());
											memerScoreLogMapper.insertSelective(log);
										}
									}
								} else if (myOrderList.size() > 0 && (score2 <= orderShop2.getOrderPrice())) {
									// 更新orderSku表

									for (MyOrder myOrder1 : myOrderList) {
										OrderSku myOrderSku = new OrderSku();
										int score1 = myOrder1.getSellPrice()
												* Integer.valueOf(myOrder1.getDeductibleRate()) / 100
												* myOrder1.getSkuNum();
										if (mypoint > score1) {
											myOrderSku.setSavePrice(score1);
											mypoint = mypoint - score1;
										} else {
											myOrderSku.setSavePrice(mypoint);
											mypoint = 0;
										}
										myOrderSku.setId(myOrder1.getId());
										orderSkuMapper.updateScore(myOrderSku);

										// 将滨惠更改到log表里面
										MemerScoreLog log = new MemerScoreLog();
										log.setCreateTime(new Date());
										log.setmId(orderShop2.getmId());
										log.setIsDel(0);
										log.setSmId(-1);
										// ssrId如果是-1表示减去，如果是1表示增加
										log.setSsrId(-1);
										log.setScore(myOrderSku.getSavePrice());
										log.setOrderseedId(myOrder1.getId());
										memerScoreLogMapper.insertSelective(log);
									}
								}

							}
						}

						Order myOrder = new Order();
						myOrder.setId(order2.getId());
						myOrder.setOrderPrice(orderPrice);
						myOrder.setSavePrice(savePrice);

						orderMapper.updateOrderPrice(myOrder);

						MemberUser user = memberUserMapper.selectByPrimaryKey(order2.getmId());
						MemberUser memberUser1 = new MemberUser();
						memberUser1.setmId(order2.getmId());
						int point = user.getPoint() - myOrder.getSavePrice();
						memberUser1.setPoint(point);
						memberUserMapper.updatePointBymId(memberUser1);
						map.put("isUseBean", "1");
						map.put("beanPrice", myOrder.getSavePrice());
					}

				}
				userSubmitOrderService.updatePayPrice(order3.getId());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * author xxj
	 */
	@Override
	public Order getOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return orderMapper.getOrderByOrderNo(orderNo);
	}

	/**
	 * 判断是否重复拼单
	 */
	@Override
	public int getByMidAndTeamNo(int mId, String teamNo, String goodsId) throws Exception {
		// row:0代表可进行到下一步，1(普通商品)代表用户已参团，2(惠省钱商品)代表用在此专区的参团次数已达上限，请前往开团
		int row = 0;
		// 如果该商品在惠省钱活动中
		TopicGoods topicGoods = new TopicGoods();
		topicGoods.setGoodsId(Integer.parseInt(goodsId));
		List<TopicGoods> listTopicG = topicGoodsMapper.selectTopicGoodsBySaveMoney(topicGoods);
		// 如果该商品是惠省钱专区的商品，则需要判断用户在该专区的参团次数限次
		if (listTopicG.size() > 0) {
			TopicSavemoneyLog log = new TopicSavemoneyLog();
			log.setmId(mId);
			List<TopicSavemoneyLog> logList = topicSavemoneyLogMapper.selectLogByParam(log);
			// 专区内所有商品，限制用户仅可参团1次，如果用户已参团过了
			if (logList.size() > 0) {
				row = 2;
			} else {
				row = 0;
			}
		} else {
			OrderTeam orderTeam = orderTeamMapper.getByMidAndTeamNo(mId, teamNo);
			if (orderTeam != null) {
				row = 1;
			} else {
				row = 0;
			}
		}

		return row;
	}

	/**
	 * author xxj
	 * 
	 * @return
	 */
	@Transactional
	public void submitJdOrder(Order order) {
		try {
			// Order order2 = orderMapper.selectByPrimaryKey(order.getId());
			Integer fz = order.getFz().intValue();
			// 是否是团购单，0不是，1是
			if (fz == 1) { // 团单
				List<OrderTeam> orderTeam = orderTeamMapper.selectOrderTeanByOrderNo(order.getOrderNo());
				if (orderTeam.size() > 0) {
					// -1 拼单失败 0拼单中 1成功
					if (orderTeam.get(0).getStatus().intValue() == 1) {
						OrderSku orderSku = new OrderSku();
						orderSku.setOrderId(order.getId());
						List<OrderSku> list = orderSkuMapper.selectJdSupport(orderSku);
						if (list.size() > 0) {
							jdOrderService.updateJDOrderId(order);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 2018-3-24程凤云
	public void insMemberBalancdLog(OrderCollectionDoc cDoc) {
		if (cDoc != null) {
			// 程凤云2018-3-24将信息insert到member_balance_log(会员收支日志表)
			MemberBalanceLog log = new MemberBalanceLog();
			// 用户的id
			log.setmId(cDoc.getmId());
			// 单位分 支出为负数 收入则正数
			log.setMoney(cDoc.getAmount());
			// 收支类型 -1支出 0收入
			log.setBalanceType(-1);
			// 目标ID
			log.setTargetId(cDoc.getOrderId());
			// 目标ID类型 1种子收支 2购买商品支出
			log.setTargetType(2);
			// 发生时间
			log.setOcTime(new Date());
			memberBalanceLogMapper.insertSelective(log);
			// end
		}
	}

	public Integer returnDauPrice(Integer goodsId) {
		TopicDauctionPrice price = new TopicDauctionPrice();
		price.setGoodsId(goodsId);
		List<TopicDauctionPrice> dauList = topicDauctionPriceMapper.selectCurrentPrice(price);
		if (dauList.size() > 0) {
			return dauList.get(0).getPrice();
		} else {
			List<GoodsSku> skuList = goodsSkuMapper.selectListByGoodsId(goodsId);
			return skuList.get(0).getTeamPrice();
		}

	}

	// 判断当前商品的拼团人数是否超额 2018.5.8 zlk
	@Override
	public int teamNumExcess(String teamNo, String goodsId) {
		// TODO Auto-generated method stub

		List<OrderTeam> o = orderTeamMapper.getByTeamNo(teamNo);
		Goods g = new Goods();
		if (StringUtils.isNotBlank(goodsId)) {
			g = goodsMapper.selectByPrimaryKey(Integer.valueOf(goodsId));
		} else {
			GoodsSku gs = goodsSkuMapper.selectByPrimaryKey(o.get(0).getGoodsSkuId());
			g = goodsMapper.selectByPrimaryKey(gs.getGoodsId());
		}
		int num = g.getTeamNum() - o.size();
		return num;
	}

	@Override
	public Map<String, Object> updateUserStoreSecond(Order order, String fz) {
		// 更新滨惠豆和优惠劵的信息 -cheng
		Map<String, Object> map = new HashMap<>();
		try {

			Order order2 = orderMapper.selectByPrimaryKey(order.getId());
			
			if (!fz.equals("1")) {
				// 1调用使用滨惠豆下的单，0不使用
				Order order3 = orderMapper.selectByPrimaryKey(order2.getId());
				if (order3.getIsBeans() == 1) {
					if (order3.getOrderPrice() > 0) {
						List<OrderShop> myOShop = orderShopMapper.getByOrderId(order3.getId());
						MemberUser memberUser = memberUserMapper.selectByMId(order3.getId());
						int mypoint = memberUser.getPoint();
						if (myOShop.size() > 0) {
							int savePrice = 0;
							int orderPrice = 0;
							int needScoreCount = 0;
							double sellPriceCount = 0;
							for (OrderShop orderShop2 : myOShop) {
								List<OrderShop> orderShopList = orderShopMapper
										.selectByOrderShopId1(orderShop2.getOrderId());
								List<OrderSku> orderSkuList = orderSkuMapper
										.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
								for (OrderSku orderSku : orderSkuList) {
									int num = orderSku.getSkuNum();
									GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
									Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
									if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
										int a = goodsSku.getSellPrice();
										int b = Integer.valueOf(goods.getDeductibleRate());
										needScoreCount = needScoreCount + a * b / 100 * num;
										sellPriceCount = sellPriceCount + a * num;
									}
								}
							}
							if (mypoint < needScoreCount) {
								needScoreCount = mypoint;
								for (OrderShop orderShop2 : myOShop) {
									int score2 = 0;
									List<OrderShop> orderShopList = orderShopMapper
											.selectByOrderShopId1(orderShop2.getOrderId());
									int needScore = 0;
									List<OrderSku> orderSkuList = orderSkuMapper
											.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
									for (OrderSku orderSku : orderSkuList) {
										int num = orderSku.getSkuNum();
										GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
										Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
										if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
											int a = goodsSku.getSellPrice();
											// 四舍五入滨惠豆
											needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
													.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + needScore;
										}
									}
									
									if (needScore > mypoint) { score2 = mypoint; } else { score2 = needScore; }
									
									OrderShop orderShop = new OrderShop();
									orderShop.setId(orderShop2.getId());
									orderShop.setOrderPrice(orderShop2.getOrderPrice() - score2);
									orderShop.setSavePrice(score2);
									if (score2 > orderShop2.getOrderPrice()) {
										orderShop.setOrderPrice(0);
										orderShop.setSavePrice(orderShop2.getOrderPrice());
									}
									savePrice = savePrice + orderShop.getSavePrice();
									orderPrice = orderPrice + orderShop.getOrderPrice();
									orderShopMapper.updateOrderPrice(orderShop);
									List<String> cartIds = JsonUtils.stringToList(order3.getCartId());
									List<MyOrder> myOrderList = orderSkuMapper.selectSkuScore(orderShop2.getId(),
											cartIds);
									// 多余的积分
									if (myOrderList.size() > 0 && (score2 > orderShop2.getOrderPrice())) {
										// 更新orderSku表
										int myScore = orderShop.getSavePrice();
										for (MyOrder myOrder1 : myOrderList) {
											// int
											// score1=myOrder1.getSellPrice()*Integer.valueOf(myOrder1.getDeductibleRate())/100*myOrder1.getSkuNum();
											int num = myOrder1.getSkuNum();
											GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(myOrder1.getSkuId());
											Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
											if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
												int a = goodsSku.getSellPrice();
												// 四舍五入滨惠豆
												needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
														.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
											}
											myScore = myScore - needScore;
											OrderSku myOrderSku = new OrderSku();
											myOrderSku.setId(myOrder1.getId());
											if (myScore <= 0) {
												myOrderSku.setSavePrice(orderShop.getSavePrice()
														- orderSkuMapper.selectSavePrice(orderShop2.getId()));
												if (myOrderSku.getSavePrice() > 0) {
													orderSkuMapper.updateScore(myOrderSku);
													// 将滨惠更改到log表里面

													if (mypoint > needScore) {
														myOrderSku.setSavePrice(needScore);
														mypoint = mypoint - needScore;
													} else {
														myOrderSku.setSavePrice(mypoint);
														mypoint = 0;
													}
													MemerScoreLog log = new MemerScoreLog();
													log.setCreateTime(new Date());
													log.setmId(orderShop2.getmId());
													log.setIsDel(0);
													log.setSmId(-1);
													// ssrId如果是-1表示减去，如果是1表示增加
													log.setSsrId(-1);
													log.setScore(myOrderSku.getSavePrice());
													log.setOrderseedId(myOrder1.getId());
													memerScoreLogMapper.insertSelective(log);
												}
												break;
											} else {
												myOrderSku.setSavePrice(needScore);
												orderSkuMapper.updateScore(myOrderSku);
												// 将滨惠更改到log表里面
												MemerScoreLog log = new MemerScoreLog();

												if (mypoint > needScore) {
													myOrderSku.setSavePrice(needScore);
													mypoint = mypoint - needScore;
												} else {
													myOrderSku.setSavePrice(mypoint);
													mypoint = 0;
												}
												log.setCreateTime(new Date());
												log.setmId(orderShop2.getmId());
												log.setIsDel(0);
												log.setSmId(-1);
												// ssrId如果是-1表示减去，如果是1表示增加
												log.setSsrId(-1);
												log.setScore(myOrderSku.getSavePrice());
												log.setOrderseedId(myOrder1.getId());
												memerScoreLogMapper.insertSelective(log);
											}
										}
									} else if (myOrderList.size() > 0 && (score2 <= orderShop2.getOrderPrice())) {
										// 更新orderSku表

										for (MyOrder myOrder1 : myOrderList) {
											OrderSku myOrderSku = new OrderSku();
											// int
											// score1=myOrder1.getSellPrice()*Integer.valueOf(myOrder1.getDeductibleRate())/100*myOrder1.getSkuNum();
											int num = myOrder1.getSkuNum();
											GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(myOrder1.getSkuId());
											Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
											if (Integer.valueOf(goods.getDeductibleRate()) > 0) {
												int a = goodsSku.getSellPrice();
												// 四舍五入滨惠豆
												needScore = new BigDecimal(a * num * needScoreCount / sellPriceCount)
														.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
											}
											if (mypoint > needScore) {
												myOrderSku.setSavePrice(needScore);
												mypoint = mypoint - needScore;
											} else {
												myOrderSku.setSavePrice(mypoint);
												mypoint = 0;
											}
											myOrderSku.setId(myOrder1.getId());
											orderSkuMapper.updateScore(myOrderSku);

											// 将滨惠更改到log表里面
											MemerScoreLog log = new MemerScoreLog();
											log.setCreateTime(new Date());
											log.setmId(orderShop2.getmId());
											log.setIsDel(0);
											log.setSmId(-1);
											// ssrId如果是-1表示减去，如果是1表示增加
											log.setSsrId(-1);
											log.setScore(myOrderSku.getSavePrice());
											log.setOrderseedId(myOrder1.getId());
											memerScoreLogMapper.insertSelective(log);
										}
									}

								}
							} else {
								for (OrderShop orderShop2 : myOShop) {
									int score2 = 0;
									List<OrderShop> orderShopList = orderShopMapper
											.selectByOrderShopId1(orderShop2.getOrderId());
									int needScore = 0;
									List<OrderSku> orderSkuList = orderSkuMapper
											.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
									for (OrderSku orderSku : orderSkuList) {
										int num = orderSku.getSkuNum();
										GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
										Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
										int a = goodsSku.getSellPrice();
										int b = Integer.valueOf(goods.getDeductibleRate());
										needScore = needScore + a * b * num / 100;
									}
									if (needScore > mypoint) {
										score2 = mypoint;
									} else {
										score2 = needScore;
									}
									OrderShop orderShop = new OrderShop();
									orderShop.setId(orderShop2.getId());
									orderShop.setOrderPrice(orderShop2.getOrderPrice() - score2);
									orderShop.setSavePrice(score2);
									if (score2 > orderShop2.getOrderPrice()) {
										orderShop.setOrderPrice(0);
										orderShop.setSavePrice(orderShop2.getOrderPrice());
									}
									savePrice = savePrice + orderShop.getSavePrice();
									orderPrice = orderPrice + orderShop.getOrderPrice();
									orderShopMapper.updateOrderPrice(orderShop);
									List<String> cartIds = JsonUtils.stringToList(order3.getCartId());
									List<MyOrder> myOrderList = orderSkuMapper.selectSkuScore(orderShop2.getId(),
											cartIds);
									// 多余的积分
									if (myOrderList.size() > 0 && (score2 > orderShop2.getOrderPrice())) {
										// 更新orderSku表
										int myScore = orderShop.getSavePrice();
										for (MyOrder myOrder1 : myOrderList) {
											int score1 = myOrder1.getSellPrice()
													* Integer.valueOf(myOrder1.getDeductibleRate()) * myOrder1.getSkuNum()/ 100;
											myScore = myScore - score1;
											OrderSku myOrderSku = new OrderSku();
											myOrderSku.setId(myOrder1.getId());
											if (myScore <= 0) {
												myOrderSku.setSavePrice(orderShop.getSavePrice()
														- orderSkuMapper.selectSavePrice(orderShop2.getId()));
												if (myOrderSku.getSavePrice() > 0) {
													orderSkuMapper.updateScore(myOrderSku);
													// 将滨惠更改到log表里面

													if (mypoint > needScore) {
														myOrderSku.setSavePrice(score1);
														mypoint = mypoint - score1;
													} else {
														myOrderSku.setSavePrice(mypoint);
														mypoint = 0;
													}
													MemerScoreLog log = new MemerScoreLog();
													log.setCreateTime(new Date());
													log.setmId(orderShop2.getmId());
													log.setIsDel(0);
													log.setSmId(-1);
													// ssrId如果是-1表示减去，如果是1表示增加
													log.setSsrId(-1);
													log.setScore(myOrderSku.getSavePrice());
													log.setOrderseedId(myOrder1.getId());
													memerScoreLogMapper.insertSelective(log);
												}
												break;
											} else {
												myOrderSku.setSavePrice(score1);
												orderSkuMapper.updateScore(myOrderSku);
												// 将滨惠更改到log表里面
												MemerScoreLog log = new MemerScoreLog();

												if (mypoint > score1) {
													myOrderSku.setSavePrice(score1);
													mypoint = mypoint - score1;
												} else {
													myOrderSku.setSavePrice(mypoint);
													mypoint = 0;
												}
												log.setCreateTime(new Date());
												log.setmId(orderShop2.getmId());
												log.setIsDel(0);
												log.setSmId(-1);
												// ssrId如果是-1表示减去，如果是1表示增加
												log.setSsrId(-1);
												log.setScore(myOrderSku.getSavePrice());
												log.setOrderseedId(myOrder1.getId());
												memerScoreLogMapper.insertSelective(log);
											}
										}
									} else if (myOrderList.size() > 0 && (score2 <= orderShop2.getOrderPrice())) {
										// 更新orderSku表
										for (MyOrder myOrder1 : myOrderList) {
											OrderSku myOrderSku = new OrderSku();
											int score1 = myOrder1.getSellPrice()
													* Integer.valueOf(myOrder1.getDeductibleRate()) * myOrder1.getSkuNum()/ 100;
											if (mypoint > score1) {
												myOrderSku.setSavePrice(score1);
												mypoint = mypoint - score1;
											} else {
												myOrderSku.setSavePrice(mypoint);
												mypoint = 0;
											}
											myOrderSku.setId(myOrder1.getId());
											orderSkuMapper.updateScore(myOrderSku);

											// 将滨惠更改到log表里面
											MemerScoreLog log = new MemerScoreLog();
											log.setCreateTime(new Date());
											log.setmId(orderShop2.getmId());
											log.setIsDel(0);
											log.setSmId(-1);
											// ssrId如果是-1表示减去，如果是1表示增加
											log.setSsrId(-1);
											log.setScore(myOrderSku.getSavePrice());
											log.setOrderseedId(myOrder1.getId());
											memerScoreLogMapper.insertSelective(log);
										}
									}

								}

							}

							Order myOrder = new Order();
							myOrder.setId(order2.getId());
							myOrder.setOrderPrice(orderPrice);
							myOrder.setSavePrice(savePrice);

							orderMapper.updateOrderPrice(myOrder);

							MemberUser user = memberUserMapper.selectByPrimaryKey(order2.getmId());
							MemberUser memberUser1 = new MemberUser();
							memberUser1.setmId(order2.getmId());
							int point = user.getPoint() - myOrder.getSavePrice();
							memberUser1.setPoint(point);
							memberUserMapper.updatePointBymId(memberUser1);
							map.put("isUseBean", "1");
							map.put("beanPrice", myOrder.getSavePrice());
						}
					}
				}
				userSubmitOrderService.updatePayPrice(order3.getId());
			} else {
				Order order3 = orderMapper.selectByPrimaryKey(order2.getId());
				if (order3.getIsBeans() == 1) {
					if (order3.getOrderPrice() > 0) {
						List<OrderShop> myOShop = orderShopMapper.getByOrderId(order3.getId());
						MemberUser memberUser = memberUserMapper.selectByMId(order3.getId());
						int mypoint = memberUser.getPoint();
						if (myOShop.size() > 0) {
							int savePrice = 0;
							int orderPrice = 0;
							for (OrderShop orderShop2 : myOShop) {
								int score2 = 0;
								List<OrderShop> orderShopList = orderShopMapper
										.selectByOrderShopId1(orderShop2.getOrderId());
								int needScore = 0;
								List<OrderSku> orderSkuList = orderSkuMapper
										.selectByOrderId(orderShopList.get(0).getOrderId(), orderShop2.getShopId());
								for (OrderSku orderSku : orderSkuList) {
									int num = orderSku.getSkuNum();
									GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
									Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
									int a = goodsSku.getTeamPrice();
									int b = Integer.valueOf(goods.getDeductibleRate());
									needScore = needScore + a * b * num / 100;
								}
								if (needScore > mypoint) {
									score2 = mypoint;
								} else {
									score2 = needScore;
								}
								OrderShop orderShop = new OrderShop();
								orderShop.setId(orderShop2.getId());
								orderShop.setOrderPrice(orderShop2.getOrderPrice() - score2);
								orderShop.setSavePrice(score2);
								if (score2 > orderShop2.getOrderPrice()) {
									orderShop.setOrderPrice(0);
									orderShop.setSavePrice(orderShop2.getOrderPrice());
								}
								savePrice = savePrice + orderShop.getSavePrice();
								orderPrice = orderPrice + orderShop.getOrderPrice();
								orderShopMapper.updateOrderPrice(orderShop);
								List<String> cartIds = JsonUtils.stringToList(order3.getCartId());
								List<MyOrder> myOrderList = orderSkuMapper.selectSkuScore(orderShop2.getId(), cartIds);
								// 多余的积分
								if (myOrderList.size() > 0 && (score2 > orderShop2.getOrderPrice())) {
									// 更新orderSku表
									int myScore = orderShop.getSavePrice();
									for (MyOrder myOrder1 : myOrderList) {
										int score1 = myOrder1.getTeamPrice()
												* Integer.valueOf(myOrder1.getDeductibleRate()) * myOrder1.getSkuNum()/ 100;
										myScore = myScore - score1;
										OrderSku myOrderSku = new OrderSku();
										myOrderSku.setId(myOrder1.getId());
										if (myScore <= 0) {
											myOrderSku.setSavePrice(orderShop.getSavePrice()
													- orderSkuMapper.selectSavePrice(orderShop2.getId()));
											if (myOrderSku.getSavePrice() > 0) {
												orderSkuMapper.updateScore(myOrderSku);
												// 将滨惠更改到log表里面

												if (mypoint > needScore) {
													myOrderSku.setSavePrice(score1);
													mypoint = mypoint - score1;
												} else {
													myOrderSku.setSavePrice(mypoint);
													mypoint = 0;
												}
												MemerScoreLog log = new MemerScoreLog();
												log.setCreateTime(new Date());
												log.setmId(orderShop2.getmId());
												log.setIsDel(0);
												log.setSmId(-1);
												// ssrId如果是-1表示减去，如果是1表示增加
												log.setSsrId(-1);
												log.setScore(myOrderSku.getSavePrice());
												log.setOrderseedId(myOrder1.getId());
												memerScoreLogMapper.insertSelective(log);
											}
											break;
										} else {
											myOrderSku.setSavePrice(score1);
											orderSkuMapper.updateScore(myOrderSku);
											// 将滨惠更改到log表里面
											MemerScoreLog log = new MemerScoreLog();

											if (mypoint > score1) {
												myOrderSku.setSavePrice(score1);
												mypoint = mypoint - score1;
											} else {
												myOrderSku.setSavePrice(mypoint);
												mypoint = 0;
											}
											log.setCreateTime(new Date());
											log.setmId(orderShop2.getmId());
											log.setIsDel(0);
											log.setSmId(-1);
											// ssrId如果是-1表示减去，如果是1表示增加
											log.setSsrId(-1);
											log.setScore(myOrderSku.getSavePrice());
											log.setOrderseedId(myOrder1.getId());
											memerScoreLogMapper.insertSelective(log);
										}
									}
								} else if (myOrderList.size() > 0 && (score2 <= orderShop2.getOrderPrice())) {
									// 更新orderSku表
									for (MyOrder myOrder1 : myOrderList) {
										OrderSku myOrderSku = new OrderSku();
										int score1 = myOrder1.getTeamPrice()
												* Integer.valueOf(myOrder1.getDeductibleRate()) / 100
												* myOrder1.getSkuNum();
										if (mypoint > score1) {
											myOrderSku.setSavePrice(score1);
											mypoint = mypoint - score1;
										} else {
											myOrderSku.setSavePrice(mypoint);
											mypoint = 0;
										}
										myOrderSku.setId(myOrder1.getId());
										orderSkuMapper.updateScore(myOrderSku);

										// 将滨惠更改到log表里面
										MemerScoreLog log = new MemerScoreLog();
										log.setCreateTime(new Date());
										log.setmId(orderShop2.getmId());
										log.setIsDel(0);
										log.setSmId(-1);
										// ssrId如果是-1表示减去，如果是1表示增加
										log.setSsrId(-1);
										log.setScore(myOrderSku.getSavePrice());
										log.setOrderseedId(myOrder1.getId());
										memerScoreLogMapper.insertSelective(log);
									}
								}

							}

							Order myOrder = new Order();
							myOrder.setId(order2.getId());
							myOrder.setOrderPrice(orderPrice);
							myOrder.setSavePrice(savePrice);

							orderMapper.updateOrderPrice(myOrder);

							MemberUser user = memberUserMapper.selectByPrimaryKey(order2.getmId());
							MemberUser memberUser1 = new MemberUser();
							memberUser1.setmId(order2.getmId());
							int point = user.getPoint() - myOrder.getSavePrice();
							memberUser1.setPoint(point);
							memberUserMapper.updatePointBymId(memberUser1);
							map.put("isUseBean", "1");
							map.put("beanPrice", myOrder.getSavePrice());
						}
					}
				}
				userSubmitOrderService.updatePayPrice(order3.getId());
			}
			List<OrderShop> myOrderShop = orderShopMapper.getByOrderId(order2.getId());
			// 计算订单失效时间
			for (OrderShop o : myOrderShop) {
				List<OrderSku> listOrderSku = orderSkuMapper.getSkuListByOrderShopId(o.getId());
				List<Integer> list2 = new ArrayList<Integer>();
				Calendar ca = Calendar.getInstance();
				if (listOrderSku.size() > 1) {
					// 1个商品以上
					for (int j = 0; j < listOrderSku.size(); j++) {
						List<ActZoneGoods> listActZoneGoods = actZoneGoodsMapper
								.getByGoodsId(listOrderSku.get(j).getGoodsId());
						if (listActZoneGoods.size() > 0) {
							// 专区商品
							for (ActZoneGoods a : listActZoneGoods) {
								if (StringUtils.isNotBlank(a.getFailuretime())) {
									list2.add(Integer.valueOf(a.getFailuretime()));
								}
							}
						} else {
							// 普通商品
							list2.add(Contants.oneDayLen);
						}
					}
					ca.setTime(order2.getAddtime());
					ca.add(Calendar.MINUTE, Collections.min(list2));
					// myOrderShop.get(0).setValidLen(ca.getTime());
					o.setValidLen(ca.getTime());
					orderShopMapper.updateByPrimaryKeySelective(o);
				} else if (listOrderSku.size() > 0) {
					// 1个商品
					List<ActZoneGoods> listActZoneGoods = actZoneGoodsMapper
							.getByGoodsId(listOrderSku.get(0).getGoodsId());
					if (listActZoneGoods.size() > 0) {
						// 证明是专区商品
						for (ActZoneGoods a : listActZoneGoods) {
							if (StringUtils.isNotBlank(a.getFailuretime())) {
								list2.add(Integer.valueOf(a.getFailuretime()));
							}
						}
						if (list2 != null && list2.size() > 0) {
							ca.setTime(order2.getAddtime());
							ca.add(Calendar.MINUTE, Collections.min(list2));
							// myOrderShop.get(0).setValidLen(ca.getTime());
							o.setValidLen(ca.getTime());
							orderShopMapper.updateByPrimaryKeySelective(o);
						}
					} else {
						// 普通商品
						ca.setTime(order2.getAddtime());
						ca.add(Calendar.MINUTE, Contants.oneDayLen);
						// myOrderShop.get(0).setValidLen(ca.getTime());
						o.setValidLen(ca.getTime());
						orderShopMapper.updateByPrimaryKeySelective(o);
					}
				}
			}
			if (order2.getCouponsId() != null && !order2.getCouponsId().equals("0")) {
				if (myOrderShop.size() > 0) {
					Integer mainPrice = 0;
					Integer mainDeliveryPrice = 0;
					Integer couponPrice = 0;
					for (OrderShop orderShop : myOrderShop) {
						OrderShop myOShop = new OrderShop();
						myOShop.setId(orderShop.getId());
						myOShop.setgDeliveryPrice(orderShop.getgDeliveryPrice());
						// 程凤云 根据coupon的id 去获取当前的记录
						Integer amount = 0;
						Integer shopDeliveryPrice = orderShop.getgDeliveryPrice();
						Map<String, Object> myCouponMap = userSubmitOrderService.selectCouponMsg(orderShop.getShopId(),
								order2.getCouponsId());
						if (myCouponMap != null && myCouponMap.get("couponId") != null
								&& myCouponMap.get("couponType") != null) {
							// 优惠券类型，1普通券，2免邮券，3红包券(使用卷的金额：当1和3的时候金额都是取amount字段)
							// 抵扣的金额
							amount = (Integer) myCouponMap.get("amount");
							Integer type = (Integer) myCouponMap.get("couponType");
							int couponLogId = (int) myCouponMap.get("couponId");
							Long catId = (Long) myCouponMap.get("catId");
							myOShop.setCouponId(couponLogId);
							if (type == 1) {
								// 使用普通卷 该商家商品价格之和上减去普通卷抵扣金额（这种情况不可能出现<0）
								Integer price = orderShop.getOrderPrice() - amount;
								myOShop.setOrderPrice(price);
								// 当抵扣的金额出现0时
								if (price <= 0) {
									myOShop.setOrderPrice(0);
									amount = orderShop.getOrderPrice();
								}
							} else if (type == 2) {
								// 使用免邮券 ： 该商家订单的邮费全免
								Integer price = orderShop.getOrderPrice() - orderShop.getgDeliveryPrice();
								myOShop.setOrderPrice(price);
								amount = shopDeliveryPrice;
								shopDeliveryPrice=0;//邮费为0
							} else if (type == 3) {
								// 使用红包卷 该商家商品价格之和上减去红包金额（<0的时候 让该商家的订单金额 为0）
								Integer price = orderShop.getOrderPrice() - amount;
								myOShop.setOrderPrice(price);
								// 当抵扣的金额出现0时
								if (price <= 0) {
									myOShop.setOrderPrice(0);
									amount = orderShop.getOrderPrice();
								}
							}
							// 将该优惠劵使用的信息同步更新到couponLog表
							CouponLog myLog = new CouponLog();
							myLog.setId(couponLogId);
							myLog.setOrderId(orderShop.getId());
							myLog.setStatus(1);
							myLog.setUseTime(new Date());
							// 同步更新order_shop表的优惠劵信息
							myOShop.setCouponPrice(amount);
							myOShop.setgDeliveryPrice(shopDeliveryPrice);
							couponLogMapper.updateByPrimaryKeySelective(myLog);

							CouponLog couponLog = couponLogMapper.selectByPrimaryKey(couponLogId);
							Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
							coupon.setUsed(coupon.getUsed() + 1);// 使用量+1
							couponMapper.updateByPrimaryKeySelective(coupon);// 同步更新coupon表的优惠劵信息

							// 同步更新order_shop表的优惠劵信息
							orderShopMapper.updateByPrimaryKeySelective(myOShop);
							// 如果该商家的该订单使用了优惠劵
							mainPrice = mainPrice + myOShop.getOrderPrice();
							mainDeliveryPrice = mainDeliveryPrice + myOShop.getgDeliveryPrice();
							couponPrice = couponPrice + amount;
							if (type != 2) {
								userSubmitOrderService.couponMony(type, catId, (Integer) myCouponMap.get("amount"),
										orderShop.getId(), orderShop.getShopId(), couponLogId,
										orderShop.getgDeliveryPrice(), myOShop.getOrderPrice());
							}
						} else {
							// 如果该商家的该订单未使用优惠劵
							mainPrice = mainPrice + orderShop.getOrderPrice();
							mainDeliveryPrice = mainDeliveryPrice + shopDeliveryPrice;
							couponPrice = couponPrice + amount;
						}
					}
					Order myOrder = new Order();
					myOrder.setId(order2.getId());
					myOrder.setOrderPrice(mainPrice);
					myOrder.setDeliveryPrice(mainDeliveryPrice);
					myOrder.setCouponsPrice(couponPrice);
					orderMapper.updateByPrimaryKeySelective(myOrder);
					map.put("isUseCoupon", "1");
					map.put("couponPrice", couponPrice);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;

	}

}
