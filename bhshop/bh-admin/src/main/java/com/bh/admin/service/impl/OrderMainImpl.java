package com.bh.admin.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.bh.admin.service.JDOrderService;
import com.bh.admin.service.OrderMainService;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.admin.util.MessageUtil;
import com.bh.admin.util.PayUtil;
import com.bh.admin.vo.HttpUtils;
import com.bh.admin.vo.UnionPayRefundVO;
import com.bh.config.Contants;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.Track;
import com.bh.queryLogistics.QueryLogistics;
import com.bh.admin.enums.PayInterfaceEnum;
import com.bh.admin.enums.RefundReasonEnum;
import com.bh.admin.enums.UnionPayInterfaceEnum;
import com.bh.admin.mapper.goods.BargainRecordMapper;
import com.bh.admin.mapper.goods.CashDepositMapper;
import com.bh.admin.mapper.goods.CouponLogMapper;
import com.bh.admin.mapper.goods.CouponMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.HollandDauctionLogMapper;
import com.bh.admin.mapper.goods.JdOrderTrackMapper;
import com.bh.admin.pojo.goods.BargainRecord;
import com.bh.admin.pojo.goods.Coupon;
import com.bh.admin.pojo.goods.CouponLog;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.mapper.order.BhDictItemMapper;
import com.bh.admin.mapper.order.JdOrderMainMapper;
import com.bh.admin.mapper.order.JdOrderSkuMapper;
import com.bh.admin.mapper.order.OrderLogMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderRefundDocMapper;
import com.bh.admin.mapper.order.OrderSendInfoMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.pojo.order.BhDictItem;
import com.bh.admin.pojo.order.JdOrderMain;
import com.bh.admin.pojo.order.JdOrderSku;
import com.bh.admin.pojo.order.MyOrder;
import com.bh.admin.pojo.order.MyOrderShop;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderLog;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderSendInfo;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.order.SmallPayRefundPojo;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberNoticeMapper;
import com.bh.admin.mapper.user.MemberSendMapper;
import com.bh.admin.mapper.user.MemberShopAdminMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.mapper.user.ScoreRuleExtMapper;
import com.bh.admin.mapper.user.SeedScoreRuleMapper;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberNotice;
import com.bh.admin.pojo.user.MemberSend;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberShopAdmin;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.pojo.user.ScoreRuleExt;
import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.admin.pojo.user.WXMSgTemplate;
import com.bh.utils.JsonUtils;
import com.bh.utils.MD5Util1;
import com.bh.utils.PageBean;
import com.bh.utils.StatusNameUtils;
import com.bh.utils.pay.HttpService;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class OrderMainImpl implements OrderMainService{
	private static final Logger logger = LoggerFactory.getLogger(OrderMainImpl.class);
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private JdOrderTrackMapper jdOrderTrackMapper;
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
	private HollandDauctionLogMapper logMapper;
	@Autowired
	private CashDepositMapper cashDepositMapper;
	@Autowired
	private BargainRecordMapper  bargainRecordMapper;
	
	@Autowired
	private  JdOrderMainMapper jdOrderMainMapper;
	@Autowired
	private  JdOrderSkuMapper jdOrderSkuMapper;
	
	@Autowired
	private BhDictItemMapper bhDictItemMapper;
	
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
				
				OrderRefundDoc refund = refundMapper.getByOrderShopId(orderShop.getId()).get(0);
				if(refund!=null){
					orderShop.setRefundStatus(refund.getStatus());
				}
				
				MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
				if(address!=null){
					orderShop.setFullName(address.getFullName()); //买家姓名
					orderShop.setTel(address.getTel());
					String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+address.getFourname()+" "+address.getAddress();
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
//		    String host = "https://wuliu.market.alicloudapi.com";
//   	        String path = "/kdi";
//   	        String method = "GET";
//   	        String appcode = "232d013ef8244587a9a4f69cb2fcca47";
//   	        Map<String, String> headers = new HashMap<String, String>();
//   	        headers.put("Authorization", "APPCODE " + appcode);
//   	        Map<String, String> querys = new HashMap<String, String>();
//   	        querys.put("no", order.getExpressNo());
    
   	    
       try{
    	   
			double deductionPrice = 0;// 拍卖抵扣
			if (order.getFz() == 5) {
				if (order.getFz() == 5) {
					BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order.getOrderNo());// 拍卖记录
					if (bargainRecord != null) {
						deductionPrice = (double) bargainRecord.getDiscountPrice() / 100;
					}
				}
			}
			orderShop.setDeductionPrice(deductionPrice);// 抵扣金额
    	   
        	//获取物流配送信息
//   	        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//   	        String Logistics = EntityUtils.toString(response.getEntity());
//   	    	JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
//   	    	orderShop.setLogistics(jsonObject2);//物流配送信息
   	    	//zlk 2018.5.19
    	    if(org.apache.commons.lang.StringUtils.isNotBlank(orderShop.getExpressName())) {
		       orderShop.setExpressName(orderShop.getExpressName()); //物流公司名字
   	    	}else {
   	    	  // orderShop.setExpressName("滨惠商城"); //物流公司名字
   	    	}
   	    	if(org.apache.commons.lang.StringUtils.isNotBlank(orderShop.getExpressNo())) {
		       orderShop.setExpressNo(orderShop.getExpressNo());   //物流单号
   	    	}else {
   	    	   //orderShop.setExpressName(""); //物流公司名字
   	    	}
   	    	//end
		    orderShop.setAddtime(order.getAddtime()); //下单时间
		    orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		    if(order.getPaymentId()!=null){
			     orderShop.setPayWay(order.getPaymentId()); //支付方式:1货到付款 2 支付宝支付  3微信支付'
		    }
		    MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		    if(address!=null){
		    	String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+ address.getFourname()+" " +address.getAddress();
			    String tel = address.getTel();
			    if(address.getTelephone()!=null){
			      	tel = address.getTel() +" "+ address.getTelephone();
			    }
			    orderShop.setFullName(address.getFullName());
			    orderShop.setTel(tel);
			    orderShop.setAddress(addr);
		    }
		
		
		    double allPrice = (double)orderShop.getOrderPrice()/100;//订单总价“分”转化成“元”
		    DecimalFormat    df   = new DecimalFormat("######0.00");   
		    orderShop.setAllPrice(Double.parseDouble(df.format(allPrice-deductionPrice)));
		
		    double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费“分”转化成“元”
		    orderShop.setRealDeliveryPrice(realDeliveryPrice);
		    
		 	    		
		    double realgDeliveryPrice=0;	
		    CouponLog couponLog = couponLogMapper.getByIdAndCouType(orderShop.getCouponId(),2);//是否使用了免邮卷				
			if(couponLog!=null){//使用了免邮卷
				realgDeliveryPrice =  (double)orderShop.getCouponPrice()/100;
			}else{//没有使用
				realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;// 邮费“分”转化成“元”
			}
		  
		    orderShop.setRealgDeliveryPrice(realgDeliveryPrice);
		    
		    double realSavePrice = (double)orderShop.getSavePrice()/100;//滨惠豆的折扣“分”转化成“元”
		    orderShop.setRealSavePrice(realSavePrice);
		    
		    if(orderShop.getCouponId()>0){//xieyc
			    double realCouponsPrice = (double)orderShop.getCouponPrice()/100;//优惠卷的折扣“分”转化成“元”
			    orderShop.setRealCouponsPrice(realCouponsPrice);
			}else{
				 orderShop.setRealCouponsPrice(0);
			}
		    List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
		    
		    List<Map<Object,Object>> list = new ArrayList<>();
		    
			if (!orderShop.getJdorderid().equals("0") && !orderShop.getJdorderid().equals("-1")) {// 不是京东下单或者下单失败并且是发货或待评价状态
				Map<String, List<OrderSku>> mapListOrderSku = new HashMap<String, List<OrderSku>>();
				for (OrderSku myOrderSku : skuList) {
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
					Map<Object,Object> map=new HashMap<Object, Object>();
					jdSkuNo=this.disposeSkuList(listSku,orderShop);
					map.put("list", listSku);
					map.put("orderStock",orderStock(orderShop.getId()+"",jdSkuNo+""));//物流信息
					list.add(map);
				}
			}else {
				this.disposeSkuList(skuList,orderShop);
				Map<Object,Object> map=new HashMap<Object, Object>();
				map.put("list", skuList);
				map.put("orderStock",orderStock(orderShop.getId()+"","0"));//物流信息
				list.add(map);
			}
	    	
		    orderShop.setOrderSku1(list);
   	    }catch(Exception e){
	       e.printStackTrace();
	    }
	    return orderShop;
	}
	/**
	 * @Description: SKU处理
	 * @author xieyc
	 * @date 2018年8月9日 上午10:31:56
	 */
	public Long disposeSkuList(List<OrderSku> skuList,OrderShop orderShop) {
		Long returnSkuId=0l;
		for (OrderSku orderSku : skuList) {
			Long jdSkuNo=0l;
			if (!orderShop.getJdorderid().equals("0")&& !orderShop.getJdorderid().equals("-1")) {//京东订单
				JdOrderSku jdOrderSku=jdOrderSkuMapper.getByOrderSkuId(orderSku.getId());
				if(jdOrderSku!=null){
					jdSkuNo=jdOrderSku.getSkuId();
					returnSkuId=jdSkuNo;
				}
			}
			orderSku.setJdSkuNo(jdSkuNo);
			double realMarketPrice = (double) orderSku.getSkuMarketPrice() / 100;// 市场价格“分”转化成“元”
			double realSellPrice = (double) orderSku.getSkuSellPriceReal() / 100;// 支付价格“分”转化成“元”
			orderSku.setRealMarketPrice(realMarketPrice);
			orderSku.setRealSellPrice(realSellPrice);

			JSONObject jsonObject = JSONObject.fromObject(orderSku.getSkuValue());
			orderSku.setValueObj(jsonObject);

			MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
			orderSku.setShopName(shop.getShopName());
		}
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
		Order order = jdOrderService.getOrderMainById(orderShop1.getOrderNo()); // 根据订单号获取order_main
		MemberUserAddress mua = jdOrderService.getUserById(order.getmAddrId()); // 根据order_main
																				// 表的m_addr_id获取
																				// member_user_address表信息
		orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
		if (mua != null) {
			orderStock.setAddress(
					mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getFourname() + mua.getAddress());// 收货地址
		}
		// 图片地址
		List<OrderSku> listOrderSku = jdOrderService.getOrderSkuByOrderId(orderShop1.getOrderId());
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
				orderStock.setJd("3");
				track.setMsgTime("");
				track.setOperator("");
				track.setContent("已送达");// 内容
				listTrack2.add(track);
			} else { // 已发货
				orderStock.setJd("3");
				Track track2 = new Track();
				track2.setMsgTime("");
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

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			return orderStock;
		} else if (orderShop1.getDeliveryWay() == 2) {
			try {
				// 京东物流
				OrderShop orderShop = new OrderShop();
				orderShop.setId(Integer.parseInt(id));
				// 查询京东物流信息，获取当前订单妥投信息，如果为妥投，就更改订单状态
				orderStock = jdOrderService.orderTrack(orderShop, Long.parseLong(jdSkuNo));
				orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
				if (mua != null) {
					orderStock.setAddress(mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getFourname()
							+ mua.getAddress());// 收货地址
				}
				orderStock.setImgeUrl(url);
				orderStock.setJd("2"); // 是京东
			} catch (Exception e) {
				e.printStackTrace();
			}

			return orderStock;
		}
		return orderStock;
	}
	
	/**
	 * 平台后台订单详情
	 */
	@Override
	public Order getOrderDetails(String id) throws Exception {
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		double deductionPrice=0;//拍卖抵扣
		if(order.getFz()==5){
			BargainRecord bargainRecord = bargainRecordMapper.getByOrderNo(order.getOrderNo());//拍卖记录
			if(bargainRecord!=null){
				deductionPrice=(double)bargainRecord.getDiscountPrice()/100;
			}
		}
		order.setDeductionPrice(deductionPrice);//抵扣金额
		
		MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		if(address!=null){
			String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+ address.getFourname()+" "+address.getAddress();
			String tel = address.getTel();
			if(address.getTelephone()!=null){
				tel = address.getTel() +" "+ address.getTelephone();
			}
			order.setFullName(address.getFullName());
			order.setTel(tel);
			order.setAddress(addr);
		}
		List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		
		double realDeliveryPrice=0;
		if(orderShopList!=null){
			double realCouponsPrice=0;//优惠卷抵扣金额
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
				
				CouponLog couponLog = couponLogMapper.getByIdAndCouType(orderShop.getCouponId(),2);//是否使用了免邮卷	
				double deliveryPrice=0;
				if(couponLog!=null){//使用了免邮卷
					deliveryPrice =  (double)orderShop.getCouponPrice()/100;
				}else{//没有使用
					deliveryPrice = (double)orderShop.getgDeliveryPrice()/100;// 邮费“分”转化成“元”
				}
				realDeliveryPrice+=deliveryPrice;//邮费
								
				double realSavePrice = (double) order.getSavePrice()/100;
				order.setRealSavePrice(realSavePrice);
				
				if(orderShop.getCouponId()>0){
					double price=(double)orderShop.getCouponPrice()/100;//优惠卷的折扣“分”转化成“元”
					realCouponsPrice += price;
				}
			}
			order.setRealDeliveryPrice(realDeliveryPrice);
			order.setRealCouponsPrice(realCouponsPrice);
			double realSavePrice = (double) order.getSavePrice()/100;
			order.setRealSavePrice(realSavePrice);
			double realPrice = (double)order.getOrderPrice()/100;//订单总价“分”转化成“元”
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			order.setRealPrice(Double.parseDouble(df.format(realPrice-deductionPrice)));
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
		if(Integer.parseInt(status)==3){
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
			    //小程序退款
				else if (payType==5) {					
				      String appid = Contants.sAppId;//小程序ID   
				      String mch_id =Contants.s_mch_id;//商户号   
				      //System.out.println("mch_id--->"+mch_id);
				      String nonce_str = String.valueOf(System.currentTimeMillis());//UUIDHexGenerator.generate();//随机字符串   
				    
				      /**/   
				      SmallPayRefundPojo smallPayRefundPojo = new SmallPayRefundPojo();
				      smallPayRefundPojo.setAppid(appid);   
				      smallPayRefundPojo.setMch_id(mch_id);   
				      smallPayRefundPojo.setNonce_str(nonce_str);   				  
				      smallPayRefundPojo.setOut_trade_no(outTradeNo);
				      smallPayRefundPojo.setTotal_fee(totalAmountStr);   
				      smallPayRefundPojo.setRefund_fee(refundAmountStr); 
				      smallPayRefundPojo.setOut_refund_no(tradeNo);
				      
				      // 把请求参数打包成数组   
				      Map<String,String> sParaTemp = new HashMap<String,String>();   
				      sParaTemp.put("appid", smallPayRefundPojo.getAppid());   
				      sParaTemp.put("mch_id", smallPayRefundPojo.getMch_id());   
				      sParaTemp.put("nonce_str", smallPayRefundPojo.getNonce_str());   
				      sParaTemp.put("out_trade_no", smallPayRefundPojo.getOut_trade_no());   
				      sParaTemp.put("total_fee",smallPayRefundPojo.getTotal_fee());   
				      sParaTemp.put("refund_fee",smallPayRefundPojo.getRefund_fee());      
				      // 除去数组中的空值和签名参数   
				      Map<String,String> sPara = PayUtil.paraFilter(sParaTemp);   
				      String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串   
				      String key = "&key="+Contants.sKey; // 商户支付密钥   
				      //System.out.println("small app prestr--->"+prestr); 
				     //System.out.println("small app key1--->"+key); 
				      //MD5运算生成签名   
				      String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();  
				      //System.out.println("mysign---->"+mysign);
				      smallPayRefundPojo.setSign(mysign);   
				      //打包要发送的xml   
				      String respXml = MessageUtil.messageToXML(smallPayRefundPojo);   
				      // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”   
				      respXml = respXml.replace("__", "_");   
				      String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";//统一下单API接口链接   
				      String param = respXml; 
				      System.out.println("param---->"+param);
				      //String result = SendRequestForUrl.sendRequest(url, param);//发起请求   
				      String result =PayUtil.httpRequest(url, "POST", param);  
				      System.out.println("result--->"+result);
				      // 将解析结果存储在HashMap中   
				      Map<String,String> map = new HashMap<String,String>();   
				      InputStream in=new ByteArrayInputStream(result.getBytes());    
				      // 读取输入流   
				      SAXReader reader = new SAXReader();   
				      Document document = reader.read(in);   
				      // 得到xml根元素   
				      Element root = document.getRootElement();   
				      // 得到根元素的所有子节点   
				      @SuppressWarnings("unchecked")   
				      List<Element> elementList = root.elements();   
				      for (Element element : elementList) {   
				          map.put(element.getName(), element.getText());   
				      }   
				      // 返回信息   
				      String return_code = map.get("return_code");//返回状态码   
				      String return_msg = map.get("return_msg");//返回信息   
				      System.out.println("small app return_msg--->"+return_msg); 
				      System.out.println("small app return_code--->"+return_code);   
				      if(return_code=="SUCCESS"){   
				    	  String refund_fee = map.get("refund_fee");//去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
				    	  String total_fee = map.get("total_fee");//订单总金额，单位为分，只能为整数，详见支付金额
				    	  
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
				//如果退款金额小于或者等于0,则直接显示退款成功
				if (refundAmount<=0) {
					doc.setStatus(Contants.REFUND_SUCCESS);//2-成功
				}
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
					String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+ address.getFourname()+" "+address.getAddress();
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimes = "1994-01-01 00:00:00"; //设置默认起始时间
		String endTimes = "2094-01-01 00:00:00";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		//endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimes = "1994-01-01 00:00:00"; //设置默认起始时间
		String endTimes = "2094-01-01 00:00:00";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		//endTimes=this.getDayAfter(endTimes);//指定日期的后一天
				
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
				/*List<OrderSku> skuList=SkuMapper.getByOrderShopId(orderShop.getId());
				if(entity.getStatus()==-1) {
					skuList = SkuMapper.getByOrderShopIdAndRefund(orderShop.getId());
				}else {
					skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				}
				
				if(skuList.size()>0){
					if(!StringUtils.isEmptyOrWhitespaceOnly(skuList.get(0).getSkuImage())){
						orderShop.setGoodsImage(skuList.get(0).getSkuImage()); //商品图片
					}
				}*/
				orderShop.setGoodsImage(orderShop.getGoodsImage()); //商品图片
				setOrderStatus(orderShop); //设置订单状态
			}
		}
		PageBean<OrderShop> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	private OrderShop setOrderStatus(OrderShop entity){
		if((entity.getIsRefund()==1 && entity.getRefund()==0)||entity.getIsRefund()==0){
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
				if(entity.getIsRefund2()==0) {
					entity.setMystatus("待评价");
				}else {
					entity.setMystatus("已评价");
				}
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
			/*List<OrderSku> skuList=SkuMapper.getByOrderShopId1(entity.getId());
			for (OrderSku orderSku : skuList) {*/
				OrderRefundDoc doc = refundMapper.getByOrderShopId1(entity.getId(),entity.getOrderSkuId());
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
					if(doc.getStatus()==8){
						if(doc.getReason().equals("换货")){
							entity.setMystatus("换货成功");
						}
					}
					if(doc.getStatus()==9){
						if(doc.getReason().equals("换货")){
							entity.setMystatus("拒绝换货");
						}
					}
					if(doc.getStatus()==10||doc.getStatus()==11){
						if(doc.getReason().equals("退款")){
							entity.setMystatus("退款中");
						}
						if(doc.getReason().equals("退款退货")){
							entity.setMystatus("退款退货中");
						}
					}
			}
		}
		return entity;
	}
	
	
	private OrderShop setOrderStatus1(OrderShop entity){
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
			/*List<OrderSku> skuList=SkuMapper.getByOrderShopId1(entity.getId());
			for (OrderSku orderSku : skuList) {*/
				OrderRefundDoc doc = refundMapper.getByOrderShopId(entity.getId()).get(0);
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
					if(doc.getStatus()==8){
						if(doc.getReason().equals("换货")){
							entity.setMystatus("换货成功");
						}
					}
					if(doc.getStatus()==9){
						if(doc.getReason().equals("换货")){
							entity.setMystatus("拒绝换货");
						}
					}
			 // }		
			}
		}
		return entity;
	}
	
	public OrderShop mOrderDetails(OrderShop entity) throws Exception{
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(entity.getId());
        //设置快递配送信息参数
	    Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
//	    String host = "https://wuliu.market.alicloudapi.com";
//	    String path = "/kdi";
//	    String method = "GET";
//	    String appcode = "232d013ef8244587a9a4f69cb2fcca47";
//	    Map<String, String> headers = new HashMap<String, String>();
//	    headers.put("Authorization", "APPCODE " + appcode);
//	    Map<String, String> querys = new HashMap<String, String>();
//	    querys.put("no", order.getExpressNo());
	    
	   try{
    		//获取物流配送信息
//	        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//	        String Logistics = EntityUtils.toString(response.getEntity());
//	    	JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
//	    	orderShop.setLogistics(jsonObject2);//物流配送信息
	    	
		    //orderShop.setExpressName(order.getExpressName()); //物流公司名字
		   // orderShop.setExpressNo(order.getExpressNo());   //物流单号
		    orderShop.setAddtime(order.getAddtime()); //下单时间
		    orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		    if(order.getPaymentId()!=null){
			     orderShop.setPayWay(order.getPaymentId()); //支付方式:1货到付款 2 支付宝支付  3微信支付'
		    }
		    MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		    String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+ address.getFourname()+" "+address.getAddress();
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
		    
		    
		    setOrderStatus1(orderShop); //设置订单状态
		    
		    if(orderShop.getCouponId()>0){//xieyc
			    double realCouponsPrice = (double)orderShop.getCouponPrice()/100;//优惠卷的折扣“分”转化成“元”
			    orderShop.setRealCouponsPrice(realCouponsPrice);
			}else{
				 orderShop.setRealCouponsPrice(0);
			}
		    List<OrderSku> skuList=null;
	        if(entity.getStatus()==1) {
	        	skuList = SkuMapper.getByOrderShopId1(orderShop.getId());
	        }else {
	        	skuList = SkuMapper.getByOrderShopId2(orderShop.getId());
	        }
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimes = "1994-01-01 00:00:00"; //设置默认起始时间
		String endTimes = "2094-01-01 00:00:00";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		//Parameters: (大于等于)1994-01-01 00:00:00.0(Timestamp), (小于)2094-01-02 00:00:00.0(Timestamp)
		//endTimes=this.getDayAfter(endTimes);//指定日期的后一天
		
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
	 * 商家后台退款订单列表(客服)
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
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
					doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
					doc.setmPhone(URLDecoder.decode(doc.getmPhone(), "utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
					doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
				}

				
				
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
		OrderSku orderSku=orderSkuMapper.selectByPrimaryKey(doc.getOrderSkuId());
		if (orderShop.getCouponId() > 0) {// xieyc
			double realCouponsPrice = (double) orderShop.getCouponPrice() / 100;// 优惠卷的折扣“分”转化成“元”
			orderShop.setRealCouponsPrice(realCouponsPrice);
		} else {
			orderShop.setRealCouponsPrice(0);
		}
		double savePrice=0;
		if(orderShop.getSavePrice()>0){
			//GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(doc.getSkuId());
			//savePrice=(double)goodsSku.getScore()/100;//该商品所花的滨惠豆   xieyc
			savePrice=(double)orderSku.getSavePrice()/100;//该商品所花的滨惠豆   xieyc
		}
		orderShop.setRealSavePrice(savePrice);//滨惠豆抵扣
		if (doc !=null) {
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
				doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
			}
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
				doc.setmPhone(URLDecoder.decode(doc.getmPhone(),"utf-8"));
			}
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
				doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
			}
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getAfterSaleReasons())) {
				orderShop.setAfterSaleReasons(doc.getAfterSaleReasons());
			}
			
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getVoucherImage())) {
				orderShop.setVoucherImage(doc.getVoucherImage());
			}
			if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getSpecifications())) {
				orderShop.setSpecifications(doc.getSpecifications());
			}
		}
		
		
		if(doc.getStatus()==0){ //设置退款状态
			orderShop.setRefundStatus(0);
		}
		//订单的审核状态
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
		if(doc.getStatus()==5){
			orderShop.setRefundStatus(5);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==6){
			orderShop.setRefundStatus(6);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==7){
			orderShop.setRefundStatus(7);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==8){
			orderShop.setRefundStatus(8);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==9){
			orderShop.setRefundStatus(9);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==10){
			orderShop.setRefundStatus(10);
			orderShop.setDisposeTime(doc.getDisposeTime()); //审核时间
			orderShop.setAdminName(doc.getAdminUser()); //审核人
		}
		if(doc.getStatus()==11){
			orderShop.setRefundStatus(11);
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
		
		//TODO		
		double realgDeliveryPrice=0;	
		CouponLog couponLog = couponLogMapper.getByIdAndCouType(orderShop.getCouponId(),2);//是否使用了免邮卷				
		if(couponLog!=null){//使用了免邮卷
			realgDeliveryPrice =  (double)orderShop.getCouponPrice()/100;
		}else{//没有使用
			realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;// 邮费“分”转化成“元”
		}
	    orderShop.setRealgDeliveryPrice(realgDeliveryPrice);//邮费
		
		Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
		
		
		
		
		orderShop.setAddtime(order.getAddtime()); //下单时间
		orderShop.setPayStatus(order.getPaymentStatus()); //支付状态:1待付款（未支付）,2已付款（已支付）
		if(order.getPaymentId()!=null){
			orderShop.setPayWay(order.getPaymentId()); //支付方式1货到付款 2 支付宝支付  3微信支付'
		}
		
		MemberUserAddress address = addressMapper.selectByPrimaryKey(order.getmAddrId());
		String addr = address.getProvname()+" "+address.getCityname()+" "+ address.getAreaname()+" "+ address.getFourname()+" "+address.getAddress();
		
		orderShop.setFullName(doc.getmName());
		orderShop.setTel(doc.getmPhone());
		orderShop.setAddress(addr);
		
		
		double allPrice = (double)doc.getAmount()/100;//退款总价“分”转化成“元”
		orderShop.setAllPrice(allPrice);
		
		List<OrderSku> skuList = new ArrayList<>();
		
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
				if(org.apache.commons.lang.StringUtils.isNotBlank(memberShop.getShopName())) {
					orderShop.setExpressName(memberShop.getShopName());
				}
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
			if(org.apache.commons.lang.StringUtils.isNotBlank(member.getUsername())){
				member.setUsername(URLDecoder.decode(member.getUsername(), "utf-8"));
			}
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
		map.put("address", entity.getProvname()+entity.getCityname()+entity.getAreaname()+entity.getFourname());
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
				String userAddress = address.getProvname()+" "+address.getCityname()+" "+address.getAreaname()+" "+address.getFourname()+" "+address.getAddress();
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
		//endTimes=this.getDayAfter(endTimes);
		
		PageHelper.startPage(1, 10, true);
		List<OrderSku> listOrderSku= skumapper.getSaleOrderList(sf.parse(startTimes),sf.parse(endTimes),shopId);
		for(int i=0;i<listOrderSku.size();i++){
			Goods goods=goodsMapper.selectByPrimaryKey(listOrderSku.get(i).getGoodsId());
			if(goods!=null){
				listOrderSku.get(i).setGoodsName(goods.getName());
			}
			listOrderSku.get(i).setTop(i+1);
		}
		
		PageBean<OrderSku> pageBean = new PageBean<>(listOrderSku);
		return pageBean;
	}

	/**
	 * 商家后台退货退款订单列表(客服)  2018.4.20 zlk
	 */
	@Override
	public PageBean<OrderRefundDoc> refundGoodList(int shopId, String order_no,
			String status, String currentPage, String pageSize,
			String startTime, String endTime) throws Exception {
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
		List<OrderRefundDoc> list = refundMapper.refundGoodList(shopId, status, order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(OrderRefundDoc doc : list){
				//程凤云

				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
					doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
					doc.setmPhone(URLDecoder.decode(doc.getmPhone(), "utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
					doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
				}
				
				
				
				
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
	 * 商家后台退货订单列表 (客服)2018.4.20 zlk
	 */
	@Override
	public PageBean<OrderRefundDoc> changeGoodList(int shopId, String order_no,
			String status, String currentPage, String pageSize,
			String startTime, String endTime) throws Exception {
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
		List<OrderRefundDoc> list = refundMapper.changeGoodList(shopId, status, order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(OrderRefundDoc doc : list){
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
					doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
					doc.setmPhone(URLDecoder.decode(doc.getmPhone(), "utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
					doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
				}

				
				
				
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
	 * 商家后台退货退款订单列表(财务)  2018.4.20 zlk
	 */
	@Override
	public PageBean<OrderRefundDoc> refundFGoodList(int shopId,
			String order_no, String status, String currentPage,
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
		List<OrderRefundDoc> list = refundMapper.refundFGoodList(shopId, status, order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(OrderRefundDoc doc : list){
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
					doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
					doc.setmPhone(URLDecoder.decode(doc.getmPhone(), "utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
					doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
				}

				
				
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
     * 后台退款订单列表(财务) zlk 2018.4.24
     */
	@Override
	public PageBean<OrderRefundDoc> orderFRefundList(int shopId,
			String order_no, String status, String currentPage,
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
		List<OrderRefundDoc> list = refundMapper.orderFRefundList(shopId, status, order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(OrderRefundDoc doc : list){
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmName())) {
					doc.setmName(URLDecoder.decode(doc.getmName(),"utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getmPhone())) {
					doc.setmPhone(URLDecoder.decode(doc.getmPhone(), "utf-8"));
				}
				if (org.apache.commons.lang.StringUtils.isNotEmpty(doc.getNote())) {
					doc.setNote(URLDecoder.decode(doc.getNote(), "utf-8"));
				}

				
				
				
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

	
	
	
	//2018-5-11程凤云新增后台商家接口
	@Override
	public PageBean<MyOrderShop> MyOrderList(OrderShop entity) throws Exception {
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
			List<MyOrderShop> list = orderShopMapper.getSimpleShareOrderList(entity);
			getRefundStatus1(list);
			PageBean<MyOrderShop> pageBean = new PageBean<>(list);
			return pageBean;
		}
		//否则照着原来的路走
		else{
			List<MyOrderShop> list = orderShopMapper.MyBackGroundOrderList(entity);
			getRefundStatus1(list);
			PageBean<MyOrderShop> pageBean = new PageBean<>(list);
			return pageBean;
		}
	}
	
	public void getRefundStatus1(List<MyOrderShop> list){
		if(list.size()>0){
			for(MyOrderShop orderShop : list){
				//查询用户的状态
				MemberUser memberUser = memberUserMapper.selectUserStatus(orderShop.getmId());
				if (memberUser ==null) {
					orderShop.setUserStatus(null);
				}else{
					orderShop.setUserStatus(memberUser.getStatus());
				}
				
				//查询该订单是否有退款的情况
				OrderRefundDoc refund =new OrderRefundDoc();
				 //refund = refundMapper.getStatusByOrderShopId(orderShop.getId());
				OrderRefundDoc doc = new OrderRefundDoc();
				doc.setOrderSkuId(orderShop.getOrderSkuId());
				refund = refundMapper.selectByOrderSkuId(doc);
				Integer refundStatus=-1;
				String reason="";
				 if(refund!=null){	
					 refundStatus=refund.getStatus();
					 reason=refund.getReason();
				     orderShop.setRefundStatus(refund.getStatus());
			    }
				//2018.5.24  zlk
				try {
					if(org.apache.commons.lang.StringUtils.isNotBlank(orderShop.getUserNick())) {
					   orderShop.setUserNick(URLDecoder.decode(orderShop.getUserNick(), "utf-8"));
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//end
				orderShop.setStatusName(StatusNameUtils.getAdminStatusName(orderShop.getStatus(),refundStatus, reason));	
				if (orderShop.getStatus()==5 && orderShop.getIsRefund1()==1) {
					orderShop.setStatusName("已评价");
				}
				//查询该订单是否有拼团的情况
				OrderTeam orderTeam = orderTeamMapper.getTeamNoAndStatusByOrderNo(orderShop.getOrderNo());
				if(orderTeam!=null){
					 //拼团状态:-1 拼单失败  0拼团中  1成功
					orderShop.setGroupType(orderTeam.getStatus());
					//机器人拼单的数量
					int robotNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 1);
					orderShop.setRobotNum(robotNum);	
					//真人拼单的数量
					int realNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 0);
					orderShop.setRealNum(realNum);
				}
			}
		}
	}
	
	public void getRefundStatus2(List<MyOrderShop> list){
		if(list.size()>0){
			for(MyOrderShop orderShop : list){
				MemberUser memberUser = memberUserMapper.selectUserStatus(orderShop.getmId());
				if (memberUser ==null) {
					orderShop.setUserStatus(null);
				}else{
					orderShop.setUserStatus(memberUser.getStatus());
				}
				OrderRefundDoc refund = new OrderRefundDoc();
				OrderRefundDoc doc=new OrderRefundDoc();
				doc.setOrderSkuId(orderShop.getOrderSkuId());
				refund = refundMapper.selectByOrderSkuId(doc);
				Integer refundStatus=-1;
				String reason="";
			    //售后类型 1:退款 2:换货 3:退款退货
			    if(refund!=null){
			    	refundStatus=refund.getStatus();
			    	reason=refund.getReason();
			    	orderShop.setRefundStatus(refund.getStatus());
			    }
			    //2018.5.24  zlk			   
			    try {
			    	if(org.apache.commons.lang.StringUtils.isNotBlank(orderShop.getUserNick())) {
						orderShop.setUserNick(URLDecoder.decode(orderShop.getUserNick(), "utf-8"));
			    	}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			  
			    //end
				try {
					orderShop.setStatusName(StatusNameUtils.getAdminStatusName(orderShop.getStatus(), refundStatus, reason));
				} catch (Exception e) {
					// TODO: handle exception
				}
				OrderTeam orderTeam = orderTeamMapper.getTeamNoAndStatusByOrderNo(orderShop.getOrderNo());
				if(orderTeam!=null){
					orderShop.setGroupType(orderTeam.getStatus()); //拼团状态'-1 拼单失败 0拼团中 1成功',
					int robotNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 1);
					orderShop.setRobotNum(robotNum);						
					int realNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 0);
					orderShop.setRealNum(realNum);
				}
				
			}
		}
	}
	/**
	 * 后台订单数量统计管理
	 */
	@Override
	public Map<String, Object> OrderNumAccount1(int shopId) throws Exception {
		HashMap<String, Object> map = new LinkedHashMap<>();
		//该商家的全部记录数
		int count = orderShopMapper.orderShopCount(0,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(Contants.IS_ALL);
		String[] str = buffer.toString().split(","); //全部
		//1 待付款
		String[] strZero = backMap2(shopId, Contants.IS_WAIT_PAY);
		//2待发货
		String[] strOne =backMap2(shopId,2);
		//3已发货
		String[] strTwo = backMap2(shopId, Contants.IS_SEND);
		//5已收货 :待评价
		String[] strThree = backMap2(shopId,Contants.IS_WAIT_EVALUATE);
		//6已取消
		String[] strFour = backMap2(shopId,Contants.IS_CANCEL); 
		//7已评价
		String[] strFive = backMap2(shopId,Contants.IS_COMPLISH); 
		//8已删除
		String[] strSeven = backMap2(shopId, Contants.IS_DELETE); 
		//9备货中
		String[] strNine = backMap2(shopId,9); 
		//程凤云2018-3-23添加 待分享的订单的数量
		String[] ten = sharePackMapByTime1(shopId,10); 
		
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
	public String[] backMap1(int shopId,int status){
		int count =orderShopMapper.selectJdOrderListCount(status,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	public String[] sharePackMapByTime1(int shopId, int status){
		int count = orderShopMapper.selectShareCount(shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	
	
	
	//新增后台--平台列表
   public	PageBean<MyOrder> mypingtailist(Order entity)throws Exception{
	   PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<MyOrder> list = mapper.selectpingtailist(entity);
		if(list.size()>0){
			list = orderInfo1(list);
		}
		PageBean<MyOrder> pageBean = new PageBean<>(list);
		return pageBean;
   }
	private List<MyOrder> orderInfo1(List<MyOrder> list){
		for(MyOrder order : list){			
			OrderTeam orderTeam = orderTeamMapper.getTeamNoAndStatusByOrderNo(order.getOrderNo());
			if(orderTeam!=null){
				order.setGroupType(orderTeam.getStatus()); //拼团状态'-1 拼单失败 0拼团中 1成功',
				int robotNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 1);
				order.setRobyNum(robotNum);
				
				int realNum = orderTeamMapper.countIdByTeamNoAndType(orderTeam.getTeamNo(), 0);
				order.setRealNum(realNum);
			}
			
			MemberUser memberUser = memberUserMapper.selectUserStatus(order.getmId());
			if(memberUser!=null){
				order.setUserStatus(memberUser.getStatus());
			}else{
				order.setUserStatus(null);
			}
			OrderRefundDoc refund = new OrderRefundDoc();
			OrderRefundDoc doc=new OrderRefundDoc();
			doc.setOrderSkuId(order.getOrderSkuId());
			refund = refundMapper.selectByOrderSkuId(doc);
			Integer refundStatus=-1;
			String reason="";
			if(refund!=null){
				refundStatus=refund.getStatus();
				reason=refund.getReason();
				order.setRefundStatus(refund.getStatus());
			}	
			//2018.5.24 zlk
			try {
				if(org.apache.commons.lang.StringUtils.isNotBlank(order.getUserNick())) {
				  order.setUserNick(URLDecoder.decode(order.getUserNick(), "utf-8"));
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//end
			try {
				order.setStatusName(StatusNameUtils.getAdminStatusName(order.getOrderShopStatus(), refundStatus, reason));	
				if (order.getOrderShopStatus()==5 && order.getIsRefund()==1) {
					order.setStatusName("已评价");
				}
			} catch (Exception e) {
			}
		}
		return list;
	}
	public Map<String, Object> mypingtaicount() throws Exception{
		String[] str = backMapAll1(Contants.IS_ALL); //全部订单
		String[] strOne = backMapAll1(Contants.IS_WAIT_PAY); //待付
		String[] strTwo = backMapAll1(Contants.IS_WAIT_SEND); //已付
		String[] strThree = backMapAll1(Contants.IS_SEND); //完成
		
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		map.put("所有订单", str);
		map.put("待付款", strOne);
		map.put("已付款", strTwo);
		map.put("已完成", strThree);
		return map;
	}
	public String[] backMapAll1(Integer status){
		int count = mapper.pingtaicount(status);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	
	public PageBean<MyOrderShop> MyJDOrderList(OrderShop entity)throws Exception{
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getShopOrderNo())) {
			List<String> orderShopNo = JsonUtils.stringToList(entity.getShopOrderNo());
			entity.setOrderShopNoList(orderShopNo);
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(entity.getOrderNo())) {
			List<String> orderNo = JsonUtils.stringToList(entity.getOrderNo());
			entity.setOrderNoList(orderNo);
		}
		 PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<MyOrderShop> list = orderShopMapper.selectJDOrderList(entity);
		if (list.size()>0) {
			getRefundStatus2(list);
		}
		PageBean<MyOrderShop> pageBean = new PageBean<>(list);
		return pageBean;
	}
	public Map<String, Object> JDOrderNumAccount(int shopId) throws Exception{
		
        HashMap<String, Object> map = new LinkedHashMap<>();
        //该商家的全部京东商品的数量
		int count = orderShopMapper.selectJdOrderListCount(0,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(Contants.IS_ALL);
		String[] str = buffer.toString().split(","); //全部
		String[] strZero = backMap1(shopId,Contants.IS_WAIT_PAY);//1待付
		String[] strOne = backMap1(shopId,Contants.IS_WAIT_SEND);//2待发货
		String[] strTwo = backMap1(shopId,Contants.IS_SEND);//3已发货
		String[] strThree = backMap1(shopId,Contants.IS_WAIT_EVALUATE);//5已收货
		String[] strFour = backMap1(shopId,Contants.IS_CANCEL); //6已取消
		String[] strFive = backMap1(shopId,Contants.IS_COMPLISH); //7已评价
		String[] strSeven = backMap1(shopId,Contants.IS_DELETE); //8已删除
		String[] strNine = backMap1(shopId,9); //9备货中
		
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
	
	public String[] backMap2(int shopId,int status){
		int count =orderShopMapper.orderShopCount(status,shopId);
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(status);
		String[] str = buffer.toString().split(",");
		return str;
	}
	/**
	 * @Description: 交易总金额、订单总跳数统计
	 * @author xieyc
	 * @date 2018年5月17日 上午9:23:19   
	 */
	public Map<String, Object> orderAccount(OrderSku orderSku) {
		HashMap<String, Object> mapReturn = new LinkedHashMap<>();//返回的map
		
		double totalPrice = 0.00;//交易总金额
		double orderPrice = 0.00;//订单总金额(不含邮费)
		double refundAmount=0.00;//退款金额
		List<OrderSku>  listSku=orderSkuMapper.getShopOrderAmount(orderSku.getStartTime(),orderSku.getEndTime(),orderSku.getShopId());
		for (OrderSku sku : listSku) {
			orderPrice+=sku.getAmount();
			int refundMoney=refundMapper.getRefundAmount(orderSku.getStartTime(),orderSku.getEndTime(),sku.getShopId());//商家退款金额
			refundAmount+=refundMoney;
		}
		totalPrice=orderPrice-refundAmount;
		
		int allNum=orderShopMapper.getAllOrderShopNum(orderSku.getStartTime(),orderSku.getEndTime(),orderSku.getShopId());//查询商家全部订单条数
		int refundNum=0; //如果整个订单都退款成功 那么不能进入到订单总条数中
		boolean flag=true;//整个订单都退款成功 为true
		List<OrderShop>  listOrderShop=orderShopMapper.getAllRefund(orderSku.getStartTime(),orderSku.getEndTime(),orderSku.getShopId());//查询全部退款订单
		for (OrderShop orderShop : listOrderShop) {//此时的orderShop下面的商品是已经全部退款了
		    List<OrderRefundDoc> docList=refundMapper.getByOrderShopId(orderShop.getId());
		    for (OrderRefundDoc orderRefundDoc : docList) {
		    	if(orderRefundDoc.getStatus()!=2){//已经退款但是退款还没有成功
					flag=false;
					break;
				}
			}
			if(flag){
				refundNum++;
			}
		}
		int orderTotalNum=allNum-refundNum;//订单总条数（订单条数-整个订单都成功退款的订单）
		totalPrice = Math.floor(totalPrice*10)/1000;
		mapReturn.put("totalPrice", totalPrice+"元");//交易总金额
		mapReturn.put("orderTotalNum",orderTotalNum+"条");//订单总条数
		
		return mapReturn;
	}
	
	
	
	
	
	
}
