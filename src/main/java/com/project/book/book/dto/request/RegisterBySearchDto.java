package com.project.book.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterBySearchDto {

    private BookDataDto item;
    private BookReviewDto review;

}