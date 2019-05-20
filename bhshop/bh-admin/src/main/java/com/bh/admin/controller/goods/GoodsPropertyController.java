package com.bh.admin.controller.goods;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.GoodsProperty;
import com.bh.admin.service.GoodsPropertyService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/property")
public class GoodsPropertyController {
	@Autowired
	private GoodsPropertyService service;
	
	/**
	 * SCJ-20180201-01
	 * @param entity
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			int row = service.add(entity);
			r = new BhResult();
			if (row == 2) {
				r.setStatus(400);
				r.setMsg("该属性名称已存在");
			}else{
				r.setStatus(200);
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
			}
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
	 * SCJ-20180201-02
	 * @param entity
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody GoodsProperty entity) {
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
	 * SCJ-20180201-03
	 * @param entity
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			GoodsProperty row = service.get(entity);
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
	 * SCJ-20180201-04
	 * @param entity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody GoodsProperty entity) {
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
	 * 属性管理列表
	 * SCJ-20180201-05
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			PageBean<GoodsProperty> row = service.listPage(entity);
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
	 * 商品新增加载属性值列表
	 * SCJ-20180201-06
	 * @param entity
	 * @return
	 */
	@RequestMapping("/selectByType")
	@ResponseBody
	public BhResult selectByType(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			List<GoodsProperty> row = service.selectByType(entity);
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
	 * 新增商品时加载所有的属性
	 *cheng-201802012-12
	 * @param entity
	 * @return
	 */
	@RequestMapping("/selectAllPro")
	@ResponseBody
	public BhResult selectAllPro(@RequestBody GoodsProperty entity) {
		BhResult r = null;
		try {
			List<GoodsProperty> row = service.selectAllPro(entity);
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
