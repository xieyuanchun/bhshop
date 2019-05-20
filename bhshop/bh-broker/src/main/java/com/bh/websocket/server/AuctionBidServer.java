package com.bh.websocket.server;

import com.bh.auc.rocketMQ.BokerNotifyListener;
import com.bh.auc.rocketMQ.SpringProducer;
import com.bh.user.pojo.Member;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.websocket.common.GetHttpSessionConfigurator;
import com.bh.websocket.entity.BidMessage;
import com.bh.websocket.entity.RocketMqMsg;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhouyongyi
 *         periodTag -- 拍卖场次唯一标签，用于分组websocket连接和拍卖消息
 */
@ServerEndpoint(value = "/websocket/auctionBidServer/{periodTag}", configurator = GetHttpSessionConfigurator.class)
public class AuctionBidServer {
    private static final Logger logger = LoggerFactory.getLogger(AuctionBidServer.class);
    public final static String SEND_TOPIC = "auctionBid";
    private WebApplicationContext webAppContext;
    private SpringProducer producerBoker;
    private static Map<String, Map<String, Session>> mapTagSession = null; //拍卖场次标签与session的hashmap
    private static Map<String, Member> mapSessionIdMember = new HashMap<String, Member>();     //sessionID与用户的关联hashmap
    private static JedisUtil jedisUtil = JedisUtil.getInstance(); //设置为全局

    public AuctionBidServer() {
        webAppContext = ContextLoader.getCurrentWebApplicationContext();
        producerBoker = (SpringProducer) webAppContext.getBean("producerBoker");// 注册生产者mq监听
        //初始化mapTagSession begin
        BokerNotifyListener bokerNotifyListener = (BokerNotifyListener) webAppContext.getBean("bokerNotifyListener");// 注册生产者mq监听
        mapTagSession = bokerNotifyListener.getMapTagSession();
        //初始化mapTagSession end
    }

    /**
     * @param session
     * @param periodTag 系统来源标识-商品id-期数 eg:bhshop-15794-1
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("periodTag") String periodTag) {
        logger.info("有新连接进入" + periodTag);
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (httpSession != null) {
            Member member = (Member) httpSession.getAttribute("userInfo");
            if (member != null) {
                mapSessionIdMember.put(session.getId(), member);  //加入到用户的地址本

                //加入到拍卖场次标签hashmap，用于接收拍卖最新价格
                Map<String, Session> sessionGroup = mapTagSession.get(periodTag);
                if (sessionGroup == null) {
                    sessionGroup = new HashMap<String, Session>();
                    sessionGroup.put(session.getId(), session);
                    mapTagSession.put(periodTag, sessionGroup);
                } else {
                    sessionGroup.put(session.getId(), session);
                }
                //读取公告板信息，并计算倒计时间，返回
                String strBidMessage = (jedisUtil.new Strings()).get(periodTag);
                if (StringUtils.isNotBlank(strBidMessage)) {//先检查redis公告板是否有此拍卖师对象
                    BidMessage bidMessage = JsonUtils.jsonToPojo(strBidMessage, BidMessage.class);
                    if (bidMessage != null) {
                        long timeNow = jedisUtil.time();
                        bidMessage.setSysTime(timeNow);
                        bidMessage.setInvertSecond(Long.valueOf((bidMessage.getChangeTime() - timeNow) / 1000).intValue());
                        String strJsonBidMessage = JsonUtils.objectToJson(bidMessage);
                        try {
                            session.getBasicRemote().sendText(strJsonBidMessage);
                        } catch (IOException e) {
                            logger.debug("onOpen 发送websocket消息失败,error=" + e.toString());
                            e.printStackTrace();
                        }
                    } else {
                        logger.debug("公告板消息无效,bidMessage=" + bidMessage.toString());
                    }
                } else {
                    logger.debug("拍卖公告板不存在");
                }
            } else {
                logger.warn("用户信息无法获取,登录异常");
            }
        } else {
            logger.warn("请先登录");
        }
    }

    /**
     * Description: 拍卖出价 :message ==>
     * {'tag':'bhshop-11778-35','key':'','msgBody':'{"auctionPrice":"99911"}'}
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        if ("...".equals(message)) { // heartbeat心跳
            return;
        } else {
            try {
                Member member = mapSessionIdMember.get(session.getId());
                if (member != null) {
                    JSONObject jsonObject = JSONObject.fromObject(message);
                    RocketMqMsg rocketMqMsg = (RocketMqMsg) JSONObject.toBean(jsonObject, RocketMqMsg.class);
                    logger.debug("SessionId=" + session.getId() + ",received=" + rocketMqMsg.toString());
                    //自动填充用户信息beign
                    BidMessage bidMessage = JsonUtils.jsonToPojo(rocketMqMsg.getMsgBody(), BidMessage.class);
                    if (bidMessage != null) {
                        bidMessage.setMid(member.getId());
                        bidMessage.setUserName(member.getUsername());
                        bidMessage.setHeadImg(member.getHeadimgurl());
                    }
                    //自动填充用户信息end
                    String strJsonBidMessage = JsonUtils.objectToJson(bidMessage);

                    Message msg = new Message(SEND_TOPIC, rocketMqMsg.getTag(), rocketMqMsg.getKey(), strJsonBidMessage.getBytes("UTF-8"));
                    SendResult sendResult = producerBoker.getProducer().send(msg);//直接把websocke收到的消息原样透传到消息队列中
                } else {
                    logger.error("onMessage error: 用户无法获取");
                }
            } catch (Exception e) {
                logger.error("SocketClient onMessage=" + e.getMessage());
                try {
                    session.getBasicRemote().sendText(e.getMessage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("AuctionBidServer onError=" + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * @param session
     * @param periodTag 系统来源标识-商品id-期数 eg:bhshop-15794-1
     */
    @OnClose
    public void onClose(Session session, @PathParam("periodTag") String periodTag) {
        mapSessionIdMember.remove(session.getId()); //删除用户地址本

        Map<String, Session> sessionGroup = mapTagSession.get(periodTag); //删除session
        if (sessionGroup != null) {
            sessionGroup.remove(session.getId());
        }
        logger.info("断开" + periodTag);
    }

}
