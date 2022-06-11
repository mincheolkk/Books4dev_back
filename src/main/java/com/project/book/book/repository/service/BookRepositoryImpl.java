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

        List<Map> resultList = new ArrayList<>();

        DetailBookResponseDto detailBookResponseDto = queryFactory.select(new QDetailBookResponseDto(
                book.title, book.authors, book.translator,
                book.publisher, book.thumbnail, book.isbn,
                book.releaseDate, book.price, findAvgStar(reqBook)
        )).from(book).where(book.eq(reqBook)).fetchOne();


        detailBookResponseDto.updateAvgStar();
        System.out.println("detailBookResponseDto.getAvgStar() = " + detailBookResponseDto.getAvgStar());
        Map result = new HashMap();

        result.put("책 데이터", detailBookResponseDto);

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


        resultList.add(result);
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

        System.out.println("resultMap = " + resultMap);
//        ObjectMapper objectMapper = new ObjectMapper();
//        ResDto dto = new ResDto("readTimeMap", readTimeMap);
//        ResDto dto2 = new ResDto("recommendTimeMap", recommendTimeMap);
//        String s = objectMapper.writeValueAsString(dto);
//        System.out.println("s = " + s);
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

}

