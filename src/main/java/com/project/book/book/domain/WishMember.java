package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.book.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private WishBook wishBook;

    @Builder
    public WishMember(Member member, WishBook wishBook) {
        this.member = member;
        this.wishBook = wishBook;
    }
}
