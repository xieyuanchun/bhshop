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
import com.bh.admin.service.GoodsBrandService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/goodsBrand")
public class GoodsBrandController {
	
	@Autowired
	private GoodsBrandService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20170906-08
	 * 根据分类id查询所有品牌
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectByCatid")
	@ResponseBody
	public BhResult selectByCatid(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String catId = map.get("catId");
			List<GoodsBrand> list = service.selectByCatid(catId);
			if(list!=null){
				result = new BhResult(list);
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
	 * SCJ-20170906-11
	 * 品牌详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/detailsGoodsBrand")
	@ResponseBody
	public BhResult detailsGoodsBrand(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			GoodsBrand goodsBrand = service.details(id);
			if(goodsBrand!=null){
				result = new BhResult(goodsBrand);
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
	 * SCJ-20170906-10
	 * 品牌列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageAll")
	@ResponseBody
	public BhResult pageAll(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   String name = map.get("name");//查询条件
		   String currentPage = map.get("currentPage");//当前第几页
		   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
			   currentPage = "1";
		   }
		   PageBean<GoodsBrand> msg = service.pageAll(name, currentPage, pageSize);
			   if (msg !=null) {
				   result = new BhResult(200, "成功", msg);
				} else {
				   result = BhResult.build(200, "暂无数据！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170906-07
	 * 添加品牌
	 * @param map
	 * @return
	 */
	@RequestMapping("/addGoodsBrand")
	@ResponseBody
	public BhResult addGoodsBrand(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String name = map.get("name");
			String logo = map.get("logo");
			String sortnum = map.get("sortnum");
			String catId = map.get("catId");
			int row = service.addGoodsBrand(name, logo, sortnum, catId);
			if(row ==0){
				result = new BhResult(BhResultEnum.FAIL, null);
			}else if(row == 1000){
				result = new BhResult(BhResultEnum.BRAND_EXIT, null);
			}else{
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171227-01
	 * 添加京东品牌
	 * @param map
	 * @return
	 */
	@RequestMapping("/addJdGoodsBrand")
	@ResponseBody
	public BhResult addJdGoodsBrand(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //从京东摘取品牌时填写
			String name = map.get("name");
			String logo = map.get("logo");
			String sortnum = map.get("sortnum");
			String catId = map.get("catId");
			int row = service.addJdGoodsBrand(id, name, logo, sortnum, catId);
			if(row ==0){
				result = new BhResult(400, "请求失败", null);
			}else{
				result = new BhResult(200, "添加成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171010-04
	 * 修改品牌
	 * @param map
	 * @return
	 */
	@RequestMapping("/editGoodsBrand")
	@ResponseBody
	public BhResult editGoodsBrand(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			String name = map.get("name");
			String logo = map.get("logo");
			String sortnum = map.get("sortnum");
			String catId = map.get("catId");
			int row = service.editGoodsBrand(id, name, logo, sortnum, catId);
			if(row ==0){
				result = new BhResult(BhResultEnum.FAIL, null);
			}else if(row == 1000){
				result = new BhResult(BhResultEnum.BRAND_EXIT, null);
			}else{
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170906-09
	 * 品牌的批量删除
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteGoodsBrand")
	@ResponseBody
	public BhResult deleteGoodsBrand(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			int row = service.batchDelete(id);
			if(row == 0){
				result = new BhResult(400, "请求失败", null);
			}else if(row == 999){
				result = new BhResult();
				result.setStatus(BhResultEnum.BRAND_CHOICED.getStatus());
				result.setMsg(BhResultEnum.BRAND_CHOICED.getMsg());
			}else{
				result = new BhResult(200, "删除成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20170927-04
	 * 获取所有品牌
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectAll")
	@ResponseBody
	public BhResult selectAll(){
		BhResult result = null;
		try {
			List<GoodsBrand> list = service.selectAll();
			if(list!=null){
				result = new BhResult(list);
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
