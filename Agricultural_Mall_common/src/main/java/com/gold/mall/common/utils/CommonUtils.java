package com.gold.mall.common.utils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <通用工具类 uuid sign>
 */
public class CommonUtils {

    /**
     * @param []
     * @return {@link String}
     * Description <生成uuid,作为一笔订单流水，也用作 nonce_str>
     * @author GOLD
     * @date 2020/2/24 14:04
     */
    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(0, 32);
        return uuid;
    }

    /**
     * @param [data]
     * @return {@link String}
     * Description <md5 生成工具类>
     * @author GOLD
     * @date 2020/2/24 14:04
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : digest) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
