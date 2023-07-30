package com.project.book.book.dto.response;

import com.project.book.book.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class DetailBookResponseDto {

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

    private BookTimeCount readTime;

    private BookTimeCount recommendTime;

    private List<KeywordScoreResponseDto> topKeywordList;

    @Builder
    public DetailBookResponseDto(Long id, String isbn, String title, String publisher, LocalDateTime releaseDate, Long price, String thumbnail, String authors, String translators, String contents, Star star, Count count, BookTimeCount readTime, BookTimeCount recommendTime, List<KeywordScoreResponseDto> topKeywordList) {
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
        this.readTime = readTime;
        this.recommendTime = recommendTime;
        this.topKeywordList = topKeywordList;
    }

    public static DetailBookResponseDto from(final Book book, final List<KeywordScoreResponseDto> topKeywordList) {
        return DetailBookResponseDto.builder()
                .id(book.getId())
                .title(book.getBookInfo().getTitle())
                .contents(book.getBookInfo().getContents())
                .authors(book.getBookInfo().getAuthors())
                .translators(book.getBookInfo().getTranslators())
                .thumbnail(book.getBookInfo().getThumbnail())
                .publisher(book.getBookInfo().getPublisher())
                .price(book.getBookInfo().getPrice())
                .isbn(book.getBookInfo().getIsbn())
                .count(book.getCount())
//                .readTime(readTime)
                .recommendTime(book.getRecommendTime())
                .releaseDate(book.getBookInfo().getReleaseDate())
                .star(book.getStar())
                .topKeywordList(topKeywordList)
                .build();
    }
}
