package com.bh.product.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.JdGoodsNotice;
import com.bh.jd.api.JDGoodsApi;
import com.bh.product.api.service.JdGoodsNoticeService;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/jdGoodsNotice")
public class JdGoodsNoticeController {
	@Autowired
	private JdGoodsNoticeService service;
	
	/**
	 * SCJ-20171225-02
	 * 商品变更消息列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String name = map.get("name");//查询条件：商品名称
			String type = map.get("type"); //消息类型(0降价  1升价)',
			String isRead = map.get("isRead"); //'是否已读(0未读 1已读)',
			String currentPage = map.get("currentPage"); //起始页
			if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				currentPage = "1";
			}
			PageBean<JdGoodsNotice> page = service.pageList(currentPage, Contants.PAGE_SIZE, name, type, isRead);
			if(page!=null){
				result = new BhResult(BhResultEnum.SUCCESS, page);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171226-01
	 * 已阅
	 * @param map
	 * @return
	 */
	@RequestMapping("/changeReadStatus")
	@ResponseBody
	public BhResult chageReadStatus(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			int row = service.changeReadStatus(id);
			if(row > 0){
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
