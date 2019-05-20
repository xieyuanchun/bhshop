package com.order.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsCartMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.TopicGoods;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.api.JDStockApi;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.goods.AreaLimit;
import com.bh.jd.bean.order.NewStock;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.StockParams;
import com.bh.jd.bean.order.Track;
import com.bh.order.mapper.JdOrderMainMapper;
import com.bh.order.mapper.JdOrderSkuMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.JdOrderMain;
import com.bh.order.pojo.JdOrderSku;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.utils.MoneyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.order.enums.RefundReasonEnum;
import com.order.user.service.JDOrderService;
import com.print.controller.HttpUtils;
import com.wechat.service.WechatTemplateMsgService;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@Service
public class JDOrderServiceImpl implements JDOrderService{
	private static final Logger logger = LoggerFactory.getLogger(JDOrderServiceImpl.class);
	
	//倒入商品的Mapper
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
	private OrderRefundDocMapper orderRefundDocMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;

	@Autowired
	private OrderShopMapper orderShopMapper;
	
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
    @Autowired
    private TopicGoodsMapper topicGoodsMapper;
    
    @Autowired
    private  JdOrderMainMapper jdOrderMainMapper;
    @Autowired
    private  JdOrderSkuMapper jdOrderSkuMapper;
    
    

	/**
	 * @Description: 查询京东物流
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	public JdResult<OrderStock> jdOrderTrack(String jdOrderId) {
		
		//List<JdOrderMain> jdOrderMainList = jdOrderMainMapper.getUpdateJdOrderMain();
		List<JdOrderMain> jdOrderMainList=new ArrayList<>();
		JdOrderMain jdOrderMain1=jdOrderMainMapper.selectByPrimaryKey(Integer.valueOf(jdOrderId));
		jdOrderMainList.add(jdOrderMain1);
		for (JdOrderMain jdOrderMain : jdOrderMainList) {
			String jdOrderStr = JDStockApi.SelectJdOrder(jdOrderMain.getJdOrderId());// 查询京东订单信息接口
			JSONObject jdOrder = JSONObject.fromObject(jdOrderStr);
			JSONObject jdOrderResult = (JSONObject) jdOrder.get("result");
			
			boolean falg=true;
			String s = jdOrderResult.getString("pOrder");
			try {
				JSONObject jsonObject = JSONObject.fromObject(s);
				falg=false;
			} catch(Exception e ) {
			}
			Object o = (Object) jdOrderResult.get("pOrder");
			if (falg) {// 更新该子订单
				JdOrderMain updateJdOrderMain = new JdOrderMain();
				updateJdOrderMain.setId(jdOrderMain.getId());
				updateJdOrderMain.setState(jdOrderResult.getInt("state"));// 物流状态:0-是新建,1-是妥投,2-是拒收
				updateJdOrderMain.setSubmitState(jdOrderResult.getInt("submitState"));// 是否确定下单：0-为未确认，1-为确认
				updateJdOrderMain.setOrderState(jdOrderResult.getInt("orderState"));// 订单状态: 0-为取消订单, 1-为有效
				updateJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(jdOrderResult.getDouble("orderPrice")));// 京东订单价格
				updateJdOrderMain.setFreight(jdOrderResult.getInt("freight"));// 运费（合同配置了才返回）
				updateJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));// 编辑时间
				jdOrderMainMapper.updateByPrimaryKeySelective(updateJdOrderMain);
			} else {// 父订单
				JdOrderMain updateJdOrderMain = new JdOrderMain();
				updateJdOrderMain.setId(jdOrderMain.getId());
				updateJdOrderMain.setType(1);//更新子订单为父订单
				jdOrderMainMapper.updateByPrimaryKeySelective(updateJdOrderMain);
				//生成该父订单的子订单
				JSONArray cOrderArray =JSONArray.fromObject(jdOrderResult.get("cOrder"));
				
				for (Object object : cOrderArray) {
					JSONObject ob =JSONObject.fromObject(object);//某个子订单
					JSONArray cOrderSkuArray =JSONArray.fromObject(ob.get("sku"));//某个子订单的sku
					
					JdOrderMain saveJdOrderMain=new JdOrderMain();
					saveJdOrderMain.setJdOrderId(ob.getLong("jdOrderId")+"");//京东订单编号
					saveJdOrderMain.setState(ob.getInt("state"));//物流状态:0-是新建,1-是妥投,2-是拒收
					saveJdOrderMain.setParentOrderId(ob.getString("pOrder"));//父订单号
					saveJdOrderMain.setType(ob.getInt("type"));//订单类型: 1-是父订单, 2-是子订单
					saveJdOrderMain.setSubmitState(ob.getInt("submitState"));//是否确定下单：0-为未确认，1-为确认
					saveJdOrderMain.setOrderState(ob.getInt("orderState"));//订单状态: 0-为取消订单 , 1-为有效
					saveJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(ob.getDouble("orderPrice")));//京东订单价格
					saveJdOrderMain.setFreight(ob.getInt("freight"));//运费（合同配置了才返回）
					saveJdOrderMain.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
					saveJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
					saveJdOrderMain.setOrderShopId(jdOrderMain.getOrderShopId());//商家订单id
					saveJdOrderMain.setSendState(0);//京东配送状态
					jdOrderMainMapper.insertSelective(saveJdOrderMain);
					
					/*********** jd_order_sku 插入记录xieyc start **********/
					for (Object obsku : cOrderSkuArray) {
						JSONObject obSku = JSONObject.fromObject(obsku);
						JdOrderSku saveJdOrderSku = new JdOrderSku();
						saveJdOrderSku.setSkuId(obSku.getLong("skuId"));
						saveJdOrderSku.setCategory(obSku.getLong("category"));
						saveJdOrderSku.setNum(obSku.getInt("num"));
						saveJdOrderSku.setName(obSku.getString("name"));
						saveJdOrderSku.setPrice(MoneyUtil.doubeToInt(obSku.getDouble("price")));
						saveJdOrderSku.setAddTime(new Date(JedisUtil.getInstance().time()));
						saveJdOrderSku.setEditTime(new Date(JedisUtil.getInstance().time()));
						saveJdOrderSku.setJdMainId(saveJdOrderMain.getId());
						JdOrderSku jdOrderSku=jdOrderSkuMapper.getByJdMainIdAndSkuId(jdOrderMain.getId(),obSku.getLong("skuId"));
						saveJdOrderSku.setOrderSkuId(jdOrderSku.getOrderSkuId());
						
						jdOrderSkuMapper.insertSelective(saveJdOrderSku);
					}
				}
			}
		}
		
		
		
		
		return null;
		
		
		
		
		
		
		//return JDStockApi.orderTrack(jdOrderId);
	}
    
    public static void main(String[] args) {
		String jdOrderStr = JDStockApi.SelectJdOrder("76768554773");// 查询京东订单信息接口
		System.out.println(jdOrderStr);
		/*JSONObject jdOrder =JSONObject.fromObject(jdOrderStr);
		JSONObject jdOrderResult =  (JSONObject) jdOrder.get("result");
		boolean falg=true;
		String s = jdOrderResult.getString("pOrder");
		try {
			JSONObject jsonObject = JSONObject.fromObject(s);
			falg=false;
		} catch(Exception e ) {
			
		}
		System.out.println(falg);
		*/
		/*
		JSONObject pOrder =(JSONObject) jdOrderResult.get("pOrder");
		JSONArray pOrderArray =JSONArray.fromObject(pOrder.get("sku"));
		for (Object object : pOrderArray) {
			JSONObject ob =JSONObject.fromObject(object);
			System.out.println(ob.get("skuId"));
		}
		JSONArray cOrderArray =JSONArray.fromObject(jdOrderResult.get("cOrder"));
		for (Object object : cOrderArray) {
			JSONObject ob =JSONObject.fromObject(object);
			System.out.println(ob.toString());
			System.out.println(ob.get("jdOrderId"));
			JSONArray cOrderSkuArray =JSONArray.fromObject(ob.get("sku"));
			for (Object objectSku : cOrderSkuArray) {
				JSONObject obSku =JSONObject.fromObject(objectSku);
				System.out.println(obSku.get("skuId"));
			}
			
		}*/
	}
   
    /**
	 * @Description: 京东订单插入
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	public void jdOrderInsert() {
		List<OrderShop>	list=orderShopMapper.getJdOrderShop();
		for (OrderShop orderShop : list) {
			String jdOrderStr=JDStockApi.SelectJdOrder(orderShop.getJdorderid());// 查询京东订单信息接口
			JSONObject jdOrder =JSONObject.fromObject(jdOrderStr);
			JSONObject jdOrderResult =  (JSONObject) jdOrder.get("result");
			Object o=(Object)jdOrderResult.get("pOrder");
			if(o.toString().equals("0")){//子订单
				JSONArray jdArray =JSONArray.fromObject(jdOrderResult.get("sku"));
				this.insertJdOrder(orderShop,jdOrderResult,jdArray,0);
			} else {//父订单
				//先插入父订单
				JSONObject pOrder =(JSONObject) jdOrderResult.get("pOrder");
				JSONArray pOrderSkuArray =JSONArray.fromObject(pOrder.get("sku"));
				this.insertJdOrder(orderShop,pOrder,pOrderSkuArray,1);
				//在插入父订单的子订单
				JSONArray cOrderArray =JSONArray.fromObject(jdOrderResult.get("cOrder"));
				for (Object object : cOrderArray) {
					JSONObject ob =JSONObject.fromObject(object);//某个子订单
					JSONArray cOrderSkuArray =JSONArray.fromObject(ob.get("sku"));//某个子订单的sku
					this.insertJdOrder(orderShop,ob,cOrderSkuArray,2);		
				}
			}
		}
	}
	public void insertJdOrder(OrderShop orderShop,JSONObject jdOrderResult,JSONArray jdArray,int falg){
		JdOrderMain saveJdOrderMain=new JdOrderMain();
		saveJdOrderMain.setJdOrderId(jdOrderResult.getLong("jdOrderId")+"");//京东订单编号
		if(falg==1){
			saveJdOrderMain.setState(0);//物流状态:0-是新建,1-是妥投,2-是拒收
			saveJdOrderMain.setParentOrderId("0");//父订单号
			saveJdOrderMain.setType(1);//订单类型: 1-是父订单, 2-是子订单
			saveJdOrderMain.setSubmitState(1);//是否确定下单：0-为未确认，1-为确认
			saveJdOrderMain.setOrderState(1);//订单状态: 0-为取消订单 , 1-为有效
		}else{
			saveJdOrderMain.setState(jdOrderResult.getInt("state"));//物流状态:0-是新建,1-是妥投,2-是拒收
			saveJdOrderMain.setParentOrderId(jdOrderResult.getString("pOrder"));//父订单号
			saveJdOrderMain.setType(jdOrderResult.getInt("type"));//订单类型: 1-是父订单, 2-是子订单
			saveJdOrderMain.setSubmitState(jdOrderResult.getInt("submitState"));//是否确定下单：0-为未确认，1-为确认
			saveJdOrderMain.setOrderState(jdOrderResult.getInt("orderState"));//订单状态: 0-为取消订单 , 1-为有效
		}
		saveJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(jdOrderResult.getDouble("orderPrice")));//京东订单价格
		saveJdOrderMain.setFreight(jdOrderResult.getInt("freight"));//运费（合同配置了才返回）
		
		saveJdOrderMain.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
		saveJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
		saveJdOrderMain.setOrderShopId(orderShop.getId());//商家订单id
		saveJdOrderMain.setSendState(0);//京东配送状态
		jdOrderMainMapper.insertSelective(saveJdOrderMain);
		
		/*********** jd_order_sku 插入记录xieyc start **********/
		for (Object object : jdArray) {
			JSONObject ob = JSONObject.fromObject(object);
			JdOrderSku saveJdOrderSku = new JdOrderSku();
			saveJdOrderSku.setSkuId(ob.getLong("skuId"));
			saveJdOrderSku.setCategory(ob.getLong("category"));
			saveJdOrderSku.setNum(ob.getInt("num"));
			//saveJdOrderSku.setName(ob.getString("name"));
			saveJdOrderSku.setPrice(MoneyUtil.doubeToInt(ob.getDouble("price")));
			saveJdOrderSku.setAddTime(new Date(JedisUtil.getInstance().time()));
			saveJdOrderSku.setEditTime(new Date(JedisUtil.getInstance().time()));
			saveJdOrderSku.setJdMainId(saveJdOrderMain.getId());
			// 根据skuId与order_shop_id查询order_sku
			OrderSku orderSku = orderSkuMapper.getByJdSkuIdAndOrderShopId(orderShop.getId(),ob.getLong("skuId") + "");
			if(orderSku!=null){
				saveJdOrderSku.setOrderSkuId(orderSku.getId());
			}
			jdOrderSkuMapper.insertSelective(saveJdOrderSku);
		}
	}
	
    
	
	public BhResult returnStock(String isFromCart,String cartIds,String area,Member member)throws Exception{
		BhResult bhResult=new BhResult();
		//isFromCart值为0时是从购物车过来，值为1时是从订单列表过来
		if (isFromCart.equals("0")) {
			//如果该商品是拍卖商品
			List<String> c = JsonUtils.stringToList(cartIds);
			GoodsCart goo = goodsCartMapper.selectByPrimaryKey(Integer.parseInt(c.get(0)));
			List<TopicGoods> topicGoodsList = topicGoodsMapper.selectByPaiMaiGoodsId(goo.getgId());
			if (topicGoodsList.size()>0) {
				if (topicGoodsList.get(0).getKuNums()<goo.getNum()) {
					bhResult = new BhResult(400, Contants.notStockMsg, null);
				}else{
					bhResult = new BhResult(200, "商品全有货", null);
				}
		    }else{
			    bhResult =getNewStockById(cartIds,area);
		    }
		}else if (isFromCart.equals("1")) {
			Order order = new Order();
			order.setmId(member.getId());
			order.setId(Integer.parseInt(cartIds));
			Order order1 =selectOrderByIds(order);
			if (order1 !=null) {
				//如果购物车的字段有数据
				if (order1.getCartId() !=null) {
					List<String> c = JsonUtils.stringToList(order1.getCartId());
					GoodsCart goo = goodsCartMapper.selectByPrimaryKey(Integer.parseInt(c.get(0)));
					List<TopicGoods> topicGoodsList = topicGoodsMapper.selectByPaiMaiGoodsId(goo.getgId());
					if (topicGoodsList.size()>0) {
						if (topicGoodsList.get(0).getKuNums()<goo.getNum()) {
							bhResult = new BhResult(400, Contants.notStockMsg, null);
						}else{
							bhResult = new BhResult(200, "商品全有货", null);
						}
				    }else{
					     bhResult =getNewStockById(order1.getCartId(),area);
				    }
				}else{
					bhResult =getNewStockByOrderId(String.valueOf(order1.getId()));
				}
			}
		}
		return bhResult;
	}
	
	public BhResult getNewStockById(String cartIds,String area) throws Exception{
		BhResult bhResult=new BhResult();
		//查询不是京东商品的库存
		bhResult =getNotJDNewStockById(cartIds, area);
		//如果有货
		if (bhResult.getStatus()==200) {
			//查询京东商品的库存，调用京东api接口
			bhResult =getJDNewStockById(cartIds,area);
		}
		return bhResult;
	}
	
	//批量获取库存接口（建议订单详情页、下单使用）:非京东/滨惠商品商品
	public BhResult getNotJDNewStockById(String cartIds,String area) throws Exception{
		BhResult bhResult = null;
		List<String> cartIdList = JsonUtils.stringToList(cartIds);
		//将京东的商品和滨惠的商品分别检出来
		//这是滨惠的商品
		List<GoodsCart> bhIds = goodsCartMapper.selectGoodsCartByBHOrJD(cartIdList, 0);
		//购物车既有京东商品，又有滨惠商品
		if (bhIds.size() >0) {
			//查询滨惠的库存
			BhResult bhStockStatus = getBHStockById(cartIds);	
			//如果京东库存有货并且滨惠自营的库存有货，则返回200全部有货
			if (bhStockStatus.getStatus()==200) {
				bhResult = new BhResult(200, "商品全有货", null);
			}else {				
				bhResult = new BhResult(400, Contants.notStockMsg, null);
			}	
		}else{
			bhResult = new BhResult(200, "有货", null);
		}
		return bhResult;
		
	}
	
	//6.26 批量获取库存接口（建议订单详情页、下单使用）:非京东商品商品
	public BhResult getJDNewStockById(String cartIds,String area) throws Exception{
		List<String> jdList=JsonUtils.stringToList(cartIds);
		//京东商品
		List<StockParams> jdIds = goodsCartMapper.selectGoodsCartByJD(jdList,1);
		// 查找京东的库存 返回值：200有货 ，201无货 ，400报错（参数不对，地址不对）
		BhResult bh = new BhResult();
		if (jdIds.size()>0) {
			bh=getJDStockById(area,jdIds);
			if (bh.getStatus() == 200) {
				bh = new BhResult(200, "商品全有货", null);
			}else if (bh.getStatus() == 201) {
				bh = new BhResult(400, Contants.notStockMsg, null);
			} 
		}else{
			bh = new BhResult(200, "全部有货", null);
		}
		return bh;
	}
	
	//查找京东的库存 返回值：200有货 ，201无货 ，400报错
	public BhResult getJDStockById(String area,List<StockParams> jdIds){
		BhResult bhResult = null;
		String skuNums = JsonUtils.objectToJson(jdIds);
		List<NewStock> list = new ArrayList<>();
		int size=0;
		JdResult<String> jdResult = null;
		if ((area !=null) && (area !="")) {
			MemberUserAddress address = memberUserAddressMapper.selectByPrimaryKey(Integer.parseInt(area));
			//是否是京东地址，0否，1是
			if (address.getIsJd() == 0) {
				size =-1;
			}else if (address.getIsJd() == 1) {
				StringBuffer sb = new StringBuffer();
				sb.append(address.getProv()).append("_");
				sb.append(address.getCity()).append("_");
				sb.append(address.getArea());
				 jdResult = JDStockApi.getNewStockById(skuNums, sb.toString());
				if (jdResult.getSuccess()) {
					String ret = jdResult.getResult();
					list = JsonUtils.jsonToList(ret, NewStock.class);
				}else{
					size =0;
				}
			}
		}else{
			jdResult = JDStockApi.getNewStockById(skuNums, "1_0_0");
			if (jdResult.getSuccess()) {
				String ret = jdResult.getResult();
				list = JsonUtils.jsonToList(ret, NewStock.class);
			}else{
				size =0;
			}
		}
		
		
		if (list.size() > 0) {
			//无货商品列表
			List<NewStock> notStockList=new ArrayList<>();
			//0代表有货，1代表无货
			for(int i=0;i<list.size();i++){				
				int stockStateId = list.get(i).getStockStateId();
				//33 有货 现货-下单立即发货
				//39 有货 在途-正在内部配货，预计2~6天到达本仓库
				//40 有货 可配货-下单后从有货仓库配货
				//36 预订
				//34 无货
				if (stockStateId == 34) {
					notStockList.add(list.get(i));
				}
			}
			if (notStockList.size()>0) {
				bhResult = new BhResult(201,"部分商品无货", notStockList);
			}else{
				bhResult = new BhResult(200,"京东商品下单全部有货", null);
			}
		}else if(size ==0){
			bhResult = new BhResult(400,jdResult.getResultMessage() , null);
		}else if (size == -1) {
			bhResult = new BhResult(400, "该地址不是京东地址", null);
		}
		return bhResult;
	}
	
	
	//查找滨惠的库存
	public BhResult getBHStockById(String cartIds){
		BhResult bhResult = null;
		List<String> ids = JsonUtils.stringToList(cartIds);
		List<GoodsCart> list = goodsCartMapper.selectGoodsName(ids);
		//如果list.size>0表示该滨惠有库存不足的商品
		if (list.size()>0) {
			//String goodsName = returnGoodsName(list);
			bhResult=new BhResult(400, Contants.notStockMsg,null);		
		}else{
			bhResult=new BhResult(200, "商品全部有货", null);		
		}
		return bhResult;
	}
	
	
	public Order selectOrderByIds(Order order) throws Exception{
		Order order1 = new Order();
		order1 = orderMapper.selectOrderBymId(order);
		return order1;
	}
	
	//当购物车的id为空时,需要通过Order 的id判断库存
	public BhResult getNewStockByOrderId(String orderId) throws Exception{
		BhResult bhResult = null;
		//0代表有货，1代表无货
		Map<String, Object> map= getBHStockByOrderId(orderId);
		Integer flag=(Integer) map.get("flag");
		if (flag == 0) {
			bhResult = new BhResult(200, "商品全有货", null);
		}else if (flag ==1) {
			bhResult = new BhResult(400, Contants.notStockMsg, null);
		}
		return bhResult;
		
	}
	
	//通过orderId 查找滨惠的库存
	public Map<String, Object> getBHStockByOrderId(String orderId){
		Map<String, Object> map=new HashMap<>();
		//这是滨惠的商品
		OrderSku orderSku = new OrderSku();
		orderSku.setOrderId(Integer.parseInt(orderId));
		List<OrderSku> list = orderSkuMapper.selectOrderShopBySelect(orderSku);
		int flag =0;
		List<String> goodsSkuIds = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(list.get(i).getSkuId());			
			int isJdSupport= goodsSku.getJdSupport();
			//是否支持京东下单，0不支持，1支持
			if (isJdSupport ==0) {
				if (goodsSku !=null) {
					int num=0;
				    num = list.get(i).getSkuNum();
					int store = goodsSku.getStoreNums() - num;
					if (store < 0) {
						flag =1;
						goodsSkuIds.add(goodsSku.getId().toString());
					}
				}
			}
		}
		map.put("flag", flag);
		map.put("goodsSkuIds", goodsSkuIds);
		return map;
	}
	
	//0下单成功   -1下单失败   -2不存在orderSku
	public int updateJDOrderId(Order order) throws Exception{
		Order order2 = orderMapper.selectByPrimaryKey(order.getId());
		MemberUserAddress memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order2.getmAddrId());
		
		List<OrderSku> orderSkuList = new ArrayList<>();
		OrderSku orderSkuParams = new OrderSku();
		orderSkuParams.setOrderId(order.getId());
		orderSkuList = orderSkuMapper.selectJDSkuId(orderSkuParams);
		
		//京东订单的拆单
		
		if (orderSkuList.size() > 0) {
			for (OrderSku orderSku : orderSkuList) {
				OrderSku orderSku2 = new OrderSku();
				orderSku2.setOrderId(order.getId());
				orderSku2.setOrderShopId(orderSku.getOrderShopId());
				List<OrderSku> orderSkus = orderSkuMapper.selectJDSkuId1(orderSku2);
				List<StockParams> jdIds = new ArrayList<>();
				for (OrderSku orderSku3 : orderSkus) {
					GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderSku3.getSkuId());
					StockParams param = new StockParams();
					param.setSkuId(String.valueOf(goodsSku.getJdSkuNo()));
					param.setNum(orderSku3.getSkuNum());
					param.setbNeedAnnex(false);
					param.setbNeedGift(false);
					jdIds.add(param);
				}
				
				//"[{skuId:'569172',num:'100'},{skuId:'1421728',num:'100','bNeedAnnex':true, 'bNeedGift':true,'price':188.00}]";//[{"skuId":商品编号, "num":商品数量,"bNeedAnnex":true, "bNeedGift":true,"price":100,"yanbao":[{"skuId":商品编号}]}]    (最高支持50种商品)
				 OrderShop orderShop = orderShopMapper.selectByPrimaryKey(orderSku.getOrderShopId());
				 
				 OrderShop newOrderShop= Dismantling(order2,orderShop);
				 if (newOrderShop==null) {
					newOrderShop=orderShop;
				 }
				 
				String thirdOrder = newOrderShop.getShopOrderNo();//必须    第三方的订单单号
				JSONArray ja3 = JSONArray.fromObject(JsonUtils.objectToJson(jdIds));
				String sku = ja3.toString();
				//bNeedAnnex表示是否需要附件，默认每个订单都给附件，默认值为：true，如果客户实在不需要附件bNeedAnnex可以给false，该参数配置为false时请谨慎，真的不会给客户发附件的;
				//bNeedGift表示是否需要增品，默认不给增品，默认值为：false，如果需要增品bNeedGift请给true,建议该参数都给true,但如果实在不需要增品可以给false;
				//price 表示透传价格，需要合同权限，接受价格权限，否则不允许传该值；
				 String name =memberUserAddress.getFullName();//必须	收货人
				 String province = String.valueOf(memberUserAddress.getProv());// 必须	一级地址
				 String city = String.valueOf(memberUserAddress.getCity());//	int	必须	二级地址
				 String county = String.valueOf(memberUserAddress.getArea());//int 必须	三级地址
				 String town = String.valueOf(memberUserAddress.getFour());//	int	必须	四级地址  (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传0)
				 String address =memberUserAddress.getAddress();//Stirng	必须	详细地址
				 String zip = "546300";//Stirng	非必须  邮编
				 String mobile= memberUserAddress.getTel();//Stirng	必须	手机号 
				 String email = "872820897@qq.com";//	Stirng	必须	邮箱
				 String invoiceState = "2";//int	必须	开票方式(1为随货开票，0为订单预借，2为集中开票 )
				 String invoiceType = "1";//int	必须	1普通发票2增值税发票
				 String selectedInvoiceTitle = "4";//int	必须	发票类型：4个人，5单位
				 String companyName = "陈滨惠";//String	必须	发票抬头  (如果selectedInvoiceTitle=5则此字段必须)
				 String invoiceContent = "1";//	int	必须	1:明细，3：电脑配件，19:耗材，22：办公用品  (备注:若增值发票则只能选1 明细)
				 String paymentType = "4";//	int	必须	支付方式 (1：货到付款，2：邮局付款，4：在线支付，5：公司转账，6：银行转账，7：网银钱包，101：金采支付)
				 String isUseBalance = "1";//	int	必须	使用余额paymentType=4时，此值固定是1   ，  其他支付方式0
				 String submitState = "1";//Int	必须	是否预占库存，0是预占库存（需要调用确认订单接口），1是不预占库存     金融支付必须预占库存传0;
		    	
				 JdResult  jdResult= JDStockApi.submitOrder(thirdOrder, sku, name, province, city, county, town, address, zip, mobile, email, invoiceState, invoiceType, selectedInvoiceTitle, companyName, invoiceContent, paymentType, isUseBalance, submitState);				
				 //生成京东订单成功
				 if (jdResult.getSuccess()) {		
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) jdResult.getResult();
					String jdOrderId = (String) map.get("jdOrderId").toString();
					System.out.println("jdOrderId-->" + jdOrderId);
					logger.info("jdOrderId" + jdOrderId);
					OrderShop orderShop2 = new OrderShop();
					orderShop2.setId(newOrderShop.getId());
					orderShop2.setJdorderid(jdOrderId);
					orderShop2.setStatus(Contants.shopStatu3);
					orderShop2.setSendTime(new Date());
					orderShop2.setDeliveryWay(2);
					orderShopMapper.updateByPrimaryKeySelective(orderShop2);
					
					/***********jd_order_main 插入记录xieyc start**********/
					String jdOrderStr=JDStockApi.SelectJdOrder(jdOrderId);// 查询京东订单信息接口
					JSONObject jdOrder =JSONObject.fromObject(jdOrderStr);
					JSONObject jdOrderResult =  (JSONObject) jdOrder.get("result");
					JSONArray jdArray =JSONArray.fromObject(jdOrderResult.get("sku"));
					
					if(jdOrderResult.getInt("pOrder")==0){
						JdOrderMain saveJdOrderMain=new JdOrderMain();
						saveJdOrderMain.setJdOrderId(jdOrderId);//京东订单编号
						saveJdOrderMain.setState(jdOrderResult.getInt("state"));//物流状态:0-是新建,1-是妥投,2-是拒收
						saveJdOrderMain.setType(jdOrderResult.getInt("type"));//订单类型: 1-是父订单, 2-是子订单
						saveJdOrderMain.setOrderPrice(MoneyUtil.doubeToInt(jdOrderResult.getDouble("orderPrice")));//京东订单价格
						saveJdOrderMain.setFreight(jdOrderResult.getInt("freight"));//运费（合同配置了才返回）
						saveJdOrderMain.setParentOrderId(jdOrderResult.getString("pOrder"));//父订单号
						saveJdOrderMain.setOrderState(jdOrderResult.getInt("orderState"));//订单状态: 0-为取消订单 , 1-为有效
						saveJdOrderMain.setSubmitState(jdOrderResult.getInt("submitState"));//是否确定下单：0-为未确认，1-为确认
						saveJdOrderMain.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
						saveJdOrderMain.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
						saveJdOrderMain.setOrderShopId(newOrderShop.getId());//商家订单id
						saveJdOrderMain.setSendState(0);//京东配送状态
						jdOrderMainMapper.insertSelective(saveJdOrderMain);
						
						/***********jd_order_sku 插入记录xieyc start**********/
						for (Object object : jdArray) {
							JSONObject ob =JSONObject.fromObject(object);
							JdOrderSku saveJdOrderSku=new JdOrderSku();
							saveJdOrderSku.setSkuId(ob.getLong("skuId"));
							saveJdOrderSku.setCategory(ob.getLong("category"));
							saveJdOrderSku.setNum(ob.getInt("num"));
							//saveJdOrderSku.setName(ob.getString("name"));
							saveJdOrderSku.setPrice(MoneyUtil.doubeToInt(ob.getDouble("price")));
							saveJdOrderSku.setAddTime(new Date(JedisUtil.getInstance().time()));
							saveJdOrderSku.setEditTime(new Date(JedisUtil.getInstance().time()));
							saveJdOrderSku.setJdMainId(saveJdOrderMain.getId());
							//根据skuId与order_shop_id查询order_sku
							OrderSku sku1 = orderSkuMapper.getByJdSkuIdAndOrderShopId(newOrderShop.getId(),ob.getLong("skuId") + "");
							if(sku1!=null){
								saveJdOrderSku.setOrderSkuId(sku1.getId());
							}
							jdOrderSkuMapper.insertSelective(saveJdOrderSku);
						}
						/***********jd_order_sku 插入记录 end**************/
					}											
					
					/***********jd_order_main 插入记录 end**************/
					
					 //程凤云 2018-4-11添加
		        	 List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop.getOrderNo());
				     if (teamList.size()>0) {
				    	  WXMSgTemplate template = new WXMSgTemplate();
				    	  template.setOrderShopId(orderShop.getId()+"");
				    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
					 }
					return -1;
				}else{
					OrderShop orderShop2 = new OrderShop();
					orderShop2.setId(newOrderShop.getId());
					orderShop2.setJdorderid("-1");
					if (jdResult.getResultCode() != null) {
						orderShop2.setErrCode(jdResult.getResultCode());
					}
					orderShop2.setErrMsg(jdResult.getResultMessage());
					orderShopMapper.updateByPrimaryKeySelective(orderShop2);
					return 0;
				}
				
			}
		}
		return -2; 
	}
	
	
	
	
	 public OrderStock orderTrack(OrderShop orderShop,Long jdSkuId) throws Exception{
		 OrderStock stock = new OrderStock();
		 OrderShop orderShop1 = orderShopMapper.selectByPrimaryKey(orderShop.getId()); //
		 Order order = orderMapper.selectByPrimaryKey(orderShop1.getOrderId());
		 MemberShop memberShop = memberShopMapper.selectByPrimaryKey(orderShop1.getShopId());
		 if (orderShop1.getJdorderid().equals("0")) {
			 Track track = new Track();
			 if (orderShop1.getIsRefund().equals(0)) {
				 if(orderShop1.getStatus().equals(9)){
					 track.setContent("备货中");
					 track.setOperator(memberShop.getLinkmanName());
					 track.setMsgTime("");
				 }else if (orderShop1.getdState().equals(2) || orderShop1.getdState().equals(3)) {
					 //订单配送信息
					 OrderSendInfo info = new OrderSendInfo();
					 info.setmId(orderShop1.getmId());
					 info.setOrderShopId(orderShop1.getId());
					 List<OrderSendInfo> orderSendInfo = orderSendInfoMapper.selectOrderShopBymid(info);
					 if (orderSendInfo.size() > 0) {
						 OrderSendInfo  info2 = orderSendInfo.get(0);
						 if (info2.getdState().equals(0)) {
							 track.setContent("备货中");
						}else if (info2.getdState().equals(1)) {
							track.setContent("发货中");
						}else if (info2.getdState().equals(2)) {
							track.setContent("已送达");
						}else if (info2.getdState().equals(3)) {
							track.setContent("已取消");
						}
						 track.setMsgTime(info2.getOcTime().toString());
						 track.setOperator(info2.getsName());
					}else {
						 track.setContent("商家自配");
						 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						 if (orderShop1.getSendTime() !=null) {
							track.setMsgTime(sdf.format(orderShop1.getSendTime()));
						}else{
							 track.setMsgTime(sdf.format(order.getAddtime()));
						}
						 track.setOperator(memberShop.getLinkmanName());
					}
				}else{
					track.setContent(null);
					track.setMsgTime(null);
					track.setMsgTime(null);
				}
			}else{//已取消
				track.setContent(null);
				track.setMsgTime(null);
				track.setMsgTime(null);
			}
			 List<Track> tracks = new ArrayList<>();
			 tracks.add(track);
			 stock.setOrderTrack(tracks);
		}else{	
			JdResult<OrderStock> ret=null;
			//某个商家订单的某个jdSkuId商品的子订单
			JdOrderMain jdOrderMain=jdOrderMainMapper.getJdOrderMainByJdSkuId(jdSkuId,orderShop1.getId());
			if(jdOrderMain!=null){
				ret = JDStockApi.orderTrack(jdOrderMain.getJdOrderId());
			}else{
				 ret = JDStockApi.orderTrack(orderShop1.getJdorderid());
			}
			if (ret!=null && ret.getSuccess()) {
				stock = ret.getResult();
				List<Track> orderTrack = stock.getOrderTrack();
				if(orderTrack.size()>0){
//					orderShop1.setdState(3); //配送中
//					orderShopMapper.updateByPrimaryKeySelective(orderShop1);
				}else{
//					orderShop1.setdState(2); //待发货
//					orderShopMapper.updateByPrimaryKeySelective(orderShop1);
				}
			}
		}
		 return stock;
	 }

	@Override
	public OrderShop getOrderById(String id) {
		// TODO Auto-generated method stub
		return orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public Order getOrderMainById(String id) {
		// TODO Auto-generated method stub
		return orderMapper.getOrderByOrderNo(id);
	}

	@Override
	public MemberUserAddress getUserById(Integer id) {
		// TODO Auto-generated method stub
		return memberUserAddressMapper.selectByPrimaryKey(id);
	}

	@Override
	public int addOrderSendInfo(OrderSendInfo orderSendInfo) {
		// TODO Auto-generated method stub
		return orderSendInfoMapper.insertSelective(orderSendInfo);
	}

	@Override
	public List<OrderSendInfo> selectByjdOrderId(OrderSendInfo record) {
		// TODO Auto-generated method stub
		return orderSendInfoMapper.selectByjdOrderId(record);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderSendInfo record) {
		// TODO Auto-generated method stub
		return orderSendInfoMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<OrderSku> getOrderSkuByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return orderSkuMapper.getOrderSkuByOrderId(orderId);
	}

	@Override
	public GoodsSku getGoodSkuBySkuId(Integer orderId) {
		// TODO Auto-generated method stub
		return goodsSkuMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public Goods getGoodsByGoodsId(Integer goodsId) {
		// TODO Auto-generated method stub
		return goodsMapper.selectByPrimaryKey(goodsId);
	}

	@Override
	public OrderShop getOrderLogistics(String id) {
		   
		    OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(id));
	        //设置快递配送信息参数
		    try{
		    Order order = orderMapper.selectByPrimaryKey(orderShop.getOrderId());
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
	
	
	  //判断该用户购买的商品是否超过数据库设定的上限
     /** 程凤云
     * isFromCart:字符串类型，取0和1值，0时是从购物车过来，值为1时是从订单列表过来
     * cartIds：如果isFromCart为0时是购物车的id,如果isFromCart为1时是订单的id
     * member:当前登录的用户
     * 返回值：如果为0可以继续购买，如果为1时提示"该用户购买该商品已达上限"
     * **/
    public BhResult memberBuyGoodsIsLimit(String cartIds,String isFromCart,Member member){
    	BhResult bhResult = null;
    	List<String> cartIds1 = JsonUtils.stringToList(cartIds);
    	if (StringUtils.isNotEmpty(isFromCart)) {
			if (isFromCart.equals("0")) {
				if (cartIds1.size()>0) {
					for (String string : cartIds1) {
						GoodsCart goodsCart = goodsCartMapper.selectByPrimaryKey(Integer.parseInt(string));
						bhResult = isCount(member.getId(), goodsCart.getgId(),goodsCart.getNum());	
						if (bhResult.getStatus()==400) {
							break;
						}
					}
				}							
			}else if (isFromCart.equals("1")) {
				List<OrderSku> orderSku = orderSkuMapper.selectOrderSkuByOrderId1(Integer.parseInt(cartIds1.get(0)));
				if (orderSku.size()>0) {
					for (OrderSku orderSku2 : orderSku) {
						bhResult = isCount(member.getId(), orderSku2.getGoodsId(),orderSku2.getSkuNum());
						if (bhResult.getStatus()==400) {
							break;
						}
					}
				}
			}
		}
    	return bhResult;
    }
    
    /**
     * 如果返回的值为0未达到购买的上限，如果是1表示达到购买的上限
     * */
    public BhResult isCount(Integer mId,Integer goodsId,Integer wantToBuy){
    	BhResult bhResult = null;
    	//获得商品
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		//一个用户购买商品的上限值:如果值为0则代表无限购买
		int goodByLimit = goods.getGoodBuyLimit();
		if (goodByLimit > 0) {
			//判断该用户已支付成功的商品
			int orderCount = orderSkuMapper.selectCountByGoods(mId,goods.getId());
			//拼单失败的单数量
			OrderRefundDoc doc = new OrderRefundDoc();
			doc.setmId(mId);
			doc.setGoodsId(goodsId);
			int failTeamCount = orderRefundDocMapper.selectFailTeamCount(doc);
			
			//退款的单数量(客服审核通过，已退款的数量不算入限购数量，可再次购买)
			doc.setReason(RefundReasonEnum.REFUND_MENEY.getReason());
			int reFundCount = orderRefundDocMapper.selectRefundCount(doc);
			
			//退货退款的单数量(数量算入限购数量)
			doc.setReason(RefundReasonEnum.REFUND_MENEY_GOODS.getReason());
			//当前的已购买数量num,所有已付款单数-拼单失败(不算限购数量)-申请退款的单(10,2,不算限购数量)+想要买的数量(算)
			int num = orderCount-failTeamCount-reFundCount +wantToBuy;
			if (goodByLimit<num) {
				//该用户购买改商品已达上线
				return bhResult = new BhResult(400, "此商品限购"+goodByLimit+"个", num);
			}		
		}
		return bhResult = new BhResult(200, "商品全有货", null);
    }

    @SuppressWarnings("unchecked")
    public String returnSkuId(BhResult JdGoods,BhResult bhGoods){
    	List<String> ids = new ArrayList<>();
    	if ((JdGoods!=null)&&(JdGoods.getData()!=null)) {
			List<NewStock> stockList=(List<NewStock>) JdGoods.getData();
			if (stockList.size()>0) {
				List<GoodsSku> goodsSkuList = goodsSkuMapper.selectSkuIdByJDSkuNo(stockList);
				if (goodsSkuList.size()>0) {
					for (GoodsSku goodsSku : goodsSkuList) {
						ids.add(goodsSku.getId().toString());
					}
				}				
			}
		}
    	if ((bhGoods!=null)&&(bhGoods.getData()!=null)) {
			List<GoodsCart> cartList =(List<GoodsCart>)  bhGoods.getData();
			if (cartList.size()>0) {
				for (GoodsCart goodsCart : cartList) {
					ids.add(goodsCart.getGskuid().toString());
				}
			}
		}
    	if (ids.size()>0) {
    		StringBuffer sb=new StringBuffer();
    		boolean first = true;
    		///第一个前面不拼接","
			for (String string : ids) {
				if(first) {
			         first=false;
			      }else{
			    	  sb.append(",");
			      }
				sb.append(string);
			}
			return sb.toString();
		}
    	return "";
    }
    
    //京东商品的区域购买限制
    public BhResult getAreaLimit(String addressId,Member member,List<String> cartIds){
    	BhResult bhResult=new BhResult();
    	//获取地址
    	MemberUserAddress memberUserAddress=memberUserAddressMapper.selectByPrimaryKey(Integer.parseInt(addressId));
    	//jdSkuNo
    	List<GoodsSku> SkuList=goodsCartMapper.selectJdSkuNo(cartIds,member.getId());
    	if (SkuList.size()>0) {
    		StringBuffer sb=new StringBuffer();
    		boolean first = true;
    		///第一个前面不拼接","
    		for (GoodsSku goodsSku : SkuList) {
				if(first) {
			         first=false;
			      }else{
			    	  sb.append(",");
			      }
				sb.append(goodsSku.getJdSkuNo());
    			
			}
    		List<String> ids = getIn(sb.toString(), memberUserAddress.getProv().toString()+"", memberUserAddress.getCity()+"", 
        			memberUserAddress.getArea()+"", memberUserAddress.getFour()+""); 
    		if (ids.size()>0) {
    			return bhResult=new BhResult(400, Contants.limitAreaMsg, ids);
			}
		}
    	return bhResult=new BhResult(200, "操作成功", "");
    }
    private final int  everyGroupNum=100;  
    public  List<String> getIn(String patientAllId,String province,String city,String county,String town) {  
        List<String> isLimit=new ArrayList<>(); 
       
        if(StringUtils.isNotEmpty(patientAllId)){  
            String[] array=patientAllId.split(",");  
            //数组总长度  
            int len=array.length;  
            //分组数  
            int groupCount=len/everyGroupNum;  
            for(int k=0;k<groupCount;k++){  
            	StringBuffer patientid=new StringBuffer(); 
                for(int i=(k*everyGroupNum);i<(k*everyGroupNum+everyGroupNum);i++){  
                    if(i==k*everyGroupNum){  
                        //每组的第一个数组  
                        patientid.append(""+array[i].trim()+"");  
                    }else{  
                        patientid.append(patientid+","+""+array[i].trim()+"");  
                    }  
                }  
                String s = JDGoodsApi.checkAreaLimit(patientid.toString(),province,city,county,town);
                Gson gson = new Gson();
                JdResult<String> str= gson.fromJson(s, new TypeToken<JdResult<String>>(){}.getType());
                if (str.getSuccess()) {
                	//Object obj = str.getResult();
					List<AreaLimit> list=JsonUtils.jsonToList(str.getResult(), AreaLimit.class);
					 for (AreaLimit areaLimit : list) {
							if (StringUtils.isNotEmpty(areaLimit.getIsAreaRestrict())) {
								isLimit.add(areaLimit.getSkuId().toString());
							}
					}
				}
            }  
            if(len%everyGroupNum != 0){  
                //未整除  
            	StringBuffer patientid=new StringBuffer(); 
                //处理最后一组数据  
                for(int j=(groupCount*everyGroupNum);j<len;j++){  
                    if(j==groupCount*everyGroupNum){  
                        //每组的第一个数组  
                        patientid.append(""+array[j].trim()+"");  
                    }else{  
                        patientid.append(patientid+","+""+array[j].trim()+"");  
                    }  
                }  
                String s = JDGoodsApi.checkAreaLimit(patientid.toString(),province,city,county,town);
                Gson gson = new Gson();
                JdResult<String> str= gson.fromJson(s, new TypeToken<JdResult<String>>(){}.getType());
                if (str.getSuccess()) {
                	//Object obj = str.getResult();
					List<AreaLimit> list=JsonUtils.jsonToList(str.getResult(), AreaLimit.class);
					 for (AreaLimit areaLimit : list) {
							if (areaLimit.getIsAreaRestrict().equals("true")) {
								isLimit.add(areaLimit.getSkuId().toString());
							}
					}
				}
               
            }  
        }  
        return isLimit;  
    } 
    
    
    
    public BhResult getAreaLimitByBH(String addressId,Member member,List<String> ids){
    	BhResult bhResult=new BhResult();
    	int flag=1;
    	MemberUserAddress memberUserAddress=memberUserAddressMapper.selectByPrimaryKey(Integer.parseInt(addressId));
    	//查询商品的区域
    	List<Goods> goodsList=goodsMapper.selectGoodsAreaByCart(ids, member.getId());
    	if (goodsList.size()>0) {
			for (Goods goods : goodsList) {
				String area = goods.getSendArea();
				if (StringUtils.isNotEmpty(area) && (!area.equals("0"))) {
					if (!area.contains(memberUserAddress.getProvname())) {
						flag=0;
						break;
					}
				}else{
					flag=0;
					break;
				}
			}
		}
    	if (flag==1) {
    		bhResult=new BhResult(200,"操作成功", null);
		}else{
			bhResult=new BhResult(400,Contants.limitAreaMsg, null);
		}
    	return bhResult;
    }
    
    public OrderShop Dismantling(Order order,OrderShop oShop) {
    	OrderShop orderShop=null;
    	OrderSku orderSku=new OrderSku();
    	orderSku.setOrderId(order.getId());
    	orderSku.setOrderShopId(oShop.getId());
    	List<OrderSku> orderSkuList = orderSkuMapper.selectJdSku(orderSku);
    	Integer orderPrice=0;
    	if (orderSkuList.size()>1) {
    		OrderSku orderSku2=new OrderSku();
    		orderSku2.setOrderId(order.getId());
    		List<OrderSku>jdSku = orderSkuMapper.selectJdSupport(orderSku2);
    		if (jdSku.size()>0) {
    			int allSize=orderSkuList.size();
        		int jdSize=jdSku.size();
        		int simple=allSize-jdSize;
        		if (simple>0) {
        			for (OrderSku jdSimpleSku : jdSku) {
        				orderPrice=orderPrice+jdSimpleSku.getSkuSellPriceReal()*jdSimpleSku.getSkuNum();
        			}orderShop=new OrderShop();
        			// 商家ID
        			orderShop.setShopId(oShop.getShopId());
        			// 用户ID
        			orderShop.setmId(order.getmId());
        			// 订单ID
        			orderShop.setOrderId(oShop.getOrderId());
        			// 订单号
        			orderShop.setOrderNo(oShop.getOrderNo());
        			// 商家订单号
        			orderShop.setShopOrderNo(oShop.getShopOrderNo()+"J");
        			// 商家订单状态：1待付，2待发货，3已发货，4已收货
        			orderShop.setStatus(2);
        			// 是否退款:0否，1是,默认0
        			orderShop.setIsRefund(0);
        			// 设置邮费
        			orderShop.setgDeliveryPrice(0);
        			// orderPrice = 邮费 + 商品价格 - 节省的钱
        			orderShop.setOrderPrice(orderPrice);
        			orderShopMapper.insertSelective(orderShop);
        			
        			for (OrderSku jdSimpleSku : jdSku) {
        				OrderSku orderSku3=new OrderSku();
            			orderSku3.setOrderShopId(orderShop.getId());
            			orderSku3.setId(jdSimpleSku.getId());
            			orderSkuMapper.updateByPrimaryKeySelective(orderSku3);
        			}
        			OrderShop oldOrderShop = new OrderShop();
        			oldOrderShop.setId(oShop.getId());
        			oldOrderShop.setOrderPrice(oShop.getOrderPrice()-orderPrice);
        			orderShopMapper.updateByPrimaryKeySelective(oldOrderShop);
    			}
        		
			}
		}
    	return orderShop;
    }
    
    //滨惠库存
    public BhResult getBHSto(Integer goodsId, String provname, Integer storeNums, Integer num) {
		BhResult result = BhResult.build(200, "全部有货", null);
		String area = goodsMapper.selectArea(goodsId);
		if (area != null && !area.equals("0")) {
			if (!area.contains(provname)) {
				return result = BhResult.build(400, Contants.goodsDetailLimitAreaMSg, null);
			}
		}else{
			return result = BhResult.build(400, Contants.goodsDetailLimitAreaMSg, null);
		}
		if (storeNums < num) {
			return result = new BhResult(400, Contants.notStockMsg, null);
		}
		return result;
	}
    
    //京东库存
    public BhResult getJDStock(String prov,String city,String area,List<StockParams> jdIds){
		BhResult result = BhResult.build(200, "全部有货", null);
		JdResult<String> jdResult = null;
		String skuNums = JsonUtils.objectToJson(jdIds);
		List<NewStock> list = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		sb.append(prov).append("_");
		sb.append(city).append("_");
		sb.append(area);
		jdResult = JDStockApi.getNewStockById(skuNums, sb.toString());
		if (jdResult.getSuccess()) {
			String ret = jdResult.getResult();
			list = JsonUtils.jsonToList(ret, NewStock.class);
		}
		
		if (list.size() > 0) {
			//无货商品列表
			List<NewStock> notStockList=new ArrayList<>();
			//0代表有货，1代表无货
			for(int i=0;i<list.size();i++){				
				int stockStateId = list.get(i).getStockStateId();
				//33 有货 现货-下单立即发货
				//39 有货 在途-正在内部配货，预计2~6天到达本仓库
				//40 有货 可配货-下单后从有货仓库配货
				//36 预订
				//34 无货
				if (stockStateId == 34) {
					notStockList.add(list.get(i));
				}
			}
			if (notStockList.size()>0) {
				result = new BhResult(400,Contants.notStockMsg, notStockList);
			}else{
				result = new BhResult(200,"京东商品下单全部有货", null);
			}
		}
		
		return result;
	}
    
    //京东地址限购商品
    public BhResult getAreaByJD(String skuIds, String prov, String city, String town, String four) {
		BhResult result = BhResult.build(200, "全部有货", null);
		String s = JDGoodsApi.checkAreaLimit(skuIds, prov, city, town, four);
		Gson gson = new Gson();
		JdResult<String> str = gson.fromJson(s, new TypeToken<JdResult<String>>() {
		}.getType());
		if (str.getSuccess()) {
			List<AreaLimit> list = JsonUtils.jsonToList(str.getResult(), AreaLimit.class);
			for (AreaLimit areaLimit : list) {
				// isAreaRestrict true 代表区域受限 false 区域不受限
				if (areaLimit.getIsAreaRestrict().equals("true")) {
					return result = BhResult.build(400, Contants.goodsDetailLimitAreaMSg, null);
				}
			}
		}
		return result;
	}

	

	
	
}
