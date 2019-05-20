package com.bh.user.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.api.service.WalletLogService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;

@RestController
@RequestMapping("/walletLog")
public class WalletLogController {
	private static final Logger logger = LoggerFactory.getLogger(WalletLogController.class);
	@Autowired
	private WalletLogService walletLogService;
	@RequestMapping("/getByMid")
	@ResponseBody
	public BhResult getByMid(HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			WalletLog walletLog = new WalletLog();
			walletLog.setmId(member.getId());
			List<WalletLog> list = walletLogService.getByMid(walletLog);
			if (list.size() > 0) {
				double money = list.get(0).getAmount();
				list.get(0).setMoney2(money / 10 / 10 + "");// 分转成元
				return BhResult.build(200, "操作成功", list);
			} else {
				return BhResult.build(400, "没有记录", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######walletLog#######" + e);
			result = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return result;
		}
	}

	// 列表
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody WalletLog entity) {
		BhResult result = null;
		try {
			PageBean<WalletLog> page = walletLogService.listPage(entity);
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(page);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######listPage#######" + e);
			result = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return result;
		}
	}

	// 列表
	@RequestMapping("/WalletRecord")
	@ResponseBody
	public BhResult WalletRecord(@RequestBody WalletLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (member != null) {
				entity.setmId(member.getId());
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			PageBean<WalletLog> page = walletLogService.WalletRecord(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######WalletRecord#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}

}
