package com.wyz.gateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * 常量池
 *
 * @author wyz
 * @version 1.0
 * @date 2020-11-04 14:32
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantPool {
    public static final byte[] ILLEGAL_REQUEST_RESPONSE_BODY =
            "{\"code\":1001,\"message\":\"access denied for inner api\",\"data\":null}"
                    .getBytes(StandardCharsets.UTF_8);

    public static final byte[] ILLEGAL_TOKEN_RESPONSE_BODY_APP =
            "{\"code\":2000,\"message\":\"access denied for illegal user token\",\"result\":{\"code\":4002,\"message\":\"请登录\"},\"data\":null}"
                    .getBytes(StandardCharsets.UTF_8);

    public static final byte[] ILLEGAL_TOKEN_RESPONSE_BODY =
            "{\"code\":2000,\"message\":\"access denied for illegal user token\",\"result\":{\"code\":2000,\"message\":\"access denied for illegal user token\"},\"data\":null}"
                    .getBytes(StandardCharsets.UTF_8);

    public static final String TOKEN = "X-Auth-Token";

    public static final String SHADOW_TOKEN = "SHADOW_TOKEN";

    public static final String ID = "ID";
}
