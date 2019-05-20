package com.order.user.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.pojo.BHSeed;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderCollectionDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberBalanceLogMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBalanceLog;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.JsonUtils;
import com.order.user.controller.IDUtils;
import com.order.user.service.JDOrderService;
import com.order.user.service.SimpleOrderService;
import com.order.user.service.UserOrderService;
import com.order.user.service.UserSubmitOrderService;
import com.thoughtworks.xstream.mapper.Mapper.Null;

import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.ActZoneGoodsMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicDauctionPriceMapper;
import com.bh.goods.pojo.TopicDauctionPrice;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;





@Service
public class UserSubmitOrderServiceImpl implements UserSubmitOrderService{

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsCartMapper goodsCartMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderSkuMapper orderSkuMapper;

	@Autowired
	private GoodsSkuMapper goodsSkuMapper;

	@Autowired
	private MemberShopMapper memberShopMapper;

	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private SimpleOrderService simpleOrderService;
	@Autowired
	private TopicDauctionPriceMapper topicDauctionPriceMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private ActZoneGoodsMapper actZoneGoodsMapper;
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;
	@Autowired
	private MemberBalanceLogMapper memberBalanceLogMapper;
	

	
	//用户提交订单前需要做最后的校验工作
	public Map<String, Object> rendOrder(Map<String, String> map, Member member, HttpServletRequest request) {
		Map<String, Object> myMap=new HashMap<>();
	    try {
	    	//订单的id:(ps:如果该商品从商品详情过来,orderId的值为0)
	    	String orderId = map.get("orderId");
	    	//cartIds:196,198购物车的id,以数组的形式返回
			String cartIds = map.get("cartIds");
			// 前端传过来的地址的id
			String addressId = map.get("addressId");
			// 如果orderId的值为0,则 生成订单
			if ("0".equals(orderId)) {
				//首先判断地址id是否为空,如果空则给提示
				if (StringUtils.isEmpty(addressId)) {
					myMap.put("mykey", "-1");
					myMap.put("order", "参数地址ID不能为空");
					return myMap;
				}
				if (StringUtils.isEmpty(cartIds)) {
					myMap.put("mykey", "-1");
					myMap.put("order", "参数地址cartIds不能为空");
					return myMap;
				}
				else{
					List<String> c = new ArrayList<>();
					c = JsonUtils.stringToList(cartIds);	
					//判断购买的数量是否大于或者等于1
					BhResult bhResult=simpleOrderService.getGoodsByCartId(cartIds);
					//如果有些商品的购买数量小于1
					if (bhResult.getStatus()==400) {
						myMap.put("mykey", "-1");
						myMap.put("order", Contants.GOODS_NUM);
						return myMap;
					}
					// 判断库存
					bhResult = jdOrderService.returnStock("0", cartIds, addressId, member);
					if (bhResult.getStatus() == 400) {
						myMap.put("mykey", "-1");
						myMap.put("order", bhResult.getMsg());
						return myMap;
					}
					// 如果库存有货,校验商品限购的数量
					bhResult = jdOrderService.memberBuyGoodsIsLimit(cartIds, "0", member);
					//bhResult 的取值范围:如果status==200则全部有货，如果status==400,则提示该商品限购x个
					if (bhResult.getStatus() == 400) {
						myMap.put("mykey", "-1");
						myMap.put("order", bhResult.getMsg());
						return myMap;
					}
					
					// 校验商品区域购买限制查询(京东商品)					
					bhResult = jdOrderService.getAreaLimit(addressId, member, c);
					if (bhResult.getStatus()==400) {
						myMap.put("mykey", "-1");
						myMap.put("order", bhResult.getMsg());
						return myMap;
					}
					
					// 判断商品区域购买限制查询(非京东商品)																	
					bhResult = jdOrderService.getAreaLimitByBH(addressId, member, c);
					if (bhResult.getStatus()==400) {
						myMap.put("mykey", "-1");
						myMap.put("order", bhResult.getMsg());
						return myMap;
					}																			
					
					//校验滨惠豆的数量是否满足
					 bhResult=simpleOrderService.getSimpleBhBean(map, member, request);
						if (bhResult.getStatus()==400) {
							myMap.put("mykey", "-1");
							myMap.put("order", bhResult.getMsg());
							return myMap;
						}
					/*Set<Integer> goodsIdSet = new HashSet<>();
					List<String> cartIds1= Arrays.asList(map.get("cartIds").split(","));
					List<GoodsCart> GoodsCartList=goodsCartMapper.selectByCartIds(cartIds1);
					if(GoodsCartList.size()>0&&GoodsCartList!=null) {
					  for (GoodsCart goodsCart : GoodsCartList) {
						goodsIdSet.add(goodsCart.getgId());
					  }
					    List<Integer> goodsList = actZoneGoodsMapper.selectByGoodsIds(goodsIdSet);
					   if (goodsList != null&&goodsList.size()>0) {
						    bhResult=simpleOrderService.getSimpleBhBean(map, member, request);
							if (bhResult.getStatus()==400) {
								myMap.put("mykey", "-1");
								myMap.put("order", bhResult.getMsg());
								return myMap;
							}
					}
					}*/
					
					
					//如果以上条件都满足，则走下单流程
					Order order=simpleOrderService.getOrder(map, member, request);
					myMap.put("mykey", "0");
					myMap.put("order", order);
				}
				
				
			}else{
				if (StringUtils.isEmpty(orderId)) {
					myMap.put("mykey", "-1");
					myMap.put("order", "参数orderId不能为空");
					return myMap;
				}
				
				//判断该订单是否存在
				Order order=simpleOrderService.selectOrderById(Integer.parseInt(orderId));
				if (order ==null) {
					myMap.put("mykey", "-1");
					myMap.put("order", "该订单不存在,请重新下单");
					return myMap;
				}
				//设置收货地址id
				List<String> c = JsonUtils.stringToList(order.getCartId());
				if (order.getFz() !=5) {
					//如果该订单不是拍卖拍卖单,则地址不变,取原来的,否则地址重新取
					addressId=order.getmAddrId().toString();
				}
				
				//判断购买的数量是否大于或者等于1
				BhResult bhResult=simpleOrderService.getGoodsByCartId(order.getCartId());
				if (bhResult.getStatus()==400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				
				//判断库存
				bhResult = jdOrderService.returnStock("1", orderId, addressId, member);
				if (bhResult.getStatus() == 400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				// 如果库存有货,校验商品限购的数量
				bhResult = jdOrderService.memberBuyGoodsIsLimit(orderId, "1", member);
				// 的取值范围:如果status==200则全部有货，如果status==400,则提示该商品限购x个
				if (bhResult.getStatus() == 400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				
				// 判断商品区域购买限制查询(京东商品)						
				bhResult = jdOrderService.getAreaLimit(addressId, member, c);
				if (bhResult.getStatus() == 400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				
				// 判断商品区域购买限制查询(非京东商品)					
				bhResult = jdOrderService.getAreaLimitByBH(addressId, member, c);
				if (bhResult.getStatus() == 400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				
				//全部有货,则校验区域的配送范围
				bhResult = jdOrderService.memberBuyGoodsIsLimit(orderId,"1",member);
				if (bhResult.getStatus() == 400) {
					myMap.put("mykey", "-1");
					myMap.put("order", bhResult.getMsg());
					return myMap;
				}
				
				//如果以上条件都满足,则走支付流程
				Order myOrder=simpleOrderService.getOrder(map, member, request);
				myMap.put("mykey", "0");
				myMap.put("order", myOrder);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			myMap.put("mykey", -1);
			myMap.put("order", null);
			// TODO: handle exception
		}
		return myMap;
		
	}
	
	
	//生成订单
	@Transactional
	public Order insertOrderBySelective(Order order, String fz, String teamNo) throws Exception {
		//购物车的id
		String cartsId = order.getCartId();
		//查询该订单是否已经存在
		Order orderc = new Order();
		orderc.setCartId(cartsId);
		orderc.setmId(order.getmId());
		List<Order> order2 = orderMapper.selectCountByCardId(orderc);

		if (order2.size() > 0) {
			Order order3 = order2.get(0);
			return order3;
		} else {
			List<String> ids = JsonUtils.stringToList(cartsId);
			List<GoodsCart> list = goodsCartMapper.selectCoodsCartByIds(ids);
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(list.get(0).getShopId());
			// order的编号
			if (StringUtils.isNotEmpty(memberShop.getBusiPayPre())) {
				order.setOrderNo(IDUtils.getOrderNo(memberShop.getBusiPayPre()));
			} else {
				order.setOrderNo(IDUtils.getOrderNo(null));
			}
			// 生成orderMain订单
			orderMapper.insertSelective(order);
			if (StringUtils.isNotEmpty(cartsId)) {	
				// 将字段isDel设为4，表示该购物车的id的商品已放到订单
				goodsCartMapper.updateGoodsCartByPrimaryKeyAndSetIsDel4(ids);
				// 修改时间 2017-10-26,根据goodsId查询对应的shopId
				List<Integer> listShopId = goodsCartMapper.selectShopId(ids);
				// 订单的金额
				int orderPrice = 0;
				// 邮费
				int mainDeliPrice = 0;
				for (int i = 0; i < listShopId.size(); i++) {
					List<GoodsCart> listGoodsCart = goodsCartMapper.selectGoodsCartByIdAndShopIds(ids, listShopId.get(i));
					Integer price = 0;
					//
					Set<Integer> goodsIdSet=new HashSet<>();
					for (int m = 0; m < listGoodsCart.size(); m++) {
						GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(listGoodsCart.get(m).getGskuid());
						//商品的数量
						int num = listGoodsCart.get(m).getNum();
						// 判断fz是否为空,如果为不为空,则是拼单的,这个时候需要取拼单价格
						if (StringUtils.isNotEmpty(fz)) {
							//商品的单价
							if (Integer.parseInt(fz) == 5) {
								int goodsSkuPrice = returnDauPrice(sku.getGoodsId());
								price = price + goodsSkuPrice * num;
							} else {
								int goodsSkuPrice = sku.getTeamPrice();
								price = price + goodsSkuPrice * num;
							}
						} else {
							int goodsSkuPrice = sku.getSellPrice();
							price = price + goodsSkuPrice * num;
						}
						goodsIdSet.add(listGoodsCart.get(m).getgId());
					}
					/**
					 * 邮费规则:2018-5-21 滨惠商城跟会员收取的邮费标准是：（跟京东一模一样）
					 * 订单金额＜49元，收取基础运费8元，不收续重运费；
					 * 49元≤订单金额＜99元，收取基础运费6元，不收续重运费；
					 * 订单金额≥99元，免基础运费，不收续重运费。
					 *
					 */
					// 邮费
					Integer shopDeliveryPrice = 0;
					 if (price >= 9900|| "1".equals(fz) || "6".equals(fz)) { //1 >99  2  如果是拼单的商品，邮费为0; 3 如果是免费兑换商品，邮费为0
						shopDeliveryPrice = 0;
					}  else  {
						//周末滨惠日和超级滨惠豆免邮
						List<Integer> isFreePostageList=actZoneGoodsMapper.selectIsFreePostage(goodsIdSet);
						if(isFreePostageList.size()>0 && isFreePostageList.size()==1 && "1".equals(isFreePostageList.get(0)+"")) {
							shopDeliveryPrice=0;
						} else { 
							if (price < 4900) {
							   shopDeliveryPrice = Contants.price1;
							} else if ((price < 9900) && (price >= 4900)) {
								shopDeliveryPrice = Contants.price2;
							}
						}
						
					}
					if ("6".equals(fz)) { //免费兑换商品
						price=0;
					}
					int total = shopDeliveryPrice + price;
					mainDeliPrice = mainDeliPrice + shopDeliveryPrice;
					orderPrice = orderPrice + total;
					OrderShop orderShop = new OrderShop();
					// 商家ID
					orderShop.setShopId(listShopId.get(i));
					// 用户ID
					orderShop.setmId(order.getmId());
					// 订单ID
					orderShop.setOrderId(order.getId());
					// 订单号
					orderShop.setOrderNo(order.getOrderNo());
					MemberShop memberShop1 = memberShopMapper.selectByPrimaryKey(listShopId.get(i));
					// 商家订单号
					if (StringUtils.isNotEmpty(memberShop1.getBusiPayPre())) {
						orderShop.setShopOrderNo(IDUtils.getOrderNo(memberShop1.getBusiPayPre()));
					} else {
						orderShop.setShopOrderNo(IDUtils.getOrderNo(null));
					}
					// 商家订单状态：1待付，2待发货，3已发货，4已收货
					orderShop.setStatus(1);
					// 是否退款:0否，1是,默认0
					orderShop.setIsRefund(0);
					// 设置邮费
					orderShop.setgDeliveryPrice(shopDeliveryPrice);
					// orderPrice = 邮费 + 商品价格 - 节省的钱
					orderShop.setOrderPrice(total);
					//优惠劵的id
					orderShop.setCouponId(0);
					//优惠劵的金额
					orderShop.setCouponPrice(0);
					//是否使用滨惠豆
					orderShop.setIsBean(order.getIsBeans());
					//生成orderShop订单
					
					orderShopMapper.insertSelective(orderShop);
					
				}
				Order order3 = new Order();
				//滨惠豆逻辑的处理
				if (order.getIsBeans().equals(1) && orderPrice<=0) {
					order3.setIsBeans(0);
				}
				order3.setmAddrId(simpleOrderService.insertAddress(String.valueOf(order.getmAddrId())));
				order3.setId(order.getId());
				order3.setOrderPrice(orderPrice);
				order3.setDeliveryPrice(mainDeliPrice);
				order3.setDeliveryPriceReal(mainDeliPrice);
				orderMapper.updateByPrimaryKeySelective(order3);
				// 通过对象orderShop查询列表
				OrderShop orderShop = new OrderShop();
				orderShop.setOrderId(order.getId());
				List<OrderShop> orderShopList = orderShopMapper.selectOrderShopByOrderIds1(orderShop);
				//生成orderSku订单
				
				insertOrderSku(list, order, orderShopList);
			
				
				Map<String,Object> myMap=userOrderService.updateUserStoreSecond(order3,fz);
				OrderCollectionDoc cDoc = new OrderCollectionDoc();
				cDoc.setAddtime(new Date());
				cDoc.setAmount(order.getOrderPrice());
				cDoc.setmId(order.getmId());
				cDoc.setOrderId(order.getId());
				cDoc.setPaymentId(order.getPaymentId());
				if (myMap!=null) {//TODO
					if ("1".equals(myMap.get("isUseCoupon"))) {
						cDoc.setCouponAmount((Integer)myMap.get("couponPrice"));
						//orderShop.setOrderPrice(orderPrice-(Integer)myMap.get("couponPrice"));
						//orderShop.setCouponId(Integer.valueOf(order.getCouponsId()));
					}
					if ("1".equals(myMap.get("isUseBean"))) {
						cDoc.setBeanAmount((Integer)myMap.get("beanPrice"));
						//orderShop.setSavePrice((Integer)myMap.get("beanPrice"));
						//orderShop.setOrderPrice(orderPrice-(Integer)myMap.get("beanPrice"));
					}
				}
				// 插入订单收款单
				orderCollectionDocMapper.insert(cDoc);
				// 程凤云2018-3-24添加代码
				insMemberBalancdLog(cDoc);
				
				Order retureOrder = orderMapper.selectByPrimaryKey(order.getId());
				retureOrder= getOrder(retureOrder);
				return retureOrder;
			} else { 
				return order;
			}

		}
		
	}
	
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
	
	// 插入orderSku
	@Transactional
	public int insertOrderSku(List<GoodsCart> list, Order order, List<OrderShop> orderShopList) throws Exception {
		int row = 0;
		try {
			if (orderShopList.size()>0) {
				for (OrderShop oShopList : orderShopList) {
					if (order !=null && StringUtils.isNotEmpty(order.getCartId())) {
						List<String>cartIds = JsonUtils.stringToList(order.getCartId());
						List<GoodsCart> carList=goodsCartMapper.getGoodsSkuListByCartId(cartIds,oShopList.getShopId());
						if (carList.size()>0) {
							for (GoodsCart goodsCart : carList) {
								OrderSku orderSku = new OrderSku();
								Integer gskuId = goodsCart.getGskuid();
								GoodsSku goodsSkuLslist = new GoodsSku();
								if (gskuId == null) {
									goodsSkuLslist = null;
								} else {
									gskuId = goodsCart.getGskuid();
									goodsSkuLslist = selectGoodsSkuByGoodsId( String.valueOf(goodsCart.getGskuid()));
								}
								Goods good = new Goods();
								// 获取good
								good = selectBygoodsId(goodsCart.getgId());
								// 订单id
								orderSku.setOrderId(order.getId());
								//order_shop的id
								orderSku.setOrderShopId(oShopList.getId());
								// 商品id
								orderSku.setGoodsId(goodsCart.getgId());
								// 商品名称
								orderSku.setGoodsName(good.getName());
								// skuid
								orderSku.setSkuId(goodsSkuLslist.getId());
								// sku编码
								orderSku.setSkuNo(goodsSkuLslist.getJdSkuNo()+"");
								// 设置图片路径 取一张
								org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSkuLslist.getValue());
								org.json.JSONArray personList = jsonObj.getJSONArray("url");
								orderSku.setSkuImage((String) personList.get(0));
								// 商品数量
								orderSku.setSkuNum(goodsCart.getNum());
								// 商品市场总价单位分
								orderSku.setSkuMarketPrice(goodsSkuLslist.getMarketPrice());
								// 支付价格单位分
								// 是否是团购单，0不是，1普通拼团单，2秒杀，3抽奖，4惠省钱，5拍卖
							      Integer fz = order.getFz();
							      if (fz == 0) {
							       orderSku.setSkuSellPriceReal(goodsSkuLslist.getSellPrice());// 支付价格单位分
							      } else if (fz == 1) {
							       orderSku.setSkuSellPriceReal(goodsSkuLslist.getTeamPrice());// 团购价格单位分
							      } else if (fz == 5) {
							       orderSku.setSkuSellPriceReal(returnDauPrice(goodsSkuLslist.getGoodsId()));// 团购价格单位分
							      } else if (fz==6) {
							       orderSku.setSkuSellPriceReal(goodsSkuLslist.getSellPrice());// 支付价格单位分
							      }
							      else {
							       orderSku.setSkuSellPriceReal(goodsSkuLslist.getTeamPrice());// 团购价格单位分
							      }
								// 商品重量		
								orderSku.setSkuWeight(goodsSkuLslist.getWeight());	
								// 规格属性数组
								orderSku.setSkuValue(goodsSkuLslist.getValue());
								// 是否已发货0、未发货;1、已发货
								orderSku.setIsSend(false);
								//是否评价0未评价.1已评价
								orderSku.setIsRefund(0);
								// 商品所属店铺id
								orderSku.setShopId(oShopList.getShopId());
								// 配送状态0初始化，1待配送，2配送中，3已完成
								orderSku.setdState(0);
								//是否退款0.为正常,1.退款中,2.退款完成
								orderSku.setRefund(0);
								
								row = orderSkuMapper.insertSelective(orderSku);
								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return row;

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
	// 通过id查询GoodsSku
	public GoodsSku selectGoodsSkuByGoodsId(String gskuId) throws Exception {
		GoodsSku list = null;
		list = goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(gskuId));
		return list;
	}
	
	public Goods selectBygoodsId(Integer id) throws Exception {
		Goods goods = goodsMapper.selectByPrimaryKey(id);
		return goods;
	}
	
	public Map<String, Object> selectCouponMsg(Integer shopId,String couponLogIdStr){
		Map<String, Object> map=new HashMap<>();
		if (couponLogIdStr!=null) {
			List<String> couponIdList=JsonUtils.stringToList(couponLogIdStr);
			List<Coupon> couponList=couponMapper.selectCouponByParam(shopId, couponIdList);
			if (couponList.size()>0) {
				Integer type=couponList.get(0).getCouponType();
				map.put("couponType", type);
				map.put("couponId", couponList.get(0).getId());
				map.put("catId", couponList.get(0).getCatId());
				if (type==1) {
					//使用普通卷    该商家商品价格之和上减去普通卷抵扣金额（这种情况不可能出现<0）
					map.put("amount", couponList.get(0).getAmount());
				}else if (type==2) {
					//使用免邮券  ： 该商家订单的邮费全免  
					map.put("amount", 0);
				}else if (type==3) {
					//使用红包卷   该商家商品价格之和上减去红包金额（<0的时候  让该商家的订单金额 为0）
					map.put("amount", couponList.get(0).getAmount());
				}
			}
		}
		return map;
	}
	
	public Order getOrder(Order myOrder){
		try {
			List<OrderShop> orderShopList= orderShopMapper.getByOrderId(myOrder.getId());
			Integer mainOrderPrice=0;
			Integer mainDeliveryPrice=0;
			if (orderShopList!=null && orderShopList.size()>0 && myOrder.getCouponsId()!=null && !myOrder.getCouponsId().equals("0")) {
				for (OrderShop orderShop : orderShopList) {
					int price=orderShop.getOrderPrice();
					Integer gDeliveryPrice=orderShop.getgDeliveryPrice();
					int amount=0;
					//查询该商家是否使用了优惠劵
					Map<String, Object> myCouponMap=selectCouponMsg(orderShop.getShopId(), myOrder.getCouponsId());
					if (myCouponMap!=null && myCouponMap.get("couponType")!=null && myCouponMap.get("amount")!=null && myCouponMap.get("couponId")!=null) {
						Integer couponType=(Integer) myCouponMap.get("couponType");
						amount=(int) myCouponMap.get("amount");
						switch (couponType) {
						case 1:
							//price=price-amount;
							if (price<=0) {
								price=0;
							}
							break;
						case 2:
							//price=price-gDeliveryPrice;
							amount=orderShop.getgDeliveryPrice();
							gDeliveryPrice=0;
							break;
						case 3:
							//price=price-amount;
							if (price<=0) {
								price=0;
							}
							break;
						default:
							break;
						}
					}
				
					mainOrderPrice=mainOrderPrice+price;
					mainDeliveryPrice=mainDeliveryPrice+gDeliveryPrice;
				}
			} else {
				mainOrderPrice=myOrder.getOrderPrice();
				mainDeliveryPrice=myOrder.getDeliveryPrice();
			}
			if (mainOrderPrice<=0) {
				Order myO=new Order();
				myO.setId(myOrder.getId());
				myO.setIsBeans(0);
				orderMapper.updateByPrimaryKeySelective(myO);
			}
			/*else{
				if (myOrder.getIsBeans().equals(1)) {
					//BHSeed bhSeed=simpleOrderService.getBhSeed(myOrder.getCartId(), myOrder.getmId().toString(), myOrder.getFz().toString(), 0);
					//int pri=mainOrderPrice-bhSeed.getNeedSeedNum();
					int pri=mainOrderPrice;
					if (pri <0) {
						mainOrderPrice=0;
					}else{
						mainOrderPrice=pri;
					}
				}
			}*/
			myOrder.setOrderPrice(mainOrderPrice);
			myOrder.setDeliveryPrice(mainDeliveryPrice);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return myOrder;
	}

	
	public Order getOrder1(Order myOrder){
		try {
			List<OrderShop> orderShopList= orderShopMapper.getByOrderId(myOrder.getId());
			Integer mainOrderPrice=0;
			Integer mainDeliveryPrice=0;
			if (orderShopList.size()>0 && myOrder.getCouponsId()!=null && !myOrder.getCouponsId().equals("0")) {
				for (OrderShop orderShop : orderShopList) {
					int price=orderShop.getOrderPrice();
					Integer couponId =0;
					Integer gDeliveryPrice=orderShop.getgDeliveryPrice();
					int amount=0;
					OrderShop orderShop2=new OrderShop();
					orderShop2.setId(orderShop.getId());
					
					Map<String, Object> myCouponMap=selectCouponMsg(orderShop.getShopId(), myOrder.getCouponsId());
					if (myCouponMap!=null && myCouponMap.get("couponType")!=null && myCouponMap.get("amount")!=null && myCouponMap.get("couponId")!=null) {
						Integer couponType=(Integer) myCouponMap.get("couponType");
						amount=(int) myCouponMap.get("amount");
						couponId=(int) myCouponMap.get("couponId");
						switch (couponType) {
						case 1:
							price=price-amount;
							if (price<=0) {
								price=0;
							}
							break;
						case 2:
							price=price-gDeliveryPrice;
							amount=orderShop.getgDeliveryPrice();
							gDeliveryPrice=0;
							break;
						case 3:
							price=price-amount;
							if (price<=0) {
								price=0;
							}
							break;
						default:
							break;
						}
						
					}
					
					mainOrderPrice=mainOrderPrice+price;
					mainDeliveryPrice=mainDeliveryPrice+gDeliveryPrice;
				}
			}else{
				mainOrderPrice=myOrder.getOrderPrice();
				mainDeliveryPrice=myOrder.getDeliveryPrice();
			}
			if (mainOrderPrice<=0) {
				Order myO=new Order();
				myO.setId(myOrder.getId());
				myO.setIsBeans(0);
				orderMapper.updateByPrimaryKeySelective(myO);
			}
			else{
				if (myOrder.getIsBeans().equals(1)) {
					BHSeed bhSeed=simpleOrderService.getBhSeed(myOrder.getCartId(), myOrder.getmId().toString(), myOrder.getFz().toString(), 0);
					int pri=mainOrderPrice-bhSeed.getNeedSeedNum();
					if (pri <0) {
						mainOrderPrice=0;
					}else{
						mainOrderPrice=pri;
					}
				}
			}
			myOrder.setOrderPrice(mainOrderPrice);
			myOrder.setDeliveryPrice(mainDeliveryPrice);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return myOrder;
	}
	
	
	/**
	 * @Description: 优惠卷抵扣金额(在使用优惠劵的前提下)
	 * @author 程凤云
	 * @date 2018年06月13日
	 * @param ouponType:优惠劵的类型:1普通券(满减)，2免邮券，3红包券
	 * @param catId:分类id
	 * @param couponPrice:优惠劵的金额
	 * @param orderShopId:优惠劵的类型:1普通券(满减)，2免邮券，3红包券
	 * @param shopId:商家的id
	 * @param logId:couponLog表的id
	 * @param gDeliveryPrice:邮费
	 */
	public void couponMony(Integer ouponType,Long catId,Integer couponPrice,Integer orderShopId,Integer shopId,Integer logId,Integer gDeliveryPrice,Integer orderPrice){
		try {
			/***
			 * 优惠券售后：
					1、优惠券使用过后无法第二次使用。也就是说售后不退优惠券，无论是订单售后还是商品售后都不退优惠券。
					2、原订单使用了优惠手段（退款金额将以商品金额按比例扣除活动优惠后的小计金额退回）
					3、优惠手段包括：满减、打折活动、优惠券、红包、免邮券等。
					4、例子：专场满减：满400-100：订单商品A=100元，商品B=300元。
					     商品A退货金额：100*（100/400）=75
					     商品B退货金额：300*（300/400）=225
			 * 
			 * 
			 * */
			//所有商品抵扣的金额
			int allGoodsPrice=0;
			List<Coupon> myCouponList=new ArrayList<>();
			if(ouponType==1){
				/***
				 * 满减优惠券售后：例子：专场满减：满400-100：订单商品A=100元，商品B=300元，商品总金额：100+300=400。
					商品a售后退款金额：商品a金额-（满减金额 * （商品a/商品总金额））=100-（100*1/4）=75
				 * */
				//普通卷（满减劵）
				if(catId==0){
					//该商家通用劵
					//该商家订单所有商品的价格（减去滨惠豆）
					allGoodsPrice=couponMapper.selectSkuPriceByOShopId(shopId,orderShopId);
					myCouponList=couponMapper.selectSkuListByOShopId(shopId, orderShopId);
				}else{
					//该商家某分类的劵
					allGoodsPrice=couponMapper.selectSkuPriceByCatId(shopId, logId, orderShopId, catId);
					myCouponList=couponMapper.selectSkuListByCatId(shopId, logId, orderShopId, catId);
				}
				if(allGoodsPrice>0){
					if (myCouponList.size()>0) {
						for (Coupon coupon : myCouponList) {
							int realMoney=coupon.getType()-coupon.getAmount();//减去滨惠豆的商品金额
							//deductionPrice=(单价*数量/sum(单价*数量)) * 优惠劵劵的金额
							double deductionPrice=((double)realMoney/allGoodsPrice)*couponPrice;//(该商品的价格/参与抵扣的所有商品的金额 )*总抵扣金额 
							DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数  		
							int p =Integer.parseInt(df.format(deductionPrice));
							OrderSku orderSku=new OrderSku();
							orderSku.setId(coupon.getId());
							orderSku.setCouponPrice(p);
							orderSkuMapper.updateByPrimaryKeySelective(orderSku);
						}
					}
					
				}
			}
			//红包劵
			if(ouponType==3){
				/**
				 *	2018-06-14 15:23:06, 由 林惠玲 添加备注。
				 * 	所有商品金额-红包>0 时    优惠卷抵扣金额 为：（该商品/总商品金额）*红包
				 * 	所有商品金额-红包<=0时   优惠卷抵扣金额 :改商品价格
				 **/
				//该商家订单所有商品的价格(不包含邮费,计算公式=sum(单价*数量-滨惠豆抵扣金额))
				allGoodsPrice=couponMapper.selectSkuPriceByOShopId(shopId,orderShopId);
				myCouponList=couponMapper.selectSkuListByOShopId(shopId, orderShopId);
				int cha=allGoodsPrice-couponPrice;
				/********************方法1************************/
				if(cha>0){//所有商品金额-红包>0 时
					for (Coupon coupon : myCouponList) {
						int realMoney=coupon.getType()-coupon.getAmount();//减去滨惠豆的商品金额
						double deductionPrice=((double)realMoney/allGoodsPrice)*couponPrice;
						DecimalFormat df = new DecimalFormat("######0"); // 四色五入转换成整数
						OrderSku orderSku = new OrderSku();
						orderSku.setId(coupon.getId());
						int p = Integer.parseInt(df.format(deductionPrice));
						orderSku.setCouponPrice(p);//抵扣金额：（该商品/总商品金额）*红包金额
						orderSkuMapper.updateByPrimaryKeySelective(orderSku);
					}
				}else{//所有商品金额-红包<=0时 
					for (Coupon coupon : myCouponList) {
						OrderSku orderSku = new OrderSku();
						orderSku.setId(coupon.getId());
						orderSku.setCouponPrice(coupon.getType());//抵扣金额为商品金额
						orderSkuMapper.updateByPrimaryKeySelective(orderSku);
					}
				}
				/********************方法2************************/
			/*	int realPrice =0;//实际支付价格
				if(cha>0){
					realPrice =cha;
				}
				for (Coupon coupon : myCouponList) {
					double deductionPrice = ((double) coupon.getType() / allGoodsPrice) * realPrice;// 该商品的价格/参与抵扣的所有商品的金额
					DecimalFormat df = new DecimalFormat("######0"); // 四色五入转换成整数
					OrderSku orderSku = new OrderSku();
					orderSku.setId(coupon.getId());
					int myP=allGoodsPrice-Integer.parseInt(df.format(deductionPrice));
					if(myP<0){
						myP=0;
					}
					orderSku.setCouponPrice(myP);//抵扣金额为商品金额
					orderSkuMapper.updateByPrimaryKeySelective(orderSku);
				}*/
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 计算skuPayPrice的值
	 * @author 程凤云
	 * @date 2018年06月14日
	 * @param orderId:订单的id(也就是order_main表的id)
	 */
	public void updatePayPrice(Integer orderId){
		try {
			List<OrderShop> orderShopList=orderShopMapper.selectByOrderId(orderId);
			if (orderShopList.size()>0) {
				Integer orderMainPayPrice=0;
				Order myOrder=new Order();
				myOrder.setId(orderId);
				for (OrderShop orderShop : orderShopList) {
					Integer orderShopSkuPayPrice=0;
					List<OrderSku> orderSkuList=orderSkuMapper.getSkuListByOrderShopId(orderShop.getId());
					if (orderSkuList.size()>0) {
						for (OrderSku orderSku : orderSkuList) {
							OrderSku myOrderSku=new OrderSku();
							myOrderSku.setId(orderSku.getId());
							Integer skuPayPrice=orderSku.getSkuNum()*orderSku.getSkuSellPriceReal()-orderSku.getSavePrice()-orderSku.getCouponPrice();
							if (skuPayPrice<=0) {
								skuPayPrice=0;
							}
							myOrderSku.setSkuPayPrice(skuPayPrice);
							orderShopSkuPayPrice=orderShopSkuPayPrice+myOrderSku.getSkuPayPrice();
							orderMainPayPrice=orderMainPayPrice+myOrderSku.getSkuPayPrice();
							orderSkuMapper.updateByPrimaryKeySelective(myOrderSku);
						}
					}
					OrderShop myOrderShop=new OrderShop();
					myOrderShop.setId(orderShop.getId());
					myOrderShop.setSkuPayPrice(orderShopSkuPayPrice);
					orderShopMapper.updateByPrimaryKeySelective(myOrderShop);
				}
				myOrder.setSkuPayPrice(orderMainPayPrice);
				orderMapper.updateByPrimaryKeySelective(myOrder);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
