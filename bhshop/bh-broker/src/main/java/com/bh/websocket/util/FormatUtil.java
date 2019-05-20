package com.bh.websocket.util;

import org.springframework.util.StringUtils;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：
 */
public class FormatUtil {

    /**
     * 过滤脚本
     * @param contant
     * @return
     */
    public static String formatScript(String contant) {
        if(StringUtils.isEmpty(contant)) {
            return contant;
        }
        contant = contant.replace("<", "&lt;");
        contant = contant.replace(">", "&gt;");
        return contant;
    }
}
