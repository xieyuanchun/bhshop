package com.bh.user.api.controller;

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
import com.bh.result.BhResult;
import com.bh.user.api.service.SeedGameService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.SeedGame;
import com.bh.user.pojo.SeedParam;

@Controller
@RequestMapping("/seed")
public class SeedGameController {
	private static final Logger logger = LoggerFactory.getLogger(SeedGameController.class);
	@Autowired
	private SeedGameService seedGameService;

	/**
	 * CHENG-201802-08游戏界面
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/gamemsg")
	@ResponseBody
	public BhResult gamemsg(@RequestBody SeedGame seedGame, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (seedGame.getmId() != null) {
				Member member = seedGameService.selectMember(seedGame.getmId());
				SeedGame seedGame2 = seedGameService.selectMemberMsg(member);
				bhResult = new BhResult(200, "查询成功", seedGame2);
			} else {
				bhResult = new BhResult(400, "mId不能为空", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######gamemsg#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	/**
	 * CHENG-201802-08收割
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shouge")
	@ResponseBody
	public BhResult shouge(@RequestBody SeedParam param, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (StringUtils.isNotEmpty(param.getId())) {
				// 用户的id,动作类型1收割,2浇水,3铲地
				bhResult = seedGameService.shouge(param);
			} else {
				bhResult = new BhResult(400, "mId不能为空", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######shouge#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	/**
	 * CHENG-2018027-03仓库列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/storehouselist")
	@ResponseBody
	public BhResult storeHouseList(@RequestBody SeedParam param, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (param.getmId() != null) {
				bhResult = seedGameService.storeHouseList(param);
			} else {
				bhResult = new BhResult(400, "mId不能为空", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######storehouselist#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

	/**
	 * CHENG-20180228-01浇水
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/wateraction")
	@ResponseBody
	public BhResult waterAction(@RequestBody SeedParam param, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (param.getId() != null) {
				// 用户的id,动作类型1收割,2浇水,3铲地
				bhResult = seedGameService.waterAction(param);
			} else {
				bhResult = new BhResult(400, "mId不能为空", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######wateraction#######" + e);
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}

}
