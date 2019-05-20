package com.bh.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsCartMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.order.JdOrderMainMapper;
import com.bh.admin.mapper.order.OrderCollectionDocMapper;
import com.bh.admin.mapper.order.OrderExpressTypeMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderPaymentMapper;
import com.bh.admin.mapper.order.OrderRefundDocMapper;
import com.bh.admin.mapper.order.OrderSendInfoMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.mapper.user.MemberUserMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsCart;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.JdOrderMain;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderSendInfo;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.pojo.user.WXMSgTemplate;
import com.bh.admin.service.JDOrderService;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.admin.vo.HttpUtils;
import com.bh.config.Contants;
import com.bh.jd.api.JDStockApi;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.NewStock;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.StockParams;
import com.bh.jd.bean.order.Track;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@Service
public class JDOrderServiceImpl implements JDOrderService{
	
	//倒入商品的Mapper
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private MemberShopMapper memberShopMember;

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
	private OrderTeamMapper orderTeamMapper;

	@Autowired
	private OrderCollectionDocMapper orderCollectionDocMapper;
	
	@Autowired
	private MemberUserMapper memberUserMapper;
	
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	@Autowired
	private  JdOrderMainMapper jdOrderMainMapper;
	

	
	//6.26.2 批量获取库存接口（建议订单详情页、下单使用）
	public BhResult getNewStockById(String cartIds,String area,Member member) throws Exception{
		BhResult bhResult = null;
		List<String> cartIdList = JsonUtils.stringToList(cartIds);
		
		//将京东的商品和滨惠的商品分别检出来
		List<StockParams> jdIds = new ArrayList<>();
		//这是滨惠的商品
		List<GoodsCart> bhIds = goodsCartMapper.selectGoodsCartByBHOrJD(cartIdList, 0);
		
		//京东商品
		List<GoodsCart> goodsCartList = goodsCartMapper.selectGoodsCartByBHOrJD(cartIdList,1);
		if (goodsCartList.size() > 0) {
			for (GoodsCart goodsCart : goodsCartList) {
				Goods good = goodsMapper.selectByPrimaryKey(goodsCart.getgId());
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(goodsCart.getGskuid());
				if (good !=null) {
					Integer isJdGoods = good.getIsJd();
					StockParams param = new StockParams();
					param.setSkuId(String.valueOf(goodsSku.getJdSkuNo()));
					param.setNum(goodsCart.getNum());
					//是否是京东商品，0否，1是
					if (isJdGoods == 1) {
						jdIds.add(param);
					}
				}
			}
		}
		//购物车既有京东商品，又有滨惠商品
		StringBuffer name=new StringBuffer();
		if ((goodsCartList.size() > 0) && (bhIds.size() >0)) {
			for (int i = 0; i < goodsCartList.size(); i++) {
				if(goodsCartList.get(i).getIsStore()==1) {
					name.append(goodsCartList.get(i).getGoodName());
				}
			}
			for (int i = 0; i < bhIds.size(); i++) {
				if(bhIds.get(i).getIsStore()==1) {
					name.append(bhIds.get(i).getGoodName());
				}
			}
			String goodsName=name.substring(0, name.length()-1);
			//0代表有货，1代表无货
			int jdFlag = 0;
			// 查找京东的库存 返回值：200有货 ，201无货 ，400报错（参数不对，地址不对）
			BhResult bh  = getJDStockById(cartIds,area,jdIds);
			if (bh.getStatus() == 400) {
				bhResult = bh;
			}else{
				if (bh.getStatus() == 200) {
					jdFlag = 0;
				}else if (bh.getStatus() == 201) {
					jdFlag = 1;
				}
				int flag = getBHStockById(cartIds);
				int sum = jdFlag + flag;
				if (sum == 0) {
					bhResult = new BhResult(200, "商品全有货", null);
				}else {
					bhResult = new BhResult(400, "下手慢了~"+goodsName+"商品被抢空了,请重新下单 ", null);
				}
			}
		}
		
		//如果购物车中仅有京东的商品
		if ((bhIds.size()  <1) &&(jdIds.size() > 0)) {
			for (int i = 0; i < jdIds.size(); i++) {
				if(jdIds.get(i).getNum()==0) {
					GoodsSku goodsSku=goodsSkuMapper.selectByPrimaryKey(Integer.valueOf(jdIds.get(i).getSkuId()));
					name.append(goodsSku.getGoodsName());
				}
			}
			String goodsName=name.substring(0, name.length()-1);
			// 查找京东的库存 返回值：200有货 ，201无货 ，400报错（参数不对，地址不对）
			BhResult bh = getJDStockById(cartIds,area,jdIds);
			if (bh.getStatus() == 200) {
				bhResult = new BhResult(200, "商品全有货", null);
			}else if (bh.getStatus() == 201) {
				bhResult = new BhResult(400, "下手慢了~"+goodsName+"商品被抢空了,请重新下单 ", null);
			} else if (bh.getStatus() == 400) {
				bhResult = bh;
			}
		}
		//如果购物车中有滨惠的商品
		if ((bhIds.size() >0) &&(jdIds.size() <1)) {
			for (int i = 0; i < bhIds.size(); i++) {
				if(bhIds.get(i).getIsStore()==1) {
					name.append(bhIds.get(i).getGoodName());
				}
			}
			String goodsName=name.substring(0, name.length()-1);
			//0代表有货，1代表无货
			int flag = getBHStockById(cartIds);
			if (flag == 0) {
				bhResult = new BhResult(200, "商品全有货", null);
			}else if (flag ==1) {
				bhResult = new BhResult(400, "下手慢了~"+goodsName+"商品被抢空了,请重新下单 ", null);
			}
		}
		return bhResult;
		
	}
	
