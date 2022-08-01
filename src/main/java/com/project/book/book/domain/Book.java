package com.project.book.book.domain;

import com.project.book.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_isbn")
    private String isbn;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_publisher")
    private String publisher;

    @Column(name = "book_dateTime")
    private LocalDateTime releaseDate;

    @Column(name = "book_price")
    private Long price;

    @Column(name = "book_thumbnail")
    private String thumbnail;

    @Column(name = "book_authors")
    private String authors;

    @Column(name = "book_translator")
    private String translator;

    @Embedded
    private StarAndCount starAndCount;

    @Embedded
    private RecommendTime recommendTime;

    @OneToMany(mappedBy = "book")
    private List<RegisterBook> registerBooks = new ArrayList<>();

    public void plusRegisterCount() {
        starAndCount.plusRegisterCount();
    }

    public void plusWishCount() {
        starAndCount.plusWishCount();
    }

    public void calculateAvgStar(Integer star) {
        starAndCount.calculateAvgStar(star);
    }

    public void plusRecommendTime(BookTime time) {
        System.out.println("time = " + time);
        recommendTime.plusRecommendTime(time);
        System.out.println("222");
    }


    @Builder
    public Book(String isbn, String title, String publisher, LocalDateTime releaseDate, Long price, String thumbnail, String authors, String translator) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.price = price;
        this.thumbnail = thumbnail;
        this.authors = authors;
        this.translator = translator;
        this.starAndCount = StarAndCount.init();
        this.recommendTime = RecommendTime.init();
    }
}