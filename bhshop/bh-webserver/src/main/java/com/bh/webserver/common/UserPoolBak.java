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
public class UserPoolBak {
    private static Logger LOG = LoggerFactory.getLogger(UserPoolBak.class);

    private static Map<Integer, Object> USER_POOL = new HashMap<Integer, Object>();

    public static void add(Integer mId,UserEntity user) {
        USER_POOL.put(mId, user);
        LOG.info("user [" + mId + "] connected");
    }

    public static void remove(Integer mId) {
        USER_POOL.remove(mId);
        LOG.info("user [" + mId + "] closed");
    }

    public static UserEntity get(Integer mId) {
        return (UserEntity) USER_POOL.get(mId);
    }

    public static Map<Integer, Object> getUserPool() {
        return USER_POOL;
    }
}
