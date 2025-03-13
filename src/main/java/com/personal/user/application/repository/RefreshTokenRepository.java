package com.personal.user.application.repository;

import com.personal.user.application.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<Long, RefreshToken> redisTemplate;

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getUserId(), refreshToken, Duration.ofDays(7));
        log.info("Saved refresh token: {}", refreshToken.getToken());
    }

    public Optional<RefreshToken> findById(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(refreshToken));
    }

    public void deleteById(Long userId) {
        redisTemplate.delete(userId);
        log.info("Deleted refresh token By userId : {}", userId);
    }

    public boolean existsById(Long userId) {
        return redisTemplate.hasKey(userId);
    }
}
