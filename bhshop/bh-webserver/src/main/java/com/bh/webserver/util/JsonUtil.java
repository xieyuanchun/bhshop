package com.bh.webserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象toJSON
     * @param object
     * @return
     */
    public static String getString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * json转换Bean
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
