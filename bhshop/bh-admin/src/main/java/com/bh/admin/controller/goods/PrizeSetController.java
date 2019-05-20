package com.bh.admin.controller.goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.PrizeSet;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.PrizeSetService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/prizeSet")
public class PrizeSetController {
	@Autowired
	private PrizeSetService service;
	
	/**
	 * SCJ-20180315-01 
	 * 中奖设置新增
	 * @param entity
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody PrizeSet entity) {
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
	 * SCJ-20180315-02 
	 * 中奖设置编辑
	 * @param entity
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BhResult edit(@RequestBody PrizeSet entity) {
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
	 * SCJ-20180315-03 
	 * 中奖设置获取
	 * @param entity
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody PrizeSet entity) {
		BhResult r = null;
		try {
			PrizeSet data = service.get(entity);
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
	 * SCJ-20180315-04 
	 * 中奖设置删除
	 * @param entity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody PrizeSet entity) {
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
	 * SCJ-20180315-05
	 * 中奖设置列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody PrizeSet entity) {
		BhResult r = null;
		try {
			PageBean<PrizeSet> page = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	/**
	 * SCJ-20180315-06
	 * 中奖算法
	 * @param entity
	 * @return
	 */
	@RequestMapping("/priceIsGet")
	@ResponseBody
	public BhResult priceIsGet(@RequestBody PrizeSet entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			if(member!= null){
				String data = service.priceIsGet(entity, member.getId());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(data);
				return r;
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * zlk-20180315-05
	 * 中奖设置和当前的商品列表
	 * @param entity
	 * @return
	 */
	@RequestMapping("/getGoodAndPrizeListPage")
	@ResponseBody
	public BhResult getGoodAndPrizeListPage(@RequestBody PrizeSet entity) {
		BhResult r = null;
		try {
			PageBean<PrizeSet> page = service.getGoodAndPrize(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
}
