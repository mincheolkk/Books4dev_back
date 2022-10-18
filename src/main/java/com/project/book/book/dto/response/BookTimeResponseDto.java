package com.project.book.book.dto.response;

import com.project.book.book.domain.BookTime;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class BookTimeResponseDto {

    private BookTime bookTime;
    private Long counting;

    @QueryProjection
    public BookTimeResponseDto(BookTime bookTime, Long counting) {
        this.bookTime = bookTime;
        this.counting = counting;
    }
}
