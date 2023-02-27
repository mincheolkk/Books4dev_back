package com.project.book.common.utils;

import com.project.book.book.domain.BookSortType;
import com.project.book.book.domain.BookTime;
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

@NoArgsConstructor
public class QuerydslUtils {

    public static BooleanExpression enumEqCheck(EnumPath select, Enum condition ) {
        return condition != null ? select.eq(condition) : null;
    }

    public static BooleanExpression stringContainsCheck(StringPath select, String condition ) {
        return hasText(condition) ? select.contains(condition) : select.isNull();
    }
}
