package com.project.book.book.dto.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterByHomeListDto {

    public String isbn;
    public BookReviewDto review;

    @Builder
    public RegisterByHomeListDto(String isbn, BookReviewDto review) {
        this.isbn = isbn;
        this.review = review;
    }
}
