package com.project.book.book.repository.impl;

import com.project.book.book.domain.*;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.ReadBookRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QReadBook.readBook;
import static com.project.book.common.utils.QuerydslUtils.enumEqCheck;

@Repository
@RequiredArgsConstructor
public class ReadBookRepositoryImpl implements ReadBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<BookTime, List<ReadBookResponseDto>> getMyReadBook(final Member member) {
        List<Tuple> tupleList = queryFactory.select(
                        readBook.readBookTime,  new QReadBookResponseDto(
                                book.id, book.title, book.isbn, book.thumbnail, readBook.star
                        ))
                .from(readBook)
                .innerJoin(readBook.book, book)
                .where(readBook.member.eq(member))
                .fetch();

        return tupleList.stream().collect(
                Collectors.groupingBy(
                        t -> t.get(readBook.readBookTime),
                        Collectors.mapping(
                                t -> t.get(
                                        new QReadBookResponseDto(
                                                book.id, book.title, book.isbn, book.thumbnail, readBook.star)),
                                Collectors.toList()
                        )
                )
        );
    }

    @Override
    public ReadBook findByMemberAndBookAndReadTime(final Member member, final Book savedBook, final BookTime readTime) {
        ReadBook findedBook = queryFactory.selectFrom(readBook)
                .where(
                        readBook.member.eq(member),
                        readBook.book.eq(savedBook),
                        enumEqCheck(readBook.readBookTime, readTime)
                ).fetchOne();

        return findedBook;
    }


    @Override
    public Double findAvgStar(final Book book) {
        return queryFactory.select(
                        readBook.star.avg())
                .from(readBook)
                .where(readBook.book.eq(book))
                .fetchOne();
        }

    @Override
    public BookTimeCount getReadTime(Book book) {
        List<Tuple> tupleList = queryFactory.select(readBook.readBookTime, readBook.readBookTime.count())
                .from(readBook)
                .where(readBook.book.eq(book))
                .groupBy(readBook.readBookTime)
                .fetch();

        BookTimeCount readTime = BookTimeCount.init();

        tupleList.stream().forEach(
                t -> readTime.calculateBookTimeCount(
                        t.get(readBook.readBookTime),
                        t.get(readBook.readBookTime.count()).intValue())
                );

        return readTime;
    }
}
