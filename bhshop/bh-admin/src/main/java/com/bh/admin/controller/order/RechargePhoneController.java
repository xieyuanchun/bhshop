package com.bh.admin.controller.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.order.RechargePhone;
import com.bh.admin.service.RechargePhoneService;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.utils.IDUtils;
import com.bh.utils.PageBean;
import com.bh.utils.recharge.MobileRecharge;

@Controller
@RequestMapping("/rechargePhone")
public class RechargePhoneController {

	@Autowired
	private RechargePhoneService rechargePhoneService;
	
	
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody RechargePhone rp) {
		
		BhResult r = null;
		try {
			 //自己生成订单号
		  	 IDUtils iDUtils = new IDUtils();
			 rp.setAddtime(new Date());
			 rp.setAmount(rp.getAmount());
			 rp.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE));
			 rechargePhoneService.add(rp);
			 
		     r = new BhResult(200,"添加成功",null);
		     
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
			
			// rechargePhoneService.update(rp);
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
	
	
	@RequestMapping("/phoneCharges")
	@ResponseBody
	public BhResult phoneCharges(@RequestBody RechargePhone rp) {
		BhResult r = null;
		try {
		     Map<String,String> map = new HashMap<String,String>();
		     map.put("30", "29.97");
		     map.put("50", "49.95");
		     map.put("100", "99.9");
		     map.put("200", "199.8");
		     map.put("300", "299.7");
		     map.put("500", "499.5");
		     r = new BhResult(200,"获取成功",map);
		}catch(Exception e) {
			 r = new BhResult(400,"获取失败",null);
			 e.printStackTrace();
		}
		return r;
	}
	
}
