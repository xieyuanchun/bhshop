package com.bh.admin.controller.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.admin.service.SeedScoreRuleService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/score")
public class SeedScoreRuleController {

	@Autowired
	private SeedScoreRuleService seedScoreRuleService;

	/**
	 * cheng-20180110-04 积分列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BhResult list(@RequestBody SeedScoreRule entity) {
		BhResult bhResult = null;
		try {

			bhResult = seedScoreRuleService.selectList(entity);

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	/**
	 * cheng-20180110-04 修改用户的积分
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult updateScore(@RequestBody SeedScoreRule entity) {
		BhResult bhResult = null;
		try {
			
			bhResult = seedScoreRuleService.updateScore(entity);

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	/**
	 * cheng-20180110-04 添加积分规则
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody SeedScoreRule entity) {
		BhResult bhResult = null;
		try {
			bhResult = seedScoreRuleService.updateScore(entity);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

}
