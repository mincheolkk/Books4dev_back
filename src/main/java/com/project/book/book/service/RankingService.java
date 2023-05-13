package com.project.book.book.service;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RankingService {
    private static final String RANKING = "ranking";
    private static final int KEYWORD_SIZE = 100;
    private final List<String> searchKeywords = new ArrayList<>();

    private final StringRedisTemplate rankingTemplate;

    public RankingService(
            @Qualifier("rankingRedisTemplateBean") StringRedisTemplate rankingTemplate
    ) {
        this.rankingTemplate = rankingTemplate;
    }


    private void incrementRankingScore(final String keyword) {
        rankingTemplate.opsForZSet().incrementScore(RANKING, keyword, 1);
    }
//
//    private void deleteKeywordFromRankingRange() {
//        rankingTemplate.opsForZSet().removeRange(RANKING, 0, -101);
//    }

    @Scheduled(cron = "0 0 3,20 * * *")
    public void scheduleSearchKeywordToRedis() {
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
//        deleteKeywordFromRankingRange();
    }

    public List<KeywordScoreResponseDto> getPopularKeyword() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = rankingTemplate.opsForZSet().reverseRangeWithScores(RANKING, 0, 2);
        return typedTuples.stream().map(KeywordScoreResponseDto::convertFromRedisRankingData).collect(Collectors.toList());
    }
}

