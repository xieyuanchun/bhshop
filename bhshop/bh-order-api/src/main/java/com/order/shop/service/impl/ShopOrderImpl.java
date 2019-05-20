package com.order.shop.service.impl;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Goods;
import com.bh.order.mapper.OrderLogMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderLog;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopAdmin;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.CoordinatesUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;
import com.order.shop.service.ShopOrderService;

@Service
@Transactional
public class ShopOrderImpl implements ShopOrderService{
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
	private MemberSendMapper sendMapper; // 配送员
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; // 商品属性
	@Autowired
	private MemberUserAddressMapper addressMapper; // 用户
	@Autowired
	private OrderLogMapper orderLogMapper;

	@Override
	public Order getShopOrderById(String id) throws Exception {
		return mapper.selectByPrimaryKey(Integer.parseInt(id));
	}
	
	/**
	 * 接单操作
	 * @param id 订单id
	 * @param adminUser 操作人员id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int orderReceiving(String id, String adminId) throws Exception{
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		String status = "12"; //status-12待抛单
		order.setStatus(Integer.parseInt(status));
		int row = mapper.updateByPrimaryKeySelective(order);
		return row;
	}


	/**
	 * 商家后台订单列表
	 */
	@Override
	public PageBean<Order> orderList(String id, String order_no, String status, String currentPage, String pageSize, String startTime, String endTime)
			throws Exception {
		List<Order> list = null;
		String isRefund = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; //设置默认起始时间
		String endTimes = "2094-01-01";
		if(!StringUtils.isEmptyOrWhitespaceOnly(startTime)){
			startTimes = startTime;
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(endTime)){
			endTimes = endTime;
		}
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		if(!StringUtils.isEmptyOrWhitespaceOnly(status)){
			if(Integer.parseInt(status)==9 || Integer.parseInt(status)==10 || Integer.parseInt(status)==11){ //9退款中，10退款失败，11退款成功
				isRefund = "1";
			}else{
				isRefund = "0";
			}
			list = mapper.orderList(Integer.parseInt(id), Integer.parseInt(status), order_no, sf.parse(startTimes), sf.parse(endTimes), Integer.parseInt(isRefund));		
			getRefundStatus(Integer.parseInt(id), list);
		}else{
			list = mapper.orderAllList(Integer.parseInt(id), order_no, sf.parse(startTimes), sf.parse(endTimes));
			getRefundStatus(Integer.parseInt(id), list);
		}
		PageBean<Order> page = new PageBean<>(list);
		return page;
	}
	
	public void getRefundStatus(int adminId, List<Order> list){
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		for(Order order : list){
			OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(order.getId(), admin.getShopId());
			if(orderShop.getdState()==1){
				order.setdState(1);; //商家订单显示已抛单状态
			}
			if(orderShop!=null){
				if(orderShop.getIsRefund()==1){
					OrderRefundDoc doc = refundMapper.getByOrderShopId(orderShop.getId());
					order.setRefundStatus(doc.getStatus());
				}
				order.setOrderShopStatus(orderShop.getStatus());
			}
			double orderPrice = orderPrice(order.getId(), adminId); //订单总金额

			order.setRealPrice(orderPrice);
		}	
	}
	
