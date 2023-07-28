package com.project.book.book.dto.request;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

import static com.project.book.common.utils.ServiceUtils.listToString;

@Getter
@NoArgsConstructor
public class BookInfoDto {

    private String title;
    private List<String> authors;
    private List<String> translators;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private Long price;
    private String thumbnail;
    private String contents;

    @Builder
    public BookInfoDto(String title, List<String> authors, List<String> translators, String isbn, ZonedDateTime datetime, String publisher, Long price, String thumbnail, String contents) {
        this.title = title;
        this.authors = authors;
        this.translators = translators;
        this.isbn = isbn;
        this.datetime = datetime;
        this.publisher = publisher;
        this.price = price;
        this.thumbnail = thumbnail;
        this.contents = contents;
    }

    public Book toBook() {
        return Book.builder()
                .bookInfo(BookInfo.builder()
                        .authors(listToString(this.authors))
                        .translators(listToString(this.translators))
                        .title(this.title)
                        .publisher(this.publisher)
                        .price(this.price)
                        .thumbnail(this.thumbnail)
                        .releaseDate(this.datetime != null ? this.datetime.toLocalDateTime() : null)
                        .isbn(this.isbn)
                        .contents(this.contents)
                        .build())
                .build();
    }
}
