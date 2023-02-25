package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.book.book.dto.request.BookReviewDto;
import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class ReadBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    public BookTime readBookTime;

    @Enumerated(EnumType.STRING)
    public BookTime recommendBookTime;

    private double star;

    @Builder
    public ReadBook(Long id, Book book, Member member, BookTime readBookTime, BookTime recommendBookTime, Integer star) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.readBookTime = readBookTime;
        this.recommendBookTime = recommendBookTime;
        this.star = (double) star;
    }

    public static ReadBook toReadBook(final Member member, final Book book, final BookReviewDto request) {
        return ReadBook.builder()
                .book(book)
                .readBookTime(request.getReadTime())
                .recommendBookTime(request.getRecommendTime())
                .star(request.getStar())
                .member(member)
                .build();
    }

    public void updateReadBook(final Integer star, final BookTime recommendBookTime) {
        this.star = (double) star;
        this.recommendBookTime = recommendBookTime;
    }
}
