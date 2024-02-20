package com.lt.boot.common;


/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS, data);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, message);
    }

    /**
     * 失败
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     */
    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     */
    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse(errorCode.getCode(), null, message);
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, T data) {
        return new BaseResponse<>(errorCode.getCode(), data, message);
    }
}
