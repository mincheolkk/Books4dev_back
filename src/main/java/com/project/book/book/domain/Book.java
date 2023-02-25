package com.project.book.book.domain;

import com.project.book.common.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

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

    @Embedded
    private Star star;

    @Embedded
    private Count count;

    @Embedded
    private RecommendTime recommendTime;

    @OneToMany(mappedBy = "book")
    private List<ReadBook> readBooks = new ArrayList<>();

    public void plusReadCount(final long count) {
        this.count.readCount((int) count);
    }

    // 괌심 수 +1
    public void plusWishCount() {
        this.count.plusWishCount();
    }

    public void calculateAvgStar(final double star) {
        this.star.calculateAvgStar(star);
    }

    public void calculateRecommendTime(final BookTime recommendTime, final int count) {
        this.recommendTime.calculateRecommendTime(recommendTime, count);
    }

    @Builder
    public Book(String isbn, String title, String publisher, LocalDateTime releaseDate, Long price, String thumbnail, String authors, String translators, String contents,Star star, Count count, RecommendTime recommendTime) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.price = price;
        this.thumbnail = thumbnail;
        this.authors = authors;
        this.translators = translators;
        this.contents = contents;
        this.star = Star.init();
        this.count = Count.init();
        this.recommendTime = RecommendTime.init();
    }
}