package com.project.book.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WishBookRequestDto {

    private String isbn;
    private String title;
    private String thumbnail;
}
