package com.project.book.book.dto.response;

import com.project.book.book.domain.RecommendTime;
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
    private Integer registerCount;
    private Integer wishCount;
    private RecommendTime recommendTimeDto;

    @QueryProjection
    public AllBookResponseDto(Long id, String title, String authors, String thumbnail, String isbn, double avgStar, Integer registerCount, Integer wishCount, RecommendTime recommendTimeDto) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.thumbnail = thumbnail;
        this.isbn = isbn;
        this.avgStar = avgStar;
        this.registerCount = registerCount;
        this.wishCount = wishCount;
        this.recommendTimeDto = recommendTimeDto;
    }
}
