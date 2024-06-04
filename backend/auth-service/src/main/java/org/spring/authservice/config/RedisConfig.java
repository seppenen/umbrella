package org.spring.authservice.config;

import org.spring.authservice.entity.AccessTokenEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfig {
    /**
     * Creates and returns the ReactiveHashOperations object for performing operations on Redis hashes.
     *
     * @param redisConnectionFactory the ReactiveRedisConnectionFactory used to create the ReactiveRedisTemplate
     * @return the ReactiveHashOperations object
     */
    @Bean
    public ReactiveHashOperations<String, Long, AccessTokenEntity> hashOperations(ReactiveRedisConnectionFactory redisConnectionFactory) {
        var template = new ReactiveRedisTemplate<>(
                redisConnectionFactory,
                RedisSerializationContext.<String, AccessTokenEntity>newSerializationContext(new StringRedisSerializer())
                        .hashKey(new GenericToStringSerializer<>(Long.class))
                        .hashValue(new Jackson2JsonRedisSerializer<>(AccessTokenEntity.class))
                        .build());
        return template.opsForHash();
    }
}
