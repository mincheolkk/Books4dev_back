package com.project.book.common.utils;

import com.project.book.book.dto.response.RankingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RedisUtil {

    private final StringRedisTemplate loginTemplate;
    private final StringRedisTemplate rankingTemplate;
    private final RedisTemplate<Long, String> keywordTemplate;

    @Autowired
    public RedisUtil(
                     @Qualifier("redisTemplateBean") StringRedisTemplate loginTemplate,
                     @Qualifier("rankingRedisTemplateBean") StringRedisTemplate rankingTemplate,
                     @Qualifier("keywordRedisTemplateBean") RedisTemplate<Long, String> keywordTemplate
    ) {
        this.loginTemplate = loginTemplate;
        this.rankingTemplate = rankingTemplate;
        this.keywordTemplate = keywordTemplate;
    }


    public void setRefreshToken(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteRefreshTokenData(String key) {
        loginTemplate.delete(key);
    }

    public String getRefreshTokenData(String key) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setBlackList(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public String getBlackListData(String key) {
        ValueOperations<String, String> valueOperations = loginTemplate.opsForValue();
        return valueOperations.get(key);
    }


}
