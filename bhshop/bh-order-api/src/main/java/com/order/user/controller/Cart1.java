package com.order.user.controller;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.CartGoodsList;
import com.bh.goods.pojo.CartList;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.LoseEfficacyGoods;
import com.bh.goods.pojo.MyGoodsCart;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.JsonUtils;
import com.order.user.service.CartService;
import com.order.user.service.UserOrderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/cart")
public class Cart1 {
	private static final Logger logger = LoggerFactory.getLogger(Cart1.class);

	@Autowired
	private CartService cartService;
	@Autowired
	private UserOrderService userOrderService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public BhResult addCart(String itemId, String num, String skuId, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			HttpSession session=request.getSession(false);
			if (session==null) {
				return BhResult.build(BhResultEnum.LOGIN_FAIL);
			}
			// 获取mId
			Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
			//商品的id
			if (StringUtils.isEmpty(itemId)) {
				return bhResult = new BhResult(400, "参数不能为空", null);
			}
			//需要添加到购物车的数量
			if (StringUtils.isEmpty(num)) {
				return bhResult = new BhResult(400, "参数不能为空", null);
			}
			if (StringUtils.isNotEmpty(num) && Integer.parseInt(num)<1) {
				return bhResult = new BhResult(400, Contants.GOODS_NUM, null);
			}
			//商品的skuId
			if (StringUtils.isEmpty(skuId)) {
				return bhResult = new BhResult(400, "参数不能为空", null);
			}
			if (member == null) {
				return bhResult = new BhResult(100, "您还未登录,请重新登录", null);
			}
			else{
				GoodsCart params = new GoodsCart();
				params.setmId(member.getId());
				params.setgId(Integer.parseInt(itemId));
				params.setIsDel(0);
				params.setGskuid(Integer.parseInt(skuId));
				GoodsCart goodsCart = cartService.selectGoodsCartBySelect(params);
				Goods goods = cartService.getShopId(Integer.parseInt(itemId));
				params.setAddtime(new Date());
				params.setNum(Integer.parseInt(num));
				if (goods!=null) {
					params.setShopId(goods.getShopId());
				}
				//不存在
				if (goodsCart==null) {
					params.setNum(Integer.parseInt(num));
					cartService.updateGoodsCartBymIdAndgoodId(params,1);
				}else{
					params.setNum(Integer.parseInt(num)+goodsCart.getNum());
					params.setId(goodsCart.getId());
					//isInsert:取0时更新，取1是插入
					cartService.updateGoodsCartBymIdAndgoodId(params,0);
				}
				return bhResult = new BhResult(200, "添加购物车成功！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.add#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * 展示购物车列表
	 */
	@RequestMapping(value = "/showCartList", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showCartList(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Member member = (Member) request.getSession(true).getAttribute(Contants.USER_INFO_ATTR_KEY);
		String jsonStr = map.get("json");
		// 创建bhResult
		BhResult bhResult = null;
		List<CartList> cartList = new ArrayList<>();
		try {
			// 如果用户未登录，需要自己包装购物车
			if (member == null) {
				if (StringUtils.isNotEmpty(jsonStr)) {
					// 判断是否为空
					List<GoodsCartPojo> goodsCartList = JsonUtils.jsonToList(jsonStr, GoodsCartPojo.class);
					List<MyGoodsCart> returnGoodsCartList = new ArrayList<>();
					for (int i = 0; i < goodsCartList.size(); i++) {
						GoodsSku goodsSku = new GoodsSku();
						MyGoodsCart goodsCart = new MyGoodsCart();
						MemberShop memberShop = new MemberShop();
						goodsSku = userOrderService
								.selectGoodsSkuByGoodsId(String.valueOf(goodsCartList.get(i).getSkuId()));// 通过主键id获取它的goodsSku
						org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
						org.json.JSONArray personList = jsonObj.getJSONArray("url");
						Object value = JsonUtils.stringToObject(goodsSku.getValue());
						String url = (String) personList.get(0);
						goodsCart.setNum(goodsCartList.get(i).getNum());
						Map<String, Object> map2 = new HashMap<>();
						map2.put("valueObj", value);
						goodsCart.setGoodsSkus(map2);
						Goods good = userOrderService.selectBygoodsId(goodsCartList.get(i).getId());// 判断是否有货
						memberShop = userOrderService.selectMemberShopByGoodId(good.getShopId());
						double realsellPrice = (double) goodsSku.getSellPrice() / 100;
						goodsCart.setRealsellPrice(realsellPrice);
						goodsCart.setGoodName(good.getName());
						goodsCart.setShopName(memberShop.getShopName());
						goodsCart.setgImage(url);
						goodsCart.setShopId(memberShop.getmId());
						returnGoodsCartList.add(goodsCart);
					}
					return bhResult = new BhResult(200, "成功", returnGoodsCartList);
				} else {
					return bhResult = new BhResult(200, "传过来的信息不能为空", null);
				}
			} else {
				// 如果用户已登录，购物车直接从数据库表里面取
				GoodsCart goodsCart = new GoodsCart();
				goodsCart.setmId(member.getId());
				goodsCart.setIsDel(0);
				cartList = cartService.selectShopMsg(goodsCart);
				if (cartList.size() > 0) {
					for (CartList cartList2 : cartList) {
						goodsCart.setShopId(cartList2.getShopId());
						List<CartGoodsList> cartGoodsList = cartService.selectGoodsMsg(goodsCart);
						if (cartGoodsList.size() > 0) {
							for (CartGoodsList cartGoodsList2 : cartGoodsList) {
								JSONObject jsonObject = JSONObject.fromObject(cartGoodsList2.getValue()); // value转义
								JSONArray personList = jsonObject.getJSONArray("url");
								String url = (String) personList.get(0);
								cartGoodsList2.setgImage(url);
								Map<String, Object> map2 = new HashMap<>();
								map2.put("valueObj", jsonObject);
								cartGoodsList2.setGoodsSkus(map2);
							}
						}
						cartList2.setGoodsCartLists(cartGoodsList);
					}
				}
			}
			Map<String, Object> list1 = new HashMap<>();
			list1.put("list", cartList);
			bhResult = new BhResult(200, "成功", list1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.showCartList#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	@RequestMapping(value = "/getobject", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getobject(@RequestBody Map<String, String> map, HttpServletRequest request, HttpSession session) {
		BhResult bhResult = null;
		String jsonStr = map.get("json");
		Member member = (Member) request.getSession(true).getAttribute(Contants.USER_INFO_ATTR_KEY);
		try {
			if (member != null) {
				if (StringUtils.isNotEmpty(jsonStr)) {
					// 判断是否为空
					List<GoodsCartPojo> goodsCartList = JsonUtils.jsonToList(jsonStr, GoodsCartPojo.class);
					List<GoodsCart> notExistCart = new ArrayList<>();
					for (GoodsCartPojo pojo : goodsCartList) {
						Goods goods = cartService.getShopId(pojo.getId());
						// 如果商家存在
						if (goods != null) {
							// 设置属性
							GoodsCart gCart = new GoodsCart();
							gCart.setmId(member.getId());
							gCart.setgId(pojo.getId());
							gCart.setIsDel(0);
							gCart.setGskuid(pojo.getSkuId());
							gCart.setShopId(goods.getShopId());
							gCart.setAddtime(new Date());
							// 查询该商品是否在数据库里面
							GoodsCart goodsCart = cartService.selectGoodsCartBySelect(gCart);
							if (goodsCart != null) {
								// 如果在则更新数量
								gCart.setId(goodsCart.getId());
								gCart.setNum(pojo.getNum() + goodsCart.getNum());
								// isInsert:取0时更新，取1是插入
								cartService.updateGoodsCartBymIdAndgoodId(gCart, 0);
							} else {
								// 如果不在则insert信息
								gCart.setNum(pojo.getNum());
								notExistCart.add(gCart);
							}
						}
					}
					// 如果未存在的，则批量insert数据库
					if (notExistCart.size() > 0) {
						cartService.insertSelectiveByBatch(notExistCart);
					}
				}
				bhResult = new BhResult(200, "登录返回成功", null);
			} else {
				bhResult = new BhResult(100, "您还未登录,请重新登录", member);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.getobject#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * 更新商品的数量
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public BhResult updateCartItemNum(String itemId, String num, String skuId, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (StringUtils.isEmpty(itemId)) {
					return bhResult = new BhResult(400, "商品的id不能为空", null);
				} else if (StringUtils.isEmpty(num)) {
					return bhResult = new BhResult(400, "商品的数量不能为空", null);
				} else if (StringUtils.isEmpty(skuId)) {
					return bhResult = new BhResult(400, "规格ID不能为空", null);
				} else if (StringUtils.isNotEmpty(num) && Integer.parseInt(num) < 1) {
					return bhResult = new BhResult(400, Contants.GOODS_NUM, null);
				} else {
					GoodsCart goodsCart = new GoodsCart();
					goodsCart.setNum(Integer.parseInt(num));
					goodsCart.setmId(member.getId());
					goodsCart.setgId(Integer.parseInt(itemId));
					goodsCart.setGskuid(Integer.parseInt(skuId));
					// isInsert:取0时更新，取1是插入
					cartService.updateGoodsCartBymIdAndgoodId(goodsCart, 0);
					bhResult = new BhResult(200, "更新成功", null);
				}
			} else {
				return bhResult = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.update#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * 删除购物车中的商品
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public BhResult deleteCartItem1(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = new BhResult();
		try {
			// 查看用户是否存在
			HttpSession session = request.getSession(false);
			if (session == null) {
				return BhResult.build(100, "您还未登录,请重新登录");
			} else {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member != null) {
					String gIds = map.get("gId");// 传的是商品的id
					String skuId = map.get("skuId");
					if (StringUtils.isEmpty(gIds)) {
						return bhResult = new BhResult(400, "商品的id不能为空", null);
					} else if (StringUtils.isEmpty(skuId)) {
						return bhResult = new BhResult(400, "规格ID不能为空", null);
					} else {
						List<String> goodsCartId = JsonUtils.stringToList(gIds);
						List<String> skuIds = JsonUtils.stringToList(skuId);
						List<GoodsCart> list = new ArrayList<>();
						for (int i = 0; i < skuIds.size(); i++) {
							GoodsCart cart = new GoodsCart();
							cart.setmId(member.getId());
							cart.setgId(Integer.parseInt(goodsCartId.get(i)));
							cart.setGskuid(Integer.parseInt(skuIds.get(i)));
							list.add(cart);
						}
						cartService.updateGoodsCartByPrimaryKeyAndgId2(list);
						bhResult = new BhResult(200, "删除成功", null);
					}
				} else {
					return BhResult.build(100, "您还未登录,请重新登录");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.delete#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/************ 统计购物车的总数 **************/
	@RequestMapping("/totalCartNum")
	@ResponseBody
	public BhResult totalCartNum(HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member == null) {
					bhResult = new BhResult(100, "用户未登录", 0);
				} else {
					int num = 0;
					num = cartService.totalCartNum(member.getId());
					if (StringUtils.isEmpty(String.valueOf(num))) {
						bhResult = new BhResult(200, "查询成功", 0);
					} else {
						bhResult = new BhResult(200, "查询成功", num);
					}
				}
			} else {
				bhResult = new BhResult(100, "用户未登录", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.totalCartNum#######" + e);
			bhResult = new BhResult(200, "查询成功，购物车的数量是：", 0);
		}
		return bhResult;
	}

	/**
	 * @Description: 失效商品列表
	 * @author xieyc
	 * @date 2018年4月20日 上午11:43:45
	 */
	@RequestMapping(value = "/loseEfficacyList")
	@ResponseBody
	public BhResult loseEfficacyList(HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			List<LoseEfficacyGoods> goodsCartList = new ArrayList<>();
			HttpSession session = request.getSession(false);
			if (session != null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member != null) {
					GoodsCart goodsCart = new GoodsCart();
					goodsCart.setmId(member.getId());
					goodsCartList = cartService.loseEfficacyList(goodsCart);
					bhResult = new BhResult(200, "成功", goodsCartList);
				} else {
					bhResult = new BhResult(200, "成功", goodsCartList);// 没有登入失效列表没有数据
				}
			} else {
				bhResult = new BhResult(200, "成功", goodsCartList);// 没有登入失效列表没有数据
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.loseEfficacyList#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * @Description: 清空失效商品
	 * @author xieyc
	 * @date 2018年4月20日 上午11:43:45
	 */
	@RequestMapping(value = "/emptyLoseEfficacyGoods")
	@ResponseBody
	public BhResult emptyLoseEfficacyGoods(HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member != null) {
					int row = cartService.emptyLoseEfficacyGoods(member.getId());
					bhResult = new BhResult(200, "成功", row);
				} else {
					bhResult = new BhResult(100, "您还未登录,请重新登录", null);
				}
			} else {
				bhResult = new BhResult(100, "您还未登录,请重新登录", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######cart.emptyLoseEfficacyGoods#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

}
