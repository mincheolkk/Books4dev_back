package com.project.book.book.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class SaveBookFromSearchDto {

    private BookInfoDto info;
    private BookReviewDto review;

    @Builder
    public SaveBookFromSearchDto(BookInfoDto info, BookReviewDto review) {
        this.info = info;
        this.review = review;
    }
}