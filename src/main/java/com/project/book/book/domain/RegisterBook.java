package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class RegisterBook extends BaseEntity {

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

    private Integer star;

    @Builder
    public RegisterBook(Long id, Book book, Member member, BookTime readBookTime, BookTime recommendBookTime, Integer star) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.readBookTime = readBookTime;
        this.recommendBookTime = recommendBookTime;
        this.star = star;
    }

    public void updateRegisterBook(Integer star) {
        this.star = star;
    }
}
