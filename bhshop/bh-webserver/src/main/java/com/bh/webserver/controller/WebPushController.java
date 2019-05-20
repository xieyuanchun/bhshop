package com.bh.webserver.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.webserver.common.Message;
import com.bh.webserver.proxy.MessageProxy;
/**
 * 
 * @author xxj
 *
 */
@RestController
@RequestMapping("/webPush")
public class WebPushController {
   @RequestMapping("/push")
   public boolean push(String message,Integer mId){
	   return MessageProxy.getInstance().getProxy(Message.class).sendSingle(message, mId);
   }
   @RequestMapping("/pushTeamMsg")
   public boolean pushTeamMsg(String message,Integer goodsId){
	   System.out.println("#####pushTeamMsg goodsId#######--->"+goodsId);
	   return MessageProxy.getInstance().getProxy(Message.class).sendPoolMsg(message,goodsId);
   }
   @RequestMapping("/pushAuctionMsg")
   public boolean pushAuctionMsg(Integer goodsId,Integer mId,String userName,String headImg,String auctionPrice,String currentPeriods){
	   System.out.println("#####pushAuctionMsg--->");
	   return MessageProxy.getInstance().getProxy(Message.class).sendAuctionMsg(goodsId,mId, userName, headImg, auctionPrice,currentPeriods);
   }
   
   @RequestMapping("/sendAuctionPayMsg")
   public boolean sendAuctionPayMsg(Integer goodsId,Integer mId,String userName, String headImg,String auctionPrice,String curTime,Integer orderId,String currentPeriods){
	   System.out.println("#####sendAuctionPayMsg#################");
	   return MessageProxy.getInstance().getProxy(Message.class).sendAuctionPayMsg(goodsId,mId,userName, headImg, auctionPrice,curTime,orderId,currentPeriods);
   }
   
   
   
}
