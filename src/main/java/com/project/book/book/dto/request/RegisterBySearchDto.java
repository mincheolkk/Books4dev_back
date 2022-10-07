package com.project.book.book.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterBySearchDto {

    private BookDataDto item;
    private BookReviewDto review;

    @Builder
    public RegisterBySearchDto(BookDataDto item, BookReviewDto review) {
        this.item = item;
        this.review = review;
    }
}