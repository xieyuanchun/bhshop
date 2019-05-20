package com.order.user.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.JdOrderTrackMapper;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.JdOrderTrack;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.OtherStock;
import com.bh.jd.bean.order.OtherTrack;
import com.bh.jd.bean.order.Track;
import com.bh.order.mapper.BhDictItemMapper;
import com.bh.order.mapper.JdOrderMainMapper;
import com.bh.order.mapper.JdOrderSkuMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.BhDictItem;
import com.bh.order.pojo.JdOrderMain;
import com.bh.order.pojo.JdOrderSku;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.queryLogistics.QueryLogistics;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.order.user.service.JDOrderService;
import com.order.user.service.SimpleOrderService;
import com.print.controller.HttpUtils;

@Controller
@RequestMapping("/jdorder")
public class JDOrderController {
	private static final Logger logger = LoggerFactory.getLogger(JDOrderController.class);
	@Value("${USERINFO}")
	private String USERINFO;

	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private JdOrderTrackMapper jdOrderTrackMapper;
	@Autowired
	private SimpleOrderService simpleOrderService;
	@Autowired
	private  JdOrderMainMapper jdOrderMainMapper;
	@Autowired
	private  JdOrderSkuMapper jdOrderSkuMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private BhDictItemMapper bhDictItemMapper;
	
	
	/**
	 * @Description: 查询京东物流
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/jdOrderTrack")
	@ResponseBody
	public BhResult orderTrack(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			JdResult<OrderStock> ret= jdOrderService.jdOrderTrack(map.get("jdOrderId"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(ret);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	
	/**
	 * @Description: 查询京东物流
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/jdOrderInsert")
	@ResponseBody
	public BhResult jdOrderInsert(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			jdOrderService.jdOrderInsert();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * CHENG-201712-25 通过购物车的id去查询该商品是否是属于京东的商品还是滨惠的商品
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping(value = "/getNewStockById", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getNewStockById(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(USERINFO);
			String cartIds = map.get("cartIds");
			String area = map.get("area");
			String isFromCart = map.get("isFromCart");
			if (StringUtils.isEmpty(cartIds)) {
				return bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "购物车的id不能为空");
			}
			if (StringUtils.isEmpty(isFromCart)) {
				return bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "参数不能为空");
			}
			if (StringUtils.isEmpty(area)) {
				return bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "配送区域的ID不能为空");
			} else {
				if (isFromCart.equals("0")) {
					bhResult=simpleOrderService.getGoodsByCartId(cartIds);
				}else if (isFromCart.equals("1")) {
					Order order=simpleOrderService.selectOrderById(Integer.parseInt(cartIds));
					bhResult=simpleOrderService.getGoodsByCartId(order.getCartId());
				}
				if (bhResult.getStatus()==200) {
					// 判断库存
					bhResult = jdOrderService.returnStock(isFromCart, cartIds, area, member);
					if (bhResult.getStatus() == 400) {
						return bhResult;
					}

					// 全部有货
					if (bhResult.getStatus() == 200) {
						bhResult = jdOrderService.memberBuyGoodsIsLimit(cartIds, isFromCart, member);
						// bhResult
						// 的取值范围:如果status==200则全部有货，如果status==400,则提示该商品限购x个
						if (bhResult.getStatus() == 400) {
							return bhResult;
						}
					}
					// 判断商品区域购买限制查询(京东商品)
					if (bhResult.getStatus() == 200) {
						List<String> c = new ArrayList<>();
						if (isFromCart.equals("0")) {
							c = JsonUtils.stringToList(cartIds);
						} else if (isFromCart.equals("1")) {
							Order order = new Order();
							order.setmId(member.getId());
							order.setId(Integer.parseInt(cartIds));
							Order order1 = jdOrderService.selectOrderByIds(order);
							c = JsonUtils.stringToList(order1.getCartId());
						}
						bhResult = jdOrderService.getAreaLimit(area, member, c);
					}
					// 判断商品区域购买限制查询(非京东商品)
					if (bhResult.getStatus() == 200) {
						List<String> c = new ArrayList<>();
						if (isFromCart.equals("0")) {
							c = JsonUtils.stringToList(cartIds);
						} else if (isFromCart.equals("1")) {
							Order order = new Order();
							order.setmId(member.getId());
							order.setId(Integer.parseInt(cartIds));
							Order order1 = jdOrderService.selectOrderByIds(order);
							c = JsonUtils.stringToList(order1.getCartId());
						}
						bhResult = jdOrderService.getAreaLimitByBH(area, member, c);
					}
					
					
					
					//全部有货
					if (bhResult.getStatus() == 200) {
						bhResult = jdOrderService.memberBuyGoodsIsLimit(cartIds,isFromCart,member);
						/*if (row == 1) {
							bhResult = new BhResult(400, "您购买该商品的数量已达上限,请选择其他的商品。", null);
						}*/
					}
				}else{
					
					return bhResult = new BhResult(400, "商品的数量必须大于或者等于1", null);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getNewStockById#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	/**
	 * CHENG-201712-25 移动端 订单详情 ，查询京东配送信息接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping(value = "/orderTrack", method = RequestMethod.POST)
	@ResponseBody
	public BhResult orderTrack(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String id = map.get("id"); // order_shop 的id
			String jdSkuNo = map.get("jdSkuNo"); //每个商品的jdSkuNo(用于京东订单拆分的时候用)
			if (StringUtils.isEmpty(id)) {
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "参数不能为空");
			} else {

				OrderStock orderStock = new OrderStock();
				/* 获取订单详情 */
				// 根据主键获取oderShop表的信息
				OrderShop orderShop1 = jdOrderService.getOrderById(id);
				// 根据订单号获取order_main
				Order order = jdOrderService.getOrderMainById(orderShop1.getOrderNo());
				// 根据order_main 表的m_addr_id获取 member_user_address表信息
				MemberUserAddress mua = jdOrderService.getUserById(order.getmAddrId());
				orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
				orderStock.setAddress(mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getAddress());// 收货地址
				// 图片地址
				List<OrderSku> listOrderSku = jdOrderService.getOrderSkuByOrderId(orderShop1.getOrderId());
				List<OrderSku> listSku=orderSkuMapper.getByOrderShopId(Integer.valueOf(id));
				if (orderShop1.getJdorderid().equals("0")|| orderShop1.getJdorderid().equals("-1")) {//京东订单
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < listSku.size(); i++) {
						GoodsSku skuList = goodsSkuMapper.selectByPrimaryKey(listSku.get(i).getSkuId());
						JSONObject jsonObj = JSONObject.fromObject(skuList.getValue());
						JSONArray personList = jsonObj.getJSONArray("url");
						String url = (String) personList.get(0);
						if(i!=0){
							sb.append(","+url);
						}else{
							sb.append(url);
						}
					}
					orderStock.setImgeUrl(sb.toString());
				}
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
					return bhResult = new BhResult(200, "获取信息成功，商家自配！", orderStock);

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
						return bhResult = new BhResult(200, "待接单！", orderStock);
					}
					if (orderSendInfo2.getdState() == 0) {

						track.setMsgTime("");// 时间
						track.setOperator(orderSendInfo2.getsName());// 配送员
						track.setContent("待发货");// 内容
						listTrack2.add(track);
						orderStock.setOrderTrack(listTrack2);
						orderStock.setJd("0"); // 自营物流
						bhResult = new BhResult(200, "待发货！", orderStock);
						return bhResult;
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
						bhResult = new BhResult(200, "发货中！", orderStock);
						return bhResult;
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
						bhResult = new BhResult(200, "已送达！", orderStock);
						return bhResult;
					} else if (orderSendInfo2.getdState() == 3) {

						track.setMsgTime("");// 时间
						track.setOperator(orderSendInfo2.getsName());// 配送员
						track.setContent("已取消");// 内容
						listTrack2.add(track);
						orderStock.setOrderTrack(listTrack2);
						orderStock.setJd("0"); // 自营物流
						bhResult = new BhResult(200, "已取消！", orderStock);
						return bhResult;
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
						bhResult = new BhResult(200, "已结算！", orderStock);
						return bhResult;
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
					return bhResult = new BhResult(200, "当前为其他物流信息！", orderStock);
				} else if (orderShop1.getDeliveryWay() == 2) {
					// 京东物流
					OrderShop orderShop = new OrderShop();
					orderShop.setId(Integer.parseInt(id));
					// 查询京东物流信息，获取当前订单妥投信息，如果为妥投，就更改订单状态
					orderStock = jdOrderService.orderTrack(orderShop,Long.parseLong(jdSkuNo));
					orderStock.setOrderNo(orderShop1.getOrderNo()); // 订单号
					orderStock.setAddress(mua.getProvname() + mua.getCityname() + mua.getAreaname() + mua.getAddress());// 收货地址
					
					JdOrderMain jdOrderMain=jdOrderMainMapper.getJdOrderMainByJdSkuId(Long.parseLong(jdSkuNo), Integer.parseInt(id));
					List<JdOrderSku> jdOrderSkuList=jdOrderSkuMapper.getByJdMainId(jdOrderMain.getId());
					StringBuffer jdSb=new StringBuffer();
					for (int i = 0; i < jdOrderSkuList.size(); i++) {
						OrderSku orderSku=orderSkuMapper.selectByPrimaryKey(jdOrderSkuList.get(i).getOrderSkuId());
						GoodsSku skuList = goodsSkuMapper.selectByPrimaryKey(orderSku.getSkuId());
						JSONObject jsonObj = JSONObject.fromObject(skuList.getValue());
						JSONArray personList = jsonObj.getJSONArray("url");
						String url = (String) personList.get(0);
						if(i!=0){
							jdSb.append(","+url);
						}else{
							jdSb.append(url);
						}
					}
					orderStock.setImgeUrl(jdSb.toString());
					orderStock.setJd("2"); // 是京东

					if (StringUtils.isEmpty(orderStock.getJdOrderId())) {
						bhResult = new BhResult(200, "目前京东没有配送信息！", orderStock);
						return bhResult;
					}
					// 把京东物流信息保存到jd_order_track表
					List<Track> listTrack = orderStock.getOrderTrack();
					// 判断当前的订单的京东JdOrderId和返回的京东JdOrderId是不是一样的
					if (listTrack.size() > 0 && orderShop1.getJdorderid().equals(orderStock.getJdOrderId())) {

						for (int i = 0; i < listTrack.size(); i++) {

							JdOrderTrack jdOrderTrack = new JdOrderTrack();
							jdOrderTrack.setOrderId(orderShop1.getOrderId());// 订单ID

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							jdOrderTrack.setMsgTime(sdf.parse(listTrack.get(i).getMsgTime()));// 时间
							jdOrderTrack.setContent(listTrack.get(i).getContent()); // 内容
							jdOrderTrack.setOperator(listTrack.get(i).getOperator());// 操作人
							// 根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
							List<JdOrderTrack> jdOrderTrackList = jdOrderTrackMapper.getByOrderId(jdOrderTrack);
							if (jdOrderTrackList != null && jdOrderTrackList.size() > 0) {
								// 有值就不保存
							} else {
								// 没值就保存
								jdOrderTrack.setOrderNo(orderShop1.getOrderNo());
								jdOrderTrackMapper.insertSelective(jdOrderTrack);
							}
						}
					}
					bhResult = new BhResult(BhResultEnum.SUCCESS, orderStock);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######orderTrack#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	public static void main(String parm[]) throws Exception {
		String host = "https://wuliu.market.alicloudapi.com";
		String path = "/kdi";
		String method = "GET";
		String appcode = "232d013ef8244587a9a4f69cb2fcca47";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("no", "544173990761");
		// 获取物流配送信息
		HttpResponse response2 = HttpUtils.doGet(host, path, method, headers, querys);
		String Logistics = EntityUtils.toString(response2.getEntity());
		System.out.println("============"+Logistics);
		JSONObject jsonObject2 =null;
		if(StringUtils.isNotBlank(Logistics)) {
			jsonObject2 = JSONObject.fromObject(Logistics);
		}else {
		    jsonObject2 = JSONObject.fromObject("{\"status\":\"201\",\"msg\":\"没有物流信息\",\"result\":\"\"}");
		}
        System.out.println("============"+jsonObject2);
	}
	
}
