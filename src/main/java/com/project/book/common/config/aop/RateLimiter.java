package com.project.book.common.config.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimiter {

    private final RedisTemplate<String, Integer> rateLimiterTemplate;

    @Autowired
    public RateLimiter(
                @Qualifier("rateLimiterRedisTemplateBean") RedisTemplate<String, Integer> rateLimiterTemplate
    ) {
        this.rateLimiterTemplate = rateLimiterTemplate;
    }

    public boolean isOneMethodAllowed(String identifier, String actionKey, int second, int limit) {
        String key = identifier + actionKey;
        Long count = rateLimiterTemplate.opsForValue().increment(key, 1);
        rateLimiterTemplate.expire(key, second, TimeUnit.SECONDS);
        return count <= limit;
    }

    public boolean isAllMethodAllowed(String identifier, int second, int limit) {
        String key = identifier;
        Long count = rateLimiterTemplate.opsForValue().increment(key, 1);
        rateLimiterTemplate.expire(key, second, TimeUnit.SECONDS);
        return count <= limit;
    }
}
