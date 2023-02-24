package com.project.book.book.repository;

import com.project.book.book.domain.Book;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.member.domain.Member;

import java.util.List;

public interface WishMemberRepositoryCustom {

    boolean existByBookAndMember(final Book book, final Member member);

    List<WishBookResponseDto> getAllWishBook(final Member member);
}
