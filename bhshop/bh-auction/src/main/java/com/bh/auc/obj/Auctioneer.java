package com.bh.auc.obj;

import com.bh.auc.mapper.AuctionConfigMapper;
import com.bh.auc.mapper.AuctionHistoryMapper;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.pojo.AuctionRecord;
import com.bh.auc.rocketMQ.SpringProducer;
import com.bh.auc.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.utils.JsonUtils;
import com.bh.auc.vo.BidMessage;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 拍卖师对象
 */
public class Auctioneer extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Auctioneer.class);
    private static final Integer RAP_HAMMER_WAIT_SECOND = 30; //下锤等待时间
    public BlockingQueue<Message> bidQueue = new ArrayBlockingQueue<Message>(3); //内部消息队列
    private BlockingQueue<AuctionRecord> recordQueue = null; //竞价任务队列
    private ApplicationContext appContext = null;
    private String periodTag; // 拍卖场次唯一标签，用于分组websocket连接和拍卖消息
    private AuctionConfig auctionConfig; //配置
    private ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService scheduExecHammer = Executors.newScheduledThreadPool(1);
    private InverterTask downPriceTask = new InverterTask(bidQueue);
    private JedisUtil jedisUtil = null;
    private int currentPrice = 0; //最新价格
    private int prePrice = 0; //上次价格
    private int mid = 0; //最新拍卖人
    private String userName = ""; //用户名
    private String headImg = ""; //头像
    private boolean endAuction = false;      //拍卖结束
    private HashMap<String, Auctioneer> mapAuctioneer = null; //拍卖列表

    private SpringProducer producerAuctioneer; //通知到notify 主题的生产者
    private AuctionConfigMapper auctionConfigMapper = null; //表AuctionConfig数据库对象
    private AuctionHistoryMapper auctionHistoryMapper = null; //表AuctionHistory数据库对象
    private int historyId = 0;//拍卖记录id

    public Auctioneer(ApplicationContext appContext, String periodTag, AuctionConfig auctionConfig, HashMap<String, Auctioneer> mapAuctioneer,
                      AuctionConfigMapper auctionConfigMapper, JedisUtil jedisUtil, SpringProducer producerAuctioneer, BlockingQueue<AuctionRecord> recordQueue) {
        logger.debug("新一期拍卖: periodTag=" + periodTag + ",auctionConfig=" + auctionConfig.toString());
        this.appContext = appContext;
        this.periodTag = periodTag;
        this.auctionConfig = auctionConfig;
        this.prePrice = auctionConfig.getActPrice();
        this.currentPrice = auctionConfig.getActPrice();
        this.mapAuctioneer = mapAuctioneer;
        this.auctionConfigMapper = auctionConfigMapper;
        this.jedisUtil = jedisUtil;
        this.producerAuctioneer = producerAuctioneer;
        this.recordQueue = recordQueue;

        this.auctionHistoryMapper = (AuctionHistoryMapper) appContext.getBean("auctionHistoryMapper");
        this.mid = 0;
    }

    @Override
    public synchronized void start() {
        sendNotify(EnumAuctionMsgType.BID_START, null); //启动后马上发送一次通知和写到公告板
        historyId = startAuction();//TODO  拍卖开始逻辑处理
        //倒数指定分钟数秒诱发降价
        try {
            int timeSection = auctionConfig.getTimeSection();
            if (timeSection <= 0) {
                timeSection = 1;
            }
            scheduExec.scheduleWithFixedDelay(downPriceTask, timeSection, timeSection, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        super.start(); //入口必须有
    }

    @Override
    public void run() {
        logger.info("Auctioneer_run_start");
        while (!endAuction) {
            try {
                Message bidMsg = bidQueue.take();
                if (bidMsg != null) {
                    processMsg(bidMsg);
                }
            } catch (InterruptedException e) {
                endAuction = true;
                logger.info("Auctioneer error=" + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.info("Auctioneer_run_end");
        interrupt();
    }

    /**
     * 处理拍卖信息
     *
     * @param bidMsg
     */
    private void processMsg(Message bidMsg) {
        if (bidMsg.getTags().equals("DOWN_PRICE")) {
            if (mid == 0) {
                if (currentPrice > auctionConfig.getLowPrice()) {
                    prePrice = currentPrice;
                    currentPrice = currentPrice - auctionConfig.getScopePrice();
                    if (currentPrice < auctionConfig.getLowPrice()) {
                        currentPrice = auctionConfig.getLowPrice();
                    }
                    sendNotify(EnumAuctionMsgType.DOWN_PRICE, null);
                    logger.debug("periodTag=" + periodTag + ",降价到currentPrice=" + currentPrice);
                } else {
                    logger.debug("本期流拍,grouptag=" + periodTag);
                    endAuction = true;
                    scheduExec.shutdownNow();
                    sendNotify(EnumAuctionMsgType.AUCTION_LOSE, null);
                    this.loseAuction();//TODO 流拍逻辑处理
                }
            } else {
                logger.debug("降价前被出价");
            }
        } else if (bidMsg.getTags().equals(periodTag)) {
            logger.debug("拍卖师监听到竞价消息=" + bidMsg.toString());
            String strBidMsg = null;
            try {
                strBidMsg = new String(bidMsg.getBody(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("竞价消息读取错误,error" + e.getMessage());
                e.printStackTrace();
            }
            BidMessage bidMessage = JsonUtils.jsonToPojo(strBidMsg, BidMessage.class);
            if (bidMessage != null && bidMessage.getAuctionPrice() > currentPrice) {
                mid = bidMessage.getMid();
                prePrice = currentPrice;
                currentPrice = bidMessage.getAuctionPrice();
                userName = bidMessage.getUserName();
                headImg = bidMessage.getHeadImg();
                sendNotify(EnumAuctionMsgType.NEW_BID, null);
                this.rendAuctionRecord();//TODO 出价记录保存
                //拍中后倒数30秒诱发下锤
                scheduExec.shutdownNow();
                scheduExecHammer.shutdownNow();
                scheduExecHammer = Executors.newScheduledThreadPool(1);
                scheduExecHammer.scheduleWithFixedDelay(new HammerTask(bidQueue), RAP_HAMMER_WAIT_SECOND, Integer.MAX_VALUE, TimeUnit.SECONDS);
                logger.debug("出价后倒数" + RAP_HAMMER_WAIT_SECOND + "秒开始,mid=" + mid + ",currentPrice=" + currentPrice + ",bidMessage=" + bidMessage.toString());
            } else {
                logger.debug("拍价无效,bidMessage=" + bidMessage.toString());
            }
        } else if (bidMsg.getTags().equals("RAP_HAMMER")) {
            scheduExecHammer.shutdownNow();
            endAuction = true;
            logger.debug("下锤!用户mid=" + mid + ",currentPrice=" + currentPrice);
            String orderId = this.hammerHandle();//TODO 下锤后逻辑处理（退保证金、生成订单、生成成交记录及更新配置（auction_config）逻辑处理）
            sendNotify(EnumAuctionMsgType.WAIT_PAY, orderId);
        }
    }

    /**
     * 更新公告板
     *
     * @param type 消息类型:0-本期开拍;1-价格下降;2-流拍;3-有新出价;4-待支付
     *             BID_START("BID_START"),
     *             DOWN_PRICE("DOWN_PRICE"),
     *             AUCTION_LOSE("AUCTION_LOSE"),
     *             NEW_BID("NEW_BID"),
     *             WAIT_PAY("WAIT_PAY");
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    public void sendNotify(EnumAuctionMsgType type, String orderId) {
        long sysTime = jedisUtil.time();   //系统当前时间
        long curTime = sysTime; //用户出价的时间
        long changeTime = sysTime;    //下次时间
        int invertSecond = 0;    //倒数秒数

        if (type == EnumAuctionMsgType.BID_START || type == EnumAuctionMsgType.DOWN_PRICE) {
            invertSecond = auctionConfig.getTimeSection() * 60;
            changeTime = sysTime + Long.valueOf(invertSecond) * 1000;
        } else if (type == EnumAuctionMsgType.AUCTION_LOSE) {
            invertSecond = 0;
            changeTime = sysTime;
        } else if (type == EnumAuctionMsgType.NEW_BID) {
            invertSecond = RAP_HAMMER_WAIT_SECOND;
            changeTime = sysTime + Long.valueOf(invertSecond) * 1000;
        } else if (type == EnumAuctionMsgType.WAIT_PAY) {
            invertSecond = 0;
            changeTime = sysTime;
        }
        String goodsTag = auctionConfig.getSysCode() + "-" + auctionConfig.getGoodsId();
        BidMessage bidMessage = new BidMessage();
        bidMessage.setPeriodTag(periodTag);
        bidMessage.setGoodsTag(goodsTag);
        bidMessage.setMid(mid);
        bidMessage.setUserName(userName);
        bidMessage.setHeadImg(headImg);
        bidMessage.setPrePrice(prePrice);
        bidMessage.setAuctionPrice(currentPrice);
        bidMessage.setFloatPrePrice(prePrice / 100f);
        bidMessage.setFloatAuctionPrice(currentPrice / 100f);
        bidMessage.setSysCode(auctionConfig.getSysCode());
        bidMessage.setGoodsId(auctionConfig.getGoodsId());
        bidMessage.setGoodsName(auctionConfig.getGoodsName());
        bidMessage.setGoodsImage(auctionConfig.getGoodsImage());
        bidMessage.setCurrentPeriods(auctionConfig.getCurrentPeriods());
        bidMessage.setSysTime(sysTime);
        bidMessage.setChangeTime(changeTime);
        bidMessage.setCurTime(curTime);
        bidMessage.setInvertSecond(invertSecond);
        bidMessage.setChangeTime(changeTime);
        bidMessage.setType(type.ordinal()); //返回其整数值
        if (StringUtils.isNotBlank(orderId)) {
            bidMessage.setOrderId(Integer.valueOf(orderId));
        }
        String strJsonBidMessage = JsonUtils.objectToJson(bidMessage);

        JedisUtil.Strings strings = jedisUtil.new Strings();
        strings.set(periodTag, strJsonBidMessage);
        strings.set(goodsTag, strJsonBidMessage);
        jedisUtil.expire(goodsTag, 3600); //设置过期时间为默认的60分钟
        jedisUtil.expire(periodTag, 3600); //设置过期时间为默认的60分钟

        Message msg = null;
        try {
            msg = new Message("auctionNotify", periodTag, "", strJsonBidMessage.getBytes("UTF-8"));
            SendResult sendResult = producerAuctioneer.getProducer().send(msg);
        } catch (Exception e) {
            logger.error("sendNotify,error=" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @Description: 拍卖开始逻辑处理
     * @author xieyc
     * @date 2018年7月10日 下午5:18:41
     */
    private int startAuction() {
        AuctionHistory auctionHistory = auctionHistoryMapper.getByConfIdAndCurrentPeriods(auctionConfig.getId(), auctionConfig.getCurrentPeriods());
        if (auctionHistory != null) {//返回id
            return auctionHistory.getId();
        } else {// 插入
            Date newDate = new Date(jedisUtil.time());// 当前时间
            AuctionHistory saveAuctionHistory = new AuctionHistory();// 初始化auction_history表
            saveAuctionHistory.setConfId(auctionConfig.getId());// 配置id
            saveAuctionHistory.setGoodsId(auctionConfig.getGoodsId());// 商品id
            saveAuctionHistory.setStartTime(newDate);// 本期拍卖的开始时间
            saveAuctionHistory.setCurrentPeriods(auctionConfig.getCurrentPeriods());// 当前期数
            saveAuctionHistory.setActPrice(auctionConfig.getActPrice());// 本期拍卖时的拍卖价格
            saveAuctionHistory.setLowPrice(auctionConfig.getLowPrice());// 本期拍卖时的最低价格
            saveAuctionHistory.setHighPrice(auctionConfig.getHighPrice());// 本期拍卖时的最高价格
            saveAuctionHistory.setTimeSection(auctionConfig.getTimeSection());// 本期拍卖时的降价时间区间
            saveAuctionHistory.setCashDeposit(auctionConfig.getCashDeposit());// 本期拍卖时的保证金
            saveAuctionHistory.setScopePrice(auctionConfig.getScopePrice());// 本期拍卖时的降价值
            saveAuctionHistory.setAddTime(newDate);//记录生成时间
            saveAuctionHistory.setUpdateTime(newDate);// 更新时间
            auctionHistoryMapper.insertSelective(saveAuctionHistory);
            return saveAuctionHistory.getId();
        }
    }

    /**
     * @Description: 流拍逻辑处理
     * @author xieyc
     * @date 2018年7月10日 下午5:18:41
     */
    private void loseAuction() {
        Date newDate = new Date(jedisUtil.time());//当前时间
        if (historyId != 0) {
            AuctionHistory updateAuctionHistory = new AuctionHistory();
            updateAuctionHistory.setId(historyId);
            updateAuctionHistory.setType(2);//状态:0-初始化;1-成交；2-流拍
            updateAuctionHistory.setLoseTime(newDate);//本期流拍时间
            updateAuctionHistory.setUpdateTime(newDate);//更新时间
            auctionHistoryMapper.updateByPrimaryKeySelective(updateAuctionHistory);
        }
        AuctionConfig updateAuctionConfig = new AuctionConfig();
        updateAuctionConfig.setId(auctionConfig.getId());
        updateAuctionConfig.setCurrentPeriods(auctionConfig.getCurrentPeriods() + 1);//当前期加+1
        auctionConfigMapper.updateByPrimaryKeySelective(updateAuctionConfig);
    } 

    /**
     * @Description: 向出价记录表中加数据
     * @author xieyc
     * @date 2018年7月10日 下午4:31:46
     */
    private void rendAuctionRecord() {
        AuctionRecord saveAuctionRecord = new AuctionRecord();
        saveAuctionRecord.setSysCode(auctionConfig.getSysCode());//商品系统来源
        saveAuctionRecord.setGoodsId(auctionConfig.getGoodsId());//商品id
        saveAuctionRecord.setGoodsSkuId(auctionConfig.getGoodsSkuId());//商品skuid
        saveAuctionRecord.setmId(mid);//出价用户id
        saveAuctionRecord.setHeadImg(headImg);//出价用户头像
        saveAuctionRecord.setUserName(userName);//出价用户名字
        saveAuctionRecord.setAucId(auctionConfig.getId());//配置id
        saveAuctionRecord.setCurrentPeriods(auctionConfig.getCurrentPeriods());//当前期
        saveAuctionRecord.setAucPrice(currentPrice);//出价价格
        saveAuctionRecord.setAddTime(new Date(jedisUtil.time()));//出价时间
        try {
            recordQueue.put(saveAuctionRecord);
        } catch (InterruptedException e) {
            logger.error("rendAuctionRecord error=" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @Description: 下锤后逻辑处理（退保证金、生成订单、生成成交记录及更新配置（auction_config）逻辑处理）
     * @author xieyc
     * @date 2018年7月9日 下午5:33:49
     */
    private String hammerHandle() {
        String orderId = null;// 订单id
        String orderNo = null;// 订单号
        Integer statusCode = null;//响应状态码（200成功）
        HttpResponse resp = this.rendAucOrderAndRefundDeposit();// 订单生成与退保证金
        try {
            if (resp != null) {
                statusCode = resp.getStatusLine().getStatusCode();//获取请求状态码
                if (statusCode != null && statusCode == 200) {
                    String strResponse = EntityUtils.toString(resp.getEntity());
                    JSONObject jsonbject = JSONObject.fromObject(strResponse);
                    JSONObject jsonbjectDate = JSONObject.fromObject(jsonbject.get("data"));
                    orderId = String.valueOf(jsonbjectDate.get("id")); // 订单id
                    orderNo = (String) jsonbjectDate.get("orderNo");// 订单号
                    if (StringUtils.isBlank(orderId)) {
                        logger.error("生成订单失败strResponse=" + strResponse);
                    } else {
                        logger.debug("生成订单orderId=" + orderId);
                    }
                } else {
                    logger.error("生成订单失败,statusCode=" + statusCode);
                }
            }
        } catch (Exception e) {
            logger.error("获取订单id和订单号  error=" + e.getMessage());
            e.printStackTrace();
        }

        /************************* auction_history 表更新 start**********************************/
        if (historyId != 0) {
            Date newDate = new Date(jedisUtil.time());// 当前时间
            AuctionHistory updateAuctionHistory = new AuctionHistory();
            updateAuctionHistory.setId(historyId);
            updateAuctionHistory.setType(1);// 状态:0-初始化;1-成交；2-流拍
            updateAuctionHistory.setBargainMid(mid);//成交人id
            if (orderId != null && orderNo != null) {
                updateAuctionHistory.setOrderId(Integer.valueOf(orderId));//成交订单id
                updateAuctionHistory.setOrderNo(orderNo);//成交订单号
            }
            updateAuctionHistory.setBargainPrice(currentPrice);//成交价格
            updateAuctionHistory.setUpdateTime(newDate);//更新时间
            auctionHistoryMapper.updateByPrimaryKeySelective(updateAuctionHistory);
        }
        /************************* auction_history 表更新 end**********************************/

        /**************************** auction_config 表更新 start******************************/
        AuctionConfig updateAuctionConfig = new AuctionConfig();
        updateAuctionConfig.setId(auctionConfig.getId());
        updateAuctionConfig.setStoreNum(auctionConfig.getStoreNum() - 1);// 减拍卖库存
        updateAuctionConfig.setCurrentPeriods(auctionConfig.getCurrentPeriods() + 1);// 当前期加+1
        auctionConfigMapper.updateByPrimaryKeySelective(updateAuctionConfig);
        logger.debug("下锤!更新拍卖信息！");
        /**************************** auction_config 表更新 end ********************************/

        return orderId;
    }

    /**
     * @Description: 订单生成与退保证金
     * @author xieyc
     * @date 2018年7月16日 下午6:57:15
     */
    private HttpResponse rendAucOrderAndRefundDeposit() {
        HttpResponse resp = null;
        try {
            //HttpPost httpPost = new HttpPost("http://localhost:8080/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
            HttpPost httpPost = new HttpPost(Contants.BIN_HUI_URL + "/bh-product-api/auction/rendAucOrderAndRefundDeposit.json");
            CloseableHttpClient client = HttpClients.createDefault();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("goodsId", auctionConfig.getGoodsId());// 商品id
            jsonParam.put("mId", mid);// 拍中的用户id
            jsonParam.put("auctionPrice", currentPrice);// 成交价格
            jsonParam.put("currentPeriods", auctionConfig.getCurrentPeriods());// 当前期
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            resp = client.execute(httpPost);
            logger.debug("下锤!生成订单与退保证金！");
        } catch (Exception e) {
            logger.error("rendAucOrderAndRefundDeposit error=" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

}
