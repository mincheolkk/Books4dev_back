package com.project.book.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class MemberBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Positive
    private Integer readTime;

    @Positive
    private Integer recommendTime;

    @Positive
    private Integer star;

    private String comment;
}
