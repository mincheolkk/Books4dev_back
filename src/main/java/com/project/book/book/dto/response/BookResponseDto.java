package com.project.book.book.dto.response;

import com.project.book.book.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class BookResponseDto {

    private Long id;

    private String isbn;

    private String title;

    private String publisher;

    private LocalDateTime releaseDate;

    private Long price;

    private String thumbnail;

    private String authors;

    private String translators;

    private String contents;

    private Star star;

    private Count count;

    private RecommendTime recommendTime;

    private List<KeywordScoreResponseDto> topKeywordList;

    @Builder
    public BookResponseDto(Long id, String isbn, String title, String publisher, LocalDateTime releaseDate, Long price, String thumbnail, String authors, String translators, String contents, Star star, Count count, RecommendTime recommendTime, List<KeywordScoreResponseDto> topKeywordList) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.price = price;
        this.thumbnail = thumbnail;
        this.authors = authors;
        this.translators = translators;
        this.contents = contents;
        this.star = star;
        this.count = count;
        this.recommendTime = recommendTime;
        this.topKeywordList = topKeywordList;
    }

    public static BookResponseDto from(final Book book, List<KeywordScoreResponseDto> topKeywordList) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .contents(book.getContents())
                .authors(book.getAuthors())
                .translators(book.getTranslators())
                .thumbnail(book.getThumbnail())
                .publisher(book.getPublisher())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .count(book.getCount())
                .recommendTime(book.getRecommendTime())
                .releaseDate(book.getReleaseDate())
                .star(book.getStar())
                .topKeywordList(topKeywordList)
                .build();
    }
}
