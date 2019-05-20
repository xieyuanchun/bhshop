package com.order.user.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.order.pojo.OrderSeed;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.pay.HttpService;
import com.google.gson.Gson;
import com.order.enums.UnionPayInterfaceEnum;
import com.order.user.service.SeedService;
import com.order.vo.UnionPayVO;

@Controller
public class SeedController {
	private static final Logger logger = LoggerFactory.getLogger(SeedController.class);
	@Autowired
	private SeedService seedService;

	/**
	 * CHENG-201712-05-01 用户购买种子
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/buyseed")
	@ResponseBody
	public BhResult buySeed(HttpServletRequest request, HttpServletResponse response) {
		try {
			String json = request.getParameter("json");
			Gson gson = new Gson();
			Map<String, String> map1 = gson.fromJson(json, Map.class);
			OrderSeed seed1 = seed(map1, request);
			String outTradeNo = seed1.getOrderNo();
			String orderBody = "null,4";// 购买种子;

			/****** xieyc modifyStart *********/
			List<MemberShop> list = seedService.selectBhShop(1);
			MemberShop memberShop = new MemberShop();
			if (list.size() > 0) {
				memberShop = list.get(0);
			} else {
				list = seedService.selectBhShop(0);
				memberShop = list.get(0);
			}

			UnionPayVO vo = new UnionPayVO();
			vo.setOriginalAmount(seed1.getOrderPrice() + "");
			vo.setTotalAmount(seed1.getOrderPrice() + "");
			vo.setMd5Key(memberShop.getMd5Key());
			vo.setAttachedData(orderBody);
			vo.setMerOrderId(outTradeNo);
			String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXSEEDPAY.getMethod(), vo);
			jsonStr = jsonStr.replaceAll("&", "&amp");
			return BhResult.build(200, "操作成功", jsonStr);
			/****** xieyc modifyEnd *********/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######buyseed#######" + e);
			return null;
			// TODO: handle exception
		}

	}

	private OrderSeed seed(Map<String, String> map, HttpServletRequest request) {
		try {
			String smId = map.get("smId");// seedModel模型的id
			String mId = map.get("mId");
			OrderSeed seed = new OrderSeed();
			Member member = new Member();
			member = seedService.selectMemberBymId(Integer.parseInt(mId));

			seed.setmId(member.getId());
			seed.setOrderNo(IDUtils.getOrderNo(null));// order的编号
			seed.setStatus(1);
			if (StringUtils.isEmpty(smId)) {
				seed.setSmId(1);
			} else {
				seed.setSmId(Integer.parseInt(smId));
			}

			OrderSeed orderSeed = new OrderSeed();
			String id = "0";
			if (id.equals("0")) {
				orderSeed = seedService.insertOrderSeed(seed);
			}
			return orderSeed;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######seed#######" + e);
		}
		return null;
	}

}
