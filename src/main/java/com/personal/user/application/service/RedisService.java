package com.personal.user.application.service;

import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.core.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<Long, Token> redisTemplate;

    public void saveData(Long key, Token value) {
        redisTemplate.opsForValue().set(key, value, Duration.ofDays(7));
    }

    public Token getData(Long key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElseThrow(() -> new TokenException(TokenErrorCode.TOKEN_NOT_FOUND));
    }
}
