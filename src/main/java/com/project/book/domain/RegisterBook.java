package com.project.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class RegisterBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Positive
    private Integer readTime;

    @Positive
    private Integer recommendTime;

    @Positive
    private Integer star;

    @Builder
    public RegisterBook(Long id, Book book, Member member, Integer readTime, Integer recommendTime, Integer star) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.readTime = readTime;
        this.recommendTime = recommendTime;
        this.star = star;
    }
}
