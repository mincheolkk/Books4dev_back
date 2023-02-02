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
    private String searchKeyWord;

    @Builder
    public BookReviewDto(BookTime readTime, BookTime recommendTime, Integer star, String searchKeyWord) {
        this.readTime = readTime;
        this.recommendTime = recommendTime;
        this.star = star;
        this.searchKeyWord = searchKeyWord;
    }
}
