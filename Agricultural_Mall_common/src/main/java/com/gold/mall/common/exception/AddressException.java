package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <收货地址异常>
 */
public class AddressException extends RuntimeException {

    public AddressException() {
        super();
    }

    public AddressException(String message) {
        super(message);
    }

    public AddressException(String message, Throwable cause) {
        super(message, cause);
    }

}
