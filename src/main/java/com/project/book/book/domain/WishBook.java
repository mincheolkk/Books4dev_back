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

    @Builder
    public WishBook(String isbn, String title, String thumbnail) {
        this.isbn = isbn;
        this.title = title;
        this.thumbnail = thumbnail;
    }
}