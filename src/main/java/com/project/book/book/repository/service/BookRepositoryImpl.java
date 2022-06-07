package com.project.book.book.repository.service;

import com.project.book.book.domain.Book;
import com.project.book.domain.QBook;
import com.project.book.book.repository.BookRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map> getDetailBook(Long id) {

        List<Map> resultList = new ArrayList<>();

        List<Book> fetch = queryFactory.selectFrom(book).where(book.id.eq(id)).fetch();

        Map result = new HashMap();
        result.put("book", fetch);

        resultList.add(result);
        return resultList;
    }
}
