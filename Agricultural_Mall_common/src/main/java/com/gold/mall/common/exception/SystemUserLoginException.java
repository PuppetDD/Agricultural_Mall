package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <自定义系统用户登录异常>
 */
public class SystemUserLoginException extends RuntimeException {

    public SystemUserLoginException() {
        super();
    }

    public SystemUserLoginException(String message) {
        super(message);
    }

    public SystemUserLoginException(String message, Throwable cause) {
        super(message, cause);
    }

}
