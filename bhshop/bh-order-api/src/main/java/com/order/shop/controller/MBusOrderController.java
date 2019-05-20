package com.order.shop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.MSendMsg;
import com.bh.order.pojo.OrderShop;
import com.bh.result.BhResult;
import com.bh.user.pojo.MBusEntity;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.order.annotaion.OrderLogAnno;
import com.order.shop.service.MBusOrderService;
import com.order.shop.service.OrderMainService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;


@Controller
@RequestMapping("/mShopMsg")
public class MBusOrderController {
	@Autowired
	private MBusOrderService mBusOrderService;
	@Autowired
	private OrderMainService orderMainService;
	
	//发货信息的回显
	@RequestMapping("/selectSendMsg")
	@ResponseBody
	public BhResult selectSendMsg(@RequestBody MSendMsg msg,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   
		   MBusEntity entity = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
		
			if (msg.getOrderShopId() != null) {
				//Integer shopId = Integer.parseInt(entity.getShopId()+"");
				MSendMsg m = mBusOrderService.selectMSendMsg(msg);
				result = new BhResult(200,"查询成功",m);
			}else{
				result = new BhResult(400,"orderShopId参数不能为空",null);
			}
			
			
		} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * cheng-20180306-01
	 * 商家后台针对整个订单抛单
	 * @return
	 */
	@RequestMapping("/mCastSheet")
	@ResponseBody
	@OrderLogAnno("抛单")
	public BhResult mCastSheet(@RequestBody Map<String, String> map, HttpSession session,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   //从session中取当前操作者的信息
		   MBusEntity entity = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
		   
		   if (entity == null) {
			  result = new BhResult(100,"您还未登录,请重新登录",null);
		   }else{
			  String userJson = JsonUtils.objectToJson(entity);
			    String id = map.get("orderShopId");
			    String shopId = map.get("shopId");
			    if (StringUtils.isEmpty(shopId)) {
					shopId = "1";
				}
			    String deliveryPrice = map.get("deliveryPrice"); //物流价
			    String type = map.get("type");
			    if (StringUtils.isEmpty(type)) {
			    	result = new BhResult(400,"type参数不能为空", null);
				}else{
					 //1代表滨惠速达，2代表商家自配
				    if (type.equals("1")) {
				    	orderMainService.castSheet(id, deliveryPrice,userJson);
					}else if (type.equals("2")) {
						orderMainService.deliveryOwner(id, Integer.parseInt(shopId));
					}
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
					
		  }
			} catch (Exception e) {
				e.printStackTrace();
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
}
