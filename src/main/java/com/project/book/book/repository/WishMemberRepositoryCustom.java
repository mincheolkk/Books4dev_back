package com.project.book.book.repository;

import com.project.book.book.domain.WishBook;
import com.project.book.book.domain.WishMember;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.member.domain.Member;

import java.util.List;

public interface WishMemberRepositoryCustom {

    boolean findByWishBook(final WishBook wishBook, final Member member);

    List<WishBookResponseDto> getAllWishBook(final Member member);

    long findWishBookCount(final String isbn);

}
