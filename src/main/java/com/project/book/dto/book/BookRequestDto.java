package com.project.book.dto.book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookRequestDto {

    private String title;
    private List<String> authors;
    private List<String> translator;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private int price;
    private String thumbnail;
    private Integer readTime;
    private Integer recommendTime;
    private Integer star;

    @Builder
    public BookRequestDto(String title, List<String> authors, List<String> translator, String isbn, ZonedDateTime datetime, String publisher, int price, String thumbnail, Integer readTime, Integer recommendTime, Integer star) {
        this.title = title;
        this.authors = authors;
        this.translator = translator;
        this.isbn = isbn;
        this.datetime = datetime;
        this.publisher = publisher;
        this.price = price;
        this.thumbnail = thumbnail;
        this.readTime = readTime;
        this.recommendTime = recommendTime;
        this.star = star;
    }
}