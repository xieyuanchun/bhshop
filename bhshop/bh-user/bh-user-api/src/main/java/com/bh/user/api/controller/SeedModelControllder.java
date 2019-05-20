package com.bh.user.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.enums.BhResultEnum;
import com.bh.order.pojo.OrderSeed;
import com.bh.result.BhResult;
import com.bh.user.api.service.SeedModelService;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedModel;
@Controller
public class SeedModelControllder {
	private static final Logger logger = LoggerFactory.getLogger(SeedModelControllder.class);
	@Autowired
	private SeedModelService seedModelService;

	/**
	 * CHENG-201712-05-01 判断用户是否已经购买了种子 --移动端接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/isBuy")
	@ResponseBody
	public BhResult isBuy(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			if (StringUtils.isEmpty(mId)) {
				result = new BhResult(400, "用户的id不能为空", null);
			} else {
				MemberUser memberUser = new MemberUser();
				memberUser.setmId(Integer.parseInt(mId));
				result = seedModelService.isBuy(memberUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######isBuy#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-05-01 已累计签到的天数接口 --移动端接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/attendancedays")
	@ResponseBody
	public BhResult attendanceDays(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			if (StringUtils.isNotEmpty(mId)) {
				MemberUser user = seedModelService.selectMemberUserBymId(Integer.parseInt(mId));
				// 根据mId查询用户,如果用户不存在
				if (user == null) {
					result = new BhResult(400, "抱歉,该用户不存在", null);
				} else {
					MemberUser memberUser1 = new MemberUser();
					memberUser1.setmId(Integer.parseInt(mId));
					BhResult bhResult = seedModelService.isBuy(memberUser1);
					// 200 未购买过种子
					if (bhResult.getStatus() == 200) {
						result = new BhResult(400, "用户未购买过种子", null);
					} else if (bhResult.getStatus() == 400) {
						MemberUser memberUser = new MemberUser();
						memberUser.setmId(Integer.parseInt(mId));
						List<SeedModel> del = new ArrayList<>();
						del = seedModelService.attendanceDays(memberUser);
						result = new BhResult(BhResultEnum.SUCCESS, del);
					}
				}
			} else {
				result = new BhResult(400, "用户的id不能为空", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######attendancedays#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-06-01 签到的接口 --移动端接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/attendances")
	@ResponseBody
	public BhResult attendances(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String mId = map.get("mId");
			String orderseedId = map.get("orderseedId");
			if (StringUtils.isEmpty(mId)) {
				bhResult = new BhResult(400, "mId参数不能为空", null);
			}
			if (StringUtils.isEmpty(orderseedId)) {
				bhResult = new BhResult(400, "orderseedId参数不能为空", null);
			} else {
				MemberUser user = seedModelService.selectMemberUserBymId(Integer.parseInt(mId));
				if (user == null) {
					bhResult = new BhResult(400, "抱歉,该用户不存在", null);
				} else {
					MemerScoreLog log = new MemerScoreLog();
					log.setmId(Integer.parseInt(mId));
					log.setOrderseedId(Integer.parseInt(orderseedId));
					int row = 0;
					row = seedModelService.attendances(log);
					// row=0签到失败,可能sql语句操作失败,1签到成功,2今天已经签到过了
					if (row == 0) {
						bhResult = new BhResult(BhResultEnum.FAIL, null);
					} else if (row == 1) {
						bhResult = new BhResult(200, "签到成功", null);
					} else if (row == 2) {
						bhResult = new BhResult(400, "今天已签到过了哦,明天再来吧!", null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######attendances#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	/**
	 * CHENG-201712-07-01 领取余额收益接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/getbalance")
	@ResponseBody
	public BhResult getBalance(@RequestBody MemerScoreLog log, HttpServletRequest request) {
		BhResult result = null;
		try {
			if (log.getmId() == null) {
				result = new BhResult(400, "mId参数不能为空", null);
			}
			if (log.getOrderseedId() == null) {
				result = new BhResult(400, "orderseedId参数不能为空", null);
			} else {
				MemberUser user = seedModelService.selectMemberUserBymId(log.getmId());
				if (user == null) {
					result = new BhResult(400, "抱歉,该用户不存在", null);
				} else {
					BhResult bhResult = seedModelService.getBalance(log);
					if (bhResult.getStatus().equals(201)) {
						result = new BhResult(200, "领取成功", bhResult.getData());
					} else if (bhResult.getStatus().equals(202)) {
						result = new BhResult(400, "你未达到领取天数", null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getbalance#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-07-01 根据种子订单获得该用户的信息
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/getusermsg")
	@ResponseBody
	public BhResult getUsermsgByOrderNo(@RequestBody OrderSeed orderSeed, HttpServletRequest request) {
		BhResult result = null;
		try {
			if (orderSeed.getOrderNo() == null) {
				result = new BhResult(400, "orderNo参数不能为空", null);
			} else {
				result = seedModelService.getUsermsgByOrderNo(orderSeed.getOrderNo());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getusermsg#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

}
