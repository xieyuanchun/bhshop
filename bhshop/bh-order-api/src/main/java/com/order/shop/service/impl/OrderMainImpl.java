package com.order.shop.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.NewsfeedWithMeInfo;
import com.bh.admin.pojo.order.SmallPayRefundPojo;
import com.bh.config.Contants;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderLogMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.BhDictItem;
import com.bh.order.pojo.MyOrderSkuPojo;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderLog;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.queryLogistics.QueryLogistics;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberNoticeMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.ScoreRuleExtMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberNotice;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopAdmin;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.ScoreRuleExt;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.IDUtils;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.utils.MD5Util1;
import com.bh.utils.PageBean;
import com.bh.utils.pay.HttpService;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import com.order.enums.PayInterfaceEnum;
import com.order.enums.RefundReasonEnum;
import com.order.enums.UnionPayInterfaceEnum;
import com.order.shop.service.OrderMainService;
import com.order.vo.UnionPayRefundVO;
import com.print.controller.HttpUtils;
import com.wechat.service.WechatTemplateMsgService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class OrderMainImpl implements OrderMainService{
	private static final Logger logger = LoggerFactory.getLogger(OrderMainImpl.class);
	@Autowired
	OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderMapper mapper; //订单
	@Autowired
	private OrderSkuMapper SkuMapper; //订单商品
	@Autowired
	private OrderShopMapper orderShopMapper; //商家订单
	@Autowired
	private MemberShopAdminMapper adminMapper; //商家管理员
	@Autowired
	private MemberShopMapper shopMapper; //店铺
	@Autowired
	private GoodsMapper goodsMapper;	//商品
	@Autowired
	private OrderRefundDocMapper refundMapper; //退款单
	@Autowired
	private MemberMapper memberMapper; // 用户
	@Autowired
	private MemberUserMapper membUserMapper; // 个人会员
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; // 商品属性
	@Autowired
	private MemberUserAddressMapper addressMapper; // 用户
	@Autowired
	private MemberSendMapper memberSendMapper;//配送员的mapper
	@Autowired
	private MemberNoticeMapper memberNoticeMapper;//消息的mapper
	@Autowired
	private OrderSendInfoMapper sendInfoMapper; // 配送单
	@Autowired
	private OrderTeamMapper orderTeamMapper; // 拼团单
	@Autowired
	private OrderSendInfoMapper ordersendMapper; //配送订单
	@Autowired
	private MemberUserMapper memberUserMapper;
	
	@Autowired
	private OrderShopMapper shopmapper; 
	
	@Autowired
	private OrderSkuMapper skumapper; 
	
	@Autowired
	private OrderMapper ordermapper; 
	
	@Autowired
	private MemberUserAddressMapper muamapper; 

	
	@Autowired
	private MemberShopMapper memberShopMapper; 
	
	@Autowired
	private OrderLogMapper orderLogMapper;
	
	@Autowired
	private CouponMapper couponMapper;
	
	@Autowired
	private CouponLogMapper couponLogMapper;
	
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	@Autowired
	private WalletMapper walletMapper;
    @Autowired
	private WalletLogMapper walletLogMapper;
    @Autowired
	private OrderRefundDocMapper orderRefundDocMapper;
	@Override
	public Order getShopOrderById(String id) throws Exception {
		return mapper.selectByPrimaryKey(Integer.parseInt(id));
	}


	/**
	 * 商家后台订单列表
	 */
	@Override
	public PageBean<OrderShop> orderList(int shopId, String order_no, String status, String currentPage, String pageSize,
			String startTime, String endTime, String isJd)throws Exception {
		List<OrderShop> list = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
	   endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		if(!StringUtils.isEmptyOrWhitespaceOnly(status)){
			list = orderShopMapper.orderShopList(shopId, Integer.parseInt(status), order_no, sf.parse(startTimes), sf.parse(endTimes), isJd);		
			getRefundStatus(list);
		}else{
			list = orderShopMapper.orderShopAllList(shopId, order_no, sf.parse(startTimes), sf.parse(endTimes), isJd);
			getRefundStatus(list);
		}
		PageBean<OrderShop> page = new PageBean<>(list);
		return page;
	}
	
	public void getRefundStatus(List<OrderShop> list){
		if(list.size()>0){
			for(OrderShop orderShop : list){
				double allPrice = (double)orderShop.getOrderPrice()/100;//订单总价“分”转化成“元”
				orderShop.setAllPrice(allPrice);
				
				double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费“分”转化成“元”
				orderShop.setRealDeliveryPrice(realDeliveryPrice);
				
				double realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;//邮费“分”转化成“元”
				orderShop.setRealgDeliveryPrice(realgDeliveryPrice);
				
				Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
				orderShop.setAddtime(order.getAddtime()); //下单时间
				
				OrderRefundDoc refund = refundMapper.getByOrderShopId(orderShop.getId());
				if(refund!=null){
					orderShop.setRefundStatus(refund.getStatus());
				}
				
				MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
				if(address!=null){
					orderShop.setFullName(address.getFullName()); //买家姓名
					orderShop.setTel(address.getTel());
					String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+address.getAddress();
					orderShop.setAddress(userAddress);
				}
				
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				Map<String, Object> map = new HashMap<>();
				if(skuList.size()>0){
					GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
					if(goodsSku.getJdSkuNo()!=null){
						map.put("jdSkuNo", goodsSku.getJdSkuNo()); //京东商品sku
					}else{
						map.put("jdSkuNo", null); //京东商品sku
					}
					if(goodsSku.getJdPrice()==0){
						map.put("jdPirce", null);
					}else{
						map.put("jdPirce", (double)goodsSku.getJdPrice()/100); //京东价
					}
					if(goodsSku.getJdProtocolPrice()==0){
						map.put("jdProtocolPrice", null); 
					}else{
						map.put("jdProtocolPrice", (double)goodsSku.getJdProtocolPrice()/100); //协议价
					}
					map.put("num", skuList.get(0).getSkuNum()); //数量
					map.put("price", (double)skuList.get(0).getSkuSellPriceReal()/100); //单价
					
					JSONObject jsonObject = JSONObject.fromObject(skuList.get(0).getSkuValue());
					//orderSku.setValueObj(jsonObject);
					map.put("value", jsonObject);
					
					Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
					if(goods!=null){
						orderShop.setTeamNum(goods.getTeamNum());
					}
				}
				orderShop.setGoodsSkuInfo(map);
				
				Member member = memberMapper.selectByPrimaryKey(orderShop.getmId()); //用户注册信息
				Map<String, Object> mbMap  = new LinkedHashMap<>();
				if(member!=null){
					mbMap.put("id", member.getId());
					mbMap.put("userNick", member.getUsername());
					mbMap.put("phone", member.getPhone());
				}else{
					mbMap.put("id", null);
					mbMap.put("userNick", null);
					mbMap.put("phone", null);
				}
				MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShop.getmId());
				if(memberUser!=null){
					mbMap.put("status", getStatus(memberUser.getStatus())); //状态
				}else{
					mbMap.put("status", null);
				}
				
				orderShop.setUserInfo(mbMap);
				
				StringBuffer buffer = new StringBuffer(); //拼接商品名称
				if(skuList.size()>0){
					orderShop.setGoodsImage(skuList.get(0).getSkuImage());
					for(OrderSku sku : skuList){
						buffer.append(sku.getGoodsName()+",");
					}
				}
				if(buffer.toString().length()>0){
					String goodsName = buffer.toString().substring(0, buffer.toString().length()-1);
					orderShop.setGoodsName(goodsName);
				}
				
				OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderShop.getOrderNo());
				if(orderTeam!=null){
					orderShop.setGroupType(orderTeam.getStatus()); //拼团状态'-1 拼单失败 0拼团中 1成功',
					int robotNum = orderTeamMapper.countByTeamNoAndType(orderTeam.getTeamNo(), 1);
					orderShop.setRobotNum(robotNum);
					
					int realNum = orderTeamMapper.countByTeamNoAndType(orderTeam.getTeamNo(), 0);
					orderShop.setRealNum(realNum);
				}
			}
		}
	}
	
	/**
	 * 商家后台订单详情PC端物流信息
	 */
	@Override
	public  OrderShop  getOrderGoodsDetails(String id) throws Exception {
	   
		    OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
	        //设置快递配送信息参数
		    Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
		    String host = "https://wuliu.market.alicloudapi.com";
   	        String path = "/kdi";
   	        String method = "GET";
   	        String appcode = "232d013ef8244587a9a4f69cb2fcca47";
   	        Map<String, String> headers = new HashMap<String, String>();
   	        headers.put("Authorization", "APPCODE " + appcode);
   	        Map<String, String> querys = new HashMap<String, String>();
   	        querys.put("no", order.getExpressNo());
    
   	    
       try{
        	//获取物流配送信息
   	        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
   	        String Logistics = EntityUtils.toString(response.getEntity());
   	    	JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
   	    	orderShop.setLogistics(jsonObject2);//物流配送信息
   	    	
		    orderShop.setExpressName(order.getExpressName()); //物流公司名字
		    orderShop.setExpressNo(order.getExpressNo());   //物流单号
		    orderShop.setAddtime(order.getAddtime()); //下单时间
		    orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		    if(order.getPaymentId()!=null){
			     orderShop.setPayWay(order.getPaymentId()); //支付方式:1货到付款 2 支付宝支付  3微信支付'
		    }
		    MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		    String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+address.getAddress();
		    String tel = address.getTel();
		    if(address.getTelephone()!=null){
		      	tel = address.getTel() +" "+ address.getTelephone();
		    }
		    orderShop.setFullName(address.getFullName());
		    orderShop.setTel(tel);
		    orderShop.setAddress(addr);
		
		
		    double allPrice = (double)orderShop.getOrderPrice()/100;//订单总价“分”转化成“元”
		    orderShop.setAllPrice(allPrice);
		
		    double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费“分”转化成“元”
		    orderShop.setRealDeliveryPrice(realDeliveryPrice);
		
		    double realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;//邮费“分”转化成“元”
		    orderShop.setRealgDeliveryPrice(realgDeliveryPrice);
		    
		    double realSavePrice = (double)orderShop.getSavePrice()/100;//滨惠豆的折扣“分”转化成“元”
		    orderShop.setRealSavePrice(realSavePrice);
		    
		 /*   if(order.getCouponsId()>0){//xieyc
			    CouponLog couponLog = couponLogMapper.selectByPrimaryKey(order.getCouponsId());
			    Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
			    double realCouponsPrice = (double)coupon.getAmount()/100;//优惠卷的折扣“分”转化成“元”
			    orderShop.setRealCouponsPrice(realCouponsPrice);
			}else{
				 orderShop.setRealCouponsPrice(0);
			}*/
		
		    List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
		    for(OrderSku orderSku : skuList){
			    double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100;//市场价格“分”转化成“元”
			    double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
			    orderSku.setRealMarketPrice(realMarketPrice);
			    orderSku.setRealSellPrice(realSellPrice);
			
			    JSONObject jsonObject = JSONObject.fromObject(orderSku.getSkuValue());
			    orderSku.setValueObj(jsonObject);
			
			    MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
			    orderSku.setShopName(shop.getShopName());
		     }
		     orderShop.setOrderSku(skuList);
   	    }catch(Exception e){
	       e.printStackTrace();
	    }
	    return orderShop;
	}
	
	/**
	 * 平台后台订单详情
	 */
	@Override
	public Order getOrderDetails(String id) throws Exception {
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		
		MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+address.getAddress();
		String tel = address.getTel();
		if(address.getTelephone()!=null){
			tel = address.getTel() +" "+ address.getTelephone();
		}
		order.setFullName(address.getFullName());
		order.setTel(tel);
		order.setAddress(addr);
		
		List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		if(orderShopList!=null){
			for(OrderShop orderShop : orderShopList){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(OrderSku orderSku : skuList){
					double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100;//市场价格“分”转化成“元”
					double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					orderSku.setRealMarketPrice(realMarketPrice);
					orderSku.setRealSellPrice(realSellPrice);
					MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
					orderSku.setShopName(shop.getShopName());
				}
				orderShop.setOrderSku(skuList);
				double realOrderPrice = (double)orderShop.getOrderPrice()/100;//商家订单总价“分”转化成“元”
				orderShop.setRealOrderPrice(realOrderPrice);
				
				double realDeliveryPrice = (double)order.getDeliveryPrice()/100; //邮费“分”转化成“元”
				order.setRealDeliveryPrice(realDeliveryPrice);
				
				double realSavePrice = (double) order.getSavePrice()/100;
				order.setRealSavePrice(realSavePrice);
				
				/*if(order.getCouponsId()>0){//xieyc
					CouponLog couponLog = couponLogMapper.selectByPrimaryKey(order.getCouponsId());
					Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
					double realCouponsPrice = (double)coupon.getAmount()/100;//优惠卷的折扣“分”转化成“元”
					orderShop.setRealCouponsPrice(realCouponsPrice);
				}else{
					orderShop.setRealCouponsPrice(0);
				}*/
			}
			double realSavePrice = (double) order.getSavePrice()/100;
			order.setRealSavePrice(realSavePrice);
			double realPrice = (double)order.getOrderPrice()/100;//订单总价“分”转化成“元”
			order.setRealPrice(realPrice);
			order.setOrderShops(orderShopList);
		}
		return order;
	}
	
	/**
	 * 统计商家订单总价
	 * @param orderId
	 * @param adminId
	 * @return
	 */
	public double orderPrice(int orderId, int adminId){
		double total = 0.00;
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(orderId, admin.getShopId());
		List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
		for(int i=0; i<skuList.size(); i++){
			double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
			total += realSellPrice * skuList.get(i).getSkuNum();
		}
		return total;
	}
	
	/**
	 * 商家整单抛单操作
	 */
	@Override
	public OrderShop castSheet(String id, String deliveryPrice,String userJson) throws Exception {
		Integer price = (int)(Double.parseDouble(deliveryPrice)*100);
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());

		orderShop.setdState(Contants.CATE_ONE);//1抛单
		orderShop.setDeliveryPrice(price); //设置物流价
		orderShop.setCastTime(new Date()); //抛单时间
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		
		List<OrderSku> list = SkuMapper.getByOrderShopId(orderShop.getId());
		for(OrderSku sku : list){
			sku.setIsSend(true);//已发货
			SkuMapper.updateByPrimaryKeySelective(sku);
		}
		
		List<OrderShop> orderShopList = orderShopMapper.getByOrderIdAndStatus(order.getId()); //查询同一订单下的商家订单是否全部发货
		if(orderShopList.size()>0){
			order.setDeliveryStatus(Contants.DELIVERY_PART); //2部分发货
		}else{
			order.setDeliveryStatus(Contants.DELIVERY_ALL); //1已发货
		}
		mapper.updateByPrimaryKeySelective(order);
		if(row ==0){
			orderShop = null;
		}else if (row == 1) {//商家抛单cheng
			MemberSend memberSend = new MemberSend();
			List<MemberSend> sends = memberSendMapper.selectMemberSendByParams(memberSend);
			if (sends.size() > 0) {
				List<MemberNotice> notice1 = new ArrayList<>();
				
				for (int i = 0; i < sends.size(); i++) {
					MemberNotice notice = new MemberNotice();
					java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					
					Date date = new Date();
				    String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
		
					String string ="您有新的信息,请注意查收!";
					notice.setmId(sends.get(i).getmId());
					notice.setMessage(string);
					notice.setMsgType(1);
					notice.setIsRead(0);
					notice.setAddTime(new Date());
					notice.setUpdateTime(new Date());
					notice.setLastExcuTime(sdf.parse(d));
					notice1.add(notice);
				}
			
				memberNoticeMapper.insertSelectiveByBatch(notice1);
				try {
					//插入订单日志
					 Map mapOne = JSON.parseObject(userJson, Map.class);
					String username = (String) mapOne.get("username");
					Integer userId =  (Integer) mapOne.get("userId");
					OrderLog orderLog = new OrderLog();
					orderLog.setAction("抛单");
					orderLog.setAddtime(new Date());
					orderLog.setAdminUser(username);
					orderLog.setUserType("商家用户");
					orderLog.setOrderStatus(orderShop.getStatus());
					orderLog.setUserTable("sys_user");
					orderLog.setUserId(userId);
					orderLog.setOrderId(orderShop.getOrderId());
					orderLog.setOrderNo(orderShop.getOrderNo());
					orderLogMapper.insertSelective(orderLog);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
				row =1;
			}
		}
		return orderShop;
	}
	
	/**
	 * 商家取消订单
	 */
	@Override
	public OrderShop cancelOrder(String id) throws Exception {
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		orderShop.setStatus(Contants.IS_CANCEL); //5取消
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		if(row == 0){
			orderShop=null;
		}
		return orderShop;
	}
	
	/**
	 * 批量抛单
	 */
	@Override
	public List<Order> batchCastSheet(String id) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		int row = mapper.batchCastSheet(list);
		SkuMapper.batchCastSheet(list);
		
		List<Order> orderList = new ArrayList<Order>();
		for(int i=0; i<list.size(); i++){
			Order order = mapper.selectByPrimaryKey(Integer.parseInt(list.get(i)));
			orderList.add(order);
		}
		if(row==0){
			orderList = null;
		}
		return orderList;
	}
	
	/**
	 * status： 1:退款失败 2:退款成功，3：已拒绝
	 * 审核退款
	 */
	@Transactional
	@Override
	public OrderShop auditRefund(String id, String status, int userId, String refuseReason,String username) throws Exception {  
	
		int row = 0;
		Boolean flag=false;//用于标识是否退款成功 xieyc
		OrderRefundDoc doc = refundMapper.selectByPrimaryKey(Integer.parseInt(id));
		OrderSku orderSku = SkuMapper.selectByPrimaryKey(doc.getOrderSkuId());
		Goods goods = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
		GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
		orderSku.setRefund(2); //退款完成
		SkuMapper.updateByPrimaryKeySelective(orderSku);
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(doc.getOrderShopId());
		String userName = shopMapper.selectUsernameBymId(userId);
		if(!StringUtils.isEmptyOrWhitespaceOnly(userName)){
			doc.setAdminUser(userName); //设置处理管理员
		}
		doc.setDisposeTime(new Date()); //设置审核时间
		if(Integer.parseInt(status)==1){
			doc.setStatus(Contants.REFUND_REFUSE); //3拒绝退款
			doc.setRefuseReason(refuseReason); //拒绝原因
		}else {
			if(doc.getReason().equals("换货")){
				doc.setStatus(Contants.REFUND_SUCCESS); //2-成功
				goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
				goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
			}else{
				//退款相关代码 xxj
				Order  order = mapper.selectByPrimaryKey(doc.getOrderId());
				//	Integer payType,String tradeNo, String outTradeNo, String refundAmount, String totalAmount
				int payType =order.getPaymentId();
				String tradeNo = order.getPaymentNo();
				String outTradeNo = order.getOrderNo();
				
				Integer refundAmount = doc.getAmount();//退款金额
				Integer totalAmount = order.getOrderPrice();
				String refundAmountStr="";
				String totalAmountStr="";
				if(order.getPaymentId()==2){
					refundAmountStr=(double)refundAmount/100+"";
					totalAmountStr=(double)totalAmount/100+"";
				}else{
					refundAmountStr=refundAmount+"";
					totalAmountStr=totalAmount+"";
				}
				
				//支付宝退款成功判断
				if(payType==2){
					StringBuffer sb = new StringBuffer();
					sb.append(PayInterfaceEnum.TOREFUND.getMethod());
					sb.append("?payType="+payType);
					sb.append("&tradeNo="+tradeNo);
					sb.append("&outTradeNo="+outTradeNo);
					sb.append("&refundAmount="+refundAmountStr);
					sb.append("&totalAmount="+totalAmountStr);
					String retJsonStr = HttpService.doGet(sb.toString());
					JSONObject josnObj  = JSONObject.fromObject(retJsonStr);
					JSONObject refundJsonObj = josnObj.getJSONObject("alipay_trade_refund_response");
					if("10000".equals(refundJsonObj.getString("code"))){
						doc.setStatus(Contants.REFUND_SUCCESS); //2-成功
						goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
						goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
						flag=true;//退款成功 xieyc
					}else{
						doc.setStatus(Contants.REFUND_FAIL);//1-失败
					}
				}
				//微信退款判断
				else if(payType==3){
					MemberShop memberShop =memberShopMapper.selectByPrimaryKey(order.getShopId());
					UnionPayRefundVO vo = new UnionPayRefundVO();
					vo.setRefundAmount(refundAmountStr);
					vo.setMerOrderId(outTradeNo);
					//vo.setMd5Key("fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR");
					vo.setMd5Key(memberShop.getMd5Key());
					logger.debug("auditRefund vo="+vo.toString());
					String ret = HttpService.doPostJson(UnionPayInterfaceEnum.WXJSREFUND.getMethod(),vo);
					logger.debug("auditRefund ret="+ret);
					if("SUCCESS".equals(ret.replaceAll("\"", ""))){
						doc.setStatus(Contants.REFUND_SUCCESS);//2-成功
						goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
						goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
						flag=true;//退款成功 xieyc
					}else{
						doc.setStatus(Contants.REFUND_FAIL);//1-失败
					}
				}
				
				/*********************退滨惠豆开始  xieyc（现在不退了）***********************************/
			/*	if(flag){//退款成功以后退滨惠豆
					Integer savePrice=orderShop.getSavePrice();//所花的滨惠豆   xieyc 
					if(savePrice>0){
						MemberUser membUser= membUserMapper.selectByPrimaryKey(orderShop.getmId());
						Integer oldSavePrice=membUser.getPoint();//该用户还剩多少滨惠豆
						membUser.setPoint(savePrice+oldSavePrice);
						membUserMapper.updateByPrimaryKeySelective(membUser);//更新
					}
				}*/
				/********************退滨惠豆结束  xieyc*********************************/
			}
			goodsMapper.updateByPrimaryKeySelective(goods);
			goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
		}
		OrderLog orderLog = new OrderLog();
		orderLog.setAction("退款审核");
		orderLog.setAddtime(new Date());
		orderLog.setAdminUser(username);
		orderLog.setUserType("商家用户");
		orderLog.setOrderStatus(orderShop.getStatus());
		orderLog.setUserTable("sys_user");
		orderLog.setUserId(userId);
		orderLog.setOrderId(orderSku.getOrderId());
		orderLog.setOrderNo(orderShop.getOrderNo());
		orderLogMapper.insertSelective(orderLog);
		
		row = refundMapper.updateByPrimaryKeySelective(doc);
		if(row<=0){
			orderShop = null;	
		}
		return orderShop;
	}
	
	/**
	 * 平台后台全部订单列表
	 */
	@Override
	public PageBean<Order> pageAll(String order_no, String status, String currentPage, String pageSize, String startTime,
			String endTime) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<Order> list = mapper.allList(Integer.parseInt(status), order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(Order order : list){
				double realPrice = (double)order.getOrderPrice()/100; //价格“分”转化成“元”
				order.setRealPrice(realPrice);
				
				double realDeliveryPrice = (double)order.getDeliveryPrice()/100; //邮费“分”转化成“元”
				order.setRealDeliveryPrice(realDeliveryPrice);
				
				StringBuffer buffer = new StringBuffer(); //拼接商品名称
				List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
				if(orderShopList.size()>0){
					for(OrderShop orderShop : orderShopList){
						List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
						if(skuList.size()>0){
							for(OrderSku sku : skuList){
								buffer.append(sku.getGoodsName()+",");
							}
						}
					}
				}
				if(buffer.toString().length()>0){
					String goodsName = buffer.toString().substring(0, buffer.toString().length()-1);
					order.setGoodsName(goodsName);
				}
				
				
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShopList.get(0).getId());
				
				order.setGoodsImage(skuList.get(0).getSkuImage());//商品主图
				
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
				Map<String, Object> map = new HashMap<>();
				if(goodsSku.getJdSkuNo()!=null){
					map.put("jdSkuNo", goodsSku.getJdSkuNo()); //京东商品sku
				}else{
					map.put("jdSkuNo", null); //京东商品sku
				}
				
				if(goodsSku.getJdPrice()==0){
					map.put("jdPirce", null);
				}else{
					map.put("jdPirce", (double)goodsSku.getJdPrice()/100); //京东价
				}
				if(goodsSku.getJdProtocolPrice()==0){
					map.put("jdProtocolPrice", null); 
				}else{
					map.put("jdProtocolPrice", (double)goodsSku.getJdProtocolPrice()/100); //协议价
				}

				map.put("num", skuList.get(0).getSkuNum()); //数量
				map.put("price", (double)skuList.get(0).getSkuSellPriceReal()/100); //单价
				
				JSONObject jsonObject = JSONObject.fromObject(skuList.get(0).getSkuValue());
				map.put("value", jsonObject);
				
				order.setGoodsSkuInfo(map);
				
				Member member = memberMapper.selectByPrimaryKey(orderShopList.get(0).getmId()); //用户注册信息
				Map<String, Object> mbMap  = new LinkedHashMap<>();
				if(member!=null){
					mbMap.put("id", member.getId());
					mbMap.put("userNick", member.getUsername());
					mbMap.put("phone", member.getPhone());
				}else{
					mbMap.put("id", null);
					mbMap.put("userNick", null);
					mbMap.put("phone", null);
				}
				
				MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShopList.get(0).getmId());
				if(memberUser!=null){
					mbMap.put("status", getStatus(memberUser.getStatus())); //状态
				}else{
					mbMap.put("status", null);
				}
				order.setUserInfo(mbMap);
				
				
				MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
				if(address!=null){
					order.setFullName(address.getFullName()); //买家姓名
					order.setTel(address.getTel());
					String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+address.getAddress();
					order.setAddress(userAddress);
				}
			}
		}
		
		PageBean<Order> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * 后台结算模块订单统计
	 */
	@Override
	public Map<Integer, Object> OrderAccount(int shopId) throws Exception {
		StringBuffer buffer = new StringBuffer();
		
		int count = mapper.OrderAccount(shopId, Contants.IS_WAIT_PAY); //1待付
		buffer.append(count+",");
		buffer.append(Contants.IS_WAIT_PAY); //1待付
		
		String[] str = buffer.toString().split(",");
		
		HashMap<Integer, Object> map = new HashMap<>();
		map.put(1, str);
		return map;
	}
	
	/**
	 * 后台订单&商品模块订单统计
	 */
	@Override
	public Map<Integer, Object> OrderWaitAccount(int shopId) throws Exception {
		List<Object> list = new ArrayList<>();
		int countOne = mapper.OrderAccount(shopId, Contants.IS_WAIT_SEND); //2待发货
		//int countTwo = mapper.notComplish(admin.getShopId()); //3,4,5 未完成
		int countTwo = mapper.OrderAccount(shopId, Contants.IS_SEND); //3已发货
		int countThree= mapper.OrderAccount(shopId, Contants.IS_CANCEL); //6已取消
		int countFour = mapper.OrderAccount(shopId, Contants.IS_COMPLISH); //7已完成
		
		StringBuffer bufferOne = new StringBuffer();
		bufferOne.append(countOne+",");
		bufferOne.append(Contants.IS_WAIT_SEND);
		String[] strOne = bufferOne.toString().split(",");
		
		StringBuffer bufferTwo = new StringBuffer();
		bufferTwo.append(countTwo+",");
		bufferTwo.append(Contants.IS_SEND);
		String[] strTwo = bufferTwo.toString().split(",");
		
		StringBuffer bufferThree = new StringBuffer();
		bufferThree.append(countThree+",");
		bufferThree.append(Contants.IS_CANCEL);
		String[] strThree = bufferThree.toString().split(",");
		
		StringBuffer bufferFour = new StringBuffer();
		bufferFour.append(countFour+",");
		bufferFour.append(Contants.IS_COMPLISH);
		String[] strFour = bufferFour.toString().split(",");
		
		HashMap<Integer, Object> map = new HashMap<>();
		map.put(1, strOne);//待发货
		map.put(2, strTwo);//已发货
		map.put(3, strThree);//已取消
		map.put(4, strFour);//已完成
		//list = Arrays.asList(buffer.toString().split(","));
		return map;
	}
	
	public String[] refund(int shopId, int status,Date startTimes,Date  endTimes){
		int count = refundMapper.OrderRefundAccount(startTimes,endTimes,shopId, status); //全部退款单
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	/**
	 * 后台退款模块订单统计
	 */
	@Override
	public Map<String, Object> OrderRefundAccount(int shopId,String startTime, String endTime) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
		String[] str = refund(shopId, -1,sf.parse(startTimes),sf.parse(endTimes));//全部订单
		String[] strOne = refund(shopId, 0,sf.parse(startTimes),sf.parse(endTimes)); //退款中
		String[] strTwo = refund(shopId, 1,sf.parse(startTimes),sf.parse(endTimes));//退款失败
		String[] strThree = refund(shopId, 2,sf.parse(startTimes),sf.parse(endTimes)); //退款成功
		String[] strFour = refund(shopId, 3,sf.parse(startTimes),sf.parse(endTimes)); //拒绝退款
		HashMap<String, Object> map = new LinkedHashMap<>();
		map.put("全部订单", str);
		map.put("审核中", strOne);
		map.put("审核失败", strTwo);
		map.put("审核成功", strThree);
		map.put("审核拒绝", strFour);
		return map;
	}
	
	/**
	 * 后台交易金额统计
	 */
	@Override
	public Map<String, String> OrderMoneyAccount(int shopId) throws Exception {
		double total = 0.00;
		double alreadyTotal = 0.00;
		double waitTotal = 0.00;
		double refundTotal = 0.00;
		HashMap<String, String> map = new HashMap<>();
		
		List<OrderShop> alreadyList = orderShopMapper.getAlreadyByShopId(shopId); //已付
		if(alreadyList.size()>0){
			for(OrderShop orderShop : alreadyList){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(int i=0; i<skuList.size(); i++){
					double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					alreadyTotal += realSellPrice * skuList.get(i).getSkuNum();
				}
			}
		}
		
		List<OrderShop> waitList = orderShopMapper.getByShopId(shopId, Contants.IS_WAIT_PAY); //待付
		if(waitList.size()>0){
			for(OrderShop orderShop : waitList){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(int i=0; i<skuList.size(); i++){
					double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					waitTotal += realSellPrice * skuList.get(i).getSkuNum();
				}
			}
		}
		
		List<OrderRefundDoc> refundList = refundMapper.getByShopId(shopId, Contants.REFUN_STATUS); //退款
		if(refundList.size()>0){
			for(OrderRefundDoc doc : refundList){
				double amountPrice = (double)doc.getAmount()/100;//退款价格“分”转化成“元”
				refundTotal += amountPrice;
			}
		}
		
		total = alreadyTotal + refundTotal; //总交易额
				
		map.put("交易总额", Double.toString(total));
		map.put("已付金额", Double.toString(alreadyTotal));
		map.put("待付金额", Double.toString(waitTotal));
		map.put("退款金额", Double.toString(refundTotal));
		return map;
	}
	
	public String[] backMap(int shopId, Date startTimes,Date endTimes,int status){
		int count = mapper.OrderAccountByTime(shopId, startTimes,endTimes,status);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	public String[] waitBackMap(int shopId,Date startTimes,Date endTimes){
		int count = mapper.waitOrderAccount(startTimes,endTimes,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(2);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	
	public String[] jdBackMap(int shopId, int status){
		int count = mapper.jdOrderAccount(shopId, status);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	/**
	 * 后台订单数量统计管理
	 */
	@Override
	public Map<String, Object> OrderNumAccount(int shopId,String startTime, String endTime) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);//指定日期的后一天
				
		HashMap<String, Object> map = new LinkedHashMap<>();
		
		int count = mapper.OrderCountAll(shopId);
		
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(Contants.IS_ALL);
		String[] str = buffer.toString().split(","); //全部
		
		String[] strZero = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes), Contants.IS_WAIT_PAY);//1待付
		//String[] strOne = backMap(shopId, Contants.IS_WAIT_SEND);
		String[] strOne =waitBackMap(shopId,sf.parse(startTimes),sf.parse(endTimes));//2待发货
		String[] strTwo = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes), Contants.IS_SEND);//3已发货
		String[] strThree = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes), Contants.IS_WAIT_EVALUATE);//5已收货
		String[] strFour = backMap(shopId, sf.parse(startTimes),sf.parse(endTimes),Contants.IS_CANCEL); //6已取消
		String[] strFive = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes),Contants.IS_COMPLISH); //7已评价
		String[] strSeven = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes), Contants.IS_DELETE); //8已删除
		String[] strNine = backMap(shopId,sf.parse(startTimes),sf.parse(endTimes), 9); //9备货中
		//程凤云2018-3-23添加待分享的订单的数量
		String[] ten = sharePackMapByTime(shopId,10,sf.parse(startTimes),sf.parse(endTimes)); //待分享
		
		map.put("所有订单", str);	
		map.put("待付款", strZero);
		map.put("待分享", ten);
		map.put("待发货", strOne);
		map.put("备货中", strNine);

		map.put("已发货", strTwo);
		map.put("已收货", strThree);
		map.put("已取消", strFour);
		map.put("已评价", strFive);
		map.put("已删除", strSeven);
		
		return map;
	}
	
	/**
	 * 后台订单数量统计管理(小程序-下四)
	 */
	@Override
	public Map<String, Object> mOrderNumAccount(int shopId) throws Exception {
		String[] strZero = mBackMap(shopId, Contants.IS_WAIT_PAY);//1待付
		String[] strOne = mBackMap(shopId, Contants.IS_WAIT_SEND);//2待发货
		String[] strTwo = mBackMap(shopId, Contants.IS_SEND);//3已发货
		String[] strThree = mBackMap(shopId, -1);//-1退款/售后
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		map.put("IS_WAIT_PAY", strZero);
		map.put("IS_WAIT_SEND", strOne);
		map.put("IS_SEND", strTwo);
		map.put("IS_REFUND", strThree);	
		return map;
	}
	
	public String[] mBackMap(int shopId, int status){
		OrderShop entity = new OrderShop();
		entity.setShopId(shopId);
		entity.setStatus(status);
		
		int count = orderShopMapper.mOrderAccount(entity);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	/**
	 * 后台订单数量统计管理(小程序-上三)
	 */
	@Override
	public Map<String, Object> mNumAccount(int shopId) throws Exception {
		int zero = countByShopIdAndStatus(shopId, Contants.IS_WAIT_PAY);
		int one = countByShopIdAndStatus(shopId, Contants.IS_WAIT_SEND);
		int two = countByShopIdAndStatus(shopId, Contants.IS_SEND);
		int three = countByShopIdAndStatus(shopId, -1);
		
		int all = zero+one+two+three;
		StringBuffer buffer= new StringBuffer();
		buffer.append(all+",");
		buffer.append(Contants.IS_WAIT_PAY);
		
		String[] strOne = buffer.toString().split(",");
		String[] strTwo = mBack(shopId, 4);//4已完成
		String[] strThree = mBack(shopId, 5);//5已关闭
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		map.put("IS_GOING", strOne);
		map.put("IS_WAIT_SEND", strTwo);
		map.put("IS_SEND", strThree);
		return map;
	}
	
	public int countByShopIdAndStatus(int shopId, int status){
		OrderShop entity = new OrderShop();
		entity.setShopId(shopId);
		entity.setStatus(status);
		int count = orderShopMapper.mOrderAccount(entity);
		return count;
	}
	
	
	public String[] mBack(int shopId, int status){
		OrderShop entity = new OrderShop();
		entity.setShopId(shopId);
		entity.setStatus(status);
		
		int count = orderShopMapper.mAccount(entity);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	/**
	 * 后台商家订单管理(小程序)
	 */
	@Override
	public PageBean<OrderShop> mOrderListPage(OrderShop entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<OrderShop> list = orderShopMapper.mOrderListPage(entity);
		if(list.size()>0){
			for(OrderShop orderShop : list){
				Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
				if(order!=null){
					orderShop.setAddtime(order.getAddtime());//下单时间
					
					MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
					if(address!=null){
						orderShop.setFullName(address.getFullName()); //买家姓名
					}
				}
				/*List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				if(skuList.size()>0){
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getSkuImage())){
						orderShop.setGoodsImage(skuList.get(0).getSkuImage()); //商品图片
					}
				}*/
				orderShop.setGoodsImage(orderShop.getGoodsImage());
				setOrderStatus(orderShop); //设置订单状态
			}
		}
		PageBean<OrderShop> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	private OrderShop setOrderStatus(OrderShop entity){
		if(entity.getIsRefund()==0){
			if(entity.getStatus()==1){
				entity.setMystatus("待付款");
			}
			if(entity.getStatus()==2 || entity.getStatus()==9){
				entity.setMystatus("待发货");
			}
			if(entity.getStatus()==3){
				entity.setMystatus("已发货");
			}
			if(entity.getStatus()==5){
				entity.setMystatus("待评价");
			}
			if(entity.getStatus()==6){
				entity.setMystatus("已取消");
			}
			if(entity.getStatus()==7){
				entity.setMystatus("已评价");
			}
			if(entity.getStatus()==8){
				entity.setMystatus("已删除");
			}
		}else{
			OrderRefundDoc doc = refundMapper.getByOrderShopId(entity.getId());
			if(doc!=null){
				if(doc.getStatus()==0){
					if(doc.getReason().equals("退款") || doc.getReason().equals(RefundReasonEnum.REFUND_TEAM.getReason())){
						entity.setMystatus("退款中");
					}
					if(doc.getReason().equals("退款退货")){
						entity.setMystatus("退款退货中");
					}
					if(doc.getReason().equals("换货")){
						entity.setMystatus("换货中");
					}
				}
				if(doc.getStatus()==1){
					if(doc.getReason().equals("退款") || doc.getReason().equals(RefundReasonEnum.REFUND_TEAM.getReason())){
						entity.setMystatus("退款失败");
					}
					if(doc.getReason().equals("退款退货")){
						entity.setMystatus("退款退货失败");
					}
					if(doc.getReason().equals("换货")){
						entity.setMystatus("换货失败");
					}
				}
				if(doc.getStatus()==2){
					if(doc.getReason().equals("退款") || doc.getReason().equals(RefundReasonEnum.REFUND_TEAM.getReason())){
						entity.setMystatus("退款成功");
					}
					if(doc.getReason().equals("退款退货")){
						entity.setMystatus("退款退货成功");
					}
					if(doc.getReason().equals("换货")){
						entity.setMystatus("换货成功");
					}
				}
				if(doc.getStatus()==3){
					if(doc.getReason().equals("退款") || doc.getReason().equals(RefundReasonEnum.REFUND_TEAM.getReason())){
						entity.setMystatus("拒绝退款");
					}
					if(doc.getReason().equals("退款退货")){
						entity.setMystatus("拒绝退款退货");
					}
					if(doc.getReason().equals("换货")){
						entity.setMystatus("拒绝换货");
					}
				}
			}
		}	
		return entity;
	}
	
	public OrderShop mOrderDetails(OrderShop entity) throws Exception{
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(entity.getId());
        //设置快递配送信息参数
	    Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
	    String host = "https://wuliu.market.alicloudapi.com";
	    String path = "/kdi";
	    String method = "GET";
	    String appcode = "232d013ef8244587a9a4f69cb2fcca47";
	    Map<String, String> headers = new HashMap<String, String>();
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("no", order.getExpressNo());
	    
	   try{
    		//获取物流配送信息
	        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	        String Logistics = EntityUtils.toString(response.getEntity());
	    	JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
	    	orderShop.setLogistics(jsonObject2);//物流配送信息
	    	
		    orderShop.setExpressName(order.getExpressName()); //物流公司名字
		    orderShop.setExpressNo(order.getExpressNo());   //物流单号
		    orderShop.setAddtime(order.getAddtime()); //下单时间
		    orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		    if(order.getPaymentId()!=null){
			     orderShop.setPayWay(order.getPaymentId()); //支付方式:1货到付款 2 支付宝支付  3微信支付'
		    }
		    MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		    String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+address.getAddress();
		    String tel = address.getTel();
		    if(address.getTelephone()!=null){
		      	tel = address.getTel() +" "+ address.getTelephone();
		    }
		    orderShop.setFullName(address.getFullName());
		    orderShop.setTel(tel);
		    orderShop.setAddress(addr);
		
		
		    double allPrice = (double)orderShop.getOrderPrice()/100;//订单总价“分”转化成“元”
		    orderShop.setAllPrice(allPrice);
		
		    double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费“分”转化成“元”
		    orderShop.setRealDeliveryPrice(realDeliveryPrice);
		
		    double realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;//邮费“分”转化成“元”
		    orderShop.setRealgDeliveryPrice(realgDeliveryPrice);
		    
		    double realSavePrice = (double)orderShop.getSavePrice()/100;//滨惠豆的折扣“分”转化成“元”
		    orderShop.setRealSavePrice(realSavePrice);
		    
		    setOrderStatus(orderShop); //设置订单状态
		    
		    if(orderShop.getCouponId()>0){//xieyc
			    double realCouponsPrice = (double)orderShop.getCouponPrice()/100;//优惠卷的折扣“分”转化成“元”
			    orderShop.setRealCouponsPrice(realCouponsPrice);
			}else{
				 orderShop.setRealCouponsPrice(0);
			}
	
		    List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
		    for(OrderSku orderSku : skuList){
			    double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100;//市场价格“分”转化成“元”
			    double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
			    orderSku.setRealMarketPrice(realMarketPrice);
			    orderSku.setRealSellPrice(realSellPrice);
			
			    JSONObject jsonObject = JSONObject.fromObject(orderSku.getSkuValue());
			    orderSku.setValueObj(jsonObject);
			
			    MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
			    orderSku.setShopName(shop.getShopName());
		     }
		     orderShop.setOrderSku(skuList);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   	return orderShop;
	}
	
	/**
	 * 订单发货走速达流程(移动端后台)
	 */
	public OrderShop mSportTech(String id, String deliveryPrice) throws Exception{
		Integer price = (int)(Double.parseDouble(deliveryPrice)*100);
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());

		orderShop.setdState(Contants.CATE_ONE);//1抛单
		orderShop.setDeliveryPrice(price); //设置物流价
		orderShop.setCastTime(new Date()); //抛单时间
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		
		List<OrderSku> list = SkuMapper.getByOrderShopId(orderShop.getId());
		for(OrderSku sku : list){
			sku.setIsSend(true);//已发货
			SkuMapper.updateByPrimaryKeySelective(sku);
		}
		
		List<OrderShop> orderShopList = orderShopMapper.getByOrderIdAndStatus(order.getId()); //查询同一订单下的商家订单是否全部发货
		if(orderShopList.size()>0){
			order.setDeliveryStatus(Contants.DELIVERY_PART); //2部分发货
		}else{
			order.setDeliveryStatus(Contants.DELIVERY_ALL); //1已发货
		}
		mapper.updateByPrimaryKeySelective(order);
		if(row ==0){
			orderShop = null;
		}else if (row == 1) {//商家抛单cheng
			MemberSend memberSend = new MemberSend();
			List<MemberSend> sends = memberSendMapper.selectMemberSendByParams(memberSend);
			if (sends.size() > 0) {
				List<MemberNotice> notice1 = new ArrayList<>();
				
				for (int i = 0; i < sends.size(); i++) {
					MemberNotice notice = new MemberNotice();
					java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					
					Date date = new Date();
				    String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
		
					String string ="您有新的信息,请注意查收!";
					notice.setmId(sends.get(i).getmId());
					notice.setMessage(string);
					notice.setMsgType(1);
					notice.setIsRead(0);
					notice.setAddTime(new Date());
					notice.setUpdateTime(new Date());
					notice.setLastExcuTime(sdf.parse(d));
					notice1.add(notice);
				}
			
				memberNoticeMapper.insertSelectiveByBatch(notice1);
				row =1;
			}
		}
		return orderShop;
	}
	
	/**
	 * 订单发货商家自配(移动端后台)
	 */
	public int mDistributeByself(String id, long shopId) throws Exception{
		int row = 0;
		int sId = 0;
		int mId = 0;
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(orderShop!=null){
			orderShop.setSendTime(new Date());
			orderShop.setStatus(Contants.IS_SEND); //订单状态-已发货
			orderShop.setdState(Contants.CATE_TWO); //配送状态-代发货
			orderShop.setDeliveryWay(1);
			row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
			
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey((int)shopId);
			if(memberShop!=null){
				Member m = new Member();
				m.setUsername(memberShop.getLinkmanPhone());
				List<Member> list = memberMapper.login(m);
				if(list.size()>0){
					MemberSend send = memberSendMapper.selectByPrimaryKey(list.get(0).getId());
					if(send==null){//创建member_send
						row = insertMemberSend(list.get(0).getId(), memberShop.getShopName(), memberShop.getAddress());
						if(row>0){
							sId = row;
							MemberSend mSend = memberSendMapper.selectByPrimaryKey(sId);
							row = insertOrderSendInfo(orderShop, mSend);//创建配送订单
						}
					}else{
						row = insertOrderSendInfo(orderShop, send);//创建配送订单
					}
				}else{//创建member/member_ser/member_send
					row = insertMember(memberShop);
					if(row>0){
						mId = row;
					}
					row = insertMemberSend(mId, memberShop.getShopName(), memberShop.getAddress());
					if(row>0){
						sId = row;
						MemberSend mSend = memberSendMapper.selectByPrimaryKey(sId);
						row = insertOrderSendInfo(orderShop, mSend);//创建配送订单
					}
				}
			}
		}
		return row;
	}
	
	
	/**
	 * 后台订单数量统计管理
	 */
	@Override
	public Map<String, Object> jdOrderCount(int shopId) throws Exception {
		HashMap<String, Object> map = new LinkedHashMap<>();
		
		int count = orderShopMapper.jdOrderCount(shopId);
		
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(Contants.IS_ALL);
		String[] str = buffer.toString().split(","); //全部
		
		String[] strZero = jdBackMap(shopId, Contants.IS_WAIT_PAY);//1待付
		String[] strOne = jdBackMap(shopId, Contants.IS_WAIT_SEND);//2待发货
		String[] strTwo = jdBackMap(shopId, Contants.IS_SEND);//3已发货
		String[] strThree = jdBackMap(shopId, Contants.IS_WAIT_EVALUATE);//5已收货
		String[] strFour = jdBackMap(shopId, Contants.IS_CANCEL); //6已取消
		String[] strFive = jdBackMap(shopId, Contants.IS_COMPLISH); //7已评价
		String[] strSeven = jdBackMap(shopId, Contants.IS_DELETE); //8已删除
		String[] strNine = jdBackMap(shopId, 9); //9备货中
		
		map.put("所有订单", str);	
		map.put("待付款", strZero);
		map.put("待发货", strOne);
		map.put("备货中", strNine);
		map.put("已发货", strTwo);
		map.put("已收货", strThree);
		map.put("已取消", strFour);
		map.put("已评价", strFive);
		map.put("已删除", strSeven);
		
		return map;
	}
	
	
	public String[] backMapAll(Integer status){
		int count = mapper.backgroundOrderAccount(status);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	/**
	 * 平台后台订单数量统计
	 */
	@Override
	public Map<String, Object> backgroundOrderAccount() throws Exception {
		String[] str = backMapAll(Contants.IS_ALL); //全部订单
		String[] strOne = backMapAll(Contants.IS_WAIT_PAY); //待付
		String[] strTwo = backMapAll(Contants.IS_WAIT_SEND); //已付
		String[] strThree = backMapAll(Contants.IS_SEND); //完成
		
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		map.put("所有订单", str);
		map.put("待付款", strOne);
		map.put("已付款", strTwo);
		map.put("已完成", strThree);
		return map;
	}

	/**
	 * 后台首页订单交易总额和订单数量统计
	 */
	@Override
	public Map<String, Object> totalNumAccount(int shopId,String startTime, String endTime) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		
		double total = 0.00;
		int count = 0;
		List<OrderShop> alreadyList = orderShopMapper.getAlreadyByShopIdByTime(sf.parse(startTimes),sf.parse(endTimes),shopId); //已付
		if(alreadyList.size()>0){
			for(OrderShop orderShop : alreadyList){
				double realOrderPrice = (double)orderShop.getOrderPrice()/100;//订单总价“分”转化成“元”
				total += realOrderPrice;
			}
		}
		count = mapper.OrderCountAllByTime(sf.parse(startTimes),sf.parse(endTimes),shopId);
		
		total = Math.floor(total*10)/10;
		StringBuffer bufferOne= new StringBuffer();
		bufferOne.append(total+",");
		bufferOne.append("元");
		String[] strOne = bufferOne.toString().split(",");
		
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append("条");
		String[] str = buffer.toString().split(",");
		
		map.put("交易总金额", strOne);
		map.put("订单总条数", str);

		return map;
	}

	/**
	 * 商家后台退款订单列表
	 */
	@Override
	public PageBean<OrderRefundDoc> orderRefundList(int shopId, String order_no, String status, String currentPage,
			String pageSize, String startTime, String endTime) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderRefundDoc> list = refundMapper.orderRefundList(shopId, status, order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(OrderRefundDoc doc : list){
				OrderShop orderShop = orderShopMapper.selectByPrimaryKey(doc.getOrderShopId()); //设置订单编号
				doc.setOrderShopNo(orderShop.getShopOrderNo());
				
				Order order = mapper.selectByPrimaryKey(doc.getOrderId()); //设置下单时间
				doc.setOrdersTime(order.getAddtime());
				
				double realPrice = (double)doc.getAmount()/100;//退款总价“分”转化成“元”
				doc.setRealPrice(realPrice);
			}
		}
		
		PageBean<OrderRefundDoc> page = new PageBean<>(list);
		return page;
	}

	
	/**
	 * 商家后台退款订单详情
	 */
	@Override
	public OrderShop getOrderRefundDetails(String id) throws Exception {
		OrderRefundDoc doc = refundMapper.selectByPrimaryKey(Integer.parseInt(id));
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(doc.getOrderShopId());
		orderShop.setRealSavePrice((double)orderShop.getSavePrice()/100);//滨惠豆抵扣
		if(doc.getStatus()==0){ //设置退款状态
			orderShop.setRefundStatus(0);
		}
		if(doc.getStatus()==1){
			orderShop.setRefundStatus(1);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==2){
			orderShop.setRefundStatus(2);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==3){
			orderShop.setRefundStatus(3);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		
		if(doc.getImg()!=null){ //退款说明图转数组
			List<String> bs = Arrays.asList(doc.getImg().split(","));
			final int size = bs.size();
			String[] arr = (String[])bs.toArray(new String[size]);
			orderShop.setImageStr(arr);
		}
		orderShop.setReason(doc.getReason()); //退款原因
		orderShop.setNote(doc.getNote()); //退款说明
		orderShop.setApplyTime(doc.getAddtime()); //申请时间
	
	    double realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;//邮费“分”转化成“元”
	    orderShop.setRealgDeliveryPrice(realgDeliveryPrice);//邮费
		
		Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
		
	/*	if (order.getCouponsId() > 0) {// xieyc
			CouponLog couponLog = couponLogMapper.selectByPrimaryKey(order.getCouponsId());
			Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());
			double realCouponsPrice = (double) coupon.getAmount() / 100;// 优惠卷的折扣“分”转化成“元”
			orderShop.setRealCouponsPrice(realCouponsPrice);
		} else {
			orderShop.setRealCouponsPrice(0);
		}
		*/
		
		orderShop.setAddtime(order.getAddtime()); //下单时间
		orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		if(order.getPaymentId()!=null){
			orderShop.setPayWay(order.getPaymentId()); //支付方式1货到付款 2 支付宝支付  3微信支付'
		}
		
		MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+address.getAddress();
		
		orderShop.setFullName(doc.getmName());
		orderShop.setTel(doc.getmPhone());
		orderShop.setAddress(addr);
		
		
		double allPrice = (double)doc.getAmount()/100;//退款总价“分”转化成“元”
		orderShop.setAllPrice(allPrice);
		
		List<OrderSku> skuList = new ArrayList<>();
		
		OrderSku orderSku = SkuMapper.selectByPrimaryKey(doc.getOrderSkuId());
		double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100;//市场价格“分”转化成“元”
		double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
		orderSku.setRealMarketPrice(realMarketPrice);
		orderSku.setRealSellPrice(realSellPrice);
		MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
		orderSku.setShopName(shop.getShopName());
		
		JSONObject jsonObject = JSONObject.fromObject(orderSku.getSkuValue()); //skuValue转义
		orderSku.setValueObj(jsonObject);
		
		skuList.add(orderSku);
		orderShop.setOrderSku(skuList);
		
		return orderShop;
	}
	public static void main(String args[]){
		// orderNo 20171116145825983772  42000000292017111650297
		// 20171117102706399368 2017111721001004270200357993
		int payType =3 ;
		String tradeNo = "4200000018201711175196498722";
		String outTradeNo = "20171117112042597243";
		String refundAmount = "0.1";
		// 301400
		String totalAmount = "0.1"+"";
		StringBuffer sb = new StringBuffer();
		sb.append(PayInterfaceEnum.TOREFUND.getMethod());
		sb.append("?payType="+payType);
		sb.append("&tradeNo="+tradeNo);
		sb.append("&outTradeNo="+outTradeNo);
		sb.append("&refundAmount="+refundAmount);
		sb.append("&totalAmount="+totalAmount);
		try{
			String retJsonStr = HttpService.doGet(sb.toString());
			JSONObject josnObj  = JSONObject.fromObject(retJsonStr);
			//支付宝退款成功判断
			if(payType==2){
				JSONObject refundJsonObj = josnObj.getJSONObject("alipay_trade_refund_response");
				if("10000".equals(refundJsonObj.getString("code"))){
				
				}else{
				
				}
			}
			//微信退款判断
			else if(payType==3){
				if("SUCCESS".equals(josnObj.getString("result_code"))){
					
				}else{
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	
	
	public List<MemberNotice> selectMemberNoticeList(MemberNotice notice){
		List<MemberNotice> list = new ArrayList<>();
		list = memberNoticeMapper.selectMemberNoticeList(notice);
		return list;
	}

	
	/**
	 * 商家后台配送订单列表
	 */
	@Override
	public PageBean<OrderSendInfo> orderSendPage(int shopId, String shopOrderNo, String dStatus, String currentPage,
			String pageSize) throws Exception {
		StringBuffer buffer = new StringBuffer();
		List<String> strList  = null;
		List<OrderShop> orderShopList = orderShopMapper.getByShopId(shopId, null);
		if(orderShopList.size()>0){
			for(OrderShop orderShop : orderShopList){
				buffer.append(orderShop.getId()+",");
			}
			if(buffer.toString().length()>0){
				String str= buffer.toString().substring(0,buffer.toString().length()-1);
				strList = Arrays.asList(str.split(","));
			}
		}
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		PageBean<OrderSendInfo> pageBean = null;
		if(strList!=null){
			List<OrderSendInfo> list = sendInfoMapper.selectAllByOrderShopId(strList, shopOrderNo, dStatus);
			if(list.size()>0){
				for(OrderSendInfo sendInfo : list){
					OrderShop shop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
					sendInfo.setShopOrderNo(shop.getShopOrderNo());
					
					if(shop.getDeliveryPrice()!=null){
						double realDeliveryPrice = (double)shop.getDeliveryPrice()/100;
						sendInfo.setRealDeliveryPrice(realDeliveryPrice); //配送费
					}
					
					if(sendInfo.getdState()==0){//配送状态  0(初始状态)待取货   1配送中   2已配送 3已取消'4已结算
						sendInfo.setDeliveryStatus(Contants.WAIT_SEND);
					}
					if(sendInfo.getdState()==1){
						sendInfo.setDeliveryStatus(Contants.SEND);
					}
					if(sendInfo.getdState()==2){
						sendInfo.setDeliveryStatus(Contants.WAIT_SETTLE);
					}
					if(sendInfo.getdState()==3){
						sendInfo.setDeliveryStatus(Contants.CANCEL);
					}
					if(sendInfo.getdState()==4){
						sendInfo.setDeliveryStatus(Contants.ALREADY_SETTLE);
					}
					
					
					if(sendInfo.getTool()==1){ //交通工具
						sendInfo.setSendTool(Contants.BIKE);
					}
					if(sendInfo.getTool()==2){
						sendInfo.setSendTool(Contants.TRICYCLE);
					}
					if(sendInfo.getTool()==3){
						sendInfo.setSendTool(Contants.ELECTROMBILE);
					}
					if(sendInfo.getTool()==4){
						sendInfo.setSendTool(Contants.BIG_ELECTROMBILE);
					}
					if(sendInfo.getTool()==5){
						sendInfo.setSendTool(Contants.MOTORBIKE);
					}
					if(sendInfo.getTool()==6){
						sendInfo.setSendTool(Contants.SEDAN_CAR);
					}
					if(sendInfo.getTool()==7){
						sendInfo.setSendTool(Contants.TRUCKS);
					}
				}
			}
			pageBean = new PageBean<>(list);
		}
		return pageBean;
	}

	/**
	 * 商家后台配送单详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public OrderSendInfo getOrderSendDetails(String id) throws Exception {
		OrderSendInfo sendInfo = sendInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(sendInfo!=null){
			OrderShop shop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
			sendInfo.setShopOrderNo(shop.getShopOrderNo());
			
			if(shop.getDeliveryPrice()!=null){
				double realDeliveryPrice = (double)shop.getDeliveryPrice()/100;
				sendInfo.setRealDeliveryPrice(realDeliveryPrice); //配送费
			}
			
			if(sendInfo.getdState()==0){//配送状态  0(初始状态)待取货   1配送中   2已配送 3已取消'4已结算
				sendInfo.setDeliveryStatus(Contants.WAIT_SEND);
			}
			if(sendInfo.getdState()==1){
				sendInfo.setDeliveryStatus(Contants.SEND);
			}
			if(sendInfo.getdState()==2){
				sendInfo.setDeliveryStatus(Contants.WAIT_SETTLE);
			}
			if(sendInfo.getdState()==3){
				sendInfo.setDeliveryStatus(Contants.CANCEL);
			}
			if(sendInfo.getdState()==4){
				sendInfo.setDeliveryStatus(Contants.ALREADY_SETTLE);
			}
			
			
			if(sendInfo.getTool()==1){ //交通工具
				sendInfo.setSendTool(Contants.BIKE);
			}
			if(sendInfo.getTool()==2){
				sendInfo.setSendTool(Contants.TRICYCLE);
			}
			if(sendInfo.getTool()==3){
				sendInfo.setSendTool(Contants.ELECTROMBILE);
			}
			if(sendInfo.getTool()==4){
				sendInfo.setSendTool(Contants.BIG_ELECTROMBILE);
			}
			if(sendInfo.getTool()==5){
				sendInfo.setSendTool(Contants.MOTORBIKE);
			}
			if(sendInfo.getTool()==6){
				sendInfo.setSendTool(Contants.SEDAN_CAR);
			}
			if(sendInfo.getTool()==7){
				sendInfo.setSendTool(Contants.TRUCKS);
			}
		}
		return sendInfo;
	}

	/**
	 * 商家后台配送单审核
	 */
	@Override
	public int auditOrderSend(String id) throws Exception {
		int row = 0;
		OrderSendInfo sendInfo = sendInfoMapper.selectByPrimaryKey(Integer.parseInt(id));
		OrderShop shop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
		if(sendInfo.getdState()==2){ //待结算
			MemberSend send = memberSendMapper.selectByPrimaryKey(sendInfo.getsId());
			if(send!=null){
				send.setTotalNum(send.getTotalNum()+1); //接单量
				if(shop!=null){
					send.setTotalIncome(send.getTotalIncome()+shop.getDeliveryPrice()); //总收入
					send.setBalance(send.getBalance()+shop.getDeliveryPrice()); // 账户余额
				}
				row = memberSendMapper.updateByPrimaryKeySelective(send);
			}
			sendInfo.setdState(Contants.DELIVER_SETTLE); //4已完成
			row = sendInfoMapper.updateByPrimaryKeySelective(sendInfo);
			
		}
		return row;
	}

	/**
	 * 订单添加备注
	 */
	@Override
	public int insertRemark(String id, String remark) throws Exception {
		int row = 0;
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(orderShop!=null){
			orderShop.setRemark(remark);
			row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		}
		return row;
	}

	/**
	 * 商家后台订单自配
	 */
	@Transactional
	@Override
	public int deliveryOwner(String id, int shopId) throws Exception {
		int row = 0;
		int sId = 0;
		int mId = 0;
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(orderShop!=null){
			orderShop.setSendTime(new Date());
			orderShop.setStatus(Contants.IS_SEND); //订单状态-已发货
			orderShop.setdState(Contants.CATE_TWO); //配送状态-代发货
			orderShop.setDeliveryWay(1); 
			
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(shopId);
			if(memberShop!=null){
				Member m = new Member();
				m.setUsername(memberShop.getLinkmanPhone());
				List<Member> list = memberMapper.login(m);
				if(list.size()>0){
					MemberSend send = memberSendMapper.selectByPrimaryKey(list.get(0).getId());
					if(send==null){//创建member_send
						row = insertMemberSend(list.get(0).getId(), memberShop.getShopName(), memberShop.getAddress());
						if(row>0){
							sId = row;
							MemberSend mSend = memberSendMapper.selectByPrimaryKey(sId);
							row = insertOrderSendInfo(orderShop, mSend);//创建配送订单
						}
					}else{
						row = insertOrderSendInfo(orderShop, send);//创建配送订单
					}
				}else{//创建member/member_ser/member_send
					row = insertMember(memberShop);
					if(row>0){
						mId = row;
					}
					row = insertMemberSend(mId, memberShop.getShopName(), memberShop.getAddress());
					if(row>0){
						sId = row;
						MemberSend mSend = memberSendMapper.selectByPrimaryKey(sId);
						row = insertOrderSendInfo(orderShop, mSend);//创建配送订单
					}
				}
				WXMSgTemplate template = new WXMSgTemplate();
				template.setOrderShopId(orderShop.getId()+"");
				wechatTemplateMsgService.sendGroupGoodTemplate(template);
				row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
			}
		}
		return row;
	}
	
	//插入配送订单信息
	private int insertOrderSendInfo(OrderShop orderShop, MemberSend send){
		int row = 0;
		OrderSendInfo sendInfo = new OrderSendInfo();
		sendInfo.setOrderShopId(orderShop.getId()); //商家订单id
		sendInfo.setmId(orderShop.getmId()); //用户id
		sendInfo.setsId(send.getmId()); //配送员id
		sendInfo.setOcTime(new Date());
		sendInfo.setsName(send.getFullName()); //配送员名称
		sendInfo.setdState(Contants.DELIVER_WAIT_SEND); //0(初始状态)待发货
		row = ordersendMapper.insertSelective(sendInfo);
		return row;
	}
	
	//插入普通用户信息
	private int insertMember(MemberShop entity){
		int row = 0;
		Member member = new Member();
		member.setType(2);
		if(entity.getShopName()!=null){
			member.setUsername(entity.getShopName());
		}else{
			member.setUsername("匿名");
		}
		member.setPhone(entity.getLinkmanPhone());
		if(entity.getLogo()!=null){
			member.setHeadimgurl(entity.getLogo());
		}
		String first = MD5Util1.encode("123456");
		String salt = MD5Util1.genCodes(first);
		first = first + salt;
		String second = MD5Util1.encode(first);
		member.setPassword(second);
		member.setSalt(salt);
		row = memberMapper.insertSelective(member);
		
		MemberUser user  = new MemberUser();
		user.setmId(member.getId());
		user.setAddtime(new Date());
		//获取签到规则： 1，签到 2,浇水 3,拼团 4单买 5,分享,6注册积分，7购物积分
		SeedScoreRule se = new SeedScoreRule();
		se.setScoreAction(6);
		List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
		if (rule.size() < 1) {
			try {
				insertRule(rule);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			rule = seedScoreRuleMapper.selectRuleByParams(se);
		}
		if (rule.size() > 0) {
			//状态 0关 1开
			if (rule.get(0).getStatus().equals(1)) {
				user.setPoint(rule.get(0).getScore());
			}
		}
		user.setNote("商家自配账号");
		row = memberUserMapper.insertSelective(user);//插入用户信息
		if(row ==1 ){
			row = member.getId();
		}
		return row;
	}
	
	//插入配送用户信息
	private int insertMemberSend(int id, String shopName, String address){
		int row = 0;
		if(id>0){
			MemberSend send = new MemberSend();
			send.setmId(id);
			if(!StringUtils.isEmptyOrWhitespaceOnly(shopName)){
				send.setFullName(shopName);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(address)){
				send.setAddress(address);
			}
			send.setTime(new Date());
			send.setScope(Contants.SEND_SCOPE);
			send.setTool(3);
			send.setStatus(1);
			row = memberSendMapper.insertSelective(send); //插入配送员信息
		}
		if(row>0){
			row = id;
		}
		return row; 
	}

	/**
	 * 商家订单处会员信息
	 */
	@Override
	public Map<String, Object> getMemerUserInfo(String mId) throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		Member member = memberMapper.selectByPrimaryKey(Integer.parseInt(mId));
		if(member!=null){
			map.put("userNick", member.getUsername()); //用户名
			map.put("phone", member.getPhone()); //联系电话
		}
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(Integer.parseInt(mId));
		if(memberUser!=null){
			map.put("fullName", memberUser.getFullName()); //真实姓名
			map.put("email", memberUser.getEmail()); //电子邮箱
			map.put("sex", memberUser.getSex()); //性别
			map.put("status", getStatus(memberUser.getStatus())); //状态
		}
		
		Map<String, Object> mapOne = new LinkedHashMap<>();
		int allCountNum = mapper.countBymId(Integer.parseInt(mId)); //总计订单（笔）
		double allCountMoney = 0.00;
		if(mapper.sumMoneyBymId(Integer.parseInt(mId))!=null){
			allCountMoney = Double.parseDouble(mapper.sumMoneyBymId(Integer.parseInt(mId)))/100; //总消费金额
		}
		
		
		int mothCountNum = mapper.countMonthOrder(Integer.parseInt(mId)); //用户本月订单（笔）
		double monthCountMoney = 0.00;
		if(mapper.sumMonthMoney(Integer.parseInt(mId))!=null){
			monthCountMoney = Double.parseDouble(mapper.sumMonthMoney(Integer.parseInt(mId)))/100; //本月消费金额
		}
		
		mapOne.put("allCountNum", allCountNum);
		mapOne.put("allCountMoney", allCountMoney);
		mapOne.put("mothCountNum", mothCountNum);
		mapOne.put("monthCountMoney", monthCountMoney);
		
		List<Map<String, Object>> orderList = new ArrayList<>();
		List<Order> list = mapper.selectBymIdAndMonth(Integer.parseInt(mId)); //本月消费订单记录
		if(list.size()>0){
			for(Order entity : list){
				Map<String, Object> orderMap = getOrder(entity);
				orderList.add(orderMap);
			}
			
		}
		mapOne.put("orderList", orderList);
		
		List<Map<String, Object>> addressList = new ArrayList<>(); //用户收货地址列表
		List<MemberUserAddress> aList = addressMapper.selectAllBymId(Integer.parseInt(mId));
		if(aList.size()>0){
			for(MemberUserAddress entity : aList){
				Map<String, Object> addressMap = getAddress(entity);
				addressList.add(addressMap);
			}
		}
		map.put("buyStyle", mapOne); //消费能力
		//map.put("buyRecord", orderList); //本月订单消费记录列表
		map.put("userAddress", addressList); //用户地址列表
		return map;
	}
	
	public static String getStatus(int status){
		String str = null;
		if(status==0 ){
			str = "正常";
		}else if(status==1){
			str = "删除";
		}else if(status==2){
			str = "锁定";
		}else if(status==3){
			str = "审核中";
		}else if(status==4){
			str = "认证通过";
		}
		return str;
	}
	
	public Map<String, Object> getOrder(Order entity){
		MemberUserAddress address = addressMapper.selectByPrimaryKey(entity.getmAddrId());
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("orerNo", entity.getOrderNo()); // 订单号
		map.put("fullName", address.getFullName()); //联系人
		map.put("mobile", address.getTel()); //联系电话
		map.put("orderPrice", (double)entity.getOrderPrice()/100); //商品总价
		map.put("realOrderPrice", (double)entity.getOrderPrice()/100); //实付金额
		map.put("addTime", entity.getAddtime()); //下单时间
		return map;
	}
	
	public Map<String, Object> getAddress(MemberUserAddress entity){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", entity.getId());
		map.put("fullName", entity.getFullName());
		map.put("address", entity.getProvname()+entity.getCityname()+entity.getAreaname());
		map.put("addressDetails", entity.getAddress());
		map.put("emailNo", null);
		map.put("tel", entity.getTel());
		map.put("useNum", null);
		return map;
	}
	
	
	/**
	 * 获得指定日期的后一天
	 * @author xieyc
	 * @param specifiedDay
	 * @return
	 */
	private  String getDayAfter(String endTimes) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		String dayAfter=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(endTimes);
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day + 1);
			dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayAfter;
	}


	@Override
	public PageBean<OrderShop> backGroundOrderList(OrderShop entity) throws Exception {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getShopOrderNo())) {
			List<String> orderShopNo = JsonUtils.stringToList(entity.getShopOrderNo());
			entity.setOrderShopNoList(orderShopNo);
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getOrderNo())) {
			List<String> orderNo = JsonUtils.stringToList(entity.getOrderNo());
			entity.setOrderNoList(orderNo);
		}
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		//status=10待分享订单的查询
		if ((entity.getStatus()!=null) && (entity.getStatus().equals(10))) {
			List<OrderShop> list = orderShopMapper.selectShareOrderListByShopId(entity);
			getRefundStatus(list);
			PageBean<OrderShop> pageBean = new PageBean<>(list);
			return pageBean;
		}
		//否则照着原来的路走
		else{
			List<OrderShop> list = orderShopMapper.backGroundOrderList(entity);
			getRefundStatus(list);
			PageBean<OrderShop> pageBean = new PageBean<>(list);
			return pageBean;
		}
	}
	
	@Override
	public PageBean<OrderShop> bgJdOrderList(OrderShop entity) throws Exception {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getShopOrderNo())) {
			List<String> orderShopNo = JsonUtils.stringToList(entity.getShopOrderNo());
			entity.setOrderShopNoList(orderShopNo);
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getOrderNo())) {
			List<String> orderNo = JsonUtils.stringToList(entity.getOrderNo());
			entity.setOrderNoList(orderNo);
		}
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<OrderShop> list = orderShopMapper.bgJdOrderList(entity);
		getRefundStatus(list);
		PageBean<OrderShop> pageBean = new PageBean<>(list);
		return pageBean;
	}
	

	@Override
	public PageBean<Order> backGroundAllOrderList(Order entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<Order> list = mapper.backGroundAllOrderList(entity);
		if(list.size()>0){
			list = orderInfo(list);
		}
		PageBean<Order> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	private List<Order> orderInfo(List<Order> list){
		for(Order order : list){
			double realPrice = (double)order.getOrderPrice()/100; //价格“分”转化成“元”
			order.setRealPrice(realPrice);
			
			double realDeliveryPrice = (double)order.getDeliveryPrice()/100; //邮费“分”转化成“元”
			order.setRealDeliveryPrice(realDeliveryPrice);
			
			StringBuffer buffer = new StringBuffer(); //拼接商品名称
			List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
			if(orderShopList.size()>0){
				for(OrderShop orderShop : orderShopList){
					List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
					if(skuList.size()>0){
						for(OrderSku sku : skuList){
							buffer.append(sku.getGoodsName()+",");
						}
					}
				}
				order.setJdOrderId(orderShopList.get(0).getJdorderid());
			}
			if(buffer.toString().length()>0){
				String goodsName = buffer.toString().substring(0, buffer.toString().length()-1);
				order.setGoodsName(goodsName);
			}
			
			OrderTeam orderTeam = orderTeamMapper.getByOrderNo(order.getOrderNo());
			if(orderTeam!=null){
				order.setGroupType(orderTeam.getStatus()); //拼团状态'-1 拼单失败 0拼团中 1成功',
				int robotNum = orderTeamMapper.countByTeamNoAndType(orderTeam.getTeamNo(), 1);
				order.setRobyNum(robotNum);
				
				int realNum = orderTeamMapper.countByTeamNoAndType(orderTeam.getTeamNo(), 0);
				order.setRealNum(realNum);
			}
			
			
			List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShopList.get(0).getId());
			if(skuList.size()>0){
				order.setGoodsImage(skuList.get(0).getSkuImage());//商品主图
				
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuList.get(0).getSkuId());
				Map<String, Object> map = new HashMap<>();
				if(goodsSku.getJdSkuNo()!=null){
					map.put("jdSkuNo", goodsSku.getJdSkuNo()); //京东商品sku
				}else{
					map.put("jdSkuNo", null); //京东商品sku
				}
				
				if(goodsSku.getJdPrice()==0){
					map.put("jdPirce", null);
				}else{
					map.put("jdPirce", (double)goodsSku.getJdPrice()/100); //京东价
				}
				if(goodsSku.getJdProtocolPrice()==0){
					map.put("jdProtocolPrice", null); 
				}else{
					map.put("jdProtocolPrice", (double)goodsSku.getJdProtocolPrice()/100); //协议价
				}

				map.put("num", skuList.get(0).getSkuNum()); //数量
				map.put("price", (double)skuList.get(0).getSkuSellPriceReal()/100); //单价
				
				JSONObject jsonObject = JSONObject.fromObject(skuList.get(0).getSkuValue());
				map.put("value", jsonObject);
				
				order.setGoodsSkuInfo(map);
				
				Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
				if(goods!=null){
					order.setTeamNum(goods.getTeamNum());
				}
			}
			
			Member member = memberMapper.selectByPrimaryKey(orderShopList.get(0).getmId()); //用户注册信息
			Map<String, Object> mbMap  = new LinkedHashMap<>();
			if(member!=null){
				mbMap.put("id", member.getId());
				mbMap.put("userNick", member.getUsername());
				mbMap.put("phone", member.getPhone());
			}else{
				mbMap.put("id", null);
				mbMap.put("userNick", null);
				mbMap.put("phone", null);
			}
			
			MemberUser memberUser = memberUserMapper.selectByPrimaryKey(orderShopList.get(0).getmId());
			if(memberUser!=null){
				mbMap.put("status", getStatus(memberUser.getStatus())); //状态
			}else{
				mbMap.put("status", null);
			}
			order.setUserInfo(mbMap);
			
			
			MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
			if(address!=null){
				order.setFullName(address.getFullName()); //买家姓名
				order.setTel(address.getTel());
				String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+address.getAddress();
				order.setAddress(userAddress);
			}
			
			MemberShop shop = memberShopMapper.selectByPrimaryKey(order.getShopId());
			if(shop!=null){
				order.setShopInfo(shop);
			}
			
		}
		return list;
	}


	@Override
	public OrderShop getOrderById(String id) {
		// TODO Auto-generated method stub
		return shopmapper.selectByPrimaryKey(Integer.parseInt(id));
	}


	@Override
	public List<OrderSku> getByOrderShopId(String id) {
		// TODO Auto-generated method stub
		return skumapper.getByOrderShopId(Integer.parseInt(id));
	}


	@Override
	public Order getOrderMainById(String id) {
		// TODO Auto-generated method stub
		return ordermapper.getOrderByOrderNo(id);
	}


	@Override
	public MemberUserAddress getUserById(Integer id) {
		// TODO Auto-generated method stub
		return muamapper.selectByPrimaryKey(id);
	}


	@Override
	public GoodsSku getGoodsSku(Integer id) {
		// TODO Auto-generated method stub
		return goodsSkuMapper.selectByPrimaryKey(id);
	}
	
	@Transactional
	@Override
	public void teamFail(String orderNo) {
		try{
			boolean falg=false;//标识是否退款成功
			OrderRefundDoc doc = new OrderRefundDoc();
			Order order = mapper.getOrderByOrderNo(orderNo);
			List<OrderSku> skuList = SkuMapper.getByOrderId(order.getId());
			OrderSku orderSku =skuList.get(0); 
			Goods goods = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
			
			OrderShop orderShopWithBh = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());//xieyc
			Integer savePrice=orderShopWithBh.getSavePrice();//所花的滨惠豆   xieyc 
			//获取优惠劵金额
			int amount = 0;	
			amount = orderSku.getSkuNum() * orderSku.getSkuSellPriceReal()-savePrice;//退款金额
			MemberShop memberShop =memberShopMapper.selectByPrimaryKey(order.getShopId());
			
			//zlk 2018.7.16
			if(order.getPaymentId()==3) {//微信退款
			
			   UnionPayRefundVO vo = new UnionPayRefundVO();
			   vo.setRefundAmount(amount+"");
			   vo.setMerOrderId(orderNo);
			   //vo.setMd5Key("fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR");
			   vo.setMd5Key(memberShop.getMd5Key());
			   String ret = HttpService.doPostJson(UnionPayInterfaceEnum.WXJSREFUND.getMethod(),vo);
			   if("SUCCESS".equals(ret.replaceAll("\"", ""))){
				  doc.setStatus(Contants.REFUND_SUCCESS);//2-成功
				  goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
				  goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
				  falg=true;//退款成功
			   }else{
				  doc.setStatus(Contants.REFUND_FAIL);//1-失败
				
			   }
			   goodsMapper.updateByPrimaryKeySelective(goods);
			   goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
			
			}else if(order.getPaymentId()==7){//钱包退款
			   
				 //根据  用户id 获取 钱包信息
	             List<Wallet> wallet = walletMapper.getWalletByUid(order.getmId());
	             wallet.get(0).setMoney(wallet.get(0).getMoney()+amount);
	             int num = walletMapper.updateByUid(wallet.get(0));
	             
	 			 //自己生成订单号
	 			 IDUtils iDUtils = new IDUtils();
	 			 WalletLog walletLog = new WalletLog();
				 walletLog.setAddTime(new Date()); //时间
				 walletLog.setAmount(amount);  //金额
				 walletLog.setmId(order.getmId());    //登录者id
				 walletLog.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE)); //订单号
				 walletLog.setInOut(0);  //进账
				 walletLog.setOrderId(order.getId());
				 walletLog.setRemark("拼团失败退款");//2018.7.10 zlk
				 if(num>0) {
				       walletLog.setStatus(1);//成功 状态 1
				 }else {
					   walletLog.setStatus(2);//失败 状态 2
				 }
			     walletLogMapper.insertSelective(walletLog); //保存一条充值记录到WalletLog表
	             
				
				
			   doc.setStatus(Contants.REFUND_SUCCESS);//2-成功
			   goods.setSale(goods.getSale()-orderSku.getSkuNum()); //销量--
			   goodsSku.setStoreNums(goodsSku.getStoreNums()+orderSku.getSkuNum()); //库存++
			   falg=true;//退款成功
			   goodsMapper.updateByPrimaryKeySelective(goods);
			   goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
				
			}
			Member member=memberMapper.selectByPrimaryKey(order.getmId());
			doc.setmName(member.getUsername());
			doc.setmPhone(member.getPhone());
			doc.setOrderSkuId(orderSku.getId());
			doc.setShopId(memberShop.getmId());
			doc.setReason(RefundReasonEnum.REFUND_TEAM.getReason());
			doc.setAddtime(new Date());
			doc.setDisposeTime(new Date());
			doc.setAdminUser("机器人");
			doc.setAmount(amount);
			doc.setGoodsId(goods.getId());
			doc.setGoodsName(goods.getName());
			doc.setNote(RefundReasonEnum.REFUND_TEAM.getReason());
			doc.setOrderAmount(order.getOrderPrice());
			doc.setOrderId(order.getId());
			doc.setOrderShopId(orderSku.getOrderShopId());
			doc.setOrderShopNo(orderSku.getShopOrderNo());
			doc.setSkuId(goodsSku.getId());
			doc.setReason(RefundReasonEnum.REFUND_TEAM.getReason());
			doc.setmId(order.getmId());

			refundMapper.insertSelective(doc);
			
			orderShopWithBh.setIsRefund(2);// 将orderShop的is_refund设置2(全部退款)
			orderShopMapper.updateByPrimaryKey(orderShopWithBh);
			if(falg){
				orderSku.setRefund(2);//退款成功   将orderSku的is_refund设置2（退款完成）
			}else{
				orderSku.setRefund(1);//退款失败   将orderSku的is_refund设置1（退款中）
			}
			orderSkuMapper.updateByPrimaryKeySelective(orderSku);
			//微信公众号发送拼团失败的推送消息   
			double goodsPrice=(double)goodsSku.getTeamPrice()/100;//商品价格(单位元)
			double refundMoney=(double)amount/100;//退款金额（单位元）
			wechatTemplateMsgService.sendTeamFailTemplate(member.getOpenid(),goods.getName(),refundMoney,orderNo,goodsPrice);
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	
	
	}


	@Override
	public OrderShop getOrderLogistics(String id) {
		// TODO Auto-generated method stub
		  
		    OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		    try{
		    //设置快递配送信息参数
		    Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
		    String host = "https://wuliu.market.alicloudapi.com";
 	        String path = "/kdi";
 	        String method = "GET";
 	        String appcode = "232d013ef8244587a9a4f69cb2fcca47";
 	        Map<String, String> headers = new HashMap<String, String>();
 	        headers.put("Authorization", "APPCODE " + appcode);
 	        Map<String, String> querys = new HashMap<String, String>();
 	        querys.put("no", order.getExpressNo());
  
 	       //获取物流配送信息
 	        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
 	        String Logistics = EntityUtils.toString(response.getEntity());
 	    	JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
 	    	orderShop.setLogistics(jsonObject2);//物流配送信息
 	    	
		    orderShop.setExpressName(order.getExpressName()); //物流公司名字
		    orderShop.setExpressNo(order.getExpressNo());   //物流单号
		    
		    
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		return orderShop;
	}


	@Override
	public Order selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}
	
	
	public void insertRule(List<SeedScoreRule> list) {
		if (list.size() < 1) {
			for(int i=1;i<=7;i++){
				SeedScoreRule rule3 = new SeedScoreRule();
				rule3.setId(i);
				rule3.setScoreAction(i);
				rule3.setScore(0);
				rule3.setStatus(0);
				switch (rule3.getId()) {
				case 1:
					rule3.setName("签到积分");
					ScoreRuleExt ext = new ScoreRuleExt();
					ext.setSrId(1);
					ext.setExtKey(1);
					ext.setExtValue(1);
					ext.setIsDel(0);
					ext.setIsSeries(0);
					scoreRuleExtMapper.insertSelective(ext);
					break;
				case 2:
					rule3.setName("浇水");
					break;
				case 3:
					rule3.setName("拼团");
				    break;
				case 4:
					rule3.setName("单买");
					break;
				case 5:
					rule3.setName("分享积分");
					ScoreRuleExt ext1 = new ScoreRuleExt();
					ext1.setSrId(5);
					ext1.setExtKey(1);
					ext1.setExtValue(1);
					ext1.setIsDel(0);
					ext1.setIsSeries(0);
					scoreRuleExtMapper.insertSelective(ext1);
					break;
				case 6:
					rule3.setName("注册积分");
					break;
				case 7:
					rule3.setName("购物积分");
					break;
				default:
					rule3.setName("");
					break;
				}
				SeedScoreRule r = seedScoreRuleMapper.selectByPrimaryKey(i);
				if (r == null) {
					seedScoreRuleMapper.insertSelective(rule3);
				}else {
					seedScoreRuleMapper.updateByPrimaryKeySelective(rule3);
				}
			}
		}
	}

	public Order getOrderByOrderNo(String orderNo) {
		Order order=mapper.getOrderByOrderNo(orderNo);
		if(order!=null){
			List<OrderSku> skuOrderList=skumapper.getSkuListByOrderId(order.getId());
			order.setOrderSku(skuOrderList);
			OrderTeam orderTeam=orderTeamMapper.getByOrderNo(orderNo);
			String teamNo=null;
			Integer num =null;//剩余名额
			if(orderTeam!=null){
				teamNo=orderTeam.getTeamNo();
				Goods goods = goodsMapper.selectByPrimaryKey(skuOrderList.get(0).getGoodsId());
				int groupCount = orderTeamMapper.groupCount(orderTeam.getTeamNo());
				num=goods.getTeamNum()-groupCount;
			}
			order.setSurplusNum(num);
			order.setFullName(teamNo);
		}
		return order;
	}
	
	public String[] sharePackMap(int shopId, int status){
		OrderShop orderShop = new OrderShop();
		orderShop.setShopId(shopId);
		int count = orderShopMapper.selectShareCountByShopId(orderShop);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	public String[] sharePackMapByTime(int shopId, int status,Date startTimes,Date endTimes){
		int count = orderShopMapper.selectShareCountByShopIdByTime(startTimes,endTimes,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}


	@Override
	public List<Map<String, Object>> getAllShopName() throws Exception {
		List<Map<String, Object>>  mapList = new ArrayList<>();
		List<MemberShop> list = shopMapper.selectAllName();
		if(list.size()>0){
			for(MemberShop entity : list){
				Map<String, Object> map = new HashMap<>();
				map.put("id", entity.getmId());
				if(entity.getShopName()!=null){
					map.put("shopName", entity.getShopName());
				}else{
					map.put("shopName", null);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	/**
	 * 
	 * @Description: 统计某个时段的销售排行
	 * @author xieyc
	 * @throws ParseException 
	 * @date 2018年3月27日 下午2:37:24 
	 */
	public PageBean<OrderSku> getSaleOrderList(Integer shopId, String startTime, String endTime) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		endTimes=this.getDayAfter(endTimes);
		
		PageHelper.startPage(1, 10, true);
		List<OrderSku> listOrderSku= skumapper.getSaleOrderList(sf.parse(startTimes),sf.parse(endTimes),shopId);
		for(int i=0;i<listOrderSku.size();i++){
			listOrderSku.get(i).setTop(i+1);	
		}
		
		PageBean<OrderSku> pageBean = new PageBean<>(listOrderSku);
		return pageBean;
	}
	
	
	/**
	 * @Description: 退款售后详情查看
	 * @author fanjh
	 * @date 2018年8月29日 下午4:37:24 
	 */
	public MyOrderSkuPojo selectOrderDetail(Integer skuId){
		String returnAddress="";
		boolean showModifyButton=false;//是否显示修改按钮（true显示 false 不显示）
		boolean showLogisticsButton=false;//是否显示物流按钮（true显示 false 不显示）
		boolean showLookLogisticsButton=false;//是否显示查看物流按钮（true显示 false 不显示）
		boolean showTip=false;
		MyOrderSkuPojo myOrderSkuPojo=orderSkuMapper.selectBySkuId(skuId);
		double realPrice=(double)myOrderSkuPojo.getAmount()/100;
		myOrderSkuPojo.setRealPrice(realPrice);//退款金额  （当status=2的时候在页面显示）
		Goods goods=goodsMapper.selectByPrimaryKey(myOrderSkuPojo.getGoodsId());
		MemberShop memberShop=memberShopMapper.selectByPrimaryKey(myOrderSkuPojo.getShopId());
		if(goods.getIsJd()==0) {
			if(memberShop.getReturnAddress()!=null&&!"".equals(memberShop.getReturnAddress())) {
				returnAddress=memberShop.getReturnAddress();
			}
		}
		int status=myOrderSkuPojo.getStatus();//退款状态
		String blackColumn =null;//黑色栏位显示的字
		String redColumn =null;//黑色栏位显示的字
		if(status!=2 && status!=8){//换货成功或者 退款成功 就不允许修改售后
			showModifyButton=true;//显示修改售后按钮  
		}
		if(status==5){
			if(myOrderSkuPojo.getExpressName()!=null&&!"".equals(myOrderSkuPojo.getExpressName())) {
				showLookLogisticsButton=true;
				blackColumn="商家正在确认收货中";
				redColumn="";
				if (myOrderSkuPojo.getRefundValidTime() != null) {
					Date date2 = new Date(JedisUtil.getInstance().time());
					long diff = myOrderSkuPojo.getRefundValidTime().getTime() - date2.getTime();// 这样得到的差值是微秒级别
					if(diff>0) {
						myOrderSkuPojo.setValidTime(diff+"");
					}
					else {
						myOrderSkuPojo.setValidTime(0+"");
					}
						
				}
			}else {
				showLogisticsButton=true;
				blackColumn="商家已经同意你的申请";
				redColumn="请寄回商品";
				myOrderSkuPojo.setReturnAddress(returnAddress);
				if (myOrderSkuPojo.getLogisticsValidTime() != null) {
					Date date2 = new Date(JedisUtil.getInstance().time());
					long diff = myOrderSkuPojo.getLogisticsValidTime().getTime() - date2.getTime();// 这样得到的差值是微秒级别
					if(diff>0) {
						myOrderSkuPojo.setValidTime(diff+"");
					}
					else {
						myOrderSkuPojo.setValidTime(0+"");
					}
				}
			}
		}
		if(status==7){
			if(myOrderSkuPojo.getExpressName()!=null&&!"".equals(myOrderSkuPojo.getExpressName())) {
				blackColumn="商家正在审核您的换货申请";
				redColumn="";
				if (myOrderSkuPojo.getRefundValidTime() != null) {
					Date date2 = new Date(JedisUtil.getInstance().time());
					long diff = myOrderSkuPojo.getRefundValidTime().getTime() - date2.getTime();// 这样得到的差值是微秒级别
					if(diff>0) {
						myOrderSkuPojo.setValidTime(diff+"");
					}
					else {
						myOrderSkuPojo.setValidTime(0+"");
					}
				}
				showLookLogisticsButton=true;
			}else {
				blackColumn="商家已经同意你的申请";
				redColumn="请寄回商品";
				myOrderSkuPojo.setReturnAddress(returnAddress);
				if (myOrderSkuPojo.getRefundValidTime() != null) {
					Date date2 = new Date(JedisUtil.getInstance().time());
					long diff = myOrderSkuPojo.getLogisticsValidTime().getTime() - date2.getTime();// 这样得到的差值是微秒级别
					if(diff>0) {
						myOrderSkuPojo.setValidTime(diff+"");
					}
					else {
						myOrderSkuPojo.setValidTime(0+"");
					}
				}
				showLogisticsButton=true;
			}
		}
		if(status==3||status==6||status==9){
			if(myOrderSkuPojo.getLogisticsValidTime()!=null) {
				if(new Date().getTime()>myOrderSkuPojo.getLogisticsValidTime().getTime()) {
					blackColumn="物流信息填写超时，售后已取消";
					redColumn=myOrderSkuPojo.getRefuseReason();
				}
				else {
					blackColumn="商家已经拒绝了你的申请";
					redColumn=myOrderSkuPojo.getRefuseReason();
				}
			}else {
				blackColumn="商家已经拒绝了你的申请";
				redColumn=myOrderSkuPojo.getRefuseReason();
			}
				
		}
		if(status==0||status==10||status==11){
			blackColumn="商家正在审核你的申请";
			redColumn="请等待商家审核你的申请";
			if (myOrderSkuPojo.getRefundValidTime() != null) {
				Date date2 = new Date(JedisUtil.getInstance().time());
				long diff = myOrderSkuPojo.getRefundValidTime().getTime() - date2.getTime();// 这样得到的差值是微秒级别
				if(diff>0) {
					myOrderSkuPojo.setValidTime(diff+"");
				}
				else {
					myOrderSkuPojo.setValidTime(0+"");
				}
			}
			if(myOrderSkuPojo.getReason().equals("换货")) {
				showTip=true;
			}
		}
		if(status==1){
			blackColumn="申请退款异常";
			redColumn="请重新申请";
		}
		myOrderSkuPojo.setBlackColumn(blackColumn);
		myOrderSkuPojo.setRedColumn(redColumn);
		try {
			myOrderSkuPojo.setmName(URLDecoder.decode(myOrderSkuPojo.getmName(), "utf-8"));
			myOrderSkuPojo.setmPhone(URLDecoder.decode(myOrderSkuPojo.getmPhone(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(myOrderSkuPojo.getExpressName()!=null&&!"".equals(myOrderSkuPojo.getExpressName())) {
			showLogisticsButton=false;
		}
		myOrderSkuPojo.setShowModifyButton(showModifyButton);
		myOrderSkuPojo.setShowLogisticsButton(showLogisticsButton);
		myOrderSkuPojo.setShowLookLogisticsButton(showLookLogisticsButton);
		myOrderSkuPojo.setShowTip(showTip);
		myOrderSkuPojo.setStatusName(this.getSkuStatus(myOrderSkuPojo));
		
		return myOrderSkuPojo;
	}
	
	// 根据orderRefundDoc设置状态
	private String getSkuStatus(MyOrderSkuPojo doc) {
		String str = null;
		switch (doc.getStatus()) { // 退款状态，0:申请退款 1:退款失败 2:退款成功 3：已拒绝 5:申请退货中
									// 6:申请退货失败 7:换货中 8：换货成功 9：换货失败 10客服通过退款审核
									// 11收货客服审核通过',
		case 0:
			if (doc.getRefundType() == 1) {
				str = "退款中";
			}
			if (doc.getRefundType() == 2) {
				str = "换货中";
			}
			if (doc.getRefundType() == 3) {
				str = "退款退货中";
			}
			break;
		case 1:
			if (doc.getRefundType() == 1) {
				str = "退款失败";
			}
			if (doc.getRefundType() == 2) {
				str = "换货失败";
			}
			if (doc.getRefundType() == 3) {
				str = "退换失败";
			}
			break;
		case 2:
			if (doc.getRefundType() == 1) {
				str = "退款成功";
			}
			if (doc.getRefundType() == 2) {
				str = "换货成功";
			}
			if (doc.getRefundType() == 3) {
				str = "退换成功";
			}
			break;
		case 3:
			if (doc.getRefundType() == 1) {
				str = "退款已拒绝";
			}
			if (doc.getRefundType() == 2) {
				str = "换货已拒绝";
			}
			if (doc.getRefundType() == 3) {
				str = "退换已拒绝";
			}
			break;
		case 5:
			if (doc.getExpressName() != null && !"".equals(doc.getExpressName())) {
				str = "退款退货中";
			} else {
				str = "请填写物流";
			}
			break;
		case 6:
			str = "退货失败";
			break;
		case 7:
			if (!StringUtils.isNullOrEmpty(doc.getExpressNo())) {
				str = "换货中";
			} else {
				str = "请填写物流";
			}
			break;
		case 8:
			str = "换货成功";
			break;
		case 9:
			if (doc.getRefundType() == 1) {
				str = "退款失败";
			}
			if (doc.getRefundType() == 2) {
				str = "换货失败";
			}
			if (doc.getRefundType() == 3) {
				str = "退换失败";
			}
			break;
		case 10:
			if (doc.getRefundType() == 1) {
				str = "退款中";
			}
			if (doc.getRefundType() == 2) {
				str = "换货中";
			}
			if (doc.getRefundType() == 3) {
				str = "退款退货中";
			}
			break;
		case 11:
			if (doc.getRefundType() == 1) {
				str = "退款中";
			}
			if (doc.getRefundType() == 2) {
				str = "换货中";
			}
			if (doc.getRefundType() == 3) {
				str = "退款退货中";
			}
			break;
		default:
			str = "状态异常";
			break;
		}
		return str;
	}

	/**
	 * @Description: 填写物流信息
	 * @author fanjh
	 * @date 2018年8月29日 下午4:37:24
	 */
	public MyOrderSkuPojo writeLogistics(Integer orderSkuId) {
		MyOrderSkuPojo myOrderSkuPojo = orderSkuMapper.selectBySkuId(orderSkuId);
		try {
			myOrderSkuPojo.setmName(URLDecoder.decode(myOrderSkuPojo.getmName(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myOrderSkuPojo;
	}

	/**
	 * @Description: 提交物流信息
	 * @author fanjh
	 * @date 2018年8月29日 下午4:37:24
	 */
	public int saveLogistics(OrderRefundDoc orderRefundDoc) {
		int row = 0;
		orderRefundDoc.setSaveTime(new Date(JedisUtil.getInstance().time()));
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date(JedisUtil.getInstance().time()));
		ca.add(Calendar.MINUTE, Contants.tenDayLen);
		orderRefundDoc.setRefundValidTime(ca.getTime());
		row = orderRefundDocMapper.updateLogistics(orderRefundDoc);
		return row;
	}

	/**
	 * @Description: 查看物流信息
	 * @author fanjh
	 * @date 2018年8月29日 下午4:37:24
	 */
	public MyOrderSkuPojo selectLogistics(Integer orderSkuId) {
		MyOrderSkuPojo myOrderSkuPojo = orderSkuMapper.selectBySkuId(orderSkuId);
		return myOrderSkuPojo;
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
	public JSONObject queryLogistics(String expressNo,String type) throws Exception {
		JSONObject jsonObject=QueryLogistics.getByExpressInfo(expressNo, type);//查询物流信息
		return jsonObject;
	}

	/**
	 * @Description: 将order_main的物流信息迁移到order_shop里面
	 * @author xieyc
	 * @date 2018年9月11日 上午10:08:43
	 * @param
	 * @return
	 */
	public int moveLogistics(Integer id) {
		int row=0;
		List<OrderShop> list = orderShopMapper.getNullLogistics(id);
		for (OrderShop orderShopNull : list) {
			Order orderNull = mapper.selectByPrimaryKey(orderShopNull.getOrderId());
			if (org.apache.commons.lang.StringUtils.isBlank(orderShopNull.getExpressName())) {
				if (org.apache.commons.lang.StringUtils.isNotBlank(orderNull.getExpressName())) {
					orderShopNull.setExpressName(orderNull.getExpressName());
				}
			}
			if (org.apache.commons.lang.StringUtils.isBlank(orderShopNull.getExpressNo())) {
				if (org.apache.commons.lang.StringUtils.isNotBlank(orderNull.getExpressNo())) {
					orderShopNull.setExpressNo(orderNull.getExpressNo());
				}
			}
			row=orderShopMapper.updateByPrimaryKeySelective(orderShopNull);
		}
		return row;
	}

}
