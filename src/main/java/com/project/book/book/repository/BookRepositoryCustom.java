package com.project.book.book.repository;

import com.project.book.book.domain.Book;

import java.util.List;
import java.util.Map;

public interface BookRepositoryCustom {

    Book findByIsbn(String isbn);

    List<Map> getDetailBook(Long id);
}
