package com.bh.admin.controller.goods;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.JdGoodsNotice;
import com.bh.admin.service.JdGoodsNoticeService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.jd.api.JDGoodsApi;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/jdGoodsNotice")
public class JdGoodsNoticeController {
	private static final Logger logger = LoggerFactory.getLogger(JdGoodsNoticeController.class);
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
	public BhResult pageList(@RequestBody JdGoodsNotice entity){
		BhResult r = null;
		try {
			PageBean<JdGoodsNotice> data = service.pageList(entity);
			r = new BhResult();
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setData(data);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
			return r;
		}
		return r;
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
