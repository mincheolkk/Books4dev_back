package com.project.book.book.domain;

import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;

import static com.project.book.book.domain.QBook.book;

public enum BookSortType {

    COUNT("COUNT", book.count.readCount.desc()),
    STAR("STAR", book.star.avgStar.desc()),
    WISH("WISH", book.count.wishCount.desc());

    private final String name;
    private final OrderSpecifier<?> orderSpecifier;

    BookSortType(String name, OrderSpecifier<?> orderSpecifier) {
        this.name = name;
        this.orderSpecifier = orderSpecifier;
    }

    public static BookSortType fromBookSortType(BookSortType condition) {
        return Arrays.stream(BookSortType.values())
                .filter(bookSortType -> bookSortType.name.equals(condition != null ? condition.name : COUNT))
                .findAny()
                .orElse(COUNT);
    }


    public OrderSpecifier<?> getOrderSpecifier() {
        return orderSpecifier;
    }
}
