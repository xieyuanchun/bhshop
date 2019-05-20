package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.GoodsBrand;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.service.GoodsCategoryService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/goodsCategory")
public class GoodsCategoryController {
	
	@Autowired
	private GoodsCategoryService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	
	/**
	 * SCJ-20170905-04
	 * 分类详情
	 */
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			GoodsCategory goodsCategory = service.selectById(id);
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
	 * SCJ-20170905-06
	 * 根据父类id获取所有子分类名称
	 */
	@RequestMapping("/selectByParent")
	@ResponseBody
	public BhResult selectByParent(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String reid = map.get("reid");
			List<GoodsCategory> goodsCategory = service.selectByParent(reid);
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
	 * SCJ-20170905-02
	 * 获取分类列表
	 * @param map
	 * @return
	 *//*
	@RequestMapping("/selectByFirst")
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
			   PageBean<GoodsCategory> msg = service.selectByFirstReid(name, currentPage, pageSize, reid);
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
			List<GoodsCategory> msg = service.selectAllList();
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
	 * SCJ-20170905-01
	 *添加分类 
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectParentInsert")
	@ResponseBody
	public BhResult selectParentInsert(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String name = map.get("name");
			String reid = map.get("reid");
			String sortnum = map.get("sortnum");
			String image = map.get("image");
			String isLast = map.get("isLast");
			String series = map.get("series");
			String flag = map.get("flag");
			if(Integer.parseInt(series)>3){
				result = BhResult.build(400, "最多添加三级");
				return result;
			}
			int row = service.selectParentInsert(name, reid, sortnum, image, flag, isLast, series);
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
	 * SCJ-20170905-03
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
			String sortnum = map.get("sortnum");
			String image = map.get("image");
			String name = map.get("name");
			String flag = map.get("flag");
			int row = service.updateCategory(id,  sortnum, image, flag,name);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else if(row ==1000){
				result = BhResult.build(400, "名称已存在");
			}else {
				result = BhResult.build(400, "添加失败");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库修改失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20170905-05
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
			String row = service.selectdelete(id);
			if (row.equals("删除失败")) {
				result = new BhResult(BhResultEnum.DELETE_FAIL, row);
			} else if(row.equals("该分类已与商品绑定")){
				result = new BhResult(BhResultEnum.GOODS_BINDING, row);
			}else if(row.equals("该分类已被品牌所绑定")){
				result = new BhResult(BhResultEnum.BRAND_BINDING, row);
			}else if(row.equals("请先删除子分类")){
				result = new BhResult(BhResultEnum.DELETE_SUBCATEGORY, row);
			}else if(row.equals("该分类已被模型所绑定")){
				result = new BhResult(BhResultEnum.MODEL_BINDING, row);
			}else{
				result = new BhResult(BhResultEnum.DELETE_SUCCESS, row);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170921-1
	 * PC首页导航分类
	 * @return
	 */
	@RequestMapping("/homePageClassify")
	@ResponseBody
	public BhResult homePageClassify() {
		BhResult result = null;
		try {
			List<GoodsCategory> list = service.homePageList();
			if(list!=null){
				result = new BhResult(200, "成功", list);
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
	 * SCJ-20170925-01
	 * 获取所有分类
	 * @return
	 */
	@RequestMapping("/selectAll")
	@ResponseBody
	public BhResult selectAll() {
		BhResult result = null;
		try {
			List<GoodsCategory> goodsCategory = service.selectAll();
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
	 * SCJ-20171016-05
	 * 商品添加时获取所有分类
	 * @return
	 */
	@RequestMapping("/selectAllByCatId")
	@ResponseBody
	public BhResult selectAllByCatId(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String catId = map.get("catId");
			List<GoodsCategory> goodsCategory = service.selectAllByCatId(catId);
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
	 * SCJ-20170925-02
	 *分类排序
	 * @param map
	 * @return
	 */
	@RequestMapping("/changeSortNum")
	@ResponseBody
	public BhResult changeSortNum(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			String sortNum = map.get("sortNum");
			int row = service.changeSortNum(id, sortNum);
			if (row == 0) {
				result = new BhResult(400, "请求失败", null);
			} else{
				result = new BhResult(200, "修改排序成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库修改失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170927-02
	 * 获取第三级分类
	 * @return
	 */
	@RequestMapping("/selectThreeLevel")
	@ResponseBody
	public BhResult selectThreeLevel() {
		BhResult result = null;
		try {
			List<GoodsCategory> goodsCategory = service.selectThreeLevel();
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
	 * SCJ-20171016-01
	 * 获取最后一级分类
	 * @return
	 */
	@RequestMapping("/selectLastLevel")
	@ResponseBody
	public BhResult selectLastLevel() {
		BhResult result = null;
		try {
			List<GoodsCategory> goodsCategory = service.selectLastLevel();
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
	 * SCJ-20170930-08
	 * 首页分类显示前6条商品
	 * @return
	 */
	@RequestMapping("/selectTopSixGoodsBycatId")
	@ResponseBody
	public BhResult selectTopSixGoodsBycatId() {
		BhResult result = null;
		try {
			List<GoodsCategory> goodsCategory = service.selectTopSixGoodsBycatId();
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
	 * SCJ-20171020-01
	 * 移动端根据父类id获取下级分类及商品数量
	 * @return
	 */
	@RequestMapping("/selectCategoryAndNumByParent")
	@ResponseBody
	public BhResult selectCategoryAndNumByParent(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String reid = map.get("reid");
			List<GoodsCategory> goodsCategory = service.selectCategoryAndNumByParent(reid);
			if(goodsCategory!=null){
				result = new BhResult(BhResultEnum.SUCCESS, goodsCategory);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * SCJ-20171107-04
	 * 商家后台分类列表管理(树形结构)
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectLinkedPage")
	@ResponseBody
	public BhResult selectLinkedPage(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			String name = map.get("name");
			PageBean<GoodsCategory> msg = service.selectLinkedPage(currentPage, Contants.PAGE_SIZE, name);
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
	 * SCJ-20171107-05
	 * 获取所有分类列表(树形结构)
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectLinkedList")
	@ResponseBody
	public BhResult selectLinkedList() {
		BhResult result = null;
		try {
			List<GoodsCategory> msg = service.selectLinkedList();
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
	 * 测试分类查询
	 * 根据分类id递归查询所有子分类
	 * @param map
	 * @return
	 */
	@RequestMapping("/getAllCateList")
	@ResponseBody
	public BhResult getAllCateList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String reid = map.get("reid");
			List<GoodsCategory> msg = service.getAllCateList(Long.parseLong(reid));
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
	 * SCJ-20171207-01
	 * 移动端获取所有一级菜单
	 * @param map
	 * @return
	 */
	@RequestMapping("/getFirstLevelList")
	@ResponseBody
	public BhResult getFirstLevelList() {
		BhResult result = null;
		try {
			List<GoodsCategory> list = service.getFirstLevelList();
			if(list!=null){
				result = new BhResult(BhResultEnum.SUCCESS, list);
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
	 * SCJ-20171207-02
	 * 移动端分类模块根据分类获取下级分类及商品
	 * @param map
	 * @return
	 */
	@RequestMapping("/getCateListAndGoods")
	@ResponseBody
	public BhResult getCateListAndGoods(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			String catId = map.get("catId");
			Map<String, Object> list = service.getCateListAndGoods(Contants.PAGE_SIZE, currentPage, catId);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171227-02
	 * 京东商品分类摘取
	 * @param map
	 * @return
	 */
	@RequestMapping("/insertJdCategory")
	@ResponseBody
	public BhResult insertJdCategory(@RequestBody Map<String, Object> map){
		BhResult result = null;
		try {
			JSONArray list = JSONArray.fromObject(map.get("list"));
			service.insertJdCategory(list);
			result = new BhResult(BhResultEnum.SUCCESS, null);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 根据分类id查品牌
	 * zlk
	 */
	@RequestMapping("/getBrandByCategory")
	@ResponseBody
	public BhResult getBrandByCategory(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			Long catId = Long.parseLong(id);
			List<GoodsBrand> list = service.getBrandByCategory(catId);
			if(list.size()>0){
				result = new BhResult(200,"查询成功！",list);
			}else{
				result = new BhResult(200,"当前分类下没有对应的品牌！",null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查所有分类
	 * zlk
	 */
	@RequestMapping("/getAllCategory")
	@ResponseBody
	public BhResult getAllCategory(){
		BhResult result = null;
		try {
			
			List<GoodsCategory> list = service.getAllCategory();
			if(list.size()>0){
				result = new BhResult(200,"查询成功！",list);
			}else{
				result = new BhResult(200,"失败！",null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/getByLevel")
	@ResponseBody
	public BhResult insert(@RequestBody GoodsCategory entity) {
		BhResult r = null;
		try {
			List<GoodsCategory> list = service.getByLevel(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	@RequestMapping("/getByReid")
	@ResponseBody
	public BhResult getByReid(@RequestBody GoodsCategory entity) {
		BhResult r = null;
		try {
			List<GoodsCategory> list = service.getByReid(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
}
