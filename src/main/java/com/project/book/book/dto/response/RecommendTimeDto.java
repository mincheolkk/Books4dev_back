package com.project.book.book.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendTimeDto {

    private Integer beforeCount;
    private Integer afterCount;
    private Integer twoYearCount;
    private Integer fiveYearCount;
    private Integer tenYearCount;

    @QueryProjection
    public RecommendTimeDto(Integer beforeCount, Integer afterCount, Integer twoYearCount, Integer fiveYearCount, Integer tenYearCount) {
        this.beforeCount = beforeCount;
        this.afterCount = afterCount;
        this.twoYearCount = twoYearCount;
        this.fiveYearCount = fiveYearCount;
        this.tenYearCount = tenYearCount;
    }
}
