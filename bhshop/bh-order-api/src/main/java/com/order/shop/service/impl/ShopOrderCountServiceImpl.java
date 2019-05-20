package com.order.shop.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.GoodsAttr;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.user.mapper.BusBankCardMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.TbBankMapper;
import com.bh.user.pojo.BusBankCard;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.TbBank;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import com.order.shop.service.ShopOrderCountService;

/**
 * @Description: 后台管理系统---统计接口
 * @author xieyc
 * @date 2018年1月6日 下午12:09:02 
 */
@Service
@Transactional
public class ShopOrderCountServiceImpl implements ShopOrderCountService {
	@Autowired
	private OrderShopMapper orderShopMapper;//商家订单
	@Autowired
	private OrderMapper orderMapper;//订单
	@Autowired
	private MemberUserAddressMapper userAddressMapper;// 用户收货地址
	@Autowired
	private OrderTeamMapper orderTeamMapper;// 拼团单
	@Autowired
	private MemberUserMapper memberUserMapper;//个人会员
	@Autowired
	private OrderSkuMapper skuMapper; //订单商品
	@Autowired
	private OrderRefundDocMapper refundMapper;//退款单
	@Autowired
	private MemberShopMapper  memberShopMapper;//店铺
	@Autowired
	private GoodsMapper goodsMapper;//商品
	@Autowired
	private MemberShopMapper shopMapper; //店铺
	@Autowired
	private BusBankCardMapper busBankCardMapper;
	@Autowired
	private TbBankMapper  tbBankMapper;
	
	
	
