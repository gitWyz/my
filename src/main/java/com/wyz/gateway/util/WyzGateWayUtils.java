package com.wyz.gateway.util;

import com.wyz.gateway.constant.ConstantPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;
import java.util.Optional;

/**
 * javadoc 本网关专用utils
 *
 * @author wyz
 * @date 2020-11-04 11:27
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WyzGateWayUtils {
    public static boolean inner(String url){
        return Optional.ofNullable(url)
                .filter(StringUtils::isNoneBlank)
                .map(u -> StringUtils.containsIgnoreCase(u, "/inner/"))
                .orElse(Boolean.FALSE);
    }

    public static boolean outer(String url){
        return Optional.ofNullable(url)
                .filter(StringUtils::isNoneBlank)
                .map(u -> StringUtils.containsIgnoreCase(u, "/outer/"))
                .orElse(Boolean.FALSE);
    }

    public static boolean user(String url){
        return Optional.ofNullable(url)
                .filter(StringUtils::isNoneBlank)
                .map(u -> StringUtils.startsWith(u, "/api/wyz/user"))
                .orElse(Boolean.FALSE);
    }

    /**
     * javadoc college
     * @apiNote 学院服务特殊处理, 需要支持cookie校验权限
     *
     * @param url 请求url
     * @return boolean
     * @author wyz
     * @date 2020-11-04 15:02
     * @modified none
     */
    public static boolean college(String url){
        return Optional.ofNullable(url)
                .filter(StringUtils::isNoneBlank)
                .map(u -> StringUtils.startsWith(u, "/api/wyz/college"))
                .orElse(Boolean.FALSE);
    }

    /**
     * javadoc findTokenFromHeader
     * @apiNote 从header中获取token
     *
     * @param exchange http
     * @return java.lang.String
     * @author wyz
     * @date 2020-11-04 11:30
     * @modified none
     */
    public static String findTokenFromHeader(ServerWebExchange exchange){
        return exchange.getRequest().getHeaders().getFirst(ConstantPool.TOKEN);
    }

    /**
     * javadoc findTokenFromHeader
     * @apiNote 从cookie中获取token
     *
     * @param exchange http
     * @return java.lang.String
     * @author wyz
     * @date 2020-11-04 11:30
     * @modified none
     */
    public static String findTokenFromCookie(ServerWebExchange exchange){
        final HttpCookie cookie = exchange.getRequest().getCookies().getFirst(ConstantPool.SHADOW_TOKEN);
        if (Objects.nonNull(cookie) && StringUtils.isNoneBlank(cookie.getValue())){
            return cookie.getValue();
        }
        return null;
    }

    /**
     * javadoc findTokenFromHeader
     * @apiNote 优先从cookie中获取token, 如果获取不到就从header中获取
     *
     * @param exchange http
     * @return java.lang.String
     * @author wyz
     * @date 2020-11-04 11:30
     * @modified none
     */
    public static String findTokenFromCookieFirst(ServerWebExchange exchange){
        final String token = findTokenFromCookie(exchange);
        if(StringUtils.isNoneBlank(token)){
            return token;
        }

        return findTokenFromHeader(exchange);
    }
}
