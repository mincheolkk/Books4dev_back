package com.project.book.book.dto.response;

import com.project.book.book.domain.RecommendTime;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AllBookResponseDto {

    private String title;
    private String authors;
    private String publisher;
    private String thumbnail;
    private String isbn;
    private Long price;

    private double avgStar;
    private Integer registerCount;
    private RecommendTime recommendTimeDto;

    @QueryProjection
    public AllBookResponseDto(String title, String authors, String publisher, String thumbnail, String isbn, Long price, double avgStar, Integer registerCount, RecommendTime recommendTimeDto) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.isbn = isbn;
        this.price = price;
        this.avgStar = avgStar;
        this.registerCount = registerCount;
        this.recommendTimeDto = recommendTimeDto;
    }
}
