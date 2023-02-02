package com.project.book.book.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

@NoArgsConstructor
@Getter
public class KeywordScoreResponseDto {

    private String keyword;
    private int score;

    @Builder
    public KeywordScoreResponseDto(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public static KeywordScoreResponseDto convertFromRedisRankingData(TypedTuple typedTuple) {
        return KeywordScoreResponseDto.builder()
                .keyword(typedTuple.getValue().toString())
                .score(typedTuple.getScore().intValue())
                .build();
    }
}