	/**
	 * 订单详情
	 */
	@Override
	public Order getOrderGoodsDetails(String id, String adminId, String fz) throws Exception {
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
		
		if(Integer.parseInt(fz)==1){ //商家后台订单详情
			MemberShopAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(adminId));
			OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(Integer.parseInt(id), admin.getShopId());
			
			double orderPrice = orderPrice(Integer.parseInt(id), Integer.parseInt(adminId)); //订单总金额
			order.setRealPrice(orderPrice);
			
			List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
			for(OrderSku orderSku : skuList){
				double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100;//市场价格“分”转化成“元”
				double realSellPrice = (double)orderSku.getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
				orderSku.setRealMarketPrice(realMarketPrice);
				orderSku.setRealSellPrice(realSellPrice);
				MemberShop shop = shopMapper.selectByPrimaryKey(orderSku.getShopId());
				orderSku.setShopName(shop.getShopName());
			}
			order.setOrderSku(skuList);
		}else{//平台后台订单详情
			
			List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
			if(orderShopList!=null){
				List<OrderSku> list = new ArrayList<>();
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
					list.addAll(skuList);
				}
				double orderPrice = (double)order.getOrderPrice()/100;//支付价格“分”转化成“元”
				order.setRealPrice(orderPrice);
				order.setOrderSku(list);
			}
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
	public Order castSheet(String id, String userJson, String deliveryPrice) throws Exception {
		 Map mapOne = JSON.parseObject(userJson, Map.class);
		 Integer userId = (Integer)mapOne.get("shopId");
		 if(StringUtils.isEmptyOrWhitespaceOnly(userId+"")){
		    	userId = 1;
		}
		
		Integer price = (int)(Double.parseDouble(deliveryPrice)*100);
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		//MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(Integer.parseInt(id), userId);
		orderShop.setdState(Contants.CATE_ONE);//1抛单
		orderShop.setDeliveryPrice(price); //设置物流价
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		
		List<OrderSku> list = SkuMapper.getByOrderShopId(orderShop.getId());
		for(OrderSku sku : list){
			sku.setIsSend(true);//已发货
			SkuMapper.updateByPrimaryKeySelective(sku);
		}
		
		List<OrderShop> orderShopList = orderShopMapper.getByOrderIdAndStatus(Integer.parseInt(id)); //查询同一订单下的商家订单是否全部发货
		if(orderShopList.size()>0){
			order.setDeliveryStatus(Contants.DELIVERY_PART); //2部分发货
		}else{
			order.setDeliveryStatus(Contants.DELIVERY_ALL); //1已发货
		}
		mapper.updateByPrimaryKeySelective(order);
		if(row ==0){
			order = null;
		}
		
		//cheng
		String username = (String) mapOne.get("username");
		Integer uId = (Integer)mapOne.get("userId");
		OrderLog orderLog = new OrderLog();
		orderLog.setAction("抛单");
		orderLog.setAddtime(new Date());
		orderLog.setAdminUser(username);
		orderLog.setUserType("商家用户");
		orderLog.setOrderStatus(orderShop.getStatus());
		orderLog.setUserTable("sys_user");
		orderLog.setUserId(uId);
		orderLog.setOrderId(orderShop.getOrderId());
		orderLog.setOrderNo(orderShop.getOrderNo());
		orderLogMapper.insertSelective(orderLog);
		return order;
	}
	
