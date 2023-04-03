package com.project.book.common.utils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;

import static org.springframework.util.StringUtils.hasText;

public class QuerydslUtils {

    public static BooleanExpression enumEqCheck(EnumPath select, Enum condition ) {
        return condition != null ? select.eq(condition) : null;
    }

    public static BooleanExpression stringContainsCheck(StringPath select, String condition ) {
        return hasText(condition) ? select.contains(condition) : select.isNull();
    }
}
