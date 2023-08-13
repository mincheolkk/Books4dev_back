package com.project.book.book.service;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Service
public class RankingService {
    // 인기 검색어 서비스

    private static final String RANKING = "ranking";
    private static final int KEYWORD_SIZE = 100;
    private final List<String> searchKeywords = new ArrayList<>();

    private final StringRedisTemplate rankingTemplate;

    public RankingService(
            @Qualifier("rankingRedisTemplateBean") StringRedisTemplate rankingTemplate
    ) {
        this.rankingTemplate = rankingTemplate;
    }

    public void addSearchKeyword(String keyword) {
        searchKeywords.add(keyword);
        if (searchKeywords.size() >= KEYWORD_SIZE) {
            saveSearchKeywordToRedis();
        }
    }

    public void saveSearchKeywordToRedis() {
        searchKeywords.forEach(
                keyword -> incrementRankingScore(keyword)
        );
        searchKeywords.clear();
    }

    public List<KeywordScoreResponseDto> getPopularKeyword() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = rankingTemplate.opsForZSet().reverseRangeWithScores(RANKING, 0, 2);
        return typedTuples.stream().map(KeywordScoreResponseDto::convertFromRedisRankingData).collect(Collectors.toList());
    }

    private void incrementRankingScore(final String keyword) {
        rankingTemplate.opsForZSet().incrementScore(RANKING, keyword, 1);
    }
}

