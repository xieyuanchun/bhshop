package com.order.user.service.impl;


import com.bh.config.Contants;
import com.bh.goods.mapper.*;
import com.bh.goods.pojo.*;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.Track;
import com.bh.order.mapper.*;
import com.bh.order.pojo.*;
import com.bh.queryLogistics.QueryLogistics;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.StatusNameUtils;
import com.order.user.service.JDOrderService;
import com.order.user.service.SimpleOrderService;
import com.order.user.service.UserOrderService;
import com.order.user.service.UserSubmitOrderService;
import com.print.controller.HttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class SimpleOrderServiceImpl implements SimpleOrderService {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOrderServiceImpl.class);
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
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private TopicGoodsMapper topicGoodsMapper;
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private TopicDauctionPriceMapper topicDauctionPriceMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private HollandDauctionLogMapper hollandDauctionLogMapper;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Autowired
	private UserSubmitOrderService userSubmitOrderService;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Autowired
	private HollandDauctionLogMapper logMapper;
	@Autowired
	private ActZoneGoodsMapper actZoneGoodsMapper;
	@Autowired
	private ActZoneMapper actZoneMapper;
	@Autowired
	private BargainRecordMapper  bargainRecordMapper;
	@Autowired
	private  JdOrderMainMapper jdOrderMainMapper;
	@Autowired
	private  JdOrderSkuMapper jdOrderSkuMapper;
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private JdOrderTrackMapper jdOrderTrackMapper;
	@Autowired
	private AspUserGuessMapper aspUserGuessMapper;
	@Autowired
	private ActivityTimeMapper activityTimeMapper;
	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;
	@Autowired
	private BhDictItemMapper bhDictItemMapper;

	

	public int insertAddress(String addressId) throws Exception {
		int row = 0;
		if (StringUtils.isNotEmpty(addressId)) {
			MemberUserAddress address = memberUserAddressMapper.selectByPrimaryKey(Integer.parseInt(addressId));
			if (address != null) {
				MemberUserAddress newAdd = new MemberUserAddress();
				newAdd.setmId(address.getmId());
				newAdd.setFullName(address.getFullName());
				newAdd.setProv(address.getProv());
				newAdd.setCity(address.getCity());
				newAdd.setArea(address.getArea());
				newAdd.setAddress(address.getAddress());
				newAdd.setTel(address.getTel());
				newAdd.setIsDefault(false);
				newAdd.setProvname(address.getProvname());
				newAdd.setCityname(address.getCityname());
				newAdd.setAreaname(address.getAreaname());
				// 0未删除，1已删除
				newAdd.setIsDel(1);
				// 该地地址是否用于一键购的地址，0用，1不用2.订单的地址
				newAdd.setEasybuy(2);
				newAdd.setLabel(address.getLabel());
				newAdd.setTelephone(address.getTelephone());
				newAdd.setIsJd(address.getIsJd());
				newAdd.setFour(address.getFour());
				newAdd.setFourname(address.getFourname());
				memberUserAddressMapper.insertSelective(newAdd);
				row = newAdd.getId();
			}
		}
		return row;
	}

	
	@SuppressWarnings("unchecked")
	public List<OrderGoodsCartListShopIdList> getOrderGoodsCart(List<String> ids, List<GoodsCart> cartList, String fz,
			String teamNo, int mId) {
		List<OrderGoodsCartListShopIdList> list2 = goodsCartMapper.selectGoodsCartListShopIdList(ids);
		try {
			if (list2.size() > 0) {

				for (OrderGoodsCartListShopIdList goodsCartListShopIdList : list2) {
					// 该商家的价格
					Integer shopPrice = 0;
					// 该商家的数量
					Integer goodsNum = 0;
					List<OrderCartGoodsList> goodList = new ArrayList<>();
					List<GoodsSku> skuList = goodsCartMapper.selectCartGoodsList1(ids,
							goodsCartListShopIdList.getShopId());
					// xieyc add
					List<Integer> goodsList = new ArrayList<Integer>();// 保存某个商家订单全部goodsId
																		// 某个商家订单全部goodsId
																		// xieyc
																		// //
																		// add
					Set<Integer> goodsIdSet = new HashSet<>();
					for (GoodsSku goodsSku : skuList) {
						goodsIdSet.add(goodsSku.getGoodsId());
						Goods goodsXyc = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());// xiey
																								// add
						goodsList.add(goodsXyc.getId());// xieyc add
						OrderCartGoodsList good = new OrderCartGoodsList();
						if (StringUtils.isNotEmpty(goodsSku.getValue())) {
							Object value = JsonUtils.stringToObject(goodsSku.getValue());
							Map<String, Object> m = new HashMap<>();
							m.put("valueObj", value);
							good.setGoodsSkus(m);
							org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
							org.json.JSONArray personList = jsonObj.getJSONArray("url");
							good.setgImage((String) personList.get(0));
						}
						double sellPice = 0;
						Integer p = 0;
						if (StringUtils.isNotEmpty(fz)) {// 如果fz不为空，价格取拼单的价格
							goodsCartListShopIdList.setFz(Integer.parseInt(fz));
							if (Integer.parseInt(fz) == 5) {
								sellPice = (double) userOrderService.returnDauPrice(goodsSku.getGoodsId()) / 100;
								p = userOrderService.returnDauPrice(goodsSku.getGoodsId());
							} else {
								sellPice = (double) goodsSku.getTeamPrice() / 100;
								p = goodsSku.getTeamPrice();
							}
						} else {
							sellPice = (double) goodsSku.getSellPrice() / 100;
							p = goodsSku.getSellPrice();
						}
						good.setCatIdOne(goodsXyc.getCatIdOne());// 一级分类 xieyc
																	// add
						good.setNum(goodsSku.getMinimum());
						good.setRealsellPrice(sellPice);
						good.setGoodName(goodsSku.getGoodsName());
						goodList.add(good);
						shopPrice = shopPrice + goodsSku.getMinimum() * p;
						goodsNum = goodsNum + goodsSku.getMinimum();
					}
					/**
					 * 邮费规则:2018-5-21 滨惠商城跟会员收取的邮费标准是：（跟京东一模一样）
					 * 订单金额＜49元，收取基础运费8元，不收续重运费； 49元≤订单金额＜99元，收取基础运费6元，不收续重运费；
					 * 订单金额≥99元，免基础运费，不收续重运费。
					 */

					goodsCartListShopIdList.setGoodsCartLists(goodList);
					goodsCartListShopIdList.setTeamNo(teamNo);
					goodsCartListShopIdList.setNum(goodsNum);
					goodsCartListShopIdList.setPrice(RegExpValidatorUtils.formatdouble((double) shopPrice / 100));

					Integer shopDeliveryPrice = 0;
					if (shopPrice >= 9900 || "1".equals(fz) || "6".equals(fz)) {
						// 1 >99 2 如果是拼单的商品，邮费为0; 3 如果是免费兑换商品，邮费为0
						shopDeliveryPrice = 0;
					} else {
						// 周末滨惠日和超级滨惠豆免邮
						List<Integer> isFreeList = actZoneGoodsMapper.selectIsFreePostage(goodsIdSet);
						if (isFreeList != null) {
							if (isFreeList.size() > 0 && isFreeList.size() == 1 && "1".equals(isFreeList.get(0)+"")) {
								shopDeliveryPrice = 0;
							} else {
								if (shopPrice < 4900) {
									shopDeliveryPrice = Contants.price1;
								} else if ((shopPrice < 9900) && (shopPrice >= 4900)) {
									shopDeliveryPrice = Contants.price2;
								}
							}
						} else {
							if (shopPrice < 4900) {
								shopDeliveryPrice = Contants.price1;
							} else if ((shopPrice < 9900) && (shopPrice >= 4900)) {
								shopDeliveryPrice = Contants.price2;
							}
						}
					}
					if ("6".equals(fz)) { // 免费兑换商品
						shopPrice = 0;
					}

					goodsCartListShopIdList
							.setDeliveryPrice(RegExpValidatorUtils.formatdouble((double) shopDeliveryPrice / 100));
					Integer allPrice = shopDeliveryPrice + shopPrice;
					goodsCartListShopIdList.setTotalPrice(RegExpValidatorUtils.formatdouble((double) allPrice / 100));

					/**************** xieyc 优惠劵逻辑 start ***********************/
					double deliPrice = RegExpValidatorUtils.formatdouble((double) shopDeliveryPrice / 100);// 邮费
					int shopId = goodsCartListShopIdList.getShopId();// 商家Id
					List<UsableCoupon> usableCouponList = new ArrayList<>();
					if (goodsList.size() == 1) {
						Goods goodsXyc = goodsMapper.selectByPrimaryKey(goodsList.get(0));
						if (goodsXyc.getSaleType() != 3 && goodsXyc.getTopicType() != 6) {// 不是拼团和不是拍卖
							int row=0;
							//查询该商品所在的所有专区
							List<ActZoneGoods> listActZone = actZoneGoodsMapper.selectIsCoupon(goodsXyc.getId());//超级滨惠豆和周末大放送也不允许使用优惠劵
							for (ActZoneGoods actZoneGoods : listActZone) {
								ActZone actZone=actZoneMapper.selectByPrimaryKey(actZoneGoods.getZoneId());
								int i=1;
								while(i!=0){//找到该专区对应的最高级专区（死循环）
									if(actZone.getReid()!=0){
										actZone=actZoneMapper.selectByPrimaryKey(actZone.getReid());
									}else{
										break;//跳出死循环
									}
								}
								if(actZone.getIsCoupon()==0){//判断最高级专区是否可以用优惠劵（只要有一个专区不能使用那么他不能使用优惠劵）
									row=1;
									break;
								}
							}
							if (row == 0) { // 商品不在专区中 则可以用优惠劵。 或者 在专区中其 对应的专区可以用优惠劵
								usableCouponList = this.couponList(goodsList, mId, deliPrice, goodsCartListShopIdList,
										shopId);// 获取可以用的优惠卷
							}
						}
					} else if (goodsList.size() > 1) {// >1的时候 肯定是从购物车过来的(拼团、拍卖、超级滨惠豆、周末大放送都不可以加入购物车)
						usableCouponList = this.couponList(goodsList, mId, deliPrice, goodsCartListShopIdList, shopId);// 获取可以用的优惠卷
					}

					goodsCartListShopIdList.setUsableCouponList(usableCouponList);

					/**************** xieyc 优惠劵逻辑 end ***********************/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list2;

	}
	
	
	

	/**
	 * 查看用户的滨惠豆 isCart:0从购物车过来，1从订单过来
	 */
	//获取商品数量  fjh 2018.7.23 add 
	public BHSeed getBhSeed1(String goodsCartIds, String mId, String fz, Integer isCart,List<GoodsSku> list) throws Exception {
		BHSeed bhSeed = new BHSeed();
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(Integer.parseInt(mId));
		// //0不允许使用滨惠豆下单，1允许
		bhSeed.setIsAllow(0);
		// 该商品需要的滨惠豆
		bhSeed.setNeedSeedNum(0);
		// 用户有的滨惠豆
		bhSeed.setOwnSeedNum(0);
		bhSeed.setIsMustOpen(0);
		if (StringUtils.isNotEmpty(goodsCartIds)) {
			List<String> gIds = JsonUtils.stringToList(goodsCartIds);
			// 如果允许使用滨惠豆(如果总价格>可抵扣的滨惠豆)
			int score =0;
			for (int i=0;i< gIds.size();i++) {
				int num=list.get(i).getMinimum();
				int goodsId=Integer.valueOf(gIds.get(i));
				score=score+goodsCartMapper.selectSumSkuScore1(goodsId,num);
			}
			if (score > 0) {
				bhSeed.setIsAllow(1);
				bhSeed.setNeedSeedNum(score);
				bhSeed.setOwnSeedNum(memberUser.getPoint());
				bhSeed.setIsMustOpen(0);
				if(bhSeed.getOwnSeedNum()>bhSeed.getNeedSeedNum()) {
					bhSeed.setNeedSeedNum(bhSeed.getNeedSeedNum());
				}else {
					bhSeed.setNeedSeedNum(bhSeed.getOwnSeedNum());
				}
				// 判断按钮是否一定开启
				if (gIds.size() > 0) {
					List<Integer> goodsIdSet = goodsCartMapper.selectGoodsId(gIds);
					Set<Integer> mySet = new HashSet<>();
					for (Integer integer : goodsIdSet) {
						mySet.add(integer);
					}
					try {
						// 周末滨惠日和超级滨惠豆的按钮需要开启
						List<Integer> isLockStoreList = actZoneGoodsMapper.selectIsLockScore(mySet);
						if (isLockStoreList.size() > 0 && isLockStoreList.size() == 1
								&& "1".equals(isLockStoreList.get(0)+"")) {
							bhSeed.setIsMustOpen(1);
							bhSeed.setNeedSeedNum(score);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bhSeed;
	}
	
	
	/**
	 * 查看用户的滨惠豆 isCart:0从购物车过来，1从订单过来
	 */
	//获取商品数量  fjh 2018.7.23 add 
	public BHSeed getBhSeed2(String goodsCartIds, String mId, String fz, Integer isCart,List<OrderSku> list) throws Exception {
		BHSeed bhSeed = new BHSeed();
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(Integer.parseInt(mId));
		// //0不允许使用滨惠豆下单，1允许
		bhSeed.setIsAllow(0);
		// 该商品需要的滨惠豆
		bhSeed.setNeedSeedNum(0);
		// 用户有的滨惠豆
		bhSeed.setOwnSeedNum(0);
		bhSeed.setIsMustOpen(0);
		if (StringUtils.isNotEmpty(goodsCartIds)) {
			List<String> gIds = JsonUtils.stringToList(goodsCartIds);
			// 如果允许使用滨惠豆(如果总价格>可抵扣的滨惠豆)
			int score =0;
			for (int i=0;i< gIds.size();i++) {
				int num=list.get(i).getSkuNum();
				int goodsId=Integer.valueOf(gIds.get(i));
				score=score+goodsCartMapper.selectSumSkuScore1(goodsId,num);
			}
			if (score > 0) {
				bhSeed.setIsAllow(1);
				bhSeed.setNeedSeedNum(score);
				bhSeed.setOwnSeedNum(memberUser.getPoint());
				bhSeed.setIsMustOpen(0);
				if(bhSeed.getOwnSeedNum()>bhSeed.getNeedSeedNum()) {
					bhSeed.setNeedSeedNum(bhSeed.getNeedSeedNum());
				}else {
					bhSeed.setNeedSeedNum(bhSeed.getOwnSeedNum());
				}
				// 判断按钮是否一定开启
				if (gIds.size() > 0) {
					List<Integer> goodsIdSet = goodsCartMapper.selectGoodsId(gIds);
					Set<Integer> mySet = new HashSet<>();
					for (Integer integer : goodsIdSet) {
						mySet.add(integer);
					}
					try {
						// 周末滨惠日和超级滨惠豆的按钮需要开启
						List<Integer> isLockStoreList = actZoneGoodsMapper.selectIsLockScore(mySet);
						if (isLockStoreList.size() > 0 && isLockStoreList.size() == 1
								&& "1".equals(isLockStoreList.get(0)+"")) {
							bhSeed.setIsMustOpen(1);
							bhSeed.setNeedSeedNum(score);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bhSeed;
	}

	public BHSeed getBhSeed(String goodsCartIds, String mId, String fz, Integer isCart) throws Exception {
		BHSeed bhSeed = new BHSeed();
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(Integer.parseInt(mId));
		// //0不允许使用滨惠豆下单，1允许
		bhSeed.setIsAllow(0);
		// 该商品需要的滨惠豆
		bhSeed.setNeedSeedNum(0);
		// 用户有的滨惠豆
		bhSeed.setOwnSeedNum(0);
		bhSeed.setIsMustOpen(0);
		if (StringUtils.isNotEmpty(goodsCartIds)) {
			List<String> gIds = JsonUtils.stringToList(goodsCartIds);
			int score=0;
			if(!fz.equals("1")) {
			for (int i=0;i< gIds.size();i++) {
				String cartId=gIds.get(i);
				GoodsCart goodsCart=goodsCartMapper.selectByPrimaryKey(Integer.valueOf(cartId));
				int num=goodsCart.getNum();
				int goodsId=Integer.valueOf(gIds.get(i));
				score=score+goodsCartMapper.selectSumSkuScore1(goodsId,num);
			}
			if (score > 0) {
				bhSeed.setIsAllow(1);
				bhSeed.setNeedSeedNum(score);
				bhSeed.setOwnSeedNum(memberUser.getPoint());
				if(bhSeed.getOwnSeedNum()>bhSeed.getNeedSeedNum()) {
					bhSeed.setNeedSeedNum(bhSeed.getNeedSeedNum());
				}else {
					bhSeed.setNeedSeedNum(bhSeed.getOwnSeedNum());
				}
				// 判断按钮是否一定开启
				/*if (gIds.size() > 0) {
					List<Integer> goodsIdSet = goodsCartMapper.selectGoodsId(gIds);
					Set<Integer> mySet = new HashSet<>();
					for (Integer integer : goodsIdSet) {
						mySet.add(integer);
					}
					try {
						// 周末滨惠日和超级滨惠豆的按钮需要开启
						List<Integer> freePostageList = actZoneGoodsMapper.selectIsLockScore(mySet);
						if (freePostageList.size() > 0 && freePostageList.size() == 1
								&& "1".equals(freePostageList.get(0)+"")) {
							bhSeed.setNeedSeedNum(score);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}*/
			}
			}else{
				for (int i=0;i< gIds.size();i++) {
					String cartId=gIds.get(i);
					GoodsCart goodsCart=goodsCartMapper.selectByPrimaryKey(Integer.valueOf(cartId));
					int num=goodsCart.getNum();
					int goodsId=Integer.valueOf(gIds.get(i));
					score=score+goodsCartMapper.selectSumSkuScore2(goodsId,num);
				}
				if (score > 0) {
					bhSeed.setIsAllow(1);
					bhSeed.setNeedSeedNum(score);
					bhSeed.setOwnSeedNum(memberUser.getPoint());
					if(bhSeed.getOwnSeedNum()>bhSeed.getNeedSeedNum()) {
						bhSeed.setNeedSeedNum(bhSeed.getNeedSeedNum());
					}else {
						bhSeed.setNeedSeedNum(bhSeed.getOwnSeedNum());
					}
					// 判断按钮是否一定开启
					/*if (gIds.size() > 0) {
						List<Integer> goodsIdSet = goodsCartMapper.selectGoodsId(gIds);
						Set<Integer> mySet = new HashSet<>();
						for (Integer integer : goodsIdSet) {
							mySet.add(integer);
						}
						try {
							// 周末滨惠日和超级滨惠豆的按钮需要开启
							List<Integer> freePostageList = actZoneGoodsMapper.selectIsLockScore(mySet);
							if (freePostageList.size() > 0 && freePostageList.size() == 1
									&& "1".equals(freePostageList.get(0)+"")) {
								bhSeed.setNeedSeedNum(score);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}*/
				}
			}
		}
		return bhSeed;
	}
	
	// 程凤云 2018-4-12判断拍卖商品是否过了活动时间如果为0代表已过时间
	public int isOverTopicTime(Integer orderId) throws Exception {
		int row = 0;
		List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(orderId);
		if (orderSkuList.size() > 0) {
			List<TopicGoods> topicGoodsList = topicGoodsMapper.selectByGoodsId(orderSkuList.get(0).getGoodsId());
			if (topicGoodsList.size() > 0) {
				row = 1;
				// 拍卖商品没过期，可以购买，把活动结束时间改为当前时间 2018.5.8 zlk
				// Topic t =
				// topicMapper.selectByPrimaryKey(topicGoodsList.get(0).getActId());
				// t.setEndTime(new Date());
				// topicMapper.updateByPrimaryKeySelective(t);
				// end
			} else {
				row = 0;
			}
		}
		return row;
	}

	/** 在购物车列表中点击‘去结算’ */
	public OrderCleanAccount selecOnePreOrderInfo(String goodsCartIds, String mId, String fz, String teamNo)
			throws Exception {
		OrderCleanAccount cleanAccount = new OrderCleanAccount();
		try {
		
			
			MemberUserAddress address = new MemberUserAddress();
			address.setmId(Integer.parseInt(mId));
			List<MemberUserAddress> addre = memberUserAddressMapper.selectAllBymId(Integer.parseInt(mId));// 根据mId查找地址
			cleanAccount.setOrderCartIds(goodsCartIds);
			List<String> goodsCartId = JsonUtils.stringToList(goodsCartIds);
			List<GoodsCart> goodsCartsList = goodsCartMapper.selectCoodsCartByIdsandmId(goodsCartId);
			// 前端取的是list里面的价格与邮费
			List<OrderGoodsCartListShopIdList> list = getOrderGoodsCart(goodsCartId, goodsCartsList, fz, teamNo,
					Integer.parseInt(mId));
			
		   //获取商品数量  fjh 2018.7.23 add 
			List<GoodsSku> list2 = getOrderGoodsCart2(goodsCartId);
			
			
			// 商品的金额，不包含邮费
			double count = 0;
			// 商品的购买数量
			int num = 0;
			// 商品的邮费
			double deliPrice = 0;
			for (OrderGoodsCartListShopIdList mySimpleList : list) {
				count = count + mySimpleList.getPrice();
				num = num + mySimpleList.getNum();
				deliPrice = deliPrice + mySimpleList.getDeliveryPrice();
			}

			List<UsableCoupon> usableCouponListAll = new ArrayList<>();
			for (OrderGoodsCartListShopIdList orderGoodsCartListShopIdList : list) {
				List<UsableCoupon> usableCouponList = orderGoodsCartListShopIdList.getUsableCouponList();
				if (usableCouponList != null) {
					usableCouponListAll.addAll(usableCouponList);
				}
			}
			cleanAccount.setUsableCouponListAll(usableCouponListAll);// 将平台和商家可以用的优惠卷都放到这里

			// isCart:0从购物车过来，1从订单过来
			BHSeed bhSeed = getBhSeed(goodsCartIds, mId, fz, 0);
			cleanAccount.setPrice(count);
			cleanAccount.setDeliveryPrice(deliPrice);
			cleanAccount.setCartIds(goodsCartIds);
			cleanAccount.setGoodsCartsList(list);
			cleanAccount.setGoodsNum(num);
			cleanAccount.setTotalCount(RegExpValidatorUtils.formatdouble(deliPrice + count));
			cleanAccount.setUserAddressesList(addre);
			cleanAccount.setBhSeed(bhSeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cleanAccount;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GoodsSku> getOrderGoodsCart2(List<String> ids) {
		    List<OrderGoodsCartListShopIdList> list2 = goodsCartMapper.selectGoodsCartListShopIdList(ids);
		    List<GoodsSku> skuList=null;
		    List list=new ArrayList<>();
			try {
				if (list2.size() > 0) {
					
					for (OrderGoodsCartListShopIdList goodsCartListShopIdList : list2) {
						skuList = goodsCartMapper.selectCartGoodsList1(ids,goodsCartListShopIdList.getShopId());
						list.addAll(skuList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return list;

	}

	/**
	 * @Description: 用户可以使用的优惠卷
	 * @author xieyc
	 * @date 2018年6月5日 下午3:23:28
	 */
	public List<UsableCoupon> couponList(List<Integer> goodsIdList, int mId, double deliPrice,
			OrderGoodsCartListShopIdList goodsCartListShopIdList, int shopId) {
		List<UsableCoupon> returnList = new ArrayList<UsableCoupon>();

		CouponLog findCouponLogFree = new CouponLog();// 查询条件
		findCouponLogFree.setmId(mId);
		findCouponLogFree.setExpireTime(new Date());
		findCouponLogFree.setShopId(shopId);
		List<CouponLog> listFree = couponLogMapper.getFreeOrRedCoupon(findCouponLogFree);// 查询某个用户拥有某个商家的全部有效红包劵和免邮劵
		for (CouponLog couponLog : listFree) {
			Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());// 优惠劵
			if (deliPrice == 0) {// 邮费为的0的时候 不显示免邮劵
				if (coupon.getCouponType() == 2) {
					continue;
				}
			}
			UsableCoupon usableCoupon = new UsableCoupon();
			this.returnCoupon(couponLog, coupon, usableCoupon);
			returnList.add(usableCoupon);
		}
		List<Goods> goodsList = goodsMapper.getGoodByIdsList(goodsIdList);// 某个商家订单全部的goods

		Map<Long, List<Goods>> linkedMap = new LinkedHashMap<Long, List<Goods>>(); // 最终要的结果(根据分类id将商品进行分组)
		for (Goods goods : goodsList) {
			long catIdOne = goods.getCatIdOne();// 一级分类id
			if (linkedMap.containsKey(catIdOne)) {
				linkedMap.get(catIdOne).add(goods);
			} else {
				List<Goods> listGoods = new ArrayList<Goods>();
				listGoods.add(goods);
				linkedMap.put(catIdOne, listGoods);
			}
		}
		for (Long catId : linkedMap.keySet()) {
			double total = 0;
			List<OrderCartGoodsList> goodsCartLists = goodsCartListShopIdList.getGoodsCartLists();
			for (OrderCartGoodsList orderCartGoodsList : goodsCartLists) {
				if (orderCartGoodsList.getCatIdOne().equals(catId)) {// 计算某个分类的商品总价
					double price = orderCartGoodsList.getRealsellPrice() * orderCartGoodsList.getNum();
					total += price;
				}
			}
			// 查询某个用户是否有普通卷
			List<CouponLog> listPt = couponLogMapper.getPtCouponWithCatId(new Date(), mId, catId, shopId);// 查询某用户拥有某个商家某中分类的满减卷
			for (CouponLog couponLog : listPt) {
				Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
				double needAmount = (double) coupon.getNeedAmount() / 100;// 商家某个分类的商品总价超过此才可以使用本优惠券
				if (total >= needAmount) {// 筛选优惠劵
					UsableCoupon usableCoupon = new UsableCoupon();
					this.returnCoupon(couponLog, coupon, usableCoupon);
					returnList.add(usableCoupon);
				}
			}
		}
		double total1 = 0;// 改商家的的商品总价格
		List<OrderCartGoodsList> goodsCartLists = goodsCartListShopIdList.getGoodsCartLists();
		for (OrderCartGoodsList orderCartGoodsList : goodsCartLists) {
			double price = orderCartGoodsList.getRealsellPrice() * orderCartGoodsList.getNum();
			total1 += price;

		}
		List<CouponLog> listCommon = couponLogMapper.getPtCouponWithCatId(new Date(), mId, 0l, shopId);// 查询某用户拥有某个商家普通卷
		for (CouponLog couponLog : listCommon) {
			Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
			double needAmount = (double) coupon.getNeedAmount() / 100;// 商家某个分类的商品总价超过此才可以使用本优惠券
			if (total1 >= needAmount) {// 筛选优惠劵
				UsableCoupon usableCoupon = new UsableCoupon();
				this.returnCoupon(couponLog, coupon, usableCoupon);
				returnList.add(usableCoupon);
			}
		}
		return returnList;
	}

	/**
	 * @Description: 处理返回的优惠卷数据
	 * @author xieyc
	 * @date 2018年6月6日 下午12:07:50
	 */
	public void returnCoupon(CouponLog couponLog, Coupon coupon, UsableCoupon usableCoupon) {
		int shopId = couponLog.getShopId();
		if (shopId == 0) {
			shopId = 1;
		}
		MemberShop shop = memberShopMapper.selectByPrimaryKey(shopId);// 店铺信息
		usableCoupon.setCouponType(coupon.getCouponType());// 优惠劵类型
		usableCoupon.setId(couponLog.getId());// 优惠劵id
		usableCoupon.setAmount((double) coupon.getAmount() / 100);// 优惠金额
		usableCoupon.setShopName(shop.getShopName());// 商家名字
		usableCoupon.setTitle(coupon.getTitle());// 优惠劵标题
		usableCoupon.setType(strType(coupon.getCouponType()));// 优惠劵
		long difference = (couponLog.getExpireTime().getTime() - new Date().getTime()) / 86400000;// 计算过期时间天数
		if (Math.abs(difference) > 10000) {// 永久劵的默认生成的过期时候是领取后的36000天
			usableCoupon.setEffectiveTime("永久有效");
		} else {
			StringBuffer sb = new StringBuffer();// 过期时间的拼凑
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(couponLog.getExpireTime());
			int year = calendar.get(Calendar.YEAR);// 年
			int month = calendar.get(Calendar.MONTH) + 1;// 月
			int day = calendar.get(Calendar.DAY_OF_MONTH);// 日
			int hour = calendar.get(Calendar.HOUR_OF_DAY);// 时
			int minute = calendar.get(Calendar.MINUTE);// 分
			sb.append(year + "年");
			if (month > 9) {
				sb.append(month + "月");
			} else {
				sb.append("0" + month + "月");
			}
			if (day > 9) {
				sb.append(day + "日 ");
			} else {
				sb.append("0" + day + "日 ");
			}
			if (hour > 9) {
				sb.append(hour + ":");
			} else {
				sb.append("0" + hour + ":");
			}
			if (minute > 9) {
				sb.append(minute + " ");
			} else {
				sb.append("0" + minute + " ");
			}
			sb.append("过期");
			usableCoupon.setEffectiveTime(sb.toString());
		}
		if (coupon.getCatId() == 0) {
			usableCoupon.setApplyStr("适用所有商品");
		} else {
			GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(coupon.getCatId());
			usableCoupon.setApplyStr("适用" + goodsCategory.getName() + "商品");
		}
	}

	/**
	 * @Description: 优惠劵类型处理
	 * @author xieyc
	 * @date 2018年6月6日 下午5:05:19
	 */
	// TODO
	public String strType(int type) {
		String strType = null;
		switch (type) {
		case 1:
			strType = "普通券";
			break;
		case 2:
			strType = "免邮券";
			break;
		case 3:
			strType = "红包券";
			break;

		default:
			strType = "错误类型";
			break;
		}
		return strType;
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

	/************* 2018-5-19 星期五， 订单取消后再次购买 ****************************/
	public OrderCleanAccount orderCancleBuy(OrderShop orderShop, String fz) throws Exception {
		OrderCleanAccount cleanAccount = new OrderCleanAccount();
		/***************** xieyc 荷兰式逻辑添加 start ********************/
		Order orderXyc = orderMapper.selectByPrimaryKey(orderShop.getOrderId());// 订单
		List<MemberUserAddress> addre = new ArrayList<>();
		if (orderXyc.getFz() == 5) {
			try {
				BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(orderXyc.getOrderNo());//拍卖记录
				if (bargainRecord != null) {
					// 获取某个用户某个商品的某一期交纳的保证金是多少
					CashDeposit findCashDeposit = new CashDeposit();// 查询条件
					findCashDeposit.setmId(bargainRecord.getUserId());
					findCashDeposit.setGoodsId(bargainRecord.getGoodsId());
					findCashDeposit.setCurrentPeriods(bargainRecord.getCurrentPeriods());
					findCashDeposit.setIsrefund(0);
					CashDeposit cashDeposit = cashDepositMapper.getCashDeposit(findCashDeposit).get(0);

					double deductionPrice = 0.00;// 荷兰式拍卖的交纳金额 xieyc
					boolean flag = false;// 是否调用支付接口 （true 调用 fasle调用
											// http://localhost:8080/bh-product-api/auction/depositJsp.json这个接口支付）
					double realPayPrice = 0.00;// 实际支付金额（订单总价-保证金）
					int price = orderXyc.getOrderPrice() - cashDeposit.getDepositPrice();
					if (price > 0) {// 保证金不足以抵扣支付金额
						flag = true;// 调用支付接口
						realPayPrice = (double) price / 100;// 实际支付金额（订单价格-保证金）
						deductionPrice = (double) cashDeposit.getDepositPrice() / 100;// 抵扣金额为保证金
					} else {
						deductionPrice = (double) orderXyc.getOrderPrice() / 100;// 抵扣金额
					}
					AuctionPojo auctionPojo = new AuctionPojo();// 荷兰式拍卖pojo
					auctionPojo.setDepositPrice((double) cashDeposit.getDepositPrice() / 100);
					auctionPojo.setFz(5);
					auctionPojo.setFlag(flag);
					auctionPojo.setRealPayPrice(realPayPrice);
					auctionPojo.setDeductionPrice(deductionPrice);

					cleanAccount.setAuctionPojo(auctionPojo);
					addre = memberUserAddressMapper.selectAllAddressByisDel(orderXyc.getmId());
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		} else {
			// 根据mId查找地址
			MemberUserAddress address = memberUserAddressMapper.selectByPrimaryKey(orderXyc.getmAddrId());
			addre.add(address);
		}
		cleanAccount.setUserAddressesList(addre);
		/***************** xieyc 荷兰式逻辑添加 end ********************/

		int shopGoodsNum = 0;
		Integer shopDeliveryPrice = 0;
		Integer shopPrice = 0;

		Order order = orderMapper.selectByPrimaryKey(orderShop.getOrderId());
		cleanAccount.setOrderCartIds(order.getCartId());

		cleanAccount.setCartIds(String.valueOf(orderShop.getOrderId()));

		double count = (double) order.getOrderPrice() / 100;
		cleanAccount.setTotalCount(count);

		List<OrderGoodsCartListShopIdList> goodsCart = new ArrayList<>();
		// 获得所有商家
		List<OrderShop> orderShopList = goodsCartMapper.selectOrderGoodsCartList(orderShop.getOrderId());
		List<OrderSku> orderSkuList=null;
		
		//查询所有商品使用的滨惠豆
		int point=0;
		int orderPrice=0;
		if(orderShopList.size()>0) {
	   	   for (OrderShop orderShop2 : orderShopList) {
			    point=point+orderShop2.getSavePrice();
		   }
		}
		cleanAccount.setPoint(point);
		for (OrderShop orderShop2 : orderShopList) {
			BigDecimal b1 = new BigDecimal(orderShop2.getOrderPrice()+orderShop2.getSavePrice());
			double totalPrice = Double.valueOf(b1.divide(new BigDecimal(100)).toString());
			double gDe = (double) orderShop2.getgDeliveryPrice() / 100;
			shopDeliveryPrice = shopDeliveryPrice + orderShop2.getgDeliveryPrice();
			OrderGoodsCartListShopIdList goodsCartListShopIdList = new OrderGoodsCartListShopIdList();
			goodsCartListShopIdList.setShopName(orderShop2.getShopName());
			goodsCartListShopIdList.setDeliveryPrice(gDe);
			goodsCartListShopIdList.setTotalPrice(totalPrice);
			goodsCartListShopIdList.setShopId(orderShop2.getShopId());	
			orderPrice+=orderShop2.getOrderPrice();
			//zlk 2018.7.27 待付款使用的优惠劵
			String[] conpon = order.getCouponsId().split(",");
			for(String conId:conpon) {
				if(!conId.equals("0")) {
			       CouponLog c = couponLogMapper.selectByPrimaryKey(Integer.valueOf(conId));
		           Coupon coupon=couponMapper.selectByPrimaryKey(c.getCouponId());
		         
			       if (coupon.getCouponType() == 2) {
			    	  //免邮券，用订单邮费
					  BigDecimal b = new BigDecimal(orderShop2.getgDeliveryPrice());
					  goodsCartListShopIdList.setCouponAmount(String.valueOf(b.divide(new BigDecimal(100))));
				   }else {
					  //不是免邮券，用优惠券的价格
		        	  BigDecimal b = new BigDecimal(coupon.getAmount());
					  goodsCartListShopIdList.setCouponAmount(String.valueOf(b.divide(new BigDecimal(100))));
				   }
		           goodsCartListShopIdList.setCouponName(coupon.getTitle());
				}
			}
			// 获得该商家的订单
			orderSkuList = goodsCartMapper.selectOrderSkuGoodsCartList(orderShop2.getId());
			Integer price = 0;
			int num = 0;
			List<OrderCartGoodsList> orderCartGoodsList = new ArrayList<>();

			List<Integer> goodsList = new ArrayList<Integer>();// 保存 goodsId
			Set<Integer> goodsIdSet = new HashSet<>(); // xieyc add
			for (OrderSku orderSku : orderSkuList) {
				Goods goodsXyc = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());// xiey
				goodsIdSet.add(orderSku.getGoodsId()); // add
				goodsList.add(goodsXyc.getId());// xieyc add
				OrderCartGoodsList oList = new OrderCartGoodsList();
				oList.setgImage(orderSku.getSkuImage());
				oList.setGoodName(orderSku.getGoodsName());
				oList.setNum(orderSku.getSkuNum());
				double realsellPrice = (double) orderSku.getSkuSellPriceReal() / 100;
				oList.setRealsellPrice(realsellPrice);
				JSONObject jsonObject = JSONObject.fromObject(orderSku.getSkuValue()); // value转义 原
				Map<String, Object> m = new HashMap<>();
				m.put("valueObj", jsonObject);
				oList.setGoodsSkus(m);
				oList.setCatIdOne(goodsXyc.getCatIdOne());// 一级分类 xieyc add
				orderCartGoodsList.add(oList);
				price = price + orderSku.getSkuSellPriceReal() * orderSku.getSkuNum();
				num = num + orderSku.getSkuNum();
			}

			goodsCartListShopIdList.setGoodsCartLists(orderCartGoodsList);
			shopPrice = shopPrice + price;
			shopGoodsNum = shopGoodsNum + num;
			goodsCartListShopIdList.setNum(num);

			/**************** xieyc 优惠劵逻辑 start *****************/
			/**
			 * 说明：拍卖，超级滨惠豆，周末大放送和拼团不能使用优惠卷 而且这些商品都不能加入购物车
			 */
			List<UsableCoupon> usableCouponList = new ArrayList<>();
			if(orderXyc.getFz() != 5){//拍卖订单不能用优惠劵
				if (goodsList.size() == 1) {
					Goods goods = goodsMapper.selectByPrimaryKey(goodsList.get(0));
					if (goods.getSaleType() != 3 && goods.getTopicType() != 6) {// 不是拼团和不是拍卖
						int shopId = orderShop2.getShopId();
						int row=0;
						//查询该商品所在的所有专区
						List<ActZoneGoods> listActZone = actZoneGoodsMapper.selectIsCoupon(goods.getId());// 超级滨惠豆和周末大放送也不允许使用优惠劵
						for (ActZoneGoods actZoneGoods : listActZone) {
							ActZone actZone=actZoneMapper.selectByPrimaryKey(actZoneGoods.getZoneId());
							int i=1;
							while(i!=0){//找到该专区对应的最高级专区（死循环）
								if(actZone.getReid()!=0){
									actZone=actZoneMapper.selectByPrimaryKey(actZone.getReid());
								}else{
									break;//跳出死循环
								}
							}
							if(actZone.getIsCoupon()==0){//判断最高级专区是否可以用优惠劵（只要有一个专区不能使用那么他不能使用优惠劵）
								row=1;
								break;
							}
						}
						if (row == 0) {// 商品不在专区中 则可以用优惠劵。 或者 在专区中其 对应的所有专区都可以用优惠劵
							usableCouponList = this.couponList(goodsList, orderXyc.getmId(), gDe, goodsCartListShopIdList,
									shopId);// 获取可以用的优惠卷 // xieyc
						}
					}
				} else if (goodsList.size() > 1) {// >1的时候
													// 肯定是从购物车过来的(拼团、拍卖、超级滨惠豆、周末大放送都不可以加入购物车)
					usableCouponList = this.couponList(goodsList, orderXyc.getmId(), gDe, goodsCartListShopIdList,
							orderShop2.getShopId());// 获取可以用的优
				}
			}
			goodsCartListShopIdList.setUsableCouponList(usableCouponList);
			/**************** xieyc 优惠劵逻辑 end *****************/
			goodsCart.add(goodsCartListShopIdList);
		}

		List<UsableCoupon> usableCouponListAll = new ArrayList<>();
		for (OrderGoodsCartListShopIdList orderGoodsCartListShopIdList : goodsCart) {
			List<UsableCoupon> usableCouponList = orderGoodsCartListShopIdList.getUsableCouponList();
			if (usableCouponList != null) {
				usableCouponListAll.addAll(usableCouponList);
			}
		}
		cleanAccount.setUsableCouponListAll(usableCouponListAll);// 将平台和商家可以用的优惠卷都放到这里
		cleanAccount.setOrderPrice(orderPrice);
		
		
		
		// isCart:0从购物车过来，1从订单过来
		BHSeed bhSeed = getBhSeed(order.getCartId(), String.valueOf(order.getmId()), String.valueOf(order.getFz()), 1);
		cleanAccount.setBhSeed(bhSeed);
		double doubleShopPrice = (double) shopPrice / 100;
		cleanAccount.setPrice(RegExpValidatorUtils.formatdouble(doubleShopPrice));
		cleanAccount.setGoodsCartsList(goodsCart);
		cleanAccount.setGoodsNum(shopGoodsNum);
		double doubleShopDeliveryPrice = (double) shopDeliveryPrice / 100;
		cleanAccount.setDeliveryPrice(RegExpValidatorUtils.formatdouble(doubleShopDeliveryPrice));

		cleanAccount.setIs_lock(1); //锁定已选择的滨惠豆和优惠劵
		return cleanAccount;
	}

	// 2018-5-22根据orderId去orderShop表查询
	public List<OrderShop> selectOrderShopListByOrderId(Integer orderId) throws Exception {
		return orderShopMapper.selectByOrderId(orderId);
	}

	public BhResult getSimpleBhBean(Map<String, String> map, Member member, HttpServletRequest request) {
		BhResult bhResult = new BhResult(200, "操作成功", null);
		try {
			String isBeans = map.get("isBeans");// 1调用使用滨惠豆下的单，0不使用
			if (isBeans.equals("1")) {
				String orderId = map.get("orderId");
				String cartIds = map.get("cartIds");// "cartIds":"196,198"购物车的id,以数组的形式返回
				String fz = map.get("fz");
				BHSeed bhSeed = new BHSeed();
				if (orderId.equals("0")) {
					bhSeed = getBhSeed(cartIds, member.getId().toString(), fz, 0);
				} else {
					Order o = orderMapper.selectByPrimaryKey(Integer.parseInt(orderId));
					bhSeed = getBhSeed(o.getCartId(), member.getId().toString(), fz, 1);
				}
				if (bhSeed != null) {
					// 如果用户的滨惠豆不足以抵抗
					int num = bhSeed.getNeedSeedNum() - bhSeed.getOwnSeedNum();
					if (num > 0) {
						return BhResult.build(400, "滨惠豆不够，无法购买 ", bhSeed);
					}
				}
			}
		} catch (Exception e) {
		}
		return bhResult;
	}

	public BhResult getGoodsByCartId(String ids) throws Exception {
		BhResult bhResult = new BhResult(200, "", null);
		if (StringUtils.isNotEmpty(ids)) {
			List<String> list = JsonUtils.stringToList(ids);
			List<GoodsCart> list1 = goodsCartMapper.getGoodsByCartId(list);
			if (list1.size() > 0) {
				// 有数量小于0
				bhResult = new BhResult(400, "", null);
			}
		}

		return bhResult;
	}

	public Order selectOrderById(Integer id) throws Exception {
		return orderMapper.selectByPrimaryKey(id);
	}

	// 生成订单
	public Order getOrder(Map<String, String> map, Member member, HttpServletRequest request) {
		String paymentId = map.get("paymentId");// 支付方式0为货到付款，1在线支付，2公司转帐，3邮局汇款
		String paymentStatus = map.get("paymentStatus");
		String addressId = map.get("addressId");// 前端传过来的地址的id
		String couponsId = map.get("couponsId");// 优惠券coupon_log表id
		String cartIds = map.get("cartIds");// "cartIds":"196,198"购物车的id,以数组的形式返回
		String orderId = map.get("orderId");
		String fz = map.get("fz");
		String teamNo = map.get("teamNo");// 团号
		String isBeans = map.get("isBeans");// 1调用使用滨惠豆下的单，0不使用
		

		String tgId = map.get("tgId"); // 活动商品明细id
		String actNo = map.get("actNo"); // 邀请码

		if (StringUtils.isEmpty(paymentId)) {
			paymentId = "1";
		}
		if (couponsId == null || couponsId.equals("")) {
			couponsId = "0";
		} /*else {
			couponsId = couponsId.split(",")[0];
		}*/
		if (paymentStatus == null || paymentStatus.equals("")) {
			paymentStatus = "1";
		}

		if (StringUtils.isEmpty(isBeans) || isBeans.equals("")) {
			isBeans = "0";
		}
		if (cartIds == null || cartIds.equals("")) {
			cartIds = "241,242,243,244";
		}
		/**** xieyc modifyStart ******/
		Integer shopId = null;
		String cartId = cartIds.split(",")[0];// 购物车id(现在数据一个购物车id对应一个订单（order_main）)
		if (!"241".equals(cartId)) {
			// 等于241代表传入的购物车id为null,但此时orderId不为null(待支付的订单再次去支付，即从订单列表过来时，此时shopId取什么值无影响)
			// 等于241 代表从订单列表过来 ，=其他值，就是从商品详情过来
			GoodsCart goodsCart = goodsCartMapper.selectByPrimaryKey(Integer.valueOf(cartId));
			if (goodsCart != null) {
				shopId = goodsCart.getShopId();
			}
		}
		/**** xieyc modifyEnd ******/

		Order order = new Order();
		// 用户的id
		order.setmId(member.getId());
		order.setShopId(shopId);
		// 收货地址的id
		order.setmAddrId(Integer.parseInt(addressId));
		order.setSkuPriceReal(0);
		order.setSkuPrice(0);
		order.setDeliveryPrice(0);
		order.setDiscountPrice(0);
		order.setPromotionPrice(0);
		order.setOrderPrice(0);
		// 优惠劵的id
		order.setCouponsId(couponsId);
		// 下单时间
		order.setAddtime(new Date());
		// 支付方式
		order.setPaymentId(Integer.parseInt(paymentId));
		// 支付状态：1代表未支付
		order.setPaymentStatus(1);
		order.setDeliveryId(0);
		order.setDeliveryStatus(0);
		order.setStatus(1);
		order.setCartId(cartIds);
		// 是否是使用滨惠豆
		order.setIsBeans(Integer.parseInt(isBeans));

		try {
			if (StringUtils.isNotEmpty(fz) && StringUtils.isNotEmpty(teamNo)) {
				order.settNo(teamNo);
				order.setFz(Integer.parseInt(fz));
			} else if (StringUtils.isNotEmpty(fz) && StringUtils.isEmpty(teamNo)) {
				order.setFz(Integer.parseInt(fz));
			} else {
				order.setFz(0);
			}
			// 如果orderId的值为0,则 生成订单
			Order order2 = new Order();
			if ("0".equals(orderId)) {
				order2 = userSubmitOrderService.insertOrderBySelective(order, fz, teamNo);
				if (!StringUtils.isEmpty(fz)) {
					order2.setGroupFz(Integer.parseInt(fz));
				}
				if (!StringUtils.isEmpty(teamNo)) {
					order2.setTeamNo(teamNo);
				}

			} else {
				// 从订单列表过来
				Order order3 = new Order();
				if (StringUtils.isNotEmpty(addressId)) {
					// xieyc 荷兰式待支付订单允许修改地址
					order3.setmAddrId(Integer.valueOf(addressId));
				}
				order3.setId(Integer.parseInt(orderId));
				order3.setPaymentId(Integer.parseInt(paymentId));
				order3.setIsBeans(Integer.parseInt(isBeans));
				order3.setCouponsId(couponsId);
				order2 = userOrderService.selectOrderById1(order3);
				Integer z = order2.getFz();
				if (z == 0) {
					order2.setGroupFz(null);
					order2.setTeamNo(null);
				} else if (order2.gettNo() != null) {
					order2.setGroupFz(z);
					order2.setTeamNo(teamNo);
				} else {
					order2.setGroupFz(z);
					order2.setTeamNo(null);
				}

			}

			/* 活动商品参数插入 */
			if (StringUtils.isNotEmpty(tgId)) {
				// 插入活动商品明细id
				order2.setTgId(Integer.parseInt(tgId));
			}
			if (StringUtils.isNotEmpty(actNo)) {
				// 活动编号
				order2.setActNo(actNo);
			}

			return order2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 方法说明:该接口是用户的订单列表 参数：对象orderShop 返回的类型：Map<String, Object>
	 **/
	public Map<String, Object> selectOrderList(OrderShop orderShopParam, Integer currentPage, Integer pageSize)
			throws Exception {
		try {
			orderShopParam.setPageSize(pageSize + "");
			Integer currentPageIndex = (currentPage - 1) * pageSize;
			orderShopParam.setCurrentPage(currentPageIndex + "");
			Map<String, Object> myMap = new HashMap<>();
			List<MyOrderShop> myOrderShopList = new ArrayList<>();
			List<OrderShop> orderShopList = new ArrayList<>();
			String  goodsName=orderShopParam.getGoodsName();
			if (StringUtils.isNotBlank(orderShopParam.getStatus() + "") && orderShopParam.getStatus() != null
					&& orderShopParam.getStatus() == 9) {
				orderShopList = orderShopMapper.selectOrderListByShare(orderShopParam.getStatus(),
						orderShopParam.getmId(), goodsName, currentPageIndex, pageSize);
			} else {
				orderShopList = orderShopMapper.selectOrderList(orderShopParam.getStatus(), orderShopParam.getmId(),
						goodsName, currentPageIndex, pageSize);
			}
			
			if (orderShopList.size() > 0) {
				for (OrderShop orderShop : orderShopList) {
					MyOrderShop myOrderShop = new MyOrderShop();
					MemberShop memberShop = memberShopMapper.selectShopMsg(orderShop.getShopId());
					// 获得商家名称
					if (memberShop != null) {
						myOrderShop.setShopName(memberShop.getShopName());
					}
					myOrderShop.setStatus(orderShop.getStatus());
					// 设置订单的id
					myOrderShop.setId(orderShop.getId());
					// 设置商家的id
					myOrderShop.setShopId(orderShop.getShopId());
					// 设置主订单的id
					myOrderShop.setOrderId(orderShop.getOrderId());
					// 设置用户的id
					myOrderShop.setmId(orderShop.getmId());

					// 根据orderNo查询该订单是否是拼团单，如果是则该单会在团购表里面有一条记录
					List<OrderTeam> team = orderTeamMapper.selectOrderTeanByOrderNo(orderShop.getOrderNo());
					// 默认设置拼团仅剩0个名额
					myOrderShop.setWaitNum(0);
					// 如果teamStatus=0,则邀请好友拼单,teamStatus的值有-1 拼团失败, 0拼团中,
					// 1成功,2该单不是拼团单
					myOrderShop.setTeamStatus(2);
					// 默认团号为null
					myOrderShop.setTeamNo(null);
					if (team.size() > 0) {
						// team.get(0).getStatus的值有-1(拼单失败),0(拼单中),1(拼单成功)
						if (team.get(0).getStatus() == 1) {
							myOrderShop.setMystatus("拼单成功");
						} else if (team.get(0).getStatus() == 0) {
							myOrderShop.setMystatus("拼单中");
							// 如果该订单在拼团中,则需要计算该团还差多少人才拼成功
							int groupCount = orderTeamMapper.groupCount(team.get(0).getTeamNo());
							GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(team.get(0).getGoodsSkuId());
							if (goodsSku != null) {
								Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
								if (goods != null) {
									int num = goods.getTeamNum() - groupCount;
									myOrderShop.setWaitNum(num);
								}
							}
						} else if (team.get(0).getStatus() == -1) {
							myOrderShop.setMystatus("拼单失败");
						} else {
							String name = StatusNameUtils.getMyStatus(orderShop.getStatus(), 1);
							myOrderShop.setMystatus(name);
						}
						myOrderShop.setTeamStatus(team.get(0).getStatus());
						myOrderShop.setTeamNo(team.get(0).getTeamNo());
					} else {
						String name = StatusNameUtils.getMyStatus(orderShop.getStatus(), 1);
						myOrderShop.setMystatus(name);
					}
					// orderShop.getIsRefund()的取值有0(该单未有售后的商品),1(该单部分商品有申请售后),2(该单的全部商品申请售后)
					Integer isRefund = orderShop.getIsRefund();
					if (isRefund == 2) {
						OrderRefundDoc doc = new OrderRefundDoc();
						doc.setmId(orderShop.getmId());
						doc.setOrderShopId(orderShop.getId());
						List<OrderRefundDoc> docList = orderRefundDocMapper.selectOrderRefundDoc(doc);
						if (docList.size() > 0) {
							myOrderShop.setMystatus("售后服务");
						} else {
							myOrderShop.setMystatus("售后服务");
						}
					}
					List<OrderSku> skuList = orderSkuMapper.selectIsRefund(orderShop.getId());
					OrderSku orderSku = new OrderSku();
					orderSku.setOrderId(orderShop.getOrderId());
					orderSku.setShopId(orderShop.getShopId());
					orderSku.setOrderShopId(orderShop.getId());
					// 如果该订单的全部商品都已经评价完,则设置mystatus为已评价
					List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);
					if (skuList.size() > 0 && skuList.size() == orderSkuList.size()) {
						myOrderShop.setMystatus("已评价");
					}
					List<MyOrderSku> myOrderSkuList = new ArrayList<>();
					if (orderSkuList.size() > 0) {
						for (OrderSku simOrderSku : orderSkuList) {
							Long jdSkuNo=0l;
							if (!orderShop.getJdorderid().equals("0")&& !orderShop.getJdorderid().equals("-1")) {//京东订单
								JdOrderSku jdOrderSku=jdOrderSkuMapper.getByOrderSkuId(simOrderSku.getId());
								if(jdOrderSku!=null){
									jdSkuNo=jdOrderSku.getSkuId();
								}
							}
							MyOrderSku myOrderSku = new MyOrderSku();
							myOrderSku.setJdSkuNo(jdSkuNo);
							myOrderSku.setId(simOrderSku.getId());
							myOrderSku.setGoodsId(simOrderSku.getGoodsId());
							myOrderSku.setGoodsName(simOrderSku.getGoodsName());
							// 商品的属性
							String value = simOrderSku.getSkuValue();
							Object object = JsonUtils.stringToObject(value);
							myOrderSku.setValueObj(object);
							myOrderSku.setSkuId(simOrderSku.getSkuId());
							myOrderSku.setSkuImage(simOrderSku.getSkuImage());
							myOrderSku.setSkuNum(simOrderSku.getSkuNum());
							// 商品的的价格
							myOrderSku.setRealSellPrice(MoneyUtil.fen2Yuan(simOrderSku.getSkuSellPriceReal() + ""));
							OrderRefundDoc doc = new OrderRefundDoc();
							doc.setOrderSkuId(simOrderSku.getId());
							OrderRefundDoc doc2 = orderRefundDocMapper.selectByOrderSkuId(doc);
							if (doc2 != null) {
								// 如果该商品在售后服务当中,则需要显示当前售后的步骤
								String name = StatusNameUtils.getRefundStatusName(doc2.getStatus(),
										doc2.getRefundType(), doc2.getExpressNo());
								myOrderSku.setMystatus(name);
							}
							// 如果订单的状态status=5：待评价
							if (orderShop.getStatus() == 5) {
								GoodsComment goodsComment = new GoodsComment();
								goodsComment.setOrderSkuId(simOrderSku.getId());
								List<GoodsComment> commentList = goodsCommentMapper.selectCommentsBySkuId(goodsComment);
								if (commentList.size() < 1) {
									// 如果isShowCommentButton的值为1,前端显示去评价按钮
									myOrderSku.setIsShowCommentButton(1);
								}
							}
							myOrderSkuList.add(myOrderSku);
						}
					}
					myOrderShop.setOrderSku(myOrderSkuList);
					//// 合计多少元
					int orderPrice = orderSkuMapper.getOrderAllPrice(orderShop.getId());
					// 有多少件商品
					int goodsNum = orderSkuMapper.selectGoodsNum(orderShop.getId());
					// 订单的邮费
					
					int realgDeliveryPrice = 0;
					CouponLog couponLog = couponLogMapper.getByIdAndCouType(orderShop.getCouponId(),2);//是否使用了免邮卷				
					if(couponLog!=null){//使用了免邮卷
						realgDeliveryPrice =  orderShop.getCouponPrice();
					}else{//没有使用
						realgDeliveryPrice = orderShop.getgDeliveryPrice();// 邮费“分”转化成“元”
					}
					myOrderShop.setRealgDeliveryPrice(MoneyUtil.fen2Yuan(realgDeliveryPrice + ""));
					// 有多少件商品
					myOrderShop.setGoodsNumber(goodsNum);
					// 合计多少元
					myOrderShop.setAllPrice(MoneyUtil.fen2Yuan(orderPrice + ""));
					//zlk  订单中的商品被删了或者已下架 
					if(orderShop.getStatus()==11||orderShop.getStatus()==12) {
						myOrderShop.setStatus(10);
						myOrderShop.setMystatus("已失效");
					}
					
					myOrderShopList.add(myOrderShop);
				}

			}
			myMap.put("list", myOrderShopList);
			return myMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Description: 是否售后
	 * @author xieyc
	 * @date 2018年8月21日 下午5:21:31 
	 */
	public boolean afterSale(Date receivedtime,Date now){
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(receivedtime);//确认收货时间
		calendar.add(Calendar.DAY_OF_MONTH,7);  //确认收货7天后
		Date before7days = calendar.getTime();   //确认收货7天后的时间
		if(before7days.getTime()<= now.getTime()){
			return false;//不可以售后
		}else{
			return true;//可以售后
		}	
	}	
	/***
	 * 
	 * 
	 * **/
	public MyOrderShopDetail selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception {
		try {			
			MyOrderShopDetail myOrderShopDetail = new MyOrderShopDetail();
			
			OrderShop orderShop2 = new OrderShop();
			orderShop2.setId(orderShop.getId());
			orderShop=orderShopMapper.selectByPrimaryKey(orderShop.getId());
			OrderShop orderShops = orderShopMapper.selectByOrderShopId(orderShop2);
		
			Order order = orderMapper.selectByPrimaryKey(orderShops.getOrderId());
			myOrderShopDetail.setStatus(orderShops.getStatus());
			myOrderShopDetail.setId(orderShops.getId());

			MemberShop memberShop = memberShopMapper.selectShopMsg(orderShops.getShopId());
			myOrderShopDetail.setShopName(memberShop.getShopName());
			if (orderShops.getStatus() == 9) {// 如果订单状态是备货中 ，改完待发货
				myOrderShopDetail.setStatus(2);
			}
			if (orderShops.getIsRefund() != 2) {
				String name = StatusNameUtils.getMyStatus(orderShops.getStatus(), 1);
				myOrderShopDetail.setMystatus(name);
				// 如果商品下架或者删除sku
				if (orderShops.getStatus() == 11 || orderShops.getStatus() == 12) {
					myOrderShopDetail.setStatus(10);
					myOrderShopDetail.setMystatus("已失效");
				}
			}
			List<OrderTeam> team = orderTeamMapper.selectOrderTeanByOrderNo(order.getOrderNo());
			if (team.size() > 0) {
				switch (team.get(0).getStatus()) {
				case -1:
					myOrderShopDetail.setMystatus("拼单失败");
					break;
				case 0:
					myOrderShopDetail.setMystatus("拼单中");
					break;
				case 1:
					myOrderShopDetail.setMystatus("拼单成功");
					break;
				}
			}
			Integer isRe = orderShops.getIsRefund();
			if (isRe == 2) {
				myOrderShopDetail.setMystatus("售后服务");
			}
			List<OrderSku> skuList = orderSkuMapper.selectIsRefund(orderShops.getId());
			OrderSku orderSku = new OrderSku();
			orderSku.setOrderId(orderShops.getOrderId());
			orderSku.setShopId(orderShops.getShopId());
			orderSku.setOrderShopId(orderShops.getId());
			List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBySelect(orderSku);
			if (skuList.size() > 0 && skuList.size() == orderSkuList.size()) {
				myOrderShopDetail.setMystatus("已评价");
			}
			
			List<Map<Object,Object>> list = new ArrayList<>();
			if (orderSkuList.size() > 0) {
				if (!orderShops.getJdorderid().equals("0") && !orderShops.getJdorderid().equals("-1")) {// 不是京东下单或者下单失败并且是发货或待评价状态
					Map<String, List<OrderSku>> mapListOrderSku = new HashMap<String, List<OrderSku>>();
					for (OrderSku myOrderSku : orderSkuList) {
						JdOrderMain jdOrderMain = jdOrderMainMapper.getByOrderSkuId(myOrderSku.getId());
						if (mapListOrderSku.containsKey(jdOrderMain.getJdOrderId())) {
							mapListOrderSku.get(jdOrderMain.getJdOrderId()).add(myOrderSku);
						} else {
							List<OrderSku> listSku = new ArrayList<OrderSku>();
							listSku.add(myOrderSku);
							mapListOrderSku.put(jdOrderMain.getJdOrderId(), listSku);
						}
					}
					Long jdSkuNo=0l;
					for (String jdOrderId : mapListOrderSku.keySet()) {
						List<OrderSku> listSku = mapListOrderSku.get(jdOrderId);
						List<MyOrderShopDetailOrderSku> myOrderShopDetailOrderSkuList = new ArrayList<>();
						Map<Object,Object> map=new HashMap<Object, Object>();
						jdSkuNo=this.getSkuDetail(map, listSku,orderShops, order,team,myOrderShopDetailOrderSkuList);
						map.put("orderStock",orderStock(orderShops.getId()+"",jdSkuNo+""));//物流信息
						list.add(map);
					}
				}else {
					Map<Object,Object> map=new HashMap<Object, Object>();
					List<MyOrderShopDetailOrderSku> myOrderShopDetailOrderSkuList = new ArrayList<>();
					this.getSkuDetail(map, orderSkuList, orderShops, order, team,myOrderShopDetailOrderSkuList);
					map.put("orderStock",orderStock(orderShops.getId()+"","0"));//物流信息
					list.add(map);
				}
			} 
			myOrderShopDetail.setOrderSku(list);

			Integer deductionPrice = 0;// 荷兰式抵扣
			if (order.getFz() == 5) {
				BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order.getOrderNo());// 拍卖记录
				if (bargainRecord != null) {
					deductionPrice = bargainRecord.getDiscountPrice();
				}
			}
			// 拍卖押金抵扣
			myOrderShopDetail.setDeductionPrice(MoneyUtil.fen2Yuan(deductionPrice + ""));
			// 商品总价
			Integer allPrice = orderSkuMapper.selectGoodsTotalPrice(orderShops.getId());
			// 运费
			double realgDeliveryPrice = 0;
			CouponLog couponLog = couponLogMapper.getByIdAndCouType(orderShop.getCouponId(),2);//是否使用了免邮卷				
			if(couponLog!=null){//使用了免邮卷
				realgDeliveryPrice =  orderShops.getCouponPrice();
			}else{//没有使用
				realgDeliveryPrice = orderShops.getgDeliveryPrice();// 邮费“分”转化成“元”
			}
			myOrderShopDetail.setRealgDeliveryPrice(MoneyUtil.fen2Yuan(realgDeliveryPrice + ""));
			// 商品总价
			myOrderShopDetail.setAllPrice(MoneyUtil.fen2Yuan(allPrice + ""));
			// 滨惠豆抵扣
			myOrderShopDetail.setRealSavePrice(MoneyUtil.fen2Yuan(orderShops.getSavePrice() + ""));
			// 优惠劵抵扣
			myOrderShopDetail.setCouponsPrice(MoneyUtil.fen2Yuan(orderShops.getCouponPrice() + ""));
			// 实付款
			myOrderShopDetail.setRealOrderPrice(MoneyUtil.fen2Yuan(orderShops.getOrderPrice() + ""));
			// 订单编号
			myOrderShopDetail.setShopOrderNo(orderShops.getShopOrderNo());

			Map<String, Object> myOrderSimpleMap = new HashMap<>();
			// 设置支付方式:默认3为微信支付
			String paymentIdName = "3";
			if (order.getOrderPrice() > 0) {
				paymentIdName = order.getPaymentId() + "";
			} else {
				paymentIdName = "4";
			}
			switch (paymentIdName) {
			case "1":
				myOrderSimpleMap.put("paymentIdName", "货到付款");
				break;
			case "2":
				myOrderSimpleMap.put("paymentIdName", "支付宝支付");
				break;
			case "3":
				myOrderSimpleMap.put("paymentIdName", "微信支付");
				break;
			case "4":
				myOrderSimpleMap.put("paymentIdName", "免支付");
				break;
			case "7":
				myOrderSimpleMap.put("paymentIdName", "钱包支付");
				break;
			default:
				myOrderSimpleMap.put("paymentIdName", "微信支付");
				break;
			}

			myOrderSimpleMap.put("id", order.getId());
			myOrderSimpleMap.put("fz", order.getFz());
			// 下单时间
			myOrderSimpleMap.put("addtime", order.getAddtime());

			myOrderShopDetail.setWaitTime("0");
			if ((orderShops.getStatus().equals(3)) && (orderShops.getdState().equals(4))) {
				String wait = RegExpValidatorUtils.getEndTime1(orderShops.getReceivedtime());
				if (wait != null) {
					if (wait.equals("00")) {
						OrderShop orderShop3 = new OrderShop();
						orderShop3.setId(orderShops.getId());
						orderShop3.setStatus(Contants.shopStatu5);
						orderShopMapper.updateByPrimaryKeySelective(orderShop3);
						myOrderShopDetail.setStatus(Contants.shopStatu5); // zlk把传过去的值状态改了
					} else {
						myOrderShopDetail.setWaitTime(wait);
					}
				} else {
					OrderShop orderShop3 = new OrderShop();
					orderShop3.setId(orderShops.getId());
					orderShop3.setStatus(Contants.shopStatu5);
					orderShopMapper.updateByPrimaryKeySelective(orderShop3);
					myOrderShopDetail.setStatus(Contants.shopStatu5);// zlk把传过去的值状态改了
				}

			}
			/** 设置地址 */
			if (order.getmAddrId() != null) {
				MemberUserAddress memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order.getmAddrId());
				// 如果查询的地址不为空，则设置该地址，如果为空，则设为null
				Map<String, Object> meMapAddress = new HashMap<>();
				// 收货人
				meMapAddress.put("fullName", memberUserAddress.getFullName());
				meMapAddress.put("tel", memberUserAddress.getTel());
				// 收货地址
				meMapAddress.put("provname", memberUserAddress.getProvname());
				meMapAddress.put("cityname", memberUserAddress.getCityname());
				meMapAddress.put("areaname", memberUserAddress.getAreaname());
				meMapAddress.put("fourname", memberUserAddress.getFourname());
				meMapAddress.put("address", memberUserAddress.getAddress());
				myOrderSimpleMap.put("memberUserAddress", meMapAddress);
			}
			myOrderShopDetail.setOrderSimple(myOrderSimpleMap);
			if (orderShops.getValidLen() != null) {
				Date date2 = new Date();
				long diff = orderShops.getValidLen().getTime() - date2.getTime();// 这样得到的差值是微秒级别
				long days = diff / (1000 * 60 * 60 * 24);
				long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
				long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
				long s = (diff / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);// 获取秒
				myOrderShopDetail.setValidLen(hours + ":" + minutes + ":" + s);// 2018.8.1
																				// zlk
			}
			return myOrderShopDetail;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	/**
	 * 
	 * @Description: 订单详情页获取sku信息
	 * @author xieyc
	 * @date 2018年8月7日 下午7:35:07 
	 */
	private Long getSkuDetail(Map<Object,Object> map,List<OrderSku> orderSkuList,OrderShop orderShops 
			,Order order,List<OrderTeam> team,List<MyOrderShopDetailOrderSku> myOrderShopDetailOrderSkuList){
		Long returnSkuId=0l;
		for (OrderSku myOrderSku : orderSkuList) {
			Long jdSkuNo=0l;
			if (!orderShops.getJdorderid().equals("0")&& !orderShops.getJdorderid().equals("-1")) {//京东订单
				JdOrderSku jdOrderSku=jdOrderSkuMapper.getByOrderSkuId(myOrderSku.getId());
				if(jdOrderSku!=null){
					jdSkuNo=jdOrderSku.getSkuId();
					returnSkuId=jdSkuNo;
				}
			}
			MyOrderShopDetailOrderSku myOrderShopDetailOrderSku = new MyOrderShopDetailOrderSku();
			
			
			/***********************售后按钮显示逻辑 start*********************************/
			boolean isAfterSale=false;
			OrderRefundDoc findOrderRefund = new OrderRefundDoc();
			findOrderRefund.setOrderSkuId(myOrderSku.getId());
			OrderRefundDoc orderRefundDoc = orderRefundDocMapper.selectByOrderSkuId(findOrderRefund);
			if(order.getFz()!=5 && order.getFz()!=6 && (orderRefundDoc==null || orderRefundDoc.getStatus()==1 || orderRefundDoc.getStatus()==3 || orderRefundDoc.getStatus()==6 || orderRefundDoc.getStatus()==9)){//没有售后过并且是不是拍卖并且不是兑换
				if (orderShops.getStatus() == 2 || orderShops.getStatus() == 9) {//是否显示售后按钮（这个时候如果允许售后之后显示==》退款按钮）
					OrderCollectionDoc orderCollectionDoc=orderCollectionDocMapper.selectByOrderId(orderShops.getOrderId());
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
					String applyTime=sf.format(orderCollectionDoc.getAddtime());
					ActivityTime activityTime=activityTimeMapper.selectByPrimaryKey(1);
					String startTime=sf.format(activityTime.getStartTime());
					String endTime =sf.format(activityTime.getEndTime());
					String nowTime =sf.format(new Date());//当前时间
					int result=applyTime.compareTo(startTime); //大于
					int result1=endTime.compareTo(applyTime); //大于
					int result2=nowTime.compareTo(endTime); //大于
					AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(orderShops.getmId());
					OrderTeam orderTeam=orderTeamMapper.getByOrderNo(order.getOrderNo());			
					if(result>0&&result1>0&&result2>0) {//支付时间大于活动开始时间并且小于结束时间并且现在时间（发起退款的时间）大于结算时间
					    if(aspUserGuess!=null) {
							isAfterSale = false;
						}else {
							if(orderShops.getStatus() == 2&&order.getFz()==1&&orderTeam.getStatus()!=1){
								isAfterSale = false;//待分享的订单不允许退款
							}else{
								isAfterSale = true;
							}
						}
					}else{
						if(orderShops.getStatus() == 2&&order.getFz()==1&&orderTeam.getStatus()!=1){
							isAfterSale = false;//待分享的订单不允许退款
						}else{
							isAfterSale = true;
						}
					}
				}
				// 是否显示售后按钮(如果确认收货7天后售后按钮不显示)==》这个时候如果可以退款可以显示换货和退款退货按钮
				if (orderShops.getStatus() == 5 || orderShops.getStatus() == 7) {
					//待评价、已评价的判断是否确认收货超过7了，超过7天不允许售后
					isAfterSale = this.afterSale(orderShops.getReceivedtime(),new Date());
				}
			}
			/***********************售后按钮显示逻辑 end*********************************/
			myOrderShopDetailOrderSku.setAfterSale(isAfterSale);
			
			
			myOrderShopDetailOrderSku.setJdSkuNo(jdSkuNo);
			// 对orderSku进行分解
			myOrderShopDetailOrderSku.setStatus(5);
			String value = myOrderSku.getSkuValue();
			Object object = JsonUtils.stringToObject(value);
			myOrderShopDetailOrderSku.setValueObj(object);// 将skuValue转为object
			myOrderShopDetailOrderSku
					.setRealSellPrice(MoneyUtil.fen2Yuan(myOrderSku.getSkuSellPriceReal() + ""));// 销售价格double=int/100
			myOrderShopDetailOrderSku.setSkuImage(myOrderSku.getSkuImage());
			myOrderShopDetailOrderSku.setGoodsName(myOrderSku.getGoodsName());
			myOrderShopDetailOrderSku.setSkuNum(myOrderSku.getSkuNum());
			myOrderShopDetailOrderSku.setGoodsId(myOrderSku.getGoodsId());
			myOrderShopDetailOrderSku.setId(myOrderSku.getId());
			int status = orderShops.getStatus();
			// 是否可以售后的判断
			// 拍卖商品不允许售后服务 or 兑换商品不允许售后服务
			if (order.getFz() == 5 || order.getFz() == 6) {
				myOrderShopDetailOrderSku.setStatus(5);
			} else {
				OrderRefundDoc orderRefundDocParam = new OrderRefundDoc();
				orderRefundDocParam.setOrderSkuId(myOrderSku.getId());
				OrderRefundDoc doc2 = orderRefundDocMapper.selectByOrderSkuId(orderRefundDocParam);
				// 该商品是否申请过售后,如果为null,则没有申请过
				if (doc2 != null) {
					String orderSkuName=null;
					try {
						 orderSkuName = StatusNameUtils.getRefundStatusName(doc2.getStatus(),
								doc2.getRefundType(), doc2.getExpressNo());
					} catch (Exception e) {
						e.printStackTrace();
					}
					myOrderShopDetailOrderSku.setMystatus(orderSkuName);
					// 0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中
					// 8：换货成功
					// 9：换货失败 10客服通过退款审核 11收货客服审核通过
					if (doc2.getStatus() == 1 || doc2.getStatus() == 3 || doc2.getStatus() == 6
							|| doc2.getStatus() == 9) {
						myOrderShopDetailOrderSku.setStatus(1);
					} else {
						myOrderShopDetailOrderSku.setStatus(5);
					}
					// 当退款售后状态为5,7时，前端显示物流按钮为1
					if (doc2.getStatus() == 5 || doc2.getStatus() == 7) {
						myOrderShopDetailOrderSku.setIsShowlogButton(1);
					} else {
						myOrderShopDetailOrderSku.setIsShowlogButton(0);
					}
				} else {
					// 如果该商品是拼团商品并且拼团成功
					if (status == 2 && team.size() > 0 && team.get(0).getStatus() == 1) {
						myOrderShopDetailOrderSku.setStatus(1);
					} else {
						myOrderShopDetailOrderSku.setStatus(5);
						// 如果该商品不是拼单商品 并且当前是待发货状态 or待发货 or 待评价 or 已评价 or
						// 备货中
						if (status == 2 && team.size() < 1) {
							// isRefund '是否退款:0否，1部分退款，2全部退款',
							List<Integer> zoneIds = actZoneGoodsMapper.selectIsRefund(myOrderSku.getGoodsId());
							// 如果该商品不在act表里,则可以售后
							if (orderShops.getIsRefund() != 2 && zoneIds != null && zoneIds.size() == 1
									&& "1".equals(zoneIds.get(0) + "")) {
								myOrderShopDetailOrderSku.setStatus(1);
							} else if (orderShops.getIsRefund() != 2 && zoneIds.size() < 1) {
								// 如果存在表里,则判断act表的is_refund字段,如果为1则可以申请售后
								myOrderShopDetailOrderSku.setStatus(1);
							}
						} else if (status == 5 || status == 7 || status == 9) {
							// isRefund '是否退款:0否，1部分退款，2全部退款',
							List<Integer> zoneIds = actZoneGoodsMapper.selectIsRefund(myOrderSku.getGoodsId());
							// 如果该商品不在act表里,则可以售后
							if (orderShops.getIsRefund() != 2 && zoneIds != null && zoneIds.size() == 1
									&& "1".equals(zoneIds.get(0) + "")) {
								myOrderShopDetailOrderSku.setStatus(1);
							} else if (orderShops.getIsRefund() != 2 && zoneIds.size() < 1) {
								// 如果存在表里,则判断act表的is_refund字段,如果为1则可以申请售后
								myOrderShopDetailOrderSku.setStatus(1);
							}
						}
					}
				}
			}

			// 是否显示去评论的按钮
			// status=5：待评价
			if (status == 5) {
				GoodsComment goodsComment = new GoodsComment();
				goodsComment.setOrderSkuId(myOrderSku.getId());
				List<GoodsComment> commentList = goodsCommentMapper.selectCommentsBySkuId(goodsComment);
				if (commentList.size() < 1) {
					myOrderShopDetailOrderSku.setIsShowCommentButton(1);
				} else {
					myOrderShopDetailOrderSku.setIsShowCommentButton(0);
				}
			}
			myOrderShopDetailOrderSkuList.add(myOrderShopDetailOrderSku);
		}
		map.put("list",myOrderShopDetailOrderSkuList);
		return returnSkuId;
	}
	
	/**
	 * @Description: 获取物流信息
	 * @author xieyc
	 * @date 2018年8月8日 下午5:08:40
	 */
	public OrderStock orderStock(String id, String jdSkuNo) {
		OrderStock orderStock = new OrderStock();
		OrderShop orderShop1 = jdOrderService.getOrderById(id);// 根据主键获取oderShop表的信息
		
		Order order = jdOrderService.getOrderMainById(orderShop1.getOrderNo());// 根据订单号获取order_main
		
		MemberUserAddress mua = jdOrderService.getUserById(order.getmAddrId());// 根据order_main 表的m_addr_id获取 member_user_address表信息
		
		orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
		orderStock.setAddress(mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getAddress());// 收货地址
		
		List<OrderSku> listOrderSku = jdOrderService.getOrderSkuByOrderId(orderShop1.getOrderId());// 图片地址
		GoodsSku skuList = goodsSkuMapper.selectByPrimaryKey(listOrderSku.get(0).getSkuId());
		JSONObject jsonObj = JSONObject.fromObject(skuList.getValue());
		JSONArray personList = jsonObj.getJSONArray("url");
		String url = (String) personList.get(0);
		orderStock.setImgeUrl(url);

		// 查询当前order_send_info,jd_order_id 为0，不是京东的物流信息，是商家自配的物流
		OrderSendInfo orderSendInfo3 = new OrderSendInfo();
		orderSendInfo3.setOrderShopId(Integer.valueOf(id));
		OrderSendInfo orderSendInfo2 = orderSendInfoMapper.selectByOrderShopId(orderSendInfo3);

		Track track = new Track();
		List<Track> listTrack2 = new ArrayList<Track>();

		if (orderShop1.getDeliveryWay() == 1) {
			// 商家自配
			if (orderShop1.getdState() == 4) { // 已送达
				orderStock.setJd("0");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (orderShop1.getReceivedtime() != null) {
					String dateString = formatter.format(orderShop1.getReceivedtime());
					track.setMsgTime(dateString);
				} else {
					track.setMsgTime("");
				}

				track.setOperator("");
				track.setContent("已送达");// 内容
				listTrack2.add(track);
			} else { // 已发货
				orderStock.setJd("0");
				Track track2 = new Track();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (orderShop1.getSendTime() != null) {
					String dateString = formatter.format(orderShop1.getSendTime());
					track2.setMsgTime(dateString);
				} else {
					track2.setMsgTime("");
				}
				track2.setOperator("");
				track2.setContent("已发货");
				listTrack2.add(track2);
			}
			orderStock.setOrderTrack(listTrack2);
			return orderStock;
		} else if (orderShop1.getDeliveryWay() == 0) {
			// 速达
			if (orderSendInfo2 == null || orderSendInfo2.equals("")) {
				// 没有信息
				orderStock.setJd("0");
				track.setMsgTime("");
				track.setOperator("");
				track.setContent("待接单");// 内容
				listTrack2.add(track);
				orderStock.setOrderTrack(listTrack2);
				return orderStock;
			}
			if (orderSendInfo2.getdState() == 0) {

				track.setMsgTime("");// 时间
				track.setOperator(orderSendInfo2.getsName());// 配送员
				track.setContent("待发货");// 内容
				listTrack2.add(track);
				orderStock.setOrderTrack(listTrack2);
				orderStock.setJd("0"); // 自营物流
				return orderStock;
			} else if (orderSendInfo2.getdState() == 1) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				track.setMsgTime("");// 时间
				track.setOperator(orderSendInfo2.getsName());// 配送员
				track.setContent("待发货");// 内容
				listTrack2.add(track);

				Track track2 = new Track();
				if (orderSendInfo2.getDeliverTime() != null) {
					track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));// 时间
				} else {
					track2.setMsgTime("");// 时间
				}
				track2.setOperator(orderSendInfo2.getsName());// 配送员
				track2.setContent("发货中");// 内容
				listTrack2.add(track2);

				orderStock.setOrderTrack(listTrack2);
				orderStock.setJd("0"); // 自营物流
				return orderStock;
			} else if (orderSendInfo2.getdState() == 2) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				track.setMsgTime("");// 时间
				track.setOperator(orderSendInfo2.getsName());// 配送员
				track.setContent("待发货");// 内容
				listTrack2.add(track);

				Track track2 = new Track();
				if (orderSendInfo2.getDeliverTime() != null) {
					track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));// 时间
				} else {
					track2.setMsgTime("");// 时间
				}
				track2.setOperator(orderSendInfo2.getsName());// 配送员
				track2.setContent("发货中");// 内容
				listTrack2.add(track2);

				Track track3 = new Track();
				track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));// 时间
				track3.setOperator(orderSendInfo2.getsName());// 配送员
				track3.setContent("已送达");// 内容
				listTrack2.add(track3);

				orderStock.setOrderTrack(listTrack2);
				orderStock.setJd("0"); // 自营物流
				return orderStock;
			} else if (orderSendInfo2.getdState() == 3) {

				track.setMsgTime("");// 时间
				track.setOperator(orderSendInfo2.getsName());// 配送员
				track.setContent("已取消");// 内容
				listTrack2.add(track);
				orderStock.setOrderTrack(listTrack2);
				orderStock.setJd("0"); // 自营物流
				return orderStock;
			} else if (orderSendInfo2.getdState() == 4) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				track.setMsgTime("");// 时间
				track.setOperator(orderSendInfo2.getsName());// 配送员
				track.setContent("待发货");// 内容
				listTrack2.add(track);

				Track track2 = new Track();
				if (orderSendInfo2.getDeliverTime() != null) {
					track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));// 时间
				} else {
					track2.setMsgTime("");// 时间
				}
				track2.setOperator(orderSendInfo2.getsName());// 配送员
				track2.setContent("发货中");// 内容
				listTrack2.add(track2);

				Track track3 = new Track();
				track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));// 时间
				track3.setOperator(orderSendInfo2.getsName());// 配送员
				track3.setContent("已送达");// 内容
				listTrack2.add(track3);

				Track track4 = new Track();
				track4.setMsgTime("");// 时间
				track4.setOperator(orderSendInfo2.getsName());// 配送员
				track4.setContent("已结算");// 内容
				listTrack2.add(track4);

				orderStock.setOrderTrack(listTrack2);
				orderStock.setJd("0"); // 自营物流
				return orderStock;
			}
		} else if (orderShop1.getDeliveryWay() == 3) {
			try {
				String expressNo=orderShop1.getExpressNo();//物流单号
				BhDictItem bhDictItem=bhDictItemMapper.getByItemName(orderShop1.getExpressName());
				String  type=null;
				if(bhDictItem!=null){
					type=bhDictItem.getItemValue();
				}
				logger.info("orderStock type"+type);
				JSONObject jsonObject=QueryLogistics.getByExpressInfo(expressNo, type);//查询物流信息
				orderStock.setJd("1"); // 其他物流
				orderStock.setLogistics(jsonObject);// 返回给前端的物流配送信息
			} catch (Exception e) {
				e.printStackTrace();
			}
			return orderStock;
		} else if (orderShop1.getDeliveryWay() == 2) {
			// 京东物流
			OrderShop orderShop = new OrderShop();
			orderShop.setId(Integer.parseInt(id));
			// 查询京东物流信息，获取当前订单妥投信息，如果为妥投，就更改订单状态
			try {
				orderStock = jdOrderService.orderTrack(orderShop, Long.parseLong(jdSkuNo));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
			orderStock.setAddress(mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getAddress());// 收货地址
			orderStock.setImgeUrl(url);
			orderStock.setJd("2"); // 是京东
			if (StringUtils.isEmpty(orderStock.getJdOrderId())) {
				return orderStock;
			}
			return orderStock;
		}
		return orderStock;
	}
	public Set<Integer> selectGoodsIdSet(List<GoodsCart> list) {
		if (list.size() > 0) {
			Set<Integer> goodsIdSet = new HashSet<>();
			for (GoodsCart goodsCart : list) {
				goodsIdSet.add(goodsCart.getgId());
			}
			return goodsIdSet;
		} else {
			return null;
		}
	}
	/**
	 * 
	 * @Description: 根据物流单号查询物流信息
	 * @author xieyc
	 * @date 2018年9月11日 上午10:08:43 
	 * @param   
	 * @return  
	 *
	 */
	public JSONObject queryLogistics(String expressNo) throws Exception{
		// 其他物流
		String host = "https://wuliu.market.alicloudapi.com";
		String path = "/kdi";
		String method = "GET";
		String appcode = "232d013ef8244587a9a4f69cb2fcca47";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("no",expressNo);
		// 获取物流配送信息
		HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
		String Logistics = EntityUtils.toString(response.getEntity());
		JSONObject jsonObject = null;
		if (StringUtils.isNotBlank(Logistics)) {
			jsonObject = JSONObject.fromObject(Logistics);
		} else {
			jsonObject = JSONObject.fromObject("{\"msg\":\"没有物流信息\"}");
		}
		return  jsonObject;
	}
}
