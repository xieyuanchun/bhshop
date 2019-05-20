package com.bh.user.api.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.result.BhResult;
import com.bh.user.api.service.CheckUserService;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/check")
public class CheckUserController {
	private static final Logger logger = LoggerFactory.getLogger(CheckUserController.class);
	@Autowired 
	private CheckUserService checkUserService;
	
	@RequestMapping(value ="/checkshopstatus",method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkshopstatus(@RequestBody Map<String, String > map) {
		BhResult result = null;		
		try {
			String pageSize = map.get("pageSize");//每页的大小
			String currentPage = map.get("currentPage");//当前页
			if (StringUtils.isEmpty(pageSize) && StringUtils.isEmpty(currentPage)) {
				pageSize ="1";
				currentPage ="10";
			}
			PageBean<MemberShop> bean = checkUserService.selectAllShopByStatus(pageSize,currentPage);
			
			if (bean !=null ) {
				result = new BhResult(200, "获取数据成功", bean);
			} else {
				result = new BhResult(400, "没有数据", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checkshopstatus#######" + e);
			result = BhResult.build(500, "数据库更新失败!");
		}
		return result;
	}
	
	@RequestMapping(value ="/checksendtatus",method = RequestMethod.POST)
	@ResponseBody
	public BhResult checksendtatus(@RequestBody Map<String, String > map) {
		BhResult result = null;		
		try {
			String pageSize = map.get("pageSize");//每页的大小
			String currentPage = map.get("currentPage");//当前页
			if (StringUtils.isEmpty(pageSize) && StringUtils.isEmpty(currentPage)) {
				pageSize ="1";
				currentPage ="10";
			}		
			PageBean<MemberSend> bean = checkUserService.selectAllSendByStatus(pageSize, currentPage);
			if (bean !=null ) {
				result = new BhResult(200, "获取数据成功", bean);
			} else {
				result = new BhResult(400, "没有数据", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######checksendtatus#######" + e);
			result = BhResult.build(500, "数据库更新失败!");
		}
		return result;
	}
	
	@RequestMapping(value ="/updateshopstatus",method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateShopStatus(@RequestBody Map<String, String> map) {
		BhResult result = null;		
		try {
			String mId = map.get("mId");
			MemberShop memberShop = new MemberShop();
			memberShop.setmId(Integer.parseInt(mId));
			memberShop.setStatus(4);
			int row = checkUserService.updateShopStatus(memberShop);
			if (row >0) {
				result = new BhResult(200, "审核成功", null);
			} else {
				result = new BhResult(400, "审核失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updateshopstatus#######" + e);
			result = BhResult.build(500, "数据库更新失败!");
		}
		return result;
	}
	
	@RequestMapping(value ="/updatesendstatus",method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateSendStatus(@RequestBody Map<String, String> map,HttpRequest request) {
		BhResult result = null;		
		try {
			String mId = map.get("mId");
			MemberSend memberSend = new MemberSend();
			memberSend.setmId(Integer.parseInt(mId));
			memberSend.setStatus(0);//状态0为已经审核1未待审核
			int row = checkUserService.updateSendStatus(memberSend);
			if (row >0) {
				result = new BhResult(200, "审核成功", null);
			} else {
				result = new BhResult(400, "审核失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updatesendstatus#######" + e);
			result = BhResult.build(500, "数据库更新失败!");
		}
		return result;
	}
	
	
	
}
