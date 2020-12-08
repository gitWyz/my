package com.wyz.gateway.handler;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * javadoc 全局统一异常处理器
 *
 * @author wyz
 * @date 2020-11-04 16:20
 * @version 1.0.0
 */
@Slf4j
public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {
    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public GlobalExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        final Map<String, Object> result = Maps.newHashMap();
        result.put("data", null);
        Throwable error = super.getError(request);
        log.error("", error);
        //NotFoundException){
        if (error instanceof ResponseStatusException){
            ResponseStatusException e = (ResponseStatusException) error;
            if (e.getStatus().value() == HttpStatus.NOT_FOUND.value()){
                result.put("code", 404);
                result.put("message", "no router handler found for this request");
                return result;
            }
        }
        result.put("code", 500);
        result.put("message", "Customized Internal Server Error");
        return result;
    }

    // 永远都返回200

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
        //return super.getHttpStatus(errorAttributes);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        //return super.getRoutingFunction(errorAttributes);
    }
}
