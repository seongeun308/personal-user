package com.personal.user.application.repository;

import com.personal.user.application.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getToken(), refreshToken, Duration.ofDays(7));
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(refreshToken));
    }

    public void deleteByToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
