package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.TopicDauction;
import com.bh.admin.pojo.goods.TopicDauctionPrice;
import com.bh.admin.service.TopicDauctionService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/dauction")
public class TopicDauctionController {
	@Autowired
	private TopicDauctionService service;
	
	/**
	 * scj-20180327-04
	 * 拍卖配置
	 * @param entity
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody TopicDauction entity) {
		BhResult r = null;
		try {
			int row = service.insert(entity);
			if(row == 999){
				r = new BhResult();
				r.setStatus(BhResultEnum.GOODS_EXCIT.getStatus());
				r.setMsg(BhResultEnum.GOODS_EXCIT.getMsg());
				return r;
			}
			if(row == 888){
				r = new BhResult();
				r.setStatus(BhResultEnum.GOODS_EXCIT_NOT_DOWN.getStatus());
				r.setMsg(BhResultEnum.GOODS_EXCIT_NOT_DOWN.getMsg());
				return r;
			}
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
	 * scj-20180327-05
	 * 定时检查变更商品价格
	 * @return
	 */
	@RequestMapping("/changeGoodsPrice")
	@ResponseBody
	public BhResult changeGoodsPrice() {
		BhResult r = null;
		try {
			service.changeGoodsPrice();
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
	 * scj-20180328-01
	 * 拍卖获取当前时间点和价格
	 * @return
	 */
	@RequestMapping("/getPointAndPrice")
	@ResponseBody
	public BhResult getPointAndPrice(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			List<TopicDauctionPrice> list= service.getPointAndPrice(Integer.parseInt(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj-20180409-01
	 * 拍卖配置详情
	 * @return
	 */
	@RequestMapping("/dauctionDetail")
	@ResponseBody
	public BhResult dauctionDetail(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			Map<String, Object> list= service.dauctionDetail(Integer.parseInt(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
}
