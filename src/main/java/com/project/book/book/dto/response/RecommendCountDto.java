package com.project.book.book.dto.response;

import com.project.book.book.domain.BookTime;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendCountDto {

    private BookTime bookTime;
    private long count;

    @QueryProjection
    public RecommendCountDto(BookTime bookTime, long count) {
        this.bookTime = bookTime;
        this.count = count;
    }
}
