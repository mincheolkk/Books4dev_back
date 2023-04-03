package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@DynamicUpdate
@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class ReadBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    public BookTime readBookTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    public BookTime recommendBookTime;

    @NotNull
    private double star;

    @Builder
    public ReadBook(Book book, Member member, BookTime readBookTime, BookTime recommendBookTime, Integer star) {
        this.book = book;
        this.member = member;
        this.readBookTime = readBookTime;
        this.recommendBookTime = recommendBookTime;
        this.star = (double) star;
    }

    public void updateReadBook(final Integer star, final BookTime recommendBookTime) {
        this.star = (double) star;
        this.recommendBookTime = recommendBookTime;
    }
}
