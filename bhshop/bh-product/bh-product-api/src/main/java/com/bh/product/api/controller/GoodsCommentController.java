package com.bh.product.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.CommentSimple;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsComment;
import com.bh.product.api.service.GoodsBrandService;
import com.bh.product.api.service.GoodsCommentService;
import com.bh.product.api.util.JedisUtil;
import com.bh.product.api.util.JedisUtil.Strings;
import com.bh.result.BhResult;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/goodsComment")
public class GoodsCommentController {
	
	@Autowired
	private GoodsCommentService service;
	
	/**
	 * SCJ-20171024-02
	 * 根据商品id查询所有评论
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectByGoodsId")
	@ResponseBody
	public BhResult selectByGoodsId(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");//商品id
			String shopId = map.get("shopId"); //商品所属店铺id
			String level = map.get("level");//评价等级--1好评,2中评,3差评
			String currentPage = map.get("currentPage"); //起始页
			String isAddEvaluate = map.get("isAddEvaluate"); //追评1
			String fz = map.get("fz"); //评价图 1
			PageBean<GoodsComment> list = service.selectByGoodsId(goodsId, shopId, level, isAddEvaluate, fz ,currentPage, Contants.PAGE_SIZE);
			if(list!=null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171024-04
	 * 根据不同条件统计所有评论
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectCount")
	@ResponseBody
	public BhResult selectCount(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");//商品id
			String shopId = map.get("shopId"); //商品所属店铺id
			HashMap<Integer, String> list = service.selectCount(goodsId, shopId);
			if(list!=null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171101-03
	 * 后台商品评论管理
	 * @param reid
	 * @return
	 */
	@RequestMapping("/pageByShopId")
	@ResponseBody
	public BhResult pageByShopId(@RequestBody GoodsComment entity, HttpServletRequest request){
		BhResult result = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 0;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			PageBean<GoodsComment> list = service.pageByShopId(entity, shopId);
			if(list!=null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171109-01
	 * 后台商品评论详情
	 * @param reid
	 * @return
	 */
	@RequestMapping("/commentDetails")
	@ResponseBody
	public BhResult commentDetails(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id");
			GoodsComment list = service.getCommentDetails(id);
			if(list!=null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS,list);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180112-02
	 * 后台添加商品评论
	 * @param reid
	 * @return
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BhResult insert(@RequestBody GoodsComment entity){
		BhResult result = null;
		try {
			entity.setIsDel(0);
			int row = service.insert(entity);
			if(row ==1 ){
				result = new BhResult(BhResultEnum.SUCCESS,null);
			}else{
				result = new BhResult(BhResultEnum.FAIL,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180112-03
	 * 显示和隐藏
	 * @param reid
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public BhResult changeStatus(@RequestBody GoodsComment entity){
		BhResult result = null;
		try {
			int row = service.changeStatus(entity);
			if(row ==1 ){
				result = new BhResult(BhResultEnum.SUCCESS,null);
			}else{
				result = new BhResult(BhResultEnum.FAIL,null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180113-01
	 * 删除
	 * @param reid
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody GoodsComment entity){
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
	 * cheng-20180130-01
	 *批量修改排序
	 * @param reid
	 * @return
	 */
	@RequestMapping("/updateCommentByBatch")
	@ResponseBody
	public BhResult updateCommentByBatch(@RequestBody Map<String, Object> map){
		BhResult r = null;
		try {
		
			 String json = JSONArray.fromObject(map.get("entity")).toString();//模型属性值
			// 判断是否为空
			List<GoodsComment> commentSimple = JsonUtils.jsonToList(json, GoodsComment.class);
			service.updateCommentByBatch(commentSimple);
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
	 * cheng-20180130-02
	 *增加人
	 * @param reid
	 * @return
	 */
	@RequestMapping("/selectPromoteUser")
	@ResponseBody
	public BhResult selectPromoteUser(@RequestBody PromoteUser entity){
		BhResult r = null;
		try {
	
		if (org.apache.commons.lang.StringUtils.isEmpty(entity.getCurrentPage())) {
			entity.setCurrentPage("1");
		}
			PageBean<PromoteUser> list = service.selectPromoteUser(entity);
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
