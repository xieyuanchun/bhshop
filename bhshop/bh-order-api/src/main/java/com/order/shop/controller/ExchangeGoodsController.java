package com.order.shop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.MyGoodsPojo;
import com.bh.jd.api.JDGoodsApi;
import com.bh.order.pojo.ExchangeGood;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.EmojiFilter;
import com.bh.utils.HttpUtils;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.pay.HttpService;
import com.google.gson.Gson;
import com.order.shop.service.ExchangeGoodsService;
import com.order.shop.service.ExchangeSkuService;
import com.order.user.service.PayCallbackService;
import com.order.user.service.SimpleOrderService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;
import com.order.vo.MyGoodsSku;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/exchangeGoods")
public class ExchangeGoodsController {

	@Autowired
	private ExchangeGoodsService service;
	@Autowired
	private SimpleOrderService simpleOrderService;
	@Autowired
	private PayCallbackService payCallbackService;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private ExchangeSkuService exchangeSkuService;
	@RequestMapping("/exchange")
	@ResponseBody
	public BhResult exchange(@RequestBody Map<String, String> map) {
		BhResult b = null;
		try {
			String phone = map.get("phone");// 收取密码号码
			String code = map.get("code");
			String tel = map.get("tel"); // 收货联系号码
			String receiver = map.get("receiver"); // 收货人
			String region = map.get("region");// 详细地址
			String adress = map.get("address");// 详细地址

			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("phone", phone);
			map2.put("code", code);
			String jsonString = HttpService.doPostJson("", map2);
			JSONObject json = JSONObject.fromObject(jsonString);
			json.get("sku");
			b = new BhResult(200, "下单成功", null);
		} catch (Exception e) {
			b = new BhResult(400, "密码错误", null);
			e.printStackTrace();
		}
		return b;
	}

	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/check")
	@ResponseBody
	public BhResult check(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		BhResult b = null;
		try {
			HttpSession session = request.getSession(false);
		    Member m = new Member();
			if(session!=null) {
			     m = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			if(m!=null&&!m.equals("")&&m.getId()!=null) {
			   //白名单限制
			   JedisUtil jedisUtil= JedisUtil.getInstance();  
			   JedisUtil.Strings strings=jedisUtil.new Strings();
			   String testMemberId = strings.get("testMemberId");				
			   if(StringUtils.isNotBlank(testMemberId)) {
				   if(!testMemberId.equals(",")) {
					   String temp="," +testMemberId + ",";
					   if(!temp.contains("," +m.getId() + ",")){
						   return b = new BhResult(400, "该模块未开发", "该模块未开发");
					   }
				   }
			   }
			   
			   String prov = (String) map.get("prov");// 省
			   String city = (String) map.get("city");// 市
			   String area = (String) map.get("area");// 区
			   
			   
			   String phone = (String) map.get("phone");// 领取手机号码
			   String code = (String) map.get("code");// 领取密码
		       //验证手机号密码是否正确 
			   String str = getCheck(phone, code);
			   Object object = JsonUtils.stringToObject(str);
			   String flag = ((JSONObject) object).get("success").toString();
			   //String flag = "true"; true 是可以领取
			   if ("true".equals(flag)) {
				  String type = ((JSONObject) object).get("type").toString();
				  //type 1 是兑换京东商品 ，type 2 是兑换优惠劵		
                  if(type.equals("1")) {
                	    //根据兑换组id  查找当前兑换组的京东商品
              	        List<ExchangeGood> egList = exchangeSkuService.get(Integer.valueOf(((JSONObject) object).get("sku").toString()));
              	        int stock = 0;
              	        for(int i=0;i<egList.size();i++) {
              			
              			   String Stock = JDGoodsApi.getStockById(String.valueOf(egList.get(i).getSkuId()),prov+"_"+city+"_"+area);
              			   JSONObject json = JSONObject.fromObject(Stock);
              			   JSONArray array = JSONArray.fromObject(json.get("result"));
              			 
              	           if(json.get("success").toString().equals("true")) {
              			 	    continue;
              	  		   }
              	           //判断京东商品是否有库存  33,40 。
              			   if(Integer.valueOf(array.getJSONObject(0).get("state").toString())==33
              				    || Integer.valueOf(array.getJSONObject(0).get("state").toString())==40) {
                      	    
              				   JSONObject jsonValue = JSONObject.fromObject(egList.get(i).getValue());
                      	       JSONArray array1 = JSONArray.fromObject(jsonValue.get("url"));
                      	       MyGoodsSku mg = new MyGoodsSku();
      				           mg.setImage(array1.get(i).toString());
      				           mg.setValue(egList.get(i).getKeyOne() + ":" + egList.get(i).getValueOne());
      				           mg.setName(egList.get(i).getName());
      				           mg.setJdSkuNo(String.valueOf(egList.get(i).getSkuId()));
      				           map.put("goods", mg);
      				           map.put("mId", m.getId());
      				           map.put("type", type);
      				           stock = 1;
      				           continue;
              			   }
              	        }
              	        //当前兑换组是商品都没有库存，就默认用第一个兑换
                	    if(stock==0) {
           				   JSONObject jsonValue = JSONObject.fromObject(egList.get(0).getValue());
                  	       JSONArray array1 = JSONArray.fromObject(jsonValue.get("url"));
                  	       MyGoodsSku mg = new MyGoodsSku();
  				           mg.setImage(array1.get(0).toString());
  				           mg.setValue(egList.get(0).getKeyOne() + ":" + egList.get(0).getValueOne());
  				           mg.setName(egList.get(0).getName());
  				           mg.setJdSkuNo(String.valueOf(egList.get(0).getSkuId()));
  				           map.put("goods", mg);
  				           map.put("mId", m.getId());
  				           map.put("type", type);
                	    }
                	  
				        b = new BhResult(200, "密码正确", map);
                  }else if(type.equals("2")){
                	    Coupon c = new Coupon();
                	    c.setId(Integer.valueOf(((JSONObject) object).get("coupon").toString()));
                	    //c.setId(202); 
                	    Coupon c1 = service.getById(c);
                	    c1.setStock(c1.getStock()-1);
                	    c1.setIsGet(c1.getIsGet()+1);
                	    if(c1.getStock()==0) {
                	    	return b = new BhResult(400, "优惠劵已领取完", "当前优惠劵已领取完");
                	    }
         			    CouponLog couponLog = new CouponLog();
        			    couponLog.setmId(m.getId());//当前领取的客户的id
        			    couponLog.setCouponId(c1.getId());//CouponId
        			    couponLog.setCreateTime(new Date());//当前的领取时间
        			    Date expireTime=null;	
        				Calendar calendar = Calendar.getInstance();
        		        calendar.setTime(new Date());
        				if(c1.getPeriodDay()!=-1){//-1表示永久有效
        			        calendar.add(Calendar.DATE, c1.getPeriodDay());
        				}else{
        					calendar.add(Calendar.DATE,36000);
        				}
        				expireTime=calendar.getTime();//自领取后多少天过期的时间
        			    couponLog.setExpireTime(expireTime);//过期时间
        			    long difference = (couponLog.getExpireTime().getTime() - new Date().getTime()) / 86400000;//计算过期时间天数
        				if (Math.abs(difference) > 10000) {//永久劵的默认生成的过期时候是领取后的36000天
        					couponLog.setEffectiveTime("永久有效");
        				}
        			    couponLog.setUseTime(new Date());//使用时间为当前时间，如果使用时间和领取时间一致，就说明没使用过
        			    couponLog.setShopId(c1.getShopId()); //商家id
        			    couponLog.setStatus(0);
        			    couponLog.setGetWay(1); //获取途径
        			    service.add(couponLog);
        			    
        			    
        			    couponMapper.updateByPrimaryKeySelective(c1);
        			    
        			    couponLog.setCouponType(c1.getCouponType());//类型
        			    couponLog.setTypeStr(strType(c1.getCouponType()));//优惠卷类型
        				couponLog.setAmount2((double)c1.getAmount()/100+"");//优惠卷金额
        				couponLog.setNeedAmount((double)c1.getNeedAmount()/100 + "");//满多少才能消费
        				if (c1.getCatId() == 0) {
        					couponLog.setApplyStr("全场通用");
        					if(c1.getCouponType()==2){//免邮卷
        						couponLog.setApplyName("任意订单免除邮费");
        					}else {
        						couponLog.setApplyName("任意商品进行抵扣");
        					}
        				} else {
        					GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(c1.getCatId());
        					couponLog.setApplyStr(goodsCategory.getName());
        					couponLog.setApplyName("仅限"+goodsCategory.getName()+"商品使用");
        				}
        				Map<String, Object> map2 = new HashMap<String, Object>();
        				map2.put("amount", couponLog.getAmount2());
        				map2.put("need_amount", couponLog.getNeedAmount());
        				map2.put("applyStr", couponLog.getApplyStr());
        				map2.put("typeStr", couponLog.getTypeStr());
        				map2.put("applyName", couponLog.getApplyName());
        				map2.put("couponType", couponLog.getCouponType());
        				map2.put("title", c1.getTitle());
        				if(StringUtils.isBlank(couponLog.getEffectiveTime())) {
        				     map2.put("expireTime", couponLog.getExpireTime());
        				}else {
        				     map2.put("expireTime", couponLog.getEffectiveTime());
        				}
        				map.put("couponLog", map2);
        				map.put("type", type);
        			    b = new BhResult(200, "密码正确", map);
                  }
			   } else {
				  b = new BhResult(400, "错误", ((JSONObject) object).get("text").toString());
			   }
		   }else {
			   return b = new BhResult(100, "请登录", null);
		   }
		} catch (Exception e) {
			b = new BhResult(400, "错误", null);
			e.printStackTrace();
		}
		return b;
	}

	// 设置,添加收货地址
	@RequestMapping(value = "/insertaddress", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertaddress(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			Member member1 = service.getById(Integer.valueOf(map.get("mId")));
			String fullName = map.get("receiver");// 收件人的名称
			String prov = map.get("prov");// 省
			String city = map.get("city");// 市
			String area = map.get("area");// 区

			String address = map.get("address");
			String tel = map.get("tel");// 移动电话
			String label = map.get("label");// 标签
			String telephone = map.get("phone");// 固定电话
			String isJd = "1";// 是否是京东地址，0否，1是
			String four = map.get("four");// 如果没有第四级就传0

			if (StringUtils.isEmpty(fullName)) {
				return new BhResult(400, "收货人不能为空", null);
			}
			if (StringUtils.isEmpty(tel)) {
				return new BhResult(400, "联系方式不能为空", null);
			}
			if (StringUtils.isEmpty(address)) {
				return new BhResult(400, "详细地址不能为空", null);
			}
			if (EmojiFilter.containsEmoji(fullName)) {
				return new BhResult(400, "收货人不能包含表情", null);
			}
			if (EmojiFilter.containsEmoji(address)) {
				return new BhResult(400, "详细地址不能包含表情", null);
			}

			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setmId(member1.getId());

			memberUserAddress.setFullName(EmojiFilter.emojiConvert1(fullName));

			memberUserAddress.setProv(Integer.parseInt(prov));
			memberUserAddress.setCity(Integer.parseInt(city));
			memberUserAddress.setArea(Integer.parseInt(area));

			memberUserAddress.setAddress(EmojiFilter.emojiConvert1(address));

			if (RegExpValidatorUtils.checkMobile(tel)) {
				memberUserAddress.setTel(tel);
			} else {
				return new BhResult(400, "您的手机号格式不正确,请重新输入!", null);
			}

			memberUserAddress.setIsDefault(false);// 2017-10-25,原来memberUserAddress.setIsDefault(true)默认,25号由chengfengyunmemberUserAddress.setIsDefault(false)
			memberUserAddress.setIsDel(0);
			if (StringUtils.isNotEmpty(label)) {
				memberUserAddress.setLabel(label);
			}
			if (StringUtils.isNotEmpty(telephone)) {
				memberUserAddress.setTelephone(telephone);
			}
			if (StringUtils.isNotEmpty(four)) {
				memberUserAddress.setFour(Integer.parseInt(four));
			} else {
				memberUserAddress.setFour(0);
			}
			memberUserAddress.setIsJd(Integer.parseInt(isJd));

			if (StringUtils.isEmpty(isJd)) {
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "isJd参数不能为空");
			} else {
				List<MemberUserAddress> list = service.selectAllAddressByisDel(memberUserAddress);// 10月31日0未删除，1已删除

//				if (list.size() < Contants.ADDRESS_COUNT) {
					int row = service.insertSelective(memberUserAddress);
					if (row > 0) {
						bhResult = new BhResult(200, "地址添加成功", memberUserAddress.getId());
					} else {
						bhResult = new BhResult(400, "地址添加失败", null);
					}
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			bhResult = BhResult.build(500, "操作失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return bhResult;
	}

	/**
	 * 2018-6-5程凤云 兑换接口
	 **/
	@ResponseBody
	@RequestMapping(value = "/doExchange",method = RequestMethod.POST)
	public BhResult doExchange(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult bhResult = null;

		try {
			//String json = request.getParameter("json");
			// {"addressId":"7109","cartIds":"9754","orderId":"0","fz":"6","mId":"14993","card":"11","mobile":"11"}
			 Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			 if (member==null) {
				 return BhResult.build(100, "您还未登录,请重新登录", null);
			 }

			String card = map.get("card");// 收取密码号码
			String mobile = map.get("mobile");
			//京东的SkuNo
			String jdSkuNo=map.get("jdSkuNo");
			
			String str = getCheck(mobile, card);
			Object object = JsonUtils.stringToObject(str);
			boolean flag1 = false;
			String flag = ((JSONObject) object).get("success").toString();
			String error_msg="";
			
			if ("false".equals(flag)) {
				error_msg= ((JSONObject) object).get("text").toString();
				flag1=false;
			} else {
				flag1=true;
			}
			
			// 向远程请求接口,验证phone和卡密是否正确,如果正确则继续走下一步，如果错误则提醒错误信息
			if (flag1) {
				
				String id =member.getId().toString();
				String addressId=map.get("addressId");
				
				if (StringUtils.isEmpty(addressId)) {
					return bhResult=BhResult.build(400, "地址id不能为空");
				}
				if (StringUtils.isBlank(jdSkuNo)) {
					return bhResult=BhResult.build(400, "jdSkuNo不能为空");
				}
				
				BhResult myBhResult=service.selectCartId(Long.valueOf(jdSkuNo), Integer.parseInt(id),addressId);
				if (myBhResult.getStatus()==400) {
					return bhResult=BhResult.build(400, myBhResult.getMsg());
				}
				map.put("cartIds", myBhResult.getData().toString());
				map.put("orderId", "0");
				map.put("fz", "6");
				
				// 生成订单
				Order order2 = simpleOrderService.getOrder(map, member, request);
				
				String orderBody = "";
				Integer fz = order2.getGroupFz();
				if (fz == null) {
					orderBody = "null,1"; // 普通下单模式
				} else if (fz.intValue() == 6) {
					orderBody = "null,1"; // 免费兑换专区
				}
				
			
				String my_str = getReturnString(mobile, card);
				Object object1 = JsonUtils.stringToObject(my_str);
				String my_falg = ((JSONObject) object1).get("success").toString();
				if ("false".equals(my_falg)) {
					error_msg= ((JSONObject) object1).get("text").toString();
					return BhResult.build(400, error_msg, null);
				} else {
					String jsonStr = "";
					String[] strs = orderBody.split(",");
					payCallbackService.paySesessUnion(order2.getOrderNo(), "", strs);
					jsonStr = "0";
					List<OrderShop> list=simpleOrderService.selectOrderShopListByOrderId(order2.getId());
					if (list.size()>0) {
						jsonStr=list.get(0).getId().toString();
					}
					return BhResult.build(200, "操作成功", jsonStr);
				}
				
			}else{
				return BhResult.build(400, error_msg, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			bhResult = new BhResult(500, "操作失败了", null);
		}
		return bhResult;
	}

	//更新兑换商品状态 
	public String getReturnString(String mobile, String card) {
		String host = "http://yy.ysq768.xyz";
		String path = "/index.php/mobile/luckyicon/checkCard";
		Map<String, String> headers = new HashMap<>();

		Map<String, String> querys = new HashMap<>();

		Map<String, String> bodys = new HashMap<>();
		bodys.put("mobile", mobile);
		bodys.put("card", card);
		try {
			HttpResponse response2 = HttpUtils.doPost(host, path, headers, querys, bodys);
			// HttpResponse response2 =
			// HttpUtils.doGet(host,path,headers,bodys);
			String str = EntityUtils.toString(response2.getEntity());
			return str;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	//校验密码
	public String getCheck(String mobile, String card) {
		String host = "http://yy.ysq768.xyz";
		String path = "/index.php/mobile/luckyicon/getCardinfo";
		Map<String, String> headers = new HashMap<>();

		Map<String, String> querys = new HashMap<>();

		Map<String, String> bodys = new HashMap<>();
		bodys.put("mobile", mobile);
		bodys.put("card", card);
		try {
			HttpResponse response2 = HttpUtils.doPost(host, path, headers, querys, bodys);
			// HttpResponse response2 =
			// HttpUtils.doGet(host,path,headers,bodys);
			String str = EntityUtils.toString(response2.getEntity());
			return str;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	
	/**
	 * @Description: 优惠劵类型处理
	 * @author xieyc
	 * 
	 * @date 2018年6月6日 下午5:05:19 
	 */
	public String strType(int type){
		String strType=null;
		switch (type) {
		case 1:
			strType="普通券";
			break;
		case 2:
			strType="免邮券";
			break;
		case 3:
			strType="红包券";
			break;

		default:
			strType="错误类型";
			break;
		}
		return strType;
	}
	
	
	@RequestMapping("/getStockById")
	@ResponseBody
	public BhResult getStockById(@RequestBody Map<String, String> map) {
		BhResult b = null;
		try {
			String prov = map.get("prov");// 省
			String city = map.get("city");// 市
			String area = map.get("area");// 区
			
			String Stock = JDGoodsApi.getStockById(map.get("sku"),prov+"_"+city+"_"+area);
			JSONObject json = JSONObject.fromObject(Stock);
			JSONArray array = JSONArray.fromObject(json.get("result"));
			
			if(Integer.valueOf(array.getJSONObject(0).get("state").toString())==33
			    || Integer.valueOf(array.getJSONObject(0).get("state").toString())==40) {
				b = new BhResult(200, "查询成功", "有货");
			}else {
				b = new BhResult(200, "查询成功", "没货");
			}
            System.out.println("============"+json+array.getJSONObject(0).get("state"));
			//b = new BhResult(200, "查询成功", Stock);
		} catch (Exception e) {
			b = new BhResult(400, "查询失败", null);
			e.printStackTrace();
		}
		return b;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/isLogin")
	@ResponseBody
	public BhResult isLogin(HttpServletRequest request) {
		BhResult b = null;
		try {
			HttpSession session = request.getSession(false);
		    Member m = new Member();
			if(session!=null) {
			     m = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			if(m!=null&&!m.equals("")&&m.getId()!=null) {
				b = new BhResult(200, "已经登录", null);
			}else {
				b = new BhResult(100, "未登录", "请登录");
			}
		
		} catch (Exception e) {
			b = new BhResult(400, "错误", null);
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * POST 
	 * 1.form-data 键值请求  
	 * 2.json 请求
	 * @param parm
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] parm) throws ParseException, IOException {
		 //创建httpclient对象
		HttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost("http://yy.ysq768.xyz/index.php/mobile/luckyicon/checkCard");
        //创建一个Entity。模拟一个表单
        List<NameValuePair> kvList = new ArrayList<NameValuePair>();
        kvList.add(new BasicNameValuePair("card","888888"));
        kvList.add(new BasicNameValuePair("mobile","123"));
        
        //包装成一个UrlEncodedFormEntity对象
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(kvList,"utf-8");
        entity.setContentEncoding("application/x-www-form-urlencoded; charset=UTF-8");
        //设置请求的内容
        httpPost.setEntity(entity);
        //执行请求操作，并拿到结果（同步阻塞）
        HttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity hentity = response.getEntity();
        if (hentity != null) {
            //按指定编码转换结果实体为String类型
           JSONObject object = JSONObject.fromObject(EntityUtils.toString(response.getEntity(), "utf-8"));
           System.out.println(object);
        }
        //POST  json方式
//          HttpPost httpPost = new HttpPost("https://bhmall.zhiyesoft.cn/bh-admin/whiteMember/openWhite");
//          CloseableHttpClient client = HttpClients.createDefault();
// 
//          JSONObject jsonParam = new JSONObject();  
//          jsonParam.put("whiteList", "on");
//          StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
//          entity.setContentEncoding("UTF-8");    
//          entity.setContentType("application/json");    
//          httpPost.setEntity(entity);
//          HttpResponse resp = client.execute(httpPost);
//          System.out.println(EntityUtils.toString(resp.getEntity()));
	}
	
	
	
}
