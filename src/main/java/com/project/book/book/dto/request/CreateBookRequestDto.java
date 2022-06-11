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
    private String translator;
    private String isbn;
    private LocalDateTime releaseDate;
    private String publisher;
    private Long price;
    private String thumbnail;

    @Builder
    public CreateBookRequestDto(String title, String authors, String translator, String isbn,
                                LocalDateTime datetime, String publisher, Long price, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.translator = translator;
        this.isbn = isbn;
        this.releaseDate = datetime;
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
                .releaseDate(releaseDate)
                .publisher(publisher)
                .price(price)
                .thumbnail(thumbnail)
                .build();
    }

}
