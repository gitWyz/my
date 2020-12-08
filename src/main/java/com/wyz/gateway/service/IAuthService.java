package com.wyz.gateway.service;

/**
 * javadoc token验证服务
 *
 * @author wyz
 * @date 2020/11/4 17:23
 * @version 1.0.0
 */
public interface IAuthService {

    /**
     * @apiNote 从token中获取Id
     *
     * @param token token
     * @return String
     * @author wyz
     * @date 2020/11/4 17:23
     */
    String findUserIdByToken(String token);
}
