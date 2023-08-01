package com.project.book.book.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WishBookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private String thumbnail;

    @Builder
    @QueryProjection
    public WishBookResponseDto(Long id, String title, String isbn, String thumbnail) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
    }
}
