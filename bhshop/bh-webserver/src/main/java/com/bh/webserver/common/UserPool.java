package com.bh.webserver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.webserver.entity.UserEntity;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 说明：用户池（sessionId/session）
 */
public class UserPool {
    private static Logger LOG = LoggerFactory.getLogger(UserPool.class);

    private static Map<Integer, Object> USER_POOL = new HashMap<Integer, Object>();

    public static void add(Integer key,UserEntity user) {
        USER_POOL.put(key, user);
        LOG.info("user [" + key + "] connected");
    }

    public static void remove(Integer key) {
        USER_POOL.remove(key);
        LOG.info("user [" + key + "] closed");
    }

    public static UserEntity get(Integer key) {
        return (UserEntity) USER_POOL.get(key);
    }

    public static Map<Integer, Object> getUserPool() {
        return USER_POOL;
    }
}
