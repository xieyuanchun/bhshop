package com.bh.admin.controller.goods;

import java.util.List;
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
import com.bh.admin.pojo.goods.GoodsModel;
import com.bh.admin.pojo.goods.GoodsModelAttr;
import com.bh.admin.service.GoodsModelService;
import com.bh.enums.BhResultEnum;


@Controller
@RequestMapping("/goodsModel")
public class GoodsModelController {

	@Autowired
	private GoodsModelService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20170926-01
	 * 用户端查询条件标签
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectByCatId")
	@ResponseBody
	public BhResult selectBycatId(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   String catid = map.get("catid");
			List<GoodsModelAttr> list = service.selectBycatId(catid);
			   if (list !=null) {
				   result = new BhResult(200, "查询成功", list);
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
	 * SCJ-20170927-01
	 * 商品添加获取所有属性标签
	 * @return
	 */
	@RequestMapping("/selectAllModel")
	@ResponseBody
	public BhResult selectAllModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String catid= map.get("catid");
			List<GoodsModel> list = service.selectAllModel(catid);
			   if (list !=null) {
				   result = new BhResult(200, "查询成功", list);
				} else {
				   result = BhResult.build(400, "暂无数据！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170928-01
	 *  平台后台商品属性列表
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public BhResult selectAllList(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String currentPage = map.get("currentPage");
		    String name = map.get("name");
		    String catid = map.get("catid");
			PageBean<GoodsModel> page = service.selectAllList(currentPage, pageSize, name, catid);
			   if (page !=null) {
				   result = new BhResult(200, "查询成功", page);
				} else {
				   result = BhResult.build(400, "暂无数据！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	/**
	 * SCJ-20170928-02
	 * 商品模型的添加
	 * @param map
	 * @return
	 */
	@RequestMapping("/insertModel")
	@ResponseBody
	public BhResult insertModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String name = map.get("name");
		   	String catId = map.get("catid");
		   	String row = service.insertModel(name, catId);
			   if (row.equals("yes")) {
				   result = new BhResult(BhResultEnum.SUCCESS,null);
				} else if(row.equals("no")){
					result = new BhResult(BhResultEnum.FAIL,null);
				}else {
					result = new BhResult(BhResultEnum.NAME_EXIST, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL,null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20170928-03
	 * 商品模型的修改
	 * @param map
	 * @return
	 */
	@RequestMapping("/editModel")
	@ResponseBody
	public BhResult editModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id");
		   	String name = map.get("name");
		   	String catId = map.get("catid");
			String row = service.editModel(id ,name ,catId);
			if (row.equals("yes")) {
				   result = new BhResult(BhResultEnum.SUCCESS,null);
				} else if(row.equals("no")){
					result = new BhResult(BhResultEnum.FAIL,null);
				}else {
					result = new BhResult(BhResultEnum.NAME_EXIST, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL,null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170928-04
	 * 商品模型的删除
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteModel")
	@ResponseBody
	public BhResult deleteModel(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id");
			int row = service.deleteModel(id);
			   if (row == 1) {
				   result = new BhResult(200, "删除成功", row);
				} else if(row ==2){
					result = BhResult.build(400, "请先删除子类属性！");
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
