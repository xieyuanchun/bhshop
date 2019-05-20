package com.bh.admin.controller.goods;

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

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.Navigation;
import com.bh.admin.service.NavigationService;
import com.bh.admin.util.JedisUtil;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;

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
		   String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			if (shopId==1) {
				String usingObject=map.get("usingObject");
				String currentPage=map.get("currentPage");
				String pageSize=map.get("pageSize");
				if (StringUtils.isBlank(usingObject)) {
					return BhResult.build(400, "参数usingObject不能为空");
				}
				List<Navigation> list=navigationService.selectList(Integer.parseInt(usingObject),currentPage,pageSize);
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}else{
				List<Navigation> list=new ArrayList<>();
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######shownavigation#######" + e.getMessage());
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody Navigation navigation ,HttpServletRequest request){
		BhResult result = null;
		try {
		     navigationService.insertSelect(navigation);
		     result=new BhResult(BhResultEnum.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######insert#######" + e.getMessage());
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody Navigation navigation ,HttpServletRequest request){
		BhResult result = null;
		try {
		     navigationService.updateSelect(navigation);
		     result=new BhResult(BhResultEnum.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######update#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
	
}
