package com.project.book.book.repository.impl;

import com.project.book.book.domain.*;
import com.project.book.book.dto.response.QReadBookResponseDto;
import com.project.book.book.dto.response.QRecommendCountDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.RecommendCountDto;
import com.project.book.book.repository.RegisterBookRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QRegisterBook.*;
import static com.project.book.common.utils.QuerydslUtils.enumEqCheck;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class RegisterBookRepositoryImpl implements RegisterBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<BookTime, List<ReadBookResponseDto>> getMyReadBook(final Member member) {
        List<Tuple> tupleList = queryFactory.select(
                        registerBook.readBookTime,  new QReadBookResponseDto(
                                book.title, book.isbn, book.thumbnail, registerBook.star
                        ))
                .from(registerBook)
                .innerJoin(registerBook.book, book)
                .where(registerBook.member.eq(member))
                .fetch();

        return tupleList.stream().collect(
                Collectors.groupingBy(
                        t -> t.get(registerBook.readBookTime),
                        Collectors.mapping(
                                t -> t.get(
                                        new QReadBookResponseDto(
                                                book.title, book.isbn, book.thumbnail, registerBook.star)),
                                Collectors.toList()
                        )
                )
        );
    }

    @Override
    public RegisterBook findByMemberAndBookAndReadTime(final Member member, final Book savedBook, final BookTime readTime) {
        RegisterBook findedBook = queryFactory.selectFrom(registerBook)
                .where(
                        registerBook.member.eq(member),
                        registerBook.book.eq(savedBook),
                        enumEqCheck(registerBook.readBookTime, readTime)
                ).fetchOne();

        return findedBook;
    }


    @Override
    public Double findAvgStar(final Book book) {
        return queryFactory.select(
                        registerBook.star.avg())
                .from(registerBook)
                .where(registerBook.book.eq(book))
                .fetchOne();
        }
}
