package com.bh.admin.service.impl;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.OrderTeamMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.WXMSgTemplate;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.admin.vo.TemplateMsgResult;
import com.bh.admin.vo.WechatTemplateMsg;
import com.bh.config.Contants;
import com.bh.config.WxTeamplate;
import com.bh.utils.HttpUtils;
import com.bh.utils.pay.JsonUtil;
import com.bh.utils.pay.WXPayUtil;

@Service
@Transactional
public class WechatTemplateMsgServiceImpl implements WechatTemplateMsgService {

	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	
	/**
	 * 发送打印销售单验证码
	 */
	@Override
	public TemplateMsgResult sendTemplate(String accessToken, String data) {
		// TODO Auto-generated method stub
		try {
		    TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken;
		    String result = HttpUtils.doPost(url,data);
		    Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
	        return templateMsgResult; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null; 
	}
    /**
     * 拼团成功
     */
	@Override
	public TemplateMsgResult sendGroupTemplate(String id, String orderId,String goodId) {
		   TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
		    System.out.println("id============>"+id);
		    System.out.println("orderId============>"+orderId);
		    System.out.println("goodId============>"+goodId);
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    Member  m= memberMapper.selectByPrimaryKey(Integer.valueOf(id));
		    Order order =  orderMapper.selectByPrimaryKey(Integer.valueOf(orderId));
		    List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		    Goods g = goodsMapper.selectByPrimaryKey(Integer.valueOf(goodId));
            TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            //根据具体模板参数组装  
            params.put("first",WechatTemplateMsg.item("【滨惠商城】恭喜您拼单成功!我们将以最快速度为您发货！请耐心等待哦！", "#000000"));  
            params.put("keyword1",WechatTemplateMsg.item(order.getOrderNo(), "#000000"));  
            params.put("keyword2",WechatTemplateMsg.item(g.getName(), "#000000"));  
 
            params.put("remark",WechatTemplateMsg.item(null, "#000000"));  
            WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
//          wechatTemplateMsg.setTemplate_id("9nuzcZht3lLWh0BucV500LidQczrRUiAl9104DcyCqs");  //模板ID 
            wechatTemplateMsg.setTemplate_id(WxTeamplate.t3);  //模板ID 
            wechatTemplateMsg.setTouser(m.getOpenid());  //openID
            wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShopList.get(0).getId());//模板跳转链接(订单详情页)
            wechatTemplateMsg.setData(params);  
            String data = JsonUtil.toJson(wechatTemplateMsg);
		    String result = HttpUtils.doPost(url,data);
		    Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
	        return templateMsgResult; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return templateMsgResult; 
	}
	
	/**
	 * 发送发起团成功消息模板
	 */
	@Override
	public TemplateMsgResult sendStartGroupTemplate(String orderNo) {
		TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    Order order =  orderMapper.getOrderByOrderNo(orderNo);
		    List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		    List<OrderSku> skuList = orderSkuMapper.getByOrderId(order.getId());
		    OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderNo);
		    Member  m= memberMapper.selectByPrimaryKey(order.getmId());
		    Goods g = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
            TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            //根据具体模板参数组装  
            int num = g.getTeamNum()-1;
            String str ="【滨惠商城】恭喜您成功发起了拼单！还差"+num+"个人即可拼单成功哦，赶紧邀请好友来拼单吧！（订单编号："+orderNo+"）";
            params.put("first",WechatTemplateMsg.item(str, "#000000"));  
            params.put("keyword1",WechatTemplateMsg.item(g.getName(), "#000000"));  
            params.put("keyword2",WechatTemplateMsg.item((double)skuList.get(0).getSkuSellPriceReal()/100+"", "#000000"));
            //2018.5.24 zlk
            if(StringUtils.isNotBlank(m.getUsername())) {
            	m.setUsername(URLDecoder.decode(m.getUsername(), "utf-8"));
            }
            //end
            params.put("keyword3",WechatTemplateMsg.item(m.getUsername(), "#000000"));
            params.put("keyword4",WechatTemplateMsg.item(g.getTeamNum()+"", "#000000"));
            params.put("keyword5",WechatTemplateMsg.item(formatter.format(orderTeam.getEndTime())+"", "#000000"));

