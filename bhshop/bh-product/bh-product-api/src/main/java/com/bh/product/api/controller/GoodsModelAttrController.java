package com.bh.product.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.bh.goods.pojo.GoodsModelAttr;
import com.bh.product.api.service.GoodsModelAttrService;


@Controller
@RequestMapping("/modelAttr")
public class GoodsModelAttrController {

	@Autowired
	private GoodsModelAttrService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20170928-05
	 *  平台后台模型属性值列表
	 * @return
	 */
	@RequestMapping("/selectPageBymId")
	@ResponseBody
	public BhResult selectPageBymId(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String mId = map.get("modelId");
		    String currentPage = map.get("currentPage");
			PageBean<GoodsModelAttr> page = service.selectPageBymId(mId, currentPage, pageSize);
			   if (page !=null) {
				   result = new BhResult(200, "查询成功", page);
				} else {
				   result = BhResult.build(400, "暂无数据！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	/**
	 * SCJ-20170928-06
	 * 模型属性值的添加
	 * @param map
	 * @return
	 */
	@RequestMapping("/insertModelAttr")
	@ResponseBody
	public BhResult insertModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String name = map.get("name");
		   	String mId = map.get("modelId");
		   	String value = map.get("value");
		   	String search = map.get("search"); //是否支持搜索0-不支持1，支持
			int row = service.insertModelAttr(mId, name, search, value);
			   if (row == 1) {
				   result = new BhResult(200, "添加成功", row);
				} else if(row == 1000){
					result = BhResult.build(400, "该名称已存在！");
				}else{
				   result = BhResult.build(400, "添加失败！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库添加失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20170928-07
	 * 模型属性的修改
	 * @param map
	 * @return
	 */
	@RequestMapping("/editModelAttr")
	@ResponseBody
	public BhResult editModelAttr(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id");
		   	String name = map.get("name");
		   	String search = map.get("search"); //是否支持搜索0-不支持1，支持
			String value = map.get("value");
			int row = service.editModelAttr(id ,name, search, value);
			 if (row == 1) {
				   result = new BhResult(200, "修改成功", row);
				} else if(row == 1000){
					result = BhResult.build(400, "名称已存在");
				}else{
				   result = BhResult.build(400, "修改失败！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库修改失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170928-07
	 * 模型属性的删除
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteModelAttr")
	@ResponseBody
	public BhResult deleteModelAttr(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id");
			int row = service.deleteModelAttr(id);
			   if (row == 1) {
				   result = new BhResult(200, "删除成功", row);
				} else if(row == 2){
					result = BhResult.build(400, "请先删除与该属性值绑定的商品！");
				}else {
				   result = BhResult.build(400, "删除失败！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库删除失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
}