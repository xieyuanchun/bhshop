package com.bh.product.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.product.api.service.GoodsShopCategoryService;
import com.bh.product.api.util.JedisUtil;
import com.bh.product.api.util.JedisUtil.Strings;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/goodsShopCategory")
public class GoodsShopCategoryController {
	
	@Autowired
	private GoodsShopCategoryService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20170906-04
	 * 分类详情
	 */
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			GoodsShopCategory goodsShopCategory = service.selectById(Integer.parseInt(id));
			if(goodsShopCategory!=null){
				result = new BhResult(200, "成功", goodsShopCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170906-06
	 * 根据父类id获取所有子分类名称
	 */
	@RequestMapping("/selectByParent")
	@ResponseBody
	public BhResult selectByParent(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String reid = map.get("reid");
			List<GoodsShopCategory> goodsCategory = service.selectByParent(reid);
			if(goodsCategory!=null){
				result = new BhResult(200, "成功", goodsCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170912-01
	 * 获取所有分类
	 * @return
	 */
	@RequestMapping("/selectAll")
	@ResponseBody
	public BhResult selectAll() {
		BhResult result = null;
		try {
			List<GoodsShopCategory> goodsCategory = service.selectAll();
			if(goodsCategory!=null){
				result = new BhResult(200, "成功", goodsCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20170906-02
	 * 获取分类列表
	 * @param map
	 * @return
	 */
	/*@RequestMapping("/selectByFirst")
	@ResponseBody
	public BhResult selectByFirst(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String name = map.get("name"); //查询条件
			String currentPage = map.get("currentPage");//当前第几页
		    if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
			    currentPage = "1";
		    }
		    String reid = map.get("reid"); //父级id
			//String pageSize = "10";
			PageBean<GoodsShopCategory> msg = service.selectByFirstReid(name, currentPage, pageSize, reid);
			if(msg!=null){
				result = new BhResult(200, "成功", msg);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}*/
	
	/**
	 * SCJ-20170905-02
	 * 获取所有分类列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectByFirst")
	@ResponseBody
	public BhResult selectByFirst() {
		BhResult result = null;
		try {
			List<GoodsShopCategory> msg = service.selectAllList();
			if(msg!=null){
				result = new BhResult(200, "成功", msg);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20170906-01
	 *添加分类 
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectParentInsert")
	@ResponseBody
	public BhResult selectParentInsert(@RequestBody Map<String, String> map, HttpServletRequest request){
		BhResult result = null;
		try {
			String name = map.get("name");
			String reid = map.get("reid");
			String sortnum = map.get("sortnum");
			String image = map.get("image");
			String series = map.get("series");
			String isLast = map.get("isLast");
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer shopId = (Integer)mapOne.get("shopId");
		    if(shopId == null){
		    	shopId = 1;
		    }
		    if(Integer.parseInt(series)>3){
				result = BhResult.build(400, "最多添加三级");
				return result;
			}
			int row = service.selectParentInsert(name, reid, sortnum, image, shopId, series, isLast);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else if(row ==1000){
				result = BhResult.build(400, "名称已存在");
			}else {
				result = BhResult.build(400, "添加失败");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170906-03
	 *修改分类 
	 * @param map
	 * @return
	 */
	@RequestMapping("/updateCategory")
	@ResponseBody
	public BhResult updateCategory(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			String name = map.get("name");
			String sortnum = map.get("sortnum");
			String reid = map.get("reid");
			String image = map.get("image");
			int row = service.updateCategory(id, name, sortnum, image, reid);
			if (row == 1) {
				result = new BhResult(200, "修改成功", null);
			} else if(row ==1000){
				result = BhResult.build(400, "名称已存在");
			}else {
				result = BhResult.build(400, "修改失败");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库修改失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20170906-05
	 *删除分类 
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteCategory")
	@ResponseBody
	public BhResult deleteCategory(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			int row = service.selectdelete(id);
			if (row == 0) {
				result = new BhResult(400, "请求失败", row);
			} else if(row == 998){
				result = new BhResult(400, "改分类已被商品绑定", row);
			}else if(row == 999){
				result = new BhResult(400, "请先删除子分类", row);
			}else{
				result = new BhResult(200, "删除成功", row);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20170927-03
	 * 获取三级分类
	 * @return
	 */
	@RequestMapping("/selectThreeLevel")
	@ResponseBody
	public BhResult selectThreeLevel() {
		BhResult result = null;
		try {
			List<GoodsShopCategory> goodsCategory = service.selectThreeLevel();
			if(goodsCategory!=null){
				result = new BhResult(200, "成功", goodsCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171016-02
	 * 获取最后一级分类
	 * @return
	 */
	@RequestMapping("/selectLastLevel")
	@ResponseBody
	public BhResult selectLastLevel() {
		BhResult result = null;
		try {
			List<GoodsShopCategory> goodsCategory = service.selectLastLevel();
			if(goodsCategory!=null){
				result = new BhResult(200, "成功", goodsCategory);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171107-02
	 * 商家后台分类列表管理(树形结构)
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectLinkedPage")
	@ResponseBody
	public BhResult selectLinkedPage(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			String name = map.get("name");
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 0;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			PageBean<GoodsShopCategory> msg = service.selectLinkedPage(currentPage, Contants.PAGE_SIZE, name, shopId);
			if(msg!=null){
				result = new BhResult(200, "成功", msg);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171107-03
	 * 获取所有分类列表(树形结构)
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectLinkedList")
	@ResponseBody
	public BhResult selectLinkedList(HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 0;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			List<GoodsShopCategory> msg = service.selectLinkedList(shopId);
			if(msg!=null){
				result = new BhResult(200, "成功", msg);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
}
