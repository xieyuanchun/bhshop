package com.bh.auc.obj;


import com.bh.auc.mapper.AuctionConfigMapper;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.rocketMQ.AuctionHouseBidListener;
import com.bh.auc.rocketMQ.SpringProducer;
import com.bh.auc.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;

/**
 * 拍卖行对象
 */
public class AuctionHouse extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AuctionHouse.class);
    private HashMap<String, Auctioneer> mapAuctioneer = null;
    private ApplicationContext appContext;
    private AuctionConfigMapper auctionConfigMapper;
    private JedisUtil jedisUtil = JedisUtil.getInstance();
    private SpringProducer producerAuctioneer; //通知到notify 主题的生产者
    private AuctionRecordTask auctionRecordTask; //竞价记录任务

    public AuctionHouse(ApplicationContext appContext) {
        this.appContext = appContext;
        this.producerAuctioneer = (SpringProducer) appContext.getBean("producerAuctioneer");
        this.auctionRecordTask = new AuctionRecordTask(appContext);
    }

    @Override
    public synchronized void start() {
        auctionConfigMapper = (AuctionConfigMapper) appContext.getBean("auctionConfigMapper");
        //初始化mapAuctioneer begin
        AuctionHouseBidListener auctionHouseBidListener = (AuctionHouseBidListener) appContext.getBean("auctionHouseBidListener");
        mapAuctioneer = auctionHouseBidListener.getMapAuctioneer();
        auctionRecordTask.start();
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String periodTag = null;
                List<AuctionConfig> list = auctionConfigMapper.getAllWaitAcu();
                if (list != null && list.size() > 0) {
                    for (AuctionConfig auctionConfig : list) {
                        //periodTag=系统来源标识-商品id-期数
                        periodTag = auctionConfig.getSysCode() + "-" + auctionConfig.getGoodsId() + "-" + auctionConfig.getCurrentPeriods();
                        Auctioneer auctioneer = mapAuctioneer.get(periodTag);
                        if (auctioneer == null) {
                            Auctioneer auctioneerNew = new Auctioneer(appContext, periodTag, auctionConfig, mapAuctioneer, auctionConfigMapper, jedisUtil,
                                    producerAuctioneer, auctionRecordTask.recordQueue);
                            auctioneerNew.start();
                            mapAuctioneer.put(periodTag, auctioneerNew);
                            logger.info("启动拍卖periodTag=" + periodTag);
                        } else {
//                            logger.debug("已有此期拍卖periodTag=" + periodTag);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("启动拍卖失败=" + e.getMessage());
                e.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(20 * 1000l); //30 秒检查一次新一期拍卖
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

}
