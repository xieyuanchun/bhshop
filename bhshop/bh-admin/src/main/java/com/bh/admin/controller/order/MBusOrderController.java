package com.bh.admin.controller.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.order.OrderSku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.annotaion.OrderLogAnno;
import com.bh.admin.pojo.goods.MSendMsg;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.service.MBusOrderService;
import com.bh.admin.service.OrderMainService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;



@Controller
@RequestMapping("/mShopMsg")
public class MBusOrderController {
	@Autowired
	private MBusOrderService mBusOrderService;
	@Autowired
	private OrderMainService orderMainService;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderMainService service;

	//发货信息的回显
	@RequestMapping("/selectSendMsg")
	@ResponseBody
	public BhResult selectSendMsg(@RequestBody MSendMsg msg,HttpServletRequest request) {
	   BhResult result = null;
	   try {

		   MBusEntity entity = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);

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
		   MBusEntity entity = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);

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
						//商品自配送 日志
						int row = service.deliveryOwner(id, Integer.parseInt(shopId));
						if (row ==1 ) {
							StringBuffer sb=new StringBuffer();
							List<OrderSku> orderSkuList= orderSkuMapper.selectByGoodsId(Integer.parseInt(id),Integer.parseInt(shopId));
							for (int j = 0; j < orderSkuList.size(); j++) {
								sb.append(orderSkuList.get(j).getGoodsId()+",");
							}
							String goodsIds=sb.substring(0, sb.length()-1);
							GoodsOperLog goodsOperLog = new GoodsOperLog();
							goodsOperLog.setOpType("商家自配");
							goodsOperLog.setGoodId(goodsIds);
							goodsOperLog.setUserId(entity.getUserId().toString());
							goodsOperLog.setOrderId(id);
							goodsOperLog.setOpTime(new Date());
							String userName = memberShopMapper.selectUsernameBymId(entity.getUserId().intValue()); //查找操作人
							goodsOperLog.setAdminUser(userName);
							goodsOperLogMapper.insertSelective(goodsOperLog);
						} else {
							result = new BhResult(BhResultEnum.FAIL, null);
						}
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
