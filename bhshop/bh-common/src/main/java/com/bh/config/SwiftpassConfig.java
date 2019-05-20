package com.bh.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * <一句话功能简述>
 * <功能详细描述>配置信息
 * 
 * @author  Administrator
 * @version  [版本号, 2014-8-29]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SwiftpassConfig {
    
    /**
     * 威富通交易密钥
     */
//测试
//	 public static String key="9d101c97133837e13dde2d32a5054abb" ;
    public static String key="463ab70f662f4ea71050c750d0e83605" ;
    
    /**
     * 威富通商户号
     */
     public static String mch_id="400590008629";
    //测试
 //   public static String mch_id="7551000001";
    
    
    /**
     * 威富通请求url
     */
    public static String req_url="https://pay.swiftpass.cn/pay/gateway";
    
    /**
     * 通知url
     */
    public static String notify_url=Contants.BIN_HUI_URL+"/bh-order-api/payCallback/swiftpassCallback";
    /**
     * 微信appid 、app_Secret
     */
 /*   public static String sub_appid="wxe96accb07b947e01";
    public static String app_Secret="3fb7f510985fbbde530c3ce868ab6757";*/
    
}
