package com.project.book.book.repository.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.book.book.domain.Book;
import com.project.book.book.domain.QBook;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepositoryCustom;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Expression;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QRegisterBook.registerBook;
import static com.project.book.book.repository.QuerydslUtils.enumEqCheck;
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

    @Override
    public Map<String, Object> getDetailBook(Book reqBook) throws JsonProcessingException {

        DetailBookResponseDto detailBookResponseDto = queryFactory.select(new QDetailBookResponseDto(
                book.title, book.authors, book.translator,
                book.publisher, book.thumbnail, book.isbn,
                book.releaseDate, book.price, findAvgStar(reqBook)
        )).from(book).where(book.eq(reqBook)).fetchOne();


        detailBookResponseDto.updateAvgStar();


        List<Tuple> fetch1 = queryFactory.select(
                        registerBook.readBookTime,
                        registerBook.readBookTime.count(),
                        registerBook.recommendBookTime,
                        registerBook.recommendBookTime.count()
                ).from(registerBook)
                .where(registerBook.book.eq(reqBook))
                .groupBy(registerBook.readBookTime
                        , registerBook.recommendBookTime)
                .fetch();


        Map readTimeMap = new HashMap();
        Map recommendTimeMap = new HashMap();
        for (Tuple tuple : fetch1) {
            readTimeMap.put(tuple.get(0, BookTime.class), tuple.get(1, int.class));
            recommendTimeMap.put(tuple.get(2, BookTime.class), tuple.get(3, int.class));
        }

        Map<String, Object> resultMap = new HashMap();
        resultMap.put("detailData",  detailBookResponseDto);
        resultMap.put("readTimeMap", readTimeMap);
        resultMap.put("recommendTimeMap", recommendTimeMap);

        return resultMap;
    }

    private Expression<Double> findAvgStar(Book request) {
        return ExpressionUtils.as(JPAExpressions.select(registerBook.star.avg())
                        .from(registerBook)
                        .where(registerBook.book.eq(request))
                , "avgStar");
    }

    private Expression<Long> findBookTimeCount(Book request) {
        return ExpressionUtils.as(
                JPAExpressions
                        .select(registerBook.readBookTime.count())
                        .from(registerBook)
                        .where(registerBook.book.eq(request))
                        .groupBy(registerBook.readBookTime), "findCount");
    }

    @Override
    public Map<String, Map> testListCount(Book reqBook) throws JsonProcessingException {

        List<Tuple> fetch1 = queryFactory.select(
                        registerBook.readBookTime,
                        registerBook.readBookTime.count(),
                        registerBook.recommendBookTime,
                        registerBook.recommendBookTime.count()
                ).from(registerBook)
                .where(registerBook.book.eq(reqBook))
                .groupBy(registerBook.readBookTime
                        , registerBook.recommendBookTime)
                .fetch();

        Map readTimeMap = new HashMap();
        Map recommendTimeMap = new HashMap();
        for (Tuple tuple : fetch1) {
            readTimeMap.put(tuple.get(0, BookTime.class), tuple.get(1, int.class));
            recommendTimeMap.put(tuple.get(2, BookTime.class), tuple.get(3, int.class));
        }

        Map<String, Map> resultMap = new HashMap();
        resultMap.put("readTimeMap", readTimeMap);
        resultMap.put("recommendTimeMap", recommendTimeMap);

        return resultMap;
    }

    @Override
    public List<Tuple> maybetuple(Book reqBook) {
        return queryFactory.select(
                        registerBook.readBookTime,
                        registerBook.readBookTime.count(),
                        registerBook.recommendBookTime,
                        registerBook.recommendBookTime.count()
                ).from(registerBook)
                .where(registerBook.book.eq(reqBook))
                .groupBy(registerBook.readBookTime
                        , registerBook.recommendBookTime)
                .fetch();
    }

    @Override
    public void howToSolve(Book reqBook, MemberType type) {
        //
        List<Tuple> tupleList = queryFactory.select(
                        book.title,
                        book.authors,
                        registerBook.readBookTime,
                        registerBook.readBookTime.count(),
                        registerBook.recommendBookTime,
                        registerBook.recommendBookTime.count(),
                        registerBook.star.avg()
                ).from(registerBook)
                .join(registerBook.member, member)
                .join(registerBook.book, book)
                .where(
//                        registerBook.readBookTime.eq(BookTime.before)
//                        registerBook.book.eq(reqBook)
//                        enumEqCheck(member.type, type)
//                        ,registerBook.member.eq(member)
                ).groupBy(
                        book.id,
                        member.type,
                        registerBook.readBookTime,
                        registerBook.recommendBookTime
                ,registerBook.id)
                .orderBy(registerBook.star.avg().asc())
                .fetch();

        System.out.println("tupleList = " + tupleList);
        System.out.println("tupleList.get(0).get(4,int.class) = " + tupleList.get(0).get(4,int.class));

//        System.out.println("jpaQuery.fetchOne().get(0,long.class) = " + jpaQuery.fetchOne().get(0,long.class));
//        System.out.println("jpaQuery.fetchOne().get(0,long.class) = " + jpaQuery.fetchOne().get(1,double.class));


    }
}

