package com.bh.admin.controller.goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.admin.pojo.goods.Goods;
import com.bh.result.BhResult;
import com.bh.admin.service.MBusShopMSGService;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.MShopInCome;
import com.bh.utils.LoggerUtil;

@Controller
@RequestMapping("/mshop")
public class MBusShopMSGController {
	
	@Autowired
	private MBusShopMSGService mBusShopMSGService;
	
	
	/**
	   * cheng-2018.3.13
	   * 商家收入接口
	   * @param map
	   * @return
	   */
	@RequestMapping("/income")
	@ResponseBody
	public BhResult selectIncome(@RequestBody Goods goods,HttpServletRequest request) {
		   BhResult bhResult = null;
		   try {
			   MBusEntity entity = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			   if (entity != null) {
				 MShopInCome inCome = mBusShopMSGService.selectMShopInCome(entity);
				 bhResult = new BhResult(200, "请求成功", inCome);
			   }else{
				   bhResult = new BhResult(100, "您还未登录,请重新登录", null);
			   }
			  
			   
			} catch (Exception e) {
				bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
				e.printStackTrace();
			}
			return bhResult;
	}
}
