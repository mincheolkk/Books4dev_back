package com.project.book.book.repository;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.RecommendCountDto;
import com.project.book.member.domain.Member;

import java.util.List;
import java.util.Map;

public interface RegisterBookRepositoryCustom {

    Map<BookTime, List<ReadBookResponseDto>> getMyReadBook(final Member member);

    RegisterBook findByMemberAndBookAndReadTime(final Member member, final Book savedBook, final BookTime readTime);

    Double findAvgStar(final Book book);

}
