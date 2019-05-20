package com.bh.user.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.AdsShop;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsShopCategory;
import com.bh.result.BhResult;
import com.bh.user.api.service.MallShopService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MyAdsShopPojo;
import com.bh.user.pojo.SimpleShop;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/mall")
public class MallShop {
	private static final Logger logger = LoggerFactory.getLogger(MallShop.class);

	@Autowired
	private MallShopService mallShopService;

	/**
	 * CHENG-2017-11-29-01 店铺商品的分类
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/goodsShopCategoryId")
	@ResponseBody
	public BhResult goodsShopCategory(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String shopId = map.get("shopId");// 商品的id
			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			} else {
				List<GoodsShopCategory> list = new ArrayList<>();
				GoodsShopCategory g = new GoodsShopCategory();
				g.setShopId(Integer.parseInt(shopId));
				g.setReid(0);
				list = mallShopService.goodsShopCategory(g);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######goodsShopCategoryId#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2017-11-29-01 店铺的信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/shopMsg")
	@ResponseBody
	public BhResult showShopMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(true).getAttribute(Contants.USER_INFO_ATTR_KEY);
			String shopId = map.get("shopId");// 商品的id
			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			} else {
				SimpleShop sim = new SimpleShop();
				sim = mallShopService.showShopMsg(Integer.parseInt(shopId), member);
				result = new BhResult(BhResultEnum.SUCCESS, sim);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######shopMsg#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2017-11-29-01 店铺的信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/shopads")
	@ResponseBody
	public BhResult shopAds(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			String shopId = map.get("shopId");// 商品的id
			String isPc = map.get("isPc");// 0-pc端图片，1-移动端图片
			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			} else {
				List<MyAdsShopPojo> list = new ArrayList<>();
				AdsShop ads = new AdsShop();
				ads.setShopId(Integer.parseInt(shopId));
				ads.setIsPc(Integer.parseInt(isPc));
				ads.setStatus(Byte.parseByte(String.valueOf(1)));
				list = mallShopService.shopads(ads);

				result = new BhResult(BhResultEnum.SUCCESS, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######shopads#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2017-11-29-01 用户看到的店铺的新品上线与热门产品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BhResult mallShopList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String shopId = map.get("shopId");
			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "店铺id不能为空");
			} else {
				Map<String, Object> myMap = mallShopService.list(Integer.parseInt(shopId));
				result = new BhResult(BhResultEnum.SUCCESS, myMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######list#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2017-11-29-01 全部商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/alllist")
	@ResponseBody
	public BhResult allList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			String name = map.get("name");
			String shopId = map.get("shopId");

			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "店铺id不能为空");
			} else {
				Goods goods = new Goods();
				String p = map.get("page");
				String r = map.get("size");
				Integer page = 0;
				Integer rows = 0;
				if (p == null || p.equals("")) {
					page = Contants.PAGE;
				} else {
					page = Integer.parseInt(p);
				}
				if (r == null || r.equals("")) {
					rows = Contants.SIZE;
				} else {
					rows = Integer.parseInt(r);
				}
				if (StringUtils.isNotEmpty(name)) {
					goods.setName(name);
				}
				goods.setShopId(Integer.parseInt(shopId));
				Map<String, Object> list = mallShopService.alllist(goods, page, rows);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2017-11-29-01 全部商品
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/alllistpc")
	@ResponseBody
	public BhResult allListPc(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			String name = map.get("name");
			String shopId = map.get("shopId");
			String shopCatId = map.get("shopCatId");
			String fz = map.get("fz");
			if (StringUtils.isEmpty(shopId)) {
				result = new BhResult(BhResultEnum.FAIL, "店铺id不能为空");
			} else {
				Goods goods = new Goods();
				String p = map.get("page");
				String r = map.get("size");
				Integer page = 0;
				Integer rows = 0;
				if (p == null || p.equals("")) {
					page = Contants.PAGE;
				} else {
					page = Integer.parseInt(p);
				}
				if (r == null || r.equals("")) {
					rows = Contants.SIZE;
				} else {
					rows = Integer.parseInt(r);
				}
				if (StringUtils.isNotEmpty(name)) {
					goods.setName(name);
				}
				if (StringUtils.isNotEmpty(shopCatId)) {
					goods.setShopCatId(Integer.parseInt(shopCatId));
				}
				goods.setShopId(Integer.parseInt(shopId));
				PageBean<Goods> list = new PageBean<Goods>();
				list = mallShopService.alllistPc(goods, page, rows, fz);
				result = new BhResult(BhResultEnum.SUCCESS, list);
			}

		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

}
