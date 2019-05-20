package com.bh.user.api.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.result.BhResult;
import com.bh.user.api.service.AreasService;
import com.bh.user.pojo.Areas;
import com.bh.user.pojo.MemberUserAddress;

@Controller
@RequestMapping("/area")
public class AreasController {
	private static final Logger logger = LoggerFactory.getLogger(AreasController.class);
	@Autowired
	private AreasService areaService;
	@RequestMapping("/getAllInfo")
	@ResponseBody
	public BhResult getAllInfo() {
		BhResult result = null;
		try {
			List<Areas> msg = areaService.getAllInfo();
			if (msg != null) {
				result = new BhResult(200, "数据获取成功", msg);
			} else {
				result = new BhResult(400, "暂无数据！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getAllInfo#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	@RequestMapping("/getAllJDInfo")
	@ResponseBody
	public BhResult getAllJDInfo() {
		BhResult result = null;
		try {
			List<GoAddressArea> msg = areaService.getAllJDInfo();
			if (msg != null) {
				result = new BhResult(200, "数据获取成功", msg);
			} else {
				result = new BhResult(400, "暂无数据！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getAllJDInfo#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	@RequestMapping("/getAllProvince")
	@ResponseBody
	public BhResult getAllProvince() {
		BhResult result = null;
		try {
			List<MemberUserAddress> msg = areaService.getProvince();
			if (msg != null) {
				result = new BhResult(200, "数据获取成功", msg);
			} else {
				result = new BhResult(400, "暂无数据！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getAllProvince#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	@RequestMapping("/getCity")
	@ResponseBody
	public BhResult getCity(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String areaId = map.get("provinceId");
			if (areaId.isEmpty()) {
				result = BhResult.build(400, "区域id不能为空！");
			}
			List<MemberUserAddress> msg = areaService.getCity(areaId);
			if (msg != null) {
				result = new BhResult(200, "数据获取成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getCity#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	@RequestMapping("/getTown")
	@ResponseBody
	public BhResult getTown(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String areaId = map.get("cityId");
			if (areaId.isEmpty()) {
				result = BhResult.build(400, "区域id不能为空！");
			}
			List<MemberUserAddress> msg = areaService.getTown(areaId);
			if (msg != null) {
				result = new BhResult(200, "数据获取成功", msg);
			} else {
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getTown#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	/**
	 * SCJ-20180106-01 递归查询省市区
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectByLevel")
	@ResponseBody
	public BhResult selectByLevel(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String parentId = map.get("parentId"); // 父级id
			String fz = map.get("fz"); // 1-省级，2-市级，3-区级
			List<Areas> list = areaService.selectByLevel(parentId, fz);
			if (list != null) {
				result = new BhResult(BhResultEnum.SUCCESS, list);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectByLevel#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
}
