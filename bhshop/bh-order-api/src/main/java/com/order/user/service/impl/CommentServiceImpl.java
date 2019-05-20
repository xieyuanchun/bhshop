package com.order.user.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.domain.NewsfeedWithMeInfo;
import com.alipay.api.domain.ShopCategoryConfigInfo;
import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.CommentSimple;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsCommentWithBLOBs;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MyGoodsComment;
import com.bh.goods.pojo.MyWaitcommentlist;
import com.bh.order.mapper.OrderCollectionDocMapper;
import com.bh.order.mapper.OrderExpressTypeMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderPaymentMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSimple;
import com.bh.order.pojo.OrderSku;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.jdbc2.optional.CallableStatementWrapper;
import com.order.user.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private GoodsCommentMapper goodsCommentMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private MemberShopMapper memberShopMember;
	
	@Autowired
	private MemberSendMapper MemberSendMapper;

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
	private OrderCollectionDocMapper orderCollectionDocMapper;
	
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	
	@Autowired
	private MemberMapper memberMapper;

	// 获得OrderSku
	public OrderSku selectOrderSkuById(Integer orderSkuId) throws Exception {
		OrderSku orderSku = null;
		orderSku = orderSkuMapper.selectByPrimaryKey(orderSkuId);
		double sellPrice = (double) orderSku.getSkuSellPriceReal()/100;
		double markPrice = (double) orderSku.getSkuMarketPrice()/100;
		Object obj = JsonUtils.stringToObject(orderSku.getSkuValue());
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderSku.getShopId());
		
		orderSku.setRealMarketPrice(markPrice);
		orderSku.setRealSellPrice(sellPrice);
		orderSku.setValueObj(obj);
		orderSku.setShopName(memberShop.getShopName());
		return orderSku;
	}
	
	// 2017-11-1 -订单详情
		@SuppressWarnings("unused")
		public OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception {

			try {

				OrderShop orderShop2 = new OrderShop();
				Set<String> set = new HashSet<>();

				orderShop2.setId(orderShop.getId());
				OrderShop orderShops = new OrderShop();
				orderShops = orderShopMapper.selectByOrderShopId(orderShop2);

				OrderSku orderSku = new OrderSku();
				orderSku.setOrderId(orderShops.getOrderId());
				orderSku.setShopId(orderShops.getShopId());

				double realDeliveryPrice = 0.0;// 总邮费
				int deliveryPrice = 0;
				double allPrice = 0.0;

				Order order = new Order();
				order = orderMapper.selectByPrimaryKey(orderShops.getOrderId());
				OrderSimple orderSimple = new OrderSimple();
				List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBy1(orderSku);

				MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.getShopId());
				orderShops.setShopName(memberShop.getShopName());

				switch (orderShops.getStatus()) {
				case 1:
					orderShops.setMystatus(Contants.orderShopStatu1);
					break;
				case 2:
					orderShops.setMystatus(Contants.orderShopStatu2);
					break;
				case 3:
					orderShops.setMystatus(Contants.orderShopStatu3);
					break;
				case 4:
					orderShops.setMystatus(Contants.orderShopStatu4);
					break;
				case 5:
					orderShops.setMystatus(Contants.orderShopStatu5);
					break;
				case 6:
					orderShops.setMystatus(Contants.orderShopStatu6);
					break;
				case 7:
					orderShops.setMystatus(Contants.orderShopStatu7);
					break;
				case 8:
					orderShops.setMystatus(Contants.orderShopStatu8);
					break;
				default:
					orderShops.setMystatus(Contants.orderShopStatu1);
				}

				double price = 0.0;
				int goodsNum = 0;
				for (int n = 0; n < orderSkuList.size(); n++) { // 对orderSku进行分解
					set.add(String.valueOf(orderSkuList.get(n).getGoodsId()));// 将商品的id捡出来
					String value = orderSkuList.get(n).getSkuValue();
					Object object = JsonUtils.stringToObject(value);
					orderSkuList.get(n).setValueObj(object);// 将skuValue转为object

					double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					orderSkuList.get(n).setRealSellPrice(realSellPrice);// 销售价格double=int/100

					double markPrice = (double) orderSkuList.get(n).getRealMarketPrice() / 100;
					orderSkuList.get(n).setRealMarketPrice(markPrice);

					Integer num = orderSkuList.get(n).getSkuNum();
					double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
					double sellPrice1 = sellPrice * num;
					goodsNum = goodsNum + num;
					price = price + sellPrice1;


					// 2017-11-30
					boolean flag = false;
					int a[]={2,5,7};
					for (int s : a) {
						if (orderShops.getStatus().equals(s)) {
						   flag = true;
						}
					}
					Integer id = orderSkuList.get(n).getId();
					OrderRefundDoc doc = new OrderRefundDoc();
					OrderRefundDoc doc2 = new OrderRefundDoc();
					doc.setOrderSkuId(id);
					doc2 = orderRefundDocMapper.selectByOrderSkuId(doc);
					if (flag) {
						if (doc2 == null) {
							//可申请退款
							orderSkuList.get(n).setStatus(Contants.refund41);
							orderSkuList.get(n).setMystatus(Contants.refund4);
						} else {
								
							   String re = ",可重新申请退款";
								switch (doc2.getStatus()) {
								case 0:
									orderSkuList.get(n).setStatus(0);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.stautName0);
									break;
								case 1:
									orderSkuList.get(n).setStatus(1);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName1 +re);
									break;
								case 2:
									orderSkuList.get(n).setStatus(2);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName2);
									break;
								case 3:
									orderSkuList.get(n).setStatus(3);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName3+re);
									break;
								default:
									break;
								}
							}
					}else{
						//如果orderShop的status不在2,5,7,则这个orderShop的sku是否在退款表里面，如果在，则显示相应的内容，如果不在，则设为5
						if (doc2 == null) {
							orderSkuList.get(n).setStatus(Contants.refund51);
							orderSkuList.get(n).setMystatus(Contants.refund5);
						} else {
							   String re = ",可重新申请退款";
								switch (doc2.getStatus()) {
								case 0:
									orderSkuList.get(n).setStatus(0);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.stautName0);
									break;
								case 1:
									orderSkuList.get(n).setStatus(1);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName1 +re);
									break;
								case 2:
									orderSkuList.get(n).setStatus(2);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName2);
									break;
								case 3:
									orderSkuList.get(n).setStatus(3);
									orderSkuList.get(n).setMystatus(Contants.statusName + Contants.statuName3+re);
									break;
								default:
									break;
								}
							}
					}
					// end
					
					
					
					}
					
				
				orderShops.setOrderSku(orderSkuList);
				List<String> myList = new ArrayList<>(set);
				List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(myList);
				for (int n = 0; n < goodsList.size(); n++) {
					int deliveryPrice1 = goodsList.get(n).getDeliveryPrice();
					double realPrice = (double) deliveryPrice1 / 100;
					realDeliveryPrice = realDeliveryPrice + realPrice;
					deliveryPrice = deliveryPrice + deliveryPrice1;
				}
				double allPrice1 = 0.0;
				allPrice1 = realDeliveryPrice + price;
				allPrice = allPrice + allPrice1;

				double realgDe = (double) orderShops.getgDeliveryPrice() / 100;
				double realOrderPrice = (double) orderShops.getOrderPrice() / 100;
				double totalproce = realOrderPrice - realgDe;

				orderShops.setRealgDeliveryPrice(realgDe);
				orderShops.setDeliveryPrice(deliveryPrice);
				orderShops.setRealDeliveryPrice(realDeliveryPrice);
				orderShops.setAllPrice(totalproce);
				orderShops.setGoodsNumber(goodsNum);
				orderShops.setRealOrderPrice(realOrderPrice);

				orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
				orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
				orderSimple.setId(order.getId());
				orderSimple.setPaymentId(order.getPaymentId());// 支付方式
				orderSimple.setPaymentStatus(order.getPaymentStatus());
				orderSimple.setAddtime(order.getAddtime());
				orderSimple.setOrderPrice(order.getOrderPrice());

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

	//添加商品评价
	public int insertGoodsComment(List<CommentSimple> commentSimples,Integer mId) throws Exception {
		int row = 0;
		for (int i = 0; i < commentSimples.size(); i++) {
			
			GoodsCommentWithBLOBs goodsCommentWithBLOB = new GoodsCommentWithBLOBs();
			OrderSendInfo orderSendInfo = new OrderSendInfo();
			
			Integer id = commentSimples.get(i).getId();
			Integer start = commentSimples.get(i).getStar();
			String remark = commentSimples.get(i).getRemark();
			String pic[] = commentSimples.get(i).getImages();
			Integer add = commentSimples.get(i).getAdd();
			Integer speedLevel = commentSimples.get(i).getSpeedLevel();
			Integer sServiceLevel = commentSimples.get(i).getsServiceLevel();
			Integer packLevel = commentSimples.get(i).getPackLevel();
			Integer notname = commentSimples.get(i).getNotname();
			
			
			OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(id);
			OrderSendInfo orderSendInfo2 = new OrderSendInfo();
			orderSendInfo2.setmId(mId);
			orderSendInfo2.setOrderShopId(orderSku.getOrderShopId());
			List<OrderSendInfo> sendInfo = orderSendInfoMapper.getSendInfo(orderSendInfo2);
			
			if (start != null) {
				goodsCommentWithBLOB.setStar(start);
				switch (start) {
				case 5:
					
					
				case 4:
					Integer i2 =1;
					byte le = (byte)i2.intValue();
					goodsCommentWithBLOB.setLevel(le);
					break;
				case 3:
					
				case 2:
					Integer i3 =2;
					byte le1 = (byte)i3.intValue();
					goodsCommentWithBLOB.setLevel(le1);
					break;
				case 1:
						
				case 0:
					Integer i4 =3;
					byte le2 = (byte)i4.intValue();
					goodsCommentWithBLOB.setLevel(le2);
					break;
					
				default:
					break;
				}
			}if (StringUtils.isNotEmpty(remark)) {
				String remark2 = URLEncoder.encode(remark, "utf-8");
				
				goodsCommentWithBLOB.setDescription(remark2);
			}if (pic.length > 0) {
				
				
				String s= org.apache.commons.lang.StringUtils.join(pic, ',');
				
				//String pics = Arrays.toString(pic);
				goodsCommentWithBLOB.setImage(s);
			}if (add == null) {
				add =0;
			}
			if (speedLevel != null) {
				orderSendInfo.setSpeedLevel(speedLevel);
			}if (sServiceLevel !=null) {
				orderSendInfo.setsServiceLevel(sServiceLevel);
			}if (packLevel !=null) {
				orderSendInfo.setPackLevel(packLevel);
			}if (notname != null) {
				goodsCommentWithBLOB.setNotname(notname);
			}
			
			goodsCommentWithBLOB.setAddtime( new Date());
			goodsCommentWithBLOB.setGoodsId(orderSku.getGoodsId());
			goodsCommentWithBLOB.setShopId(orderSku.getShopId());
			goodsCommentWithBLOB.setmId(mId);
			goodsCommentWithBLOB.setOrderId(orderSku.getOrderId());
			goodsCommentWithBLOB.setOrderSkuId(id);
			goodsCommentWithBLOB.setSkuValue(orderSku.getSkuValue());
			
			orderSendInfo.setOcTime(new Date());
			
			goodsCommentWithBLOB.setIsDel(0);
			GoodsComment goodsComment = new GoodsComment();
			goodsComment.setmId(mId);
			goodsComment.setIsAddEvaluate(add);
			goodsComment.setOrderSkuId(id);
			GoodsComment g1 = goodsCommentMapper.selectByParams(goodsComment);
			
			if (add == 0) {			
				OrderSku orderSku2 = new OrderSku();
				orderSku2 = orderSkuMapper.selectByPrimaryKey(id);
				
				OrderSku record1 = new OrderSku();
				record1.setOrderShopId(orderSku2.getOrderShopId());
				OrderSku record2 = new OrderSku();
				record2.setOrderId(goodsCommentWithBLOB.getOrderId());
				List<OrderSku> t = orderSkuMapper.selectOrderSkuByParams2(record1);//如果该商家下的所有商品已评价，则将order_shop的表的status=7
				List<OrderSku> t1 = orderSkuMapper.selectOrderSkuByParams2(record2);//如果该orderId下面的所有商品都已评价，则ordermain 表订单状态status·变成已完成3，用户评价的时候控制
				
				goodsCommentWithBLOB.setIsAddEvaluate(0);
				goodsCommentWithBLOB.setReid(0);
				if (g1 != null) {
					goodsComment.setId(g1.getId());
					goodsCommentWithBLOB.setId(g1.getId());
					row = goodsCommentMapper.updateByPrimaryKeySelective(goodsCommentWithBLOB);
				}else{
					row = goodsCommentMapper.insertSelective(goodsCommentWithBLOB);
				}
				try {
					if (row == 1) {
						if (t.size() > 1) {
							//多个商品
							List<OrderSku> tt = orderSkuMapper.selectOrderSkuByOrderShopId(orderSku2.getOrderShopId());//如果该商家下的所有商品已评价，则将order_shop的表的status=7
							if (tt.size()>0) {
								int size=tt.size();
								int tSize=t.size()-1;
								//如果多个商品已评论完，且现在评论的商品是最后一个商品，则更改orderShop和orderMain的状态
								if (size==tSize) {
									OrderShop orderShop = new OrderShop();//设置orderShop的状态为：7
									orderShop.setId(orderSku2.getOrderShopId());
									orderShop.setStatus(Contants.IS_COMPLISH);
									orderShopMapper.updateByPrimaryKeySelective(orderShop);
									
									Order order = new Order();
									order.setId(goodsCommentWithBLOB.getOrderId());
									order.setStatus(3);
									order.setCompletetime(new Date());
									orderMapper.updateByPrimaryKeySelective(order);
								}
							}
						
						}else if(t.size()==1){
							OrderShop orderShop = new OrderShop();//设置orderShop的状态为：7
							orderShop.setId(orderSku2.getOrderShopId());
							orderShop.setStatus(Contants.IS_COMPLISH);
							orderShopMapper.updateByPrimaryKeySelective(orderShop);
						}
						/********ordermain 表订单状态status·变成已完成3，用户评价的时候，要判断这个订单下面的所有商家订单是否全部已评价，如果全部已评，status改成3*********/
						if (t1.size()==1) {
							//如果只有一个商品
							Order order = new Order();
							order.setId(goodsCommentWithBLOB.getOrderId());
							order.setStatus(3);
							order.setCompletetime(new Date());
							orderMapper.updateByPrimaryKeySelective(order);
						}
						OrderSku record = new OrderSku();
						record.setId(id);
						record.setIsRefund(1);
						orderSkuMapper.updateByPrimaryKeySelective(record);
						Integer goodId = goodsCommentWithBLOB.getGoodsId();//前端评论的时候，每新增一条评论，就要给商品的评论次数＋1
						Goods good = goodsMapper.selectByPrimaryKey(goodId);
						Goods goods = new Goods();
						goods.setId(goodId);
						if (good != null) {
							goods.setComments(good.getComments()+1);
							goodsMapper.updateByPrimaryKeySelective(goods);
						}
						if (sendInfo.size()>0) {
							orderSendInfo.setId(sendInfo.get(0).getId());
							orderSendInfoMapper.updateByPrimaryKeySelective(orderSendInfo);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}if (add == 1) {
				goodsCommentWithBLOB.setIsAddEvaluate(1);
				GoodsCommentWithBLOBs goodsCommentWithBLOBs = new GoodsCommentWithBLOBs();
				goodsCommentWithBLOBs.setmId(mId);
				goodsCommentWithBLOBs.setIsAddEvaluate(0);
				goodsCommentWithBLOBs.setOrderSkuId(id);
				GoodsComment g = goodsCommentMapper.selectByParams(goodsCommentWithBLOBs);
				goodsCommentWithBLOB.setReid(g.getId());
				
				if (g1 != null) {
					row = goodsCommentMapper.updateByPrimaryKeySelective(goodsComment);
				}else{
					row = goodsCommentMapper.insertSelective(goodsCommentWithBLOB);
				}
				
				
			}
			
		}
		return row;
	}
	
	/****该商品评论*****/
	public List<GoodsComment> getListComment(List<String> ids){
		List<GoodsComment> list = new ArrayList<>();
		list = goodsCommentMapper.selectCommentsByIds(ids);
		return list;
	}

	// 2017-11-3 -显示需要评价的商品列表
	public PageBean<OrderShop> showWaitCommentList(OrderShop orderShop,Integer page ,Integer rows) throws Exception {

		try {
			List<OrderShop> orderShopList = new ArrayList<>();
		
			PageHelper.startPage(page, rows, true);

			orderShopList = orderShopMapper.selectOrderShopBySelect(orderShop);// and m_id = ? and status=5 group by order_id order by order_id desc
																				

			for (int i = 0; i < orderShopList.size(); i++) {
				OrderShop orderShop2 = new OrderShop();
				orderShop2.setmId(orderShop.getmId());
				orderShop2.setOrderId(orderShopList.get(i).getOrderId());
				orderShop2.setStatus(Contants.IS_WAIT_EVALUATE);
				List<OrderShop> orderShops = orderShopMapper.selectOrderShopByOrderIds(orderShop2);
				for (int m = 0; m < orderShops.size(); m++) {
					OrderSku orderSku = new OrderSku();
					orderSku.setOrderId(orderShops.get(m).getOrderId());
					orderSku.setShopId(orderShops.get(m).getShopId());
					List<OrderSku> orderSkuList = orderSkuMapper.selectOrderShopBy1(orderSku);
					MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShops.get(m).getShopId());
					orderShops.get(m).setShopName(memberShop.getShopName());
					orderShops.get(m).setOrderSku(orderSkuList);
					
					switch (orderShops.get(m).getStatus()) {
					case 1:  orderShops.get(m).setMystatus(Contants.orderShopStatu1); break;
					case 2:  orderShops.get(m).setMystatus(Contants.orderShopStatu2);break;
					case 3:  orderShops.get(m).setMystatus(Contants.orderShopStatu3);break;
					case 4:  orderShops.get(m).setMystatus(Contants.orderShopStatu4);break;
					case 5:  orderShops.get(m).setMystatus(Contants.orderShopStatu5);break;
					case 6:  orderShops.get(m).setMystatus(Contants.orderShopStatu6);break;
					case 7:  orderShops.get(m).setMystatus(Contants.orderShopStatu7);break;
					case 8: orderShops.get(m).setMystatus(Contants.orderShopStatu8);break;
					default: orderShops.get(m).setMystatus(Contants.orderShopStatu1);
			    	}
					
					double price = 0.0;
					int goodsNum = 0;
					for (int n = 0; n < orderSkuList.size(); n++) {
						String value = orderSkuList.get(n).getSkuValue();
						Object object = JsonUtils.stringToObject(value);
						orderSkuList.get(n).setValueObj(object);
						double realSellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						orderSkuList.get(n).setRealSellPrice(realSellPrice);
						double markPrice = (double) orderSkuList.get(n).getSkuMarketPrice()/100;
						orderSkuList.get(n).setRealMarketPrice(markPrice);
						Integer num = orderSkuList.get(n).getSkuNum();
						double sellPrice = (double) orderSkuList.get(n).getSkuSellPriceReal() / 100;
						double sellPrice1 = sellPrice * num;
						goodsNum = goodsNum + num;
						price = price + sellPrice1;
					}
					
					double realgDe = (double) orderShops.get(m).getgDeliveryPrice()/100;
					
					double realOrderPrice = (double) orderShops.get(m).getOrderPrice()/100;
					double totalproce = realgDe + realOrderPrice;
					orderShops.get(m).setRealgDeliveryPrice(realgDe);
					orderShops.get(m).setGoodsNumber(goodsNum);
					orderShops.get(m).setRealOrderPrice(realOrderPrice);
					orderShops.get(m).setAllPrice(totalproce);
				}
				orderShopList.get(i).setOrderShopList(orderShops);
				orderShopList.get(i).setShopOrderNo(null);

				Order order = orderMapper.selectByPrimaryKey(orderShopList.get(i).getOrderId());
				OrderSimple orderSimple = new OrderSimple();
				orderSimple.setId(order.getId());
				orderSimple.setPaymentId(order.getPaymentId());// 支付方式
				orderSimple.setPaymentStatus(order.getPaymentStatus());
				orderSimple.setAddtime(order.getAddtime());
				orderSimple.setOrderPrice(order.getOrderPrice());
				double realOrderPrice = (double) order.getOrderPrice() / 100;
				double realGdel = (double) order.getDeliveryPrice()/100;
				orderSimple.setG_deliveryPrice(realGdel);
				orderSimple.setRealOrderPrice(realOrderPrice);
				orderSimple.setPaymentIdName(String.valueOf(order.getPaymentId()));
				orderSimple.setPaymentStatusName(String.valueOf(order.getPaymentStatus()));
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

			PageInfo<OrderShop> info = new PageInfo<>(orderShopList);

			PageBean<OrderShop> pageBean = null;

			pageBean = new PageBean<>(orderShopList);
			pageBean.setTotal(info.getTotal());
			pageBean.setList(info.getList());

			return pageBean;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public List<MyWaitcommentlist> selectMyWaitCommentList(Integer mId)throws Exception{
		List<MyWaitcommentlist> comments = goodsCommentMapper.selectMyWaitCommentList(mId);
		return comments;
		
	}
	
	/**********显示已评价商品的列表*************/
	public List<MyGoodsComment> showCommentList(GoodsComment comment,Integer page ,Integer rows) throws Exception{
		List<MyGoodsComment> comments = new ArrayList<>();
		int currentPag=(page-1) * rows;
		comments = goodsCommentMapper.selectGoodsComment(comment.getmId(),currentPag,rows);
		if (comments.size()>0) {
			for(int i=0;i<comments.size();i++){
				if (StringUtils.isNotEmpty(comments.get(i).getLevelName())) {
					Object value = JsonUtils.stringToObject(comments.get(i).getLevelName());
					comments.get(i).setSkuValueObj(value);
					comments.get(i).setGoodsImage(RegExpValidatorUtils.returnNewString(comments.get(i).getLevelName()));
				}
			     Byte level = comments.get(i).getLevel();
			     switch (level) {
				case 1:
					comments.get(i).setLevelName(Contants.levelName1);
					break;
	            case 2:
	            	comments.get(i).setLevelName(Contants.levelName2);
					break;	
	           case 3:
	        	   comments.get(i).setLevelName(Contants.levelName3);
					break;
	           
				default:
					break;
				}
				comments.get(i).setmName(URLDecoder.decode(comments.get(i).getmName(), "utf-8"));
				comments.get(i).setSkuPrice(MoneyUtil.fen2Yuan(comments.get(i).getSkuPrice()));
				try {
					if (comments.get(i).getDescription()!=null) {
						comments.get(i).setDescription(URLDecoder.decode(comments.get(i).getDescription(),"utf-8"));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				 if(comments.get(i).getMyImage()!=null){ //评价图片转数组
						List<String> bs = Arrays.asList(comments.get(i).getMyImage().split(","));
						final int size = bs.size();
						String[] arr = (String[])bs.toArray(new String[size]);
						comments.get(i).setImageStr(arr);
					}
			}
		}
		
		return comments;
	}
}
