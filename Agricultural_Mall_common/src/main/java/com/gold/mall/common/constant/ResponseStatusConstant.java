package com.gold.mall.common.constant;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <响应客户端请求数据常量>
 */
public interface ResponseStatusConstant {

    /**
     * 响应状态码 1 表示成功
     */
    int RESPONSE_STATUS_SUCCESS = 1;
    /**
     * 响应状态码 2 表示失败
     */
    int RESPONSE_STATUS_FAIL = 2;
    /**
     * 响应状态码 3 表示权限不足
     */
    int RESPONSE_STATUS_NO_PERMISSION = 3;

}