            params.put("remark",WechatTemplateMsg.item(null, "#000000"));  
            WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
//          wechatTemplateMsg.setTemplate_id("0k-_h01HdXqXX1GLda4j7jVY2fOkDFRjoe0nYI-Vm0Y");  //模板ID
            wechatTemplateMsg.setTemplate_id(WxTeamplate.t1);  //模板ID
            wechatTemplateMsg.setTouser(m.getOpenid());  //openID
            wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShopList.get(0).getId());//模板跳转链接(订单详情页)
            wechatTemplateMsg.setData(params);  
            String data = JsonUtil.toJson(wechatTemplateMsg);
		    String result = HttpUtils.doPost(url,data);
		    Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
	        return templateMsgResult; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return templateMsgResult;
	}
	
	
	/**
	 * @Description: 微信公众号拼单商品发货提醒
	 * @author chengc
	 * @date 2018年4月10日 下午18:29:54
	 */
	@Override
	public TemplateMsgResult sendGroupGoodTemplate(WXMSgTemplate template) {
		 TemplateMsgResult templateMsgResult1 = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
			OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(template.getOrderShopId()));
		    System.out.println("orderShopId============>"+template.getOrderShopId());
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    Member  m= memberMapper.selectByPrimaryKey(orderShop.getmId());
		    String shopName=memberShopMapper.selectShopName(orderShop.getShopId());
		    List<OrderSku> orderSkus = orderSkuMapper.getOrderSkuByOrderShopId(Integer.parseInt(template.getOrderShopId()));
		   if (orderSkus.size()>0) {
			     for (OrderSku orderSku : orderSkus) {
			    	    //zlk 2018.6.19
			    	    if(orderSku.getRefund()!=0) {
			    	    	continue;
			    	    }
			    	    //end
			    	    TemplateMsgResult templateMsgResult = new TemplateMsgResult();
			    	    Goods g = goodsMapper.selectByPrimaryKey(orderSku.getGoodsId());
				        Date currentTime = new Date();
				        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        String dateString = formatter.format(currentTime);
					    
					    TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
						// 根据具体模板参数组装
						params.put("first", WechatTemplateMsg.item("【滨惠商城】您的商品已发货啦！", "#000000"));
						params.put("keyword1", WechatTemplateMsg.item(orderShop.getOrderNo(), "#000000"));
						params.put("keyword2", WechatTemplateMsg.item(g.getName(), "#000000"));
						//供应商
						params.put("keyword3", WechatTemplateMsg.item(shopName, "#000000"));
						params.put("keyword4", WechatTemplateMsg.item(dateString, "#000000"));
						params.put("remark", WechatTemplateMsg.item("请登陆个人中心，进入订单列表查看物流信息哦！", "#000000"));
						WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
//						wechatTemplateMsg.setTemplate_id("arHgvM4wcWQ-Cc7WF8ZGxCyilL-Ka5o0UxkvHsckeWY");//模板ID
						wechatTemplateMsg.setTemplate_id(WxTeamplate.t6);//模板ID
						wechatTemplateMsg.setTouser(m.getOpenid()); // openID
						wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+template.getOrderShopId());//模板跳转链接(订单详情页)
						wechatTemplateMsg.setData(params); 
						String data = JsonUtil.toJson(wechatTemplateMsg);
						String result = HttpUtils.doPost(url, data);
						Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
						templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
						templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
						templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
						//return templateMsgResult;
				}
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return templateMsgResult1;
	}
	
	
	/**
	 * 给团主发送某某加入团成功消息模板
	 */
	@Override
	public TemplateMsgResult sendJoinGroupTemplate(String orderNo) {
		TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    Order order =  orderMapper.getOrderByOrderNo(orderNo);
		    List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		    List<OrderSku> skuList = orderSkuMapper.getByOrderId(order.getId());
		    OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderNo);
		    OrderTeam orderTeamOne = orderTeamMapper.getByTeamNoAndOwner(orderTeam.getTeamNo());
		    Member  m= memberMapper.selectByPrimaryKey(order.getmId());
		    Member mOne = memberMapper.selectByPrimaryKey(orderTeamOne.getmId());
		    Goods g = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
            TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            //根据具体模板参数组装  
            //程凤云
            int count = 0;
            count = orderTeamMapper.groupCount(orderTeam.getTeamNo());
            int num = g.getTeamNum()-count;
            String str =null;
            //2018/5/24 zlk
            if(StringUtils.isNotBlank(m.getUsername())){
            	m.setUsername(URLDecoder.decode(m.getUsername(), "utf-8"));
            }
            //end
            if(num==0){//拼单成功
            	 str = "【滨惠商城】用户"+m.getUsername()+"参与了您的拼单商品，已拼单成功哦！（订单编号："+orderNo+"）";
            }else{
            	 str = "【滨惠商城】用户"+m.getUsername()+"参与了您的拼单商品，还差"+num+"个人即可拼单成功哦，赶紧邀请好友来拼单吧！（订单编号："+orderNo+"）";
            }
            params.put("first",WechatTemplateMsg.item(str, "#000000"));  
            params.put("keyword1",WechatTemplateMsg.item(g.getName(), "#000000"));  
            params.put("keyword2",WechatTemplateMsg.item((double)skuList.get(0).getSkuSellPriceReal()/100+"", "#000000"));
            params.put("keyword3",WechatTemplateMsg.item(formatter.format(orderTeam.getEndTime())+"", "#000000"));
           
 
            params.put("remark",WechatTemplateMsg.item(null, "#000000"));  
            WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
