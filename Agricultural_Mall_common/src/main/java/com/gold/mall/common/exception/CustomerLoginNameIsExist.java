package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <用户账户名校验自定义异常>
 */
public class CustomerLoginNameIsExist extends RuntimeException {

    public CustomerLoginNameIsExist() {
        super();
    }

    public CustomerLoginNameIsExist(String message) {
        super(message);
    }

    public CustomerLoginNameIsExist(String message, Throwable cause) {
        super(message, cause);
    }

}
