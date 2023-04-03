package com.project.book.book.repository;

import com.project.book.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

    @Query("SELECT b FROM Book b WHERE b.bookInfo.isbn = :isbn")
    Book findByIsbn(final String isbn);
}