	//查找京东的库存 返回值：200有货 ，201无货 ，400报错
	public BhResult getJDStockById(String cartIds,String area,List<StockParams> jdIds){
		BhResult bhResult = null;
		int flag =-1;
		String skuNums = JsonUtils.objectToJson(jdIds);
		List<NewStock> list = new ArrayList<>();
		int size=0;
		JdResult<String> jdResult = null;
		if (area !=null) {
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
			//0代表有货，1代表无货
			for(int i=0;i<list.size();i++){
				int stockStateId = list.get(i).getStockStateId();
				//33 有货 现货-下单立即发货
				//39 有货 在途-正在内部配货，预计2~6天到达本仓库
				//40 有货 可配货-下单后从有货仓库配货
				//36 预订
				//34 无货
				if (stockStateId == 34) {
					flag =1;
					break;
				}else {
					flag =0;
				}
			}
			if (flag ==0) {
				bhResult = new BhResult(200,"有货", null);
			}else if (flag == 1) {
				bhResult = new BhResult(201,"无货货", null);
			}
		}else if(size ==0){
			bhResult = new BhResult(400,jdResult.getResultMessage() , null);
		}else if (size == -1) {
			bhResult = new BhResult(400, "该地址不是京东地址", null);
		}
		return bhResult;
	}
	
	
	//查找滨惠的库存
	public int getBHStockById(String cartIds){
		int flag =0;
		List<String> ids = JsonUtils.stringToList(cartIds);
		List<GoodsCart> list = goodsCartMapper.selectGoodsCart(ids);
		for(int i=0;i<list.size();i++){
			//Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getgId());
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(list.get(i).getGskuid());
			if (goodsSku !=null) {
				int num=0;
			    num = goodsCartMapper.selectTotalNum(ids, list.get(i).getgId());
				int store = goodsSku.getStoreNums() - num;
				if (store < 0) {
					flag =1;
					break;
				}
			}
		}
		return flag;
	}
	
	
	public Order selectOrderByIds(Order order) throws Exception{
		Order order1 = new Order();
		order1 = orderMapper.selectOrderBymId(order);
		return order1;
	}
	
