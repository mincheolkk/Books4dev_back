package com.project.book.common.utils;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RedisUtil {

    private final StringRedisTemplate loginTemplate;
    private final StringRedisTemplate rankingTemplate;
    private final RedisTemplate<Long, String> keywordTemplate;

    public RedisUtil(
            @Qualifier("redisTemplateBean") StringRedisTemplate loginTemplate,
            @Qualifier("rankingRedisTemplateBean") StringRedisTemplate rankingTemplate,
            @Qualifier("keywordRedisTemplateBean") RedisTemplate<Long, String> keywordTemplate
    ) {
        this.loginTemplate = loginTemplate;
        this.rankingTemplate = rankingTemplate;
        this.keywordTemplate = keywordTemplate;
    }

    private static final String RANKING = "ranking";
    private static final int KEYWORD_SIZE = 100;
    private final List<String> searchKeywords = new ArrayList<>();

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

    private void incrementRankingScore(final String keyword) {
        rankingTemplate.opsForZSet().incrementScore(RANKING, keyword, 1);
    }

    public List<KeywordScoreResponseDto> getPopularKeyword() {
        Set<TypedTuple<String>> typedTuples = rankingTemplate.opsForZSet().reverseRangeWithScores(RANKING, 0, 2);
        return typedTuples.stream().map(KeywordScoreResponseDto::convertFromRedisRankingData).collect(Collectors.toList());
    }

    public List<KeywordScoreResponseDto> getRelatedKeyword(final Long id) {
        Set<TypedTuple<String>> typedTuples = keywordTemplate.opsForZSet().reverseRangeWithScores(id, 0, 2);
        return typedTuples.stream().map(KeywordScoreResponseDto::convertFromRedisRankingData).collect(Collectors.toList());
    }

    public void incrementKeywordScore(final Long bookId, final String keyword) {
        keywordTemplate.opsForZSet().incrementScore(bookId, keyword, 1);
    }

    private void deleteKeywordFromRankingRange() {
        rankingTemplate.opsForZSet().removeRange(RANKING, 0, -101);
    }

    @Scheduled(cron = "0 0 3,15 * * *")
    private void scheduleSearchKeywordToRedis() {
        searchKeywordToRedis();
    }

    public void getSearchKeywords(String keyword) {
        searchKeywords.add(keyword);

        if (searchKeywords.size() >= KEYWORD_SIZE) {
            searchKeywordToRedis();
        }
    }

    private void searchKeywordToRedis() {
        searchKeywords.forEach(
                keyword -> incrementRankingScore(keyword)
        );
        searchKeywords.clear();
        deleteKeywordFromRankingRange();
    }
}
