package com.wyz.gateway.handler;

import com.wyz.gateway.common.Resp;
import com.wyz.gateway.common.RespType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * javadoc hystrix降级处理器
 * 
 * @author wyz
 * @date 2020/11/04 17:02
 * @version 1.0.0 
 */
@Component
@Slf4j
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    private static final Resp<Object> DEFAULT_BODY = Resp.error(RespType.RPC_FAILED);

    @Override
    @NonNull
    public Mono<ServerResponse> handle(ServerRequest request) {
        request.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
                .ifPresent(origin -> log.error("handle request[{}] failed, hystrix fallback, default response returned", origin));
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(DEFAULT_BODY));
    }
}
