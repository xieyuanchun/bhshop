package com.order.user.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.enums.OrderStatusEnum;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.goods.pojo.TopicSeckillLog;
import com.bh.order.pojo.Order;
import com.bh.user.pojo.Member;
import com.order.shop.service.OrderTeamService;
import com.order.user.service.PayCallbackService;
import com.order.user.service.UserOrderService;

@Service
public class PayCallbackServiceImpl implements PayCallbackService{
	 @Autowired
	   private UserOrderService userOrderService;
	   @Autowired
	   private OrderTeamService orderTeamService;	
	
	   /***
		 * out_trade_no:订单号，即order_main表的orderNo
		 * transaction_id:第三方交易号
		 * retOrder:order对象
		 * **/
	 private void paySucess(String outTradeNo, String tradeNo,Order retOrder) {
		Order reqOrder = new Order();
		reqOrder.setStatus(OrderStatusEnum.PAY_ORDER.getStatus());
		reqOrder.setPaymentNo(tradeNo);
		reqOrder.setOrderNo(outTradeNo);
		reqOrder.setPaymentStatus(OrderStatusEnum.PAY_ORDER.getStatus());
		reqOrder.setPaymentId(retOrder.getPaymentId());
		reqOrder.setOrderPrice(retOrder.getOrderPrice());
		reqOrder.setId(retOrder.getId());
		reqOrder.setmId(retOrder.getmId());
		userOrderService.updateStatusByOrderNo(reqOrder);
	}
	 
	 public void paySesessUnion(String out_trade_no,String transaction_id ,String[] strs){
		try {
			 Order retOrder = userOrderService.getOrderByOrderNo(out_trade_no);
				if (retOrder != null && retOrder.getStatus().intValue() == OrderStatusEnum.BUILD_ORDER.getStatus()) {
					if(strs[1].equals("2") || strs[1].equals("3")){ //团模式
						String teamNo = strs[0];
						if(teamNo.equals("null")){
							teamNo = null;
						}					
						orderTeamService.insertGroupOrder(out_trade_no, teamNo);
					}else if(strs[1].equals("5")){ //秒杀
						String tgId = strs[2];
						TopicSeckillLog entity = new TopicSeckillLog();
						entity.setOrderNo(out_trade_no);
						entity.setTgId(Integer.parseInt(tgId));
						orderTeamService.addSeckillLog(entity);
						
					}else if(strs[1].equals("7") || strs[1].equals("8")){ //抽奖
						String topicNo = strs[0];
						String tgId = strs[2];
						if(topicNo.equals("null")){
							topicNo = null;
						}
						TopicPrizeLog entity = new TopicPrizeLog();
						entity.setTopicNo(topicNo); //活动商品团号
						entity.setOrderNo(out_trade_no); //订单号
						entity.setTgId(Integer.parseInt(tgId)); //活动商品明细id
						orderTeamService.add(entity);
					}else if(strs[1].equals("9") || strs[1].equals("10")){ //惠省钱
						String topicNo = strs[0];
						String tgId = strs[2];
						String actNo = strs[3];
						if(topicNo.equals("null")){
							topicNo = null;
						}
						if(actNo.equals("null")){
							actNo = null;
						}
						TopicSavemoneyLog entity = new TopicSavemoneyLog();
						entity.setMyNo(topicNo);//活动商品团号
						entity.setOrderNo(out_trade_no);//订单号
						entity.setTgId(Integer.parseInt(tgId));//活动商品明细id
						entity.setActNo(actNo);//活动编号
						orderTeamService.addTopicSaveMoney(entity);
					}else if(strs[1].equals("11")&&strs[0].equals("null")){ //拍卖	
						orderTeamService.updateAuctionStatus(out_trade_no);// 更新拍卖相关表
					}
					paySucess(out_trade_no,transaction_id,retOrder);
				}
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 }

}
