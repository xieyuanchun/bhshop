package com.bh.product.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.GoodsSaleP;
import com.bh.product.api.service.CouponService;
import com.bh.product.api.service.GoodsSaleService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Wallet;

@Controller
@RequestMapping("/goodSale")
public class GoodsSaleController {

	@Autowired	
	private GoodsSaleService goodsSaleService;
	
	// 获取
	@RequestMapping("/add")
	@ResponseBody
	public BhResult get(@RequestBody GoodsSaleP entity) {
		BhResult r = null;
		try {
			GoodsSaleP g = new GoodsSaleP();
			g.setMax(entity.getMax()*100);
			g.setMin(entity.getMin()*100);
			List<GoodsSaleP> list = goodsSaleService.get(g);
			
			GoodsSaleP g1 = new GoodsSaleP();
			g1.setMax(entity.getMax()*100);
			List<GoodsSaleP> list1 = goodsSaleService.get(g1);
			
			GoodsSaleP g2 = new GoodsSaleP();
			g2.setMin(entity.getMin()*100);
			List<GoodsSaleP> list2 = goodsSaleService.get(g2);
			
			if(list.size()>0||list1.size()>0||list2.size()>0){
				return r = new BhResult(400,"添加的区间不能与已有的相同",null);
			}else{
				entity.setMax(entity.getMax()*100);
				entity.setMin(entity.getMin()*100);
				goodsSaleService.add(entity);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}

	}
	
	//修改
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody GoodsSaleP entity) {
		BhResult r = null;
		try {
			GoodsSaleP g = new GoodsSaleP();
			g.setMax(entity.getMax()*100);
			g.setMin(entity.getMin()*100);
			List<GoodsSaleP> list = goodsSaleService.get(g);
			
			GoodsSaleP g1 = new GoodsSaleP();
			g1.setMax(entity.getMax()*100);
			List<GoodsSaleP> list1 = goodsSaleService.get(g1);
			
			GoodsSaleP g2 = new GoodsSaleP();
			g2.setMin(entity.getMin()*100);
			List<GoodsSaleP> list2 = goodsSaleService.get(g2);
			
			if(list.size()>0||list1.size()>0||list2.size()>0){
				return r = new BhResult(400,"更改的区间不能与已有的相同",null);
			}else{
				entity.setMax(entity.getMax()*100);
				entity.setMin(entity.getMin()*100);
				goodsSaleService.update(entity);
			}
			
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	//删除
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody GoodsSaleP entity) {
		BhResult r = null;
		try {
			goodsSaleService.delete(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	//分页显示
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody GoodsSaleP entity) {
		BhResult result = null;
		try {
			 Map<String,Object> map = goodsSaleService.list(entity);
			 result = new BhResult();
			 result.setStatus(BhResultEnum.SUCCESS.getStatus());
			 result.setMsg(BhResultEnum.SUCCESS.getMsg());
			 result.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return result;
		}
		return result;
	}
	
	
	
}
