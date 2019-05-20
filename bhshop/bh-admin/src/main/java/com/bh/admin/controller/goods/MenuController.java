package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.Menu;
import com.bh.admin.service.MenuService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private MenuService service;
	
	/**
	 * SCJ-20171011-01
	 * 新增菜单
	 * @param map
	 * @return
	 */
	@RequestMapping("/addMenu")
	@ResponseBody
	public BhResult addMenu(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String name = map.get("name");
			String image = map.get("image");
			String content = map.get("content");
			String link = map.get("link");
			String sortnum = map.get("sortnum");
			String series = map.get("series");
			String adId = map.get("adId");
			String reid = map.get("reid");
			int row  = service.addMenu(name, image, content, link, sortnum, series, adId, reid);
			if(row == 0){
				result = new BhResult(400, "添加失败",null);
			}else{
				result = new BhResult(200, "添加成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171011-02
	 * 修改菜单
	 * @param map
	 * @return
	 */
	@RequestMapping("/editMenu")
	@ResponseBody
	public BhResult editMenu(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			String name = map.get("name");
			String image = map.get("image");
			String content = map.get("content");
			String link = map.get("link");
			String sortnum = map.get("sortnum");
			String adId = map.get("adId");
			String series = map.get("series");
			String reid = map.get("reid");
			int row  = service.editMenu(id, name, image, content, link, sortnum, adId, series, reid);
			if(row == 0){
				result = new BhResult(400, "修改失败",null);
			}else{
				result = new BhResult(200, "修改成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171011-03
	 * 菜单分页列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/menuPageList")
	@ResponseBody
	public BhResult menuPageList(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			PageBean<Menu> list = service.menuPageList(currentPage, Contants.PAGE_SIZE);
			if(list != null){
				result = new BhResult(200, "获取成功",list);
			}else{
				result = new BhResult(400, "获取失败", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171011-04
	 * 批量删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public BhResult batchDelete(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   int row =service.batchDelete(id);
				   if (row ==0) {
					   result = new BhResult(BhResultEnum.DELETE_FAIL,null);
					} else if(row == 998){
						result = new BhResult(BhResultEnum.DELETE_BATCH_FAIL,null);
					}else{
						result = new BhResult(BhResultEnum.DELETE_SUCCESS,null);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL,null);
					LoggerUtil.getLogger().error(e.getMessage());
					e.printStackTrace();
				}
		return result;
	}
	
	/**
	 * SCJ-201718-02
	 * 后台根据父类id获取下级菜单列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getListByReid")
	@ResponseBody
	public BhResult getListByReid(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			String reid = map.get("reid");
			PageBean<Menu> list = service.getListByReid(currentPage, Contants.PAGE_SIZE, reid);
			if(list != null){
				result = new BhResult(200, "获取成功",list);
			}else{
				result = new BhResult(400, "获取失败", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171010-06
	 * 移动端获取所有一级菜单
	 * @param map
	 * @return
	 */
	@RequestMapping("/menuList")
	@ResponseBody
	public BhResult menuList(){
		BhResult result = null;
		try {
			List<Menu> list = service.menuList();
			if(list != null){
				result = new BhResult(200, "获取成功",list);
			}else{
				result = new BhResult(400, "获取失败", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171018-01
	 * 移动端获取所有二级菜单
	 * @param map
	 * @return
	 */
	@RequestMapping("/menuTwoLevelList")
	@ResponseBody
	public BhResult menuTwoLevelList(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String reid = map.get("id");
			List<Menu> list = service.menuTwoLevelList(reid);
			if(list != null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
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
	 * SCJ-20171110-01
	 * 获取所有菜单列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectLinkedList")
	@ResponseBody
	public BhResult selectLinkedList() {
		BhResult result = null;
		try {
			List<Menu> msg = service.selectLinkedList();
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
