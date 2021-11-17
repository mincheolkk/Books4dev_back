package com.project.book.repository;

import com.project.book.domain.Book;

public interface BookRepositoryCustom {

    Book findByIsbn(String isbn);
}
