package com.wyz.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.wyz.gateway.service.IAuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * javadoc token验证服务实现
 *
 * @author wyz
 * @date 2020/3/22 11:35
 * @version 1.0.0
 */
@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Value(value = "${wyz.security.secret.key}")
    private String secretKey;

    @Override
    public String findUserIdByToken(String token) {
        final String id = this.findTokenPreloadByToken(token);
        if(Objects.isNull(id)){
            return null;
        }
        return id;
    }

    private String findTokenPreloadByToken(String token){
        if(StringUtils.isBlank(token)){
            return null;
        }

        try{
            final Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            final String subject = claims.getSubject();
            return JSON.parseObject(subject, String.class);
        }catch (Exception ex){
            log.error("find token-preload in token=[{}] exception: ", token, ex);
        }
        return null;
    }
}
