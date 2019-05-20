package com.bh.admin.controller.goods;

import java.util.Date;
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
import com.bh.admin.mapper.order.ExtFilesMapper;
import com.bh.admin.pojo.goods.Ads;
import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.service.AdsService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/ads")
public class AdsController {
	
	@Autowired
	private AdsService service;
	@Autowired
    private ExtFilesMapper extFilesMapper;//文件
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20170930-02
	 * 移动端二级页面广告
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("adId"); //菜单id
			List<Ads> ads = service.selectById(id);
			if(ads!=null){
				result = new BhResult(200, "成功",ads);
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
	 * SCJ-20171011-05
	 * 首页广告轮播图
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectListByIsMain")
	@ResponseBody
	public BhResult selectListByIsMain(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String isPc = map.get("isPc");
			List<Ads> ads = service.selectListByIsMain(isPc);
			if(ads!=null){
				result = new BhResult(200, "成功",ads);
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
	 * SCJ-20171011-06
	 * 后台广告管理列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String isPc = map.get("isPc"); //0-pc端，1-移动端
			String isMain = map.get("isMain"); //0二级页面广告 ，1主页广告
			String name = map.get("name"); //广告名称
			String fz = map.get("fz"); //排序 -1按排序号
			String currentPage = map.get("currentPage");
			PageBean<Ads> list = service.pageList(isPc, isMain, name, fz, currentPage, Contants.PAGE_SIZE);
			if(list!=null){
				result = new BhResult(200, "成功",list);
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
	 * 	SCJ-20171017-03
	 * 后台广告的新增
	 * @param map
	 * @return
	 */
	@RequestMapping("/addAds")
	@ResponseBody
	public BhResult addAds(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String name = map.get("name");  //广告名称
			String image = map.get("image"); //广告图片
			String content = map.get("content"); // 广告内容
			String link = map.get("link"); //广告链接
			String sLink = map.get("sLink"); //小程序广告链接
			String sortnum = map.get("sortnum"); //广告排序
			String isMain = map.get("isMain"); //是否是首页广告0不是，1是
			String isPc = map.get("isPc"); //0-pc,1-移动端
			int row = service.addAds(name, image, content, link,sLink, sortnum, isMain, isPc);
			if(row == 0){
				result = new BhResult(400, "新增失败",null);
			}else{
				result = new BhResult(200, "新增成功",null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171017-02
	 * 后台广告的编辑
	 * @param map
	 * @return
	 */
	@RequestMapping("/editAds")
	@ResponseBody
	public BhResult editAds(@RequestBody Ads ads){
		BhResult result = null;
		try {			
			int row = service.editAds(ads);
			if(row == 0){
				result = new BhResult(BhResultEnum.FAIL,null);
			}else if(row == 1000){
				result = new BhResult(BhResultEnum.OTHER_FAIL,null);
			}else{
				result = new BhResult(BhResultEnum.SUCCESS,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * SCJ-20171012-02
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
			   String row = service.batchDelete(id);
				   if (row.equals("删除失败")) {
					   result = new BhResult(BhResultEnum.DELETE_FAIL, null);
					} else if(row.equals("删除成功")){
					   result = new BhResult(BhResultEnum.DELETE_SUCCESS, null);
					}else {
						result = new BhResult(BhResultEnum.DELETE_FAIL, row);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20171101-01
	 * 广告的删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteAds")
	@ResponseBody
	public BhResult deleteAds(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   int row = service.deleteAds(id);
				   if (row == 0) {
					   result = new BhResult(BhResultEnum.DELETE_FAIL, null);
					} else{
					   result = new BhResult(BhResultEnum.DELETE_SUCCESS, null);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20171101-02
	 * 广告的禁用和启用
	 * @param id
	 * @return
	 */
	@RequestMapping("/startAds")
	@ResponseBody
	public BhResult startAds(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   String status = map.get("status"); //0禁用，1启用
			   int row = service.startAds(id, status);
				   if (row == 0) {
					   result = new BhResult(BhResultEnum.FAIL, null);
					} else{
					   result = new BhResult(BhResultEnum.SUCCESS, null);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20171016-04
	 * 添加或菜单时加载广告列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public BhResult getList(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String menuId = map.get("menuId");
			List<Ads> list = service.getList(menuId);
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
	 * SCJ-20171209-01
	 * 添加广告时加载排序号
	 * @param map
	 * @return
	 */
	@RequestMapping("/getSortNum")
	@ResponseBody
	public BhResult getSortNum(){
		BhResult result = null;
		try {
			List<String> list = service.getSortNum();
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
	 * SCJ-20171209-02
	 * 修改广告时加载排序号
	 * @param map
	 * @return
	 */
	@RequestMapping("/getChangeSortNum")
	@ResponseBody
	public BhResult getChangeSortNum(){
		BhResult result = null;
		try {
			List<String> list = service.getChangeSortNum();
			if(list!=null){
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171209-03
	 * 获取当前最大排序号
	 * @param map
	 * @return
	 */
	@RequestMapping("/getBigSortNum")
	@ResponseBody
	public BhResult getBigSortNum(){
		BhResult result = null;
		try {
			int row = service.getBigSortNum();
			if(row >= 0){
				result = new BhResult(BhResultEnum.SUCCESS, row);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
