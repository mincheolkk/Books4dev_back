package com.project.book.book.domain;

import com.querydsl.core.types.OrderSpecifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.book.book.domain.BookTime.*;
import static com.project.book.book.domain.QBook.book;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookTimeTest {

    @DisplayName("null이 아니면 해당 BookTime을 반환한다.")
    @Test
    void fromBookTime() {
        // given
        BookTime condition = after;

        // when
        BookTime result = BookTime.fromBookTime(condition);

        // then
        assertThat(result).isEqualTo(condition);
    }

    @DisplayName("null일 경우 BookTime을 All로 반환한다")
    @Test
    void fromBookTimeWhenNull() {
        // given
        BookTime condition = null;

        // when
        BookTime result = BookTime.fromBookTime(condition);

        // then
        assertThat(result).isEqualTo(All);
    }

    @DisplayName("각 BookTime에 맞는 OrderSpecifier를 반환한다.")
    @Test
    void getOrderSpecifier() {

        // when
        assertAll(
                () -> {
                    assertThat(before.getOrderSpecifier()).isEqualTo(book.recommendTime.beforeCount.desc());
                    assertThat(after.getOrderSpecifier()).isEqualTo(book.recommendTime.afterCount.desc());
                    assertThat(threeYear.getOrderSpecifier()).isEqualTo(book.recommendTime.threeYearCount.desc());
                    assertThat(sixYear.getOrderSpecifier()).isEqualTo(book.recommendTime.sixYearCount.desc());
                    assertThat(anyTime.getOrderSpecifier()).isEqualTo(book.recommendTime.anyTimeCount.desc());
                    assertThat(All.getOrderSpecifier()).isEqualTo(book.count.readCount.desc());
                }
        );
    }

    @DisplayName("두 개의 메서드를 같이 사용한다.")
    @Test
    void withMethods() {
        // given
        BookTime condition = BookTime.after;

        // when
        BookTime result = BookTime.fromBookTime(condition);
        OrderSpecifier<?> resultOrderSpecifier = result.getOrderSpecifier();

        // then
        assertThat(resultOrderSpecifier).isEqualTo(book.recommendTime.afterCount.desc());
    }

    @DisplayName("null일 때는 읽은순의 내림차순으로 반환한다.")
    @Test
    void withMethodsWhenNull() {
        // given
        BookTime condition = null;

        // when
        BookTime result = BookTime.fromBookTime(condition);
        OrderSpecifier<?> resultOrderSpecifier = result.getOrderSpecifier();

        // then
        assertThat(resultOrderSpecifier).isEqualTo(book.count.readCount.desc());
    }


}