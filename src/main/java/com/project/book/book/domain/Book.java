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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Embedded
    private BookInfo bookInfo;

    @Embedded
    private Star star;

    @Embedded
    private Count count;

    @Embedded
    private BookTimeCount recommendTime;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<ReadBook> readBooks = new ArrayList<>();

    public void calculateReadCount(final long count) {
        this.count.calculateReadCount((int) count);
    }

    public void calculateWishCount(int count) {
        this.count.calculateWishCount(count);
    }

    public void calculateCommentCount(int count) {
        this.count.calculateCommentCount(count);
    }

    public void calculateAvgStar(final double star) {
        this.star.calculateAvgStar(star);
    }

    public void calculateRecommendTime(final BookTime recommendTime, final int count) {
        this.recommendTime.calculateBookTimeCount(recommendTime, count);
    }

    @Builder
    public Book(Long id, BookInfo bookInfo) {
        this.id = id;
        this.bookInfo = bookInfo;
        this.star = Star.init();
        this.count = Count.init();
        this.recommendTime = BookTimeCount.init();
    }
}