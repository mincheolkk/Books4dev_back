package com.project.book.book.domain;

import com.project.book.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReadBookTest {

    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        member = Member.builder().build();
        book = Book.builder().build();
    }


    @DisplayName("주어진 정보를 바탕으로 읽은 책을 생성한다.")
    @Test
    void createReadBook() {
        // given
        BookTime readBookTime = BookTime.before;
        BookTime recommendBookTime = BookTime.after;
        Integer star = 4;

        // when
        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(readBookTime)
                .recommendBookTime(recommendBookTime)
                .star(star)
                .build();

        // then
        assertAll(
                () -> {
                    assertThat(readBook.getBook()).isEqualTo(book);
                    assertThat(readBook.getMember()).isEqualTo(member);
                    assertThat(readBook.getReadBookTime()).isEqualTo(readBookTime);
                    assertThat(readBook.getRecommendBookTime()).isEqualTo(recommendBookTime);
                    assertThat(readBook.getStar()).isEqualTo((double) star);
                }
        );
    }

    @DisplayName("주어진 정보를 바탕으로 읽은 책을 수정한다.")
    @Test
    void updateReadBook() {
        // given
        BookTime readBookTime = BookTime.before;
        BookTime recommendBookTime = BookTime.after;
        Integer star = 4;

        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(readBookTime)
                .recommendBookTime(recommendBookTime)
                .star(star)
                .build();

        // when
        Integer newStar = 5;
        BookTime newRecommendBookTime = BookTime.threeYear;
        readBook.updateReadBook(newStar, newRecommendBookTime);

        // then
        assertAll(
                () -> {
                    assertThat(readBook.getBook()).isEqualTo(book);
                    assertThat(readBook.getMember()).isEqualTo(member);
                    assertThat(readBook.getStar()).isNotEqualTo((double) star);
                    assertThat(readBook.getRecommendBookTime()).isNotEqualTo(recommendBookTime);
                    assertThat(readBook.getStar()).isEqualTo((double) newStar);
                    assertThat(readBook.getRecommendBookTime()).isEqualTo(newRecommendBookTime);
                }
        );
    }
}