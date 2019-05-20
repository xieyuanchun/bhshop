package com.bh.product.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.product.api.service.CouponLogService;
import com.bh.product.api.service.CouponService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;

@Controller
@RequestMapping("/couponLog")
public class CouponLogController {

	/**
	 * 优惠劵日志
	 */
	
	@Autowired
	private CouponLogService couponLogService;
	
	@Autowired	
	private CouponService couponService;
	/**
	 * 添加优惠劵日志
	 */
	@RequestMapping("/addCouponLog")
	@ResponseBody
	public BhResult addCouponLog(@RequestBody CouponLog couponLog , HttpServletRequest request){
		   BhResult  result = null;
		  
		   try{
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			  
			   Coupon coupon = couponService.selectByPrimaryKey(couponLog.getId());
			   int row = 0;
			   CouponLog couponLog1 = new CouponLog();
			   couponLog1.setmId(member.getId());//当前领取的客户的id
			   couponLog1.setCouponId(couponLog.getId());//CouponId
			   couponLog1.setCreateTime(new Date());//当前的领取时间
			   couponLog1.setExpireTime(coupon.getEndTime());//过期时间
			   couponLog1.setUseTime(new Date());//使用时间为当前时间，如果使用时间和领取时间一致，就说明没使用过
			   couponLog1.setShopId(couponLog.getShopId()); //商家id
			   List<CouponLog> list = couponLogService.getByMidAndCouponId(couponLog1);
			   //判断当前的优惠劵有没领取过
			   if(list.size()<1){
				    //没领过并且剩余数量大于0才能领取。把当前的优惠劵的剩余数量改了
				    Coupon coupon2 = couponService.selectByPrimaryKey(couponLog.getId());//CouponId
				    if(coupon2.getStock()>0){
				    	 row = couponLogService.addCouponLog(couponLog1);
				    	 coupon2.setStock(coupon2.getStock()-1);//剩余数量
					     int row2 =couponService.updateCoupon(coupon2);//更改操作
				    }else{
				    	return result = new BhResult(500,"优惠劵已领完",null);
				    }
			   }else{
				   return result = new BhResult(500,"不能重复领取优惠劵",null);
			   }
			 
			   if(row>0){
				   result = new BhResult(200,"添加信息成功",null);
			   }else{
				   result = new BhResult(400,"添加信息失败",null);
			   }
			  
		   }catch(Exception e){
			   result = new BhResult(100,"没登录不可领取优惠劵！请进行登录",null);
			   e.printStackTrace();
		   }
		   return result;
	}
		
	
	/**
	 * 我的移动端的优惠劵列表
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody CouponLog couponLog , HttpServletRequest request){
		   BhResult  result = null;
		  
		   try{
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   couponLog.setmId(member.getId());
			   Map<String,Object> map = couponLogService.listCouponLog(couponLog);
			   result = new BhResult();
			   result.setStatus(BhResultEnum.SUCCESS.getStatus());
			   result.setMsg(BhResultEnum.SUCCESS.getMsg());
			   result.setData(map);
		   }catch(Exception e){
			   result = new BhResult(500,"操作失败",null);
			   e.printStackTrace();
		   }
		   return result;
	}

	/**
	 * @Description: 每种优惠卷类型统计
	 * @author xieyc
	 * @date 2018年6月8日 下午8:58:01
	 */
	@RequestMapping("/couponNum")
	@ResponseBody
	public BhResult couponNum(HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			Map<String, Object> map = couponLogService.couponNum(member.getId());
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(map);
		} catch (Exception e) {
			result = new BhResult(500, "操作失败", null);
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 我的移动端，当前可以使用的优惠劵列表
	 */
	@RequestMapping("/goodListPage")
	@ResponseBody
	public BhResult goodListPage(@RequestBody CouponLog couponLog , HttpServletRequest request){
		   BhResult  result = null;
		   //前端传shopId 和当前的页数currentPage
		   try{
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   couponLog.setmId(member.getId());
			   Map<String,Object> map = couponLogService.goodListCouponLog(couponLog);
			   result = new BhResult();
			   result.setStatus(BhResultEnum.SUCCESS.getStatus());
			   result.setMsg(BhResultEnum.SUCCESS.getMsg());
			   result.setData(map);
		   }catch(Exception e){
			   result = new BhResult(400,"操作失败",null);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	
	
	/**
	 * PC显示所有优惠劵日志
	 */
	@RequestMapping("/allListPage")
	@ResponseBody
	public BhResult allListPage(@RequestBody CouponLog couponLog){
		   BhResult  result = null;
		  
		   try{
			   
			   Map<String,Object> map = couponLogService.allListCouponLog(couponLog);
			   result = new BhResult();
			   result.setStatus(BhResultEnum.SUCCESS.getStatus());
			   result.setMsg(BhResultEnum.SUCCESS.getMsg());
			   result.setData(map);
		   }catch(Exception e){
			   result = new BhResult(400,"操作失败",null);
			   e.printStackTrace();
		   }
		   return result;
	}
}
