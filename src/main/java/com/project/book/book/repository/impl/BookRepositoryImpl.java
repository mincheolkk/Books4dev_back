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
                        book.id, book.bookInfo.title, book.bookInfo.authors, book.bookInfo.thumbnail,
                        book.bookInfo.isbn, book.star.avgStar,
                        book.count.readCount, book.count.wishCount, book.count.commentCount, book.recommendTime))
                .from(book)
                .join(book.readBooks, readBook)
                .join(readBook.member, member)
                .where(
                        enumEqCheck(readBook.member.type, condition.getMemberType()),
                        enumEqCheck(readBook.recommendBookTime, condition.getRecommendType())
                )
                .orderBy(
                        getRecommendTime(condition.getRecommendType()),
                        getBookSortType(condition.getSortType()),
                        book.id.asc()
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .distinct()
                .fetch();
    }

    @Override
    public Long countAllBooks(final AllBookFilterDto condition) {
        return queryFactory
                .selectFrom(book)
                .join(book.readBooks, readBook)
                .join(readBook.member, member)
                .where(
                        enumEqCheck(readBook.member.type, condition.getMemberType()),
                        enumEqCheck(readBook.recommendBookTime, condition.getRecommendType())
                )
                .distinct().fetchCount();
    }

    @Override
    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return queryFactory.select(new QAllBookResponseDto(
                        book.id, book.bookInfo.title, book.bookInfo.authors, book.bookInfo.thumbnail,
                        book.bookInfo.isbn, book.star.avgStar,
                        book.count.readCount, book.count.wishCount, book.count.commentCount, book.recommendTime))
                .from(book)
                .where(
                        bookSearchBooleanBuilder(text)
                )
                .orderBy(
                        book.count.readCount.desc(),
                        book.star.avgStar.desc()
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
                    stringContainsCheck(book.bookInfo.title, text)
                            .or(stringContainsCheck(book.bookInfo.contents, text))
                            .or(stringContainsCheck(book.bookInfo.authors, text))
                            .or(stringContainsCheck(book.bookInfo.translators, text))
                            .or(stringContainsCheck(book.bookInfo.publisher, text))
            );
        }

        return conditionBuilder;
    }
}