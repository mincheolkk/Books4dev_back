package com.project.book.book.repository.impl;

import com.project.book.book.domain.BookSortType;
import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.project.book.book.domain.BookSortType.fromBookSortType;
import static com.project.book.book.domain.BookTime.*;
import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QReadBook.readBook;
import static com.project.book.common.utils.QuerydslUtils.*;
import static com.project.book.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AllBookResponseDto> getAllBooks(final AllBookFilterDto condition, final Pageable pageRequest) {
        return queryFactory.select(new QAllBookResponseDto(
                        book.id, book.title, book.authors, book.thumbnail,
                        book.isbn, book.star.avgStar,
                        book.count.readCount, book.count.wishCount, book.count.commentCount,book.recommendTime))
                .from(book)
                .join(book.readBooks, readBook)
                .join(readBook.member, member)
                .where(
                        enumEqCheck(readBook.member.type, condition.getMemberType()),
                        enumEqCheck(readBook.recommendBookTime, condition.getRecommendType())
                )
                .orderBy(
                        getBookSortType(condition.getSortType()),
                        getRecommendTime(condition.getRecommendType())
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .distinct()
                .fetch();
    }

    @Override
    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return queryFactory.select(new QAllBookResponseDto(
                        book.id, book.title, book.authors, book.thumbnail,
                        book.isbn, book.star.avgStar,
                        book.count.readCount, book.count.wishCount, book.count.commentCount, book.recommendTime))
                .from(book)
                .where(
                        bookSearchBooleanBuilder(text)
                )
                .distinct()
                .fetch();
    }

    private OrderSpecifier<?> getBookSortType(BookSortType sortType) {
        return fromBookSortType(sortType).getOrderSpecifier();
    }

    private OrderSpecifier<?> getRecommendTime(BookTime recommendTime) {
        return fromBookTime(recommendTime).getOrderSpecifier();
    }

    private BooleanBuilder bookSearchBooleanBuilder(final String text) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (StringUtils.hasText(text)) {
                conditionBuilder.and(
                    stringContainsCheck(book.title, text)
                            .or(stringContainsCheck(book.contents, text))
                            .or(stringContainsCheck(book.authors, text))
                            .or(stringContainsCheck(book.translators, text))
                            .or(stringContainsCheck(book.publisher, text))
            );
        }

        return conditionBuilder;
    }
}