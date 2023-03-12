package com.project.book.book.dto.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveBookFromListDto {

    public String isbn;
    public BookReviewDto review;

    @Builder
    public SaveBookFromListDto(String isbn, BookReviewDto review) {
        this.isbn = isbn;
        this.review = review;
    }
}
