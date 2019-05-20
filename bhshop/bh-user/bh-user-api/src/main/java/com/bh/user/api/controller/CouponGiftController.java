package com.bh.user.api.controller;

import java.util.List;
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
import com.bh.goods.pojo.CouponPojo;
import com.bh.result.BhResult;
import com.bh.user.api.service.CouponGiftService;
import com.bh.user.pojo.Member;

@Controller
@RequestMapping("/couponGift")
public class CouponGiftController {
	private static final Logger logger = LoggerFactory.getLogger(CouponGiftController.class);
	@Autowired
	private CouponGiftService couponService;

	/**
	 * @Description: 用户第一次登入的时候大礼包展示接口(作废)
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	@RequestMapping("/showCouponGift")
	@ResponseBody
	public BhResult rendAuctionOrder(HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (member != null) {
				List<CouponPojo> list = couponService.showCouponGift(member.getId());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(list);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showCouponGift#######" + e);
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}

	/**
	 * @Description: 更新优惠卷(点击领取)
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	@RequestMapping("/changeLogStatus")
	@ResponseBody
	public BhResult changeLogStatus(HttpServletRequest request, @RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			int row = couponService.changeLogStatus(map.get("id"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######changeLogStatus#######" + e);
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}

	/**
	 * @Description: 是否发送过大礼包
	 * @author xieyc
	 * @date 2018年6月8日 下午5:58:30
	 */
	@RequestMapping("/isFirstLogin")
	@ResponseBody
	public BhResult isGetGift(HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member != null) {
					boolean flag = couponService.isGetGift(member.getId());
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(flag);
				} else {
					r = new BhResult();
					r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
					r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######isFirstLogin#######" + e);
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}

	/**
	 * @Description: 第一次登入标识(作废)
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	@RequestMapping("/isFirstLogin1")
	@ResponseBody
	public BhResult isFirstLogin(HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (member != null) {
				boolean flag = couponService.isFirstLogin(member.getId());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(flag);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######isFirstLogin1#######" + e);
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}

}
