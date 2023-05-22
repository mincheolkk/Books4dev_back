package com.project.book.member.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {

    private final StringRedisTemplate loginTemplate;

    public TokenService(@Qualifier("redisTemplateBean") StringRedisTemplate loginTemplate) {
        this.loginTemplate = loginTemplate;
    }

    public void setRefreshToken(final String key, final String value, final long duration) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteRefreshToken(final String key) {
        loginTemplate.delete(key);
    }

    public String getRefreshToken(final String key) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setBlackList(final String key, final String value, final long duration) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public String getBlackList(final String key) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
