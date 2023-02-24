package com.project.book.book.dto.request;

import com.project.book.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

import static com.project.book.common.utils.ServiceUtils.listToString;

@Builder
@Getter
@AllArgsConstructor
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
    private String contents;

    public Book toBook() {
        return Book.builder()
                .authors(listToString(this.authors))
                .translators(listToString(this.translators))
                .title(this.title)
                .publisher(this.publisher)
                .price(this.price)
                .thumbnail(this.thumbnail)
                .releaseDate(this.datetime != null ? this.datetime.toLocalDateTime() : null)
                .isbn(this.isbn)
                .contents(this.contents)
                .build();
    }

    public Boolean validCheck() {
        if (this.isbn.length() < 1) {
            return false;
        }
        return true;
    }
}
