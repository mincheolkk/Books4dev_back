package com.project.book.book.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookInfo {

    @Column(name = "book_isbn", unique = true)
    private String isbn;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_publisher")
    private String publisher;

    @Column(name = "book_dateTime")
    private LocalDateTime releaseDate;

    @Column(name = "book_price" )
    private Long price;

    @Column(name = "book_thumbnail")
    private String thumbnail;

    @Column(name = "book_authors")
    private String authors;

    @Column(name = "book_translator")
    private String translators;

    @Column(name = "book_contents", columnDefinition = "TEXT")
    private String contents;

    @Builder
    public BookInfo(String isbn, String title, String publisher, LocalDateTime releaseDate, Long price, String thumbnail, String authors, String translators, String contents) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.price = price;
        this.thumbnail = thumbnail;
        this.authors = authors;
        this.translators = translators;
        this.contents = contents;
    }
}
