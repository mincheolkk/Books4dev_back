package com.project.book.book.dto.response;

import com.project.book.book.domain.BookTimeCount;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllBookResponseDto {

    private Long id;
    private String title;
    private String authors;
    private String thumbnail;
    private String isbn;

    private double avgStar;
    private Integer readCount;
    private Integer wishCount;
    private BookTimeCount recommendTime;

    @QueryProjection
    public AllBookResponseDto(Long id, String title, String authors, String thumbnail, String isbn, double avgStar, Integer readCount, Integer wishCount, BookTimeCount recommendTime) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.isbn = isbn;
        this.avgStar = avgStar;
        this.readCount = readCount;
        this.wishCount = wishCount;
        this.recommendTime = recommendTime;
    }
}
