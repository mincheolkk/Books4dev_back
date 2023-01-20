package com.project.book.book.repository.service;

import com.project.book.book.domain.*;
import com.project.book.book.dto.response.QReadBookResponseDto;
import com.project.book.book.dto.response.QRecommendCountDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.RecommendCountDto;
import com.project.book.book.repository.RegisterBookRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    public Map<BookTime, List<ReadBookResponseDto>> getMyReadBook(Member member) {
        return queryFactory
                .from(registerBook)
                .where(
                        registerBook.member.eq(member))
                .transform(
                        groupBy(registerBook.readBookTime).as(list(
                                                    new QReadBookResponseDto(
                                                        book.title, book.isbn, book.thumbnail,
                                                        registerBook.star
                                                    ))));
    }

    public RegisterBook findByMemberAndBookAndReadTime(Member member, Book savedBook, BookTime readTime) {
        RegisterBook findedBook = queryFactory.selectFrom(registerBook)
                .where(
                        registerBook.member.eq(member),
                        registerBook.book.eq(savedBook),
                        enumEqCheck(registerBook.readBookTime, readTime)
                ).fetchOne();

        return findedBook;
    }

    @Override
    public List<RecommendCountDto> findRecommendCount(Book book) {
        return queryFactory.select(new QRecommendCountDto(
                        registerBook.recommendBookTime,
                        registerBook.recommendBookTime.count()
                )).from(registerBook)
                .where(registerBook.book.eq(book))
                .groupBy(registerBook.recommendBookTime)
                .fetch();
    }

    @Override
    public Double findAvgStar(Book book) {
        return queryFactory.select(
                        registerBook.star.avg())
                .from(registerBook)
                .where(registerBook.book.eq(book))
                .fetchOne();
        }
}
