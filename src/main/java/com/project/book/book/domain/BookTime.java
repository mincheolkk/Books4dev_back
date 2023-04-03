package com.project.book.book.domain;

import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;

import static com.project.book.book.domain.QBook.book;

public enum BookTime {

    before("before",book.recommendTime.beforeCount.desc()),
    after("after",  book.recommendTime.afterCount.desc()),
    threeYear("threeYear", book.recommendTime.threeYearCount.desc()),
    sixYear("sixYear", book.recommendTime.sixYearCount.desc()),
    anyTime("anyTime",  book.recommendTime.anyTimeCount.desc()),
    All("All", book.count.readCount.desc());

    private final String name;
    private final OrderSpecifier<?> orderSpecifier;

    BookTime(String name, OrderSpecifier<?> orderSpecifier) {
        this.name = name;
        this.orderSpecifier = orderSpecifier;
    }

    public static BookTime fromBookTime(BookTime condition) {
        return Arrays.stream(BookTime.values())
                .filter(bookTime -> bookTime.name.equals(condition != null ? condition.name : All))
                .findAny()
                .orElse(All);
    }

    public OrderSpecifier<?> getOrderSpecifier() {
        return orderSpecifier;
    }
}
