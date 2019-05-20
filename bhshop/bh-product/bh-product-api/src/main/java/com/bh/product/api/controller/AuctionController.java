package com.bh.product.api.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.AuctionGoodsInfo;
import com.bh.goods.pojo.CashDeposit;
import com.bh.order.pojo.Order;
import com.bh.product.api.service.AuctionService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
 
@Controller
@RequestMapping("/auction")
public class AuctionController {
	private static final Logger logger = LoggerFactory.getLogger(AuctionController.class);
	@Autowired
	private AuctionService auctionService;
	
	/**
	 * @Description: 拍卖订单生成 与 退保证金
	 * @author xieyc
	 * @date 2018年5月23日 下午5:04:14 
	 */
	@RequestMapping("/rendAucOrderAndRefundDeposit")
	@ResponseBody
	public BhResult rendAucOrderAndRefundDeposit(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {		
			int mId= Integer.valueOf(map.get("mId"));
			int goodsId=Integer.valueOf(map.get("goodsId"));
			int auctionPrice=Integer.valueOf(map.get("auctionPrice"));
			int currentPeriods=Integer.valueOf(map.get("currentPeriods"));
			Order order=auctionService.rendAucOrderAndRefundDeposit(auctionPrice,mId,goodsId,currentPeriods);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(order);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 退保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/refundDeposit")
	@ResponseBody
	public BhResult refundDeposit(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			int mId= Integer.valueOf(map.get("mId"));
			int goodsId=Integer.valueOf(map.get("goodsId"));
			int currentPeriods=Integer.valueOf(map.get("currentPeriods"));
			auctionService.refundDeposit(goodsId,currentPeriods,mId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 当订单价格小于保证金的时候,点击去结算调用次接口（不调此wxjsp接口）==》退回成功者多交的保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/depositJsp")
	@ResponseBody
	public BhResult depositJsp(@RequestBody Map<String,String> map) {
		BhResult r = null;
		try {
			String orderId=map.get("orderId");
			String addressId=map.get("addressId");
			auctionService.depositJsp(orderId,addressId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 用户某个商品某一期是否已经交了保证金
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/isPayDeposit")
	@ResponseBody
	public BhResult isPayDeposit(@RequestBody CashDeposit cashDeposit,HttpServletRequest request) {
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
				cashDeposit.setmId(member.getId());
				Map<String,Object> map= auctionService.isPayDeposit(cashDeposit);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(map);
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description:交保证金接口
	 * @author xieyc
	 * @date 2018年7月10日 上午9:56:09 
	 */
	@RequestMapping("/payDeposit")
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
			if(member!=null){
				cashDeposit.setmId(member.getId());
				int row = auctionService.payDeposit(cashDeposit);
				if(row==1){
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}else if(row==-1){
					r = BhResult.build(400, "钱包不存在");
				}else if(row==-2){
					r = BhResult.build(400, "你的钱包金额不足");
				}
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 拍卖时获取商品信息接口
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/getBhShopGoodsInfo")
	@ResponseBody
	public AuctionGoodsInfo getBhShopGoodsInfo(@RequestBody Map<String,String> map) {
		AuctionGoodsInfo auctionGoodsInfo=null;
		try {
			String goodsId=map.get("goodsId");
			auctionGoodsInfo=auctionService.getBhShopGoodsInfo(Integer.valueOf(goodsId));
		} catch (Exception e) {
			logger.error("拍卖时获取商品信息接口"+e.getMessage());
			e.printStackTrace();
		}
		return auctionGoodsInfo;
	}
	
	/**
	 * @Description:某个商品某个用户是否收藏过
	 * @author xieyc
	 * @date @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/isCollect")
	@ResponseBody
	public BhResult isCollect(@RequestBody Map<String,String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String goodsIds = map.get("goodsIds");
			Integer mId=null;
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
					mId=member.getId();
				}
			}
			Map<String,Object> renturnMap=auctionService.isCollect(goodsIds,mId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(renturnMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
	
	
	
}
