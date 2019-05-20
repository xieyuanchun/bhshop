package com.bh.user.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.goods.pojo.GoodsFavorite;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.order.pojo.OrderInfoPojo;
import com.bh.result.BhResult;
import com.bh.user.api.service.UserFavoriteService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShopFavorite;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/userfavorite")
public class UserFavoriteController {
	private static final Logger logger = LoggerFactory.getLogger(UserFavoriteController.class);
	@Autowired
	private UserFavoriteService userFavoriteService;

	/**
	 * CHENG-20171120-01 新增用户收藏
	 * 
	 * @param map(goodsId-商品的id,skuId-sku的id)
	 * @return
	 */
	@RequestMapping("/addgoodsfavorite")
	@ResponseBody
	public BhResult addFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");
			String skuId = map.get("skuId");
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);

			if (StringUtils.isEmpty(goodsId)) {
				result = new BhResult(400, "商品的id不能为空", null);
			} else if (StringUtils.isEmpty(skuId)) {
				result = new BhResult(400, "skuId不能为空", null);
			} else {
				GoodsFavorite favorite = new GoodsFavorite();
				favorite.setmId(member.getId());
				favorite.setGoodsId(Integer.parseInt(goodsId));
				favorite.setSkuId(Integer.parseInt(skuId));
				int row = userFavoriteService.addGoodFavorite(favorite);
				if (row == 0) {
					result = new BhResult(BhResultEnum.FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addgoodsfavorite#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171120-02 显示用户收藏列表
	 * 
	 * @param map(goodsId-商品的id,skuId-sku的id)
	 * @return
	 */
	@RequestMapping("/showgoodsfavorite")
	@ResponseBody
	public BhResult showGoodsFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
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
			String categoryId = map.get("categoryId");
			String goodName = map.get("goodname");
			GoodsFavorite favorite = new GoodsFavorite();
			favorite.setmId(member.getId());
			if (StringUtils.isNotEmpty(categoryId)) {
				favorite.setCategoryId(Long.parseLong(categoryId));
			}
			if (StringUtils.isNotEmpty(goodName)) {
				favorite.setGoodName(goodName);
			}
			PageBean<GoodsFavorite> list = userFavoriteService.showGoodsFavorite(favorite, page, rows);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showgoodsfavorite#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171120-03 删除用户收藏
	 * 
	 * @param map(goodsId-商品的id,skuId-sku的id)
	 * @return
	 */
	@RequestMapping("/deletegoodsfavorite")
	@ResponseBody
	public BhResult deleteFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isEmpty(id)) {
				result = new BhResult(400, "id不能为空", null);
			} else {
				GoodsFavorite favorite = new GoodsFavorite();
				favorite.setmId(member.getId());
				favorite.setShopName(id);
				int row = userFavoriteService.deleteGoodFavorite(favorite);
				if (row == 0) {
					result = new BhResult(BhResultEnum.FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######deletegoodsfavorite#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171120-04 显示用户收藏的分类名称 -获取最后一级分类
	 * 
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/showgoodscategory")
	@ResponseBody
	public BhResult showGoodsCategory(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			GoodsCategory goodsCategory = new GoodsCategory();
			goodsCategory.setReid(member.getId().longValue());
			List<GoodsCategory> list = userFavoriteService.showGoodsCategory(goodsCategory);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showgoodscategory#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171121-01 显示用户收藏的店铺的分类名称 -根据该用户的收藏的商铺的id关联的cat_id
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/showshopcategory")
	@ResponseBody
	public BhResult showShopCategory(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			GoodsCategory goodsCategory = new GoodsCategory();
			goodsCategory.setReid(member.getId().longValue());
			List<GoodsCategory> list = userFavoriteService.showShopCategory(goodsCategory);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showshopcategory#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171128-01 显示分类的浏览记录的商品关联的cat_id
	 * 
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/showhistorycategory")
	@ResponseBody
	public BhResult showHistoryCategory(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			GoodsCategory goodsCategory = new GoodsCategory();
			goodsCategory.setReid(member.getId().longValue());
			List<GoodsCategory> list = userFavoriteService.selectHistoryCategory(goodsCategory);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showhistorycategory#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171121-02 显示用户收藏商家的列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/showshopfavorite")
	@ResponseBody
	public BhResult showShopFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			String p = map.get("page");
			String r = map.get("size");
			String shopName = map.get("shopName");
			MemberShopFavorite favorite = new MemberShopFavorite();
			Integer page = 0;
			Integer rows = 0;
			Integer page1 = 0;
			Integer rows1 = 0;
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
			if (StringUtils.isNotEmpty(shopName)) {
				favorite.setShopName(shopName);
			}
			favorite.setmId(member.getId());
			PageBean<MemberShopFavorite> list = userFavoriteService.showShopFavorite(favorite, page, rows, page1,
					rows1);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showshopfavorite#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171122-01 显示用户收藏商家的列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/selectsimilarity")
	@ResponseBody
	public BhResult selectSimilarity(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
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
			// 分类的id
			String cId = map.get("categoryId");
			Goods goods = new Goods();
			goods.setCatId(Long.parseLong(cId));
			Map<String, Object> list = userFavoriteService.selectSimilarity(goods, page, rows);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectsimilarity#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171122-02 删除用户收藏
	 * 
	 * @param map(id)
	 * @return
	 */
	@RequestMapping("/deleteshopfavorite")
	@ResponseBody
	public BhResult deleteShopFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isEmpty(id)) {
				result = new BhResult(400, "id不能为空", null);
			} else {
				MemberShopFavorite favorite = new MemberShopFavorite();
				favorite.setmId(member.getId());
				favorite.setShopName(id);
				int row = userFavoriteService.deleteShopFavorite(favorite);
				if (row == 0) {
					result = new BhResult(BhResultEnum.FAIL, null);
				} else if (row == 2) {
					result = new BhResult(BhResultEnum.SUCCESS, null);
				} else if (row == 1) {
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######deleteshopfavorite#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171123-01 显示用户的历史浏览记录
	 * 
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/historylist")
	@ResponseBody
	public BhResult historyList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String p = map.get("page");
			String r = map.get("size");
			String categoryId = map.get("categoryId");
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
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberUserAccessLog accessLog = new MemberUserAccessLog();
			accessLog.setmId(member.getId());

			if (StringUtils.isNotEmpty(categoryId)) {
				accessLog.setGoodsId(Integer.parseInt(categoryId));
			}
			PageBean<MemberUserAccessLog> pageBean = userFavoriteService.showhistoryList(accessLog, page, rows);
			result = new BhResult(BhResultEnum.SUCCESS, pageBean);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######historylist#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171123-01 用户收藏的商品和店铺的数量
	 * 
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/selectfavoritenum")
	@ResponseBody
	public BhResult selectFavoriteNum(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberUserAccessLog accessLog = new MemberUserAccessLog();
			accessLog.setmId(member.getId());
			OrderInfoPojo pojo = new OrderInfoPojo();
			pojo = userFavoriteService.selectFavoriteNum(member.getId());
			result = new BhResult(BhResultEnum.SUCCESS, pojo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectfavoritenum#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-20171123-01 用户收藏的商品和店铺的数量
	 * 
	 * @param map(参数-无)
	 * @return
	 */
	@RequestMapping("/selectonegoodfavoritenum")
	@ResponseBody
	public BhResult selectOneGoodGavoritenum(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String id = map.get("id");
			GoodsFavorite goodsFavorite = new GoodsFavorite();
			if (StringUtils.isEmpty(id)) {
				result = new BhResult(400, "参数不能为空", null);
			} else {
				goodsFavorite.setId(Integer.parseInt(id));
				Goods good = userFavoriteService.selectOneGoodGavoritenum(goodsFavorite);
				result = new BhResult(BhResultEnum.SUCCESS, good);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectonegoodfavoritenum#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

}
