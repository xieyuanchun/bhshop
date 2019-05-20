package com.order.shop.service.impl;
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

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopAdminMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.CoordinatesUtil;
import com.bh.utils.GetLatitude;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.github.pagehelper.PageHelper;
import com.ibm.icu.text.DecimalFormat;
import com.order.enums.SendCancelReasonEnum;
import com.order.enums.SendOrderTypeEnum;
import com.order.enums.VehicleEnum;
import com.order.shop.service.OrderDeliveryService;
import com.wechat.service.WechatTemplateMsgService;


@Service
@Transactional
public class OrderDeliveryImpl implements OrderDeliveryService{
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
	private MemberMapper memberMapper; // 用户
	@Autowired
	private MemberSendMapper sendMapper; // 配送员
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; // 商品属性
	@Autowired
	private MemberUserAddressMapper addressMapper; // 用户
	@Autowired
	private OrderSendInfoMapper orderSendMapper; //配送单
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	/**
	 * 配送端可接单列表
	 */
	@Override
	public PageBean<OrderShop> deliveryOrder(String currentPage, String pageSize, String lat, String lng, int mId) throws Exception {
		List<OrderShop> list = orderShopMapper.getOrderByDstate(Contants.CATE_ONE);// 1订单处于已抛单状态
		MemberSend memberSend = sendMapper.selectByPrimaryKey(mId);
		
		List<OrderShop> otherList = orderShopMapper.getOrderByDstate(Contants.CATE_ONE);
		otherList.clear(); //清空列表
		
		if(memberSend!=null){
			int speed = getSpeed(memberSend.getTool());//送货速度
			otherList = getRangeAndPriceTypeOrder(memberSend.getType(), speed, mId, lat, lng, list, otherList);
		}
		
		PageParams<OrderShop> params = new PageParams<>();
		PageBean<OrderShop> pageBean = new PageBean<>();
	    params.setCurPage(Integer.parseInt(currentPage));
	    params.setPageSize(Integer.parseInt(pageSize));
	    params.setResult(otherList);
	    PageBean<OrderShop> retPage = pageBean.getPageResult(params);
		return retPage;
	}
	
	/**
	 * 配送费高低距离远近类型可接单
	 * @param fz
	 * @param speed
	 * @param mId
	 * @param lat
	 * @param lng
	 * @param list
	 * @param otherList
	 * @return
	 */
	public List<OrderShop> getRangeAndPriceTypeOrder(int fz, int speed, int mId, String lat, String lng, List<OrderShop> list, List<OrderShop> otherList){
		MemberShop memberShop = null;
		Double distance = 0.00;
		Double shoptoUserDistance = 0.00;
		int deliveryTime = 0;
		String uAddress = null;
		StringBuffer buffer = new StringBuffer();
		for(OrderShop orderShop: list){
			memberShop = shopMapper.selectByPrimaryKey(orderShop.getShopId());
			if(memberShop!=null){
				MemberSend memberSend = sendMapper.selectByPrimaryKey(mId); //计算该单是否在配送范围内
				if(memberSend != null){
					distance = CoordinatesUtil.getDistanceOfMeter(memberShop.getLat(), memberShop.getLon(), Double.parseDouble(lat), Double.parseDouble(lng));
					if(Integer.parseInt(memberSend.getScope())>distance){//插入在配送范围内的订单
						buffer.append(orderShop.getId()+",");
					}
				}
			}
		}
		if(buffer.toString().length()>0){
			String str = buffer.toString().substring(0,buffer.toString().length()-1);
			List<String> idList = Arrays.asList(str.split(","));
			
			otherList = orderShopMapper.getDeliveryPriceTypeOrder(idList, fz, lng, lat);//范围订单-按类型排序
			for(OrderShop orderShop : otherList){
				memberShop = shopMapper.selectByPrimaryKey(orderShop.getShopId());
				distance = CoordinatesUtil.getDistanceOfMeter(memberShop.getLat(), memberShop.getLon(), Double.parseDouble(lat), Double.parseDouble(lng));
				
				double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送价格“分”转化成“元”
				orderShop.setRealDeliveryPrice(realDeliveryPrice); //配送费单位元
				orderShop.setShopName(memberShop.getShopName()); //店铺名称
				orderShop.setShopAddress(memberShop.getAddress()); //商家地址
				
				orderShop.setShopDistance(Math.floor((distance/1000)*10)/10); //配送员到商家距离单位km
				
				Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
				MemberUserAddress userAddress = addressMapper.selectByPrimaryKey(order.getmAddrId()); //获取订单用户地址
				orderShop.setAddress(userAddress.getAddress());//用户地址
				if(userAddress!=null){
					uAddress = userAddress.getProvname()+userAddress.getAreaname()+userAddress.getCityname()+userAddress.getAreaname()+userAddress.getAddress();
				}
				Map<String,String> map = GetLatitude.getlnglat(uAddress);//根据用户地址获取经纬度
				shoptoUserDistance = CoordinatesUtil.getDistanceOfMeter(memberShop.getLat(), memberShop.getLon(), Double.parseDouble(map.get("lat")), Double.parseDouble(map.get("lng")));
				orderShop.setShoptoUserDistance(Math.floor((shoptoUserDistance/1000)*10)/10); //商家到用户距离单位km
				
				deliveryTime = (int) Math.round(((Math.floor((distance/1000)*10)/10 + Math.floor((shoptoUserDistance/1000)*10)/10) * speed));
				if(deliveryTime<30){
					deliveryTime = Contants.MIN_TIME;
				}
				orderShop.setDeliveryTime(deliveryTime); //配送时间单位 ‘分钟’
			}
		}
		return otherList;
	}
	
