package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.domain.RegisterBook;
import com.project.book.dto.book.BookRequsetDto;
import com.project.book.dto.book.CreateBookRequestDto;
import com.project.book.repository.BookRepository;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class BookServiceTest {

    @Autowired BookService bookService;
    @Autowired BookRepository repository;

    @Test
    public void 책등록() throws Exception {

        //given

        List<String> testAuthors = new ArrayList<>();
        testAuthors.add("test");
        testAuthors.add("test2");
        System.out.println("testAuthors = " + testAuthors);


        BookRequsetDto requsetDto = BookRequsetDto.builder()
                .datetime(ZonedDateTime.now())
                .authors(testAuthors)
                .translator(null)
                .isbn("123131313")
                .title("test")
                .price(0)
                .thumbnail("url:test")
                .publisher("tes")
                .readTime(5)
                .recommendTime(5)
                .star(5)
                .build();

        Book orRegisterBook = bookService.createOrRegisterBook(requsetDto);

        System.out.println("orRegisterBook = " + orRegisterBook);
//        System.out.println("orRegisterBook.getIsbn() = " + orRegisterBook.getIsbn());
        System.out.println("orRegisterBook.getAuthors() = " + orRegisterBook.getAuthors());
        System.out.println("orRegisterBook.getTranslator() = " + orRegisterBook.getTranslator());
        System.out.println("orRegisterBook.getDateTime() = " + orRegisterBook.getDateTime());
        //when

        //then
    }




}