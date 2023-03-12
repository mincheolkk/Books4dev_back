package com.project.book.book.dto.request;

import com.project.book.book.domain.BookTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class BookReviewDto {

    private BookTime readTime;
    private BookTime recommendTime;
    private Integer star;
    private String searchKeyword;

    @Builder
    public BookReviewDto(BookTime readTime, BookTime recommendTime, Integer star, String searchKeyword) {
        this.readTime = readTime;
        this.recommendTime = recommendTime;
        this.star = star;
        this.searchKeyword = searchKeyword;
    }
}
