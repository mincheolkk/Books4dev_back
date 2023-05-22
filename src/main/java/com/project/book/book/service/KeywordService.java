package com.project.book.book.service;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KeywordService {
    // 연관 검색어 서비스

    private final RedisTemplate<Long, String> keywordTemplate;

    public KeywordService(@Qualifier("keywordRedisTemplateBean")  RedisTemplate<Long, String> keywordTemplate) {
        this.keywordTemplate = keywordTemplate;
    }

    public List<KeywordScoreResponseDto> getRelatedKeyword(final Long id) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = keywordTemplate.opsForZSet().reverseRangeWithScores(id, 0, 2);
        return typedTuples.stream().map(KeywordScoreResponseDto::convertFromRedisRankingData).collect(Collectors.toList());
    }

    public void incrementKeywordScore(final Long bookId, final String keyword) {
        keywordTemplate.opsForZSet().incrementScore(bookId, keyword, 1);
    }
}