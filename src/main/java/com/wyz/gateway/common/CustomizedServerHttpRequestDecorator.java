package com.wyz.gateway.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;

/**
 * javadoc rebuild request
 *
 * @author wyz
 * @date 2020/11/04 17:17
 * @version 1.0.0
 */
public class CustomizedServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private final HttpHeaders refactorHeaders;
    public CustomizedServerHttpRequestDecorator(ServerHttpRequest delegate, HttpHeaders headers) {
        super(delegate);
        this.refactorHeaders = headers;
    }

    @Override
    @NonNull
    public HttpHeaders getHeaders() {
        return this.refactorHeaders;
    }
}
