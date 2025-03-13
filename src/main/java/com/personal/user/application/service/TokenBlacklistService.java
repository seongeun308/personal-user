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

    public void addToBlacklist(String accessToken, long expirationInSec) {
        redisTemplate.opsForValue()
                .set("blacklist:" + accessToken, "", expirationInSec, TimeUnit.SECONDS);

        log.info("Blacklist access token added: {}", accessToken);
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }
}