package com.gold.mall.common.config;

/**
 * @author GOLD
 * @date 2020/2/26 14:11
 * Description <支付宝配置>
 */
public class AliPayConfig {

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    public static String app_id = "2016101900720931";
    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCi9o0lZC0RVvxVjblbqLLFRWozYfu9OKSN5LNaIoBHM5MP+KZvzVg+OCtOxBh7MtshG/AEGTJKPmid8vJt82hdpITqwMpy6WLrClaV3rLwvF3VyuPFGMiSWDkww4XBkVrsPjGiLjHPbLV2XSc5i/YMdicPcpsGz6EZ9L8zTMsRb4ePARopnhgYjJttfl6FkZlg94czoDLWamK+nh9B1jAaLJO6QuIcO2en2I2s2q8+8ah/5ayTUdIYBEPjI5P26voSBkVNEjPmvJjRS5t7afTCFtU7DjNF//0zX/I17tqdk0nHtfLDS0CwJmiIZA0QkMJdcWFGC3cIzdb6P/NT8FwfAgMBAAECggEAYLF2EmecLhu04XaUMwSn/lEvsIlE+4NucBrxKFwsTRMXAXUCHHh1EYX2fGfyNrd4Oe7/vQ7OPitFm+KZYg/5Lwb9LuVxujkszlV2Brr65ch+zOnzt/D6oYrjdEGvTalXj7FfdeF/AJp6GV8wdP7M4ZwBlTLlNWdhKauE/4VjQFY/Y/EDYJ3PrXzEw5fOHULL1oZ5kjd0LZASU7T0hHPOa0hqT78hdZx5pdXSGZSG0i/wO4BXl083SfJIqyi9OLp45J1+tKtPgmvtsEVDcmwTeqDmcDdpIttV0itrTH1nArclYdggGbFbZzZagHnmdxokpLdG1HMxnUijV9jEGXnfYQKBgQDgI+OMak7fIwYD86BGcGSlLkyzeaqJJz7q57cmXENkFS7fvMUt+ykz6Igyk9zHOB9bGRIZJ8LAU6WTIG6rwG4rzPjk1w26nMvSf/GpZH4MPXQchHVrBQaYUujAGeZiw5GIrdt24l7eesg2Rad9Aw/5hd/ehU5MzIIPOI3h/w2btwKBgQC6IIYCcjXtZmn98zVCbtW0Q9JJeLmev8w1NExeEtNBXfdOhs9lH8f8gtECDiNqZgZcVh05IBGjubHLaeNX74wiXxSLQG3OX660GAEPJob1u5qksUqnxRKQdjqDkoq3ZK1rxjjMGr3soEvYBh1OLFXgPy15JJuugUZP3uYJeC+S2QKBgHe9VHuX80irSjRkO1RGCM5hnKIoth0NNUSCw0of8fSOiHsIOoJk8c8EQ3ZZnZFj/wRIsW1020qppQOjSNxCMdj+aFzAMlpmF1D9/tkSkW/yiIwPiza/+ZT4fdTaQUSz25Sb5GlvZ2UNTNYJgf972lXf2gfjSUAn5LdC7DE2c0ujAoGAQY/lRIVQg6A4o7CAyTbbZhUhxKmW0NmEqBUU9WoyKetciren5FoOit7VmqC6TudDRFE9F/s423A0tY9F+pqxxQWy6d43D6PamSt/bAPJNFPyBCNDRdeAU4yhzejXngQDplv7c8HTtr88GAsHVZr+YPBAJ1UC1bRJvSap5xoDv/kCgYEAi0asQC5K/8B6mrQTfVW4Cjxn857PvNUdO7smyQpbTFTvDYSd9/ZkLYAZpwXI3kj5Pu3VDAcreMaWpuUDfR9v4PtOZ7AWcI8Sqk2VKRAWcwjgESAK99/E2jdhqYWHYJXJmHPWpoyppkDcMbQ4RM9xZ/ZWODCxX0vptAYc3uopRRA=";

    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUR+Tsaf4JzV67TU0KrFJCj3UdpOTMEfTLNR5RQOjJBnObmEfmg7eZnxm0JlQ2JuiqrNFEoZYuY+TDrMrWQLeXJhBvmfBxqfNs51bYl2fibEZMiWMLYc2CFXHgeM9V5Tln+F88Hx9cMKXUgn2LXYCWZLjIsDhm8zafO8wTBWWGIB9K5ONqRAthvL2y0mFss+ITsZQwRIJkGtzoPr0vbYsA6IEA0Sab/dqTuX34muWpGMINKEYgotb4BVRMXsdzByMi0OCXnRKICRXvBVzLzjJDHxkC/VqIlPW8Ix/7agAYUcjgKBgxlLDKomUtIZ5zvpKthOqR8GKq/lXShfgyhtJQIDAQAB\n";

    /***
     *功能描述: 服务器异步通知页面路径  需 http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    //public static String notify_url = "http://120.77.212.201/reception/pay/aliPayNotifyNotice";

    /**
     * 服务器异步通知页面路径，部署服务器上，注意修改地址
     */
    public static String notify_url = "http://120.77.212.201:8080/A-Mall_Reception/pay/aliPayNotifyNotice";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //public static String return_url = "https://120.77.212.201/reception/pay/aliPayReturnNotice";
    /**
     * 页面跳转同步通知页面路径，部署服务器上，注意修改地址
     */
    public static String return_url = "http://120.77.212.201:8080/A-Mall_Reception/reception/pay/aliPayReturnNotice";

    /**
     * 签名方式
     */
    public static String sign_type = "RSA2";

    /**
     * 字符编码格式
     */
    public static String charset = "utf-8";

    /**
     * 支付宝网关
     */
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

}
