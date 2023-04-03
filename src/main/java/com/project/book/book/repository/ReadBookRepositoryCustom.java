package com.project.book.book.repository;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.BookTimeCount;
import com.project.book.book.domain.ReadBook;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.member.domain.Member;

import java.util.List;
import java.util.Map;

public interface ReadBookRepositoryCustom {

    Map<BookTime, List<ReadBookResponseDto>> getMemberReadBook(final Member member);

    ReadBook findByMemberAndBookAndReadTime(final Member member, final Book savedBook, final BookTime readTime);

    Double findAvgStar(final Book book);

    BookTimeCount getReadTime(Book book);
}
