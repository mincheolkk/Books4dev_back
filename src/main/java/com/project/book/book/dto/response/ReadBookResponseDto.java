package com.project.book.book.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadBookResponseDto {

    private String title;
    private String isbn;
    private String thumbnail;
    private double star;

    @QueryProjection
    public ReadBookResponseDto(String title, String isbn, String thumbnail, double star) {
        this.title = title;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.star = star;
    }
}
