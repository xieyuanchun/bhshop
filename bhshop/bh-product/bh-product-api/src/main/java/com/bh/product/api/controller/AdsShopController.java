package com.bh.product.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.AdsShop;
import com.bh.product.api.service.AdsShopService;
import com.bh.product.api.util.JedisUtil;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/adsShop")
public class AdsShopController {
	@Autowired
	private AdsShopService service;
	
	/**
	 * 	SCJ-20171129-02
	 * 店铺轮播图的新增
	 * @param map
	 * @return
	 */
	@RequestMapping("/addAdsShop")
	@ResponseBody
	public BhResult addAdsShop(@RequestBody Map<String, String> map, HttpServletRequest request){
		BhResult result = null;
		try {
			String name = map.get("name");  //广告名称
			String image = map.get("image"); //广告图片
			String content = map.get("content"); // 广告内容
			String link = map.get("link"); //广告链接
			String sortnum = map.get("sortnum"); //广告排序
			String goodsId = map.get("goodsId"); //商品id
			String isPc = map.get("isPc"); //0-pc,1-移动端
			String status = map.get("status"); //是否启用'状态 0禁用 1正常',
			
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer shopId = (Integer)mapOne.get("shopId");
		    if(shopId == null){
		    	shopId = 1;
		    }
			int row = service.addShopAds(name, image, content, link, sortnum, isPc, shopId, goodsId, status);
			if(row == 1){
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 	SCJ-20171129-03
	 *  店铺轮播图的修改
	 * @param map
	 * @return
	 */
	@RequestMapping("/editAdsShop")
	@ResponseBody
	public BhResult editAdsShop(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //广告id
			String name = map.get("name");  //广告名称
			String image = map.get("image"); //广告图片
			String content = map.get("content"); // 广告内容
			String link = map.get("link"); //广告链接
			String sortnum = map.get("sortnum"); //广告排序
			String goodsId = map.get("goodsId"); //商品id
			String status = map.get("status"); //是否启用'状态 0禁用 1正常',
			String isPc = map.get("isPc"); //'0-pc端图片，1-移动端图片',
			
			int row = service.editShopAds(id, name, image, content, link, sortnum, goodsId, status, isPc);
			if(row == 1){
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 	SCJ-20171129-04
	 *  店铺轮播图的删除
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteAdsShop")
	@ResponseBody
	public BhResult deleteAdsShop(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //广告id
			
			int row = service.delectShopAds(id);
			if(row == 1){
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 	SCJ-20171129-05
	 *  店铺轮播图分页列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map, HttpServletRequest request){
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage"); //广告id
			String name = map.get("name");  //广告名称
			String isPc = map.get("isPc"); //0-pc,1-移动端
			
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer shopId = (Integer)mapOne.get("shopId");
		    if(shopId == null){
		    	shopId = 0;
		    }
			PageBean<AdsShop> page = service.pageList(currentPage, Contants.PAGE_SIZE, name, isPc, shopId);
			result = new BhResult(BhResultEnum.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 	SCJ-20171129-06
	 *  店铺轮播图的详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/shopAdsDetails")
	@ResponseBody
	public BhResult shopAdsDetails(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //广告id
			AdsShop adsShop = service.shopAdsDetails(id);
			if(adsShop != null){
				result = new BhResult(BhResultEnum.SUCCESS, adsShop);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
