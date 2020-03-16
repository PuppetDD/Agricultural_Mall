package com.gold.mall.common.utils;

import com.gold.mall.common.constant.ResponseStatusConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <响应数据封装类>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {

    /**
     * 状态码
     */
    private int status;
    /**
     * 消息
     */
    private String message;
    /**
     * 返回的数据
     */
    private Object data;

    /**
     * @param []
     * @return {@link ResponseResult}
     * Description <成功>
     * @author GOLD
     * @date 2020/2/24 14:16
     */
    public static ResponseResult success() {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, "success", null);
    }

    /**
     * @param [message]
     * @return {@link ResponseResult}
     * Description <成功 带 message>
     * @author GOLD
     * @date 2020/2/24 14:16
     */
    public static ResponseResult success(String message) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, message, null);
    }

    /**
     * @param [data]
     * @return {@link ResponseResult}
     * Description <成功，带data>
     * @author GOLD
     * @date 2020/2/24 14:16
     */
    public static ResponseResult success(Object data) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, "success", data);
    }

    /**
     * @param [message, data]
     * @return {@link ResponseResult}
     * Description <成功，带data 和 message>
     * @author GOLD
     * @date 2020/2/24 14:17
     */
    public static ResponseResult success(String message, Object data) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_SUCCESS, message, data);
    }

    /**
     * @param []
     * @return {@link ResponseResult}
     * Description <失败>
     * @author GOLD
     * @date 2020/2/24 14:17
     */
    public static ResponseResult fail() {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_FAIL, "fail", null);
    }

    /**
     * @param [message]
     * @return {@link ResponseResult}
     * Description <失败 带消息>
     * @author GOLD
     * @date 2020/2/24 14:17
     */
    public static ResponseResult fail(String message) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_FAIL, message, null);
    }

    /**
     * @param [message]
     * @return {@link ResponseResult}
     * Description <失败 带消息>
     * @author GOLD
     * @date 2020/2/24 14:17
     */
    public static ResponseResult deny(String message) {
        return new ResponseResult(ResponseStatusConstant.RESPONSE_STATUS_NO_PERMISSION, message, null);
    }

}
