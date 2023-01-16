package com.project.book.book.dto.request;

import com.project.book.book.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Objects;

import static com.project.book.common.utils.ServiceUtils.listToString;

@Getter
@NoArgsConstructor
public class RegisterBySearchDto {

    private BookDataDto item;
    private BookReviewDto review;

    public Book toBook(BookDataDto request) {
        return Book.builder()
                .authors(listToString(request.getAuthors()))
                .translators(listToString(request.getTranslators()))
                .title(request.getTitle())
                .publisher(request.getPublisher())
                .price(request.getPrice())
                .thumbnail(request.getThumbnail())
                .releaseDate(request.getDatetime().toLocalDateTime())
                .isbn(request.getIsbn())
                .contents(request.getContents())
                .build();
    }
}