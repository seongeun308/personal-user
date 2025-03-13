package com.personal.user.application.common.config;

import com.personal.user.application.model.RefreshToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Long, RefreshToken> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new RedisSerializer<Long>() {
            @Override
            public byte[] serialize(Long value) throws SerializationException {
                return new byte[0];
            }

            @Override
            public Long deserialize(byte[] bytes) throws SerializationException {
                return 0L;
            }
        });

        Jackson2JsonRedisSerializer<RefreshToken> valueSerializer = new Jackson2JsonRedisSerializer<>(RefreshToken.class);
        redisTemplate.setValueSerializer(valueSerializer);

        return redisTemplate;
    }
}