//          wechatTemplateMsg.setTemplate_id("Q1X1rM2VBff6xBzMC4Y_XYVCKpqGxfnKzi3wBXL4nFs");  //模板ID
            wechatTemplateMsg.setTemplate_id(WxTeamplate.t5);  //模板ID
            wechatTemplateMsg.setTouser(mOne.getOpenid());  //openID
            wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShopList.get(0).getId());//模板跳转链接(订单详情页)
            wechatTemplateMsg.setData(params);  
            String data = JsonUtil.toJson(wechatTemplateMsg);
		    String result = HttpUtils.doPost(url,data);
		    Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
	        return templateMsgResult; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return templateMsgResult;
	}
	
	
	/**
	 * 给参团人发送参团成功消息模板
	 */
	@Override
	public TemplateMsgResult sendJoinedGroupTemplate(String orderNo) {
		TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    Order order =  orderMapper.getOrderByOrderNo(orderNo);
		    List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
		    List<OrderSku> skuList = orderSkuMapper.getByOrderId(order.getId());
		    OrderTeam orderTeam = orderTeamMapper.getByOrderNo(orderNo);
		    OrderTeam orderTeamOne = orderTeamMapper.getByTeamNoAndOwner(orderTeam.getTeamNo());
		    Member  m= memberMapper.selectByPrimaryKey(order.getmId());
		    Member mOne = memberMapper.selectByPrimaryKey(orderTeamOne.getmId());
		    Goods g = goodsMapper.selectByPrimaryKey(skuList.get(0).getGoodsId());
            TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            //根据具体模板参数组装  
            //程凤云
            int count = 0;
            count = orderTeamMapper.groupCount(orderTeam.getTeamNo());
            int num = g.getTeamNum()-count;
            String str =null;
            //2018.5.24 zlk
            if(StringUtils.isNotBlank(m.getUsername())){
            	m.setUsername(URLDecoder.decode(m.getUsername(), "utf-8"));          
            }
            if(StringUtils.isNotBlank(mOne.getUsername())){           	
            	mOne.setUsername(URLDecoder.decode(mOne.getUsername(), "utf-8"));
            }
            //end
            if(num==0){//拼单成功
            	 str= "【滨惠商城】用户"+m.getUsername()+"您参与了"+mOne.getUsername()+"发起的拼团，已拼单成功（订单编号："+orderNo+"）";
            }else{
            	 str= "【滨惠商城】用户"+m.getUsername()+"您参与了"+mOne.getUsername()+"发起的拼团，还差"+num+"个人即可拼单成功哦，赶紧邀请好友来拼单吧！（订单编号："+orderNo+"）";
            }
            params.put("first",WechatTemplateMsg.item(str, "#000000"));  
            params.put("keyword1",WechatTemplateMsg.item(g.getName(), "#000000"));  
            params.put("keyword2",WechatTemplateMsg.item((double)skuList.get(0).getSkuSellPriceReal()/100+"", "#000000"));
            params.put("keyword3",WechatTemplateMsg.item(formatter.format(orderTeam.getEndTime())+"", "#000000"));
           
 
            params.put("remark",WechatTemplateMsg.item(null, "#000000"));  
            WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
            wechatTemplateMsg.setTemplate_id("Q1X1rM2VBff6xBzMC4Y_XYVCKpqGxfnKzi3wBXL4nFs");  //模板ID 
            wechatTemplateMsg.setTouser(m.getOpenid());  //openID
            wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShopList.get(0).getId());//模板跳转链接(订单详情页)
            wechatTemplateMsg.setData(params);  
            String data = JsonUtil.toJson(wechatTemplateMsg);
		    String result = HttpUtils.doPost(url,data);
		    Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
	        return templateMsgResult; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return templateMsgResult;
	}

	/**
	 * @Description: 微信公众号拼单商品收货提醒
	 * @author cheng
	 * @date 2018年4月10日 下午18:29:54
	 */
	@Override
	public TemplateMsgResult receiGroupGoodsGoodTemplate(WXMSgTemplate template) {
		   TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
		    System.out.println("orderShopId============>"+template.getOrderShopId());
		    String url= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+String.valueOf(map3.get("access_token"));
		    //Member  m= memberMapper.selectByPrimaryKey(Integer.parseInt(template.getOrderShopId()));
		    OrderShop orderShop = orderShopMapper.selectByPrimaryKey(Integer.parseInt(template.getOrderShopId()));
		    Member  m= memberMapper.selectByPrimaryKey(orderShop.getmId());
		    Order order =  orderMapper.selectByPrimaryKey(orderShop.getOrderId());
		    List<OrderSku> orderSkus = orderSkuMapper.getOrderSkuByOrderId(orderShop.getOrderId());
		    Goods g = goodsMapper.selectByPrimaryKey(orderSkus.get(0).getGoodsId());
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    
		    
		    TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
			// 根据具体模板参数组装
			params.put("first", WechatTemplateMsg.item("【滨惠商城】您的拼单商品已收货！", "#000000"));
			//订单号
			params.put("keyword1", WechatTemplateMsg.item(order.getOrderNo(), "#000000"));
			//商品的名称
			params.put("keyword2", WechatTemplateMsg.item(g.getName()+"", "#000000"));
			//下单时间
			params.put("keyword3", WechatTemplateMsg.item(formatter.format(order.getAddtime())+"", "#000000"));
			//发货时间
			params.put("keyword4", WechatTemplateMsg.item(formatter.format(orderShop.getSendTime())+"", "#000000"));
			//收货时间
			params.put("keyword5", WechatTemplateMsg.item(formatter.format(orderShop.getReceivedtime())+"", "#000000"));
			params.put("remark", WechatTemplateMsg.item("请登陆个人中心，进入订单列表进行评价哦！", "#000000"));
			WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
//			wechatTemplateMsg.setTemplate_id("hKDgdZrNim9Orrzs4nqRerCr_hQI8jPnkmMsFUWoOBo");//模板ID
			wechatTemplateMsg.setTemplate_id(WxTeamplate.t7);//模板ID
			wechatTemplateMsg.setTouser(m.getOpenid()); // openID
			wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShop.getId());//模板跳转链接(订单详情页)
			wechatTemplateMsg.setData(params); 
			String data = JsonUtil.toJson(wechatTemplateMsg);
			String result = HttpUtils.doPost(url, data);
			Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
			return templateMsgResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return templateMsgResult; 
	}

	/**
	 * @Description: 微信公众号发送拼团失败的推送消息
	 * @author xieyc
	 * @date 2018年4月10日 下午18:29:54
	 */
	public TemplateMsgResult sendTeamFailTemplate(String opendId,String goodsName,double refundMoney,String orderNo,double goodsPrice) {
		TemplateMsgResult templateMsgResult = new TemplateMsgResult();
		try {
			Map<String, Object> map3 = WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
			String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
					+ String.valueOf(map3.get("access_token"));
			TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
			Order order =  orderMapper.getOrderByOrderNo(orderNo);
			List<OrderShop> orderShopList = orderShopMapper.getByOrderId(order.getId());
			
			// 根据具体模板参数组装
			params.put("first", WechatTemplateMsg.item("【滨惠商城】您的订单号为："+orderNo+"。由于拼单人数不足，未能在规定时间内拼单成功，可进入网站重新发起拼单哦！", "#000000"));
			params.put("keyword1", WechatTemplateMsg.item(goodsName, "#000000"));
			params.put("keyword2", WechatTemplateMsg.item(goodsPrice+"元", "#000000"));
			params.put("keyword3", WechatTemplateMsg.item(refundMoney+"元", "#000000"));
			params.put("remark", WechatTemplateMsg.item("已退款，请注意查收！", "#000000"));
			WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();
//			wechatTemplateMsg.setTemplate_id("1TW3Q-TGBG6Ws721zDXHBlqyY8x-z1kt6fkJvRNXjpk");//模板ID
			wechatTemplateMsg.setTemplate_id(WxTeamplate.t2);//模板ID
			wechatTemplateMsg.setTouser(opendId); // openID
			wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/orderdetail?orderItemId="+orderShopList.get(0).getId());//模板跳转链接(订单详情页)
			wechatTemplateMsg.setData(params); 
			String data = JsonUtil.toJson(wechatTemplateMsg);
			String result = HttpUtils.doPost(url, data);
			Map<String, Object> resultMap = JsonUtil.fromJson(result, HashMap.class);
			templateMsgResult.setErrcode(String.valueOf(resultMap.get("errcode")));
			templateMsgResult.setErrmsg(String.valueOf(resultMap.get("errmsg")));
			templateMsgResult.setMsgid(String.valueOf(resultMap.get("msgid")));
			return templateMsgResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return templateMsgResult;
	}


}
