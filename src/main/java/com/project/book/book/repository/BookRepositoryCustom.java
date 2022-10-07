package com.project.book.book.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.response.AllBookResponseDto;
import com.project.book.member.domain.MemberType;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BookRepositoryCustom {

    Book findByIsbn(String isbn);

//    Map<String, Object> getDetailBook(Book book) throws JsonProcessingException;

    List<AllBookResponseDto> getAllBooks(AllBookFilterDto condition, Pageable pageable);

    List<AllBookResponseDto> findByTitle(String title);

}
