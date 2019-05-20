package com.bh.admin.controller.order;

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

import net.sf.json.JSONArray;
//import org.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.JdOrderTrackMapper;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.JdOrderTrack;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.OtherStock;
import com.bh.jd.bean.order.OtherTrack;
import com.bh.jd.bean.order.Track;
import com.bh.admin.mapper.order.OrderSendInfoMapper;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderSendInfo;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.result.BhResult;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.service.JDOrderService;
import com.bh.admin.vo.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Controller
@RequestMapping("/jdorder")
public class JDOrderController {
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
    
	/**
	 * CHENG-201712-25
	 * 通过购物车的id去查询该商品是否是属于京东的商品还是滨惠的商品
	 * @param map()
	 * @return
	 */
	@RequestMapping(value ="/getNewStockById",method=RequestMethod.POST)
	@ResponseBody
	public BhResult getNewStockById(@RequestBody Map<String, String> map,HttpServletRequest request,HttpServletResponse response){
		BhResult bhResult = null;
		try {
			Member member = (Member) request.getSession().getAttribute(USERINFO);
			String cartIds = map.get("cartIds");
			String area = map.get("area");
			String isFromCart = map.get("isFromCart");
			if(StringUtils.isEmpty(cartIds)){
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "购物车的id不能为空");
			} 
			else{
				//isFromCart值为0时是从购物车过来，值为1时是从订单列表过来
				if (isFromCart.equals("0")) {
					bhResult = jdOrderService.getNewStockById(cartIds,area,member);
				}else if (isFromCart.equals("1")) {
					Order order = new Order();
					order.setmId(member.getId());
					order.setId(Integer.parseInt(cartIds));
					Order order1 = jdOrderService.selectOrderByIds(order);
					if (order1 !=null) {
						//如果购物车的字段有数据
						if (order1.getCartId() !=null) {
							bhResult = jdOrderService.getNewStockById(order1.getCartId(),area,member);
						}else{
							bhResult = jdOrderService.getNewStockByOrderId(String.valueOf(order1.getId()));
						}
					}
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}
	
	
	/**
	 * CHENG-201712-25
	 * 移动端   订单详情 ，查询京东配送信息接口
	 * @param map()
	 * @return
	 */
	@RequestMapping(value ="/orderTrack",method=RequestMethod.POST)
	@ResponseBody
	public BhResult orderTrack(@RequestBody Map<String, String> map,HttpServletRequest request,HttpServletResponse response){
		BhResult bhResult = null;
		try {
			Member member = (Member) request.getSession().getAttribute(USERINFO);
			String id = map.get("id"); //order_shop 的id
			
			if(StringUtils.isEmpty(id)){
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "参数不能为空");
			}else{

				OrderStock orderStock  = new OrderStock();
				/* 获取订单详情*/
				//根据主键获取oderShop表的信息
			    OrderShop orderShop1 = jdOrderService.getOrderById(id);
			    //根据订单号获取order_main
			    Order order = jdOrderService.getOrderMainById(orderShop1.getOrderNo());
			    //根据order_main 表的m_addr_id获取 member_user_address表信息
			    MemberUserAddress mua = jdOrderService.getUserById(order.getmAddrId());
			    orderStock.setOrderNo(orderShop1.getOrderNo()); //订单号
			    orderStock.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getAddress());//收货地址
			    //图片地址
			    List<OrderSku> listOrderSku = jdOrderService.getOrderSkuByOrderId(orderShop1.getOrderId());
			    GoodsSku skuList = goodsSkuMapper.selectByPrimaryKey(listOrderSku.get(0).getSkuId());
			    JSONObject jsonObj = JSONObject.fromObject(skuList.getValue()); 
				JSONArray personList = jsonObj.getJSONArray("url");
				String url = (String) personList.get(0);
				orderStock.setImgeUrl(url); 
				
			    //查询当前order_send_info,jd_order_id 为0，不是京东的物流信息，是商家自配的物流
				OrderSendInfo orderSendInfo3 = new OrderSendInfo();
				orderSendInfo3.setOrderShopId(Integer.valueOf(id));
				OrderSendInfo orderSendInfo2 = orderSendInfoMapper.selectByOrderShopId(orderSendInfo3);
				
				Track track = new Track();
				List<Track> listTrack2 = new ArrayList<Track>();
				
				if(orderShop1.getDeliveryWay()==1){
					//商家自配
					if(orderShop1.getdState()==4){   //已送达
					    orderStock.setJd("0");
					    track.setMsgTime("");
				    	track.setOperator("");
					    track.setContent("已送达");//内容
						listTrack2.add(track);
					}else{  //已发货
						orderStock.setJd("0");
						Track track2 = new Track();
					    track2.setMsgTime("");
					    track2.setOperator("");
					    track2.setContent("已发货");
						listTrack2.add(track2);
					}
				
					orderStock.setOrderTrack(listTrack2);
					return bhResult = new BhResult(200,"获取信息成功，商家自配！", orderStock);
					
				}else if(orderShop1.getDeliveryWay()==0){
					  //速达
					if(orderSendInfo2==null||orderSendInfo2.equals("")){
						//没有信息
						  orderStock.setJd("0");
						  track.setMsgTime("");
						  track.setOperator("");
						  track.setContent("待接单");//内容
						  listTrack2.add(track);
						  orderStock.setOrderTrack(listTrack2);
						  return bhResult = new BhResult(200,"待接单！", orderStock);
					}
					if(orderSendInfo2.getdState()==0){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    track.setMsgTime("");//时间
					    track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"待发货！", orderStock);
			    	    return bhResult;
			    	}else if(orderSendInfo2.getdState()==1){
				
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    track2.setMsgTime(sdf.format(orderSendInfo2.getOcTime()));//时间
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
					    
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"发货中！", orderStock);
			        	return bhResult;
				    }else if(orderSendInfo2.getdState()==2){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    track2.setMsgTime(sdf.format(orderSendInfo2.getOcTime()));//时间
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
				        
					    Track track3 = new Track();
					    track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));//时间
					    track3.setOperator(orderSendInfo2.getsName());//配送员
					    track3.setContent("已送达");//内容
					    listTrack2.add(track3);
					    
				    	orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"已送达！", orderStock);
			    	    return bhResult;
				    }else if(orderSendInfo2.getdState()==3){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
					    track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("已取消");//内容
					    listTrack2.add(track);
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"已取消！", orderStock);
			    	    return bhResult;
				    }else if(orderSendInfo2.getdState()==4){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    track2.setMsgTime(sdf.format(orderSendInfo2.getOcTime()));//时间
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
				        
					    Track track3 = new Track();
					    track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));//时间
					    track3.setOperator(orderSendInfo2.getsName());//配送员
					    track3.setContent("已送达");//内容
					    listTrack2.add(track3);
				        
					    Track track4 = new Track();
					    track4.setMsgTime("");//时间
					    track4.setOperator(orderSendInfo2.getsName());//配送员
					    track4.setContent("已结算");//内容
					    listTrack2.add(track4);
					    
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
				  	    bhResult = new BhResult(200,"已结算！", orderStock);
			        	return bhResult;
				    }
					
				}else if(orderShop1.getDeliveryWay()==3){
					  //其他物流
					  String host = "https://wuliu.market.alicloudapi.com";
			   	      String path = "/kdi";
			   	      String method = "GET";
			   	      String appcode = "232d013ef8244587a9a4f69cb2fcca47";
			   	      Map<String, String> headers = new HashMap<String, String>();
			   	      headers.put("Authorization", "APPCODE " + appcode);
			   	      Map<String, String> querys = new HashMap<String, String>();
			   	      querys.put("no", order.getExpressNo());
			   	      //获取物流配送信息
			   	      HttpResponse response2 = HttpUtils.doGet(host, path, method, headers, querys);
			   	      String Logistics = EntityUtils.toString(response2.getEntity());
			   	      JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
			   	      orderStock.setJd("1"); //其他物流
			   	      orderStock.setLogistics(jsonObject2);//返回给前端的物流配送信息
			   	    
			   	      if(jsonObject2.get("status").equals("0")){
			   	        //把返回的json转换成实体类字段
			   	        Gson gson = new Gson();
					    JdResult<OtherStock> ret = gson.fromJson(Logistics, new TypeToken<JdResult<OtherStock>>(){}.getType());
			   	        //其他物流信息
					    OtherStock otherStock =  ret.getResult();
					    List<OtherTrack> list = otherStock.getOtherTrack();
					    //把物流信息保存至数据库中
					    for(int i=0;i<list.size();i++){
						  
						  JdOrderTrack jdOrderTrack = new JdOrderTrack();
				    	  jdOrderTrack.setOrderId(orderShop1.getOrderId());//订单ID
				    		
				    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	  jdOrderTrack.setMsgTime(sdf.parse(list.get(i).getTime()));//时间
				    	  jdOrderTrack.setContent(list.get(i).getStatus()); //内容
				    	  //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
				    	  List<JdOrderTrack> jdOrderTrackList  = jdOrderTrackMapper.getByOrderId(jdOrderTrack);
				    	  if(jdOrderTrackList!=null&&jdOrderTrackList.size()>0){
				    			//有值就不保存
				    		}else{
				    			//没值就保存
				    			jdOrderTrack.setOrderNo(orderShop1.getOrderNo());
				    			jdOrderTrack.setStatus("1");
				    			int row = jdOrderTrackMapper.insertSelective(jdOrderTrack);
				    	  }
						
					    }
			   	      }
			   	      return bhResult = new BhResult(200,"当前为其他物流信息！", orderStock);
				}else if(orderShop1.getDeliveryWay()==2){
				      //京东物流
				      OrderShop orderShop = new OrderShop();
				      orderShop.setId(Integer.parseInt(id));
				      //查询京东物流信息，获取当前订单妥投信息，如果为妥投，就更改订单状态
			          orderStock  = jdOrderService.orderTrack(orderShop,null); 
			          orderStock.setOrderNo(orderShop1.getOrderNo()); //订单号
			          orderStock.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getAddress());//收货地址
				      orderStock.setImgeUrl(url); 
			          orderStock.setJd("2"); //是京东
			    
			          if(StringUtils.isEmpty(orderStock.getJdOrderId())){
			    	     bhResult = new BhResult(200,"目前京东没有配送信息！", orderStock);
			    	     return bhResult;
			          }
			          //把京东物流信息保存到jd_order_track表
			          List<Track> listTrack = orderStock.getOrderTrack();
			          //判断当前的订单的京东JdOrderId和返回的京东JdOrderId是不是一样的
			          if(listTrack.size()>0&&orderShop1.getJdorderid().equals(orderStock.getJdOrderId())){
			    	
			    	     for(int i=0;i<listTrack.size();i++){
			    		
			    		   JdOrderTrack jdOrderTrack = new JdOrderTrack();
			    		   jdOrderTrack.setOrderId(orderShop1.getOrderId());//订单ID
			    		
			    		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    		   jdOrderTrack.setMsgTime(sdf.parse(listTrack.get(i).getMsgTime()));//时间
			    		   jdOrderTrack.setContent(listTrack.get(i).getContent()); //内容
			    		   jdOrderTrack.setOperator(listTrack.get(i).getOperator());//操作人
			    		   //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
			    		   List<JdOrderTrack> jdOrderTrackList  = jdOrderTrackMapper.getByOrderId(jdOrderTrack);
			    		   if(jdOrderTrackList!=null&&jdOrderTrackList.size()>0){
			    			   //有值就不保存
			    		   }else{
			    			   //没值就保存
			    			   jdOrderTrack.setOrderNo(orderShop1.getOrderNo());
			    			   int row = jdOrderTrackMapper.insertSelective(jdOrderTrack);
			    		   }
			    	    }
			          }
			          bhResult = new BhResult(BhResultEnum.SUCCESS, orderStock);
			     		 
			    }
			 }
		}catch (Exception e) {
			e.printStackTrace();
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}
	
	
	
	
}
