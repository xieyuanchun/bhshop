package com.bh.product.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.TopicType;
import com.bh.product.api.service.TopicTypeService;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/topicType")
public class TopicTypeController {
	
	@Autowired	
	private TopicTypeService topicTypeService;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * @Description: 根据条件查询活动类型列表
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	@RequestMapping("/getTopicTypeList")
	@ResponseBody
	public BhResult getTopicTypeList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			String id = map.get("id");// 活动类型id
			String name = map.get("name");//活动名称
			String status=map.get("status");//活动状态 （1开启，0关闭）
			if (StringUtils.isEmptyOrWhitespaceOnly(currentPage)) {
				currentPage = "1";
			}
			PageBean<TopicType> pageTopicType = topicTypeService.getTopicTypeList(currentPage,pageSize,id,name,status);

			if (pageTopicType != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageTopicType);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * 
	 * @Description: 添加活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	@RequestMapping("/addTopicType")
	@ResponseBody
	public BhResult addExtFileCate(@RequestBody TopicType topicType) {
		BhResult result = null;
		try {
			int row = topicTypeService.addTopicType(topicType);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
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
	 * @Description: 更新活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	@RequestMapping("/updateTopicType")
	@ResponseBody
	public BhResult updateTopicType(@RequestBody TopicType topicType) {
		BhResult result = null;
		try {
			int row = topicTypeService.updateTopicType(topicType);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
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
	 * SCJ-20180111-04
	 * 专题活动添加获取所有活动分类
	 * @return
	 */
	@RequestMapping("/listAll")
	@ResponseBody
	public BhResult listAll(@RequestBody TopicType entity) {
		BhResult result = null;
		try {
			List<TopicType> list = topicTypeService.listAll(entity);
			if (list != null) {
				result = new BhResult(BhResultEnum.SUCCESS, list);
			} else {
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
	 * @Description:删除活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	@RequestMapping("/deleteTopicType")
	@ResponseBody
	public BhResult deleteTopicType(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Integer row = topicTypeService.deleteTopicType(id);
			if (row == 0) {
				result = BhResult.build(400, "请求失败");
			} else if(row==1){
				result = BhResult.build(200, "删除成功", row);
			}else if(row==-1){
				result = BhResult.build(400, "不允许删除，被引用");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * @Description: 根据条件查询活动类型列表
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	@RequestMapping("/getTopicTypeById")
	@ResponseBody
	public BhResult getTopicTypeById(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			
			String id = map.get("id");// 活动类型id
			TopicType topicType=topicTypeService.getTopicTypeById(id);
			if (topicType != null) {
				result = new BhResult(BhResultEnum.SUCCESS, topicType);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
}
