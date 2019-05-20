package com.bh.admin.controller.goods;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.CouponGift;
import com.bh.admin.service.CouponGiftService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/couponGift")
public class CouponGiftController {
	@Autowired
	private CouponGiftService service;
	
	/**
	 * <p>
	 * Description: 优惠券大礼包的新增
	 * </p>
	 * 
	 * @author scj
	 * @date 2018年6月5日
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody CouponGift entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map map = JSON.parseObject(userJson, Map.class);
			Object sId = map.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			entity.setShopId(shopId);
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
	 * <p>Description: 优惠券大礼包的编辑</p>
	 *  @author scj  
	 *  @date 2018年6月5日
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BhResult edit(@RequestBody CouponGift entity){
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
	 * <p>Description: 优惠券大礼包的删除</p>
	 *  @author scj  
	 *  @date 2018年6月5日
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody CouponGift entity){
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
	 * <p>Description: 优惠券大礼包详情</p>
	 *  @author scj  
	 *  @date 2018年6月5日
	 */
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody CouponGift entity){
		BhResult r = null;
		try {
			CouponGift data = service.get(entity);
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
	 * <p>Description: 优惠券大礼包管理列表</p>
	 *  @author scj  
	 *  @date 2018年6月5日
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody CouponGift entity,HttpServletRequest request){
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map map = JSON.parseObject(userJson, Map.class);
			Object sId = map.get("shopId");
			Integer shopId = null;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			entity.setShopId(shopId);
			PageBean<CouponGift> data = service.listPage(entity);
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
	 * <p>Description: 大礼包编辑时删除优惠券</p>
	 *  @author scj  
	 *  @date 2018年6月5日
	 */
	@RequestMapping("/deleteAndGift")
	@ResponseBody
	public BhResult deleteAndGift(@RequestBody Map<String, String> map){
		BhResult r = null;
		try {
			String id = map.get("id");
			service.deleteAndGift(Integer.parseInt(id));
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
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody Map<String, String> map){
		BhResult r = null;
		try {
			String id = map.get("id");
			String isWhiteList = map.get("isWhiteList");
			CouponGift c = new CouponGift();
			c.setId(Integer.valueOf(id));
			c.setIsWhiteList(Integer.valueOf(isWhiteList));
			service.update(c);
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
