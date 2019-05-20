package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.GoAddressArea;
import com.bh.admin.service.JDAreaService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.bh.jd.bean.area.CheckArea;

@Controller
@RequestMapping("/api")
public class JDAreaController {
	
	@Autowired
	private JDAreaService jdAreaService;
	
	
	/**
	 * 
	 * 获取所有的省
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/getProvince")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			GoAddressArea area = new GoAddressArea();
			area.setParentId(0);
			List<GoAddressArea> list = jdAreaService.selectGoAddressByParams(area);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * 获取所有的省:微信端
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/mGetProvince")
	@ResponseBody
	public BhResult mSelectById(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			GoAddressArea area = new GoAddressArea();
			area.setParentId(0);
			List<GoAddressArea> list = jdAreaService.mSelectGoAddressByParams(area);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 
	 * 获取二级区域
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/getCity")
	@ResponseBody
	public BhResult getCity(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String parentId = map.get("id");
			GoAddressArea area = new GoAddressArea();
			if (StringUtils.isNotEmpty(parentId)) {
				area.setParentId(Integer.parseInt(parentId));
				List<GoAddressArea> list = jdAreaService.selectGoAddressByParams(area);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}else{
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			}
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * 获取三级区域
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/getCounty")
	@ResponseBody
	public BhResult getCounty(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String parentId = map.get("id");
			GoAddressArea area = new GoAddressArea();
			if (StringUtils.isNotEmpty(parentId)) {
				area.setParentId(Integer.parseInt(parentId));
				List<GoAddressArea> list = jdAreaService.selectGoAddressByParams(area);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}else{
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			}
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 
	 * 获取四级区域
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/getTown")
	@ResponseBody
	public BhResult getTown(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String parentId = map.get("id");
			GoAddressArea area = new GoAddressArea();
			if (StringUtils.isNotEmpty(parentId)) {
				area.setParentId(Integer.parseInt(parentId));
				List<GoAddressArea> list = jdAreaService.selectGoAddressByParams(area);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}else{
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			}
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 
	 * 获取四级区域
	 * @param 
	 * @return
	 */
	@RequestMapping("/area/checkArea")
	@ResponseBody
	public BhResult CheckArea(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String provinceId = map.get("provinceId");
			String cityId = map.get("cityId");
			String countyId = map.get("countyId");
			String townId = map.get("townId");
			
		
			CheckArea ch = jdAreaService.checkArea(provinceId, cityId, countyId, townId);
			result = new BhResult(BhResultEnum.SUCCESS, ch);
			return result;
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
