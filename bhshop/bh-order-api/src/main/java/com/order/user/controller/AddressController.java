package com.order.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.goods.pojo.GoodsSku;
import com.bh.jd.bean.order.StockParams;
import com.bh.result.BhResult;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.SimpleAddress;
import com.order.user.service.JDOrderService;

@Controller
public class AddressController {
	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
	@Autowired
	private JDOrderService jDOrderService;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private MemberUserAddressMapper memberUserAddressMapper;
	@Autowired
	private GoAddressAreaMapper goAddressAreamapper;

	/**
	 * 前端用户用到的商品详情的库存接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/apiGoodsDetailsStock")
	@ResponseBody
	public BhResult apiGoodsDetailsStock(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String goodsSkuId = map.get("goodsSkuId");
			String addresId = map.get("addresId");
			// lat<纬度>,lng<经度>
			String num = map.get("num");// 数量
			// 未登录的时候
			String prov = map.get("prov");
			String city = map.get("city");
			String area1 = map.get("area");
			String four = map.get("four");

			if (StringUtils.isEmpty(goodsSkuId)) {
				return result = BhResult.build(400, "参数goodsSkuId不能为空");
			}
			if (StringUtils.isEmpty(num)) {
				return result = BhResult.build(400, "参数num不能为空");
			} if (StringUtils.isNotEmpty(num) && Integer.parseInt(num)<1) {
				return result = BhResult.build(400,"数量必须要大于或者等于1");
			}
			else {
				result = BhResult.build(200, "全部有货", null);
				// 用户已登录
				if (StringUtils.isNotEmpty(addresId)) {
					String provname = memberUserAddressMapper.selectProvName(Integer.parseInt(addresId));
					GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(goodsSkuId));
					if (goodsSku.getJdSupport() == 0) {
						result = jDOrderService.getBHSto(goodsSku.getGoodsId(), provname, goodsSku.getStoreNums(),
								Integer.parseInt(num));
					} else if (goodsSku.getJdSupport() == 1) {
						MemberUserAddress memberUserAddress = memberUserAddressMapper
								.selectByPrimaryKey(Integer.parseInt(addresId));
						List<StockParams> stockParams = new ArrayList<>();
						StockParams params = new StockParams();
						params.setNum(Integer.parseInt(num));
						params.setSkuId(goodsSku.getJdSkuNo().toString());
						stockParams.add(params);
						result=jDOrderService.getJDStock(memberUserAddress.getProv().toString(), memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),stockParams);
						if (result.getStatus() == 200) {
							if (!memberUserAddress.getFour().equals(0)) {
								result = jDOrderService.getAreaByJD(goodsSku.getJdSkuNo().toString(), memberUserAddress.getProv().toString(),
										memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),
										memberUserAddress.getFour().toString());
							} else {
								result = jDOrderService.getAreaByJD(goodsSku.getJdSkuNo().toString(), memberUserAddress.getProv().toString(),
										memberUserAddress.getCity().toString(), memberUserAddress.getArea().toString(),
										"");
							}

						}
					}
				}

				// 用户未登录
				else {
					GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(goodsSkuId));
					if (goodsSku.getJdSupport() == 0) {
						GoAddressArea goAddressArea=null;
						if(("").equals(prov)||prov==null) {
							 goAddressArea=goAddressAreamapper.selectByPrimaryKey(Integer.parseInt(city));
						}else {
							 goAddressArea=goAddressAreamapper.selectByPrimaryKey(Integer.parseInt(prov));
						}
						
						result = jDOrderService.getBHSto(goodsSku.getGoodsId(), goAddressArea.getName(), goodsSku.getStoreNums(),
								Integer.parseInt(num));
					} else if (goodsSku.getJdSupport() == 1) {
						List<StockParams> stockParams = new ArrayList<>();
						StockParams params = new StockParams();
						params.setNum(Integer.parseInt(num));
						params.setSkuId(goodsSku.getJdSkuNo().toString());
						stockParams.add(params);
						result = jDOrderService.getJDStock( prov, city, area1,stockParams);
						if (result.getStatus() == 200) {
							if (!four.equals("0")) {
								result = jDOrderService.getAreaByJD(goodsSku.getJdSkuNo().toString(), prov.toString(), city.toString(), area1, four);
							} else {
								result = jDOrderService.getAreaByJD(goodsSku.getJdSkuNo().toString(), prov.toString(), city.toString(), area1, "");
							}

						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######apiGoodsDetailsStock#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

	/**
	 * 前端用户用到的商品详情的库存接口
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/address")
	@ResponseBody
	public BhResult address(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			//2018.6.1 zlk
			HttpSession session = request.getSession(false);
			Member member = new Member();
		    if(session!=null) {
			     member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			//end
			if (member == null) {
				result = new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				List<MemberUserAddress> addresses = memberUserAddressMapper.selectUserAddress(member.getId());
				List<SimpleAddress> simpleAddresses = new ArrayList<>();
				if (addresses.size() > 0) {
					for (MemberUserAddress memberUserAddress : addresses) {
						SimpleAddress simpleAddress = new SimpleAddress();
						StringBuffer sb = new StringBuffer();
						sb.append(memberUserAddress.getProvname()).append(" ").append(memberUserAddress.getCityname())
								.append(" ").append(memberUserAddress.getAreaname()).append(" ")
								.append(memberUserAddress.getAddress());
						simpleAddress.setAddress(sb.toString());
						simpleAddress.setId(memberUserAddress.getId());
						simpleAddress.setIsDefault(memberUserAddress.getIsDefault());
						simpleAddress.setProv(memberUserAddress.getProv().toString());
						simpleAddress.setCity(memberUserAddress.getCity().toString());
						simpleAddress.setArea(memberUserAddress.getArea().toString());
						simpleAddress.setFour(memberUserAddress.getFour().toString());
						simpleAddresses.add(simpleAddress);
					}
				}
				result = new BhResult(200, "操作成功", simpleAddresses);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######address#######" + e);
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}
}
