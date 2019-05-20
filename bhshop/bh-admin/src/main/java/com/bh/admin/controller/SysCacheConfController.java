package com.bh.admin.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.user.SysCacheConf;
import com.bh.admin.service.SysCacheConfService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;
import com.bh.utils.cache.CacheConfig;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/sysCacheConf")
public class SysCacheConfController {
	private static final Logger logger = LoggerFactory.getLogger(SysCacheConfController.class);
	@Autowired
	private SysCacheConfService service;
	
	/**
	 * <p>Description: 添加</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody SysCacheConf entity){
		BhResult r= null;
		try {
			service.add(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * <p>Description: 编辑</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BhResult edit(@RequestBody SysCacheConf entity){
		BhResult r= null;
		try {
			service.edit(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * <p>Description: 删除</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody SysCacheConf entity){
		BhResult r= null;
		try {
			service.delete(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * <p>Description: 详情</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody SysCacheConf entity){
		BhResult r= null;
		try {
			SysCacheConf data = service.get(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * <p>Description: 列表管理</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody SysCacheConf entity){
		BhResult r= null;
		try {
			PageBean<SysCacheConf> data = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * <p>Description: 根据组名-定时更新缓存表数据</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/loadSysCacheConf")
	@ResponseBody
	public BhResult loadSysCacheConf(@RequestBody Map<String, String[]> map){
		BhResult r= null;
		try {
			String[] params = map.get("params");
			service.loadSysCacheConf(params);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * <p>Description: 读取缓存测试</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/loadCacheTest")
	@ResponseBody
	public BhResult loadCacheTest(@RequestBody Map<String, String> map){
		BhResult r= null;
		try {
			String key = map.get("key");
			Object obj = CacheConfig.getIfPresent(key);
			if(obj!=null){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(obj);
			}else{
				String data = "从数据库获取！";
				CacheConfig.put(key, data);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(data);
			}
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * <p>Description: 缓存测试2</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	@RequestMapping("/cacheTest")
	@ResponseBody
	public BhResult cacheTest(@RequestBody Map<String, String> map){
		BhResult r= null;
		try {
			String key = map.get("key");
			String data = service.getValByConfigKey(key);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			r = BhResult.build(500, "数据库操作异常！");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
}
