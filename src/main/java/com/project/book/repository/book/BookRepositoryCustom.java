package com.project.book.repository.book;

import com.project.book.domain.Book;

import java.util.List;
import java.util.Map;

public interface BookRepositoryCustom {

    Book findByIsbn(String isbn);

    List<Map> getDetailBook(Long id);
}
