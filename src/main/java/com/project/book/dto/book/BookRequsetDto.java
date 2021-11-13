package com.project.book.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookRequsetDto {

    private String title;
    private List<String> authors;
    private List<String> translator;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private Long price;
    private String thumbnail;
    private Integer readTime;
    private Integer recommendTime;
    private Integer star;

    public void create(
            String title,
    ) {

    }

    public void addBookData() {

    }
}
