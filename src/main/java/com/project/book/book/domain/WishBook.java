package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.book.common.domain.BaseEntity;
import com.project.book.member.domain.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@NoArgsConstructor
@Entity
public class WishBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String isbn;
    private String title;
    private String thumbnail;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public WishBook(String isbn, String title, String thumbnail, Member member) {
        this.isbn = isbn;
        this.title = title;
        this.thumbnail = thumbnail;
        this.member = member;
    }
}
