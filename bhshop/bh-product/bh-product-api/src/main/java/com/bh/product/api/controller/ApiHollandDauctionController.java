package com.bh.product.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.CashDeposit;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.HollandDauction;
import com.bh.order.pojo.Order;
import com.bh.product.api.service.ApiHollandDauctionService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/api/dauction")
public class ApiHollandDauctionController {
	@Autowired
	private ApiHollandDauctionService service;
	
	/**
	 * <p>Description: 拍卖区商品列表</p>
	 *  @author scj  
	 *  @date 2018年5月21日
	 */
	@RequestMapping("/apiDauctionList")
	@ResponseBody
	public BhResult apiDauctionList(@RequestBody HollandDauction entity, HttpServletRequest request) {
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
			PageBean<HollandDauction> page = service.apiDauctionList(entity, member);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	@RequestMapping("/dauctionNotice")
	@ResponseBody
	public BhResult dauctionNotice(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String mId = map.get("mId");
			String goodsId = map.get("goodsId");
			String auctionPrice = map.get("auctionPrice");
			String currentPeriods = map.get("currentPeriods");
			boolean data = service.dauctionNotice(Integer.parseInt(mId),auctionPrice, goodsId, currentPeriods);
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
	 * <p>Description: 交保证金接口</p>
	 *  @author scj  
	 *  @date 2018年5月25日
	 */
	@RequestMapping("/payDeposit")//《迁移至AuctionController》
	@ResponseBody
	public BhResult payDeposit(@RequestBody CashDeposit cashDeposit,HttpServletRequest request) {
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
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			cashDeposit.setmId(member.getId());
			int row = service.payDeposit(cashDeposit);
			if(row==666){
				r = new BhResult();
				r.setStatus(row);
				r.setMsg("钱包不存在");
				return r;
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}
	/**
	 * @Description: 退保证金测试
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/refundDeposit")//《迁移至AuctionController》
	@ResponseBody
	public BhResult refundDeposit(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			int mId= Integer.valueOf(map.get("mId"));
			int goodsId=Integer.valueOf(map.get("goodsId"));
			int currentPeriods=Integer.valueOf(map.get("currentPeriods"));
			service.refundDeposit(goodsId,currentPeriods,mId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	/**
	 * @Description: 当订单价格小于保证金的时候,点击去结算调用次接口（不调此wxjsp接口）==》退回成功者多交的保证金(作废)
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/depositJsp")//《迁移至AuctionController》
	@ResponseBody
	public BhResult depositJsp(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			String orderId=map.get("orderId");
			String addressId=map.get("addressId");
			service.depositJsp(orderId,addressId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}
	
	/**
	 * <p>Description: 获取最新一期拍卖配置信息</p>
	 *  @author scj  
	 *  @date 2018年6月7日
	 */
	@RequestMapping("/dauctionDetail")
	@ResponseBody
	public BhResult dauctionDetail(@RequestBody HollandDauction entity, HttpServletRequest request) {
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
			Map<String, Object> data = service.dauctionDetail(entity, member);
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
	 * <p>Description: 获取最新一期拍卖配置信息</p>
	 *  @author scj  
	 *  @date 2018年6月7日
	 */
	@RequestMapping("/nextDauctionDetail")
	@ResponseBody
	public BhResult nextDauctionDetail(@RequestBody HollandDauction entity) {
		BhResult r = null;
		try {
			Map<String, Object> data = service.nextDauctionDetail(entity);
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
}
