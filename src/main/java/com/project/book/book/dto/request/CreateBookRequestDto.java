package com.project.book.book.dto.request;

import com.project.book.book.domain.Book;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBookRequestDto {

    private String title;
    private String authors;
    private String translators;
    private String isbn;
    private LocalDateTime releaseDate;
    private String publisher;
    private Long price;
    private String thumbnail;
    private String contents;

    @Builder
    public CreateBookRequestDto(String title, String authors, String translators, String isbn,
                                LocalDateTime datetime, String publisher, Long price, String thumbnail, String contents) {
        this.title = title;
        this.authors = authors;
        this.translators = translators;
        this.isbn = isbn;
        this.releaseDate = datetime;
        this.publisher = publisher;
        this.price = price;
        this.thumbnail = thumbnail;
        this.contents = contents;
    }

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .authors(authors)
                .translators(translators)
                .isbn(isbn)
                .releaseDate(releaseDate)
                .publisher(publisher)
                .price(price)
                .thumbnail(thumbnail)
                .contents(contents)
                .build();
    }

}
