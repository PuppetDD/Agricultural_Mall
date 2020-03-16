package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <用户不存在自定义异常>
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
