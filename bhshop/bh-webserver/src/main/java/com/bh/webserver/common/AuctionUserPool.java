package com.bh.webserver.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.webserver.entity.HolllandAuctionEntity;

/**
 * 
 * 说明：用户池（sessionId/session）
 */
public class AuctionUserPool {
    private static Logger LOG = LoggerFactory.getLogger(AuctionUserPool.class);

    private static Map<String, HolllandAuctionEntity> AUCTION_USER_POOL = new HashMap<String, HolllandAuctionEntity>();

    private static Map<String, Map<String, HolllandAuctionEntity>> AUCTION_USERLIST_POOL = new HashMap<String, Map<String, HolllandAuctionEntity>>();

    public static void add(String key,HolllandAuctionEntity user) {
    	AUCTION_USER_POOL.put(key, user);
        LOG.info("user [" + key + "] connected");
    }

    public static void remove(String key) {
    	AUCTION_USER_POOL.remove(key);
        LOG.info("user [" + key + "] closed");
    }

    public static HolllandAuctionEntity get(String key) {
        return  AUCTION_USER_POOL.get(key);
    }
    
    /**
     * @param gAndpKey 商品id-期数
     * @param users 用户列表
     */
    public static void addAuctionEntityList(String gAndpKey,Map<String, HolllandAuctionEntity> users) {
    	AUCTION_USERLIST_POOL.put(gAndpKey, users);
        LOG.info("AuctionSocket users [" + gAndpKey + "] connected");
    }
    
    /**
     * @param gAndpKey 商品id-期数
     * @return 同一商品同个期数的用户列表
     */
    public static Map<String, HolllandAuctionEntity> getAuctionEntityList(String gAndpKey) {
        return  AUCTION_USERLIST_POOL.get(gAndpKey);
    }

    public static Map<String, HolllandAuctionEntity> getUserPool() {
        return AUCTION_USER_POOL;
    }
    
    public static Map<String, Map<String, HolllandAuctionEntity>> getUserMapPool() {
        return AUCTION_USERLIST_POOL;
    }
}
