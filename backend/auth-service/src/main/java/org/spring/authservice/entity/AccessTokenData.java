package org.spring.authservice.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("access_token")
public class AccessTokenData implements Serializable {
    private Long id;
    private String token;
}