	/**
	 * 根据交通方式获取配送速度
	 * @param vehicle
	 * @return
	 */
	public int getSpeed(int vehicle){
		int speed = 0; //送货速度
		if(vehicle==2){//三轮车
			speed = Contants.TRICYCLE_SPEED;
		}else if(vehicle==3){//电动车
			speed = Contants.ELECTROMBILE_SPEED;
		}else if(vehicle==4){//大踏板电动车
			speed = Contants.BIG_ELECTROMBILE_SPEED;
		}else if(vehicle==5){//摩托车
			speed = Contants.MOTORBIKE_SPEED;
		}else if(vehicle==6){//小汽车
			speed = Contants.SEDAN_CAR_SPEED;
		}else if(vehicle==7){//小货车
			speed = Contants.TRUCKS_SPEED;
		}else if(vehicle==8){//步行
			speed = Contants.WALK_SPEED;
		}else if(vehicle==9){//驾车
			speed = Contants.DRIVE_SPEED;
		}else{
			speed = Contants.BIKE_SPEED;//单车
		}
		return speed;
	}
	
	/**
	 * 配送员接单
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int receiveOrder(String id, int sId) throws Exception {
		int row = 0;
		OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		if(orderShop.getStatus()==2 && orderShop.getdState()==1){
			MemberSend send = sendMapper.selectByPrimaryKey(sId);
			if(send!=null){
				orderShop.setdState(Contants.CATE_TWO);; //2-待发货
				orderShopMapper.updateByPrimaryKeySelective(orderShop);
				
				OrderSendInfo sendInfo = new OrderSendInfo();
				sendInfo.setOrderShopId(Integer.parseInt(id)); //商家订单id
				sendInfo.setmId(orderShop.getmId()); //用户id
				sendInfo.setsId(sId); //配送员id
				sendInfo.setOcTime(new Date());
				sendInfo.setTool(send.getTool());//配送工具
				sendInfo.setsName(send.getFullName()); //配送员名称
				sendInfo.setdState(Contants.DELIVER_WAIT_SEND); //0(初始状态)待发货
				row = orderSendMapper.insertSelective(sendInfo);
			}else{
				row = 300;
			}
		}else{
			row = 600;
		}
		return row;
	}
	
	/**
	 * 确认发货
	 * @param id
	 * @param sId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int sendGoods(String id, int sId) throws Exception {
		int row = 0;
		MemberSend send = sendMapper.selectByPrimaryKey(sId);
		if(send!=null){
			OrderSendInfo sendInfo = orderSendMapper.selectByPrimaryKey(Integer.parseInt(id));
			sendInfo.setdState(Contants.DELIVER_SEND); //1-发货中
			sendInfo.setDeliverTime(new Date()); //发货时间
			row = orderSendMapper.updateByPrimaryKeySelective(sendInfo);
			
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
			orderShop.setdState(Contants.CATE_THREE); //3-配送中
			orderShop.setStatus(Contants.IS_SEND); //已发货
			orderShop.setSendTime(new Date()); //发货时间
			orderShopMapper.updateByPrimaryKeySelective(orderShop);
			WXMSgTemplate template = new WXMSgTemplate();
			template.setOrderShopId(orderShop.getId()+"");
			wechatTemplateMsgService.sendGroupGoodTemplate(template);
		}else{
			row = 300;
		}
		return row;
	}
	
	/**
	 * 配送任务列表
	 */
	@Override
	public PageBean<OrderSendInfo> orderTask(String currentPage, String pageSize, String status, int mId, String lat, String lng) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<OrderSendInfo> list = orderSendMapper.selectBySidAndStatus(mId, status);
		if(list.size()>0){
			for(OrderSendInfo sendInfo : list){
				OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
				MemberShop memberShop = shopMapper.selectByPrimaryKey(orderShop.getShopId());
				
				if(sendInfo.getdState()==0){//配送状态  0(初始状态)待取货   1配送中   2已配送 3已取消',
					sendInfo.setDeliveryStatus(Contants.WAIT_SEND);
				}
				if(sendInfo.getdState()==1){
					sendInfo.setDeliveryStatus(Contants.SEND);
				}
				if(sendInfo.getdState()==2){
					sendInfo.setDeliveryStatus(Contants.ALREADY_SEND);
				}
				if(sendInfo.getdState()==3){
					sendInfo.setDeliveryStatus(Contants.CANCEL);
				}
				
				double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送价格“分”转化成“元”
				sendInfo.setRealDeliveryPrice(realDeliveryPrice); //配送费单位元
				sendInfo.setShopName(memberShop.getShopName()); //店铺名称
				sendInfo.setShopAddress(memberShop.getAddress()); //商家地址
				
				Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
				MemberUserAddress userAddress = addressMapper.selectByPrimaryKey(order.getmAddrId()); //获取订单用户地址
				sendInfo.setUserAddress(userAddress.getAddress());//用户地址
			}
		}
		PageBean<OrderSendInfo> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 取消配送
	 */
	@Override
	public int cancelOrder(String id, String reason, int sId) throws Exception {
		int row = 0;
		MemberSend send = sendMapper.selectByPrimaryKey(sId);
		if(send!=null){
			OrderSendInfo sendInfo = orderSendMapper.selectByPrimaryKey(Integer.parseInt(id));
			sendInfo.setdState(Contants.DELIVER_CANCEL); //3-已取消
			
			if(Integer.parseInt(reason)==1){
				sendInfo.setCancelReason("距离远无法送达");
			}
			if(Integer.parseInt(reason)==2){
				sendInfo.setCancelReason("工作繁忙来不及送");
			}
			if(Integer.parseInt(reason)==3){
				sendInfo.setCancelReason("其他原因");
			}
			row = orderSendMapper.updateByPrimaryKeySelective(sendInfo);
			
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
			orderShop.setdState(Contants.CATE_ZERO); //0-待抛单
			orderShop.setStatus(Contants.CATE_TWO);; //2-待发货
			orderShopMapper.updateByPrimaryKeySelective(orderShop);
		}else{
			row = 300;
		}
		return row;
	}
	
