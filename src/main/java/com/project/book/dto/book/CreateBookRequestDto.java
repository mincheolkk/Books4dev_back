package com.project.book.dto.book;

import com.project.book.domain.Book;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBookRequestDto {

    private String title;
    private String authors;
    private String translator;
    private String isbn;
    private LocalDateTime datetime;
    private String publisher;
    private int price;
    private String thumbnail;

    @Builder
    public CreateBookRequestDto(String title, String authors, String translator, String isbn,
                                LocalDateTime datetime, String publisher, int price, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.translator = translator;
        this.isbn = isbn;
        this.datetime = datetime;
        this.publisher = publisher;
        this.price = price;
        this.thumbnail = thumbnail;
    }

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
