package com.project.book.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WishBookRequestDto {

    private String isbn;
    private String title;
    private String thumbnail;

    public static WishBookRequestDto create(String isbn, String title, String thumbnail) {
        return new WishBookRequestDto(isbn, title, thumbnail);
    }
}
