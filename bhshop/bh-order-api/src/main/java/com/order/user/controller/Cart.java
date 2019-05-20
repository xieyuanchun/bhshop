package com.order.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.bh.config.Contants;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartList;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.goods.pojo.GoodsSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.order.user.service.UserOrderService;



@SessionAttributes
@Controller
@RequestMapping("/cart1")
public class Cart {
	private static final Logger logger = LoggerFactory.getLogger(Cart.class);
	@Autowired
	private UserOrderService userOrderService;

	/**
	 * 展示购物车列表
	 */
	@RequestMapping(value = "/showCartList", method = RequestMethod.POST)
	@ResponseBody
	public BhResult showCartList(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);

		String goodIds = map.get("json");
		// 创建bhResult
		BhResult bhResult = null;
		try {
			// String p, String r, String p1, String r1,
			String p = "";
			String r = "";
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

			PageParams<GoodsCartListShopIdList> params = new PageParams<>();
			// 分页传递给前端
			if (member == null) {
				if (StringUtils.isNotEmpty(goodIds)) {
					// 判断是否为空
					List<GoodsCartPojo> goodsCartList = JsonUtils.jsonToList(goodIds, GoodsCartPojo.class);
					List<GoodsCart> returnGoodsCartList = new ArrayList<>();
					for (int i = 0; i < goodsCartList.size(); i++) {
						userOrderService.selectById("");
						GoodsSku goodsSku = new GoodsSku();
						GoodsCart goodsCart = new GoodsCart();
						MemberShop memberShop = new MemberShop();
						goodsSku = userOrderService
								.selectGoodsSkuByGoodsId(String.valueOf(goodsCartList.get(i).getSkuId()));// 通过主键id获取它的goodsSku
						
						
						org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
					     org.json.JSONArray personList = jsonObj.getJSONArray("url");
				       
					       Object value = JsonUtils.stringToObject(goodsSku.getValue());
					     goodsSku.setValueObj(value);
					     
					    String url = (String) personList.get(0);
						goodsCart.setmId(null);
						goodsCart.setgId(goodsCartList.get(i).getId());
						goodsCart.setNum(goodsCartList.get(i).getNum());
						goodsCart.setAddtime(null);
						goodsCart.setGskuid(goodsCartList.get(i).getSkuId());
						goodsCart.setGoodsSkus(goodsSku);
						Goods good = userOrderService.selectBygoodsId(goodsCartList.get(i).getId());// 判断是否有货
						memberShop = userOrderService.selectMemberShopByGoodId(good.getShopId());
						goodsCart.setSellPrice(goodsSku.getSellPrice());// 传过来的元，存进入的分
						double realsellPrice = (double) goodsSku.getSellPrice() / 100;
						goodsCart.setRealsellPrice(realsellPrice);
						goodsCart.setGoodName(good.getName());
						goodsCart.setShopName(memberShop.getShopName());
						goodsCart.setIsDel(0);
						goodsCart.setShopId(memberShop.getmId());
						goodsCart.setgImage(url);
						
						// 2017-12-26,判断是京东商品还是滨惠商品
						//是否是京东商品，0否，1是
						int isJDGoods = good.getIsJd();
						if (isJDGoods ==0) {
							//滨惠是否有货：0有货，1无货
							int sto = goodsSku.getStoreNums()-goodsCart.getNum();
							if (sto < 0) {
								goodsCart.setIsStore(1);
								goodsCart.setStoreName(Contants.storeNumsNo);
							}else {
								goodsCart.setIsStore(0);
								goodsCart.setStoreName(Contants.storeNumsYes);
							}
						}
						//如果京东商品,默认设置有货
						else if (isJDGoods ==1) {
							goodsCart.setIsStore(0);
							goodsCart.setStoreName(Contants.storeNumsYes);
						}
						
						returnGoodsCartList.add(goodsCart);
					}
					bhResult = new BhResult(200, "成功", returnGoodsCartList);
				} else {
					bhResult = new BhResult(200, "传过来的信息不能为空", null);
				}

			} else {

				GoodsCart goodsCart = new GoodsCart();
				goodsCart.setmId(member.getId());
				goodsCart.setIsDel(0);
				ArrayList<GoodsCart> cartList1 = (ArrayList<GoodsCart>) userOrderService.getGoodscart(goodsCart);
				ArrayList<GoodsCart> now = cartList1;
				List<GoodsCartListShopIdList> my = removeDuplicate1(cartList1, now, goodsCart);
				PageBean<GoodsCartListShopIdList> carts = new PageBean<>();
				params.setCurPage(page);
				params.setPageSize(rows);
				params.setResult(my);
				carts.getPageResult(params);

				PageBeanList<GoodsCartList> list = new PageBeanList<>();
				list.setList(carts);
				list.setLogin(true);
				bhResult = new BhResult(200, "成功", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######showCartList#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}
	
	public List<GoodsCartListShopIdList> removeDuplicate1(List<GoodsCart> list, List<GoodsCart> list3,
			GoodsCart goodsCart2) {
		List<GoodsCartListShopIdList> list2 = new ArrayList<>();
		List<GoodsCart> goodsCart1 = new ArrayList<>();

		try {
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = list.size() - 1; j > i; j--) {
					if (list.get(j).getShopId().equals(list.get(i).getShopId())) {
						list.remove(j);
					} else {
						MemberShop memberShop = new MemberShop();
						GoodsCart goodsCart = new GoodsCart();
						goodsCart.setShopId(list.get(j).getShopId());
						memberShop = userOrderService.selectMemberShopByGoodId(list.get(j).getShopId());
						goodsCart.setShopName(memberShop.getShopName());
						goodsCart1.add(goodsCart);
					}
				}
			}
			List<GoodsCart> list4 = userOrderService.selectGoodsCartShopIds(goodsCart2);
			ArrayList<GoodsCart> cartList1 = (ArrayList<GoodsCart>) userOrderService.getGoodscart(goodsCart2);
			
			List<GoodsCart> goodsCarts = cartList1;
			for (int i = 0; i < list4.size(); i++) {// list
				GoodsCartListShopIdList goodsCartListShopIdList = new GoodsCartListShopIdList();
				List<GoodsCart> add = new ArrayList<>();
				for (int j = 0; j < goodsCarts.size(); j++) {
					List<GoodsCart> g = new ArrayList<>();
					if (goodsCarts.get(j).getShopId().equals(list4.get(i).getShopId())) {// list3
						GoodsCart goodsCart = new GoodsCart();

						Goods good = new Goods();
						GoodsSku goodsSku = new GoodsSku();

						good = userOrderService.selectBygoodsId(goodsCarts.get(j).getgId());// 获取good
						goodsSku = userOrderService
								.selectGoodsSkuByGoodsId(String.valueOf(goodsCarts.get(j).getGskuid()));

						goodsCart.setAddtime(goodsCarts.get(j).getAddtime());
						
						org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue());
					     org.json.JSONArray personList = jsonObj.getJSONArray("url");
					 
						Object value = JsonUtils.stringToObject(goodsSku.getValue());
						goodsSku.setValueObj(value);
						
						goodsCart.setgId((goodsCarts.get(j).getgId()));
						goodsCart.setgImage((String)personList.get(0));
						goodsCart.setGoodName(good.getName());
						goodsCart.setGoodsSkus(goodsSku);
						goodsCart.setId(goodsCarts.get(j).getId());
						goodsCart.setIsDel(goodsCarts.get(j).getIsDel());
						goodsCart.setmId(goodsCarts.get(j).getmId());
						goodsCart.setNum(goodsCarts.get(j).getNum());

						double realsellPrice = (double) goodsSku.getSellPrice() / 100;
						goodsCart.setRealsellPrice(realsellPrice);
						goodsCart.setSellPrice(goodsSku.getSellPrice());// 市场价格“分”转化成“元”

						goodsCart.setGskuid(goodsCarts.get(j).getGskuid());
						g.add(goodsCart);
					}
					add.addAll(g);
				}
				MemberShop memberShop = new MemberShop();
				memberShop = userOrderService.selectMemberShopByGoodId(list4.get(i).getShopId());// 获取shop

				goodsCartListShopIdList.setGoodsCartLists(add);
				goodsCartListShopIdList.setShopId(list4.get(i).getShopId());// list
				goodsCartListShopIdList.setShopName(memberShop.getShopName());// list
				list2.add(goodsCartListShopIdList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("####### removeDuplicate1 #######" + e);
		}
		return list2;
	}
	
}
