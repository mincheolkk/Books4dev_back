package com.project.book.book.dto.request;

import com.project.book.book.domain.WishBook;
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

    public WishBook toEntity() {
        return new WishBook(isbn, title, thumbnail);
    }
}
