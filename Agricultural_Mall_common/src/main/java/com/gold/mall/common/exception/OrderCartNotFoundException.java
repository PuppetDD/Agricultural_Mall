package com.gold.mall.common.exception;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <订单 购物车异常>
 */
public class OrderCartNotFoundException extends RuntimeException {

    public OrderCartNotFoundException() {
        super();
    }

    public OrderCartNotFoundException(String message) {
        super(message);
    }

    public OrderCartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
