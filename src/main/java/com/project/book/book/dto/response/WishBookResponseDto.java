package com.project.book.book.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WishBookResponseDto {

    private String title;
    private String isbn;
    private String thumbnail;

    @QueryProjection
    public WishBookResponseDto(String title, String isbn, String thumbnail) {
        this.title = title;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
    }
}
