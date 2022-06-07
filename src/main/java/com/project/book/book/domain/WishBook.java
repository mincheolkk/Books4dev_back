package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;

import javax.persistence.*;

@Entity
public class WishBook extends BaseEntity {

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
}
