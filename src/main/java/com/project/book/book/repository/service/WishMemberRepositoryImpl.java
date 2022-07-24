package com.project.book.book.repository.service;

import com.project.book.book.domain.QWishMember;
import com.project.book.book.domain.WishBook;
import com.project.book.book.domain.WishMember;
import com.project.book.book.repository.WishMemberRepositoryCustom;
import com.project.book.member.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WishMemberRepositoryImpl implements WishMemberRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    public boolean findByWishBook(WishBook wishBook, Member member){
        return queryFactory.selectFrom(QWishMember.wishMember)
                .where(
                        QWishMember.wishMember.member.eq(member),
                        QWishMember.wishMember.wishBook.eq(wishBook)
                ).fetchOne() != null;

    }
}
