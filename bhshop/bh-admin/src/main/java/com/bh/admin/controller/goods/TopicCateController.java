package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.TopicCate;
import com.bh.admin.service.TopicCateService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/topicCate")
public class TopicCateController {
	@Autowired
	private TopicCateService service;
	
	/**
	 * SCJ-20180110-01
	 * 新增专题分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody TopicCate entity) {
	   BhResult result = null;
	   try {
			int row = service.add(entity);
		    if (row == 1) {
			   result = new BhResult(BhResultEnum.SUCCESS, null);
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
	 * SCJ-20180110-02
	 * 修改专题分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody TopicCate entity) {
	   BhResult result = null;
	   try {
			int row = service.update(entity);
		    if (row == 1) {
			   result = new BhResult(BhResultEnum.SUCCESS, null);
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
	 * SCJ-20180110-03
	 * 删除专题分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody TopicCate entity) {
	   BhResult result = null;
	   try {
			int row = service.delete(entity);
		    if (row == 1) {
			   result = new BhResult(BhResultEnum.SUCCESS, null);
			} else if(row == 1000){
				result = new BhResult(BhResultEnum.DELETE_BATCH_FAIL, null);
			}else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180110-04
	 * 专题分类列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody TopicCate entity) {
	   BhResult result = null;
	   try {
		    PageBean<TopicCate> page = service.listPage(entity);
		    if (page != null) {
			   result = new BhResult(BhResultEnum.SUCCESS, page);
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
	 * SCJ-20180111-01
	 * 专题分类详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody TopicCate entity) {
	   BhResult result = null;
	   try {
		    TopicCate cate = service.get(entity);
		    if (cate != null) {
			   result = new BhResult(BhResultEnum.SUCCESS, cate);
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
	 * SCJ-20180111-02
	 * 加载所有分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/listAll")
	@ResponseBody
	public BhResult listAll() {
	   BhResult result = null;
	   try {
		    List<TopicCate> cate = service.listAll();
		    if (cate != null) {
			   result = new BhResult(BhResultEnum.SUCCESS, cate);
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
	 * SCJ-20180111-03
	 * 专题活动添加所有分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/getLinkedAll")
	@ResponseBody
	public BhResult getLinkedAll() {
	   BhResult result = null;
	   try {
		    List<TopicCate> cate = service.getLinkedAll();
		    if (cate != null) {
			   result = new BhResult(BhResultEnum.SUCCESS, cate);
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
