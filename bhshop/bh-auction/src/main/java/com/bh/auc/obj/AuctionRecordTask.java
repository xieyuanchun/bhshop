package com.bh.auc.obj;

import com.bh.auc.mapper.AuctionRecordMapper;
import com.bh.auc.pojo.AuctionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AuctionRecordTask extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(HammerTask.class);
    public BlockingQueue<AuctionRecord> recordQueue = new LinkedBlockingQueue<AuctionRecord>(); //内部消息队列
    private List<AuctionRecord> listWaitInsert = new ArrayList<AuctionRecord>(); //等待插入数据
    private long lastInsertTime = System.currentTimeMillis(); //最新入库时间
    private AuctionRecordMapper auctionRecordMapper = null;//表AuctionRecord数据库对象
    private ApplicationContext appContext;

    public AuctionRecordTask(ApplicationContext appContext) {
        this.appContext = appContext;
        this.auctionRecordMapper = (AuctionRecordMapper) appContext.getBean("auctionRecordMapper");
    }

    @Override
    public void run() {
        logger.info("AuctionRecordTask_run_start");
        while (true) {
            try {
                AuctionRecord auctionRecord = recordQueue.poll(1, TimeUnit.SECONDS);
                if (auctionRecord != null) {
                	listWaitInsert.add(auctionRecord);  
                    if (listWaitInsert.size() >= 10 || (System.currentTimeMillis() - lastInsertTime) >= 2000l) {
                        if(listWaitInsert.size()>0) {
                            auctionRecordMapper.insertBatch(listWaitInsert); 
                            listWaitInsert.clear();
                        }
                        lastInsertTime = System.currentTimeMillis();
                    }
                }
            } catch (InterruptedException e) {
                logger.info("AuctionRecordTask error=" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
