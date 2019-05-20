package com.bh.admin.service.impl;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.order.BhDictItemMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderRefundDocMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.mapper.user.MemberUserAddressMapper;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.BhDictItem;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.service.PrintsService;
import com.bh.admin.util.PrintUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Service
@Transactional
public class PrintsServiceImpl implements PrintsService{

	@Autowired
	private OrderShopMapper shopmapper; 
	
	@Autowired
	private OrderSkuMapper skumapper; 
	
	@Autowired
	private OrderMapper ordermapper; 
	
	@Autowired
	private MemberUserAddressMapper muamapper; 
	
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; 
	
	@Autowired
	private MemberShopMapper memberShopMapper; 
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;
	@Autowired
	private BhDictItemMapper bhDictItemMapper;
	//通过orderNo查找orderTeam
	public List<OrderTeam> selectOrderTeam(String orderNo) throws Exception{
		List<OrderTeam> teams = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderNo);
		return teams;
	}
	
	@Override
	public OrderShop getOrderById(String id,Integer shopId) {
		// TODO Auto-generated method stub
		//return shopmapper.selectByPrimaryKey(Integer.parseInt(id));
		OrderShop orderShop = new OrderShop();
		orderShop.setShopOrderNo(id); //商家订单号
		orderShop.setShopId(shopId);
		return shopmapper.getByOrderNo(orderShop);
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

	@Override
	public MemberShop getMemberShopById(Integer id) {
		// TODO Auto-generated method stub
		return memberShopMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderShop record) {
		// TODO Auto-generated method stub
		return shopmapper.updateByPrimaryKey(record);
	}

	@Override
	public void updateExpressByOrderNo(Order record) {
		// TODO Auto-generated method stub
		ordermapper.updateExpressByOrderNo(record);
	}
	
	@Override
	public void updateExpressByOrderNo1(OrderShop record) {
		// TODO Auto-generated method stub
		ordermapper.updateExpressByOrderNo1(record);
	}
	

	@Override
	public int updatePrintByPrimaryKey(OrderShop record) {
		// TODO Auto-generated method stub
		return shopmapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public OrderShop getByOrderNo(String orderNo,Integer shopId) {
		// TODO Auto-generated method stub
		OrderShop o = new OrderShop();
		o.setOrderNo(orderNo);
		o.setShopId(shopId);
		return shopmapper.getByOrderNos(o);
	}


	private static HttpClient getHttpClient(int port){
		PoolingClientConnectionManager pcm = new PoolingClientConnectionManager();
		SSLContext ctx=null;
		try{
			ctx = SSLContext.getInstance("TLS");
			X509TrustManager x509=new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers(){
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{x509}, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme sch = new Scheme("https", port, ssf);
		pcm.getSchemeRegistry().register(sch);
		return new DefaultHttpClient(pcm);
	}
	@Override
	public Map<String,Object> getCode(Map<String,Object> mp){
		Long expressNo=null;
		Map<String,Object> map = null ;
		try {
		    //参数 begin
		    String url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";//顺丰内网地址
		    int port = 443;
		
		    String checkword = "7JOWlP6QKUmdI6N5pi3jovH0zjHjAmYH";
		    //参数 end  
			String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
					"<Request service='OrderService' lang='zh-CN'>\r\n" + 
					"<Head>7681844305</Head>\r\n" + 
					"<Body>\r\n" + 
					"<Order orderid ='"+mp.get("orderNo")+"'\r\n" + 
					"j_company='"+mp.get("j_address")+"'\r\n" + 
					"j_contact='蔡先生' j_tel='"+mp.get("j_tel")+"' j_mobile=''\r\n" + 
					"j_province='"+mp.get("j_province")+"' j_city='"+mp.get("j_city")+"' j_county='"+mp.get("j_county")+"'\r\n" + 
					"j_address='"+mp.get("j_address")+"'\r\n" + 
					"d_company=''\r\n" + 
					"d_contact='"+mp.get("d_contact")+"' d_tel='"+mp.get("d_tel")+"' d_mobile=''\r\n" + 
					"d_province='"+mp.get("d_province")+"' d_city='"+mp.get("d_city")+"' d_county='"+mp.get("d_county")+"'\r\n" + 
					"d_address='"+mp.get("d_address")+"'\r\n" + 
					"express_type ='1'\r\n" + 
					"pay_method ='2' custid ='7681844305'\r\n" + 
					"parcel_quantity ='1' cargo_total_weight ='2.35' sendstarttime =''\r\n" + 
					"order_source ='' remark =''>\r\n" + 
					"<Cargo Name='' count='2' unit='̨' weight='0.02' amount='100' currency='CNY' source_area=''></Cargo>\r\n" + 
					"</Order>\r\n" + 
					"</Body>\r\n" + 
					"</Request>";
		    
		    String verifyCode=PrintUtil.md5EncryptAndBase64(xml + checkword);
		
		    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		    nvps.add(new BasicNameValuePair("xml", xml));
		    nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		
		    HttpClient httpclient=getHttpClient(port);
		    HttpPost httpPost = new HttpPost(url);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		    HttpResponse response = null;
		    try {
		   	     response = httpclient.execute(httpPost);
		    } catch (ClientProtocolException e) {
			     // TODO Auto-generated catch block
			     e.printStackTrace();
		    } catch (IOException e) {
			    // TODO Auto-generated catch block
			     e.printStackTrace();
		    }
		
		    if (response.getStatusLine().getStatusCode() == 200){
			   //System.out.println("================succeess"+EntityUtils.toString(response.getEntity()));
			   org.json.JSONObject xmlJSONObj = null;
		       try {
				   xmlJSONObj = XML.toJSONObject(EntityUtils.toString(response.getEntity()));
			   } catch (JSONException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   } catch (ParseException e) {
				  // TODO Auto-generated catch block
				   e.printStackTrace();
			   } catch (IOException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
		 	   }  
//		       System.out.println("========================="+mp.toString());  
//		       System.out.println("sql5"+xmlJSONObj.toString());  
			   expressNo = (Long) xmlJSONObj.getJSONObject("Response").getJSONObject("Body").getJSONObject("OrderResponse").get("mailno");
//			   Object destcode =  xmlJSONObj.getJSONObject("Response").getJSONObject("Body").getJSONObject("OrderResponse").get("destcode");
//			   Object origincode =  xmlJSONObj.getJSONObject("Response").getJSONObject("Body").getJSONObject("OrderResponse").get("origincode");
			   map = new HashMap<String,Object>();
//			   map.put("destcode", destcode);
//			   map.put("origincode", origincode);
			   map.put("expressNo", expressNo);
		    } else {
              //EntityUtils.consume(response.getEntity());
			  //System.out.println("============response status error: " + response.getStatusLine().getStatusCode());
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
		
	}

	@Override
	public String getTaobao(Map<String, Object> mp) {

		JSONObject o = new JSONObject();
		JSONObject cainiao_waybill_ii_get_response = new JSONObject();
		JSONObject modules = new JSONObject();
		
		JSONArray waybill_cloud_print_response = new JSONArray();
		JSONObject kong = new JSONObject();
		JSONObject print_data = new JSONObject();
		JSONObject object_id = new JSONObject();
		//JSONObject waybill_code = new JSONObject();
		JSONObject data = new JSONObject();

		
		JSONObject recipient = new JSONObject();
		JSONObject address = new JSONObject();
		
		address.put("city", mp.get("d_city"));
		address.put("detail", mp.get("d_address"));
		address.put("district", mp.get("d_county"));
		address.put("province", mp.get("d_province"));
		address.put("town",mp.get("d_four"));
		
		data.put("cpCode", mp.get("code"));
		data.put("needEncrypt", false);
		data.put("parent", false);
		
		recipient.put("address", address);
		recipient.put("mobile", "");
		recipient.put("name", mp.get("d_contact"));
		recipient.put("phone", mp.get("d_tel"));
    	//recipient.put("address", address);
		
		data.put("recipient", recipient);
		
		JSONObject routingInfo = new JSONObject();
		JSONObject consolidation = new JSONObject();
		consolidation.put("code", "991001");
		
		JSONObject origin = new JSONObject(); 
		origin.put("code", "754004");
		origin.put("name", "广东省潮州市");
		
		JSONObject sortation = new JSONObject();
		sortation.put("name", "");
		
		routingInfo.put("consolidation", consolidation);
		routingInfo.put("origin", origin);
		routingInfo.put("routeCode", "");
		routingInfo.put("sortation", sortation);
		data.put("routingInfo", routingInfo);
		
		JSONObject sender = new JSONObject();
		JSONObject addres = new JSONObject();
		addres.put("city", "潮州市");
		addres.put("detail", "绿榕南路绿榕楼");
		addres.put("district", "枫溪区");
		addres.put("province", "广东省");

		sender.put("address", addres);
		sender.put("mobile", mp.get("orderNo"));
		sender.put("name", mp.get("j_contact"));
		sender.put("phone", mp.get("j_tel"));
		data.put("sender", sender);
		
		
		JSONObject shippingOption = new JSONObject();
		shippingOption.put("code", "STANDARD_EXPRESS");
		shippingOption.put("title", "标准快递");
		data.put("shippingOption", shippingOption);
		data.put("waybillCode", mp.get("expressNo"));
		
		object_id.put("object_id", 1);
		print_data.put("data", data);
		print_data.put("signature", "MD:nMa3dYccRsjbm1dPBFYQdw==");
		print_data.put("templateURL",mp.get("templateURL"));
		
		kong.put("object_id", 1);
		kong.put("print_data", " "+print_data+" ");
		kong.put("waybill_code", mp.get("expressNo"));
		
		waybill_cloud_print_response.add(kong);
		
		modules.put("waybill_cloud_print_response", waybill_cloud_print_response);
		
		cainiao_waybill_ii_get_response.put("modules", modules);
		cainiao_waybill_ii_get_response.put("request_id", "2isb13m7f2ki");
		o.put("cainiao_waybill_ii_get_response", cainiao_waybill_ii_get_response);
		cainiao_waybill_ii_get_response= null;
		modules = null;
		waybill_cloud_print_response = null;
		kong= null;
		print_data = null;
		data = null;
		sender = null;
		addres = null;
		object_id = null;
		shippingOption = null;
		routingInfo = null;
		consolidation = null;
		origin = null;
		sortation = null;
		recipient = null;
		address = null;
		return o.toString();
	}

	@Override
	public List<OrderRefundDoc> getOrderRefundDoc(Integer id) {
		// TODO Auto-generated method stub
		return orderRefundDocMapper.getByOrderSkuId(id);
	}

	/**
	 * 
	 * @Description:快递公司信息查询     
	 * @author xieyc
	 * @date 2018年9月12日 下午5:44:50
	 * @param
	 * @return
	 *
	 */
	public List<BhDictItem> getExpressInfo() {
		return bhDictItemMapper.getListByDicId(1);
	}

	
}
