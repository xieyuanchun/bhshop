package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.MessageQSet;
import com.bh.admin.service.MessageQSetService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;

@Controller
@RequestMapping("/messageQSet")
public class MessageQSetController {
	@Autowired
	private MessageQSetService service;
	
	/**
	 * SCJ-20180108-01
	 * 设置接受消息人
	 * @param map
	 * @return
	 */
	@RequestMapping("/insertQSet")
	@ResponseBody
	public BhResult insertQSet(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String sendTo = map.get("sendTo");
		    String type = map.get("type");
		    String remark = map.get("remark");
		    int row = service.insertQSet(sendTo, type, remark);
		    if (row ==1) {
			   result = new BhResult(BhResultEnum.SUCCESS, null);
			}else if(row ==-1){
			   result = new BhResult(BhResultEnum.ERROR_EMAIL, null);
			}else if(row == -2){
				result = new BhResult(BhResultEnum.ERROR_MOBILE, null);
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
	
	/**
	 * SCJ-20180109-03
	 * 接收消息账号管理
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String type = map.get("type");
		    String currentPage = map.get("currentPage");
		    PageBean<MessageQSet> list = service.pageList(currentPage, Contants.PAGE_SIZE, type);
		    if (list!=null) {
			   result = new BhResult(BhResultEnum.SUCCESS, list);
			}else {
			   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180108-04
	 * 设置接受消息人
	 * @param map
	 * @return
	 */
	@RequestMapping("/updateQSet")
	@ResponseBody
	public BhResult updateQSet(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id");
		    String sendTo = map.get("sendTo");
		    String type = map.get("type");
		    String remark = map.get("remark");
		    int row = service.updateQSet(id, sendTo, remark, type);
		    if (row ==1) {
				   result = new BhResult(BhResultEnum.SUCCESS, null);
				}else if(row == -1){
				   result = new BhResult(BhResultEnum.ERROR_EMAIL, null);
				}else if(row == -2){
					result = new BhResult(BhResultEnum.ERROR_MOBILE, null);
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
	
	
	/**
	 * SCJ-20180108-05
	 * 删除接受消息人
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteQSet")
	@ResponseBody
	public BhResult deleteQSet(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id");
		    int row = service.deleteQSet(id);
		    if (row ==1) {
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
