package com.bh.product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.ItemModelValue;
import com.bh.product.api.service.ItemModelValueService;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/itemModelValue")
public class ItemModelValueController {
	@Autowired
	private ItemModelValueService service;
	
	/**
	 * scj-20180320-07
	 * 新增
	 * @param entity
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			service.insert(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj-20180320-08
	 * 修改
	 * @param entity
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BhResult edit(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			service.edit(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj-20180320-09
	 * 获取
	 * @param entity
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			ItemModelValue data = service.get(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj-20180320-10
	 * 删除
	 * @param entity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			service.delete(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * zlk-2018.03.22
	 * 移动端 商品属性新增
	 * @param entity
	 * @return
	 */
	@RequestMapping("/mInsert")
	@ResponseBody
	public BhResult mInsert(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			service.insert(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * zlk-2018.03.22
	 *移动端 商品属性 修改
	 * @param entity
	 * @return
	 */
	@RequestMapping("/mEdit")
	@ResponseBody
	public BhResult mEdit(@RequestBody ItemModelValue entity) {
		BhResult r = null;
		try {
			service.edit(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
}
