package com.project.book.repository.service;

import com.project.book.domain.Book;
import com.project.book.domain.QBook;
import com.project.book.repository.BookRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.project.book.domain.QBook.*;


@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Book findByIsbn(String isbn) {

        Book book = queryFactory.selectFrom(QBook.book).where(QBook.book.isbn.eq(isbn)).fetchOne();

        return book;
    }
}
