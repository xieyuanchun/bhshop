package com.bh.webserver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.webserver.entity.HolllandAuctionEntity;
import com.bh.webserver.entity.Text;
import com.bh.webserver.entity.UserEntity;
import com.bh.webserver.util.JsonUtil;

import javax.websocket.Session;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * 说明：
 */
public class Message {

	private static Logger LOG = LoggerFactory.getLogger(Message.class);

	/**
	 * 给除自己外的所有用户发送消息
	 * 
	 * @param message
	 * @param filterSession
	 */
	public void send(String message, Session filterSession) {
         if("...".equals(message)){
        	 return;
         }
		// 从昵称池拿出用户昵称
		Text text = JsonUtil.getBean(message, Text.class);
		text.setUserName(NickPool.get(filterSession.getId()));
		// 判断是否是需要执行的脚本，是则return，由代理处理脚本
		if (CMD.isCMD(text.getMessage())) {
			return;
		}
		// 给所有用户发送消息
		Set<Integer> keys = UserPool.getUserPool().keySet();
		for (Integer key : keys) {
			UserEntity user = (UserEntity) UserPool.get(key);
			Session session = user.getSession();
			// 屏蔽状态关闭的用户
			if (!session.isOpen()) {
				UserPool.remove(key);
				continue;
			}
			// 排除自己
			if (session.equals(filterSession)) {
				continue;
			}
			try {
				// 发送消息
				session.getBasicRemote().sendText(JsonUtil.getString(text));
			} catch (IOException e) {
				LOG.error("给用户 [" + session.getId() + "] 发送消息失败", e);
			}
		}
	}

	public boolean sendSingle(String message, Integer mId) {
		UserEntity user = UserPool.get(mId);
		if (user != null) {
			try {
				Session session = user.getSession();
				// 发送消息
				Text text = new Text();
				text.setUserName("系统消息");
				text.setMessage(message);
				session.getBasicRemote().sendText(JsonUtil.getString(text));
				return true;
			} catch (IOException e) {
				LOG.error("给用户 [" + mId + "] 发送消息失败", e);
				return false;
			}
		} else {
			// System.out.println("########用户不在线##############");
			return false;
		}

	}

	public boolean sendPoolMsg(String message,Integer goodsId) {
		System.out.println("####sendPoolMsg#########");
		Map<Integer, Object> userPool = UserPool.getUserPool();
		if (userPool != null) {
			try {
				for (Map.Entry<Integer, Object> entry : userPool.entrySet()) {
					UserEntity user =(UserEntity)entry.getValue();
					Session session = user.getSession();
					// 发送消息
					Text text = new Text();
					text.setUserName("系统消息");
					text.setMessage(message);
					text.setGoodsId(goodsId);
					System.out.println("webserver  goodsId----->"+goodsId);
					session.getBasicRemote().sendText(JsonUtil.getString(text));
				}
				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			System.out.println("########没有用户在线##############");
			return false;
		}

	}
	
	public boolean sendAuctionMsg(Integer goodsId,Integer mId,String userName,String headImg,String auctionPrice,String currentPeriods) {
		System.out.println("####pushAuctionMsg#########");
		String key = goodsId+"-"+currentPeriods;
		Map<String, HolllandAuctionEntity> userPool = AuctionUserPool.getAuctionEntityList(key);

		if (userPool != null) {
			System.out.println("AuctionSocket sendAuctionMsg userPool size --->"+userPool.size());
			try {
				for (Entry<String, HolllandAuctionEntity> entry : userPool.entrySet()) {

					System.out.println("AuctionSocket sendAuctionMsg userPool getKey --->"+entry.getKey());
					HolllandAuctionEntity user = entry.getValue();
					Session session = user.getSession();
					// 发送消息
					Text text = new Text();
					text.setType(0);
					text.setmId(mId);
					text.setUserName(userName);
					text.setHeadImg(headImg);
					text.setAuctionPrice(auctionPrice);
					// text.setGoodsId(goodsId);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String curTime = sdf.format(new Date());
					text.setCurTime(curTime);
					System.out.println("AuctionSocket   JsonUtil.getString(text)--->" + JsonUtil.getString(text));
					session.getBasicRemote().sendText(JsonUtil.getString(text));
				}
				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			System.out.println("########没有用户在线##############");
			return false;
		}

	}
	
	public boolean sendAuctionPayMsg(Integer goodsId,Integer mId,String userName,String headImg,String auctionPrice,String curTime,Integer orderId,String currentPeriods) {
	//	UserEntity user = AuctionUserPool.get(mId);
		String key = goodsId+"-"+currentPeriods;
		Map<String, HolllandAuctionEntity> userPool = AuctionUserPool.getAuctionEntityList(key);
		if (userPool != null) {
			try {
				for (Entry<String, HolllandAuctionEntity> entry : userPool.entrySet()) {
					HolllandAuctionEntity user = entry.getValue();
					Session session = user.getSession();
					Text text = new Text();
					text.setOrderId(orderId);
					text.setCurTime(curTime);
					text.setHeadImg(headImg);
					text.setUserName(userName);
					text.setAuctionPrice(auctionPrice);
					text.setType(1);
					text.setmId(mId);
					// text.setGoodsId(goodsId);
					System.out.println("sendAuctionPayMsg   JsonUtil.getString(text)--->" + JsonUtil.getString(text));
					// text.setmId(user.getmId());
					session.getBasicRemote().sendText(JsonUtil.getString(text));
				}
				return true;
			} catch (IOException e) {
				LOG.error("给用户 [" + mId + "] 发送消息失败", e);
				return false;
			}
		} else {
			// System.out.println("########用户不在线##############");
			return false;
		}

	}
}
