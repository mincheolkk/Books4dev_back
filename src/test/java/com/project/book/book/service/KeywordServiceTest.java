package com.project.book.book.service;

import com.project.book.book.dto.response.KeywordScoreResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeywordServiceTest {

    @Mock
    private RedisTemplate<Long, String> keywordTemplate;

    @InjectMocks
    private KeywordService keywordService;

    @DisplayName("연관 검색어를 리스트로 최대 3개를 조회한다.")
    @Test
    void getRelatedKeyword() {
        // given
        Long bookId = 1L;
        String keyword1 = "java";
        String keyword2 = "tdd";
        String keyword3 = "spring";
        long start = 0L;
        long end = 2L;

        Set<ZSetOperations.TypedTuple<String>> typedTuples = new TreeSet<>(
                (t1, t2) -> Double.compare(t2.getScore(), t1.getScore())
        );
        typedTuples.add(new DefaultTypedTuple<>(keyword1, 10.0));
        typedTuples.add(new DefaultTypedTuple<>(keyword2, 5.0));
        typedTuples.add(new DefaultTypedTuple<>(keyword3, 3.0));

        ZSetOperations<Long, String> zSetOperationsMock = mock(ZSetOperations.class);
        given(keywordTemplate.opsForZSet()).willReturn(zSetOperationsMock);
        given(keywordTemplate.opsForZSet().reverseRangeWithScores(eq(bookId), eq(start), eq(end)))
                .willReturn(typedTuples);

        // when
        List<KeywordScoreResponseDto> results = keywordService.getRelatedKeyword(bookId);

        // then
        assertAll(
                () -> {
                    assertThat(results.size()).isEqualTo(typedTuples.size());
                    assertThat(results.get(0).getKeyword()).isEqualTo(keyword1);
                    assertThat(results.get(0).getScore()).isEqualTo(10);
                    assertThat(results.get(1).getKeyword()).isEqualTo(keyword2);
                    assertThat(results.get(1).getScore()).isEqualTo(5);
                    assertThat(results.get(2).getKeyword()).isEqualTo(keyword3);
                    assertThat(results.get(2).getScore()).isEqualTo(3);
                }
        );
    }

    @DisplayName("키워드와 책Id를 파라미터로 연관 검색어의 스코어를 1씩 증가시킨다.")
    @Test
    void incrementKeywordScore() {
        // given
        Long bookId = 1L;
        String keyword = "java";

        ZSetOperations<Long, String> zSetOperationsMock = mock(ZSetOperations.class);
        given(keywordTemplate.opsForZSet()).willReturn(zSetOperationsMock);

        // when
        keywordService.incrementKeywordScore(bookId, keyword);

        // then
        verify(zSetOperationsMock, times(1)).incrementScore(bookId, keyword, 1);;
    }
}