	/**
	 * 测试用
	 */
	public GoodsAttr test() {
		
		List<OrderShop> orderShopList=orderShopMapper.getByOrderId(null);
		if(orderShopList==null){
			System.out.println("listNull");
		}else{
			System.out.println(orderShopList.size());
		}
		
		
		Order order =orderMapper.selectByPrimaryKey(4118);
		if(order==null){
			System.out.println("objNull");
		}else{
			System.out.println(order.getId());
			System.out.println(order.getPaymentNo());
		}
		return null;
		
	}
	
	
	/**
	 * @Description: 进账统计--根据条件查询商家订单列表
	 * @author xieyc
	 * @date 2018年1月4日 上午10:54:54
	 */
	public PageBean<OrderShop> getShopOrderList(String strId, String strOrder_id, String order_no, String payment_no,
			String strPayment_id, String strStatus, String currentPage, String pageSize,Integer shopId) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		Integer id=null;
		Integer status=null;
		Integer payment_id=null;
		Integer order_id=null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(strId)){
			id=Integer.parseInt(strId);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strOrder_id)){
			order_id=Integer.parseInt(strOrder_id);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strPayment_id)){
			payment_id=Integer.parseInt(strPayment_id);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strStatus)){
			status=Integer.parseInt(strStatus);
		}
		List<OrderShop> listOrderShop = orderShopMapper.getShopOrderList(id,order_id,order_no, payment_no, payment_id,status,shopId);
		if(listOrderShop!=null&&listOrderShop.size()>0){
			for (OrderShop orderShop : listOrderShop) {
				double allPrice = (double)orderShop.getOrderPrice()/100;//商家订单总价“分”转化成“元”
				orderShop.setAllPrice(allPrice);
				Integer orderId=orderShop.getOrderId();
				Order order=orderMapper.selectByPrimaryKey(orderId);
				orderShop.setPayWay(order.getPaymentId());//设置支付方式
				orderShop.setPaymentNo(order.getPaymentNo());//设置第三方支付交易号
				orderShop.setPayStatus(order.getStatus());//设置支付状态
			}
		}
		PageBean<OrderShop> pageOrderShop = new PageBean<>(listOrderShop);
		return pageOrderShop;
	}
	/**
	 * @Description: 统计：根据id查询某个商家订单详情
	 * @author xieyc
	 * @date 2018年1月4日 上午20:52:54
	 */
	public OrderShop getOrderShopDetails(String id) {
		OrderShop orderShop=null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(id)){
			orderShop=orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		
			if(orderShop!=null){
				List<OrderSku> orderSkuList = skuMapper.getByOrderShopId(orderShop.getId());
				for(OrderSku orderSku : orderSkuList){
					double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					orderSku.setRealSellPrice(realSellPrice);//设置单价
				}
				orderShop.setOrderSku(orderSkuList);//设置该商家下所购买的所以商品的信息
				
				double allPrice = (double)orderShop.getOrderPrice()/100;//订单价格“分”转化成“元”
				orderShop.setAllPrice(allPrice);
				
				double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费“分”转化成“元”
				orderShop.setRealDeliveryPrice(realDeliveryPrice);//设置配送费
				
				double realgDeliveryPrice = (double)orderShop.getgDeliveryPrice()/100;//邮费“分”转化成“元”
				orderShop.setRealgDeliveryPrice(realgDeliveryPrice);
				BigDecimal b = new BigDecimal(allPrice-realgDeliveryPrice);
				orderShop.setRealOrderPrice( b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//设置订单原价（订单总价-邮费）
					
				OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderShop.getOrderNo());
				if(orderTeam!=null){
					orderShop.setGroupType(orderTeam.getStatus()); //拼团状态'-1 拼单失败 0拼团中 1成功',
				}
				
				Order order=orderMapper.selectByPrimaryKey(orderShop.getOrderId());
				
				MemberUserAddress userAddress = userAddressMapper.selectByPrimaryKey(order.getmAddrId());
				String address = userAddress.getProvname()+" "+userAddress.getCityname()+" "+ userAddress.getAreaname()+" "+userAddress.getAddress();
				String tel = userAddress.getTel();
				/*if(userAddress.getTelephone()!=null){
					tel = userAddress.getTel() +" "+ userAddress.getTelephone();
				}*/
				order.setFullName(userAddress.getFullName());//设置买家姓名
				order.setTel(tel);//设置联系方式
				order.setAddress(address);//设置买家地址
				
				Integer mId=userAddress.getmId();
				MemberUser memberUser=memberUserMapper.selectByPrimaryKey(mId);
				if(memberUser!=null){
					order.setMemberUserName(memberUser.getFullName());//设置会员名
				}
				orderShop.setOrder(order);
			}
		}
		return orderShop;
	}
	
	
	/**
	 * @Description: 统计-出账列表
	 * @author xieyc
	 * @throws ParseException 
	 * @date 2018年1月5日 下午2:55:10 
	 */
	public PageBean<OrderRefundDoc> countRefundList(String strId, String strOrder_id, String order_no, String payment_no,
			String strStatus, String currentPage, String pageSize, String startTime, String endTime,Integer shopId) throws ParseException {
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
		
		Integer id=null;
		Integer status=null;
		Integer order_id=null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(strId)){
			id=Integer.parseInt(strId);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strOrder_id)){
			order_id=Integer.parseInt(strOrder_id);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strStatus)){
			status=Integer.parseInt(strStatus);
		}
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderRefundDoc> listRefund = refundMapper.countRefundList(id,order_id,order_no,payment_no,status,sf.parse(startTimes), sf.parse(endTimes),shopId);		
		if(listRefund!=null && listRefund.size()>0){
			for (OrderRefundDoc orderRefundDoc : listRefund) {
				double realAmount = (double)orderRefundDoc.getAmount()/100;//退款金额“分”转化成“元”
				orderRefundDoc.setRealAmount(realAmount);//设置退款金额
				
				Integer orderId= orderRefundDoc.getOrderId();
				Order order =orderMapper.selectByPrimaryKey(orderId);
				String orderNo=order.getOrderNo();
				String paymentNo=order.getPaymentNo();
				orderRefundDoc.setPaymentNo(paymentNo);//设置支付单号
				orderRefundDoc.setOrderNo(orderNo);//设置订单单号
			}
		}
		PageBean<OrderRefundDoc> page = new PageBean<>(listRefund);
		return page;
	}

	/**
	 * @Description: 统计-出账列表中某条记录的详细列表
	 * @author xieyc
	 * @date 2018年1月4日 上午20:52:54
	 */
	public OrderRefundDoc getCountRefundDetail(String id) throws Exception {
		OrderRefundDoc orderRefundDoc=null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(id)){
			orderRefundDoc=refundMapper.selectByPrimaryKey(Integer.parseInt(id));
		
			if(orderRefundDoc!=null){
				double realAmount = (double)orderRefundDoc.getAmount()/100;//退款金额“分”转化成“元”
				orderRefundDoc.setRealAmount(realAmount);//设置退款金额
				
				Order order =orderMapper.selectByPrimaryKey(orderRefundDoc.getOrderId());
				MemberUserAddress userAddress = userAddressMapper.selectByPrimaryKey(order.getmAddrId());
				String address = userAddress.getProvname()+" "+userAddress.getCityname()+" "+ userAddress.getAreaname()+" "+userAddress.getAddress();
				String tel = userAddress.getTel();
				/*if(userAddress.getTelephone()!=null){
					tel = userAddress.getTel() +" "+ userAddress.getTelephone();
				}*/
				order.setFullName(userAddress.getFullName());//设置买家姓名
				order.setTel(tel);//设置联系方式
				order.setAddress(address);//设置买家地址
				Integer mId=userAddress.getmId();
				MemberUser memberUser=memberUserMapper.selectByPrimaryKey(mId);
				if(memberUser!=null){
					order.setMemberUserName(memberUser.getFullName());//设置会员名
				}
				
				OrderSku orderSku=skuMapper.selectByPrimaryKey(orderRefundDoc.getOrderSkuId());
				double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
				orderSku.setRealSellPrice(realSellPrice);//设置单价
				orderRefundDoc.setOrder(order);
				orderRefundDoc.setOrderSku(orderSku);
			}
		}
		return orderRefundDoc;
	}
	
	/**
	 * @Description: 统计--概要
	 * @author xieyc
	 * @date 2018年1月5日 下午7:13:47 
	 */
	public Map<String, Object> countOutline(Integer shopId) throws Exception {
		Map<String, Object> mapObj=new HashMap<String, Object>();
		
		Integer shopStatus=Contants.SHOP_STEP_CHECK_PASS;//店铺状态审核通过  (step 6审核中，7审核成功，8审核失败)
		Integer goodsStatus=Contants.GOODS_STATUES_PUTAWASY;//商品状态上架
		
		int allShopNum=0;//商家总数
		int allgoodsNum=0;//商品总数
		int ptGoodsNum=0;//平台商品		
		int shopGoodsNum=0;	//商家商品	
		double ptOrdersAmount=0.00;//平台下单金额
		double ptRefundOrderAmount=0.00;//平台退款金额
		double shopOrdersAmount=0.00;//商家下单金额
		double shopRefundOrdersAmount=0.00;//商家退款金额
		
		if(shopId!=null && shopId!=1){
			shopGoodsNum=goodsMapper.countShopGoods(goodsStatus,shopId);//统计某个商家商品数
			List<OrderShop> listShopOrder=orderShopMapper.getShopOrder(shopId);//查询某个商家已经支付的订单
			double shopOrdersAmountWithRef=0;
			if(listShopOrder!=null&& listShopOrder.size()>0){
				//统计某个商家下单总金额
				for (OrderShop orderShop : listShopOrder) {
					double orderPrice=(double)orderShop.getOrderPrice()/100;
					double gDeliveryPrice=(double)orderShop.getgDeliveryPrice()/100;//邮费
					double realPrice=orderPrice-gDeliveryPrice;
					shopOrdersAmountWithRef+=realPrice;
				}
			}
			shopRefundOrdersAmount=this.shopRefundMoney(shopId);//某个商家的所有退款成功订单金额
			shopOrdersAmount=shopOrdersAmountWithRef-shopRefundOrdersAmount;//商家下单金额（除去邮费和退款金额）
		}else{
			allShopNum=memberShopMapper.countShopCheckPass(shopStatus);//统计商家总数
			allgoodsNum=goodsMapper.countNormalGoods(goodsStatus);//统计商品总数
			ptGoodsNum=goodsMapper.countShopGoods(goodsStatus,shopId);//统计平台商品数
			List<OrderShop> listAllShopOrder=orderShopMapper.getShopOrder(shopId);//已支付的订单
			double ptOrdersAmountWithRef=0;
			if(listAllShopOrder!=null&& listAllShopOrder.size()>0){
				//统计平台下单总金额
				for (OrderShop orderShop : listAllShopOrder) {
					double orderPrice=(double)orderShop.getOrderPrice()/100;
					double gDeliveryPrice=(double)orderShop.getgDeliveryPrice()/100;//邮费
					double realPrice=orderPrice-gDeliveryPrice;
					//System.out.println(realPrice);
					ptOrdersAmountWithRef+=realPrice;
				}
			}
			ptRefundOrderAmount=this.shopRefundMoney(shopId);//平台退款成功的订单金额
			ptOrdersAmount=ptOrdersAmountWithRef-ptRefundOrderAmount;//平台下单金额（除去邮费和退款金额）
		}
		mapObj.put("allShopNum",allShopNum );
		mapObj.put("allgoodsNum",allgoodsNum);
		mapObj.put("ptGoodsNum",ptGoodsNum);
		mapObj.put("shopGoodsNum",shopGoodsNum);
		
		DecimalFormat  df= new DecimalFormat("######0.00");//精确2位   
		mapObj.put("ptOrdersAmount",df.format(ptOrdersAmount));
		mapObj.put("shopOrdersAmount",df.format(shopOrdersAmount));
		mapObj.put("ptOrderRefundAmount",df.format(ptRefundOrderAmount));
		mapObj.put("shopOrdersRefundAmount",df.format(shopRefundOrdersAmount));
		return mapObj;
	}
	
	/**
	 * @Description: 统计--7日内店铺销售TOP10
	 * @author xieyc
	 * @date 2018年1月6日 下午2:16:21
	 */
	public PageBean<OrderShop> getTopTenShopList() throws Exception {

		Integer currentPage = 1;// 第一页
		Integer pageSize = 10;// 前10条记录

		Date endTime = new Date(System.currentTimeMillis());// 当前时间
		Date startTime = this.getStartTime();// 获取开始时间

		PageHelper.startPage(currentPage, pageSize, true);

		List<OrderShop> listOrderShop = orderShopMapper.getTopTenShopList(startTime, endTime);
		for (int i = 0; i < listOrderShop.size(); i++) {
			OrderShop orderShop = listOrderShop.get(i);
			Integer shopId = orderShop.getShopId();
			MemberShop memberShop = shopMapper.selectByPrimaryKey(shopId);
			orderShop.setShopName(memberShop.getShopName());// 设置店铺名字
			double realAmount = (double) orderShop.getAmount() / 100;// 转化为元
			// 获取该7日类下单的订单,并且在该7日类退款的金额
			double shopRefundOrdersAmount = this.refundMoneyWithDay(startTime, endTime, orderShop.getShopId());// 商家退款金额
			double realShopOrderAmount = realAmount - shopRefundOrdersAmount;
			DecimalFormat df = new DecimalFormat("######0.00");// 精确2位
			orderShop.setRealShopOrderAmount(df.format(realShopOrderAmount));// 设置商家下单总价
		}
		//根据下单金额进行排序
		Collections.sort(listOrderShop, new Comparator<OrderShop>() {
			public int compare(OrderShop o1, OrderShop o2) {
				double amount1 = Double.valueOf(o1.getRealShopOrderAmount())*100;
				double amount2 = Double.valueOf(o2.getRealShopOrderAmount())*100;
				return (int)( amount2- amount1);
			}
		});
		for (int i = 0; i < listOrderShop.size(); i++) {
			OrderShop orderShop = listOrderShop.get(i);
			orderShop.setTop(i + 1);// 设置排序
		}
		PageBean<OrderShop> pageOrderShop = new PageBean<>(listOrderShop);
		return pageOrderShop;

	}
	/**
	 * @Description: 统计--7日内商品销售TOP10
	 * @author xieyc
	 * @throws ParseException 
	 * @date 2018年1月6日 下午2:16:21 
	 */
	public PageBean<OrderSku> getTopTenGoodsList() throws Exception {
		Integer  currentPage=1;//第一页
		Integer  pageSize=10;//前10条记录
		
	    Date endTime = new Date(System.currentTimeMillis());//当前时间
	    Date startTime=this.getStartTime();//获取开始时间
		
	    PageHelper.startPage(currentPage, pageSize, true);
	    
	    Integer orderSkuRefundStatus=0;//正常状态(商品订单的状态 ：'是否退款0.为正常,1.退款中,2.退款完成', )
	    
		List<OrderSku> listOrderSku= skuMapper.getTopTenGoodsList(startTime,endTime,orderSkuRefundStatus);
		for(int i=0;i<listOrderSku.size();i++){
			listOrderSku.get(i).setTop(i+1);	
		}
		PageBean<OrderSku> pageOrderSku = new PageBean<>(listOrderSku);
		return pageOrderSku;

	}
	/**
	 * @Description: 获取当前时间前6天的时间
	 * @author xieyc
	 * @date 2018年1月11日 下午8:16:09 
	 */
	private Date getStartTime() {
		Date startTime=null;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -6);//某天的前6天的时间
			startTime = calendar.getTime();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			startTime = sf.parse(sf.format(startTime));
			System.out.println(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startTime;
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
	/**
	 * @Description: 获取某个商家的退款成功金额（单位元）
	 * @author xieyc
	 * @date 2018年1月13日 上午10:31:24 
	 */
	private double shopRefundMoney(Integer shopId){
		Integer refundStatus=Contants.REFUND_SUCCESS;//退款成功
		List<OrderRefundDoc> listPtRefundOrder=refundMapper.getRefundOrder(shopId,refundStatus);//查询某个商家的所有退款成功订单
		double refundOrdersAmount=0;
		if(listPtRefundOrder!=null&&listPtRefundOrder.size()>0){
			//统计某个商家的退款金额
			for (OrderRefundDoc orderRefundDoc : listPtRefundOrder) {
				double orderPrice=(double)orderRefundDoc.getAmount()/100;
				refundOrdersAmount+=orderPrice;
			}
		}
		return refundOrdersAmount;
	}
	/**
	 * @Description: 所有退款成功金额（单位元）
	 * @author xieyc
	 * @date 2018年1月13日 上午10:31:24 
	 */
	private double refundMoney(){
		Integer refundStatus=Contants.REFUND_SUCCESS;//退款成功
		List<OrderRefundDoc> listPtRefundOrder=refundMapper.getRefundOrder(null,refundStatus);//查询所有退款成功订单
		double refundOrdersAmount=0;
		if(listPtRefundOrder!=null&&listPtRefundOrder.size()>0){
			//统计退款金额
			for (OrderRefundDoc orderRefundDoc : listPtRefundOrder) {
				double orderPrice=(double)orderRefundDoc.getAmount()/100;
				refundOrdersAmount+=orderPrice;
			}
		}
		return refundOrdersAmount;
	}
	
	/**
	 * @Description: 获取某个商家在该7日类下单的订单,并且在该7日类退款成功的金额
	 * @author xieyc
	 * @date 2018年1月13日 上午10:31:24 
	 */
	private double refundMoneyWithDay(Date startTime,Date endTime,Integer shopId){
		Integer refundStatus=Contants.REFUND_SUCCESS;//退款成功
		List<OrderRefundDoc> listPtRefundOrder=refundMapper.getRefundOrderWithDay(shopId,refundStatus,startTime,endTime);//查询所有退款成功订单
		double refundOrdersAmount=0;
		if(listPtRefundOrder!=null&&listPtRefundOrder.size()>0){
			//统计退款金额
			for (OrderRefundDoc orderRefundDoc : listPtRefundOrder) {
				double orderPrice=(double)orderRefundDoc.getAmount()/100;
				refundOrdersAmount+=orderPrice;
			}
		}
		return refundOrdersAmount;
	}

	/**
	 * @Description: 移动端查询某个商家的订单
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public PageBean<OrderShop> mgetShopOrderList(String currentPage, String pageSize, int shopId) {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderShop> listOrderShop = orderShopMapper.mgetByShopId(shopId);
		for (OrderShop orderShop : listOrderShop) {
			double allPrice = (double) orderShop.getOrderPrice() / 100;// 商家订单总价“分”转化成“元”
			orderShop.setAllPrice(allPrice);//订单总价
			Integer orderId = orderShop.getOrderId();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			orderShop.setPayWay(order.getPaymentId());// 设置支付方式
			orderShop.setOrderNo(order.getOrderNo());//订单编号
			orderShop.setAddtime(order.getAddtime());//下单时间
			List <OrderSku> orderSkuList= skuMapper.getByOrderId(order.getId());
			orderShop.setGoodName(orderSkuList.get(0).getGoodsName());//商品名字
		}
		PageBean<OrderShop> pageOrderShop = new PageBean<>(listOrderShop);
		return pageOrderShop;
	}
	
	/**
	 * @Description: 统计-出账列表
	 * @author xieyc
	 * @throws ParseException
	 * @date 2018年1月5日 下午2:55:10
	 */
	public PageBean<OrderRefundDoc> mgetRefundList(String currentPage, String pageSize, int shopId) {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderRefundDoc> listRefund = refundMapper.mgetRefundList(shopId);

		for (OrderRefundDoc orderRefundDoc : listRefund) {
			double realAmount = (double) orderRefundDoc.getAmount() / 100;// 退款金额“分”转化成“元”
			orderRefundDoc.setRealAmount(realAmount);// 设置退款金额

			Integer orderId = orderRefundDoc.getOrderId();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			String orderNo = order.getOrderNo();
			String paymentNo = order.getPaymentNo();
			orderRefundDoc.setPaymentNo(paymentNo);// 设置支付单号
			orderRefundDoc.setPayWay(order.getPaymentId());//设置支付方式
			orderRefundDoc.setOrderNo(orderNo);// 设置订单编号
			
			List <OrderSku> orderSkuList= skuMapper.getByOrderId(order.getId());
			orderRefundDoc.setGoodsName(orderSkuList.get(0).getGoodsName());//商品名字
		}
		PageBean<OrderRefundDoc> page = new PageBean<>(listRefund);
		return page;
	}


	/**
	 * @Description: 折线图数据展示
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public Map<String,Double> mgetAmountEveryDay(String day, int shopId) {
		Map<String,Double> map=new LinkedHashMap<String,Double>();
		for (int i = Integer.valueOf(day)-1;i >=0; i--) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)-i);
			Date today = calendar.getTime();
			// 查询某一天已支付的订单金额总和
			double amountEveryDay=orderShopMapper.mgetShopOrderEveryDay(shopId,today);
			double realamountEveryDay=amountEveryDay/100;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String result = format.format(today);
			map.put(result, realamountEveryDay);
		}
		return map;
	}


	/**
	 * @Description: 某个商家绑定的银行卡展示
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public List<BusBankCard> mgetBusBankCard(BusBankCard busBankCard) {
		List<BusBankCard> busBankCardList = busBankCardMapper.selectMemberBankCartByParams(busBankCard);
		String img = null;
		for (BusBankCard busBank : busBankCardList) {
			TbBank tbBank = tbBankMapper.getByBankName(busBankCard.getBankName());
			if (tbBank != null && tbBank.getImg()!=null) {
				img = tbBank.getImg();
			} else {
				img = Contants.DEFAULT_BANK_IMG;
			}
			busBank.setImg(img);
		}
		return busBankCardList;
	}
	
	/**
	 * @Description: 移动端查询某个商家进账和出账记录
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public PageBean<OrderShop> mgetCountDetailList(String currentPage, String pageSize, int shopId, int isFalg)
			throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderShop> listOrderShop =new ArrayList<OrderShop>();
		Integer isRefund=null;
		if(isFalg==0){//查询全部;
			listOrderShop = orderShopMapper.mgetCountDetailList(shopId,null);
		}else if(isFalg==1){//查询进账
			isRefund=0;
			listOrderShop = orderShopMapper.mgetCountDetailList(shopId,isRefund);
		}else{//查询出账
			isRefund=1;
			listOrderShop = orderShopMapper.mgetCountDetailList(shopId,isRefund);
		}
		//Map<String, List<OrderShop>> resultMap= new LinkedHashMap<String, List<OrderShop>>(); // 最终要的结
		for (OrderShop orderShop : listOrderShop) {
			
			Integer orderId = orderShop.getOrderId();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			orderShop.setPayWay(order.getPaymentId());// 设置支付方式
			orderShop.setOrderNo(order.getOrderNo());//订单编号
			orderShop.setAddtime(order.getAddtime());//下单时间
			List <OrderSku> orderSkuList= skuMapper.getByOrderId(order.getId());
			orderShop.setGoodName(orderSkuList.get(0).getGoodsName());//商品名字
			double allPrice =0;
			if(orderShop.getIsRefund()==0){
				allPrice = (double) orderShop.getOrderPrice() / 100;// 商家订单总价“分”转化成“元”
				
			}else{
				List <OrderRefundDoc> list =refundMapper.getRefundGoodsList(orderId);
				if(list.size()>0){
					allPrice = (double) list.get(0).getAmount()/ 100;//退款金额“分”转化成“元”
				}
			}
			orderShop.setAllPrice(allPrice);//订单总价或退款价格
			
			Date addtime=orderShop.getAddtime();
			Calendar cal = Calendar.getInstance();
			cal.setTime(addtime);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			StringBuffer sb=new StringBuffer();
			sb.append(year);
			sb.append("-");
			sb.append(month);
			orderShop.setMonth(month);
			orderShop.setYear(year);
			orderShop.setYearMonth(sb.toString());
//			if(resultMap.containsKey(sb.toString()+"")){
//				resultMap.get(sb.toString()+"").add(orderShop);
//			}else{
//				List<OrderShop> list = new ArrayList<OrderShop>();
//				list.add(orderShop);
//				resultMap.put(sb.toString()+"",list);
//			}
		}
		PageBean<OrderShop> pageOrderShop = new PageBean<>(listOrderShop);
		return pageOrderShop;
	}


	/**
	 * @Description: 更新商家绑定的银行信息
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public int mUpdateBusBankCard(BusBankCard busBankCard) {
		return busBankCardMapper.updateByPrimaryKeySelective(busBankCard);
	}
	/**
	 * @Description: 获取商家某个的银行信息
	 * @author xieyc
	 * @date 2018年3月15日 下午4:54:54
	 */
	public BusBankCard mgetBusBank(String id) {
		BusBankCard busBankCard=busBankCardMapper.selectByPrimaryKey(Integer.valueOf(id));
		String img=null;
		if(busBankCard!=null){
			TbBank tbBank =tbBankMapper.getByBankName(busBankCard.getBankName());
			if(tbBank!=null && tbBank.getImg()!=null){
				img=tbBank.getImg();
			}else{
				img=Contants.DEFAULT_BANK_IMG;
			}
		}
		busBankCard.setImg(img);
		return  busBankCard;
	}
	public static void main(String[] args) throws ParseException {
	/*    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    Date date = sdf.parse("2018-11-04");//初始日期   
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		System.out.println("Month: " + month);
	    System.out.println("Year: " + year);*/
		Integer i=null;
		System.out.println(i/100);
	}
	
	
	
}
