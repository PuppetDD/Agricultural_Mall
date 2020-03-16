package com.gold.mall.common.config;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <微信支付配置类>
 */
public class WxPayConfig {

    /**
     * 支付公众账号的 appid
     */
    public final static String wxpay_appId = "";
    /**
     * 支付公众账号的 app_secret
     */
    public final static String wxpay_appsecret = "";
    /**
     * 商户id
     */
    public final static String wxpay_mer_id = "";
    /**
     * 支付秘钥
     */
    public final static String wxpay_key = "";
    /**
     * 支付回调地址
     * */
    //public final static String wxpay_callback = "http://120.77.212.201/reception/wxPay/callback";
    /**
     * 部署服务器上
     */
    public final static String wxpay_callback = "http://120.77.212.201:8080/A-Mall_Reception/reception/wxPay/callback";
    /**
     * 统一下单url
     */
    /**
     * public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
     */
    public final static String UNIFIED_ORDER_URL = "";

}
