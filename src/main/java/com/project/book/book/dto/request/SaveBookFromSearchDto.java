package com.project.book.book.dto.request;

import com.project.book.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.project.book.common.utils.ServiceUtils.listToString;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveBookFromSearchDto {

    private BookDataDto item;
    private BookReviewDto review;

    public Book toBook(final BookDataDto request) {
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