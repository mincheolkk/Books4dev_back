package com.project.book.book.repository.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.QBook;
import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QRegisterBook.registerBook;
import static com.project.book.common.utils.QuerydslUtils.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Book findByIsbn(String isbn) {

        Book book = queryFactory.selectFrom(QBook.book).where(QBook.book.isbn.eq(isbn)).fetchOne();

        return book;
    }

    @Override
    public List<AllBookResponseDto> getAllBooks(AllBookFilterDto condition, Pageable pageRequest) {

        return queryFactory.select(new QAllBookResponseDto(
                        book.id, book.title, book.authors, book.publisher, book.thumbnail,
                        book.isbn, book.price, book.star.avgStar, book.count.registerCount,
                        book.recommendTime, book.count.wishCount))
                .from(book)
                .join(book.registerBooks, registerBook)
                .where(
                        enumEqCheck(registerBook.member.type, condition.getMemberType()),
                        enumEqCheck(registerBook.recommendBookTime, condition.getRecommendType())
                )
                .orderBy(
                        getBookSortType(condition.getSortType()),
                        getBookSortByTime(condition.getRecommendType())
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .distinct()
                .fetch();
    }

    public List<AllBookResponseDto> findByTitle(String title) {
        return queryFactory.select(new QAllBookResponseDto(
                        book.id, book.title, book.authors, book.publisher, book.thumbnail,
                        book.isbn, book.price, book.star.avgStar, book.count.registerCount,
                        book.recommendTime, book.count.wishCount))
                .from(book)
                .join(book.registerBooks, registerBook)
                .where(
                        book.title.contains(title)
                )
                .distinct()
                .fetch();
    }
}

