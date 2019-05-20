package com.bh.auc.api.controller;

import com.bh.auc.api.service.AuctionApiService;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionRecord;
import com.bh.auc.vo.AuctionApiGoods;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;



/**
 * @Description: 移动端接口
 * @author xieyc
 * @date 2018年7月6日 上午10:39:39 
 */
@Controller
@RequestMapping("/auctionApi")
public class AuctionApiController {
	private static final Logger logger = LoggerFactory.getLogger(AuctionApiController.class);
	@Autowired
	private AuctionApiService  auctionApiService;
	/**
	 * @Description: 订单生成和退保证金测试
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/rendAucOrderAndRefundDeposit")
	@ResponseBody
	public BhResult rendAucOrderAndRefundDeposit() {
		BhResult r = null;
		try {
			auctionApiService.rendAucOrderAndRefundDeposit();
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
	 * @Description: 退保证金测试
	 * @author xieyc
	 * @date 2018年7月9日 下午5:39:59 
	 */
	@RequestMapping("/refundDeposit")
	@ResponseBody
	public BhResult refundDeposit() {
		BhResult r = null;
		try {
			auctionApiService.refundDeposit();
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
	 * @Description: app拍卖商品列表
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/auctionApiGoodList")
	@ResponseBody
	public BhResult auctionApiGoodList(@RequestBody AuctionConfig entity,HttpServletRequest request ) {
		BhResult r = null;
		try {
			List<AuctionApiGoods> listAuctionApiGoods  = auctionApiService.auctionApiGoodList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(listAuctionApiGoods);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description:api拍卖商品详情
	 * @author xieyc
	 * @date @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/auctionApiGoodDetails")
	@ResponseBody
	public BhResult auctionApiGoodDetails(@RequestBody Map<String,String> map ,HttpServletRequest request) {
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
				String goodsId = map.get("goodsId");
				String currentPeriods = map.get("currentPeriods");
				Map<String, Object> returnMap = auctionApiService.auctionApiGoodDetails(Integer.parseInt(goodsId),
						member.getId(), Integer.parseInt(currentPeriods));
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(returnMap);
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
	 * @Description: api用户拍卖历史记录
	 * @author xieyc
	 * @date 2018/7/23 10:49
	 */
	@RequestMapping("/apiUserAuctionRecord")
	@ResponseBody
	public BhResult apiUserAuctionRecord(@RequestBody AuctionRecord entity, HttpServletRequest request) {
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
			}
			List< Map<String, Object> > data = auctionApiService.apiUserAuctionRecord(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("api用户拍卖历史记录"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
}
