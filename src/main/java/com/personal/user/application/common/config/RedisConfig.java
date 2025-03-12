package com.personal.user.application.common.config;

import com.personal.user.core.domain.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Long, Token> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, Token> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(new RedisSerializer<Long>() {
            @Override
            public byte[] serialize(Long aLong) throws SerializationException {
                return String.valueOf(aLong).getBytes();
            }

            @Override
            public Long deserialize(byte[] bytes) throws SerializationException {
                return Long.valueOf(new String(bytes));
            }
        });

        Jackson2JsonRedisSerializer<Token> valueSerializer = new Jackson2JsonRedisSerializer<>(Token.class);
        redisTemplate.setValueSerializer(valueSerializer);

        return redisTemplate;
    }
}