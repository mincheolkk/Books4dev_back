package com.project.book.book.repository.impl;

import com.project.book.book.domain.Book;
import com.project.book.book.dto.response.QWishBookResponseDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.WishMemberRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.book.book.domain.QBook.book;
import static com.project.book.book.domain.QWishMember.*;

@Repository
@RequiredArgsConstructor
public class WishMemberRepositoryImpl implements WishMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existByBookAndMember(final Book savedBook, final Member member){
        return queryFactory.selectFrom(wishMember)
                .where(
                        wishMember.member.eq(member),
                        wishMember.book.eq(savedBook)
                ).fetchOne() != null;
    }

    @Override
    public List<WishBookResponseDto> getAllWishBook(final Member member) {
        List<WishBookResponseDto> fetch = queryFactory.select(new QWishBookResponseDto(
                        book.id, book.title, book.isbn, book.thumbnail
                ))
                .from(wishMember)
                .join(wishMember.book, book)
                .where(wishMember.member.eq(member))
                .fetch();
        return fetch;
    }
}