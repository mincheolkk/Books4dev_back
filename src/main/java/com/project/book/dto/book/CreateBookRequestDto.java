package com.project.book.dto.book;

import com.project.book.domain.Book;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBookRequestDto {

    private String title;
    private String authors;
    private String translator;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private Long price;
    private String thumbnail;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .authors(authors)
                .translator(translator)
                .isbn(isbn)
                .dateTime(datetime)
                .publisher(publisher)
                .price(price)
                .thumbnail(thumbnail)
                .build();
    }

}
