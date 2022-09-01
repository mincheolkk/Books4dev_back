package com.project.book.common.utils;

import com.project.book.book.domain.BookSortType;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.QBook;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.project.book.book.domain.BookSortType.*;
import static com.project.book.book.domain.BookTime.*;
import static com.project.book.book.domain.QBook.*;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuerydslUtils {

    public static BooleanExpression enumEqCheck(EnumPath select, Enum condition ) {
        if(condition!=null) {
            return select.eq(condition);
        } else {
            return null;
        }
    }

    public static BooleanExpression stringEqCheck(StringPath select, String condition ) {
        return hasText(condition) ? select.eq(condition) : null;
    }

    public static OrderSpecifier<?> getBookSortType(BookSortType sortType) {
        if (sortType == null) {
            return book.starAndCount.registerCount.desc();
        } else if (sortType == COUNT) {
            return book.starAndCount.registerCount.desc();
        } else if (sortType == STAR) {
            return book.starAndCount.avgStar.desc();
        } else if (sortType == WISH) {
            return book.starAndCount.wishCount.desc();
        }
        return book.starAndCount.registerCount.desc();
    }

    public static OrderSpecifier<?> getBookSortByTime(BookTime sortType) {

        if (sortType == null) {
            return book.starAndCount.avgStar.desc();
        } else if (sortType == before) {
            return book.recommendTime.beforeCount.desc();
        } else if (sortType == after) {
            return book.recommendTime.afterCount.desc();
        } else if (sortType == twoYear) {
            return book.recommendTime.twoYearCount.desc();
        } else if (sortType == fiveYear) {
            return book.recommendTime.fiveYearCount.desc();
        } else if (sortType == tenYear) {
            return book.recommendTime.tenYearCount.desc();
        }
        return book.starAndCount.avgStar.desc();
    }
}