	//当购物车的id为空时,需要通过Order 的id判断库存
	public BhResult getNewStockByOrderId(String orderId) throws Exception{
		StringBuffer name=new StringBuffer();
		BhResult bhResult = null;
		OrderSku orderSku = new OrderSku();
		orderSku.setOrderId(Integer.parseInt(orderId));
		List<OrderSku> list = orderSkuMapper.selectOrderShopBySelect(orderSku);
		for(int i=0;i<list.size();i++){
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(list.get(i).getSkuId());
			Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
			int isJd= good.getIsJd();
			//是否是京东商品，0否，1是
			if (isJd ==0) {
				if (goodsSku !=null) {
					int num=0;
				    num = list.get(i).getSkuNum();
					int store = goodsSku.getStoreNums() - num;
					if (store < 0) {
						name.append(goodsSku.getGoodsName());
					}
				}
			}
		}
		String goodsName=name.substring(0, name.length()-1);
		//0代表有货，1代表无货
		int flag = getBHStockByOrderId(orderId);
		if (flag == 0) {
			bhResult = new BhResult(200, "商品全有货", null);
		}else if (flag ==1) {
			bhResult = new BhResult(400, "下手慢了~"+goodsName+"商品被抢空了,请重新下单 ", null);
		}
		return bhResult;
		
	}
	
	//通过orderId 查找滨惠的库存
	public int getBHStockByOrderId(String orderId){
		//这是滨惠的商品
		OrderSku orderSku = new OrderSku();
		orderSku.setOrderId(Integer.parseInt(orderId));
		List<OrderSku> list = orderSkuMapper.selectOrderShopBySelect(orderSku);
		int flag =0;
		for(int i=0;i<list.size();i++){
			GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(list.get(i).getSkuId());
			Goods good = goodsMapper.selectByPrimaryKey(list.get(i).getGoodsId());
			int isJd= good.getIsJd();
			//是否是京东商品，0否，1是
			if (isJd ==0) {
				if (goodsSku !=null) {
					int num=0;
				    num = list.get(i).getSkuNum();
					int store = goodsSku.getStoreNums() - num;
					if (store < 0) {
						flag =1;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	//0下单成功   -1下单失败   -2不存在orderSku
	public int updateJDOrderId(Order order) throws Exception{
		Order order2 = orderMapper.selectByPrimaryKey(order.getId());
		MemberUserAddress memberUserAddress = memberUserAddressMapper.selectByPrimaryKey(order2.getmAddrId());
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(order2.getmId());
		if (memberUser.getEmail() == null) {
			memberUser.setEmail("872820897@qq.com");
		}
		List<OrderSku> orderSkuList = new ArrayList<>();
		OrderSku orderSkuParams = new OrderSku();
		orderSkuParams.setOrderId(order.getId());
		orderSkuList = orderSkuMapper.selectJDSkuId(orderSkuParams);
		
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
				String thirdOrder = orderShop.getShopOrderNo();//必须    第三方的订单单号
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
				 String email = memberUser.getEmail();//	Stirng	必须	邮箱
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
					OrderShop orderShop2 = new OrderShop();
					orderShop2.setId(orderShop.getId());
					orderShop2.setJdorderid(jdOrderId);
					orderShop2.setStatus(Contants.shopStatu3);
					orderShop2.setSendTime(new Date());
					orderShop2.setDeliveryWay(2);
					orderShopMapper.updateByPrimaryKeySelective(orderShop2);
					 //程凤云 2018-4-11添加
		        	 List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop.getOrderNo());
				     if (teamList.size()>0) {
				    	  WXMSgTemplate template = new WXMSgTemplate();
				    	  template.setOrderShopId(orderShop.getId()+"");
				    	 // wechatTemplateMsgServicel.sendGroupGoodTemplate(template);
				    	    wechatTemplateMsgService.sendGroupGoodTemplate(template);
					 }
					return -1;
				}else{
					OrderShop orderShop2 = new OrderShop();
					orderShop2.setId(orderShop.getId());
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
			//获取京东订单信息
			JdResult<OrderStock> ret = null;
			//某个商家订单的某个jdSkuId商品的子订单
			JdOrderMain jdOrderMain=jdOrderMainMapper.getJdOrderMainByJdSkuId(jdSkuId,orderShop1.getId());
			if(jdOrderMain!=null){
				ret = JDStockApi.orderTrack(jdOrderMain.getJdOrderId());
			}else{
				 ret = JDStockApi.orderTrack(orderShop1.getJdorderid());
			}
			
			if (ret.getSuccess()) {
				stock = ret.getResult();
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



}
