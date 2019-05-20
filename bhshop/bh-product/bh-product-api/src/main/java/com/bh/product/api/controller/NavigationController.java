package com.bh.product.api.controller;

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
import com.bh.product.api.service.NavigationService;
import com.bh.result.BhResult;
import com.bh.user.vo.NavigationVo;

@Controller
@RequestMapping("/navigation")
public class NavigationController {
	private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);
	@Autowired
	private NavigationService navigationService;
	
	@RequestMapping("/shownavigation")
	@ResponseBody
	public BhResult addFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String usingObject=map.get("usingObject");
			if (StringUtils.isBlank(usingObject)) {
				return BhResult.build(400, "参数usingObject不能为空");
			}
			List<NavigationVo> list=navigationService.getNavigationMsg(Integer.parseInt(usingObject));
			result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######shownavigation#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
}
