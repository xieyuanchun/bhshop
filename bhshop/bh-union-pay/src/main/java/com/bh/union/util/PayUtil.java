package com.bh.union.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PayUtil {
	public static  String buildSignString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key) || "sign_type".equals(key))
                continue;
            if (StringUtils.isEmpty(params.get(key)))
                continue;
            keys.add(key);
        }

        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }

        return buf.toString();

    }
	
	public static  Map<String,String> buildSignMap(Map<String, String> params) {
        List keys = new ArrayList(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key) || "sign_type".equals(key))
                continue;
            if (params.get(key)==null)
                continue;
            keys.add(key);
        }
        Collections.sort(keys);
        Map retMap = new HashMap<Object,Object>();
        for (int i = 0; i < keys.size(); i++) {
        	Object key = keys.get(i);
        	Object value = params.get(key);
            retMap.put(key, value);
        }
        return retMap;
      
    }
}
