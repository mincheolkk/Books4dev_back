package com.project.book.book.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookDataDto {

    private String title;
    private List<String> authors;
    private List<String> translators;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private Long price;
    private String thumbnail;

    @Builder
    public BookDataDto(String title, List<String> authors, List<String> translators, String isbn, ZonedDateTime datetime, String publisher, Long price, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.translators = translators;
        this.isbn = isbn;
        this.datetime = datetime;
        this.publisher = publisher;
        this.price = price;
        this.thumbnail = thumbnail;
    }
}
