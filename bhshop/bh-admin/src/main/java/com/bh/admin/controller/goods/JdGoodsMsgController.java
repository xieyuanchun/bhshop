package com.bh.admin.controller.goods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.JdGoodsMsg;
import com.bh.admin.service.JdGoodsMsgService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/jdGoodsMsg")
public class JdGoodsMsgController {
	private static final Logger logger = LoggerFactory.getLogger(JdGoodsMsgController.class);
	@Autowired
	private JdGoodsMsgService service;
	
	/**
	 * <p>Description: 京东变更消息列表</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("listPage")
	@ResponseBody
	public BhResult jdGoodsListPage(@RequestBody JdGoodsMsg entity){
		BhResult r = null;
		try {
			PageBean<JdGoodsMsg> data = service.listPage(entity);
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
	 * <p>Description: 已阅</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("isLook")
	@ResponseBody
	public BhResult isLook(@RequestBody JdGoodsMsg entity){
		BhResult r = null;
		try {
			service.isLook(entity);
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
	 * <p>Description: 消息删除</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("delete")
	@ResponseBody
	public BhResult delete(@RequestBody JdGoodsMsg entity){
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
	 * <p>Description: 上下架消息</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("receiveUpOrDownMsg")
	@ResponseBody
	public BhResult receiveUpOrDownMsg(){
		BhResult r = null;
		try {
			service.receiveUpOrDownMsg();
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
	 * <p>Description: 删除新增消息</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("receiveDeleteOrAddMsg")
	@ResponseBody
	public BhResult receiveDeleteOrAddMsg(){
		BhResult r = null;
		try {
			service.receiveDeleteOrAddMsg();
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
	 * <p>Description: 价格变动消息</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("receivePriceChangeMsg")
	@ResponseBody
	public BhResult receivePriceChangeMsg(){
		BhResult r = null;
		try {
			service.receivePriceChangeMsg();
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
	 * <p>Description: 参数及介绍变更消息</p>
	 *  @author scj  
	 *  @date 2018年5月30日
	 */
	@RequestMapping("introduceChangeMsg")
	@ResponseBody
	public BhResult introduceChangeMsg(){
		BhResult r = null;
		try {
			service.introduceChangeMsg();
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
	 * <p>Description:京东商品下架删除逻辑处理Test </p>
	 *  @author scj  
	 *  @date 2018年7月19日
	 */
	@RequestMapping("jdDownDeleteTest")
	@ResponseBody
	public BhResult jdDownDeleteTest(@RequestBody JdGoodsMsg entity){
		BhResult r = null;
		try {
			int row = service.jdDownDeleteTest(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
}