	/**
	 * 商家订单商品抛单操作
	 */
	@Override
	public Order castSheetOne(String id) throws Exception {
		OrderSku sku = SkuMapper.selectByPrimaryKey(Integer.parseInt(id));
		Order order = mapper.selectByPrimaryKey(sku.getOrderId());
		sku.setdState(Contants.CATE_ONE); //1抛单
		sku.setIsSend(true);//已发货
		int row = SkuMapper.updateByPrimaryKeySelective(sku);
		
		List<OrderSku> skuList = SkuMapper.getByOrderShopIdAndStatus(sku.getOrderShopId());
		if(skuList.size()==0){
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sku.getOrderShopId());
			orderShop.setStatus(Contants.IS_SEND); //3已经发货
		}
		if(row == 0){
			order = null;
		}
		return order;
	}
	
	/**
	 * 订单商品列表
	 */
	@Override
	public PageBean<OrderSku> getOrderGoodsList(int adminId, String orderId, String currentPage, String pageSize) throws Exception {
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(Integer.parseInt(orderId), admin.getShopId());
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize),true);
		List<OrderSku> list = SkuMapper.getByOrderShopId(orderShop.getId());
		for(OrderSku orderSku : list){
			double realPrice = (double)orderSku.getSkuSellPriceReal()/100; //支付价格“分”转化成“元”
			orderSku.setRealSellPrice(realPrice);
			double realMarketPrice = (double)orderSku.getSkuMarketPrice()/100; //市场价格“分”转化成“元”
			orderSku.setRealMarketPrice(realMarketPrice);
		}
		PageBean<OrderSku> pageBean = new PageBean<>(list);

		return pageBean;
	}
	
	/**
	 * 批量删除
	 */
	@Transactional
	@Override
	public List<Order> batchDelete(String id) throws Exception {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		         list.add(str[i]);
		}
		List<Order> orderList = new ArrayList<Order>();
		for(int i=0; i<list.size(); i++){
			Order order = mapper.selectByPrimaryKey(Integer.parseInt(list.get(i)));
			orderList.add(order);
		}
		int row = mapper.batchDelete(list);
		if(row==0){
			orderList = null;
		}
		return orderList;
	}

	/**
	 * 
	 * 退款单商品列表
	 */
	@Override
	public PageBean<OrderRefundDoc> getRefundGoodsList(String orderId, String currentPage, String pageSize) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize),true);
		List<OrderRefundDoc> list = refundMapper.getRefundGoodsList(Integer.parseInt(orderId));//退款商品列表
		for(OrderRefundDoc orderRefundDoc : list){
			double realPrice = (double)orderRefundDoc.getAmount()/100; //价格“分”转化成“元”
			orderRefundDoc.setRealPrice(realPrice);
		}
		int count = refundMapper.countRefundGoodsList(Integer.parseInt(orderId)); //总条数
		for(OrderRefundDoc doc : list){
			Member member = memberMapper.selectByPrimaryKey(doc.getmId());
			doc.setmName(member.getUsername());
			Goods goods  = goodsMapper.selectByPrimaryKey(doc.getGoodsId());
			doc.setGoodsName(goods.getName());
		}
		PageInfo<OrderRefundDoc> info = new PageInfo<>(list);			
		PageBean<OrderRefundDoc> pageBean = new PageBean<>(list);//分页插件
		pageBean.setTotal(count);
		pageBean.setList(info.getList());

		return pageBean;
	}
	
	/**
	 * 商家取消订单
	 */
	@Override
	public Order cancelOrder(String id, String adminId) throws Exception {
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(adminId));
		OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(Integer.parseInt(id), admin.getShopId());
		orderShop.setStatus(Contants.IS_CANCEL); //5取消
		int row = orderShopMapper.updateByPrimaryKeySelective(orderShop);
		if(row == 0){
			order=null;
		}
		return order;
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
	 * 审核退款
	 */
	@Transactional
	@Override
	public Order auditRefund(String id, String status, String adminId) throws Exception {
		int row = 0;
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(adminId));
		OrderShop orderShop = orderShopMapper.getByOrderIdAndShopId(Integer.parseInt(id), admin.getShopId());

		OrderRefundDoc doc = refundMapper.getByOrderShopId(orderShop.getId());
		doc.setStatus(Integer.parseInt(status));
		row = refundMapper.updateByPrimaryKeySelective(doc);
		if(row == 0){
			order = null;
		}
		return order;
	}
	
	/**
	 * 配送端订单列表
	 */
	@Override
	public PageBean<Order> deliveryWaitList(String orderNo, String fz, String currentPage, String pageSize, int mId) throws Exception {
		mId = 73;
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize),true);
		List<Order> list = null;
		List<Order> otherList = new ArrayList<>();
		int count = 0;
		if(Integer.parseInt(fz)==1){ //1-待配送，2-配送中，3已完成，4待结算
			list = mapper.list(0, 1, orderNo);
			MemberShop memberShop = null;
			Double distance = 0.00;
			for(Order order: list){
				memberShop = shopMapper.selectByPrimaryKey(order.getShopId());
				if(memberShop!=null){
					MemberSend memberSend = sendMapper.selectByPrimaryKey(mId); //计算该单是否在配送范围内
					distance = CoordinatesUtil.Distance(memberShop.getLon(), memberShop.getLat(), memberSend.getLon(), memberSend.getLat());
					if(Integer.parseInt(memberSend.getScope())>distance){
						otherList.add(order);
					}
				}
			}
			list = otherList;
			count = list.size();
		}
		if(Integer.parseInt(fz)==2){ 
			list = mapper.list(0, 2, orderNo);
			count = mapper.listCount(0, 2, orderNo);
		}
		if(Integer.parseInt(fz)==3){ 
			list = mapper.list(0, 3, orderNo);
			count = mapper.listCount(0, 3, orderNo);
		}
		if(Integer.parseInt(fz)==4){ 
			list = mapper.waitList(0, 3, orderNo, 2);
			count = mapper.waitListCount(0, 3, orderNo, 2);
		}
		
		for(Order order : list){
			List<OrderSku> orderSku = SkuMapper.getOrderGoodsList(order.getId());  //查询订单下的商品列表
			for(OrderSku sku : orderSku){
				Goods goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
				sku.setGoodsName(goods.getName());
			}
			order.setOrderSku(orderSku);
		}
				
		PageBean<Order> pageBean = new PageBean<>(list);
		pageBean.setTotal(count);
		pageBean.setList(list);

		return pageBean;
	}
	
	/**
	 * 配送端抢单
	 */
	@Override
	public Order robOrder(String id, String sId) throws Exception {
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		order.setIsShopCheckout(2);//待结算
		int row = mapper.updateByPrimaryKeySelective(order);
		List<OrderSku> list = SkuMapper.getOrderGoodsList(Integer.parseInt(id));
		for(OrderSku sku : list){
			sku.setsId(Integer.decode(sId)); //绑定订单商品配送员id
			sku.setdState(2); //配送中
			SkuMapper.updateByPrimaryKeySelective(sku);
		}
		if(row == 0){
			order = null;
		}
		return order;
	}
	
	/**
	 * 配送端商品已送达
	 */
	@Override
	public Order alreadyDelivery(String id) throws Exception {
		int row = 0;
		List<OrderSku> list = SkuMapper.getOrderGoodsList(Integer.parseInt(id));
		for(OrderSku sku : list){
			sku.setdState(3); //待结算
			row = SkuMapper.updateByPrimaryKeySelective(sku);
		}
		Order order = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(row == 0){
			order = null;
		}
		return order;
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
		endTimes=this.getDayAfter(endTimes);
		
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<Order> list = mapper.allList(Integer.parseInt(status), order_no, sf.parse(startTimes), sf.parse(endTimes));		
		if(list.size()>0){
			for(Order order : list){
				double realPrice = (double)order.getOrderPrice()/100; //价格“分”转化成“元”
				order.setRealPrice(realPrice);
			}
		}
		
		PageBean<Order> page = new PageBean<>(list);
		return page;
	}
	
	/**
	 * 后台结算模块订单统计
	 */
	@Override
	public Map<Integer, Object> OrderAccount(int adminId) throws Exception {
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		StringBuffer buffer = new StringBuffer();
		
		int count = mapper.OrderAccount(admin.getShopId(), Contants.IS_WAIT_PAY); //1待付
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
	public Map<Integer, Object> OrderWaitAccount(int adminId) throws Exception {
		List<Object> list = new ArrayList<>();
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		int countOne = mapper.OrderAccount(admin.getShopId(), Contants.IS_WAIT_SEND); //2待发货
		//int countTwo = mapper.notComplish(admin.getShopId()); //3,4,5 未完成
		int countTwo = mapper.OrderAccount(admin.getShopId(), Contants.IS_SEND); //3已发货
		int countThree= mapper.OrderAccount(admin.getShopId(), Contants.IS_CANCEL); //6已取消
		int countFour = mapper.OrderAccount(admin.getShopId(), Contants.IS_COMPLISH); //7已完成
		
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
	
	/**
	 * 后台退款模块订单统计
	 */
	@Override
	public Map<Integer, Object> OrderRefundAccount(int adminId) throws Exception {
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		StringBuffer buffer = new StringBuffer();
		
		int count = mapper.OrderRefundAccount(admin.getShopId()); //退款单
		buffer.append(count+",");
		buffer.append(Contants.IS_REFUND); //2待发货
		
		String[] str = buffer.toString().split(",");
		
		HashMap<Integer, Object> map = new HashMap<>();
		map.put(1, str);
		return map;
	}
	
	/**
	 * 后台交易金额统计
	 */
	@Override
	public Map<String, String> OrderMoneyAccount(int adminId) throws Exception {
		double total = 0.00;
		double alreadyTotal = 0.00;
		double waitTotal = 0.00;
		double refundTotal = 0.00;
		HashMap<String, String> map = new HashMap<>();
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		
		/*List<OrderShop> list = orderShopMapper.getByShopId(admin.getShopId(), null); //所有订单
		if(list.size()>0){
			for(OrderShop orderShop : list){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(int i=0; i<skuList.size(); i++){
					double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					total += realSellPrice * skuList.get(i).getSkuNum();
				}
			}
		}*/
		
		List<OrderShop> alreadyList = orderShopMapper.getAlreadyByShopId(admin.getShopId()); //已付
		if(alreadyList.size()>0){
			for(OrderShop orderShop : alreadyList){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(int i=0; i<skuList.size(); i++){
					double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					alreadyTotal += realSellPrice * skuList.get(i).getSkuNum();
				}
			}
		}
		
		List<OrderShop> waitList = orderShopMapper.getByShopId(admin.getShopId(), Contants.IS_WAIT_PAY); //待付
		if(waitList.size()>0){
			for(OrderShop orderShop : waitList){
				List<OrderSku> skuList = SkuMapper.getByOrderShopId(orderShop.getId());
				for(int i=0; i<skuList.size(); i++){
					double realSellPrice = (double)skuList.get(i).getSkuSellPriceReal()/100;//支付价格“分”转化成“元”
					waitTotal += realSellPrice * skuList.get(i).getSkuNum();
				}
			}
		}
		
		List<OrderRefundDoc> refundList = refundMapper.getByShopId(admin.getShopId(), Contants.REFUN_STATUS); //退款
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
	
	public String[] backMap(int shopId, int status){
		int count = mapper.OrderAccount(shopId, status);
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
	public Map<String, Object> OrderNumAccount(int adminId) throws Exception {
		MemberShopAdmin admin = adminMapper.selectByPrimaryKey(adminId);
		HashMap<String, Object> map = new LinkedHashMap<>();
		
		int count = mapper.OrderCountAll(admin.getShopId());
		int countSix = mapper.OrderRefundAccount(admin.getShopId()); //退款单
		
		StringBuffer buffer= new StringBuffer();
		buffer.append(count+",");
		buffer.append(Contants.IS_ALL);
		String[] str = buffer.toString().split(","); //全部
		
		StringBuffer bufferOne= new StringBuffer();
		bufferOne.append(countSix+",");
		bufferOne.append(Contants.IS_REFUND);
		String[] strSix = bufferOne.toString().split(","); //退款
		
		String[] strZero = backMap(admin.getShopId(), Contants.IS_WAIT_PAY);//1待付
		String[] strOne = backMap(admin.getShopId(), Contants.IS_WAIT_SEND);//2待发货
		String[] strTwo = backMap(admin.getShopId(), Contants.IS_SEND);//3已发货
		String[] strThree = backMap(admin.getShopId(), Contants.IS_WAIT_EVALUATE);//5已收货
		String[] strFour = backMap(admin.getShopId(), Contants.IS_CANCEL); //6已取消
		String[] strFive = backMap(admin.getShopId(), Contants.IS_COMPLISH); //7已评价
		String[] strSeven = backMap(admin.getShopId(), Contants.IS_DELETE); //8已删除
		
		map.put("所有订单", str);	
		map.put("待付款", strZero);
		map.put("待发货", strOne);
		map.put("已发货", strTwo);
		map.put("已收货", strThree);
		map.put("已取消", strFour);
		map.put("已评价", strFive);
		map.put("退款", strSix);
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

	

}