	/**
	 * 加载取消原因列表
	 */
	@Override
	public List<Map<String, Object>> getReasonList() throws Exception {
		List<Map<String, Object>> list = SendCancelReasonEnum.getReasonList();
		return list;
	}
	
	/**
	 * 交通工具列表
	 */
	@Override
	public List<Map<String, Object>> getVehicleList(int sId) throws Exception {
		List<Map<String, Object>> list = VehicleEnum.getVehicleList();
		MemberSend memberSend = sendMapper.selectByPrimaryKey(sId);
		if(memberSend!=null){
			if(memberSend.getTool()!=null){
				for(Map<String, Object> map : list){
					if(map.get("code")==memberSend.getTool()){
						map.put("isDefult", 1);
					}else{
						map.put("isDefult", 0);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 订单类型列表
	 */
	@Override
	public List<Map<String, Object>> sendOrderType(int sId) throws Exception {
		List<Map<String, Object>> list = SendOrderTypeEnum.getTypeList();
		MemberSend memberSend = sendMapper.selectByPrimaryKey(sId);
		if(memberSend!=null){
			if(memberSend.getType()!=null){
				for(Map<String, Object> map : list){
					if(map.get("code")==memberSend.getType()){
						map.put("isDefult", 1);
					}else{
						map.put("isDefult", 0);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 交通工具的保存
	 */
	@Override
	public int saveVehicle(String code, int sId) throws Exception {
		int row = 0;
		MemberSend memberSend = sendMapper.selectByPrimaryKey(sId);
		if(memberSend!=null){
			memberSend.setTool(Integer.parseInt(code));
			row = sendMapper.updateByPrimaryKeySelective(memberSend);
		}
		return row;
	}
	
	/**
	 * 设置订单类型的保存
	 */
	@Override
	public int saveType(String code, int sId) throws Exception {
		int row = 0;
		MemberSend memberSend = sendMapper.selectByPrimaryKey(sId);
		if(memberSend!=null){
			memberSend.setType(Integer.parseInt(code));
			row = sendMapper.updateByPrimaryKeySelective(memberSend);
		}
		return row;
	}
	
	/**
	 * 订单已送达
	 */
	@Override
	public int orderAreadlySend(String id, int sId) throws Exception {
		int row = 0;
		MemberSend memberSend = sendMapper.selectByPrimaryKey(sId);
		if(memberSend!=null){
			OrderSendInfo sendInfo = orderSendMapper.selectByPrimaryKey(Integer.parseInt(id));
			sendInfo.setdState(Contants.DELIVER_ALREADY_SEND); //2-已送达
			sendInfo.setSendTime(new Date()); //送达时间
			row = orderSendMapper.updateByPrimaryKeySelective(sendInfo);
			
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
			//cheng
			orderShop.setReceivedtime(new Date());
			orderShop.setdState(Contants.CATE_FOUR); //4-已送达
			orderShopMapper.updateByPrimaryKeySelective(orderShop);
		}else{
			row = 300;
		}
		return row;
	}
	
	/**
	 * 接单数量和总金额统计
	 */
	@Override
	public Map<String, Object> sendAccount(int sId) throws Exception {
		HashMap<String, Object> map = new LinkedHashMap<>();
		MemberSend send = sendMapper.selectByPrimaryKey(sId);
		if(send!=null){
			double total = (double)send.getTotalIncome()/100;
			total = Math.floor(total*10)/10;
			
			int count = send.getTotalNum();
			map.put("num", count);
			map.put("price", total);
		}else{
			map.put("num", 0);
			map.put("price", 0);
		}

		return map;
	}
	/*public Map<String, Object> sendAccount(int sId) throws Exception {
		
		HashMap<String, Object> map = new LinkedHashMap<>();
		double total = 0.00;
		int count = 0;
			List<OrderShop> alreadyList = orderShopMapper.sendAccount(sId, Contants.DELIVER_ALREADY_SEND); //已完成
			if(alreadyList.size()>0){
				for(OrderShop orderShop : alreadyList){
					double DeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送费总价“分”转化成“元”
					total += DeliveryPrice;
				}
			}
		count = alreadyList.size();
		
		total = Math.floor(total*10)/10;
		map.put("num", count);
		map.put("price", total);

		return map;
	}*/
	
	/**
	 * 配送单详情
	 */
	@Override
	public OrderShop deliveryOrderDetails(String id, String lat, String lng, int mId, String fz) throws Exception {
		MemberShop memberShop = null;
		Double distance = 0.00;
		Double shoptoUserDistance = 0.00;
		int deliveryTime = 0;
		String uAddress = null;
		
		OrderShop shopOrder = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
		MemberSend memberSend = sendMapper.selectByPrimaryKey(mId);
		//int speed = getSpeed(memberSend.getTool());//送货速度
		int speed = getSpeed(Integer.parseInt(fz)); //送货速度
		
		List<String> idList = Arrays.asList(id.toString().split(","));
		
		List<OrderShop> otherList = orderShopMapper.getDeliveryPriceTypeOrder(idList, memberSend.getType(), lng, lat);//范围订单-按类型排序
		for(OrderShop orderShop : otherList){
			memberShop = shopMapper.selectByPrimaryKey(orderShop.getShopId());
			distance = CoordinatesUtil.getDistanceOfMeter(memberShop.getLat(), memberShop.getLon(), Double.parseDouble(lat), Double.parseDouble(lng));
			
			double realDeliveryPrice = (double)orderShop.getDeliveryPrice()/100;//配送价格“分”转化成“元”
			shopOrder.setRealDeliveryPrice(realDeliveryPrice); //配送费单位元
			shopOrder.setShopName(memberShop.getShopName()); //店铺名称
			shopOrder.setShopAddress(memberShop.getAddress()); //商家地址
			shopOrder.setShopLng(memberShop.getLon()); //商家经纬度
			shopOrder.setShopLat(memberShop.getLat());
			
			shopOrder.setShopDistance(Math.floor((distance/1000)*10)/10); //配送员到商家距离单位km
			
			Order order = mapper.selectByPrimaryKey(orderShop.getOrderId());
			MemberUserAddress userAddress = addressMapper.selectByPrimaryKey(order.getmAddrId()); //获取订单用户地址
			shopOrder.setAddress(userAddress.getAddress());//用户地址
			if(userAddress!=null){
				uAddress = userAddress.getProvname()+userAddress.getAreaname()+userAddress.getCityname()+userAddress.getAreaname()+userAddress.getAddress();
			}
			Map<String,String> map = GetLatitude.getlnglat(uAddress);//根据用户地址获取经纬度
			shoptoUserDistance = CoordinatesUtil.getDistanceOfMeter(memberShop.getLat(), memberShop.getLon(), Double.parseDouble(map.get("lat")), Double.parseDouble(map.get("lng")));
			shopOrder.setShoptoUserDistance(Math.floor((shoptoUserDistance/1000)*10)/10); //商家到用户距离单位km
			shopOrder.setUserLng(Double.parseDouble(map.get("lng"))); //订单地址经纬度
			shopOrder.setUserLat(Double.parseDouble(map.get("lat")));
			
			deliveryTime = (int) ((Math.floor((distance/1000)*10)/10 + Math.floor((shoptoUserDistance/1000)*10)/10) * speed);
			shopOrder.setDeliveryTime(deliveryTime); //配送时间单位 ‘分钟’
			
			
		}
		return shopOrder;
	}
	
	/**
	 * 判断是否配送员
	 */
	@Override
	public boolean isDeliveryman(int mId) throws Exception {
		boolean flag = false;
		MemberSend send = sendMapper.selectByPrimaryKey(mId);
		if(send!=null){
			//cheng  判断send不为null的同时还要判断status=1(0待审核，1审核成功，2审核失败)
			if (send.getStatus().equals(1)) {
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 接单排行
	 */
	@Override
	public List<Member> orderArrange(int mId) throws Exception {
		List<Member> list = memberMapper.orderArrange();
		for(int i=0; i<list.size(); i++){
			int orderNum = orderSendMapper.countBySid(list.get(i).getId());
			list.get(i).setOrderNum(orderNum); //接单数量
			
			String top = "No."+(i+1);
			list.get(i).setTop(top);
			
		}
		return list;
	}
	
	/**
	 * 我的钱包
	 */
	@Override
	public Map<String, Object> mySendWallet(int sId) throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		MemberSend send = sendMapper.selectByPrimaryKey(sId);
		if(send!=null){
			double realBalance = (double)send.getBalance()/100;
			double realCashPledge = (double)send.getCashPledge()/100;
			
			DecimalFormat df = new DecimalFormat("0.00"); 
			map.put("balance", df.format(realBalance));
			map.put("cashPledge", df.format(realCashPledge));
		}
		return map;
	}
	
	/**
	 * 收工
	 */
	@Override
	public int knockOff(int sId, String fz) throws Exception {
		int row = 0;
		MemberSend send = sendMapper.selectByPrimaryKey(sId);
		if(send!=null){
			if(Integer.parseInt(fz)==1){
				send.setOnline(Contants.ONLINE_open); //开工
			}
			if(Integer.parseInt(fz)==2){
				send.setOnline(Contants.ONLINE_OFF); //收工
			}
			row = sendMapper.updateByPrimaryKeySelective(send);
		}
		return row;
	}
	
	/**
	 * 评价记录
	 */
	@Override
	public Map<String, Object> evaluateRecord(String fz, int sId) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat    df   = new DecimalFormat("0.0"); 
		Calendar cal = Calendar.getInstance();
		int countAll = 0;
		int countGood = 0;
		int countMedium = 0;
		int countBad = 0;
		String goodRate = null;
		String badRate = null;
		
		List<Integer> goodStr = new ArrayList<>(); //好评星级数
		List<Integer> mediumStr = new ArrayList<>(); //中评星级数
		List<Integer> badStr = new ArrayList<>(); //差评星级数
		if(Integer.parseInt(fz)==1){//本月
			countAll = orderSendMapper.countByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), null);
			countGood = orderSendMapper.countByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 1);
			countMedium = orderSendMapper.countByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 2);
			countBad = orderSendMapper.countByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 3);
			
			goodRate = df.format((double)countGood/countAll * 100); //五星好评率
			
			badRate = df.format((double)countBad/countAll * 100); //一星差评率
			
			List<OrderSendInfo> goodList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 1);
			for(OrderSendInfo sendInfo : goodList){
				goodStr.add(sendInfo.getsServiceLevel());
			}
			List<OrderSendInfo> mediumList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 2);
			for(OrderSendInfo sendInfo : mediumList){
				mediumStr.add(sendInfo.getsServiceLevel());
			}
			List<OrderSendInfo> badList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(Calendar.getInstance().getTime()), 3);
			for(OrderSendInfo sendInfo : badList){
				badStr.add(sendInfo.getsServiceLevel());
			}
 
		}
		if(Integer.parseInt(fz)==2){//上个月
			cal.add(Calendar.MONTH, -1);
			countAll = orderSendMapper.countByTimeSidAndLevel(sId, format.format(cal.getTime()), null);
			countGood = orderSendMapper.countByTimeSidAndLevel(sId, format.format(cal.getTime()), 1);
			countMedium = orderSendMapper.countByTimeSidAndLevel(sId, format.format(cal.getTime()), 2);
			countBad = orderSendMapper.countByTimeSidAndLevel(sId, format.format(cal.getTime()), 3);
			
			goodRate = df.format((double)countGood/countAll * 100);//五星好评率
			
			badRate = df.format((double)countBad/countAll * 100); //一星差评率
			
			List<OrderSendInfo> goodList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(cal.getTime()), 1);
			for(OrderSendInfo sendInfo : goodList){
				goodStr.add(sendInfo.getsServiceLevel());
			}
			List<OrderSendInfo> mediumList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(cal.getTime()), 2);
			for(OrderSendInfo sendInfo : mediumList){
				mediumStr.add(sendInfo.getsServiceLevel());
			}
			List<OrderSendInfo> badList = orderSendMapper.selectByTimeSidAndLevel(sId, format.format(cal.getTime()), 3);
			for(OrderSendInfo sendInfo : badList){
				badStr.add(sendInfo.getsServiceLevel());
			}
		}
		
		
		
		Map<String, Object> badMap = new LinkedHashMap<>();
		badMap.put("count", countBad);
		badMap.put("starNum", badStr);
		
		Map<String, Object> mediumMap = new LinkedHashMap<>();
		mediumMap.put("count", countMedium);
		mediumMap.put("starNum", mediumStr);
		
		Map<String, Object> goodsMap = new LinkedHashMap<>();
		goodsMap.put("count", countGood);
		goodsMap.put("starNum", goodStr);
		
		Map<String, Object> mapLevel= new LinkedHashMap<>();
		mapLevel.put("goodsEvaluate", goodsMap);
		mapLevel.put("mediumEvaluate", mediumMap);
		mapLevel.put("badEvaluate", badMap);
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("totalNum", countAll);
		map.put("goodsRate", goodRate);
		map.put("badRate", badRate);
		map.put("EvaluateLevel", mapLevel);
		
		return map;
	}

}
