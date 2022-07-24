package com.project.book.book.repository;

import com.project.book.book.domain.WishBook;
import com.project.book.member.domain.Member;

public interface WishMemberRepositoryCustom {

    boolean findByWishBook(WishBook wishBook, Member member);

}
