package com.project.book.book.repository;

import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.member.domain.Member;

import java.util.List;

public interface RegisterBookRepositoryCustom {

    List<ReadBookResponseDto> testReadbook(Member member, BookTime readTime);
}
