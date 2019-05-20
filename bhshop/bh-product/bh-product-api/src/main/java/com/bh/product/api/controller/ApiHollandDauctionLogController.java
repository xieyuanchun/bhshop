package com.bh.product.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.HollandDauctionLog;
import com.bh.order.pojo.Order;
import com.bh.product.api.service.ApiHollandDauctionLogService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/api/dauctionLog")
public class ApiHollandDauctionLogController {
	@Autowired
	private ApiHollandDauctionLogService service;
	
	/**
	 * <p>Description: 用户竞拍操作</p>
	 *  @author scj  
	 *  @date 2018年5月21日
	 */
	@Scope("singleton")
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody HollandDauctionLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
				}
			}
			if(member!=null){
				entity.setmId(member.getId());
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			int row = service.insert(entity);
			if(row==999){
				r = new BhResult();
				r.setStatus(999);
				r.setMsg("请先缴纳押金");
				return r;
			}else if(row==555){
				r = new BhResult();
				r.setStatus(555);
				r.setMsg("已有用户出价高于您，请重新出价");
				return r;
			}else if(row==666){
				r = new BhResult();
				r.setStatus(666);
				r.setMsg("活动已结束");
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	/**
	 * <p>Description: 拍卖记录</p>
	 *  @author scj  
	 *  @date 2018年5月22日
	 */
	@RequestMapping("/apiLogList")
	@ResponseBody
	public BhResult apiLogList(@RequestBody HollandDauctionLog entity) {
		BhResult r = null;
		try {
			PageBean<HollandDauctionLog> data = service.apiLogList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	/**
	 * @Description: 拍卖订单生成测试
	 * @author xieyc
	 * @date 2018年5月23日 下午5:04:14 
	 */
	@RequestMapping("/rendAuctionOrder")//《迁移至AuctionController》
	@ResponseBody
	public BhResult rendAuctionOrder(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {		
			int mId= Integer.valueOf(map.get("mId"));
			int goodsId=Integer.valueOf(map.get("goodsId"));
			int auctionPrice=Integer.valueOf(map.get("auctionPrice"));
			Order order=service.rendAuctionOrder(auctionPrice,mId,goodsId);
			System.out.println(order.toString());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(order);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	
	@RequestMapping("/updateSecondStatus")
	@ResponseBody
	public BhResult updateSecondStatus() {
		BhResult r = null;
		try {
			service.updateSecondStatus();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	/**
	 * <p>Description: 去竞拍（first调）</p>
	 *  @author scj  
	 *  @date 2018年5月28日
	 */
	@RequestMapping("/insertFirst")
	@ResponseBody
	public BhResult insertFirst(@RequestBody HollandDauctionLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
				}
			}
			if(member != null){
				entity.setmId(member.getId());
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			int row = service.insertFirst(entity);
			if(row==666){
				r = new BhResult();
				r.setStatus(666);
				r.setMsg("活动已结束");
				return r;
			}else if(row==999){
				r = new BhResult();
				r.setStatus(999);
				r.setMsg("请先缴纳押金");
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	/**
	 * <p>Description: 用户竞拍历史记录</p>
	 *  @author scj  
	 *  @date 2018年5月22日
	 */
	@RequestMapping("/apiUserLogList")
	@ResponseBody
	public BhResult apiLogList(@RequestBody HollandDauctionLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
				}
			}
			if(member != null){
				entity.setmId(member.getId());
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			List<Map<String, Object>> data = service.apiUserLogList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	@RequestMapping("/updateLostTimeRecord")
	@ResponseBody
	public BhResult updateLostTimeRecord() {
		BhResult r = null;
		try {
			service.updateLostTimeRecord();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
}
