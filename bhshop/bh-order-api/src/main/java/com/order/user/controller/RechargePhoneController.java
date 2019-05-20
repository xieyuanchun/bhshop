package com.order.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.bean.Alipay;
import com.bh.config.Contants;
import com.bh.order.pojo.RechargePhone;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.AlipayUtil;
import com.bh.utils.IPUtils;
import com.bh.utils.PageBean;
import com.bh.utils.pay.HttpService;
import com.bh.utils.pay.WXPayUtil;
import com.bh.utils.recharge.MobileRecharge;
import com.google.gson.Gson;
import com.order.enums.UnionPayInterfaceEnum;
import com.order.user.service.RechargePhoneService;
import com.order.util.smallAppPay.PayResult;
import com.order.util.smallAppPay.PayVo;
import com.order.vo.UnionPayVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/rechargePhone")
public class RechargePhoneController {

	@Autowired
	private RechargePhoneService rechargePhoneService;
	
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody RechargePhone rp,HttpServletRequest request) {
		
		BhResult r = null;
		try {
			 Member m = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			 if(m!=null&&!m.equals("")){
				 rp.setMid(m.getId());
			 }
			 //自己生成订单号
		  	 IDUtils iDUtils = new IDUtils();
			 rp.setAddtime(new Date());
			 
			 double amount2 = Double.valueOf(rp.getAmount2());			 
			 rp.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2*10*10)));
			 rp.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
			 rechargePhoneService.add(rp);
			 
		     UnionPayVO vo = new UnionPayVO();
			 String orderBody = "null,0003"+","+rp.getId();
			 vo.setTotalAmount(rp.getAmount()+"");
			 vo.setOriginalAmount(rp.getAmount()+"");
			 vo.setAttachedData(orderBody);
			 vo.setMerOrderId(rp.getOrderNo());
			 vo.setMd5Key(Contants.PLAT_MD5_KEY);
			 String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXMRECHARGE.getMethod(),vo);
			 //System.out.println("before jsonStr--->" + jsonStr);
			 jsonStr = jsonStr.replaceAll("&", "&amp");
			 
		     r = new BhResult(200,"添加成功",jsonStr);
		     
		}catch(Exception e) {
			 r = new BhResult(400,"添加失败",null);
			 e.printStackTrace();
		}
		
		return r;
	}
	
	
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody RechargePhone rp) {
		
		BhResult r = null;
		try {
             //rechargePhoneService.update(rp);
		     r = new BhResult(200,"修改成功",null);
		     
		}catch(Exception e) {
			 r = new BhResult(400,"修改失败",null);
			 e.printStackTrace();
		}
		
		return r;
	}
	
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody RechargePhone rp) {
		
		BhResult r = null;
		try {
			 rechargePhoneService.delete(rp);
		     r = new BhResult(200,"删除成功",null);
		     
		}catch(Exception e) {
			 r = new BhResult(400,"删除失败",null);
			 e.printStackTrace();
		}
		
		return r;
	}
	
	
	
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody RechargePhone rp) {
		BhResult r = null;
		try {
		     PageBean<RechargePhone> page = rechargePhoneService.listPage(rp);
		     r = new BhResult(200,"获取成功",page);
		}catch(Exception e) {
			 r = new BhResult(400,"获取失败",null);
			 e.printStackTrace();
		}
		return r;
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/phoneCharges")
	@ResponseBody
	public BhResult phoneCharges(@RequestBody RechargePhone rp,HttpServletRequest request) {
		BhResult r = null;
		try {
			 HttpSession session = request.getSession(false);
			 Member member =new Member();
			 if(session!=null) {
			     member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
			 }
		     Map<String,String> map = new HashMap<String,String>();
		     map.put("30", "29.97");
		     map.put("50", "49.95");
		     map.put("100", "99.9");
		     map.put("200", "199.8");
		     map.put("300", "299.7");
		     map.put("500", "499.5");
		     if(member!=null&&!member.equals("")&&member.getId()!=null) {
		    	 map.put("login", "true");
		     }else {
		    	 map.put("login", "false");
		     }
		     r = new BhResult(200,"获取成功",map);
		}catch(Exception e) {
			 r = new BhResult(400,"获取失败",null);
			 e.printStackTrace();
		}
		return r;
	}
	
	
	@RequestMapping("/getByOrderId")
	@ResponseBody
	public BhResult getByOrderId(@RequestBody RechargePhone rp) {
		BhResult r = null;
		try {
			 List<RechargePhone> list = rechargePhoneService.getByOrderNo(rp);
             
		     r = new BhResult(200,"获取成功",list.get(0));
		}catch(Exception e) {
			 r = new BhResult(400,"获取失败",null);
			 e.printStackTrace();
		}
		return r;
	}
	
	
	@RequestMapping("/getOrderDetails")
	@ResponseBody
	public BhResult getOrderDetails(@RequestBody RechargePhone rp) {
		BhResult r = null;
		try {
			 rechargePhoneService.getOrderDetails();
             
		     r = new BhResult(200,"获取成功",null);
		}catch(Exception e) {
			 r = new BhResult(400,"获取失败",null);
			 e.printStackTrace();
		}
		return r;
	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/smallAppAdd")
	@ResponseBody
	public BhResult smallAppAdd(HttpServletRequest request,HttpServletResponse response) {
		
		BhResult r = null;
		try {
			 String json = request.getParameter("json");
			 Gson gson = new Gson();
			 Map<String, String> map = gson.fromJson(json, Map.class);
			 String code = map.get("code");
			 String money2=map.get("money");
			 String phone=map.get("phone");
			 String openid = WXPayUtil.getSmallAppOpenid(Contants.sAppId, Contants.sAppSecret, code);
			 
			 Member m = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			 RechargePhone rp = new RechargePhone();
			 if(m!=null&&!m.equals("")){
				 rp.setMid(m.getId());
			 }
			 //自己生成订单号
		  	 IDUtils iDUtils = new IDUtils();
			 rp.setAddtime(new Date());
			 
			 double amount2 = Double.valueOf(money2);			 
			 rp.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2*10*10)));
			 rp.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
			 rp.setPhone(phone);
			 rechargePhoneService.add(rp);
			  
			 PayVo payVo = new PayVo();
			 payVo.setOpenId(openid);
			 payVo.setOut_trade_no(rp.getOrderNo());
			 String orderBody = "null,0003"+","+rp.getId();
			 payVo.setAttach(orderBody);
			 payVo.setTotal_fee(rp.getAmount()+"");
			 String spbill_create_ip = IPUtils.getIpAddr(request);//终端IP   
			 payVo.setSpbill_create_ip(spbill_create_ip);
			 payVo.setBody("订单描述");
			 PayResult  payResult = new PayResult();
			 JSONObject ret = payResult.getPayResult(payVo);
			 //System.out.println("smallApp ret--->"+ret);
			 
		     r = new BhResult(200,"添加成功",ret.toString());
		     
		}catch(Exception e) {
			 r = new BhResult(400,"添加失败",null);
			 e.printStackTrace();
		}
		
		return r;
	}
	
	
	
	/**
	 * app话费充值
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/appRechargePhone")
	@ResponseBody
	public BhResult appRechargePhone(@RequestBody Map<String, Object>map,HttpServletRequest request,HttpServletResponse response) {
		
		BhResult r = null;
		try {

			 String money2=(String) map.get("money");
			 String phone=(String) map.get("phone");
			 String type=(String) map.get("type");
			 if (StringUtils.isBlank(money2) || StringUtils.isBlank(phone)) {
				return r=new BhResult(400, "参数不能为空", null);
			}
			 //1支付宝；2微信支付
			 if (StringUtils.isBlank(type)) {
				 type="1";
			 }
			 
			 
			 Member m = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			 RechargePhone rp = new RechargePhone();
			 if(m!=null&&!m.equals("")){
				 rp.setMid(m.getId());
			 }
			
			 rp.setAddtime(new Date());
			 
			 double amount2 = Double.valueOf(money2);			 
			 rp.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2*10*10)));
			 rp.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
			 rp.setPhone(phone);
			 rechargePhoneService.add(rp);
			 String orderBody = "null,0003"+","+rp.getId();
			 
			if ("1".equals(type)) {
				Alipay alipay = new Alipay();
				alipay.setOutTradeNo(rp.getOrderNo());// 订单号
				alipay.setTotalAmount(money2);// 总金额
				alipay.setSubject("创建订单标题");// 创建订单标题
				alipay.setBody(orderBody);// 创建订单描述
				r = AlipayUtil.createAppTrade(alipay);
			}else{
				return r=new BhResult(400, "暂时不支持微信支付", null);
			}
		     
		}catch(Exception e) {
			 r = new BhResult(400,"添加失败",null);
			 e.printStackTrace();
		}
		return r;
		
	}
}
