package com.project.book.book.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

public class QuerydslUtils {


    public static BooleanExpression enumEqCheck(EnumPath select, Enum condition ) {
        if(condition!=null) {
            return select.eq(condition);
        } else {
            return null;
        }
    }
}
