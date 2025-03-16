package com.personal.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public void addToBlacklist(String accessToken, Long userId, long expirationInSec) {
        redisTemplate.opsForValue()
                .set("blacklist:" + accessToken, "userId:" + userId.toString(), expirationInSec, TimeUnit.SECONDS);

        log.info("Blacklist access token added: {}", accessToken);
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }
}