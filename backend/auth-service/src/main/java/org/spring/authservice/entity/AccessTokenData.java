package org.spring.authservice.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("AccessTokenData")
public class AccessTokenData implements Serializable {
    @Id
    private Long id;
    private String token;
}
