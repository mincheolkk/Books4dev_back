package com.project.book.book.repository;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.RecommendCountDto;
import com.project.book.member.domain.Member;

import java.util.List;

public interface RegisterBookRepositoryCustom {

    List<ReadBookResponseDto> testReadbook(Member member, BookTime readTime);

    RegisterBook findByMemberAndBookAndReadTime(Member member, Book savedBook, BookTime readTime);


    List<RecommendCountDto> findRecommendCount(Book book);

    Double findAvgStar(Book book);

}
