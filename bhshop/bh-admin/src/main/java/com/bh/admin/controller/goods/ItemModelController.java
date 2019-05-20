package com.bh.admin.controller.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.ItemModel;
import com.bh.admin.service.ItemModelService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/itemModel")
public class ItemModelController {
	@Autowired
	private ItemModelService service;
	
	/**
	 * scj-20180320-01
	 * 新增
	 * @param entity
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody ItemModel entity) {
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
	 * scj-20180320-02
	 * 修改
	 * @param entity
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BhResult edit(@RequestBody ItemModel entity) {
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
	 * scj-20180320-03
	 * 详情
	 * @param entity
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody ItemModel entity) {
		BhResult r = null;
		try {
			ItemModel data = service.get(entity);
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
	 * scj-20180320-04
	 * 删除
	 * @param entity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody ItemModel entity) {
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
	 * scj-20180320-05
	 * 列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody ItemModel entity) {
		BhResult r = null;
		try {
			PageBean<ItemModel> data = service.listPage(entity);
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
	 * scj-20180320-06
	 * 根据分类加载模型
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getByCatId")
	@ResponseBody
	public BhResult getByCatId(@RequestBody ItemModel entity) {
		BhResult r = null;
		try {
			ItemModel data = service.getByCatId(entity);
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
	 * zlk-2018.03.22
	 *移动端   根据分类加载模型
	 * @param entity
	 * @return
	 */
	@RequestMapping("/mGetByCatId")
	@ResponseBody
	public BhResult mGetByCatId(@RequestBody ItemModel entity) {
		BhResult r = null;
		try {
			ItemModel data = service.getByCatId(entity);
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
	
}
