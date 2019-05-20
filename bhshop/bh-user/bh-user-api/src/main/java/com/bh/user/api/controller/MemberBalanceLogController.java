package com.bh.user.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberBalanceLogService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBalanceLog;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/balanceLog")
public class MemberBalanceLogController {
	private static final Logger logger = LoggerFactory.getLogger(MemberBalanceLogController.class);

	@Autowired
	private MemberBalanceLogService service;
	/**
	 * SCJ-20180324 个人钱包收支明细
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody MemberBalanceLog entity, HttpServletRequest request) {
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
			PageBean<MemberBalanceLog> page = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######listPage#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
}
