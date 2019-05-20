package com.bh.product.api.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.GoodsTag;
import com.bh.product.api.service.GoodsTagService;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/tag")
public class GoodsTagController {
	@Autowired
	private GoodsTagService service;
	
	/**
	 * SCJ-20180202-01
	 * @param entity
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody GoodsTag entity) {
		BhResult r = null;
		try {
			int row = service.add(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * SCJ-20180202-02
	 * @param entity
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody GoodsTag entity) {
		BhResult r = null;
		try {
			int row = service.update(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * SCJ-20180202-03
	 * @param entity
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody GoodsTag entity) {
		BhResult r = null;
		try {
			GoodsTag row = service.get(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * SCJ-20180202-04
	 * @param entity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody GoodsTag entity) {
		BhResult r = null;
		try {
			int row = service.delete(entity);
			r = new BhResult();
			if(row == 999){
				r.setStatus(BhResultEnum.DELETE_EXIT.getStatus());
				r.setMsg(BhResultEnum.DELETE_EXIT.getMsg());
				r.setData(row);
				return r;
			}
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * 标签管理列表
	 * SCJ-20180202-05
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody GoodsTag entity) {
		BhResult r = null;
		try {
			PageBean<GoodsTag> row = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * 商品新增获取标签
	 * SCJ-20180202-06
	 * @param entity
	 * @return
	 */
	@RequestMapping("/selectAll")
	@ResponseBody
	public BhResult selectAll() {
		BhResult r = null;
		try {
			List<GoodsTag> row = service.selectAll();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
}
