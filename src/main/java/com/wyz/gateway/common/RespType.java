package com.wyz.gateway.common;

/**
 * 返回值code message枚举类
 *
 * @author wyz
 * @version 1.0
 * @date 2020-11-04 14:38
 **/
public enum RespType {
    /**
     * 正常情况下的响应值
     **/
    OK(0, "success"),
    /**
     * 正常情况下错误响应值
     **/
    ERROR(-1, "error"),

    NO_ROUTER_HANDLER_FOUND(404, "no router handler found"),

    RPC_FAILED(1000, "process request failed"),

    ILLEGAL_INNER_REQUEST(1001, "access denied for inner api"),

    ;

    private final Integer code;

    private final String message;

    RespType(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
