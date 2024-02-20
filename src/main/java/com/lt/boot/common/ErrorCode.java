package com.lt.boot.common;

import lombok.Getter;

/**
 * 自定义错误码
 */
@Getter
public enum ErrorCode {
    SUCCESS(200, "成功"),
    PARAMS_ERROR(40000, "请求参数不合法"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    UPLOAD_ERROR(50001, "文件太大上传失败"),
    OPERATION_ERROR(50002, "操作失败"),
    DB_SAVE_EXCEPTION(50003, "数据新增失败"),
    DB_BATCH_SAVE_EXCEPTION(50003, "数据批量新增失败"),
    DB_DELETE_EXCEPTION(50005, "数据删除失败"),
    DB_BATCH_DELETE_EXCEPTION(50006, "数据批量删除失败"),
    DB_UPDATE_EXCEPTION(50007, "数据更新失败"),
    DB_BATCH_UPDATE_EXCEPTION(50008, "数据批量更新失败"),
    INVALID_VERIFY_CODE(50009, "验证码错误"),
    REQUEST_OPERATE_FREQUENTLY(500010, "操作频繁,请稍后重试"),
    REQUEST_TIME_OUT(500011, "请求超时"),
    USER_NOT_EXISTS(50012, "用户不存在"),
    INVALID_USER_TYPE(50013, "无效的用户类型"),
    ACCOUNT_EXPIRATION(50014, "账号过期"),
    PASSWORD_ERROR(50015, "用户名或密码错误"),
    PASSWORD_EXPIRATION(50016, "密码过期"),
    ACCOUNT_UNAVAILABLE(50017, "账号不可用"),
    ACCOUNT_LOCKOUT(50018, "该账号被禁用"),
    ROLE_EXISTS_ERROR(50019, "该角色已经存在，请更改角色名称");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
