package com.project.book.book.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadBookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private String thumbnail;
    private double star;

    @Builder
    @QueryProjection
    public ReadBookResponseDto(Long id, String title, String isbn, String thumbnail, double star) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.star = star;
    }
}
