package com.bh.websocket.server;

import java.io.IOException;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.bh.auc.rocketMQ.AuctionListNotifyListener;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.websocket.entity.BidMessage;

/**
 * @author xxj
 *         periodTag -- 拍卖场次唯一标签，用于分组websocket连接和拍卖消息
 */
@ServerEndpoint(value = "/websocket/auctionList", configurator = SpringConfigurator.class)
public class AuctionListWsServer {
    private static final Logger logger = LoggerFactory.getLogger(AuctionListWsServer.class);
    private static Map<String, Session> mapSession = null;     //web socket session地址本
    private static JedisUtil jedisUtil = JedisUtil.getInstance(); //设置为全局
    private WebApplicationContext webAppContext;

    public AuctionListWsServer() {
        webAppContext = ContextLoader.getCurrentWebApplicationContext();
        //初始化 mapSession begin
        AuctionListNotifyListener auctionListNotifyListener = (AuctionListNotifyListener) webAppContext.getBean("auctionListNotifyListener");// 注册生产者mq监听
        mapSession = auctionListNotifyListener.getMapSession();
        //初始化mapSession end
    }

    /**
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        mapSession.put(session.getId(), session); //加入到用户的web socket session地址本
        logger.info("连接");
    }

    /**
     * Description: periodTag ==> bhshop-11778-21
     *
     * @author scj
     * @date 2018年7月4日
     */
    @OnMessage
    public void onMessage(Session session, String goodsTag) {
        if ("...".equals(goodsTag)) { // heartbeat心跳
            return;
        } else {
            if (goodsTag.contains("-")) { //减少redis的压力
                JedisUtil.Strings strings = jedisUtil.new Strings();
                String strBidMessage = strings.get(goodsTag);
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
                        logger.debug("拍价无效,bidMessage=" + bidMessage.toString());
                    }
                } else {
                    logger.debug("输入的拍卖公告板 goodsTag=" + goodsTag + "不存在");
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("AuctionListWsServer onError=" + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        mapSession.remove(session.getId()); //删除用户session
        logger.info("断开" + session.getId());
    }

}
