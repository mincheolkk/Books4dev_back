package com.project.book.book.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.member.domain.MemberType;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Map;

public interface BookRepositoryCustom {

    Book findByIsbn(String isbn);

    Map<String, Object> getDetailBook(Book book) throws JsonProcessingException;


}
