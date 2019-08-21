package com.gangukeji.xzqn.utils;

import org.springframework.lang.Nullable;

/**
 * 错误码定义
 */
public enum ResultStatus {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    NO_CONTENT(204, "No Content"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    PARAM_ERROR(1001, "参数错误"),
    USER_NOT_FOUND(1101, "用户不存在"),
    USER_NOT_LOGIN(1102, "用户未登录"),
    USERNAME_OR_PASSWORD_ERROR(1103, "用户名或密码错误"),
    PASSWORD_ERROR(1104, "密码错误"),
    EMPTY(1201, "空"),
    ERROR_FORMAT_FILE(1202, "文件格式错误"),
    IO_EXCEPTION(1203, "文件读取时错误"),
    ENTITY_NOT_FOUND(1301, "数据不存在");


    private final int code;

    private final String msg;

    ResultStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    /**
     * Return the enum constant of this type with the specified numeric value.
     *
     * @param statusCode the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static ResultStatus valueOf(int statusCode) {
        ResultStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }


    /**
     * Resolve the given status code to an {@code HttpStatus}, if possible.
     *
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code HttpStatus}, or {@code null} if not found
     * @since 5.0
     */
    @Nullable
    public static ResultStatus resolve(int statusCode) {
        for (ResultStatus status : values()) {
            if (status.code == statusCode) {
                return status;
            }
        }
        return null;
    }
}
