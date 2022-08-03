package com.project.book.book.repository.service;

import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.QBook;
import com.project.book.book.domain.QRegisterBook;
import com.project.book.book.dto.response.QReadBookResponseDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.repository.RegisterBookRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.book.book.domain.BookTime.tenYear;
import static com.project.book.book.domain.BookTime.twoYear;
import static com.project.book.book.domain.QBook.*;
import static com.project.book.book.domain.QRegisterBook.*;
import static com.project.book.common.utils.QuerydslUtils.enumEqCheck;

@Repository
@RequiredArgsConstructor
public class RegisterBookRepositoryImpl implements RegisterBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<ReadBookResponseDto> testReadbook(Member member, BookTime readTime) {
        return queryFactory.select(new QReadBookResponseDto(
                        book.title, book.isbn, book.thumbnail,
                        registerBook.star
                )).from(registerBook)
                .join(registerBook.book, book)
                .where(
                        registerBook.member.eq(member),
                        enumEqCheck(registerBook.recommendBookTime, readTime)
                )
                .fetch();

    }
}
