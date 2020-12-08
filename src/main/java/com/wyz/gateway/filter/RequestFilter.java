package com.wyz.gateway.filter;

import com.wyz.gateway.common.CustomizedServerHttpRequestDecorator;
import com.wyz.gateway.constant.ConstantPool;
import com.wyz.gateway.service.IAuthService;
import com.wyz.gateway.util.WyzGateWayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 请求处理权限过滤器
 *
 * @author wyz
 * @date 2020/11/4 14:30
 * @version 1.0.0
 */
@Component
@Slf4j
public class RequestFilter implements GlobalFilter, Ordered {

    private IAuthService authService;
    @Autowired
    public void setAuthService(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        final String url = exchange.getRequest().getURI().getPath();

        if (WyzGateWayUtils.user(url)){
            return chain.filter(exchange);
        }

        // 如果是不需要校验权限的接口, 则直接放过
        if (WyzGateWayUtils.outer(url)) {
            if (reset(exchange)){
                return chain.filter(exchange.mutate().request(rebuildRequest(exchange)).build());
            }else {
                return chain.filter(exchange);
            }
        }

        // 内部接口, 禁止从外部访问
        if (WyzGateWayUtils.inner(url)){
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return exchange.getResponse()
                    .writeWith(
                            Mono.just(
                                    exchange.getResponse().bufferFactory().wrap(ConstantPool.ILLEGAL_REQUEST_RESPONSE_BODY)
                            )
                    );
        }

        final String token = WyzGateWayUtils.college(url) ?
                WyzGateWayUtils.findTokenFromCookieFirst(exchange)
                :
                WyzGateWayUtils.findTokenFromHeader(exchange);

        // 正常的接口处理
        //final String token = this.findToken(url, exchange);
        //exchange.getRequest().getHeaders().getFirst(ConstantPool.TOKEN);
        // 清除一下两个key, 防止接口中的id被外部串改, 等取到platform id后再set进去

        final String platformId = authService.findUserIdByToken(token);
        if (StringUtils.isNoneBlank(platformId)){
            // 清掉X-Auth-Token 和 Platform-Id
            // 认证通过了, 必须得重构request
            return chain.filter(exchange.mutate().request(this.rebuildRequest(exchange, platformId)).build());
        }

        // 认证没通过
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return exchange.getResponse()
                .writeWith(
                        Mono.just(
                                exchange.getResponse().bufferFactory().wrap(
                                        url.startsWith("/api/ws/college/app/live/play") ?
                                                ConstantPool.ILLEGAL_TOKEN_RESPONSE_BODY_APP
                                                :
                                                ConstantPool.ILLEGAL_TOKEN_RESPONSE_BODY
                                )
                        )
                );
    }

    private boolean reset(ServerWebExchange exchange){
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(r ->
                        StringUtils.isNoneBlank(r.getHeaders().getFirst(ConstantPool.TOKEN)) ||
                                StringUtils.isNoneBlank(r.getHeaders().getFirst(ConstantPool.ID))
                )
                .orElse(Boolean.FALSE);
    }

    private ServerHttpRequestDecorator rebuildRequest(ServerWebExchange exchange){
        HttpHeaders headers = rebuildHeaders(exchange);
        return new CustomizedServerHttpRequestDecorator(exchange.getRequest(), headers);
    }

    private ServerHttpRequestDecorator rebuildRequest(ServerWebExchange exchange, String platformId){
        HttpHeaders headers = rebuildHeaders(exchange);
        headers.add(ConstantPool.ID, platformId);
        return new CustomizedServerHttpRequestDecorator(exchange.getRequest(), headers);
    }
    private HttpHeaders rebuildHeaders(ServerWebExchange exchange){
        final HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(ConstantPool.ID);
        headers.remove(ConstantPool.TOKEN);
        return headers;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
