package com.project.book.book.repository.impl;

import com.project.book.book.domain.WishBook;
import com.project.book.book.dto.response.QWishBookResponseDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.WishMemberRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.book.book.domain.QWishBook.*;
import static com.project.book.book.domain.QWishMember.*;

@Repository
@RequiredArgsConstructor
public class WishMemberRepositoryImpl implements WishMemberRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    public boolean findByWishBook(final WishBook wishBook, final Member member){
        return queryFactory.selectFrom(wishMember)
                .where(
                        wishMember.member.eq(member),
                        wishMember.wishBook.eq(wishBook)
                ).fetchOne() != null;
    }

    public List<WishBookResponseDto> getAllWishBook(final Member member) {
        List<WishBookResponseDto> fetch = queryFactory.select(new QWishBookResponseDto(
                        wishBook.title, wishBook.isbn, wishBook.thumbnail
                ))
                .from(wishMember)
                .join(wishMember.wishBook, wishBook)
                .where(wishMember.member.eq(member))
                .fetch();
        return fetch;
    }

    @Override
    public long findWishBookCount(final String isbn) {
        long fetchCount = queryFactory.selectFrom(wishMember)
                .join(wishMember.wishBook, wishBook)
                .where(wishBook.isbn.eq(isbn))
                .fetchCount();

        return fetchCount;
    }
}