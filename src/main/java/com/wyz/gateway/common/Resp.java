package com.wyz.gateway.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 接口结果包裹类
 *
 * @author wyz
 * @version 1.0
 * @date 2020-11-04 14:41
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Accessors(chain = true)
public class Resp<T> {
    /**
     * 接口处理结果code标志, 默认情况下 0 成功, -1 失败, 其他情况下的交互互相协商即可
     **/
    private Integer code;

    /**
     * 接口处理结果message标志, 默认情况下 success 或 处理成功 标志成功, error 或 处理失败 标志失败
     **/
    private String message;

    /**
     * 结构处理完成后需要返回的数据本身
     **/
    private T data;

    /**
     * 某些情况下需要额外携带一些交互值, 都在这个字段中
     * 注: 此字段被Jackson的注解标志, 为null的情况下不会参与到Json序列化, 既为null时接口返回值中无此字段
     **/
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Object extra;

    /**
     * javadoc ok
     *
     * @param code    自定义code
     * @param message 自定义消息
     * @param data    返回的数据
     * @return com.ws.leo.common.response.LeoResp<T>
     * @apiNote 接口处理成功, 但是需要自定义的code 及 message 与前端交互
     * @author wyz
     * @date 2020/11/04 15:18
     * @modified none
     */
    public static <T> Resp<T> ok(Integer code, String message, T data) {
        return ok(code, message, data, null);
    }

    public static <T> Resp<T> ok(Integer code, String message, T data, Object extra) {
        return new Resp<>(code, message, data, extra);
    }

    public static <T> Resp<T> error(RespType respType) {
        return ok(respType.getCode(), respType.getMessage(), null);
    }
}
