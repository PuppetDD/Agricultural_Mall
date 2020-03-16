package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <手机号未注册自定义异常类>
 */
public class PhoneNotRegisterException extends RuntimeException {

    public PhoneNotRegisterException() {
        super();
    }

    public PhoneNotRegisterException(String message) {
        super(message);
    }

    public PhoneNotRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

}
