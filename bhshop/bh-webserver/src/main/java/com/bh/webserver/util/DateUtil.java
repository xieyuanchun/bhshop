package com.bh.webserver.util;

import java.time.LocalDateTime;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：
 */
public class DateUtil {

    public static String now() {
        return LocalDateTime.now().toString().replace("T", " ");
    }
}
