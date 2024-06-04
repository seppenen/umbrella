package org.spring.authservice.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("AccessToken")
@AllArgsConstructor
public class AccessTokenEntity implements Serializable {
    @Id
    private Long id;
    private String token;
}
