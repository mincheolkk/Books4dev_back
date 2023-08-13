package com.project.book.book.service;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    private static final String RANKING = "ranking";

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RankingService rankingService;

    @DisplayName("인기 검색어를 100개 단위로 서버에 모으고, 레디스에 저장시킨다.")
    @Test
    public void addSearchKeyword() {
        // given
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        for (int i = 0; i < 100; i++) {
            rankingService.addSearchKeyword("test Keyword" + i);
        }

        // then
        verify(zSetOperations, times(100)).incrementScore(anyString(), anyString(), anyDouble());
    }

    @DisplayName("인기 검색어가 100개가 되지 않으면, 아직 레디스에 보내지 않는다.")
    @Test
    public void keywordsLessThan100() {
        // given
        lenient().when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        // when
        for (int i = 0; i < 99; i++) {
            rankingService.addSearchKeyword("test Keyword" + i);
        }

        // then
        verify(zSetOperations, never()).incrementScore(anyString(), anyString(), anyDouble());
    }

    @DisplayName("인기 검색어가 100개~200개 사이라면, 앞선 100개만 레디스에 보내진 상태다.")
    @Test
    public void keywordsMoreThan100() {
        // given
        lenient().when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        // when
        for (int i = 0; i < 150; i++) {
            rankingService.addSearchKeyword("test Keyword" + i);
        }

        // then
        verify(zSetOperations, times(100)).incrementScore(anyString(), anyString(), anyDouble());
    }

    @DisplayName("인기 검색어가 200개가 될때, 앞선 100개 이후 모아진 100개를 레디스에 보낸다.")
    @Test
    public void addSearchKeyword2() {
        // given
        lenient().when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        // when
        for (int i = 0; i < 200; i++) {
            rankingService.addSearchKeyword("test Keyword" + i);
        }

        // then
        verify(zSetOperations, times(200)).incrementScore(anyString(), anyString(), anyDouble());
    }

    @DisplayName("인기 검색어를 조회하면, 검색어 검색된 횟수를 리스트로 반환한다.")
    @Test
    public void getPopularKeywordTest() {
        // given
        String keyword1 = "java";
        int score1 = 10;
        String keyword2 = "mysql";
        int score2 = 8;
        String keyword3 = "js";
        int score3 = 5;

        ZSetOperations.TypedTuple<String> tuple1 = new DefaultTypedTuple<>(keyword1, (double) score1);
        ZSetOperations.TypedTuple<String> tuple2 = new DefaultTypedTuple<>(keyword2, (double) score2);
        ZSetOperations.TypedTuple<String> tuple3 = new DefaultTypedTuple<>(keyword3, (double)score3);
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(tuple1);
        typedTuples.add(tuple2);
        typedTuples.add(tuple3);

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRangeWithScores(RANKING, 0, 2)).willReturn(typedTuples);

         // when
        List<KeywordScoreResponseDto> responses = rankingService.getPopularKeyword();

        // then
        assertThat(responses).hasSize(3)
                .extracting("keyword", "score")
                .containsExactlyInAnyOrder(
                        tuple(keyword1, score1),
                        tuple(keyword2, score2),
                        tuple(keyword3, score3)
                );
    }

    @DisplayName("인기 검색어를 레디스에 저장한다. 저장 후, 리스트는 clear 시킨다.")
    @Test
    public void saveSearchKeywordToRedis() {
        // given
        String keyword1 = "java";
        String keyword2 = "mysql";
        String keyword3 = "js";
        List<String> keywords = Arrays.asList(keyword1, keyword2, keyword3);

        keywords.forEach(rankingService::addSearchKeyword);
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        rankingService.saveSearchKeywordToRedis();

        // then
        keywords.forEach(keyword ->
            verify(zSetOperations, times(1)).incrementScore(RANKING, keyword, 1)
        );
        assertThat(rankingService.getSearchKeywords()).isEmpty();
    }
}