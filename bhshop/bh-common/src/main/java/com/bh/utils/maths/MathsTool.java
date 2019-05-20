package com.bh.utils.maths;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * Created by ZhouYongyi on 2017/4/27.
 */
public class MathsTool {

    /**
     * 最急产生固定格式的ip 192.168.1.开头
     *
     * @return
     */
    public static String RandomIp() {
        int max = 255;
        int min = 1;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return "192.168.1." + String.valueOf(s);
    }

    /**
     * 最急产生固定格式的ip ，前缀自己定义
     *
     * @return
     */
    public static String RandomIp(String prefix) {
        int max = 255;
        int min = 1;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return prefix + String.valueOf(s);
    }

    /**
     * 随机产生最小为1，最大为max的整数
     *
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        int min = 1;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 随机产生min到max的整数
     *
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    /**
     * 随机产生浮点数
     *
     * @return
     */
    public static Double randomDouble() {
        Random random = new Random();
        return random.nextDouble();
    }

    /**
     * 字符串解析为两位小数点的浮点数
     *
     * @param value
     * @return
     */
    public static double formatDouble(String value) {
        if (StringUtils.isNotBlank(value)) {
            Double s = Double.valueOf(value);
            String ret = String.format("%.2f", s);
            return Double.valueOf(ret);
        } else {
            return 0.00f;
        }
    }

    /**
     * double解析为两位小数点的浮点数
     *
     * @param value
     * @return
     */
    public static double formatDouble(Double value) {
        if (value != null) {
            Double s = Double.valueOf(value);
            String ret = String.format("%.2f", s);
            return Double.valueOf(ret);
        } else {
            return 0.00f;
        }
    }

    /**
     * 产生18位随机数(0000-9999)
     * @return 4位随机数
     */
    public static String getRandomString() {
        Random random = new Random();
        String ret = String.valueOf(System.currentTimeMillis())
                + String.valueOf(random.nextInt(9999) % 9999);
        return ret;
    }
}
