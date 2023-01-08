package com.project.book.book.repository.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookSortType;
import com.project.book.book.domain.QBook;
import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepositoryCustom;
import com.project.book.member.domain.MemberType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Expression;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.project.book.book.domain.BookSortType.*;
import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QRegisterBook.registerBook;
import static com.project.book.common.utils.QuerydslUtils.*;
import static com.project.book.member.domain.QMember.member;


@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Book findByIsbn(String isbn) {

        Book book = queryFactory.selectFrom(QBook.book).where(QBook.book.isbn.eq(isbn)).fetchOne();

        return book;
    }

//    @Override
//    public Map<String, Object> getDetailBook(Book reqBook) {
//
//        DetailBookResponseDto detailBookResponseDto = queryFactory.select(new QDetailBookResponseDto(
//                book.title, book.authors, book.translator,
//                book.publisher, book.thumbnail, book.isbn,
//                book.releaseDate, book.price, findAvgStar(reqBook)
//        )).from(book).where(book.eq(reqBook)).fetchOne();
//
//
//        detailBookResponseDto.updateAvgStar();
//
//
//        List<Tuple> fetch1 = queryFactory.select(
//                        registerBook.readBookTime,
//                        registerBook.readBookTime.count(),
//                        registerBook.recommendBookTime,
//                        registerBook.recommendBookTime.count()
//                ).from(registerBook)
//                .where(registerBook.book.eq(reqBook))
//                .groupBy(registerBook.readBookTime
//                        , registerBook.recommendBookTime)
//                .fetch();
//
//
//        Map readTimeMap = new HashMap();
//        Map recommendTimeMap = new HashMap();
//        for (Tuple tuple : fetch1) {
//            readTimeMap.put(tuple.get(0, BookTime.class), tuple.get(1, int.class));
//            recommendTimeMap.put(tuple.get(2, BookTime.class), tuple.get(3, int.class));
//        }
//
//        Map<String, Object> resultMap = new HashMap();
//        resultMap.put("detailData",  detailBookResponseDto);
//        resultMap.put("readTimeMap", readTimeMap);
//        resultMap.put("recommendTimeMap", recommendTimeMap);
//
//        return resultMap;
//    }


//    private Expression<Double> findAvgStar(Book request) {
//        return ExpressionUtils.as(JPAExpressions.select(registerBook.star.avg())
//                        .from(registerBook)
//                        .where(registerBook.book.eq(request))
//                , "avgStar");
//    }
//
//    private Expression<Long> findBookTimeCount(Book request) {
//        return ExpressionUtils.as(
//                JPAExpressions
//                        .select(registerBook.readBookTime.count())
//                        .from(registerBook)
//                        .where(registerBook.book.eq(request))
//                        .groupBy(registerBook.readBookTime), "findCount");
//    }

    @Override
    public List<AllBookResponseDto> getAllBooks(AllBookFilterDto condition, Pageable pageRequest) {

        return queryFactory.select(new QAllBookResponseDto(
                        book.title, book.authors, book.publisher, book.thumbnail,
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
                        book.title, book.authors, book.publisher, book.thumbnail,
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

