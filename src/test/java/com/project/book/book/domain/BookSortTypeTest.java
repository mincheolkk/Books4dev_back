package com.project.book.book.domain;

import com.querydsl.core.types.OrderSpecifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.book.book.domain.BookSortType.*;
import static com.project.book.book.domain.QBook.book;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookSortTypeTest {

    @DisplayName("null이 아니면 해당 BookSortType을 반환한다.")
    @Test
    void fromBookSortType() {
        // given
        BookSortType condition = STAR;

        // when
        BookSortType result = BookSortType.fromBookSortType(condition);

        // then
        assertThat(result).isEqualTo(condition);
    }

    @DisplayName("null일 경우 BookSortType을 COUNT로 반환한다")
    @Test
    void fromBookSortTypeWhenNull() {
        // given
        BookSortType condition = null;

        // when
        BookSortType result = BookSortType.fromBookSortType(condition);

        // then
        assertThat(result).isEqualTo(COUNT);
    }

    @DisplayName("각 BookSortType에 맞는 OrderSpecifier를 반환한다.")
    @Test
    void getOrderSpecifier() {

        // when
        assertAll(
                () -> {
                    assertThat(COUNT.getOrderSpecifier()).isEqualTo(book.count.readCount.desc());
                    assertThat(STAR.getOrderSpecifier()).isEqualTo(book.star.avgStar.desc());
                    assertThat(WISH.getOrderSpecifier()).isEqualTo(book.count.wishCount.desc());
                    assertThat(COMMENT.getOrderSpecifier()).isEqualTo(book.count.commentCount.desc());
                }
        );
    }

    @DisplayName("두 개의 메서드를 같이 사용한다.")
    @Test
    void withMethods() {
        // given
        BookSortType condition = STAR;

        // when
        BookSortType result = BookSortType.fromBookSortType(condition);
        OrderSpecifier<?> resultOrderSpecifier = result.getOrderSpecifier();

        // then
        assertThat(resultOrderSpecifier).isEqualTo(book.star.avgStar.desc());
    }

    @DisplayName("null일 때는 읽은순의 내림차순으로 반환한다.")
    @Test
    void withMethodsWhenNull() {
        // given
        BookSortType condition = null;

        // when
        BookSortType result = BookSortType.fromBookSortType(condition);
        OrderSpecifier<?> resultOrderSpecifier = result.getOrderSpecifier();

        // then
        assertThat(resultOrderSpecifier).isEqualTo(book.count.readCount.desc());
    }

}