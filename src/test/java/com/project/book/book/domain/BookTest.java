package com.project.book.book.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BookTest {

    @DisplayName("초기값을 확인한다")
    @Test
    void initValue() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // then
        assertAll(
                () -> {
                    assertThat(book.getStar().getAvgStar()).isEqualTo(0);
                    assertThat(book.getCount().getWishCount()).isEqualTo(0);
                    assertThat(book.getCount().getCommentCount()).isEqualTo(0);
                    assertThat(book.getCount().getReadCount()).isEqualTo(0);
                }
        );
    }

    @DisplayName("책에 포함된 카운트들은 음수일 수 없다. 음수인 경우엔 0을 반환한다.")
    @Test
    void countsAreNotMinus() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateRecommendTime(BookTime.before, -100);
        book.calculateRecommendTime(BookTime.after, -100);
        book.calculateRecommendTime(BookTime.threeYear, -100);
        book.calculateRecommendTime(BookTime.sixYear, -100);
        book.calculateRecommendTime(BookTime.anyTime, -100);
        book.calculateWishCount(-100);
        book.calculateReadCount(-100);
        book.calculateCommentCount(-100);

        // then
        assertThat(book.getRecommendTime().getBeforeCount()).isEqualTo(0);
        assertThat(book.getRecommendTime().getAfterCount()).isEqualTo(0);
        assertThat(book.getRecommendTime().getThreeYearCount()).isEqualTo(0);
        assertThat(book.getRecommendTime().getSixYearCount()).isEqualTo(0);
        assertThat(book.getRecommendTime().getAnyTimeCount()).isEqualTo(0);
        assertThat(book.getCount().getWishCount()).isEqualTo(0);
        assertThat(book.getCount().getReadCount()).isEqualTo(0);
        assertThat(book.getCount().getCommentCount()).isEqualTo(0);
    }

    @DisplayName("추천 시기 카운트를 계산한다")
    @Test
    void recommendCount() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateRecommendTime(BookTime.before, 1);
        book.calculateRecommendTime(BookTime.before, 1);
        book.calculateRecommendTime(BookTime.before, -1);

        // then
        assertThat(book.getRecommendTime().getBeforeCount()).isEqualTo(1);
    }

    @DisplayName("읽은 사람 카운트를 계산한다")
    @Test
    void readCount() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateReadCount(1);
        book.calculateReadCount(1);
        book.calculateReadCount(-1);

        // then
        assertThat(book.getCount().getReadCount()).isEqualTo(1);
    }

    @DisplayName("관심 있는 사람 카운트를 계산한다")
    @Test
    void wishCount() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateWishCount(1);

        // then
        assertThat(book.getCount().getWishCount()).isNotEqualTo(0);
        assertThat(book.getCount().getWishCount()).isEqualTo(1);
    }

    @DisplayName("평점을 계산한다")
    @Test
    void star() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateAvgStar(2);

        // then
        assertThat(book.getStar().getAvgStar()).isEqualTo(2);
    }

    @DisplayName("평점은 음수일 수 없다. 음수인 경우엔 0을 반환한다.")
    @Test
    void starIsNotMinus() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateAvgStar(-2);

        // then
        assertThat(book.getStar().getAvgStar()).isEqualTo(0);
    }

    @DisplayName("평점은 5를 넘을 수 없다. 넘는 경우엔 5를 반환한다.")
    @Test
    void underFive() {
        // given
        Book book = Book.builder()
                .id(1L)
                .build();

        // when
        book.calculateAvgStar(10);

        // then
        assertThat(book.getStar().getAvgStar()).isEqualTo(5);
    }
}
