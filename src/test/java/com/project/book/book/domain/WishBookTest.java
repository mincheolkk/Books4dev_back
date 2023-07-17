package com.project.book.book.domain;

import com.project.book.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WishBookTest {

    @DisplayName("관심있는 책을 생성한다.")
    @Test
    void createWishBook() {
        // given
        Book book = Book.builder().build();
        Member member = Member.builder().build();

        // when
        WishBook wishBook = WishBook.builder()
                .book(book)
                .member(member)
                .build();

        // then
        assertThat(wishBook.getBook()).isEqualTo(book);
        assertThat(wishBook.getMember()).isEqualTo(member);
    }
}