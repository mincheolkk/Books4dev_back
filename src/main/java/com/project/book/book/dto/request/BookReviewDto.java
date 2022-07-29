package com.project.book.book.dto.request;

import com.project.book.book.domain.BookTime;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class BookReviewDto {

    private BookTime readTime;
    private BookTime recommendTime;
    private Integer star;
}
