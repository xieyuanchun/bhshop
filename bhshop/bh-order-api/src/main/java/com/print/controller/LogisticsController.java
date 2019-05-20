package com.print.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.order.pojo.OrderShop;
import com.bh.result.BhResult;
import com.print.service.LogisticsService;

@Controller
@RequestMapping("/logistics")
public class LogisticsController {

	@Autowired
	private LogisticsService logisticsService;
	/**
	 * @Description: zlk 
	 * @author 
	 * @date 2018年4月11日 下午9:26:53 
	 */
	@RequestMapping("/getLogistics")
	@ResponseBody
	public BhResult updateFixedSale(@RequestBody OrderShop os) {
		BhResult r = null;
		try {
			os.setJdorderid("72691618389");
//			logisticsService.getLogistics(os);
			logisticsService.getLogistics();
			r = new BhResult(200, "成功",null);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
}
